package cruxarina.weatherlink;

import java.io.File;
import java.util.Scanner;

/**
 * This class enables reading in of configuration information from a configuration text file. 
 * The setting/property/field name is the first string on a line, then followed by the 
 * value of the setting separated by either a space, tab or equals (=) sign. Lines beginning
 * with a hash (#) will be treated as a comment and therefore ignored.
 *  
 * @author Anthony Gibbons
 *
 */
public class ConfigFile {

	// Constructors
	
	public ConfigFile(String ConfigFileName) {
	
		this.cConfigfileName = ConfigFileName;
		this.cConfigAccessible = false;
		
		try {

			// Open the config file
			cConfigFile = new File(cConfigfileName);
		
			// Check if config file is accessible
			if (cConfigFile.canRead()) {cConfigAccessible = true;}			
		
		} catch (Exception e) {}
	
	}

	// Getters and Setters
	
	/**
	 * Gets the path to the config file
	 * @return	String containing the path to the config file
	 */
	public String getFilename() { 
		return this.cConfigfileName;
	}
	
	/**
	 * Sets the path to the config file
	 * @param filename	The path and filename of the config file
	 */
	public void setFilename(String filename) {
		this.cConfigfileName = filename;
	}
	
	// Public Methods
	
	/**
	 * Gets the value of the supplied field/property from the configuration file
	 * @param PropertyName	The name of the field/setting/property to look for 
	 * @return				The value of the property if it exists, otherwise nothing. 
	 * 						Will return nothing if the configuration file could not 
	 * 						be found
	 */
	public String getConfig(String PropertyName) {
		
		String lFileLine = "";
		String lValue = "";
		
		// Config file is case insensitive
		PropertyName = PropertyName.toLowerCase();
		
		try {

			// Set up the buffered leader for reading lines of text
			Scanner lLineReader = new Scanner(cConfigFile);
			
			// Read through the file
			while (lLineReader.hasNextLine()) {
				
				// Get a line from the config file
				lFileLine = lLineReader.nextLine();
				
				// Remove any unwanted whitespace
				lFileLine = lFileLine.trim();
				
				// Make sure the line actually has text
				if (lFileLine.length() > 0) {
				
					// Make sure the first character isn't a hash (#) symbol indicating that this line is a comment
					if (lFileLine.charAt(0)!='#') {
						
						// Check if this line has the required property
						if (lFileLine.toLowerCase().startsWith(PropertyName)) {
							
							// Split the line using the required delimiters of space, tab, or equal sign
							String [] lLineElements = lFileLine.split("=", 2);
							
							if (lLineElements.length == 1) {
								
								// Split the line using the required delimiters of space, tab, or equal sign
								lLineElements = lFileLine.split("[\\s\\t=]", 2);
								
							}
							
							// Check that the first element contains exactly the required setting
							// and that setting is not a partial string
							if (lLineElements[0].trim().length()==PropertyName.length()) {
								
								// Make sure that the setting name isn't the only thing
								if (lLineElements.length > 1) {
									
									// Assign the property value
									lValue = lLineElements[1].trim(); 
									
									// Close the line reader
									lLineReader.close();
									
									// Return the value found
									return lValue;
								}
							}
						}
					}
				}
			}
			
			lLineReader.close();
			
		} catch (Exception e) {
			
		}
		
		// Return the value
		return lValue;
	}
	
	/**
	 * Checks if the config file is accessible or not
	 * @return	True if config file is accessible, false if not
	 */
	public boolean isAccessible() {
		return cConfigAccessible;
	}
	
	// PRIVATE ATTRIBUTES
	
	// The path to the config file
	protected String cConfigfileName;
	
	// This will be false if the config file is not accessible
	protected boolean cConfigAccessible; 
	
	// The config file
	private File cConfigFile;
	
}
