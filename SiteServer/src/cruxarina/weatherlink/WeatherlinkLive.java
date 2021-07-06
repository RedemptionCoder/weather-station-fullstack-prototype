package cruxarina.weatherlink;
import java.util.ArrayList;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.net.http.HttpClient.Version;
import com.google.gson.*;

public class WeatherlinkLive {
	
	/**
	 * Default constructor. Sets port to 80, and HTTP request to "/v1/current_conditions" 
	 */
	public WeatherlinkLive() {
		
		cDeviceIP = "";
		setPort(80);
		setRequestString("/v1/current_conditions");
		cErrorLog = new ArrayList<String>();
		cMessages = new ArrayList<String>();
		cLastError = "";
		cLastMessage = "";
	}
	
	// Getter and Setters -----------------------------------------------------------------------------------
	
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
	
	// Error Logging - need a new base class for this ---------------------------------------------------------
	
	

	/**
	 * Adds an error message to the class' error log
	 * @param Message	A string containing an error message
	 */
	public void logError(String Message) {
		cErrorLog.add(Message);
		cLastError = Message;
	}
	
	/**
	 * Adds a message to the class' message log
	 * @param Message	A string containing an message
	 */
	public void logMessage(String Message) {
		cMessages.add(Message);
		cLastMessage = Message;
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
	
	// --------------------------------------------------------------------------------------------------------
	
	
	// Public Methods -----------------------------------------------------------------------------------------
	
	/**
	 * Gets a summarized list of current observations
	 * @return A string containing a summarized list of observations
	 */
	public String getObservationSummary() {
		
		if (getObservations()) {
			return deserializeJSON();
		} else {
			return "Failed to get data from the device";
		}
	}
	
	// Private Methods ---------------------------------------------------------------------------------------
	
	
	private boolean getObservations() {
			
		HttpClient lClient = HttpClient.newBuilder()
				.version(Version.HTTP_1_1)
				.build();
		
		HttpRequest lRequest = HttpRequest.newBuilder()
				.uri(URI.create("HTTP://" + cDeviceIP + ":" + cPort + cRequestString))
				.build();
		
		try {
			HttpResponse<String> lResponse = lClient.send(lRequest, HttpResponse.BodyHandlers.ofString());
			cObservationsJSONString = lResponse.body();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private String deserializeJSON() {
		
		Gson lgson = new Gson();
		
		// Get temp from live
		JsonObject lObs = lgson.fromJson(cObservationsJSONString, JsonObject.class);
		
		return "Current Speed: " + (lObs.get("wind_speed_last")) + 
				"km/h, 2min average: " + (lObs.get("wind_speed_avg_last_2_min")) + 
				"km/h, current temp: " + (lObs.get("temp")) + "\u00B0C";	
		
		
		//return "Current Speed: " + (lObs.get("wind_speed_last").getAsFloat()  * (float)1.6) + 
		//		"km/h, 2min average: " + (lObs.get("wind_speed_avg_last_2_min").getAsFloat() * (float)1.6 ) + 
		//		"km/h, current temp: " + (((lObs.get("temp").getAsFloat() - 32) * 5/9)) + "\u00B0C";	
		
	}
	
	
	// Private Attributes  --------------------------------------------------------------------------------
	
	// The IP address of the device
	private String cDeviceIP = "";
	
	// The port to access the device, default is HTTP which is 80
	private int cPort = 80;
	
	// The HTTP request string used to get the data
	private String cRequestString;

	// The list of errors that may have occurred
	private ArrayList<String> cErrorLog;
	
	// The last error logged
	private String cLastError;
	
	// The list of messages that may have been generated
	private ArrayList<String> cMessages;
	
	// The last message that was logged
	private String cLastMessage;
	
	// The observations from the weather station
	private String cObservationsJSONString;
	
	
}
