/**
 * The WeatherlinkLiveObservations class extends the Observations class with the aim of dealing 
 * with JSON deserialization and serialization (future development). Using the constructor with 
 * this class with a JSON string as a parameter will cause the class to deserialize the data and 
 * store it in all of the observation variables. 
 * 
 * @author Anthony Gibbons
 *
 */
package cruxarina.weatherlink;
import cruxarina.weatherlink.*;

public class WeatherlinkLiveObservations extends Observations {

	// CONSTRUCTORS
	
	public WeatherlinkLiveObservations() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Deserializes the JSON and stores into this observations object. 
	 * @param JSON String containing JSON
	 */
	public WeatherlinkLiveObservations(String JSON) {
		
	}
	
	
	
	// PUBLIC METHODS
	
	/**
	 * Sets the JSON string containing source observations data for deserialization
	 * @param JSON	String containing JSON
	 */
	public void setJSON(String JSON) {
		cJSON = JSON;
		StationInterface lInterface = null;
		
		lInterface.getTemp();
	}
	
	/**
	 * Gets the JSON string containing source observations data for deserialization
	 * @return	JSON string. 
	 */
	public String getJSON() {
		return cJSON;
	}
	
	// PRIVATE METHODS
	
	
	
	// PRIVATE ATTRIBUTES
	
	private String cJSON;
	
	
}
