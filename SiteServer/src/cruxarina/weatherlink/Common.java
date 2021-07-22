/**
 * Contains a lot of common functionality used for Cruxarina projects
 * Included functionalities
 * <ul>
 * <li>Error Logging</li>
 * <li>Message Logging</li>
 * </ul>
 * 
 * @author Anthony Gibbons
 */


package cruxarina.weatherlink;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.activation.*;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.*;

import java.time.LocalDateTime;
import java.time.format.*;
import java.io.*;



public class Common {
	
	// CONSTRUCTORS
	
	Common() {
		
		cLog = new ArrayList<String>();
		cTimerLog = new ArrayList<String>();
		cLastError = "";
		cLastMessage = "";
		cSaveLogToMemory = false;
		cErrorTag = "ERROR: ";
		cLogFilePath = "";
		cLogFileAccessible = false;
		cDebugOutput = false;
		cStartTime = new GregorianCalendar();
		cStopTime = new GregorianCalendar();
		cTimeTakenInMilliseconds = 0;
		cSendingAccountUsernameEmail = "";
		cFromDisplayName = "";
		cPassword = "";
		cDestinationEmail = "";
		cSmtpServerFQDN = "smtp.gmail.com";	
		cSmtpPort = "587";
		cUseAuth = "true";
		cUseStartTls = "true";
		
		// Record the initial memory usage
		cFreeMemoryAtStart = Runtime.getRuntime().freeMemory();
		cMemoryUsedAtStart = Runtime.getRuntime().totalMemory();
		
	}
	
	// PUBLIC METHODS
	
	/**
	 * Sets the path to the configuration file
	 * @param Path	A path to the configuration file
	 */
	public void setConfigFilePath(String Path) {
		this.cConfig.setFilename(Path);
	}
	
	/**
	 * Gets the path to the config file
	 * @return	The path to the config file
	 */
	public String getConfigFilePath() {
		return this.cConfig.getFilename();
	}
	
	/**
	 * Gets the value of a property from the config file. Will return a zero length
	 * string if the config file was not found, accessible, or setting not found. 
	 * @param PropertyName	The name of the property
	 * @return				The value of the property
	 */
	public String getConfig(String PropertyName) {
		return this.cConfig.getConfig(PropertyName);
	}
	
	/**
	 * Sets the string that will be put in front of errors in the log file
	 * @param errorTag	The error tag to use.
	 */
	public void setErrorTag(String errorTag) {
		cErrorTag = errorTag;
	}
	
	/**
	 * Gets the string that will be put in front of errors in the log file
	 * @return	A String containing the error tag
	 */
	public String getErrorTag() {
		return cErrorTag;
	}
	
	/**
	 * Sets the path to the log file
	 * @param path
	 */
	public void setLogFilePath(String path) {
		cLogFilePath = path;
	}
	
	/**
	 * Gets the path to the log file
	 * @return	The path to the log file
	 */
	public String getLogFilePath() {
		return cLogFilePath;
	}
	
	/**
	 * Gets whether debug output is enabled
	 * @return 	True if enabled, false if not
	 */
	public boolean isDebugOutput() {
		return cDebugOutput;
	}

	/**
	 * Sets whether debug output should be shown
	 * @param debugOutput True if enabled, false if not
	 */
	public void setDebugOutput(boolean debugOutput) {
		cDebugOutput = debugOutput;
	}
	
	/**
	 * Gets whether the log file is accessible or not
	 * @return	True if log file is accessible, false if not.
	 */
	public boolean isLogFileAccessible() {
		return cLogFileAccessible;
	}
	
	/**
	 * Sets the whether to save the log to memory as well as log file
	 * @param value	True to save log to memory as well, or false if not (default)
	 */
	public void setSaveLogToMemory(boolean value) {
		cSaveLogToMemory = value;
	}
	
	/**
	 * Gets whether the log will be saved to memory or not
	 * @return	true If saving 
	 */
	public boolean isSaveLogToMemory() {
		return cSaveLogToMemory;
	}
	
	/**
	 * The gets username usually in the form of the full email address to use 
	 * for the sending email account.
	 * @return The sending account username/email address
	 */
	public String getSendingAccountUsernameEmail() {
		return cSendingAccountUsernameEmail;
	}

	/**
	 *  Sets the username usually in the form of the full email address to use 
	 * for the sending email account.
	 * @param sendingAccountUsernameEmail The The sending account username/email address to set
	 */
	public void setSendingAccountUsernameEmail(String sendingAccountUsernameEmail) {
		cSendingAccountUsernameEmail = sendingAccountUsernameEmail;
	}

	/**
	 * Gets the actual person name that will come up as the sender
	 * @return The sender displayName
	 */
	public String getFromDisplayName() {
		return cFromDisplayName;
	}

	/**
	 * Sets the actual person name that will come up as the sender
	 * @param fromDisplayName The sender displayName
	 */
	public void setFromDisplayName(String fromDisplayName) {
		cFromDisplayName = fromDisplayName;
	}

	/**
	 * Gets the plain text password being used to authenticate with the smtp server
	 * @return The password
	 */
	public String getPassword() {
		return cPassword;
	}

	/**
	 * Sets the plain text password being used to authenticate with the smtp server
	 * @param The password
	 */
	public void setPassword(String password) {
		cPassword = password;
	}

	/**
	 * Gets the destination email address to which to send the alerts to
	 * @return The destination email address 
	 */
	public String getDestinationEmail() {
		return cDestinationEmail;
	}

	/**
	 * Sets the destination email address to which to send the alerts to
	 * @param destinationEmail The destination email address
	 */
	public void setDestinationEmail(String destinationEmail) {
		cDestinationEmail = destinationEmail;
	}

	/**
	 * Gets the fully qualified domain name of the smtp server 
	 * that will send the email
	 * @return The FQDN of the sending smtp server
	 */
	public String getSmtpServerFQDN() {
		return cSmtpServerFQDN;
	}

	/**
	 * Sets the fully qualified domain name of the smtp server 
	 * that will send the email
	 * @param smtpServerFQDN The FQDN of the sending smtp server
	 */
	public void setSmtpServerFQDN(String smtpServerFQDN) {
		cSmtpServerFQDN = smtpServerFQDN;
	}

	/**
	 * Gets the port being used for the smtp server
	 * @return The smtp port
	 */
	public String getSmtpPort() {
		return cSmtpPort;
	}

	/**
	 * Gets the port being used for the smtp server
	 * @param The smtp port
	 */
	public void setSmtpPort(String smtpPort) {
		cSmtpPort = smtpPort;
	}

	/**
	 * Gets whether to use authentication with the smtp server
	 * @return A string either true or false
	 */
	public String getUseAuth() {
		return cUseAuth;
	}

	/**
	 * Sets whether to use authentication with the smtp server
	 * @param useAuth A string either true or false
	 */
	public void setUseAuth(String useAuth) {
		cUseAuth = useAuth.toLowerCase();
	}

	/**
	 * Gets whether to use STARTTLS or not
	 * @return A string either true or false
	 */
	public String getUseStartTls() {
		return cUseStartTls;
	}

	/**
	 * Sets whether to use STARTTLS or not
	 * @param A string either true or false
	 */
	public void setUseStartTls(String useStartTls) {
		cUseStartTls = useStartTls.toLowerCase();
	}

	/**
	 * Adds an error message to the class' error log
	 * @param Message			A string containing an error message
	 */
	public void logError(String Message) {
		
		// Write error to log with error tag
		logErrorOrMessage(cErrorTag + Message);
			
		// Set the last error to the most recent one
		cLastError = Message;
		
	}
	
	/**
	 * Adds an error message to the class' error log and sends an email
	 * alert with the supplied subject.
	 * @param Message			A string containing an error message
	 * @param Subject			The subject to include for the email
	 */
	public void logError(String Message, String Subject) {
		
		// Write error to log with error tag
		logErrorOrMessage(cErrorTag + Message);
		
		// Send the message as an email alert
		sendMailAlert(Message, Subject);
			
		// Set the last error to the most recent one
		cLastError = Message;
		
	}
	
	/**
	 * Adds a message to the class' message log
	 * @param Message			A string containing an message
	 */
	public void logMessage(String Message) {
		
		// Write message to log
		logErrorOrMessage(Message);
		
		// Set the last message to the most recent one
		cLastMessage = Message;
		
	}
	
	/**
	 * Adds a message to the class' message log and sends an email
	 * alert with the supplied subject.
	 * @param Message			A string containing an message
	 * @param Subject			The subject to include for the email
	 */
	public void logMessage(String Message, String Subject) {
		
		// Write message to log
		logErrorOrMessage(Message);
		
		// Send the message as an email alert
		sendMailAlert(Message, Subject);
		
		// Set the last message to the most recent one
		cLastMessage = Message;
		
	}
	
	/**
	 * Send an email with the contents of the log in memory.
	 * @param Message	The message to send
	 * @param Subject	The subject
	 */
	public void sendLogAsEmail(String Message, String Subject) {
		
		// Print out the message
		String lMessageBody = String.format("\n%s\n\n", Message);
		
		// Print log message contents
		lMessageBody += String.format("The following events were logged:\n\n");
		
		// Print the contents of the log
		for (String logItem : cLog) {
			lMessageBody += String.format("\t%s\n", logItem);
		}
		
		// Send the email
		sendMailAlert(lMessageBody, Subject);
		
	}
	
	/**
	 * Send an email with the contents of the log in memory.
	 * @param Message	The message to send
	 * @param Subject	The subject
	 */
	public void sendTimerLogAsEmail(String Message, String Subject) {
		
		// Print out the message
		String lMessageBody = String.format("\n%s\n\n", Message);
		
		// Print log message contents
		lMessageBody += String.format("The following events were logged:\n\n");
		
		// Print the contents of the log
		for (String logItem : cTimerLog) {
			lMessageBody += String.format("\t%s\n", logItem);
		}
		
		// Send the email
		sendMailAlert(lMessageBody, Subject);
		
	}
	
	/**
	 * Gets the last error that was logged
	 * @return The last error message
	 */
	public String lastError() {
		return cLastError;
	}
	
	/**
	 * Returns the last message that was logged
	 * @return The last message
	 */
	public String lastMessage() {
		return cLastMessage;
	}
	
	/**
	 * Attempts to save all messages and errors to the log file.
	 * @return	True if successful, false if not
	 */
	public boolean saveLogToFile() {
		
		
		if (cLog.size() > 0) {
			
			try {
			
				// Create or access the file
				File lLogFile = new File(cLogFilePath);
				
				// Create the file writer, and create the file if it doesn't exist
				FileWriter lLogWriter = new FileWriter(lLogFile);
				
				for (String lMessage : cLog) {
					
						// Write the message to the file
						lLogWriter.write(lMessage + "\n");
						
				}
				
				// Close the file
				lLogWriter.close();
					
			} catch (Exception e) {

				logError(e.getMessage());
				
				// File system issues, return false
				return false;
					
			}
				
		}
			
		// Message successfully written to the log file or there were no messages
		return true;
		
	}
	
	/**
	 * Sends to contents of the logs in memory to standard output
	 */
	public void printLogs() {
		
		if (cLog.size() > 0) {
				
			for (String lMessage : cLog) {
				
				// Write the message to the file
				System.out.println(lMessage);
					
			}
				
		}
		
	}
	
	/**
	 * Clears the log in memory. Note that this will not clear the log file.
	 * If the log file is not accessible, or the log messages can't be written
	 * to the log file, the log will not clear.
	 * @return	true If the log file is accessible and the log was cleared successfully
	 * 			or false if the log file was not accessible. 
	 */
	public boolean clearLogs() {
		
		if (cLogFileAccessible) {
			cLog.clear();
		} else {
			return false; 
		}
		
		return true;
	}
	
	/**
	 * Resets the timer and sets the total time taken to 
	 * zero milliseconds and clears the timer log
	 */
	public void resetTimer() {
		cTimeTakenInMilliseconds = 0;
		cStartTime = new GregorianCalendar();
		// Clear the timer log as well
		cTimerLog = new ArrayList<String>();
	}
	
	/**
	 * Records the start time for the timer
	 */
	public void startTimer() {
		
		// Record the start time
		cStartTime = new GregorianCalendar();
		
	}
	
	/**
	 * Start the timer and log a message in the logs
	 * @param Message	The message to include for this start of timer
	 */
	public void startTimer(String Message) {
		
		// Log the message if in debug mode
		if (cDebugOutput) {logMessage(Message);}
		
		// Write the message to log in memory
		cTimerLog.add(getTimeStamp() + " " + Message);
		
		// Record the start time
		startTimer();
		
	}
	
	/**
	 * Records the stop time for the timer
	 */
	public void stopTimer() {
		
		// Record the stop time
		cStopTime = new GregorianCalendar();
		
		// Add the total time 
		cTimeTakenInMilliseconds += getTimeTaken();
		
	}
	
	/**
	 * Stop the timer and log a message in the logs
	 * @param Message	The message to include for this stop of timer
	 */
	public void stopTimer(String Message) {
		
		// Log the message if in debug mode
		if (cDebugOutput) {logMessage(Message);}
		
		// Write the message to log in memory
		cTimerLog.add(getTimeStamp() + " " + Message);
		
		// Record the stop time
		stopTimer();
		
	}
	
	/**
	 * Returns the time taken as a string suitable for direct inclusion in a log
	 * @return	String	Time taken as string
	 */
	public String getTimeTakenAsString() {
		
		double lTimeTaken = (cStopTime.getTimeInMillis() - cStartTime.getTimeInMillis());
		
		if (lTimeTaken > 1000) {
			
			// Convert to seconds
			lTimeTaken = lTimeTaken / 1000;
			if (lTimeTaken > 60) {
				
				// Convert to minutes
				lTimeTaken = lTimeTaken / 60;
				if (lTimeTaken > 60) {
					
					// Convert to hours
					lTimeTaken = lTimeTaken / 60;
					if (lTimeTaken > 24) {
						
						// Convert to days
						lTimeTaken = lTimeTaken / 24;
						
						// return time taken as string
						return Double.toString(lTimeTaken) + " days";
					} else {
						// return time taken as string
						return Double.toString(lTimeTaken) + " hours";
					}
				} else {
					// return time taken as string
					return Double.toString(lTimeTaken) + " minutes";
				}
			} else {
				// return time taken as string
				return Double.toString(lTimeTaken) + " seconds";
			}
		}
		
		// return time taken as string
		return Double.toString(lTimeTaken) + " milliseconds";
		
	}
	
	/**
	 * Gets the time taken from when the timer was started to when the timer was stopped.
	 * @return	The time taken in milliseconds
	 */
	public double getTimeTaken() {
		return (cStopTime.getTimeInMillis() - cStartTime.getTimeInMillis());
	}
	
	/**
	 * Returns the total time taken as a string suitable for direct inclusion in a log
	 * @return	String	Time total taken as string
	 */
	public String getTotalTimeTakenAsString() {
		
		// The the total time taken 
		double lTimeTaken = getTotalTimeTaken();
		
		if (cTimeTakenInMilliseconds > 1000) {
			
			// Convert to seconds
			lTimeTaken = lTimeTaken / 1000;
			if (lTimeTaken > 60) {
				
				// Convert to minutes
				lTimeTaken = lTimeTaken / 60;
				if (lTimeTaken > 60) {
					
					// Convert to hours
					lTimeTaken = lTimeTaken / 60;
					if (lTimeTaken > 24) {
						
						// Convert to days
						lTimeTaken = lTimeTaken / 24;
						
						// return time taken as string
						return Double.toString(lTimeTaken) + " days";
					} else {
						// return time taken as string
						return Double.toString(lTimeTaken) + " hours";
					}
				} else {
					// return time taken as string
					return Double.toString(lTimeTaken) + " minutes";
				}
			} else {
				// return time taken as string
				return Double.toString(lTimeTaken) + " seconds";
			}
		}
		
		// return time taken as string
		return Double.toString(lTimeTaken) + " milliseconds";
		
	}
	
	/**
	 * Gets the total time taken from when the timer was originally started to when the timer was stopped.
	 * @return	The time taken in milliseconds
	 */
	public double getTotalTimeTaken() {
		return cTimeTakenInMilliseconds;
	}
	
	/**
	 * Logs the message supplied with the time taken
	 * @param Message	The message to include
	 * @return 			The time taken in milliseconds
	 */
	public double logTimeTaken(String Message) {
		
		// Only log the time taken if in debug mode
		if (cDebugOutput) {
			// Log the message
			logMessage(Message + " in " + getTimeTakenAsString());
		} 
		
		// Add this to the timer log as well
		cTimerLog.add(getTimeStamp() + " " + Message + " in " + getTimeTakenAsString());
		
		// Return the time taken
		return getTimeTaken();
	}
	
	/**
	 * Logs the message supplied with the time taken
	 * @param Message		The message to include
	 * @param DebugOnly		Set to true to force the message even if not in debug mode. Default is false
	 */
	public void logTimeTaken(String Message, boolean DebugOnly) {
		
		// Only log the time taken if in debug mode
		if (cDebugOutput || !DebugOnly) {
			// Log the message
			logMessage(Message + " in " + getTimeTakenAsString());
		}
	}
	
	/**
	 * Logs the message supplied with the total time taken since last timer reset
	 * @param Message	The message to include
	 * @return 			The total time taken in milliseconds
	 */
	public double logTotalTimeTaken(String Message) {
		
		// Only log the time taken if in debug mode
		if (cDebugOutput) {
			// Log the message
			logMessage(Message + " in " + getTotalTimeTakenAsString());
		} 
		
		// Add this to the timer log as well
		cTimerLog.add(getTimeStamp() + " " + Message + " in " + getTotalTimeTakenAsString());
		
		// Return the time taken
		return getTotalTimeTaken();
	}
	
	// Memory Functions getters and setters
	
	/**
	 * Gets the free memory available at the start
	 * @return	Free memory in bytes
	 */
	public long getFreeMemoryAtStart() {
		return cFreeMemoryAtStart;
	}
	
	/**
	 * Gets the free memory available at the start
	 * @return	Free memory in megabytes
	 */
	public long getFreeMemoryAtStartInMB() {
		return cFreeMemoryAtStart / 1048576;
	}
	
	/**
	 * Gets the memory used at the start
	 * @return	Used memory in bytes
	 */
	public long getMemoryUsedAtStart() {
		return cMemoryUsedAtStart;
	}
	
	/**
	 * Gets the memory used at the start
	 * @return	Used memory in megabytes
	 */
	public long getMemoryUsedyAtStartInMB() {
		return cMemoryUsedAtStart / 1048576;
	}
	
	/**
	 * Gets the amount of free memory currently
	 * @return	The amount of free memory 
	 */
	public long getFreeMemory() {
		return Runtime.getRuntime().freeMemory();
	}
	
	/**
	 * Gets the amount of free memory in megabytes
	 * @return	The amount of free memory in megabytes
	 */
	public long getFreeMemoryInMB() {
		return getFreeMemory() / 1048576;
	}
	
	/**
	 * Gets the amount of memory used currently
	 * @return	The amount of memory being used
	 */
	public long getMemoryUsed() {
		return Runtime.getRuntime().totalMemory();
	}
	
	/**
	 * Gets the amount of memory used currently in megabytes
	 * @return	The amount of memory being used in megabytes
	 */
	public long getMemoryUsedInMB() {
		return getMemoryUsed() / 1048576;
	}
	
	/**
	 * Runs the Java runtime garbage collector to try and free up some
	 * memory. 
	 */
	public void freeUpMemory() {
		Runtime.getRuntime().gc();
	}
		
	/**
	 * Adds memory status to the timer log, and if in debug mode, to the main log as well
	 */
	public void addMemStatToTimerLog() {
		cTimerLog.add(String.format("\nMemory used at start: %d MB, Free at Start: %d MB", getMemoryUsedyAtStartInMB(), getFreeMemoryAtStartInMB()));
		cTimerLog.add(String.format("\nMemory used currently: %d MB, Free: %d MB", getMemoryUsedInMB(), getFreeMemoryInMB()));
		if (cDebugOutput) addMemStatToLog();
	}
	
	/**
	 * Adds memory status to the log
	 */
	public void addMemStatToLog() {
		logMessage(String.format("\nMemory used at start: %d MB, Free at Start: %d MB", getMemoryUsedyAtStartInMB(), getFreeMemoryAtStartInMB()));
		logMessage(String.format("\nMemory used currently: %d MB, Free: %d MB", getMemoryUsedInMB(), getFreeMemoryInMB()));
	}
	
	// end Memory Functions
	
	// PRIVATE METHODS
	
	/**
	 * Writes a message to the log file. If the log file cannot be created or is not accessible
	 * the function returns a false. Each message will have a date and time preceding the message
	 * @param message	The message to be written to the log file.
	 * @return			True if success, false if not.
	 */
	private boolean writeToLogFile(String message) {
		
		// Create or access the file
		File lLogFile = new File(cLogFilePath);

		try {
			
			// Check if the log file exists
			if (!lLogFile.canWrite()) {
				
				// Try and create the log file
				if (!lLogFile.createNewFile()) {
					throw new Exception("Log file could not be created");
				}
			}
			
			// Create the file writer, and create the file if it doesn't exist
			FileWriter lLogWriter = new FileWriter(lLogFile, true);
			
			// Write the message to the file
			lLogWriter.write(getTimeStamp() + " " + message + System.lineSeparator());
			
			// Close the file
			lLogWriter.close();
			
		} catch (Exception e) {
			
			// Debug output
			debugOut(e, "Could not create file");
			
			// File system issues, return false
			return false;
			
		}

		// Message successfully written to the log file
		return true;
		
	}

	/**
	 * Tries to write a message or error to the log file. If this fails, the error or message gets added
	 * to log in memory. The error or message will get added to the log in memory as well if the 
	 * cSaveLogToMemory flag is set to true
	 * @param Message	The message or error to be logged
	 */
	private void logErrorOrMessage(String Message) {
	
		cLogFileAccessible = writeToLogFile(Message);
		
		if (!cLogFileAccessible || cSaveLogToMemory) {
			
			// Could not write the message to the log file, so store it in memory
			cLog.add(getTimeStamp() + " " + Message);
						
		}
		
	}
	
	/**
	 * Gets a timestamp to add to the log file
	 * @return	String containing the timestamp
	 */
	protected String getTimeStamp() {
		
		LocalDateTime lTimestamp = LocalDateTime.now();
		return lTimestamp.format(DateTimeFormatter.ofPattern("MMM d HH:mm:ss"));
		
	}
	
	private void debugOut(Exception e, String message) {
		if (cDebugOutput) {
			System.out.println(message);
			debugOut(e);
		}
	}
	
	private void debugOut(Exception e) {
		if (cDebugOutput) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Sends an email alert 
	 * @param message	The message to include
	 * @param subject	The subject to include
	 * @return			True if successful, false if not - errors logged
	 */
	private boolean sendMailAlert(String message, String subject) {
		
		// Create the properties object
		Properties lProperties = new Properties();
		
		// Set the properties for the session
		lProperties.put("mail.smtp.host", cSmtpServerFQDN);
		lProperties.put("mail.smtp.port", cSmtpPort);
		lProperties.put("mail.smtp.auth", cUseAuth);
		lProperties.put("mail.smtp.starttls.enable", cUseStartTls);
		
		// Set up the authentication
		Authenticator lAuth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(cSendingAccountUsernameEmail, cPassword);
			}
		};
		
		// Create the session
		Session lSession = Session.getDefaultInstance(lProperties, lAuth);
		
		try {
		
			// Set up the message
			MimeMessage lMessage = new MimeMessage(lSession);
						
			lMessage.addHeader("Content-type", "text/HTML; charset=UTF-8");
			lMessage.addHeader("format","flowed");
			lMessage.addHeader("Content-Transfer-Encoding","8bit");
			lMessage.setFrom(new InternetAddress(cSendingAccountUsernameEmail, cFromDisplayName));
			lMessage.setReplyTo(InternetAddress.parse(cSendingAccountUsernameEmail, false));
			lMessage.setSubject(subject, "UTF-8");
			lMessage.setText(message, "UTF-8");
			lMessage.setSentDate(new Date());
			lMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(cDestinationEmail, false));
			
			// Try and send the message
			Transport.send(lMessage);
			
		} catch (Exception e) {
			
			// Log the issue in the log file
			logError(e.getMessage());
			
			// Sending failed, return false
			return false;
		}
		
		// Sending successful, return true
		return true;
		
	}
	
	
	// PRIVATE ATTRIBUTES

	// The list of errors that may have occurred
	private ArrayList<String> cLog;
	
	// The list of timer events 
	private ArrayList<String> cTimerLog;
	
	// The last error logged
	private String cLastError;
	
	// The last message that was logged
	private String cLastMessage;
	
	// If true, this will put log messages in memory as well as 
	// the error log file
	private boolean cSaveLogToMemory;
	
	// A configuration file
	private ConfigFile cConfig;
	
	// The string to put in front of errors
	private String cErrorTag;
	
	// The path to the log file
	private String cLogFilePath;
	
	// Whether the log file is accessible
	private boolean cLogFileAccessible;
	
	// Used for debugging
	private boolean cDebugOutput;
	
	// the start time for the timer
	private GregorianCalendar cStartTime;
	
	// the end time for the timer
	private GregorianCalendar cStopTime;
	
	// The total time taken 
	private double cTimeTakenInMilliseconds;
	
	// email related attributes --------------------------------------------
	
	// The email account email address/username used for sending alerts
	private String cSendingAccountUsernameEmail;
	
	// The name to display for sending account user.
	private String cFromDisplayName;
	
	// The password for the sending account
	private String cPassword;
	
	// The destination recipient email address
	private String cDestinationEmail;
	
	// The fully qualified domain name of the smtp server. 
	private String cSmtpServerFQDN;	
	
	// The port for the smtp server to use
	private String cSmtpPort;
	
	// Whether to use authentication or not
	private String cUseAuth;
	
	// Whether or not to use STARTTLS
	private String cUseStartTls;
	
	// memory management functions
	
	// The amount of free memory at the start
	private long cFreeMemoryAtStart;
	
	// The amount of memory used at start
	private long cMemoryUsedAtStart;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
