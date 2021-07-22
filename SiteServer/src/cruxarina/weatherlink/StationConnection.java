package cruxarina.weatherlink;


import java.io.IOException;
import java.net.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.util.*;

import com.google.gson.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class StationConnection implements StationInterface {

	// CONSTRUCTORS
	public StationConnection() {
		cDeviceIP = "";
		setPort(80);
		setRequestString("/v1/current_conditions");
		setBroadcastRequestString("/v1/real_time");
		setBroadcastDuration(1);
		LogFile = new Common();
		LogFile.setLogFilePath("/var/log/wsssd/wsssd.log");
		cHttpHost = "";
		cHttpReceiveFile = "siteServerRec.php";
		cLiveBufSize = 2000;
		cLiveReceivingPort = 22222;
		cLiveTimeout = 5000;
		cStartTime = new GregorianCalendar();
		setRefreshUpdateFrequency(60);
		cDegradedPerformanceLatencyThreshold = 500;
		cDegradedPerformanceTollerance = 5;
		cResponseTooLongThreshold = 2400;
		cDegradedPerformanceCount = 0;
		cDegradedPerformanceState = false;
		
	}
	
	public StationConnection(String DeviceIP) {
		this();
		cDeviceIP = DeviceIP;
				
		// Get the data
		refresh();
		
	}
	
	/**
	 * Initializes this connection from the supplied config file
	 * @param config
	 */
	public StationConnection(ConfigFile config) {
		this();
		this.cConfiguration = config;
		
		// Load configuration
		String lDeviceIP = cConfiguration.getConfig("WeatherLinkLiveIP");
		String lPort = cConfiguration.getConfig("WeahterLinkLivePort");
		String lRequestString = cConfiguration.getConfig("RequestString");
		String lBroadcastRequestString = cConfiguration.getConfig("BroadcastRequestString");
		String lBroadcastDuration = cConfiguration.getConfig("BroadcastDuration");
		String lHttpHost = cConfiguration.getConfig("WebAppServerHTTPHost");
		String lHttpReceiveFile = cConfiguration.getConfig("WebAppReceiveScript");
		String lLiveReceivingPort = cConfiguration.getConfig("LiveUDPReceivePort");
		String lLiveTimeout = cConfiguration.getConfig("LiveUDPTimeOut");
		String lRefreshUpdateFrequency = cConfiguration.getConfig("RefreshUpdateFrequency");
		String lLogFilePath = cConfiguration.getConfig("LogFilePath");	
		String lDegradedPerformanceLatencyThreshold = cConfiguration.getConfig("DegradedPerformanceLatencyThreshold");
		String lDegradedPerformanceTollerance = cConfiguration.getConfig("DegradedPerformanceTollerance");
		String lResponseTooLongThreshold = cConfiguration.getConfig("ResponseTooLongThreshold");
		String lSendingAccountUsernameEmail = cConfiguration.getConfig("SendingAccountUsernameEmail");
		String lFromDisplayName = cConfiguration.getConfig("FromDisplayName");
		String lPassword = cConfiguration.getConfig("Password");
		String lDestinationEmail = cConfiguration.getConfig("DestinationEmail");
		String lSmtpServerFQDN = cConfiguration.getConfig("SmtpServerFQDN");
		String lSmtpPort = cConfiguration.getConfig("SmtpPort");
		String lUseAuth = cConfiguration.getConfig("UseAuth");
		String lUseStartTls = cConfiguration.getConfig("UseStartTl");
		
		// Apply configured values
		cDeviceIP = (lDeviceIP.length() > 0) ? lDeviceIP: cDeviceIP;
		setPort((lPort.length() > 0) ? Integer.parseInt(lPort) : getPort());
		setRequestString((lRequestString.length() > 0) ? lRequestString : getRequestString());
		setBroadcastRequestString((lBroadcastRequestString.length() > 0) ? lBroadcastRequestString : getBroadcastRequestString());
		setBroadcastDuration((lBroadcastDuration.length() > 0) ? Integer.parseInt(lBroadcastDuration) : getBroadcastDuration());
		cHttpHost = (lHttpHost.length() > 0) ? lHttpHost : cHttpHost;
		cHttpReceiveFile = (lHttpReceiveFile.length() > 0) ? lHttpReceiveFile : cHttpReceiveFile;
		cLiveReceivingPort = (lLiveReceivingPort.length() > 0) ? Integer.parseInt(lLiveReceivingPort) : cLiveReceivingPort;
		cLiveTimeout = (lLiveTimeout.length() > 0) ? Integer.parseInt(lLiveTimeout) : cLiveTimeout;
		setRefreshUpdateFrequency((lRefreshUpdateFrequency.length() > 0) ? Integer.parseInt(lRefreshUpdateFrequency): getRefreshUpdateFrequency());
		LogFile.setLogFilePath((lLogFilePath.length() > 0) ? lLogFilePath : LogFile.getLogFilePath());
		cDegradedPerformanceLatencyThreshold = ((lDegradedPerformanceLatencyThreshold.length() > 0) ? Integer.parseInt(lDegradedPerformanceLatencyThreshold) : getDegradedPerformanceLatencyThreshold());
		cDegradedPerformanceTollerance = ((lDegradedPerformanceTollerance.length() > 0) ? Integer.parseInt(lDegradedPerformanceTollerance) : getDegradedPerformanceTollerance());
		cResponseTooLongThreshold = ((lResponseTooLongThreshold.length() > 0) ? Integer.parseInt(lResponseTooLongThreshold) : getResponseTooLongThreshold());
		LogFile.setSendingAccountUsernameEmail((lSendingAccountUsernameEmail.length() > 0) ? lSendingAccountUsernameEmail : LogFile.getSendingAccountUsernameEmail());
		LogFile.setFromDisplayName((lFromDisplayName.length() > 0) ? lFromDisplayName : LogFile.getFromDisplayName());
		LogFile.setPassword((lPassword.length() > 0) ? lPassword : LogFile.getPassword());
		LogFile.setDestinationEmail((lDestinationEmail.length() > 0) ? lDestinationEmail : LogFile.getDestinationEmail());
		LogFile.setSmtpServerFQDN((lSmtpServerFQDN.length() > 0) ? lSmtpServerFQDN : LogFile.getSmtpServerFQDN());
		LogFile.setSmtpPort((lSmtpPort.length() > 0) ? lSmtpPort : LogFile.getSmtpPort());
		LogFile.setUseAuth((lUseAuth.length() > 0) ? lUseAuth : LogFile.getUseAuth());
		LogFile.setUseStartTls((lUseStartTls.length() > 0) ? lUseStartTls : LogFile.getUseStartTls());
		
		// Get the data
		refresh();
		
	}
	
	public StationConnection(String DeviceIP, String RequestString) {
		
		this();
		cDeviceIP = DeviceIP;
		setRequestString(RequestString);
		
		// Get the data
		refresh();
		
	}
	
	public StationConnection(String DeviceIP, int Port) {

		this();
		cDeviceIP = DeviceIP;
		setPort(Port);
		
		// Get the data
		refresh();
		
	}

	public StationConnection(String DeviceIP, int Port, String RequestString) {

		this();
		cDeviceIP = DeviceIP;
		setPort(Port);
		setRequestString(RequestString);
		
		// Get the data
		refresh();
		
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * Gets the IP address of the device
	 * @return IP address of the device
	 */
	public String getDeviceIP() {
		return cDeviceIP;
	}

	/**
	 * Sets the IP address of the device
	 * @param deviceIP
	 */
	public void setDeviceIP(String deviceIP) {
		cDeviceIP = deviceIP;
	}
	
	/**
	 * The port used for the HTTP request, default set to 80.
	 * @return the port
	 */
	public int getPort() {
		return cPort;
	}

	/** 
	 * The port used for the HTTP request, default set to 80.
	 * @param port the port to set
	 */
	public void setPort(int port) {
		cPort = port;
	}

	/**
	 * Gets the request string used to start UDP broadcasts of live weather data on the network
	 * @return	The reqeust string
	 */
	public String getBroadcastRequestString() {
		return cBroadcastRequestString;
	}
	
	/**
	 * Sets the request string used to start UDP broadcasts of live weather data on the network
	 * @param 	BroadcastRequestString	The request to send. e.g. /v1/real_time or /v1/current_conditions
	 */
	public void setBroadcastRequestString(String BroadcastRequestString) {
		cBroadcastRequestString = BroadcastRequestString;
	}
	
	/**
	 * Gets the amount of hours live data broadcast will last for
	 * @return	How many hours the live broadcast will last for
	 */
	public int getBroadcastDuration() {
		// Convert seconds to hours
		return cBroadcastDuration / 3600;
	}
	
	/**
	 * Sets the amount of hours a live broadcast will be sent. Maximum is 24 hours.
	 * @param 	Hours	How many hours to keep the broadcasts online
	 */
	public void setBroadcastDuration(int Hours) {
		// Convert hours to seconds
		cBroadcastDuration = Hours * 3600;
	}
	
	/**
	 * Gets the HTTP request string to get the data. Default is /v1/current_conditions
	 * @return The requestString
	 */
	public String getRequestString() {
		return cRequestString;
	}


	/** Gets the HTTP request string to get the data. Default is /v1/current_conditions
	 * @param requestString the requestString to set
	 */
	public void setRequestString(String requestString) {
		cRequestString = requestString;
	}

	/**
	 * @return the observationsString
	 */
	public String getObservationsString() {
		return cObservationsJSONString;
	}

	/**
	 * @param observationsString the observationsString to set
	 */
	public void setObservationsString(String observationsString) {
		cObservationsJSONString = observationsString;
	}
	
	/**
	 * Gets the buffer size to receive the WeatherLink live UDP packet. Default is 2000 bytes.
	 * @return	The size of the buffer in bytes
	 */
	public int getLiveBufSize() {
		return cLiveBufSize;
	}
	
	/**
	 * Gets the buffer size to receive the WeatherLink live UDP packet. Default is 2000 bytes.
	 * 
	 * @param 	BufferSize 	The size of the buffer to use in bytes
	 */
	public void setLiveBufSize(int BufferSize) {
		cLiveBufSize = BufferSize;
	}
	
	/**
	 * Gets the port number used to listen to live WeatherLink Live UDP packets. The default is 22222
	 * @return	The port number to listen on for UDP packets
	 */
	public int getLiveReceivingPort() {
		return cLiveReceivingPort;
	}
	
	/**
	 * Sets the port number used to listen to live WeatherLink Live UDP packets. The default is 22222
	 * 
	 * @param 	PortNumber	The port number to listen on.
	 */
	public void setLiveReceivingPort(int PortNumber) {
		cLiveReceivingPort = PortNumber;
	}
	/**
	 * Gets the amount of time in seconds to wait for a UDP packet from the device. The default is 6 seconds
	 * @return The amount of time in seconds to wait
	 */
	public int getLiveTimeout() {
		return cLiveTimeout / 1000;
	}

	/**
	 * Sets the amount of time in seconds to wait for a UDP packet from the device. 
	 * @param 	liveTimeout 	The amount of time in seconds to wait.
	 */
	public void setLiveTimeout(int liveTimeout) {
		cLiveTimeout = liveTimeout * 1000;
	}
	 
	/**
	 * Sets the refresh update frequency. This is when all sensors values are updated, not just the wind speed, 
	 * direction, and rain values during a live refresh session.
	 * @param 	seconds		The amount of seconds to wait before the next full refresh
	 */
	public void setRefreshUpdateFrequency(int seconds) {
		cRefreshFrequency = seconds * 1000;
	}
	
	/**
	 * Sets the refresh update frequency. This is when all sensors values are updated, not just the wind speed, 
	 * direction, and rain values during a live refresh session.
	 * @return	The amount of seconds to wait before the next full refresh
	 */
	public int getRefreshUpdateFrequency() {
		return cRefreshFrequency / 1000;
	}
	
	/**
	 * Gets the URL on a HTTP webhost that will receive the forwarded weather station
	 * data for access over the Internet. 
	 * @return The string containing the URL of the webhost
	 */
	public String getHttpHost() {
		return cHttpHost;
	}

	/**
	 * Sets the URL on a HTTP webhost that will receive the forwarded weather station
	 * data for access over the Internet. 
	 * 
	 * @param 	HttpHost 	The URL to set
	 */
	public void setHttpHost(String HttpHost) {
		
		// Get rid of the forward slash at the end if there is one
		this.cHttpHost = (HttpHost.charAt(HttpHost.length()-1)=='/') ? HttpHost.substring(0, HttpHost.length()-2) : HttpHost;
	}
	
	/**
	 * Gets the actual PHP filename that processes the http request forwarded to the host
	 * @return	The PHP filename
	 */
	public String getReceiveFile() {
		return this.cHttpReceiveFile;
	}
	
	/**
	 * Sets the actual PHP filename that processes the http request forwarded to the host
	 * @param Filename	The PHP filename that will process the request. 
	 */
	public void setReceiveFile(String Filename) {
		this.cHttpReceiveFile = Filename;
	}
	
	/**
	 * Returns the time taken to upload observations data to the HttpHost in seconds
	 * @return The time taken
	 */
	public double getHttpRequestResponseTime() {
		return cHttpRequestResponseTime;
	}
	
	/**
	 * Gets the amount of milliseconds of http host response that is acceptable
	 * @return	Time in milliseconds
	 */
	public int getDegradedPerformanceLatencyThreshold() {
		return cDegradedPerformanceLatencyThreshold;
	}
	
	/**
	 * Sets the amount of milliseconds of http host response that is acceptable
	 * @return	Time in milliseconds
	 */
	public void setDegradedPerformanceLatencyThreshold(int threshold) {
		cDegradedPerformanceLatencyThreshold = threshold;
	}
	
	/**
	* Gets the amount of time in milliseconds considered too slow resulting in
	* an immediate error condition. 
	* @return Response Time threshold in milliseconds
	*/
	public int getResponseTooLongThreshold() {
		return cResponseTooLongThreshold;
	}
	
	/**
	 * Sets the amount of time in milliseconds considered too slow resulting in
	 * an immediate error condition. 
	 * @param responseThreshold		The amount of time in milliseconds
	 */
	public void getResponseTooLongThreshold(int responseThreshold) {
		cResponseTooLongThreshold = responseThreshold;
	}
	
	/**
	 * Gets the amount of times in a row that the http host response is allowed to
	 * be above the threshold
	 * @return	The amount of times
	 */
	public int getDegradedPerformanceTollerance() {
		return cDegradedPerformanceTollerance;
	}
	
	/**
	 * Sets the amount of times in a row that the http host response is allowed to
	 * be above the threshold
	 * @return	The amount of times
	 */
	public int setDegradedPerformanceTollerance() {
		return cDegradedPerformanceTollerance;
	}
	
	/**
	 * Returns the configuration file used by this Station Connection class.
	 * @return	The configuration file
	 */
	public ConfigFile getConfig() {
		return cConfiguration;
	}
	
	/**
	 * Adds an error message to the class' error log
	 * @param Message	A string containing an error message
	 */
	public void logError(String Message) {
		LogFile.logError(Message);
	}
	
	/**
	 * Adds an error message to the class' error log and sends an email
	 * @param Message	A string containing an error message
	 * @param Subject	The email subject
	 */
	public void logError(String Message, String Subject) {
		LogFile.logError(Message, Subject);
	}
	
	/**
	 * Adds a message to the class' message log
	 * @param Message	A string containing an message
	 */
	public void logMessage(String Message) {
		LogFile.logMessage(Message);
	}
	
	/**
	 * Adds a message to the class' message log and sends an email
	 * @param Message	A string containing an message
	 * @param Subject	The email subject
	 */
	public void logMessage(String Message, String Subject) {
		LogFile.logMessage(Message, Subject);
	}
	
	/**
	 * Gets the last error that was logged
	 * @return The last error message
	 */
	public String lastError() {
		return LogFile.lastError();
	}
	
	/**
	 * Returns the last message that was logged
	 * @return The last message
	 */
	public String lastMessage() {
		return LogFile.lastMessage();
	}
	
	/**
	 * Gets the latest JSON string from the device and deserializes it
	 * Check logs for issues if data did not refresh.
	 */
	public boolean refresh() {
		
		if (!getObservations()) {
			logError("Failed to connect and retrieve data");
			return false;
		}
		
		if (!deserializeJSON()) {
			logError("Failed to deserialize JSSON");
			return false;
		}
		
		// Attempt to send the JSON data from the weather station to the web server
		if (cHttpHost.length()>0) {
			if (!forwardToHttpHost()) {
				logError("Could not forward local station data to HTTP host");
				return false;
			}
		}
		
		return true; 
	}
	
	/**
	 * Gets live data from the Weatherlink Live appliance's UDP broadcast packets
	 * providing up to date weather data refreshing once every 2.5 seconds. 
	 * @return	True if data retrieved successfully, or false if timed out or if there 
	 * 			were network errors.
	 */
	public boolean refreshLive() {
						
		// Reset the timer
		LogFile.resetTimer();
				
		LogFile.startTimer("Obtaining live data from broadcast packet");
		
		if (!getLiveObservations()) {
		
			LogFile.stopTimer();
			LogFile.logTimeTaken("Failed to get live data from broadcast");
			
			LogFile.startTimer("Requesting broadcast stream from device " + cDeviceIP);
			// broadcast may not have started
			if (!startLiveBroadcast()) {
				
				// Add memory status to the timer log as well
				LogFile.addMemStatToTimerLog();
				
				// Send email alert
				LogFile.sendTimerLogAsEmail("Catastrophic Failure: Could not start live broadcast!", "Wsssd Serivce Failed!");
				
				return false;
			} else {
				
				LogFile.stopTimer();
				LogFile.logTimeTaken("Broadcast stream requested from the device");
				
				LogFile.startTimer("Attempting to obtain live data from broadcast packet again");
				
				if (!getLiveObservations()) {
					
					// Add memory status to the timer log as well
					LogFile.addMemStatToTimerLog();
					
					// Send email alert
					LogFile.sendTimerLogAsEmail("Catastrophic Failure: Could not receive broadcast packets!", "Wsssd Serivce Failed!");
					
					return false;
				}
				
			}
		}
		
		LogFile.stopTimer("Data obtained from broadcast packet");
		LogFile.logTimeTaken("Live data obtained");
		
		
		LogFile.startTimer("Deserializing JSON data");
		
		if (!deserializeJSON()) {
			logError("Could not deserialize JSON. " + lastError());
			return false;
		}
		
		LogFile.stopTimer();
		LogFile.logTimeTaken("Successfully deserialized the JSON data");
		
		// Attempt to send the JSON data from the weather station to the web server
				
		if (cHttpHost.length()>0) {
		
			LogFile.startTimer("Attempting to send JSON data to web app server");
						
			if (!forwardToHttpHost()) {
				logError("Could not forward local station data to HTTP host");
				return false;
			}
			
			LogFile.stopTimer();
			LogFile.logTimeTaken("Successfully forwarded JSON data to web app server");
			
			// Record the Http Request Response Time
			cHttpRequestResponseTime = LogFile.getTimeTaken() / 1000.00;
			
			// Log the time taken for the whole process.
			LogFile.logTotalTimeTaken("Latest weather data forwarded");
			
			// Add memory status to the timer log as well
			LogFile.addMemStatToTimerLog();
			
			// Report the performance of this run
			reportPerformance((int)LogFile.getTotalTimeTaken());
			
		}
		
		return true;
	}
	
	// PUBLIC METHODS
	
	/**
	 * Gets the sensor ID
	 * @return The sensor ID
	 */
	public long getLsid() { 
		JsonElement lResult = getJsonElement("lsid");
		return (lResult == null || lResult.isJsonNull()) ? INT_ERROR : lResult.getAsInt(); 
	}
	/** 
	 * Gets the data structure type, which is specific to groups of sensors.
	 * @return The Data Structure Type
	 */
	public int getDataStructureType() {
		JsonElement lResult = getJsonElement("data_structure_type");
		return (lResult == null || lResult.isJsonNull()) ? INT_ERROR : lResult.getAsInt(); 
	}
	/**
	 * Gets the ID of a sensor suite 
	 * @return The Transmitter ID
	 */
	public int getTransmitterID() {
		JsonElement lResult = getJsonElement("txid");
		return (lResult == null || lResult.isJsonNull()) ? INT_ERROR : lResult.getAsInt(); 
	}
	/**
	 * Gets the outside temperature
	 * @return The outside temperature
	 */
	public float getTemp() { 
		JsonElement lResult = getJsonElement("temp");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the humidity as percentage
	 * @return The humidity
	 */
	public float getHum() {
		JsonElement lResult = getJsonElement("hum");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the Dew point 
	 * @return The Dew Point
	 */
	public float getDewPoint() {
		JsonElement lResult = getJsonElement("dew_point");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the wet bulb temperature 
	 * @return 	The Wet Bulb temperature
	 */
	public float getWetBulb() {
		JsonElement lResult = getJsonElement("wet_bulb");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the heat index 
	 * @return The Heat Index
	 */
	public float getHeatIndex() {
		JsonElement lResult = getJsonElement("heat_index");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/** 
	 * Gets the wind chill temperature 
	 * @return The Wind Chill temperature
	 */
	public float getWindChill() {
		JsonElement lResult = getJsonElement("wind_chill");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the THW index 
	 * @return The THW Index
	 */
	public float getThwIndex() {
		JsonElement lResult = getJsonElement("thw_index");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the THSW index
	 * @return The THSW Index
	 */
	public float getThswIndex() {
		JsonElement lResult = getJsonElement("thsw_index");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the most recent wind speed 
	 * @return The most recent wind speed detected.
	 */
	public float getWindPpeedLast() {
		JsonElement lResult = getJsonElement("wind_speed_last");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/** Gets the most recent/current Wind Direction in degrees from North 
	 * Going clockwise
	 * @return The most recent/current Wind Direction
	 */
	public float getWindDirLast() {
		JsonElement lResult = getJsonElement("wind_dir_last");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/** 
	 * Gets the average wind speed over the last minute
	 * @return The average wind speed over the last minute
	 */
	public float getWindSpeedAvgLast_1_min() {
		JsonElement lResult = getJsonElement("wind_speed_avg_last_1_min");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Returns the average wind direction over the last one minute in degrees
	 * @return The average wind direction over the last 1 minute
	 */
	public float getWindDirScalarAvg_last_1_min() {
		JsonElement lResult = getJsonElement("wind_dir_scalar_avg_last_1_min");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/** 
	 * Gets the average wind speed over the last two minutes
	 * @return The average wind speed over the last 2 minutes
	 */
	public float getWindSpeedAvgLast_2_min() {
		JsonElement lResult = getJsonElement("wind_speed_avg_last_2_min");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Returns the average wind direction over the last two minutes in degrees
	 * @return The wind direction over the last 2 minutes
	 */
	public float getWindDirScalarAvg_last_2_min() {
		JsonElement lResult = getJsonElement("wind_dir_scalar_avg_last_2_min");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the highest wind speed reached in the last 2 minutes 
	 * @return The highest wind speed reached in the last 2 minutes 
	 */
	public float getWindSpeedHi_last_2_min() {
		JsonElement lResult = getJsonElement("wind_speed_hi_last_2_min");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/** 
	 * Gets the Wind Direction in degrees when blowing at highest speed in the last 2 minutes
	 * @return The Wind Direction at highest speed in last 2 minutes in degrees 
	 */
	public float getWindDirAtHiSpeedLast_2_min() {
		JsonElement lResult = getJsonElement("wind_dir_at_hi_speed_last_2_min");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the average wind speed during the last 10 minutes
	 * @return The average wind speed during the last 10 minutes 
	 * 	 
	 */
	public float getWindSpeedAvgLast_10_min() {
		JsonElement lResult = getJsonElement("wind_speed_avg_last_10_min");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Returns the average scalar wind direction during the last 10 minutes in degrees
	 * @return The average scalar wind direction 
	 */
	public float getWindDirScalarAvgLast_10_min() {
		JsonElement lResult = getJsonElement("wind_dir_scalar_avg_last_10_min");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the maximum wind speed during the last 10 minutes 
	 * @return The maximum wind speed during the last 10 minutes
	 */
	public float getWindSpeedHiLast_10_min() {
		JsonElement lResult = getJsonElement("wind_speed_hi_last_10_min");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the wind direction of the maximum wind speed during the last 10 minutes in degrees
	 * @return The wind direction of the maximum wind speed during the last 10 minutes in degrees
	 */
	public float getWindDirAtHiSpeedLast_10_min() {
		JsonElement lResult = getJsonElement("wind_dir_at_hi_speed_last_10_min");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the size/volume of the rain tipping bucket/spoon installed in the sensor suite. 
	 * @return The tipping bucket/spoon volume
	 */
	public int getRainSize() {
		JsonElement lResult = getJsonElement("rain_size");
		return (lResult == null || lResult.isJsonNull()) ? INT_ERROR : lResult.getAsInt();
	}
	/**
	 * Returns the current rain rate at units per hour 
	 * @return The current rain rate
	 */
	public float getRainRateLast() {
		JsonElement lResult = getJsonElement("rain_rate_last");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Returns the highest rain rate over one minute
	 * @return The highest rain rate over one minute
	 */
	public float getRainRateHi() {
		JsonElement lResult = getJsonElement("rain_rate_hi");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Returns the rainfall during the last 15 minutes 
	 * @return The rainfall during the last 15 minutes
	 */
	public float getRainfallLast_15_min() {
		JsonElement lResult = getJsonElement("rainfall_last_15_min");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the highest rain rate during the last 15 minutes
	 * @return The highest rain rate during the last 15 minutes
	 */
	public float getRainRateHiLast_15_min() {
		JsonElement lResult = getJsonElement("rain_rate_hi_last_15_min");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/** 
	 * Gets the rainfall during the last 60 minutes
	 * @return The rainfall during the last 60 minutes
	 */
	public float getRainfallLast_60_min() {
		JsonElement lResult = getJsonElement("rainfall_last_60_min");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the rainfall during the last 24 hours
	 * @return The rainfall during the last 24 hours
	 */
	public float getRainfallLast_24_hr() {
		JsonElement lResult = getJsonElement("rainfall_last_24_hr");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the total amount of rain during the current rain storm event 
	 * @return The total amount of rain during the current rain storm event
	 */
	public float getRainStorm() {
		JsonElement lResult = getJsonElement("rain_storm");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the date and time the rain storm/event started in seconds since start
	 * of 1970
	 * @return Seconds since 1970 when rain storm started (UNIX timestamp)
	 */
	public long getRainStormStartAt() {
		JsonElement lResult = getJsonElement("rain_storm_start_at");
		return (lResult == null || lResult.isJsonNull()) ? LONG_ERROR : lResult.getAsLong();
	}
	/**
	 * Gets the solar radiation
	 * @return The solar radiation
	 */
	public float getSolarRad() {
		JsonElement lResult = getJsonElement("solar_rad");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the UV index
	 * @return The UV Index
	 */
	public float getUvIndex() {
		JsonElement lResult = getJsonElement("uv_index");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the configured radio receiver state
	 * @return The radio receiver state
	 */
	public int getRxState(int transmitterID) {
		JsonElement lResult = getJsonElement("rx_state", transmitterID);
		return (lResult == null || lResult.isJsonNull()) ? INT_ERROR : lResult.getAsInt();
	}
	/**
	 * Gets the status of the battery
	 * @return The status of the battery flag
	 */
	public int getTransBatteryFlag(int transmitterID) {
		JsonElement lResult = getJsonElement("trans_battery_flag", transmitterID);
		return (lResult == null || lResult.isJsonNull()) ? INT_ERROR : lResult.getAsInt();
	}
	/**
	 * Gets the daily rainfall 
	 * @return The daily rainfall
	 */ 
	public int getRrainfallDaily() {
		JsonElement lResult = getJsonElement("rainfall_daily");
		return (lResult == null || lResult.isJsonNull()) ? INT_ERROR : lResult.getAsInt();
	}
	/**
	 * Gets the monthly rainfall 
	 * @return The monthly rainfall
	 */
	public int getRainfallMonthly() {
		JsonElement lResult = getJsonElement("rainfall_monthly");
		return (lResult == null || lResult.isJsonNull()) ? INT_ERROR : lResult.getAsInt();
	}
	/**
	 * Gets the yearly rainfall 
	 * @return The Yearly Rainfall
	 */
	public int getRainfallYear() {
		JsonElement lResult = getJsonElement("rainfall_year");
		return (lResult == null || lResult.isJsonNull()) ? INT_ERROR : lResult.getAsInt();
	}
	/**
	 * Gets the total amount of rain during the last rain storm event 
	 * @return Total amount of rain during the last rain storm event
	 */
	public int getRainStormLast() {
		JsonElement lResult = getJsonElement("rain_storm_last");
		return (lResult == null || lResult.isJsonNull()) ? INT_ERROR : lResult.getAsInt();
	}
	/**
	 * Gets the UNIX time stamp in seconds indicating when the last rain
	 * storm started
	 * @return UNIX time stamp of when the last rain storm started
	 */
	public long getRainStormLastStartAtSeconds() {
		JsonElement lResult = getJsonElement("rain_storm_last_start_at");
		return (lResult == null || lResult.isJsonNull()) ? LONG_ERROR : lResult.getAsLong();
	}
	/**
	 * Gets the UNIX time stamp in seconds indicating when the last rain
	 * storm ended
	 * @return UNIX time stamp of when the last rain storm ended
	 */
	public long getRainStormLastEndAtSeconds() {
		JsonElement lResult = getJsonElement("rain_storm_last_end_at");
		return (lResult == null || lResult.isJsonNull()) ? LONG_ERROR : lResult.getAsLong();
	}
	/** 
	 * Gets the soil temperature for sensor slot 1
	 * @return The soil temperature
	 */
	public float getSoilTemp1() {
		JsonElement lResult = getJsonElement("temp_1");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/** 
	 * Gets the soil temperature for sensor slot 2
	 * @return The soil temperature
	 */
	public float getTemp_2() {
		JsonElement lResult = getJsonElement("temp_2");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/** 
	 * Gets the soil temperature for sensor slot 3
	 * @return The soil temperature
	 */
	public float getTemp_3() {
		JsonElement lResult = getJsonElement("temp_3");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/** 
	 * Gets the soil temperature for sensor slot 4
	 * @return The soil temperature
	 */
	public float getTemp_4() {
		JsonElement lResult = getJsonElement("temp_4");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the soil moisture for slot 1
	 * @return The Soil Moisture
	 */
	public float getMoistSoil1() {
		JsonElement lResult = getJsonElement("moist_soil_1");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the soil moisture for slot 2
	 * @return The Soil Moisture
	 */
	public float getMpistSoil2() {
		JsonElement lResult = getJsonElement("moist_soil_2");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the soil moisture for slot 3
	 * @return The Soil Moisture
	 */
	public float getMoistSoil3() {
		JsonElement lResult = getJsonElement("moist_soil_3");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the soil moisture for slot 4
	 * @return The Soil Moisture
	 */
	public float getMoistSoil4() {
		JsonElement lResult = getJsonElement("moist_soil_4");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the leaf wetness for slot 1
	 * @return The leaf wetness
	 */
	public float getWetleaf1() {
		JsonElement lResult = getJsonElement("wet_leaf_1");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the leaf wetness for slot 2
	 * @return The leaf wetness
	 */
	public float getWetleaf2() {
		JsonElement lResult = getJsonElement("wet_leaf_2");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the indoor temperature
	 * @return The indoor temperature
	 */
	public float getInTemp() { 
		JsonElement lResult = getJsonElement("temp_in");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the indoor humidity
	 * @return The indoor humidity
	 */
	public float getInHum() {
		JsonElement lResult = getJsonElement("hum_in");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the indoor dew point
	 * @return The indoor dew point
	 */
	public float getInDewPoint() {
		JsonElement lResult = getJsonElement("dew_point_in");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the indoor heat index
	 * @return The indoor heat index
	 */
	public float getInHeatIndex() {
		JsonElement lResult = getJsonElement("heat_index_in");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**	 
	 * Gets the barometer reading with elevation adjusted
	 * @return The barometer reading
	 */
	public float getBarSeaLevel() {
		JsonElement lResult = getJsonElement("bar_sea_level");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the current 3 hour barometer trend
	 * @return The barometer trend
	 */
	public float getBarTrend() {
		JsonElement lResult = getJsonElement("bar_trend");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	/**
	 * Gets the raw barometer reading
	 * @return the raw barometer reading
	 */
	public float getBarAbsolute() {
		JsonElement lResult = getJsonElement("bar_absolute");
		return (lResult == null || lResult.isJsonNull()) ? FLOAT_ERROR : lResult.getAsFloat();
	}
	
	/**
	 * Gets the date and time the last packet was received from the station interface
	 * @return 	The date and time the last packet was received from the station interface
	 */
	public GregorianCalendar getLastPacketReceiveTime() {
		return cLastPacketReceiveTime;
	}

	/**
	 * Gets the date and time the last packet was received from the station interface
	 * as a string suitable for mysql and maria db. The date format is according to 
	 * the format given by YYYY-MM-DD hh:mm:ss
	 * @return	String containing the date and time the last packet was received from the station interface
	 */
	public String getLastPacketReceiveTimeAsString() {
		
		String lRecTime = "";
		
		lRecTime = Integer.toString(cLastPacketReceiveTime.get(Calendar.YEAR)) + "-" +
				Integer.toString(cLastPacketReceiveTime.get(Calendar.MONTH)+1) + "-" +
				Integer.toString(cLastPacketReceiveTime.get(Calendar.DAY_OF_MONTH)) + " " +
				Integer.toString(cLastPacketReceiveTime.get(Calendar.HOUR_OF_DAY)) + ":" +
				Integer.toString(cLastPacketReceiveTime.get(Calendar.MINUTE)) + ":" +
				Integer.toString(cLastPacketReceiveTime.get(Calendar.SECOND));
		
		return lRecTime;
		
	}
	
	/**
	 * Gets the date and time the last packet was received from the station interface as 
	 * a JSON field suitable for concatenating to a JSON string.
	 * @param lastJsonField	True if the field will be the last field, or false to allow
	 * 						more fields to be appended. 
	 * @return	Part of a JSON string containing the date and time
	 */
	public String getLastPacketReceiveTimeAsJsonField(boolean lastJsonField) {
		
		String lJsonField = "";
		
		lJsonField = "," + "\"last_packet_rec_time\":" +
				"\"" + getLastPacketReceiveTimeAsString() + "\"" +
				((lastJsonField)?"}":"");
		
		return lJsonField;
		
	}
	
	/** Sets the date and time the last packet was received from the station interface
	 * @param lastPacketReceiveTime The date and time the last packet was received 
	 * 								from the station interface
	 */
	public void setLastPacketReceiveTime(GregorianCalendar lastPacketReceiveTime) {
		cLastPacketReceiveTime = lastPacketReceiveTime;
	}
	
	/**
	 * Gets the last packet response time in milliseconds as a JSON string suitable for
	 * concatenating to a JSON string. 
	 * @param lastJsonField		True if this field is the last
	 * @return					Portion of a JSON string containing last packet response time.
	 */
	public String getLastPacketResponseTimeAsJsonField(boolean lastJsonField) {
		
		String lJsonField = "";
		
		// Get the response time in milliseconds 
		int lLastPacketReponseTime = (int)(cHttpRequestResponseTime * 1000);
		
		lJsonField = "," + "\"prev_packet_latency\":" +
				"\"" + lLastPacketReponseTime + "\"" +
				((lastJsonField)?"}":"");
		
		return lJsonField;
		
	}
	
	/**
	 * If data was successfully received from the device then this will return true. If there were 
	 * connection issues with the device, then this function will return false.
	 * @return	True if connection successful, false if not.
	 */
	public boolean isConnected() {
		return cConnectionState;
	}
	
	// PRIVATE METHODS
	
	
	/**
	 * Forwards the JSON data retrieved from the local weather station to a HTTP host. Use the method setHttpHost() 
	 * to set the URL that will receive the JSON data.
	 * 
	 * @return	True if data was successfully sent, False if there were issues. Errors are logged in this
	 * 			object's error log.
	 */
	private boolean forwardToHttpHost() {
		
		String lResponseString = "";
		
		// Add the previous packet response time and packet receive time to the json string
		
		// Remove the closing bracket from the json string for adding more fields
		cObservationsJSONString = cObservationsJSONString.substring(0, cObservationsJSONString.length()-1);
		
		// Add the packet receive time
		cObservationsJSONString += getLastPacketReceiveTimeAsJsonField(false);
		
		// Add the last packet response time
		cObservationsJSONString += getLastPacketResponseTimeAsJsonField(true);
		
		HttpClient lClient = HttpClient.newBuilder()
				.version(Version.HTTP_1_1)
				.build();
		
		try {
			HttpRequest lRequest = HttpRequest.newBuilder()
					.uri(URI.create(cHttpHost + "/" + cHttpReceiveFile))
					.header("Content-Type", "application/json")
					.POST(BodyPublishers.ofString(cObservationsJSONString))
					.build();
		
			try {
				
				HttpResponse<String> lResponse = lClient.send(lRequest, HttpResponse.BodyHandlers.ofString());
				lResponseString = lResponse.body();
				
			} catch (IOException e) {
				logError(e.getMessage());
				return false;
			} catch (InterruptedException e) {
				logError(e.getMessage());
				return false;
			} catch (Exception e) {
				logError(e.getMessage());
				return false;
			}
		
		} catch (Exception e) {
			logError(e.getMessage());
			return false;
		}
		return true;
		
	}
	
	/**
	 * This function deals with reporting of performance issues with the Http App Server Host
	 * @param timeTaken	The response time in milliseconds from the http host
	 */
	private void reportPerformance(int timeTaken) {
		
		// Check if the response was way too slow
		if (timeTaken > cResponseTooLongThreshold) {
			
			// single response way too slow, resulting in an 
			// automatic degraded performance
			cDegradedPerformanceCount = cDegradedPerformanceTollerance; 
		}
		
		// If response is above threshold
		if (timeTaken > cDegradedPerformanceLatencyThreshold) {
			
			// If this has happened too many times and connection is not in a degraded state
			if (cDegradedPerformanceCount >= cDegradedPerformanceTollerance 
					&& !cDegradedPerformanceState) {
				
				// Send an email alert
					
				// Send email with timings
				LogFile.sendTimerLogAsEmail("Degraded Performance: App Server Response is slow!", "App Server Connectivity Poor");
			
				// Log an error that performance is degraded
				logError("Degraded Performance: App Server Response is slow!");
				
				// Set the degraded state to true
				cDegradedPerformanceState = true;						
			}
			
			// Increment the degraded performance
			if (cDegradedPerformanceCount<cDegradedPerformanceTollerance) {cDegradedPerformanceCount++;}
			
			// Log a message to report the degraded performance
			logMessage("Response from " + cHttpHost + " was " + timeTaken + " which is above the acceptable threshold of performance");
		
		} else {
			
			// If connection is in a degraded state and it has been a while since 
			// the connection was in a degraded state, set the state to false 
			// and report that the performance has improved
			if (cDegradedPerformanceState && cDegradedPerformanceCount==0) {
				
				// Report service back to normal
				logMessage("Degraded Performance Improved: Service is back to normal.", "App Server Connectivity Restored");
				
				// set degraded state back to false
				cDegradedPerformanceState = false;
			}
			
			// decrement the degraded count
			if (cDegradedPerformanceCount>0) {
				// Log a message to report improved performance
				if (cDegradedPerformanceState) {logMessage("Response from " + cHttpHost + " was " + timeTaken + " which is improving");}
				cDegradedPerformanceCount--;
			}
		}
		
	}
	
	private boolean getObservations() {
		
		HttpClient lClient = HttpClient.newBuilder()
				.version(Version.HTTP_1_1)
				.build();
		
		try {
			HttpRequest lRequest = HttpRequest.newBuilder()
				.uri(URI.create("HTTP://" + cDeviceIP + ":" + cPort + cRequestString))
				.build();
		
			try {
				HttpResponse<String> lResponse = lClient.send(lRequest, HttpResponse.BodyHandlers.ofString());
				cObservationsJSONString = lResponse.body();
				
				// Connection was successful
				cConnectionState = true;
				// Record the time the packet was received
				setLastPacketReceiveTime(new GregorianCalendar());
				
			} catch (IOException e) {
				logError(e.getMessage());
				cConnectionState = false;
				return false;
			} catch (InterruptedException e) {
				logError(e.getMessage());
				cConnectionState = false;
				return false;
			}
		
		} catch (Exception e) {
			logError(e.getMessage());
			return false;
		}
		
		return true;
	}
	
	/**
	 * Captures live UDP packets from the WeatherLink live appliance and obtains the JSON data
	 * structure from these packets and saves it for deserialization.
	 * @return	True if packets were found, otherwise false if there were network related errors
	 */
	private boolean getLiveObservations() {
		
		byte lBuf[] = new byte[cLiveBufSize];
		
		// Get the current time
		GregorianCalendar lNow = new GregorianCalendar();
		
		// Do a full update of all sensors if the refresh frequency has elapsed
		if ((lNow.getTimeInMillis()-cStartTime.getTimeInMillis())>=cRefreshFrequency) {
			if (!refresh()) {return false;}
			
			// Reset the time
			cStartTime = new GregorianCalendar();
		
		} else {
		
			// Set up the live UDP receiving socket
			DatagramSocket lSocket;
			try {
				lSocket = new DatagramSocket(cLiveReceivingPort);
				
				// Set the timeout
				lSocket.setSoTimeout(cLiveTimeout);
				
				// Create the packet
				DatagramPacket lPacket = new DatagramPacket(lBuf, lBuf.length);		
				
				// Capture the packet
				try {
					
					lSocket.receive(lPacket);
				
					// Put the data into the buffer.
					lBuf = lPacket.getData();
					
					// Close the socket, we're done. 
					lSocket.close();
					
					// Convert the bytes to get the string containing the JSON
					String lLiveData = new String(lBuf).trim();	
	
					// assign the retrieved data to the class' JSON string
					// also add {data: at the start and finish with }. For some stupid
					// reason, the UDP packets don't have this. 
					
					cObservationsJSONString = "{\"data\":" + lLiveData + "}";
					
					// Record the time the packet was received
					setLastPacketReceiveTime(new GregorianCalendar());
					
				} catch (Exception e) {
					logError(e.getMessage());
					lSocket.close();
					return false;
				}
				
			} catch (SocketException e) {
				logError(e.getMessage());
				return false;
			} 
		}
		return true;
	}
	
	/**
	 * Sends a HTTP request to the Weatherlink Live device and returns the Google GSON JsonObject with the 
	 * response from the Weatherlink Live device. Do not include the host/IP or the protocol portion of 
	 * the string. 
	 * @param 	requestString	The request to send. e.g. /v1/real_time or /v1/current_conditions
	 * @return	A GSON JsonObject containing the response from the Weatherlink Live device. Will return a 
	 * 			null object if there were errors.
	 */
	
	private JsonObject sendHTTPrequest(String requestString) {
		
		// Google GSON object
		Gson lgson = new Gson();
		
		// A Gson Json Object, initially empty or null
		JsonObject lJsonObject = null;
		
		// The response string from the device
		String lResponseString = "";
		
		// The http client object used to access the HTTP interface
		HttpClient lClient = HttpClient.newBuilder()
				.version(Version.HTTP_1_1)
				.build();
		
		try {
			// The http request object containing the request string
			HttpRequest lRequest = HttpRequest.newBuilder()
					.uri(URI.create("HTTP://" + cDeviceIP + ":" + cPort + requestString))
					.build();
			
			try {
				HttpResponse<String> lResponse = lClient.send(lRequest, HttpResponse.BodyHandlers.ofString());
				lResponseString = lResponse.body();
				
				// try and convert the request string into a JSON object
				try {
					// De-serialize the JSON into a GSON JSON object to work with later on
					lJsonObject = lgson.fromJson(lResponseString, JsonObject.class);
				} catch (Exception e) {
					logError(e.getMessage());
					return lJsonObject;
				}
				
			} catch (IOException e) {
				logError(e.getMessage());
				return lJsonObject;
			} catch (InterruptedException e) {
				logError(e.getMessage());
				return lJsonObject;
			}
		} catch (Exception e) {
			logError(e.getMessage());
		}
			
		return lJsonObject;
	}
	
	/**
	 * Requests the device to start sending the live broadcast packets out onto the LAN. 
	 * 
	 * @return	True if broadcast could be started, false if not.
	 */
	private boolean startLiveBroadcast() {
		
		// JsonObject returned by device
		JsonObject lDeviceResponse;
		
		// Send the request to the device
		lDeviceResponse = sendHTTPrequest(cBroadcastRequestString + "?duration=" + cBroadcastDuration);
		
		// Check whether a response is received from the device
		if (lDeviceResponse == null) {
			logError("No response from device. " + lastError());
			return false;
		}
		
		// try and get the receiving port number from the device
		try {
			cLiveReceivingPort = lDeviceResponse.getAsJsonObject("data").get("broadcast_port").getAsInt();
		} catch (Exception e) {
			
			// try and check if an error is returned in the JSON object
			String lErrorMessage = lDeviceResponse.get("error").getAsString();
			
			// Could not find an error, so the expected structure of the JSON object is out of date
			if (lErrorMessage == "null" || lErrorMessage.length() == 0 || lErrorMessage == null) {
				logError("JSON Structure unexpected. Software update may need to be required");
				return false;
			} else {
				
				// Log the error message from the device and return false
				logError(lErrorMessage);
				return false;
			}
		}
		
		// Try and get the broadcast duration from the device
		try {
			cActualBroadcastDuration = lDeviceResponse.getAsJsonObject("data").get("duration").getAsInt();
		} catch (Exception e) {
			String lErrorMessage = lDeviceResponse.get("error").getAsString();
			if (lErrorMessage == "null" || lErrorMessage.length() == 0 || lErrorMessage == null) {
				logError("JSON Structure unexpected. Software update may need to be required");
				return false;
			} else {
				logError(lErrorMessage);
				return false;
			}
		}
				
		return true;
	}
	
	/**
	 * Uses the Google GSON library to deserialize the JSON object containing the weather data
	 * @return	True if there were no issues, otherwise false with errors being logged
	 */
	private Boolean deserializeJSON() {
		
		Gson lgson = new Gson();
		JsonObject lJsonObject;
		
		try {
			// De-serialize the JSON into a GSON JSON object to work with later on
			lJsonObject = lgson.fromJson(cObservationsJSONString, JsonObject.class);
		} catch (Exception e) {
			logError(e.getMessage());
			return false;
		}
		
		try {
			cConditions = lJsonObject.getAsJsonObject("data").getAsJsonArray("conditions"); 			
		} catch (Exception e) {
			logError("JSON object invalid or schema changed. " + e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * Returns the JsonElement given the specified name
	 * @param 	elementName		The name of the Json Element
	 * @return	null If no element was found, or the element if found
	 */
	private JsonElement getJsonElement(String elementName) {
		
		JsonElement lResult = null;

		for (int i = 0; i < cConditions.size(); i++) {
			
			lResult = cConditions.get(i).getAsJsonObject().get(elementName);
			if (lResult != null) {
				return lResult;
			}
		}
		
		return lResult;
	}
	
	/**
	 * Returns the JsonElement given the specified name and target transmitter ID
	 * @param 	elementName		The name of the Json Element
	 * @param	transmitterID	The ID of the transmitter.
	 * @return	null If no element was found, or the element if found
	 */
	private JsonElement getJsonElement(String elementName, int transmitterID) {
		
		JsonElement lResult = null;

		for (int i = 0; i < cConditions.size(); i++) {
			if (transmitterID==cConditions.get(i).getAsJsonObject().get("txid").getAsInt()) {
				lResult = cConditions.get(i).getAsJsonObject().get(elementName);
				if (lResult != null) {
					return lResult;
				}
			}
		}
		
		return lResult;
	}
	
	// PRIVATE ATTRIBUTES
	
	// The IP address of the device
		private String cDeviceIP = "";
		
		// The port to access the device, default is HTTP which is 80
		private int cPort = 80;
		
		// The HTTP request string used to get the data
		private String cRequestString;
		
		// The HTTP request used to request broadcast packets
		private String cBroadcastRequestString;
		
		// The broadcast duration in hours
		private int cBroadcastDuration;
		
		// The duration that the device returns may not be the same as the requested duration
		private int cActualBroadcastDuration;
		
		private int cBroadcastPort;
		
		// The size of the expected packet from the WeatherLink Live device
		private int cLiveBufSize;
		
		// The port number to listed for the WeatherLink Live packets
		private int cLiveReceivingPort;
		
		// The timeout in milliseconds to stop waiting for UDP packets
		private int cLiveTimeout;
		
		// The web host to which to send json packets to
		private String cHttpHost;
		
		// The interface that receives the data
		private String cHttpReceiveFile;
		
		// Provides error logging capabilities
		public Common LogFile;
		
		// The observations from the weather station
		private String cObservationsJSONString;
		
		// The deserialized GSON JSON object
		private JsonObject cMain, cSoilConditions, cIndoorConditions, cBarometer;
	
		// The timestamp of when the live refresh started
		private GregorianCalendar cStartTime;
		
		// The time the last packet was received from the station interface
		private GregorianCalendar cLastPacketReceiveTime;
		
		// The frequency of full refreshes since start of live refresh
		private int cRefreshFrequency;
		
		// The time taken forwarding JSON to server via http
		double cHttpRequestResponseTime;
		
		// The amount of milliseconds of http host response that is acceptable
		private int cDegradedPerformanceLatencyThreshold;
		
		// The amount of time in milliseconds considered too slow resulting in
		// an immediate error condition. 
		private int cResponseTooLongThreshold;
		
		/**
		 * The amount of times in a row that the http host response is allowed to
		 * be above the threshold
		 */
		private int cDegradedPerformanceTollerance;
		
		// The amount of times in a row that the http host response was slower 
		// than the acceptable threshold
		private int cDegradedPerformanceCount;
		
		// This is set to true whenever the http host or internet connection is slow
		private boolean cDegradedPerformanceState;
		
		// The JSon Array containing the conditions - very annoying!
		JsonArray cConditions;
		
		// The config file used to initialise this class
		private ConfigFile cConfiguration;
		
		// False if connection issues
		private boolean cConnectionState;
}
