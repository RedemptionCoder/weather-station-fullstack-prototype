import cruxarina.weatherlink.*;
import cruxarina.weatherlink.Observations.OUTPUT_STRING;

public class Main {
	
	public static void main(String[] args) {
		
		boolean lFullOutput = getArgFlag(args, "f");
		boolean lDebugOutput = getArgFlag(args, "d");
		
		String lConfigFilePath = getArgValue(args, "c");
		ConfigFile lConfig = new ConfigFile("/etc/wsssd/wsssd.conf");
		
		// Check if user wants help using the wsssd service/daemon
		if (getArgFlag(args, "h")) {
			
			// Show command line help information
			showHelp();
		} else {
		
			// Check if there is a custom config file from command line parameters
			if (lConfigFilePath.length() > 0) {lConfig = new ConfigFile(lConfigFilePath);}
			
			// Check if the config file is accessible
			if (lConfig.isAccessible()) {
			
				// Set up the connection to the weather station interface
				StationConnection lConnection = new StationConnection(lConfig);
				
				// Set debugging if enabled
				if (lDebugOutput) {lConnection.LogFile.setDebugOutput(true);}
								
				// Set up the Site Server
				WeatherStationSiteServer lWSSS = new WeatherStationSiteServer(lConnection);
				
				// Set debugging if enabled
				if (lDebugOutput) {lWSSS.setDebugOutput(true);}
				
				
				// Get the data from the Site Server
				while (lWSSS.refreshLiveObservations()) {
					
					// Get the observations for summary display
					Observations lObservation = lWSSS.getData();
					
					// Suppress normal output if debugging is enabled
					if (!lDebugOutput) {
					
						// Check if user wants a full observations report
						if (lFullOutput) {
							
							// print out the full observations report
							System.out.println(lObservation.toString());
						} else {
							
							// Only print out a summary of essential observations
							System.out.println(lObservation.toString(OUTPUT_STRING.ESSENTIAL));
							System.out.println("");
						}
						
					}
					
					// If the data is being forwarded to a web app server, display the forwarding latency
					if (lConnection.getHttpHost().length() > 0) {
						System.out.println("");
						System.out.println("Time taken to forward observations to host: " + lConnection.getHttpRequestResponseTime() + " seconds");
						System.out.println("");
					}
					
				}
				 
				// Could not connect to the weather station interface - stop the service and report errors
				System.out.println("Failed to refresh live data. " + lWSSS.lastError());
				
				// flush out any logs that have not been saved yet
				flushLogs(lWSSS, lConnection);
				
						
			} else {
				
				// Report that the configuration file is inaccessible
				System.out.println("Could not access the configuration file: " + lConfigFilePath);
				
			}
			
		}
	}
	
	/**
	 * Looks for the value of the supplied parameter.
	 * @param args			The command line arguments
	 * @param parameter		The parameter to look for
	 * @return				The value of the parameter
	 */ 
	private static String getArgValue(String [] args, String parameter) {
		
		String lPar = "-" + parameter;
		
		// Go through each elements of the argument array
		for (int i = 0; i < args.length; i++) {
			
			// If the parameter exists, then return true
			if (args[i].equals(lPar)) {
				
				if ((i+1)!=args.length) {
					if (args[i+1].startsWith("-")) {
						// there is no value for the parameter, the next parameter has been found
						// return empty string
						return "";
					} else {
						// return the value
						return args[i+1];
					}
				} else {
					// end of array reached. No value for parameter
					// return empty string
					return "";
				}
			}
		}
				
		return "";
		
	}
	
	/**
	 * Looks for the presence of a parameter in the command line arguments.
	 * If the parameter exists, is indicates a true for that parameter.
	 * @param args			The command line arguments
	 * @param parameter		The parameter to look for
	 * @return				True if the parameter has been found
	 */
	private static boolean getArgFlag(String[] args, String parameter) {
		
		// Attach the prefix character
		String lPar = "-" + parameter;
		
		// Go through each elements of the argument array
		for (String lParEl : args) {
			
			// If the parameter exists, then return true
			if (lParEl.equals(lPar)) {
				return true;
			}
		}
		
		// Parameter does not exist, return false
		return false;
	}
	
	
	// Private attributes
	
	// Outputs all weather data to console
	boolean cFullConsoleOutput = false;
	
	/**
	 * Displays the command help information.
	 */
	private static void showHelp() {
		
		System.out.println("");
		System.out.println("wsssd [OPTIONS...]");
		System.out.println("");
		System.out.println("Where options include:");
		System.out.println("");
		System.out.println(String.format("  -h\t\t\tShow this help"));
		System.out.println(String.format("  -c <config path>\tThe file system path to the configuration file"));
		System.out.println(String.format("  -f\t\t\tShow a fully formatted weather output with all observation information "));
		System.out.println(String.format("  -d\t\t\tPrint out debugging information. Normal output suppressed."));
		System.out.println("");
		
	}
	
	/**
	 * Tries to save any log file entries in memory to the log file. If this fails, log messages will be
	 * sent to standard output. 
	 * @param Wsss					The site server
	 * @param stationConnection		The Station Connection
	 */
	private static void flushLogs(WeatherStationSiteServer Wsss, StationConnection stationConnection) {
		
		if (!Wsss.isLogFileAccessible()) {
			// try and save the log again
			if (!Wsss.saveLogToFile()) {
				// Still couldn't save, flush to standard output
				Wsss.printLogs();
			}
		}
		
		if (!stationConnection.LogFile.isLogFileAccessible()) {
			// try and save the log again
			if (!stationConnection.LogFile.saveLogToFile()) {
				// Still couldn't save, flush to standard output
				stationConnection.LogFile.printLogs();
			}
		}
		
		
	}
	
}
