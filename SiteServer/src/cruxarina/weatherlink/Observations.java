/**
 * Observations is a class that contains all of the weather observations data gathered from a sensor or 
 * sensor suite of a weather station. 
 * <p> 
 * Data can be set using the getter and setter functions. Before setting observations data, make sure that
 * the default units are set to match the units of the source data. When changing the default units, the 
 * class performs conversions on the observations data automatically, so the output from the getter functions
 * will match the default units set. 
 * <p>
 * Use the toString() method to output the observations data as a formatted string. 
 * 
 * @author Anthony Gibbons (BEngSoft MIEAust) Cruxarina Solutions
 */
package cruxarina.weatherlink;

import java.util.*;

import cruxarina.weatherlink.Observations.Unit_Barometer;
import cruxarina.weatherlink.Observations.Unit_Rain;
import cruxarina.weatherlink.Observations.Unit_Temp;
import cruxarina.weatherlink.Observations.Unit_WindDirection;
import cruxarina.weatherlink.Observations.Unit_WindSpeed;

import java.text.*;
import java.lang.Math;

public class Observations {

	// Constants
	
		public static final float NO_VALUE = -999;
		public static final String NO_VALUE_TO_STRING = "---";
		public static final float FLOAT_ERROR = (float)-9999.9;
		public static final int INT_ERROR = -9999;
		public static final long LONG_ERROR = -9999;
	
	// Default Constructor
	
	public Observations() {
		cDateFormat = "d MMM yyyy h:mm a";
		cDefTempUnit = Unit_Temp.FAHRENHEIT;
		cDefWindSpUnit = Unit_WindSpeed.MPH;
		cDefWindDirUnit = Unit_WindDirection.DEGREES;
		cDefBarUnit = Unit_Barometer.INCHES;
		cDefRainUnit = Unit_Rain.COUNTS;
		cRainSizeDefUnit = Unit_Rain.MM;
		
		// Set all of the default output units
		setOutputTempUnit(Unit_Temp.CELSIUS);
		setOutputBarUnit(Unit_Barometer.HPA);
		setOutputRainUnit(Unit_Rain.MM);
		setOutputWindDirUnit(Unit_WindDirection.CARDINAL);
		setOutputWindSpUnit(Unit_WindSpeed.KPH);
		
		// Units coming in from the sensor default
		setInputTempUnit(Unit_Temp.FAHRENHEIT);
		setInputBarUnit(Unit_Barometer.INCHES);
		setInputRainUnit(Unit_Rain.COUNTS);
		setInputWindDirUnit(Unit_WindDirection.DEGREES);
		setInputWindSpUnit(Unit_WindSpeed.MPH);
		
		// Set all of the observation values to an 'empty value' 
		// 0 doesn't make sense as 0 is legitimate as a weather observation.
		// null causes errors.
		
		cLsid = LONG_ERROR;                      		
		cDataStructureType = INT_ERROR;                 
		cTransmitterID = INT_ERROR;                     
		cTemp = FLOAT_ERROR;                            
		cHum = FLOAT_ERROR;                             
		cDewPoint = FLOAT_ERROR;                        
		cWetBulb = FLOAT_ERROR;                         
		cHeatIndex = FLOAT_ERROR;                       
		cWindChill = FLOAT_ERROR;                       
		cThwIndex = FLOAT_ERROR;                        
		cThswIndex = FLOAT_ERROR;                       
		cWindPpeedLast = FLOAT_ERROR;                   
		cWindDirLast = FLOAT_ERROR;                     
		cWindSpeedAvgLast_1_min = FLOAT_ERROR;          
		cWindDirScalarAvg_last_1_min = FLOAT_ERROR;     
		cWindSpeedAvgLast_2_min = FLOAT_ERROR;          
		cWindDirScalarAvg_last_2_min = FLOAT_ERROR;     
		cWindSpeedHi_last_2_min = FLOAT_ERROR;          
		cWindDirAtHiSpeedLast_2_min = FLOAT_ERROR;      
		cWindSpeedAvgLast_10_min = FLOAT_ERROR;         
		cWindDirScalarAvgLast_10_min = FLOAT_ERROR;     
		cWindSpeedHiLast_10_min = FLOAT_ERROR;          
		cWindDirAtHiSpeedLast_10_min = FLOAT_ERROR;     
		cRainSize = FLOAT_ERROR;                        
		cRainRateLast = FLOAT_ERROR;                    
		cRainRateHi = FLOAT_ERROR;                      
		cRainfallLast_15_min = FLOAT_ERROR;             
		cRainRateHiLast_15_min = FLOAT_ERROR;           
		cRainfallLast_60_min = FLOAT_ERROR;             
		cRainfallLast_24_hr = FLOAT_ERROR;              
		cRainStorm = FLOAT_ERROR;                       
		cRainStormStartAt = INT_ERROR;                 
		cSolarRad = FLOAT_ERROR;                        
		cUvIndex = FLOAT_ERROR;                         
		cRxState = INT_ERROR;                           
		cTransBatteryFlag = INT_ERROR;                  
		cRrainfallDaily = FLOAT_ERROR;                  
		cRainfallMonthly = FLOAT_ERROR;                 
		cRainfallYear = FLOAT_ERROR;                    
		cRainStormLast = FLOAT_ERROR;                   
		cRainStormLastStartAt = INT_ERROR;             
		cRainStormLastEndAt = INT_ERROR;               
		cSoilTemp1 = FLOAT_ERROR;                       
		cTemp_3 = FLOAT_ERROR;                          
		cTemp_4 = FLOAT_ERROR;                          
		cTemp_2 = FLOAT_ERROR;                          
		cMoistSoil1 = FLOAT_ERROR;                      
		cMpistSoil2 = FLOAT_ERROR;                      
		cMoistSoil3 = FLOAT_ERROR;                      
		cMoistSoil4 = FLOAT_ERROR;                      
		cWetleaf1 = FLOAT_ERROR;                        
		cWetleaf2 = FLOAT_ERROR;                        
		cInTemp = FLOAT_ERROR;                          
		cInHum = FLOAT_ERROR;                           
		cInDewPoint = FLOAT_ERROR;                      
		cInHeatIndex = FLOAT_ERROR;                     
		cBarSeaLevel = FLOAT_ERROR;                     
		cBarTrend = FLOAT_ERROR;                        
		cBarAbsolute = FLOAT_ERROR;                     
		
	}
	
	/**
	 * Loads the weather unit settings from the supplied configuration file
	 * @param config	The configuration file
	 */
	Observations(ConfigFile config) {
		this();
		
		// Set the input and output units from config file
		
		String lOutputTempUnit = config.getConfig("OutputTempUnit");
		String lOutputBarUnit = config.getConfig("OutputBarometerUnit");
		String lOutputRainUnit = config.getConfig("OutputRainUnit");
		String lOutputWindDirUnit = config.getConfig("OutputWindDirUnit");
		String lOutputWindSpUnit = config.getConfig("OutputWindSpeedUnit");
		
		if (lOutputTempUnit.length() > 0) {setOutputTempUnit(getTempUnitFromString(lOutputTempUnit));}
		if (lOutputBarUnit.length() > 0) {setOutputBarUnit(getBarometerUnitFromString(lOutputBarUnit));}
		if (lOutputRainUnit.length() > 0) {setOutputRainUnit(getRainUnitFromString(lOutputRainUnit));}
		if (lOutputWindDirUnit.length() > 0) {setOutputWindDirUnit(getWindDirectionUnitFromString(lOutputWindDirUnit));}
		if (lOutputWindSpUnit.length() > 0) {setOutputWindSpUnit(getWindSpeedUnitFromString(lOutputWindSpUnit));}	
		
		String lInputTempUnit = config.getConfig("InputTempUnit");
		String lInputBarUnit = config.getConfig("InputBarometerUnit");
		String lInputRainUnit = config.getConfig("InputRainUnit");
		String lInputWindDirUnit = config.getConfig("InputWindDirUnit");
		String lInputWindSpUnit = config.getConfig("InputWindSpeedUnit");
		
		if (lInputTempUnit.length() > 0) {setInputTempUnit(getTempUnitFromString(lInputTempUnit));}
		if (lInputBarUnit.length() > 0) {setInputBarUnit(getBarometerUnitFromString(lInputBarUnit));}
		if (lInputRainUnit.length() > 0) {setInputRainUnit(getRainUnitFromString(lInputRainUnit));}
		if (lInputWindDirUnit.length() > 0) {setInputWindDirUnit(getWindDirectionUnitFromString(lInputWindDirUnit));}
		if (lInputWindSpUnit.length() > 0) {setInputWindSpUnit(getWindSpeedUnitFromString(lInputWindSpUnit));}	
		
	}
	
	// Copy constructor
	Observations(Observations source) {
		cDataStructureType = source.cDataStructureType;                 
		cTransmitterID = source.cTransmitterID;                     
		cTemp = source.cTemp;                            
		cHum = source.cHum;                             
		cDewPoint = source.cDewPoint;                        
		cWetBulb = source.cWetBulb;                         
		cHeatIndex = source.cHeatIndex;                       
		cWindChill = source.cWindChill;                       
		cThwIndex = source.cThwIndex;                        
		cThswIndex = source.cThswIndex;                       
		cWindPpeedLast = source.cWindPpeedLast;                   
		cWindDirLast = source.cWindDirLast;                     
		cWindSpeedAvgLast_1_min = source.cWindSpeedAvgLast_1_min;          
		cWindDirScalarAvg_last_1_min = source.cWindDirScalarAvg_last_1_min;     
		cWindSpeedAvgLast_2_min = source.cWindSpeedAvgLast_2_min;          
		cWindDirScalarAvg_last_2_min = source.cWindDirScalarAvg_last_2_min;     
		cWindSpeedHi_last_2_min = source.cWindSpeedHi_last_2_min;          
		cWindDirAtHiSpeedLast_2_min = source.cWindDirAtHiSpeedLast_2_min;      
		cWindSpeedAvgLast_10_min = source.cWindSpeedAvgLast_10_min;         
		cWindDirScalarAvgLast_10_min = source.cWindDirScalarAvgLast_10_min;     
		cWindSpeedHiLast_10_min = source.cWindSpeedHiLast_10_min;          
		cWindDirAtHiSpeedLast_10_min = source.cWindDirAtHiSpeedLast_10_min;     
		cRainSize = source.cRainSize;                        
		cRainRateLast = source.cRainRateLast;                    
		cRainRateHi = source.cRainRateHi;                      
		cRainfallLast_15_min = source.cRainfallLast_15_min;             
		cRainRateHiLast_15_min = source.cRainRateHiLast_15_min;           
		cRainfallLast_60_min = source.cRainfallLast_60_min;             
		cRainfallLast_24_hr = source.cRainfallLast_24_hr;              
		cRainStorm = source.cRainStorm;                       
		cRainStormStartAt = source.cRainStormStartAt;                 
		cSolarRad = source.cSolarRad;                        
		cUvIndex = source.cUvIndex;                         
		cRxState = source.cRxState;                           
		cTransBatteryFlag = source.cTransBatteryFlag;                  
		cRrainfallDaily = source.cRrainfallDaily;                  
		cRainfallMonthly = source.cRainfallMonthly;                 
		cRainfallYear = source.cRainfallYear;                    
		cRainStormLast = source.cRainStormLast;                   
		cRainStormLastStartAt = source.cRainStormLastStartAt;             
		cRainStormLastEndAt = source.cRainStormLastEndAt;               
		cSoilTemp1 = source.cSoilTemp1;                       
		cTemp_3 = source.cTemp_3;                          
		cTemp_4 = source.cTemp_4;                          
		cTemp_2 = source.cTemp_2;                          
		cMoistSoil1 = source.cMoistSoil1;                      
		cMpistSoil2 = source.cMpistSoil2;                      
		cMoistSoil3 = source.cMoistSoil3;                      
		cMoistSoil4 = source.cMoistSoil4;                      
		cWetleaf1 = source.cWetleaf1;                        
		cWetleaf2 = source.cWetleaf2;                        
		cInTemp = source.cInTemp;                          
		cInHum = source.cInHum;                           
		cInDewPoint = source.cInDewPoint;                      
		cInHeatIndex = source.cInHeatIndex;                     
		cBarSeaLevel = source.cBarSeaLevel;                     
		cBarTrend = source.cBarTrend;                        
		cBarAbsolute = source.cBarAbsolute;  
		cDateFormat = source.cDateFormat;
		cDefTempUnit = source.cDefTempUnit;
		cDefWindSpUnit = source.cDefWindSpUnit;
		cDefWindDirUnit = source.cDefWindDirUnit;
		cDefBarUnit = source.cDefBarUnit;
		cDefRainUnit = source.cDefRainUnit;
		cRainSizeDefUnit = source.cRainSizeDefUnit;
	}
	
	// Enums --------------------------------------------------------------------------------------------------------------------------
	
	// Used for converting between units
	 public enum Unit_Temp {
		CELSIUS,
		FAHRENHEIT
	}
	
	// Used for converting between units
	public enum Unit_WindSpeed {
		KPH,
		MPH,
		KNOTS,
		MPS
	}
	
	// Used for converting between units
	public enum Unit_WindDirection {
		DEGREES,
		CARDINAL
	}
	
	// Used for converting between units
	public enum Unit_Barometer {
		INCHES,
		MILIMETERS,
		MB,
		HPA
	}
	
	// Used for converting between units
	public enum Unit_Rain {
		IN,
		MM,
		COUNTS
	}
	
	// Used for converting between units
	public enum Unit_Elevation {
		FEET,
		METERS
	}
	
	// Used for toString() functions to select which data to output
	public enum OUTPUT_STRING {
		ESSENTIAL,
		WIND,
		RAIN,
		ALL,
		DATAFILE
	}
	
	// Observation data getters and setters -----------------------------------------------------------------------------------------
	
	/**
	 * Gets the sensor ID
	 * @return The sensor ID
	 */
	public long getLsid() {
		return cLsid;
	}
	/**
	 * Sets the sensor ID
	 * @param lsid	The sensor ID
	 */
	public void setLsid(long lsid) {
		cLsid = lsid;
	}
	/** 
	 * Gets the data structure type, which is specific to groups of sensors.
	 * @return The Data Structure Type
	 */
	public int getDataStructureType() {
		return cDataStructureType;
	}	
	/**
	 * Sets the data structure type. 
	 * @param dataStructureType The Data Structure Type
	 */
	public void setDataStructureType(int dataStructureType) {
		cDataStructureType = dataStructureType;
	}
	/**
	 * Gets the ID of a sensor suite 
	 * @return The Transmitter ID
	 */
	public int getTransmitterID() {
		return cTransmitterID;
	}
	/**
	 * Sets the ID of a sensor suite 
	 * @param transmitterID The Transmitter ID to set
	 */
	public void setTransmitterID(int transmitterID) {
		cTransmitterID = transmitterID;
	}
		/**
	 * Gets the outside temperature
	 * @return The outside temperature
	 */
	public float getTemp() {
		return convertTemp(cTemp, cInputTempUnit, cOutputTempUnit);
	}
	/**
	 * Sets the outside temperature
	 * @param temp The value of the outside temperature to set
	 */
	public void setTemp(float temp) {
		cTemp = temp;
	}
	/**
	 * Gets the apparent feels like temperature according to  the Australian Bureau of Meteorology citing  
	 * Norms of apparent temperature in Australia, Aust. Met. Mag., 1994, Vol 43, 1-161.
	 * @return	The apparent temperature
	 */
	public float getAppTemp() {
		return this.convertTemp((float)(calcAppTemp(this.convertTemp(cTemp, cInputTempUnit, Unit_Temp.CELSIUS), cHum, this.convertWindSpeed(this.cWindSpeedAvgLast_10_min, cInputWindSpUnit, Unit_WindSpeed.MPS))), Unit_Temp.CELSIUS, cOutputTempUnit);
	}
	/**
	 * Gets the humidity as percentage
	 * @return The humidity
	 */
	public float getHum() {
		return cHum;
	}
	/**
	 * Sets the humidity as a percentage
	 * @param hum The humidity as percentage
	 */
	public void setHum(float hum) {
		cHum = hum;
	}
	/**
	 * Gets the Dew point 
	 * @return The Dew Point
	 */
	public float getDewPoint() {
		return convertTemp(cDewPoint, cInputTempUnit, cOutputTempUnit);
	}
	/** 
	 * Sets the Dew Point 
	 * @param dewPoint The Dew Point to set
	 */
	public void setDewPoint(float dewPoint) {
		cDewPoint = dewPoint;
	}
	/**
	 * Gets the wet bulb temperature 
	 * @return 	The Wet Bulb temperature
	 */
	public float getWetBulb() {
		return convertTemp(cWetBulb, cInputTempUnit, cOutputTempUnit);
	}
	/**
	 * Sets the wet bulb temperature 
	 * @param wetBulb The Wet Bulb temperature to set
	 */
	public void setWetBulb(float wetBulb) {
		cWetBulb = wetBulb;
	}
	/**
	 * Gets the heat index 
	 * @return The Heat Index
	 */
	public float getHeatIndex() {
		return convertTemp(cHeatIndex, cInputTempUnit, cOutputTempUnit);
	}
	/**
	 * Sets the heat index
	 * @param heatIndex The Heat Index to set
	 */
	public void setHeatIndex(float heatIndex) {
		cHeatIndex = heatIndex;
	}
	/** 
	 * Gets the wind chill temperature 
	 * @return The Wind Chill temperature
	 */
	public float getWindChill() {
		return convertTemp(cWindChill, cInputTempUnit, cOutputTempUnit);
	}
	/**
	 * Sets the Wind Chill temperature 
	 * @param windChill The Wind Chill to set
	 */
	public void setWindChill(float windChill) {
		cWindChill = windChill;
	}
	/**
	 * Gets the THW index 
	 * @return The THW Index
	 */
	public float getThwIndex() {
		return convertTemp(cThwIndex, cInputTempUnit, cOutputTempUnit);
	}
	/**
	* Sets the THW Index 
	 * @param thwIndex The THW Index to set
	 */
	public void setThwIndex(float thwIndex) {
		cThwIndex = thwIndex;
	}
	/**
	 * Gets the THSW index
	 * @return The THSW Index
	 */
	public float getThswIndex() {
		return convertTemp(cThswIndex, cInputTempUnit, cOutputTempUnit);
	}
	/**
	 * Sets the THSW Index
	 * @param thswIndex The THSW Index to set
	 */
	public void setThswIndex(float thswIndex) {
		cThswIndex = thswIndex;
	}
	/**
	 * Gets the most recent wind speed 
	 * @return The most recent wind speed detected.
	 */
	public float getWindPpeedLast() {
		return convertWindSpeed(cWindPpeedLast, cInputWindSpUnit, cOutputWindSpUnit);
	}
	/**
	 * Sets the wind speed detected most recently 
	 * @param windPpeedLast The most recent wind speed to set
	 */
	public void setWindPpeedLast(float windPpeedLast) {
		cWindPpeedLast = windPpeedLast;
	}
	/** Gets the most recent/current Wind Direction in degrees from North 
	 * Going clockwise
	 * @return The most recent/current Wind Direction
	 */
	public float getWindDirLast() {
		return cWindDirLast;
	}
	/** Gets the most recent/current Wind Direction as cardinal strings
	 * like N, SW, SE etc. 
	 * @return The most recent/current Wind Direction as cardinal
	 */
	public String getWindDirLastCardinal() {
		return getWindCardinal(cWindDirLast);
	}
	/** Sets the most recent/current wind direction in degrees from North
	 * going clockwise
	 * @param windDirLast The current wind direction to set
	 */
	public void setWindDirLast(float windDirLast) {
		cWindDirLast = windDirLast;
	}
	/** 
	 * Gets the average wind speed over the last minute
	 * @return The average wind speed over the last minute
	 */
	public float getWindSpeedAvgLast_1_min() {
		return convertWindSpeed(cWindSpeedAvgLast_1_min, cInputWindSpUnit, cOutputWindSpUnit);
	}
	/**
	 * Sets the average wind speed over the last minute
	 * @param windSpeedAvgLast_1_min The average wind speed over the last minute
	 */
	public void setWindSpeedAvgLast_1_min(float windSpeedAvgLast_1_min) {
		cWindSpeedAvgLast_1_min = windSpeedAvgLast_1_min;
	}
	/**
	 * Returns the average wind direction over the last one minute in degrees
	 * @return The average wind direction over the last 1 minute
	 */
	public float getWindDirScalarAvg_last_1_min() {
		return cWindDirScalarAvg_last_1_min;
	}
	/**
	 * Returns the average wind direction over the last one minute in as cardinal string
	 * @return The wind direction over the last 1 minute
	 */
	public String getWindDirScalarAvgCardinal_last_1_min() {
		return getWindCardinal(cWindDirScalarAvg_last_1_min);
	}
	/**
	 * Sets the average wind direction over the last minute in degrees
	 * @param windDirScalarAvg_last_1_min the Wind Direction average over last 1 min to set
	 */
	public void setWindDirScalarAvg_last_1_min(float windDirScalarAvg_last_1_min) {
		cWindDirScalarAvg_last_1_min = windDirScalarAvg_last_1_min;
	}
	/** 
	 * Gets the average wind speed over the last two minutes
	 * @return The average wind speed over the last 2 minutes
	 */
	public float getWindSpeedAvgLast_2_min() {
		return convertWindSpeed(cWindSpeedAvgLast_2_min, cInputWindSpUnit, cOutputWindSpUnit);
	}
	/**
	 * Sets the average wind speed over the last 2 minutes
	 * @param windSpeedAvgLast_2_min The average wind speed over the last 2 minutes
	 */
	public void setWindSpeedAvgLast_2_min(float windSpeedAvgLast_2_min) {
		cWindSpeedAvgLast_2_min = windSpeedAvgLast_2_min;
	}
	/**
	 * Returns the average wind direction over the last two minutes in degrees
	 * @return The wind direction over the last 2 minutes
	 */
	public float getWindDirScalarAvg_last_2_min() {
		return cWindDirScalarAvg_last_2_min;
	}
	/**
	 * Returns the average wind direction over the last two minutes as cardinal string
	 * @return The wind direction over the last 2 minutes
	 */
	public String getWindDirScalarAvgCardinal_last_2_min() {
		return getWindCardinal(cWindDirScalarAvg_last_2_min);
	}
	/**
	 * Sets the average wind direction over the last two minutes in degrees
	 * @param windDirScalarAvg_last_2_min the Wind Direction average over last 2 min to set
	 */
	public void setWindDirScalarAvg_last_2_min(float windDirScalarAvg_last_2_min) {
		cWindDirScalarAvg_last_2_min = windDirScalarAvg_last_2_min;
	}
	/**
	 * Gets the highest wind speed reached in the last 2 minutes 
	 * @return The highest wind speed reached in the last 2 minutes 
	 */
	public float getWindSpeedHi_last_2_min() {
		return convertWindSpeed(cWindSpeedHi_last_2_min, cInputWindSpUnit, cOutputWindSpUnit);
	}
	/**
	 * Sets the highest wind speed reached in the last 2 minutes 
	 * @param windSpeedHi_last_2_min The highest wind speed reached in the last 2 minutes
	 */
	public void setWindSpeedHi_last_2_min(float windSpeedHi_last_2_min) {
		cWindSpeedHi_last_2_min = windSpeedHi_last_2_min;
	}
	/** 
	 * Gets the Wind Direction in degrees when blowing at highest speed in the last 2 minutes
	 * @return The Wind Direction at highest speed in last 2 minutes in degrees 
	 */
	public float getWindDirAtHiSpeedLast_2_min() {
		return cWindDirAtHiSpeedLast_2_min;
	}
	/** 
	 * Gets the Wind Direction as a cardinal string when blowing at highest speed in the last 2 minutes
	 * @return The Wind Direction at highest speed in last 2 minutes as cardinal letters 
	 */
	public String getCardinalWindDirAtHiSpeedLast_2_min() {
		return getWindCardinal(cWindDirAtHiSpeedLast_2_min);
	}
	/**
	 * Sets the wind direction of the highest speed during the last 2 minutes 
	 * @param windDirAtHiSpeedLast_2_min The wind direction to set
	 */
	public void setWindDirAtHiSpeedLast_2_min(float windDirAtHiSpeedLast_2_min) {
		cWindDirAtHiSpeedLast_2_min = windDirAtHiSpeedLast_2_min;
	}
	/**
	 * Gets the average wind speed during the last 10 minutes
	 * @return The average wind speed during the last 10 minutes 
	 * 	 
	 */
	public float getWindSpeedAvgLast_10_min() {
		return convertWindSpeed(cWindSpeedAvgLast_10_min, cInputWindSpUnit, cOutputWindSpUnit);
	}
	/**
	 * Sets the average wind speed during the last 10 minutes 
	 * @param windSpeedAvgLast_10_min The average wind speed over the last 10 minutes to set
	 */
	public void setWindSpeedAvgLast_10_min(float windSpeedAvgLast_10_min) {
		cWindSpeedAvgLast_10_min = windSpeedAvgLast_10_min;
	}
	/**
	 * Returns the average scalar wind direction during the last 10 minutes in degrees
	 * @return The average scalar wind direction 
	 */
	public float getWindDirScalarAvgLast_10_min() {
		return cWindDirScalarAvgLast_10_min;
	}
	/**
	 * Returns the average scalar wind direction during the last 10 minutes as a cardinal string
	 * @return The average scalar wind direction 
	 */
	public String getCardinalWindDirScalarAvgLast_10_min() {
		return getWindCardinal(cWindDirScalarAvgLast_10_min);
	}
	/**
	 * Sets the average scalar wind direction during the last 10 minutes in degrees
	 * @param windDirScalarAvgLast_10_min The average scalar wind direction during the last 10 minutes in degrees
	 */
	public void setWindDirScalarAvgLast_10_min(float windDirScalarAvgLast_10_min) {
		cWindDirScalarAvgLast_10_min = windDirScalarAvgLast_10_min;
	}
	/**
	 * Gets the maximum wind speed during the last 10 minutes 
	 * @return The maximum wind speed during the last 10 minutes
	 */
	public float getWindSpeedHiLast_10_min() {
		return convertWindSpeed(cWindSpeedHiLast_10_min, cInputWindSpUnit, cOutputWindSpUnit);
	}
	/**
	 * Sets the maximum wind speed during the last 10 minutes 
	 * @param windSpeedHiLast_10_min The maximum speed speed during the last 10 minutes
	 */
	public void setWindSpeedHiLast_10_min(float windSpeedHiLast_10_min) {
		cWindSpeedHiLast_10_min = windSpeedHiLast_10_min;
	}
	/**
	 * Gets the wind direction of the maximum wind speed during the last 10 minutes in degrees
	 * @return The wind direction of the maximum wind speed during the last 10 minutes in degrees
	 */
	public float getWindDirAtHiSpeedLast_10_min() {
		return cWindDirAtHiSpeedLast_10_min;
	}
	/**
	 * Gets the wind direction of the maximum wind speed during the last 10 minutes as a cardinal string, e.g. NW, E, SE etc. 
	 * @return The wind direction of the maximum wind speed during the last 10 minutes as cardinal string
	 */
	public String getCardinalWindDirAtHiSpeedLast_10_min() {
		return getWindCardinal(cWindDirAtHiSpeedLast_10_min);
	}
	/**
	 * Sets the wind direction of the maximum wind speed during the last 10 minutes in degrees. 
	 * @param windDirAtHiSpeedLast_10_min The wind direction of the maximum wind speed during the last 10 minutes in degrees.
	 */
	public void setWindDirAtHiSpeedLast_10_min(float windDirAtHiSpeedLast_10_min) {
		cWindDirAtHiSpeedLast_10_min = windDirAtHiSpeedLast_10_min;
	}
	/**
	 * Gets the size/volume of the rain tipping bucket/spoon installed in the sensor suite. 
	 * @return The tipping bucket/spoon volume
	 */
	public float getRainSize() {
		return cRainSize;
	}
	/**
	 * Sets the size or volume of the rain tipping bucket. 0 is invalid, 1 is 0.01in, 2 is 0.2mm, 3 is 0.1mm and 4 is 0.001in.
	 * @param rainSize The size or volume of the rain bucket to set
	 */
	public void setRainSize(int rainSize) {
		
		// These are the WeatherLink rain bucket sizes.
		// rain collector type/size **(0: Reserved, 1: 0.01", 2: 0.2 mm, 3:  0.1 mm, 4: 0.001")**
		
		switch (rainSize) {
		
		case 1:
			cRainSize =  (float)0.01;
			cRainSizeDefUnit = Unit_Rain.IN;
			break;
		case 2:
			cRainSize = (float)0.2;
			cRainSizeDefUnit = Unit_Rain.MM;
			break;
		case 3:
			cRainSize = (float)0.1; 
			cRainSizeDefUnit = Unit_Rain.MM;
			break;
		case 4:
			cRainSize = (float)0.001;
			cRainSizeDefUnit = Unit_Rain.IN;
			break;
		default:
			// Invalid value, so set the size of the bucket to 0.2mm as default
			cRainSize = (float)0.2;
			cRainSizeDefUnit = Unit_Rain.MM;
		}
		 
	}
	/**
	 * Returns the current rain rate at units per hour 
	 * @return The current rain rate
	 */
	public float getRainRateLast() {
		return convertRainMeasure(cRainRateLast, cInputRainUnit, cOutputRainUnit);
	}
	/**
	 * Sets the current rain rate at units per hour 
	 * @param rainRateLast The current rain rate
	 */
	public void setRainRateLast(float rainRateLast) {
		cRainRateLast = rainRateLast;
	}
	/**
	 * Returns the highest rain rate over one minute
	 * @return The highest rain rate over one minute
	 */
	public float getRainRateHi() {
		return convertRainMeasure(cRainRateHi, cInputRainUnit, cOutputRainUnit);
	}
	/**
	 * Sets the highest rain rate over the last minute
	 * @param rainRateHi The highest rain rate to set over the last one minute
	 */
	public void setRainRateHi(float rainRateHi) {
		cRainRateHi = rainRateHi;
	}
	/**
	 * Returns the rainfall during the last 15 minutes 
	 * @return The rainfall during the last 15 minutes
	 */
	public float getRainfallLast_15_min() {
		return convertRainMeasure(cRainfallLast_15_min, cInputRainUnit, cOutputRainUnit);
	}
	/**
	 * Sets the rainfall during the last 15 minutes 
	 * @param rainfallLast_15_min The rainfall during the last 15 minutes
	 */
	public void setRainfallLast_15_min(float rainfallLast_15_min) {
		cRainfallLast_15_min = rainfallLast_15_min;
	}
	/**
	 * Gets the highest rain rate during the last 15 minutes
	 * @return The highest rain rate during the last 15 minutes
	 */
	public float getRainRateHiLast_15_min() {
		return convertRainMeasure(cRainRateHiLast_15_min, cInputRainUnit, cOutputRainUnit);
	}
	/**
	 * Sets the highest rain rate during the last 15 minutes
	 * @param rainRateHiLast_15_min The highest rain rate during the last 15 minutes to set
	 */
	public void setRainRateHiLast_15_min(float rainRateHiLast_15_min) {
		cRainRateHiLast_15_min = rainRateHiLast_15_min;
	}
	/** 
	 * Gets the rainfall during the last 60 minutes
	 * @return The rainfall during the last 60 minutes
	 */
	public float getRainfallLast_60_min() {
		return convertRainMeasure(cRainfallLast_60_min, cInputRainUnit, cOutputRainUnit);
	}
	/**
	 * Sets the rainfall during the last 60 minutes 
	 * @param rainfallLast_60_min The rainfall during the last 60 minutes
	 */
	public void setRainfallLast_60_min(float rainfallLast_60_min) {
		cRainfallLast_60_min = rainfallLast_60_min;
	}
	/**
	 * Gets the rainfall during the last 24 hours
	 * @return The rainfall during the last 24 hours
	 */
	public float getRainfallLast_24_hr() {
		return convertRainMeasure(cRainfallLast_24_hr, cInputRainUnit, cOutputRainUnit);
	}
	/**
	 * Sets the rainfall during the last 24 hours 
	 * @param rainfallLast_24_hr The rainfall during the last 24 hours to set
	 */
	public void setRainfallLast_24_hr(float rainfallLast_24_hr) {
		cRainfallLast_24_hr = rainfallLast_24_hr;
	}
	/**
	 * Gets the total amount of rain during the current rain storm event 
	 * @return The total amount of rain during the current rain storm event
	 */
	public float getRainStorm() {
		return convertRainMeasure(cRainStorm, cInputRainUnit, cOutputRainUnit);
	}
	/**
	 * Sets the total amount of rain during the current rain storm event
	 * @param rainStorm The total amount of rain during the current rain storm event to set
	 */
	public void setRainStorm(float rainStorm) {
		cRainStorm = rainStorm;
	}
	/**
	 * Gets the date and time the rain storm/event started
	 * @return A string representing the date and time
	 */
	public String getRainStormStartAt() {
		
		return getDateTimeFromUNIXtimeStamp(cRainStormStartAt);
	}
	/**
	 * Sets the date and time the rain storm/event started.
	 * @param rainStormStartAt 	The amount of seconds since the start of 1970 to when the 
	 * 							rain storm/event started - this Unix timestamp
	 */
	public void setRainStormStartAt(long rainStormStartAt) {
		cRainStormStartAt = rainStormStartAt;
	}
	/**
	 * Gets the solar radiation
	 * @return The solar radiation
	 */
	public float getSolarRad() {
		return cSolarRad;
	}
	/**
	 * Sets the solar radiation
	 * @param solarRad The solar radiation to set
	 */
	public void setSolarRad(float solarRad) {
		cSolarRad = solarRad;
	}
	/**
	 * Gets the UV index
	 * @return The UV Index
	 */
	public float getUvIndex() {
		return cUvIndex;
	}
	/**
	 * Sets the UV index
	 * @param uvIndex The UV Index to set
	 */
	public void setUvIndex(float uvIndex) {
		cUvIndex = uvIndex;
	}
	/**
	 * Gets the configured radio receiver state
	 * @return The radio receiver state
	 */
	public int getRxState() {
		return cRxState;
	}
	/**
	 * Sets the configured radio receiver state
	 * @param rxState The radio receiver state to set
	 */
	public void setRxState(int rxState) {
		cRxState = rxState;
	}
	/**
	 * Gets the status of the battery
	 * @return The status of the battery flag
	 */
	public int getTransBatteryFlag() {
		return cTransBatteryFlag;
	}
	/**
	 * Sets the status of the battery
	 * @param transBatteryFlag The battery status flag to set
	 */
	public void setTransBatteryFlag(int transBatteryFlag) {
		cTransBatteryFlag = transBatteryFlag;
	}
	/**
	 * Gets the daily rainfall 
	 * @return The daily rainfall
	 */ 
	public float getRrainfallDaily() {
		return convertRainMeasure(cRrainfallDaily, cInputRainUnit, cOutputRainUnit);
	}
	/**
	 * Sets the daily rainfall
	 * @param rainfallDaily The daily rainfall to set
	 */
	public void setRrainfallDaily(int rainfallDaily) {
		cRrainfallDaily = rainfallDaily;
	}
	/**
	 * Gets the monthly rainfall 
	 * @return The monthly rainfall
	 */
	public float getRainfallMonthly() {
		return convertRainMeasure(cRainfallMonthly, cInputRainUnit, cOutputRainUnit);
	}
	/**
	 * Sets the monthly rainfall 
	 * @param rainfallMonthly The monthly rainfall to set
	 */
	public void setRainfallMonthly(int rainfallMonthly) {
		cRainfallMonthly = rainfallMonthly;
	}
	/**
	 * Gets the yearly rainfall 
	 * @return The Yearly Rainfall
	 */
	public float getRainfallYear() {
		return convertRainMeasure(cRainfallYear, cInputRainUnit, cOutputRainUnit);
	}
	/**
	 * Sets the yearly rainfall 
	 * @param rainfallYear The yearly rainfall
	 */
	public void setRainfallYear(int rainfallYear) {
		cRainfallYear = rainfallYear;
	}
	/**
	 * Gets the total amount of rain during the last rain storm event 
	 * @return Total amount of rain during the last rain storm event
	 */
	public float getRainStormLast() {
		return convertRainMeasure(cRainStormLast, cInputRainUnit, cOutputRainUnit);
	}
	/**
	 * Sets the total amount of rain during the last rain storm event
	 * @param rainStormLast The total amount of rain during the last rain storm event to set
	 */
	public void setRainStormLast(int rainStormLast) {
		cRainStormLast = rainStormLast;
	}
	/**
	 * Gets the UNIX time stamp in seconds indicating when the last rain
	 * storm started
	 * @return UNIX time stamp of when the last rain storm started
	 */
	public long getRainStormLastStartAtSeconds() {
		return cRainStormLastStartAt;
	}
	/**
	 * Gets a string representing the date and time the last rain storm started
	 * @return The date and time when the last rain storm started
	 */
	public String getRainStormLastStartAt() {
		return getDateTimeFromUNIXtimeStamp(cRainStormLastStartAt);
	}
	/**
	 * Sets the date and time when the last rain storm stated using the 
	 * UNIX time stamp in seconds since 1970
	 * @param rainStormLastStartAt The date and time last rain storm started in seconds
	 */
	public void setRainStormLastStartAt(long rainStormLastStartAt) {
		cRainStormLastStartAt = rainStormLastStartAt;
	}
	/**
	 * Gets the UNIX time stamp in seconds indicating when the last rain
	 * storm ended
	 * @return UNIX time stamp of when the last rain storm ended
	 */
	public long getRainStormLastEndAtSeconds() {
		return cRainStormLastEndAt;
	}
	/**
	 * Gets a string representing the date and time the last rain storm ended
	 * @return The date and time when the last rain storm ended
	 */
	public String getRainStormLastEndAt() {
		return getDateTimeFromUNIXtimeStamp(cRainStormLastEndAt);
	}
	/**
	 * Sets the date and time when the last rain storm ended using the 
	 * UNIX time stamp in seconds since 1970
	 * @param rainStormLastStartAt The date and time last rain storm ended in seconds
	 */
	public void setRainStormLastEndAt(long rainStormLastEndAt) {
		cRainStormLastEndAt = rainStormLastEndAt;
	}
	
	/** 
	 * Gets the soil temperature for sensor slot 1
	 * @return The soil temperature
	 */
	public float getSoilTemp1() {
		return convertTemp(cSoilTemp1, cInputTempUnit, cOutputTempUnit);
	}
	/**
	 * Sets the soil temperature for sensor slot 1
	 * @param The soil temperature to set
	 */
	public void setSoilTemp1(float soilTemp1) {
		cSoilTemp1 = soilTemp1;
	}
	/** 
	 * Gets the soil temperature for sensor slot 2
	 * @return The soil temperature
	 */
	public float getTemp_2() {
		return convertTemp(cTemp_2, cInputTempUnit, cOutputTempUnit);
	}
	/**
	 * Sets the soil temperature for sensor slot 2
	 * @param The soil temperature to set
	 */
	public void setTemp_2(float temp_2) {
		cTemp_2 = temp_2;
	}
	/** 
	 * Gets the soil temperature for sensor slot 3
	 * @return The soil temperature
	 */
	public float getTemp_3() {
		return convertTemp(cTemp_3, cInputTempUnit, cOutputTempUnit);
	}
	/**
	 * Sets the soil temperature for sensor slot 3
	 * @param The soil temperature to set
	 */
	public void setTemp_3(float temp_3) {
		cTemp_3 = temp_3;
	}
	/** 
	 * Gets the soil temperature for sensor slot 4
	 * @return The soil temperature
	 */
	public float getTemp_4() {
		return convertTemp(cTemp_4, cInputTempUnit, cOutputTempUnit);
	}
	/**
	 * Sets the soil temperature for sensor slot 4
	 * @param The soil temperature to set
	 */
	public void setTemp_4(float temp_4) {
		cTemp_4 = temp_4;
	}
	/**
	 * Gets the soil moisture for slot 1
	 * @return The Soil Moisture
	 */
	public float getMoistSoil1() {
		return cMoistSoil1;
	}
	/**
	 * Sets the soil moisture for slot 1
	 * @param moistSoil1 The Soil Moisture to set
	 */
	public void setMoistSoil1(float moistSoil1) {
		cMoistSoil1 = moistSoil1;
	}
	/**
	 * Gets the soil moisture for slot 2
	 * @return The Soil Moisture
	 */
	public float getMpistSoil2() {
		return cMpistSoil2;
	}
	/**
	 * Sets the soil moisture for slot 2
	 * @param moistSoil1 The Soil Moisture to set
	 */
	public void setMpistSoil2(float mpistSoil2) {
		cMpistSoil2 = mpistSoil2;
	}
	/**
	 * Gets the soil moisture for slot 3
	 * @return The Soil Moisture
	 */
	public float getMoistSoil3() {
		return cMoistSoil3;
	}
	/**
	 * Sets the soil moisture for slot 3
	 * @param moistSoil1 The Soil Moisture to set
	 */
	public void setMoistSoil3(float moistSoil3) {
		cMoistSoil3 = moistSoil3;
	}
	/**
	 * Gets the soil moisture for slot 4
	 * @return The Soil Moisture
	 */
	public float getMoistSoil4() {
		return cMoistSoil4;
	}
	/**
	 * Sets the soil moisture for slot 4
	 * @param moistSoil1 The Soil Moisture to set
	 */
	public void setMoistSoil4(float moistSoil4) {
		cMoistSoil4 = moistSoil4;
	}
	/**
	 * Gets the leaf wetness for slot 1
	 * @return The leaf wetness
	 */
	public float getWetleaf1() {
		return cWetleaf1;
	}
	/**
	 * Sets the leaf wetness for slot 1
	 * @param wetleaf1 The leaf wetness to set
	 */
	public void setWetleaf1(float wetleaf1) {
		cWetleaf1 = wetleaf1;
	}
	/**
	 * Gets the leaf wetness for slot 2
	 * @return The leaf wetness
	 */
	public float getWetleaf2() {
		return cWetleaf2;
	}
	/**
	 * Sets the leaf wetness for slot 2
	 * @param wetleaf2 The leaf wetness to set
	 */
	public void setWetleaf2(float wetleaf2) {
		cWetleaf2 = wetleaf2;
	}
	/**
	 * Gets the indoor temperature
	 * @return The indoor temperature
	 */
	public float getInTemp() {
		return convertTemp(cInTemp, cInputTempUnit, cOutputTempUnit);
	}
	/**
	 * Sets the indoor temperature
	 * @param inTemp The indoor temperature to set
	 */
	public void setInTemp(float inTemp) {
		cInTemp = inTemp;
	}
	/**
	 * Gets the apparent feels like temperature according to  the Australian Bureau of Meteorology citing  
	 * Norms of apparent temperature in Australia, Aust. Met. Mag., 1994, Vol 43, 1-161.
	 * @return	The apparent temperature
	 */
	public float getInAppTemp() {
		return this.convertTemp((float)(calcAppTemp(this.convertTemp(cInTemp, cInputTempUnit, Unit_Temp.CELSIUS), cInHum, 0)), Unit_Temp.CELSIUS, cOutputTempUnit);
	}
	/**
	 * Gets the indoor humidity
	 * @return The indoor humidity
	 */
	public float getInHum() {
		return cInHum;
	}
	/**
	 * Sets the indoor humidity
	 * @param inHum The indoor humidity to set
	 */
	public void setInHum(float inHum) {
		cInHum = inHum;
	}
	/**
	 * Gets the indoor dew point
	 * @return The indoor dew point
	 */
	public float getInDewPoint() {
		return convertTemp(cInDewPoint, cInputTempUnit, cOutputTempUnit);
	}
	/**
	 * Sets the indoor dew point
	 * @param inDewPoint The indoor dew point to set
	 */
	public void setInDewPoint(float inDewPoint) {
		cInDewPoint = inDewPoint;
	}
	/**
	 * Gets the indoor heat index
	 * @return The indoor heat index
	 */
	public float getInHeatIndex() {
		return convertTemp(cInHeatIndex, cInputTempUnit, cOutputTempUnit);
	}
	/**
	 * Sets the indoor heat index
	 * @param inHeatIndex The indoor heat index to set
	 */
	public void setInHeatIndex(float inHeatIndex) {
		cInHeatIndex = inHeatIndex;
	}
	
	/**	 
	 * Gets the barometer reading with elevation adjusted
	 * @return The barometer reading
	 */
	public float getBarSeaLevel() {
		return convertPressure(cBarSeaLevel, cInputBarUnit, cOutputBarUnit);
	}
	/**
	 * Sets the barometer reading with elevation adjusted
	 * @param barSeaLevel	The barometer reading to set
	 */
	public void setBarSeaLevel(float barSeaLevel) {
		cBarSeaLevel = barSeaLevel;
	}
	/**
	 * Gets the current 3 hour barometer trend
	 * @return The barometer trend
	 */
	public float getBarTrend() {
		return convertPressure(cBarTrend, cInputBarUnit, cOutputBarUnit);
	}
	/**
	 * Sets the current 3 hour barometer trend
	 * @param barTrend	The barometer trend to set
	 */
	public void setBarTrend(float barTrend) {
		cBarTrend = barTrend;
	}
	/**
	 * Gets the raw barometer reading
	 * @return the raw barometer reading
	 */
	public float getBarAbsolute() {
		return convertPressure(cBarAbsolute, cInputBarUnit, cOutputBarUnit);
	}
	/**
	 * Sets the raw barometer reading
	 * @param barAbsolute The raw barometer reading to set
	 */
	public void setBarAbsolute(float barAbsolute) {
		cBarAbsolute = barAbsolute;
	}
	
	/**
	 * Returns the date display style/format used for dates and times
	 * @return String representing the format
	 */
	public String getDateFormat() {
		return cDateFormat;
	}
	/**
	 * The display format to use for dates. 
	 *
	 * @param DateFormat The format string to use for displaying dates
	 */
	public void setDateFormat(String DateFormat) {
		cDateFormat = DateFormat;
	}
	
	/** UNITS OF MEASURE HANDLING ---------------------------------------------------------------------------------------------
	*/
	
	/**

	 * Gets the default unit for temperature set for class
	 * @return The default temperature unit
	 */
	public Unit_Temp getDefTempUnit() {
		return cDefTempUnit;
	}
	/**
	 * Sets the default unit for temperature for this class and converts 
	 * all temperatures stored in this object to the new default unit
	 * @param defTempUnit 	The unit for temperature to use as default
	 */
	public void setDefTempUnit(Unit_Temp defTempUnit) {
		
		cTemp = convertTemp(cTemp, cDefTempUnit, defTempUnit);       
		cDewPoint = convertTemp(cDewPoint, cDefTempUnit, defTempUnit);   
		cWetBulb = convertTemp(cWetBulb, cDefTempUnit, defTempUnit);
		cHeatIndex = convertTemp(cHeatIndex, cDefTempUnit, defTempUnit);
		cWindChill = convertTemp(cWindChill, cDefTempUnit, defTempUnit);
		cThwIndex = convertTemp(cThwIndex, cDefTempUnit, defTempUnit);
		cThswIndex = convertTemp(cThswIndex, cDefTempUnit, defTempUnit);
		cSoilTemp1 = convertTemp(cSoilTemp1, cDefTempUnit, defTempUnit);
		cTemp_3 = convertTemp(cTemp_3, cDefTempUnit, defTempUnit);
		cTemp_4 = convertTemp(cTemp_4, cDefTempUnit, defTempUnit);
		cTemp_2 = convertTemp(cTemp_2, cDefTempUnit, defTempUnit);
		cInTemp = convertTemp(cInTemp, cDefTempUnit, defTempUnit);
		cInDewPoint = convertTemp(cInDewPoint, cDefTempUnit, defTempUnit);
		cInHeatIndex = convertTemp(cInHeatIndex, cDefTempUnit, defTempUnit);
		
		cDefTempUnit = defTempUnit;
		
	}
	/**
	 * Gets the default unit for Wind Speed for this class
	 * @return The default unit for Wind Speed
	 */
	public Unit_WindSpeed getDefWindSpUnit() {
		return cDefWindSpUnit;
	}
	/**
	 * Sets the default unit for Wind Speed for this class and converts
	 * all wind speeds stored in this object to the new default unit
	 * @param defWindSpUnit 	The unit for Wind Speed to use as default
	 */
	public void setDefWindSpUnit(Unit_WindSpeed defWindSpUnit) {
		
		cWindPpeedLast = convertWindSpeed(cWindPpeedLast, cDefWindSpUnit, defWindSpUnit);
		cWindSpeedAvgLast_1_min = convertWindSpeed(cWindSpeedAvgLast_1_min, cDefWindSpUnit, defWindSpUnit);
		cWindSpeedAvgLast_2_min = convertWindSpeed(cWindSpeedAvgLast_2_min, cDefWindSpUnit, defWindSpUnit);
		cWindSpeedHi_last_2_min = convertWindSpeed(cWindSpeedHi_last_2_min, cDefWindSpUnit, defWindSpUnit);
		cWindSpeedAvgLast_10_min = convertWindSpeed(cWindSpeedAvgLast_10_min, cDefWindSpUnit, defWindSpUnit);
		cWindSpeedHiLast_10_min = convertWindSpeed(cWindSpeedHiLast_10_min, cDefWindSpUnit, defWindSpUnit);
		
		cDefWindSpUnit = defWindSpUnit;
	}
	/**
	 * The default unit for Wind Direction being used for this class
	 * @return The default unit for Wind Direction
	 */
	public Unit_WindDirection getDefWindDirUnit() {
		return cDefWindDirUnit;
	}
	/**
	 * The default unit for Wind Direction to use for this class
	 * @param defWindDirUnit	The unit for wind direction to use as default
	 */
	public void setDefWindDirUnit(Unit_WindDirection defWindDirUnit) {
		cDefWindDirUnit = defWindDirUnit;
	}
	/**
	 * Gets the default unit to use for barometric pressure used by this class
	 * @return The unit used for Barometric pressure
	 */
	public Unit_Barometer getDefBarUnit() {
		return cDefBarUnit;
	}
	/**
	 * Sets the unit to use for Barometric pressure for this class and converts
	 * all pressure values stored in this object to the new default unit
	 * @param defBarUnit	The unit to use for Barometric pressure
	 */
	public void setDefBarUnit(Unit_Barometer defBarUnit) {
		
		cBarSeaLevel = convertPressure(cBarSeaLevel, cDefBarUnit, defBarUnit);
		cBarTrend = convertPressure(cBarTrend, cDefBarUnit, defBarUnit);
		cBarAbsolute = convertPressure(cBarAbsolute, cDefBarUnit, defBarUnit);
		
		cDefBarUnit = defBarUnit;
	}
	/**
	 * Gets the default unit used for measuring rain amount in this class
	 * @return The default unit for rain 
	 */
	public Unit_Rain getDefRainUnit() {
		return cDefRainUnit;
	}
	/**
	 * Sets the default unit used for rain amount for this object and converts all rain values 
	 * to the new default unit.  
	 * @param defRainUnit	The unit to use for rain for this class
	 */
	public void setDefRainUnit(Unit_Rain defRainUnit) {
				
		// cRainSize = convertRainMeasure(cRainSize, cRainSizeDefUnit, (defRainUnit==Unit_Rain.COUNTS ? cRainSizeDefUnit : defRainUnit));
		
		cRainRateLast = convertRainMeasure(cRainRateLast, cDefRainUnit, defRainUnit);
		cRainRateHi = convertRainMeasure(cRainRateHi, cDefRainUnit, defRainUnit);
		cRainfallLast_15_min = convertRainMeasure(cRainfallLast_15_min, cDefRainUnit, defRainUnit);
		cRainRateHiLast_15_min = convertRainMeasure(cRainRateHiLast_15_min, cDefRainUnit, defRainUnit);
		cRainfallLast_60_min = convertRainMeasure(cRainfallLast_60_min, cDefRainUnit, defRainUnit);
		cRainfallLast_24_hr = convertRainMeasure(cRainfallLast_24_hr, cDefRainUnit, defRainUnit);
		cRainStorm = convertRainMeasure(cRainStorm, cDefRainUnit, defRainUnit);
		cRrainfallDaily = convertRainMeasure(cRrainfallDaily, cDefRainUnit, defRainUnit);
		cRainfallMonthly = convertRainMeasure(cRainfallMonthly, cDefRainUnit, defRainUnit);
		cRainfallYear = convertRainMeasure(cRainfallYear, cDefRainUnit, defRainUnit);
		cRainStormLast = convertRainMeasure(cRainStormLast, cDefRainUnit, defRainUnit);
		
		// cRainSizeDefUnit = (defRainUnit==Unit_Rain.COUNTS ? cRainSizeDefUnit : defRainUnit);
		cDefRainUnit = defRainUnit;
	
	}

	
	
	/** 
	 * Gets the input unit for temperature. This is the unit of measure expected
	 * from the source sensor data.
	 * @return	The unit of measure for temperature used by the source data
	 */
	public Unit_Temp getInputTempUnit() {
		return cInputTempUnit;
	}
	/**
	 * Sets the input unit for temperature. This is the unit of measure expected
	 * from the source sensor data.
	 * @param 	inputTempUnit 	The unit of measure for temperature used by the source data
	 */
	public void setInputTempUnit(Unit_Temp inputTempUnit) {
		cInputTempUnit = inputTempUnit;
	}
	/**
	 * Gets the input unit for wind speed. This is the unit of measure expected
	 * from the source sensor data.
	 * @return 	The unit of measure for wind speed used by the source data
	 */
	public Unit_WindSpeed getInputWindSpUnit() {
		return cInputWindSpUnit;
	}
	/**
	 * Sets the input unit for wind speed. This is the unit of measure expected
	 * from the source sensor data.
	 * @param 	inputWindSpUnit		The unit of measure for wind speed used by the source data
	 */
	public void setInputWindSpUnit(Unit_WindSpeed inputWindSpUnit) {
		cInputWindSpUnit = inputWindSpUnit;
	}
	/**
	 * Gets the input unit for wind direction. This is the unit of measure expected
	 * from the source sensor data.
	 * @return 	The unit of measure for wind speed used by the source data
	 */
	public Unit_WindDirection getInputWindDirUnit() {
		return cInputWindDirUnit;
	}
	/**
	 * Sets the input unit for wind direction. This is the unit of measure expected
	 * from the source sensor data.
	 * @param 	inputWindDirUnit 	The unit of measure for wind speed used by the source data
	 */
	public void setInputWindDirUnit(Unit_WindDirection inputWindDirUnit) {
		cInputWindDirUnit = inputWindDirUnit;
	}
	/**
	 * Gets the input unit for barometric pressure. This is the unit of measure expected
	 * from the source sensor data.
	 * @return 	The unit of measure for barometric pressure used by the source data
	 */
	public Unit_Barometer getInputBarUnit() {
		return cInputBarUnit;
	}
	/**
	 * Sets the input unit for barometric pressure. This is the unit of measure expected
	 * from the source sensor data.
	 * @param 	inputBarUnit 	The unit of measure for barometric pressure used by the source data
	 */
	public void setInputBarUnit(Unit_Barometer inputBarUnit) {
		cInputBarUnit = inputBarUnit;
	}
	/**
	 * Gets the input unit for rain. This is the unit of measure expected
	 * from the source sensor data.
	 * @return 	The unit of measure for rain used by the source data
	 */
	public Unit_Rain getInputRainUnit() {
		return cInputRainUnit;
	}
	/**
	 * Sets the input unit for rain. This is the unit of measure expected
	 * from the source sensor data.
	 * @param 	inputRainUnit 	The unit of measure for rain used by the source data
	 */
	public void setInputRainUnit(Unit_Rain inputRainUnit) {
		cInputRainUnit = inputRainUnit;
	}
	/**
	 * Gets the output unit for temperature. This is the unit of measure to be used
	 * for output from this object. 
	 * @return 	The unit of measure for temperature required for output
	 */
	public Unit_Temp getOutputTempUnit() {
		return cOutputTempUnit;
	}
	/**
	 * Sets the output unit for temperature. This is the unit of measure to be used
	 * for output from this object. 
	 * @param 	outputTempUnit 	The unit of measure for temperature required for output
	 */
	public void setOutputTempUnit(Unit_Temp outputTempUnit) {
		cOutputTempUnit = outputTempUnit;
	}
	/**
	 * Gets the output unit for wind speed. This is the unit of measure to be used
	 * for output from this object. 
	 * @return 	The unit of measure for wind speed required for output
	 */
	public Unit_WindSpeed getOutputWindSpUnit() {
		return cOutputWindSpUnit;
	}
	/**
	 * Sets the output unit for wind speed. This is the unit of measure to be used
	 * for output from this object. 
	 * @param 	outputWindSpUnit 	The unit of measure for wind speed required for output
	 */
	public void setOutputWindSpUnit(Unit_WindSpeed outputWindSpUnit) {
		cOutputWindSpUnit = outputWindSpUnit;
	}
	/**
	 * Gets the output unit for wind direction. This is the unit of measure to be used
	 * for output from this object. 
	 * @return 	The unit of measure for wind direction required for output
	 */
	public Unit_WindDirection getOutputWindDirUnit() {
		return cOutputWindDirUnit;
	}
	/**
	 * Sets the output unit for wind direction. This is the unit of measure to be used
	 * for output from this object. 
	 * @param 	outputWindDirUnit 	The unit of measure for wind direction required for output
	 */
	public void setOutputWindDirUnit(Unit_WindDirection outputWindDirUnit) {
		cOutputWindDirUnit = outputWindDirUnit;
	}
	/**
	 * Gets the output unit for barometric pressure. This is the unit of measure to be used
	 * for output from this object. 
	 * @return	The unit of measure for barometric pressure required for output
	 */
	public Unit_Barometer getOutputBarUnit() {
		return cOutputBarUnit;
	}
	/**
	 * Sets the output unit for barometric pressure. This is the unit of measure to be used
	 * for output from this object. 
	 * @param 	outputBarUnit 	The unit of measure for barometric pressure required for output
	 */
	public void setOutputBarUnit(Unit_Barometer outputBarUnit) {
		cOutputBarUnit = outputBarUnit;
	}
	/**
	 * Sets the output unit for rain. This is the unit of measure to be used
	 * for output from this object. 
	 * @return 	The unit of measure for rain required for output
	 */
	public Unit_Rain getOutputRainUnit() {
		return cOutputRainUnit;
	}
	/**
	 * Sets the output unit for rain. This is the unit of measure to be used
	 * for output from this object. 
	 * @param 	outputRainUnit 	The unit of measure for rain required for output
	 */
	public void setOutputRainUnit(Unit_Rain outputRainUnit) {
		cOutputRainUnit = outputRainUnit;
	}

	/**
	 * Converts a string representation of the temperature unit to a Unit_Temp enum. 
	 * @param Unit	A string describing the unit to convert
	 * @return		A Observations.Unit_Temp
	 */
	public Unit_Temp getTempUnitFromString(String Unit) {
		
		// Needs to be case insensitive
		String lUnit = Unit.toUpperCase();
				
		switch(lUnit) {
		case "CELSIUS":
			return Unit_Temp.CELSIUS;
		case "FAHRENHEIT":
			return Unit_Temp.FAHRENHEIT;
			default:
				return Unit_Temp.FAHRENHEIT;
		}
		
	}
	
	/**
	 * Converts a string representation of the windspeed unit to a Unit_WindSpeed enum. 
	 * @param Unit	A string describing the unit to convert
	 * @return		A Observations.Unit_WindSpeed
	 */
	public Unit_WindSpeed getWindSpeedUnitFromString(String Unit) {
		
		// Needs to be case insensitive
		String lUnit = Unit.toUpperCase();
				
		switch(lUnit) {
		case "KPH":
			return Unit_WindSpeed.KPH;
		case "MPH":
			return Unit_WindSpeed.MPH;
		case "KNOTS":
			return Unit_WindSpeed.KNOTS;
		case "MPS":
			return Unit_WindSpeed.MPS;
			default:
				return Unit_WindSpeed.MPH;
		}
		
	}
	
	/**
	 * Converts a string representation of the wind direction unit to a Unit_WindDirection enum. 
	 * @param Unit	A string describing the unit to convert
	 * @return		A Observations.Unit_WindDirection
	 */
	public Unit_WindDirection getWindDirectionUnitFromString(String Unit) {
		
		// Needs to be case insensitive
		String lUnit = Unit.toUpperCase();
				
		switch(lUnit) {
		case "DEGREES":
			return Unit_WindDirection.DEGREES;
		case "CARDINAL":
			return Unit_WindDirection.CARDINAL;
			default:
				return Unit_WindDirection.DEGREES;
		}
		
	}
	
	/**
	 * Converts a string representation of the barometer unit to a Unit_Barometer enum. 
	 * @param Unit	A string describing the unit to convert
	 * @return		A Observations.Unit_Barometer
	 */
	public Unit_Barometer getBarometerUnitFromString(String Unit) {
		
		// Needs to be case insensitive
		String lUnit = Unit.toUpperCase();
				
		switch(lUnit) {
		case "INCHES":
			return Unit_Barometer.INCHES;
		case "MILIMETERS":
			return Unit_Barometer.MILIMETERS;
		case "MB":
			return Unit_Barometer.MB;
		case "HPA":
			return Unit_Barometer.HPA;
			default:
				return Unit_Barometer.INCHES;
		}
		
	}
	
	/**
	 * Converts a string representation of the rain unit to a Unit_Rain enum. 
	 * @param Unit	A string describing the unit to convert
	 * @return		A Observations.Unit_Rain
	 */
	public Unit_Rain getRainUnitFromString(String Unit) {
		
		// Needs to be case insensitive
		String lUnit = Unit.toUpperCase();
				
		switch(lUnit) {
		case "IN":
			return Unit_Rain.IN;
		case "MM":
			return Unit_Rain.MM;
		case "COUNTS":
			return Unit_Rain.COUNTS;
			default:
				return Unit_Rain.COUNTS;
		}
		
	}
	
	
	
	// To String - toString() output functions --------------------------------------------------------------------------------------------
	/**
	 * Returns a formatted string with tabs and new lines in human readable and presentable form containing 
	 * the entire observation readings contents of this object. 
	 */
	public String toString() {
		
		StringBuilder lString = new StringBuilder();
						
		lString.append("Outdoor Conditions:\n\n");
		if (cTemp > NO_VALUE) lString.append(String.format("\tTemperature:\t\t\t%.1f %s (Feels like: %.1f %s)\n", 
			getTemp(), tempUnit(),
			getAppTemp(), tempUnit()));
		if (cHum > NO_VALUE) lString.append(String.format("\tHumidity:\t\t\t%.1f %s \n", cHum, "\u0025")); //
		if (cBarAbsolute > NO_VALUE) lString.append(String.format("\tBarometric Pressure:\t\t%.1f %s, trending: %.1f %s (Raw Reading: %.1f %s)\n", 
			getBarSeaLevel(), barUnit(), 
			getBarTrend(), barUnit(),
			getBarAbsolute(), barUnit()));
		
		if (cWindDirLast > NO_VALUE) lString.append(String.format("\tWinds:\t\t\t\tBlowing %s at %.1f %s\n", dirUnit(cWindDirLast), 
			getWindPpeedLast(), 
			speedUnit()));
		
		if (cRainStorm > 0) lString.append(String.format("\tTotal Rain (Storm):\t\t%.1f %s since %s\n\n", getRainStorm(), rainUnit(), this.getRainStormStartAt()));
		if (cWindDirScalarAvgLast_10_min > NO_VALUE) lString.append(String.format("\tWind Averages\t\t\t10 min: %s at %.1f %s (Gust: %s at %.1f %s)\n", 
			dirUnit(cWindDirScalarAvgLast_10_min),
			getWindSpeedAvgLast_10_min(), speedUnit(),
			dirUnit(cWindDirAtHiSpeedLast_10_min),
			getWindSpeedHiLast_10_min(), speedUnit()));
		
		if (cWindDirScalarAvg_last_2_min > NO_VALUE) lString.append(String.format("\t\t\t\t\t2 min: %s at %.1f %s (Gust: %s at %.1f %s)\n", 
			dirUnit(cWindDirScalarAvg_last_2_min),
			getWindSpeedAvgLast_2_min(), speedUnit(),
			dirUnit(cWindDirAtHiSpeedLast_2_min),
			getWindSpeedHi_last_2_min(), speedUnit()));
		
		if (cWindDirScalarAvg_last_1_min > NO_VALUE) lString.append(String.format("\t\t\t\t\t1 min: %s at %.1f %s\n\n", dirUnit(cWindDirScalarAvg_last_1_min),
			getWindSpeedAvgLast_1_min(), speedUnit()));
		
		lString.append(String.format("\tRainfall:\n"));
		
		if (cRainRateLast > NO_VALUE) lString.append(String.format("\t\tCurrent Rate:\t\t%.1f %s/h (1 min max: %.1f %s/h)\n", getRainRateLast(), rainUnit(),
			getRainRateHi(), rainUnit()));
		
		if (cRainfallLast_15_min > NO_VALUE) lString.append(String.format("\t\tLast 15 minutes:\t%.1f %s (Highest Rate: %.1f %s/h)\n", getRainfallLast_15_min(), rainUnit(),
			getRainRateHiLast_15_min(), rainUnit()));
		
		if (cRainfallLast_60_min > NO_VALUE) lString.append(String.format("\t\tLast 60 minutes:\t%.1f %s\n", getRainfallLast_60_min(), rainUnit()));	
		if (cRrainfallDaily > NO_VALUE) lString.append(String.format("\t\tToday:\t\t\t%.1f %s\n", getRrainfallDaily(), rainUnit()));
		if (cRainfallLast_24_hr > NO_VALUE) lString.append(String.format("\t\tLast 24 hours:\t\t%.1f %s\n", getRainfallLast_24_hr(), rainUnit()));
		if (cRainfallMonthly > NO_VALUE) lString.append(String.format("\t\tThis month:\t\t%.1f %s\n", getRainfallMonthly(), rainUnit()));
		if (cRainfallYear > NO_VALUE) lString.append(String.format("\t\tThis year:\t\t%.1f %s\n", getRainfallYear(), rainUnit()));
		lString.append(String.format("\t\tLast Rain Event:\tStarted at:\t%s\n", getRainStormLastStartAt()));
		lString.append(String.format("\t\t\t\t\tEnded at:\t%s\n", getRainStormLastEndAt()));
		if (cRainStormLast > NO_VALUE) lString.append(String.format("\t\t\t\t\tTotal:\t\t%.1f %s\n\n", getRainStormLast(), rainUnit()));
		if (cRainSize > NO_VALUE) lString.append(String.format("\tRain Collector Size:\t\t%.1f %s\n\n", cRainSize, rainSizeUnit()));
		if (cDewPoint > NO_VALUE) lString.append(String.format("\tDew Point:\t\t\t%.1f %s\n", getDewPoint(), tempUnit()));
		if (cWetBulb > NO_VALUE) lString.append(String.format("\tWet Bulb:\t\t\t%.1f %s\n", getWetBulb(), tempUnit()));
		if (cHeatIndex > NO_VALUE) lString.append(String.format("\tHeat Index:\t\t\t%.1f %s\n", getHeatIndex(), tempUnit()));
		if (cWindChill > NO_VALUE) lString.append(String.format("\tWind Chill:\t\t\t%.1f %s\n", getWindChill(), tempUnit()));
		if (cThwIndex > NO_VALUE) lString.append(String.format("\tTHW Index:\t\t\t%.1f %s\n", getThwIndex(), tempUnit()));
		if (cThswIndex > NO_VALUE) lString.append(String.format("\tTHSW Index:\t\t\t%.1f %s\n", getThswIndex(), tempUnit()));
		if (cSolarRad > NO_VALUE) lString.append(String.format("\tSolar Radiation:\t\t%.1f W/m%s\n", cSolarRad, "\u00B2"));
		if (cUvIndex > NO_VALUE) lString.append(String.format("\tUV Index:\t\t\t%.1f\n\n", cUvIndex));
		
		lString.append(String.format("\tVegetation:\n\n"));
		
		if (cSoilTemp1 > NO_VALUE) lString.append(String.format("\t\tSoil Temp 1:\t\t%.1f %s\n", getSoilTemp1(), tempUnit()));
		if (cMoistSoil1 > NO_VALUE) lString.append(String.format("\t\tSoil Moisture 1:\t%.1f cb\n\n", cMoistSoil1));
		if (cTemp_2 > NO_VALUE) lString.append(String.format("\t\tSoil Temp 2:\t\t%.1f %s\n", getTemp_2(), tempUnit()));
		if (cMpistSoil2 > NO_VALUE) lString.append(String.format("\t\tSoil Moisture 2:\t%.1f cb\n\n", cMpistSoil2));
		if (cTemp_3 > NO_VALUE) lString.append(String.format("\t\tSoil Temp 3:\t\t%.1f %s\n", getTemp_3(), tempUnit()));
		if (cMoistSoil3 > NO_VALUE) lString.append(String.format("\t\tSoil Moisture 3:\t%.1f cb\n\n", cMoistSoil3));
		if (cTemp_4 > NO_VALUE) lString.append(String.format("\t\tSoil Temp 4:\t\t%.1f %s\n", getTemp_4(), tempUnit()));
		if (cMoistSoil4 > NO_VALUE) lString.append(String.format("\t\tSoil Moisture 4:\t%.1f cb\n\n", cMoistSoil4));
		if (cWetleaf1 > NO_VALUE) lString.append(String.format("\t\tWet Leaf 1:\t\t%.1f\n", cWetleaf1));
		if (cWetleaf2 > NO_VALUE) lString.append(String.format("\t\tWet Leaf 2:\t\t%.1f\n\n", cWetleaf2));
		
		lString.append(String.format("\tIndoor Conditions:\n\n"));
		
		if (cInTemp > NO_VALUE) lString.append(String.format("\t\tTemperature:\t\t%.1f %s (Feels like: %.1f %s)\n", 
			getInTemp(), tempUnit(),
			getInAppTemp(), tempUnit()));
		if (cInHum > NO_VALUE) lString.append(String.format("\t\tHumidity:\t\t%.1f %s\n", getInHum(), "\u0025"));
		if (cInDewPoint > NO_VALUE) lString.append(String.format("\t\tDew Point:\t\t%.1f %s\n", getInDewPoint(), tempUnit()));
		if (cInHeatIndex > NO_VALUE) lString.append(String.format("\t\tHeat Index:\t\t%.1f %s\n\n", getInHeatIndex(), tempUnit()));
		
		lString.append(String.format("\tDevice Information:\n\n"));
		
		if (cLsid > NO_VALUE) lString.append(String.format("\t\tLogical Sensor ID:\t%d\n", cLsid));
		if (cDataStructureType > NO_VALUE) lString.append(String.format("\t\tData Structure Type:\t%d\n", cDataStructureType));
		if (cTransmitterID > NO_VALUE) lString.append(String.format("\t\tTransmitter ID:\t\t%d\n", cTransmitterID));
		if (cRxState > NO_VALUE) lString.append(String.format("\t\tRadio Receiver State:\t%d\n", cRxState));
		if (cTransBatteryFlag > NO_VALUE) lString.append(String.format("\t\tTranmitter Battery:\t%d\n", cTransBatteryFlag));
		
		return lString.toString();
	}
	
	/**
	 * Unlike the proper toString method, this one allows for the output of only small groups of data based on 
	 * the type of data. There is a selection of ALL, DATAFILE, ESSENTIAL, RAIN and WIND. These are enum values
	 * that are passed on to this function to select the different outputs. ALL outputs the same as toString(), 
	 * DATAFILE will return a string with comma separated values suitable for saving to a text file, ESSENTIAL
	 * will display only the important weather information that is typically visible on a console screen, and the
	 * rest is self explanatory.
	 * @param Output	The OUTPUT_STRING enum with selectable values
	 * @return	String object containing formatted observation information
	 */
	public String toString(OUTPUT_STRING Output) {
		
		String lOutput = "";
		
		switch (Output) {
		
		case ALL:
				lOutput = toString();
			break;
		case DATAFILE:
				lOutput = String.format("%s\n%s", toStringDatafileHeader(),toStringDatafile());
			break;
		case ESSENTIAL:
				lOutput = toStringEssential();
			break;
		case RAIN:
				lOutput = toStringRain();
			break;
		case WIND:
				lOutput = toStringWind();
			break;
		}
		
		return lOutput;
		
	}
	
	/**
	 * Calculates the apparent temperature according to the Australian Bureau of Meteorology citing  
	 * Norms of apparent temperature in Australia, Aust. Met. Mag., 1994, Vol 43, 1-161. 
	 * @param 	Temp		Temperature in degrees celcius
	 * @param 	Hum			Percentage relative humidity	
	 * @param 	WindSpeed	Wind speed in metres per second (m/s)
	 * @return				The feels like apparent temperature
	 */
	public double calcAppTemp(double Temp, double Hum, double WindSpeed) {
		
		double lWaterVapourPressure;
		
		// Work out the Water Vapour Pressure
		lWaterVapourPressure = (Hum/100) * 6.105 * Math.exp(17.27 * Temp/(237.7+Temp));
		
		// Work out the apparent temperature
		return (Temp + 0.33*lWaterVapourPressure - 0.70*WindSpeed - 4.00);
		
	}
	
	// Private Methods -------------------------------------------------------------------------------------------------------
	
	// Unit conversion functions
	
	/**
	 * Converts between different units of temperature for internal use	
	 * @param Temp	The decimal value of the temperature
	 * @param From	The unit to convert from, use the Unit_Temp enum
	 * @param To	The unit to convert to, use the Unit_Temp enum
	 * @return		The converted value as a decimal float
	 */
	private float convertTemp(float Temp, Unit_Temp From, Unit_Temp To) {
		
		float lConvertedTemp = Temp;
		
		switch (From) {
		
		case FAHRENHEIT:
			if (To == Unit_Temp.CELSIUS) {lConvertedTemp = ((Temp - 32) * (float)(5.0/9.0));}
			break;
		case CELSIUS:
			if (To == Unit_Temp.FAHRENHEIT) {lConvertedTemp = ((Temp * (float)(9.0/5.0)) + 32);}
			break;
		}
		
		return lConvertedTemp;
	
	}
	
	/**
	 * Converts between different units of speed for internal use
	 * @param Speed	The decimal value of the speed
	 * @param From	The unit to convert from, use the Unit_WindSpeed enum
	 * @param To	The unit to convert to, use the Unit_WindSpeed enum
	 * @return		The converted value as a decimal float
	 */
	private float convertWindSpeed(float Speed, Unit_WindSpeed From, Unit_WindSpeed To) {

		float lConvertedSpeed = Speed;
		
		switch (From) {
		case KPH:
			
			switch (To) {
			case MPH:
				lConvertedSpeed = (float)(Speed / 1.609);
			break;
			case KNOTS:
				lConvertedSpeed = (float)(Speed / 1.852);
			break;
			case MPS:	
				lConvertedSpeed = (float)(Speed / 3.6);
			break;
			}
			
		break;
		case MPH:
		
			switch (To) {
			case KPH:
				lConvertedSpeed = (float)(Speed * 1.609);
			break;
			case KNOTS:
				lConvertedSpeed = (float)(Speed / 1.151);
			break;
			case MPS:	
				lConvertedSpeed = (float)(Speed / 2.237);
			break;
			}
			
		break;
		case KNOTS:
		
			switch (To) {
			case KPH:
				lConvertedSpeed = (float)(Speed * 1.852);
			break;
			case MPH:
				lConvertedSpeed = (float)(Speed * 1.151);
			break;
			case MPS:	
				lConvertedSpeed = (float)(Speed / 1.944);
			break;
			}
			
		break;
		case MPS:	
		
			switch (To) {
			case KPH:
				lConvertedSpeed = (float)(Speed * 3.6);
			break;
			case MPH:
				lConvertedSpeed = (float)(Speed * 2.237);
			break;
			case KNOTS:
				lConvertedSpeed = (float)(Speed * 1.944);
			break;
			}
			
		break;
		}
		
		return lConvertedSpeed;
		
	}
	
	/**
	 * Returns the cardinal wind direction string for a given direction in degrees from North working clockwise
	 * @param 	Direction		Wind Direction in degrees
	 * @return	Direction as N, NNE, ...., NW, NNW etc etc. Returns an empty string if there
	 * 			were issues
	 */
	private String getWindCardinal(float Direction) {
		
		if ((Direction >= 0 && Direction < 11.25)||(Direction > 348.75 && Direction <= 360)) {
			return "N";
		} else if (Direction > 11.25 && Direction < 33.75 ) {
			return "NNE";
		} else if (Direction > 33.75 && Direction < 56.25 ) {
			return "NE";
		} else if (Direction > 56.25 && Direction < 78.75 ) {
			return "ENE";
		} else if (Direction > 78.75 && Direction < 101.25 ) {
			return "E";
		} else if (Direction > 101.25 && Direction < 123.75 ) {
			return "ESE";
		} else if (Direction > 123.75 && Direction < 146.25 ) {
			return "SE";
		} else if (Direction > 146.25 && Direction < 168.75 ) {
			return "SSE";
		} else if (Direction > 168.75 && Direction < 191.25 ) {
			return "S";
		} else if (Direction > 191.25 && Direction < 213.75 ) {
			return "SSW";
		} else if (Direction > 213.75 && Direction < 236.25 ) {
			return "SW";
		} else if (Direction > 236.255 && Direction < 258.75 ) {
			return "WSW";
		} else if (Direction > 258.75 && Direction < 281.25 ) {
			return "W";
		} else if (Direction > 281.25 && Direction < 303.75 ) {
			return "WNW";
		} else if (Direction > 303.75 && Direction < 326.25 ) {
			return "NW";
		} else if (Direction > 326.25 && Direction < 348.75 ) {
			return "NNW";
		}
		
		return "";
		
	}

	/**
	 * Converts between different units of pressure for internal use
	 * @param PressureValue	The decimal value of the pressure
	 * @param From			The unit to convert from, use the Unit_Barometer enum
	 * @param To			The unit to convert to, use the Unit_Baronmeter enum
	 * @return				The converted value as a decimal float
	 */
	private float convertPressure(float PressureValue, Unit_Barometer From, Unit_Barometer To) {
	
		float lConvertedValue = PressureValue;
		
		switch (From) {
		case HPA:
			switch (To) {
			case INCHES:
					lConvertedValue = (float)(PressureValue / 33.864);
				break;
			case MILIMETERS:
					lConvertedValue = (float)(PressureValue / 1.333);
				break;
			}
			
			break;
		case INCHES:
			switch (To) {
			case HPA:
					lConvertedValue = (float)(PressureValue * 33.864);
				break;
			case MB:
					lConvertedValue = (float)(PressureValue * 33.864);
				break;
			case MILIMETERS:
					lConvertedValue = (float)(PressureValue * 25.4);
				break;
			}
			break;
		case MB:
			switch (To) {
			case INCHES:
					lConvertedValue = (float)(PressureValue / 33.864);
				break;
			case MILIMETERS:
					lConvertedValue = (float)(PressureValue / 1.333);
				break;
			}
			break;
		case MILIMETERS:
			switch (To) {
			case HPA:
					lConvertedValue = (float)(PressureValue * 1.333);
				break;
			case INCHES:
					lConvertedValue = (float)(PressureValue / 25.4);
				break;
			case MB:
					lConvertedValue = (float)(PressureValue * 1.333);
				break;
			}
			break;
		}
		
		return lConvertedValue;
		
	}
		
	/**
	 * Converts between different units of rain for internal use
	 * @param RainValue	The decimal value of the rain amount
	 * @param From		The unit to convert from, use the Unit_Rain enum
	 * @param To		The unit to convert to, use the Unit_Rain enum
	 * @return			The converted value as a decimal float
	 */
	private float convertRainMeasure(float RainValue, Unit_Rain From, Unit_Rain To) {
		
		float lConvertedRain = RainValue;
		
		switch (From) {
		
		case IN:
			if (To == Unit_Rain.MM) {lConvertedRain = (float)(RainValue * 25.4);}
			if (To == Unit_Rain.COUNTS) {

				// Need to convert the rain value to counts. Counts represent the number
				// of times the rain tipping bucket tipped. The tipping bucket comes in 
				// various sizes depending on the model of sensor used. This is set in 
				// cRainSize using the default measurement unit for rain. 
				
				// convert RainValue to default rain unit for this object
				lConvertedRain = convertRainMeasure(RainValue, Unit_Rain.IN, this.cRainSizeDefUnit);
				
				// divide the rain value by bucket size to get number of counts or tips
				lConvertedRain = lConvertedRain / cRainSize;
			}
			break;
		case MM:
			if (To == Unit_Rain.IN) {lConvertedRain = (float)(RainValue / 25.4);}
			if (To == Unit_Rain.COUNTS) {
				
				// convert RainValue to default rain unit for this object
				lConvertedRain = convertRainMeasure(RainValue, Unit_Rain.MM, this.cRainSizeDefUnit);
				
				// divide the rain value by bucket size to get number of counts or tips
				lConvertedRain = lConvertedRain / cRainSize;
			}
			break;
		case COUNTS:
			
			// Multiply the tipping counts by cRainSize to get the amount of rain
			lConvertedRain = (float)(RainValue * cRainSize);
			
			// Convert to the required unit
			lConvertedRain = convertRainMeasure(lConvertedRain, this.cRainSizeDefUnit, To);
			
			break;
		}
		
		// Return the value
		return lConvertedRain;
		
	}
	
	/**
	 * Converts between different units of length for internal use
	 * @param ElevationValue	The decimal value of the length
	 * @param From				The unit to convert from, use the Unit_Elevation enum
	 * @param To				The unit to convert to, use the Unit_Elevation enum
	 * @return					The converted value as a decimal float
	 */
	private float convertElevation(float ElevationValue, Unit_Elevation From, Unit_Elevation To) {
		
		float lConvertedElevation = ElevationValue;
		
		switch (From) {
		
		case FEET:
			if (To == Unit_Elevation.METERS) {lConvertedElevation = (float)(ElevationValue / 3.281);}
			break;
		case METERS:
			if (To == Unit_Elevation.FEET) {lConvertedElevation = (float)(ElevationValue * 3.281);}
			break;
		}
		
		return lConvertedElevation;
		
	}
	
	/**
	 * Returns a string representing the time from the UNIX time in seconds
	 * @param Seconds	Unix timestamp in seconds
	 * @return	String containing the time and date
	 */
	private String getDateTimeFromUNIXtimeStamp(long Seconds) {
		
		//convert seconds to milliseconds
		Date lDate = new Date(Seconds*1000); 
		
		// format of the date
		SimpleDateFormat lDateOutput = new SimpleDateFormat(cDateFormat);
		
		// Return the date as a string
		return (Seconds > 0) ? lDateOutput.format(lDate): "";
	}
	
	/**
	 * Returns a string representing the time from the UNIX time in seconds
	 * @param Seconds		Unix timestamp in seconds
	 * @param DateFormat	The display format the date should use. Optional. 
	 * @return	String containing the time and date
	 */
	private String getDateTimeFromUNIXtimeStamp(long Seconds, String DateFormat) {
		
		//convert seconds to milliseconds
		Date lDate = new Date(Seconds*1000); 
		
		// format of the date
		SimpleDateFormat lDateOutput = new SimpleDateFormat(DateFormat);
		
		// Return the date as a string
		return lDateOutput.format(lDate);
	}
	
	// Unit Outputs
	
	/**
	 * Returns a string representing the unit
	 * @return The unit string
	 */
	private String tempUnit() {

		String lUnitOut = "";
		switch (cOutputTempUnit) {
		case CELSIUS:
				lUnitOut = "C";
			break;
		case FAHRENHEIT: 
				lUnitOut = "F";
			break;
		}
		return lUnitOut;
	}
	
	/**
	 * Returns a string representing the unit
	 * @return The unit string
	 */
	private String speedUnit() {

		String lUnitOut = "";
		switch (cOutputWindSpUnit) {
		case KNOTS:
				lUnitOut = "knots";
			break;
		case MPH: 
				lUnitOut = "mph";
			break;
		case KPH:
				lUnitOut = "km/h";
			break;
		case MPS:
				lUnitOut = "m/s";
			break;
		}
		return lUnitOut;
	}
	
	/**
	 * Returns the wind direction based on default unit for wind direction
	 * @param 	Direction	The wind direction in degrees
	 * @return	A string representing the wind direction 
	 */
	private String dirUnit(float Direction) {
		
		String lUnitOut = "";
		
		switch (cOutputWindDirUnit) {
		
		case CARDINAL:
			lUnitOut = getWindCardinal(Direction);
			break;
		case DEGREES:
			lUnitOut = String.format("%d deg", (int)Direction);
			break;
		}
		
		return lUnitOut;
		
	}
	
	/**
	 * Returns a string representing the unit
	 * @return The unit string
	 */
	private String barUnit() {

		String lUnitOut = "";
		switch (cOutputBarUnit) {
		case HPA:
				lUnitOut = "hPa";
			break;
		case INCHES: 
				lUnitOut = "in";
			break;
		case MB:
				lUnitOut = "mb";
			break;
		case MILIMETERS:
				lUnitOut = "mm";
			break;
		}
		return lUnitOut;
	}
	
	/**
	 * Returns a string representing the unit
	 * @return The unit string
	 */
	private String rainUnit() {

		String lUnitOut = "";
		switch (cOutputRainUnit) {
		case COUNTS:
				lUnitOut = "counts";
			break;
		case IN: 
				lUnitOut = "in";
			break;
		case MM:
				lUnitOut = "mm";
			break;
		}
		return lUnitOut;
	}

	/**
	 * Returns a string representing the unit
	 * @return The unit string
	 */
	private String rainSizeUnit() {

		String lUnitOut = "";
		switch (cRainSizeDefUnit) {
		case COUNTS:
				lUnitOut = "counts";
			break;
		case IN: 
				lUnitOut = "in";
			break;
		case MM:
				lUnitOut = "mm";
			break;
		}
		return lUnitOut;
	}
	
	
	// To String Outputs
	
	/**
	 * Returns the field header information for the data file output
	 * @return String containing the field header names
	 */
	private String toStringDatafileHeader() {
		
		StringBuilder lString = new StringBuilder();
		
		lString.append(String.format("OutdoorTemperature,"));
		lString.append(String.format("AppTemperature,"));
		lString.append(String.format("OutdoorHumidity,"));
		lString.append(String.format("BarometricPressure,"));
		lString.append(String.format("Trending,"));
		lString.append(String.format("RawReading,"));
		lString.append(String.format("WindDir,"));
		lString.append(String.format("WindSpeed,"));
		lString.append(String.format("TotalRainCurrentStorm,"));
		lString.append(String.format("CurrentStormStart,"));
		lString.append(String.format("WindAverages10dir,"));
		lString.append(String.format("WindAverages10speed,"));
		lString.append(String.format("Wind10HighDir,"));
		lString.append(String.format("Wind10HighSpeed,"));
		lString.append(String.format("WindAverages2dir,"));
		lString.append(String.format("WindAverages2speed,"));
		lString.append(String.format("Wind2HighDir,"));
		lString.append(String.format("Wind2HighSpeed,"));
		lString.append(String.format("WindAverages1dir,"));
		lString.append(String.format("WindAverages1speed,"));
		lString.append(String.format("RainRate,"));
		lString.append(String.format("RainRateHighOneMinute,"));
		lString.append(String.format("RainLast15min,"));
		lString.append(String.format("RainLast15minHighRate,"));
		lString.append(String.format("RainLast60minutes,"));	
		lString.append(String.format("RainToday,"));
		lString.append(String.format("RainLast24hours,"));
		lString.append(String.format("RainThisMonth,"));
		lString.append(String.format("RainThisYear,"));
		lString.append(String.format("LastRainEventStartedAt,"));
		lString.append(String.format("LastRainEventEndedAt,"));
		lString.append(String.format("LastRainEventTotal,"));
		lString.append(String.format("RainCollectorSize,"));
		lString.append(String.format("OutdoorDewPoint,"));
		lString.append(String.format("OutdoorWetBulb,"));
		lString.append(String.format("OutdoorHeatIndex,"));
		lString.append(String.format("OutdoorWindChill,"));
		lString.append(String.format("OutdoorTHWIndex,"));
		lString.append(String.format("OutdoorTHSWIndex,"));
		lString.append(String.format("SolarRadiation,"));
		lString.append(String.format("UVIndex,"));
		lString.append(String.format("SoilTemp1,"));
		lString.append(String.format("SoilMoisture1,"));
		lString.append(String.format("SoilTemp2,"));
		lString.append(String.format("SoilMoisture2cb,"));
		lString.append(String.format("SoilTemp3,"));
		lString.append(String.format("SoilMoisture3cb,"));
		lString.append(String.format("SoilTemp4,"));
		lString.append(String.format("SoilMoisture4cb,"));
		lString.append(String.format("WetLeaf1,"));
		lString.append(String.format("WetLeaf2,"));
		lString.append(String.format("IndoorTemperature,"));
		lString.append(String.format("IndoorAppTemperature,"));
		lString.append(String.format("IndoorHumidity,"));
		lString.append(String.format("IndoorDewPoint,"));
		lString.append(String.format("IndoorHeatIndex,"));
		lString.append(String.format("LogicalSensorID,"));
		lString.append(String.format("DataStructureType,"));
		lString.append(String.format("TransmitterID,"));
		lString.append(String.format("RadioReceiverState,"));
		lString.append(String.format("TranmitterBattery"));
		
		return lString.toString();
		
	}
	
	/**
	 * Returns a comma delimited string with all of the weather observation data
	 * @return A comma delimited string containing observation data
	 */
	private String toStringDatafile() {
		
		StringBuilder lString = new StringBuilder();
						
		if (cTemp > NO_VALUE) {lString.append(String.format("%.1f,%.1f", getTemp(),getAppTemp()));} else {lString.append(",");}
		if (cHum > NO_VALUE) {lString.append(String.format("%.1f,", cHum));} else {lString.append(",");}
		if (cBarAbsolute > NO_VALUE) {lString.append(String.format("%.1f,%.1f,%.1f,",
				getBarSeaLevel(),
				getBarTrend(),
				getBarAbsolute()));} else {lString.append(",,,");}
		
		if (cWindDirLast > NO_VALUE) {lString.append(String.format("%s,%.1f,", 
				dirUnit(cWindDirLast), 
				getWindPpeedLast()));} else {lString.append(",,");}
		
		if (cRainStorm > NO_VALUE) {lString.append(String.format("%.1f,%s,", getRainStorm(), this.getRainStormStartAt()));} else {lString.append(",,");}
		if (cWindDirScalarAvgLast_10_min > NO_VALUE) {lString.append(String.format("%s,%.1f,%s,%.1f,",
			dirUnit(cWindDirScalarAvgLast_10_min),
			getWindSpeedAvgLast_10_min(),
			dirUnit(cWindDirAtHiSpeedLast_10_min),
			getWindSpeedHiLast_10_min()));} else {lString.append(",,,,");}
		
		if (cWindDirScalarAvg_last_2_min > NO_VALUE) {lString.append(String.format("%s,%.1f,%s,%.1f,", 
			dirUnit(cWindDirScalarAvg_last_2_min),
			getWindSpeedAvgLast_2_min(),
			dirUnit(cWindDirAtHiSpeedLast_2_min),
			getWindSpeedHi_last_2_min()));} else {lString.append(",,,,");}
		
		if (cWindDirScalarAvg_last_1_min > NO_VALUE) {lString.append(String.format("%s,%.1f,", 
			dirUnit(cWindDirScalarAvg_last_1_min),
			getWindSpeedAvgLast_1_min()));} else {lString.append(",,");}
		
		if (cRainRateLast > NO_VALUE) {lString.append(String.format("%.1f,%.1f,", 
			getRainRateLast(),
			getRainRateHi()));} else {lString.append(",,");}
		
		if (cRainfallLast_15_min > NO_VALUE) {lString.append(String.format("%.1f,%.1f,", 
			getRainfallLast_15_min(),
			getRainRateHiLast_15_min()));} else {lString.append(",,");}
		
		if (cRainfallLast_60_min > NO_VALUE) {lString.append(String.format("%.1f,", getRainfallLast_60_min()));} else {lString.append(",");}	
		if (cRrainfallDaily > NO_VALUE) {lString.append(String.format("%.1f,", getRrainfallDaily()));} else {lString.append(",");}
		if (cRainfallLast_24_hr > NO_VALUE) {lString.append(String.format("%.1f,", getRainfallLast_24_hr()));} else {lString.append(",");}
		if (cRainfallMonthly > NO_VALUE) {lString.append(String.format("%.1f,", getRainfallMonthly()));} else {lString.append(",");}
		if (cRainfallYear > NO_VALUE) {lString.append(String.format("%.1f,", getRainfallYear()));} else {lString.append(",");}
		lString.append(String.format("%s,", getRainStormLastStartAt()));
		lString.append(String.format("%s,", getRainStormLastEndAt()));
		if (cRainStormLast > NO_VALUE) {lString.append(String.format("%.1f,", getRainStormLast()));} else {lString.append(",");}
		if (cRainSize > NO_VALUE) {lString.append(String.format("%.1f,", cRainSize));} else {lString.append(",");}
		if (cDewPoint > NO_VALUE) {lString.append(String.format("%.1f,", getDewPoint()));} else {lString.append(",");}
		if (cWetBulb > NO_VALUE) {lString.append(String.format("%.1f,", getWetBulb()));} else {lString.append(",");}
		if (cHeatIndex > NO_VALUE) {lString.append(String.format("%.1f,", getHeatIndex()));} else {lString.append(",");}
		if (cWindChill > NO_VALUE) {lString.append(String.format("%.1f,", getWindChill()));} else {lString.append(",");}
		if (cThwIndex > NO_VALUE) {lString.append(String.format("%.1f,", getThwIndex()));} else {lString.append(",");}
		if (cThswIndex > NO_VALUE) {lString.append(String.format("%.1f,", getThswIndex()));} else {lString.append(",");}
		if (cSolarRad > NO_VALUE) {lString.append(String.format("%.1f,", cSolarRad));} else {lString.append(",");}
		if (cUvIndex > NO_VALUE) {lString.append(String.format("%.1f,", cUvIndex));} else {lString.append(",");}
		if (cSoilTemp1 > NO_VALUE) {lString.append(String.format("%.1f,", getSoilTemp1()));} else {lString.append(",");}
		if (cMoistSoil1 > NO_VALUE) {lString.append(String.format("%.1f,", cMoistSoil1));} else {lString.append(",");}
		if (cTemp_2 > NO_VALUE) {lString.append(String.format("%.1f,", getTemp_2()));} else {lString.append(",");}
		if (cMpistSoil2 > NO_VALUE) {lString.append(String.format("%.1f,", cMpistSoil2));} else {lString.append(",");}
		if (cTemp_3 > NO_VALUE) {lString.append(String.format("%.1f,", getTemp_3()));} else {lString.append(",");}
		if (cMoistSoil3 > NO_VALUE) {lString.append(String.format("%.1f,", cMoistSoil3));} else {lString.append(",");}
		if (cTemp_4 > NO_VALUE) {lString.append(String.format("%.1f,", getTemp_4()));} else {lString.append(",");}
		if (cMoistSoil4 > NO_VALUE) {lString.append(String.format("%.1f,", cMoistSoil4));} else {lString.append(",");}
		if (cWetleaf1 > NO_VALUE) {lString.append(String.format("%.1f,", cWetleaf1));} else {lString.append(",");}
		if (cWetleaf2 > NO_VALUE) {lString.append(String.format("%.1f,,", cWetleaf2));} else {lString.append(",");}
		if (cInTemp > NO_VALUE) {lString.append(String.format("%.1f,%.1f", getInTemp(),getInAppTemp()));} else {lString.append(",");}
		if (cInHum > NO_VALUE) {lString.append(String.format("%.1f,", getInHum()));} else {lString.append(",");}
		if (cInDewPoint > NO_VALUE) {lString.append(String.format("%.1f,", getInDewPoint()));} else {lString.append(",");}
		if (cInHeatIndex > NO_VALUE) {lString.append(String.format("%.1f,", getInHeatIndex()));} else {lString.append(",");}
		if (cLsid > NO_VALUE) {lString.append(String.format("%d,", cLsid));} else {lString.append(",");}
		if (cDataStructureType > NO_VALUE) {lString.append(String.format("%d,", cDataStructureType));} else {lString.append(",");}
		if (cTransmitterID > NO_VALUE) {lString.append(String.format("%d,", cTransmitterID));} else {lString.append(",");}
		if (cRxState > NO_VALUE) {lString.append(String.format("%d,", cRxState));} else {lString.append(",");}
		if (cTemp > NO_VALUE) {lString.append(String.format("%d", cTransBatteryFlag));} else {lString.append("");}
		
		return lString.toString();
	}
	 /**
	 * Returns essential weather observations information such as indoor and outdoor temperatures, and humidities. Also
	 * includes wind speed information and important rainfall information. The sensor battery and radio states
	 * are also returned. Three lines of text is returned.
	 * @return A string containing essential weather information.
	 */
	public String toStringEssential() {
		
		StringBuilder lString = new StringBuilder();
						
		if (cTemp > NO_VALUE) lString.append(String.format("Temperature: %.1f %s (Feels like: %.1f %s)\n", 
				getTemp(), tempUnit(),
				getAppTemp(), tempUnit()));
		if (cHum > NO_VALUE) lString.append(String.format("Hum: %.1f %s, ", cHum, "\u0025"));
		if (cInTemp > NO_VALUE) lString.append(String.format("Indoor Temperature: %.1f %s (Feels like: %.1f %s)\n", 
				getInTemp(), tempUnit(),
				getInAppTemp(), tempUnit()));
		if (cInHum > NO_VALUE) lString.append(String.format("In-Hum: %.1f %s, ", cInHum, "\u0025"));
		if (cBarSeaLevel > NO_VALUE) lString.append(String.format("Bar: %.1f %s, trend:  %.1f %s, ", 
			getBarSeaLevel(), barUnit(), 
			getBarTrend(), barUnit()));
		
		if (cWindDirLast > NO_VALUE) lString.append(String.format("Winds: %s at %.1f %s, ", dirUnit(cWindDirLast), 
			getWindPpeedLast(), 
			speedUnit()));
		
		if (cRainStorm > NO_VALUE) lString.append(String.format("Rain (Storm): %.1f %s since %s\n", getRainStorm(), rainUnit(), this.getRainStormStartAt()));
		if (cWindDirScalarAvgLast_10_min > NO_VALUE) lString.append(String.format("Wind 10 min:  %s at %.1f %s (Gust:  %s at %.1f %s), ", 
			dirUnit(cWindDirScalarAvgLast_10_min),
			getWindSpeedAvgLast_10_min(), speedUnit(),
			dirUnit(cWindDirAtHiSpeedLast_10_min),
			getWindSpeedHiLast_10_min(), speedUnit()));
		
		if (cWindDirScalarAvg_last_2_min > NO_VALUE) lString.append(String.format("2 min:  %s at %.1f %s (Gust:  %s at %.1f %s), ", 
			dirUnit(cWindDirScalarAvg_last_2_min),
			getWindSpeedAvgLast_2_min(), speedUnit(),
			dirUnit(cWindDirAtHiSpeedLast_2_min),
			getWindSpeedHi_last_2_min(), speedUnit()));
				
		if (cRainRateLast > NO_VALUE) lString.append(String.format("Current Rate: %.1f %s/h, ", getRainRateLast(), rainUnit(),
			getRainRateHi(), rainUnit()));
		
		if (cRainfallLast_15_min > NO_VALUE) lString.append(String.format("15 min: %.1f %s (Highest Rate:  %.1f %s/h)\n", getRainfallLast_15_min(), rainUnit(),
			getRainRateHiLast_15_min(), rainUnit()));
		
		if (cRrainfallDaily > NO_VALUE) lString.append(String.format("Today: %.1f %s, ", getRrainfallDaily(), rainUnit()));
		if (cDewPoint > NO_VALUE) lString.append(String.format("Dew: %.1f %s, ", getDewPoint(), tempUnit()));
		if (cWetBulb > NO_VALUE) lString.append(String.format("Wet: %.1f %s, ", getWetBulb(), tempUnit()));
		if (cHeatIndex > NO_VALUE) lString.append(String.format("Heat: %.1f %s, ", getHeatIndex(), tempUnit()));
		if (cWindChill > NO_VALUE) lString.append(String.format("Chill: %.1f %s, ", getWindChill(), tempUnit()));
		
		if (cRxState > NO_VALUE) lString.append(String.format("Radio Receiver State: %d, ", cRxState));
		if (cTransBatteryFlag > NO_VALUE) lString.append(String.format("Tranmitter Battery: %d, ", cTransBatteryFlag));
		
		return lString.toString();
	}
	
	/**
	 * Returns a string containing just the rain data
	 * @return A string containing the rain data
	 */
	public String toStringRain() {
		
		StringBuilder lString = new StringBuilder();
					
		if (cRainStorm > NO_VALUE) lString.append(String.format("Rain (Storm): %.1f %s since %s, ", getRainStorm(), rainUnit(), this.getRainStormStartAt()));
		if (cRainRateLast > NO_VALUE) lString.append(String.format("Rate: %.1f %s/h, ", getRainRateLast(), rainUnit(),
			getRainRateHi(), rainUnit()));
		
		if (cRainfallLast_15_min > NO_VALUE) lString.append(String.format("15 min: %.1f %s (Hign Rate: %.1f %s/h), ", getRainfallLast_15_min(), rainUnit(),
			getRainRateHiLast_15_min(), rainUnit()));
		
		if (cRrainfallDaily > NO_VALUE) lString.append(String.format("Today: %.1f %s, ", getRrainfallDaily(), rainUnit()));
		if (cRainfallLast_24_hr > NO_VALUE) lString.append(String.format("Last 24 hours: %.1f %s, ", getRainfallLast_24_hr(), rainUnit()));
		if (cRainfallMonthly > NO_VALUE) lString.append(String.format("This month: %.1f %s, ", getRainfallMonthly(), rainUnit()));
		if (cRainfallYear > NO_VALUE) lString.append(String.format("This year: %.1f %s, ", getRainfallYear(), rainUnit()));
		lString.append(String.format("Last Rain Event:Started at: %s, ", getRainStormLastStartAt()));
		lString.append(String.format("Ended at:%s, ", getRainStormLastEndAt()));
		if (cRainStormLast > NO_VALUE) lString.append(String.format("Total: %.1f %s, ", getRainStormLast(), rainUnit()));
		if (cRainSize > NO_VALUE) lString.append(String.format("Rain Size: %.1f %s", cRainSize, rainSizeUnit()));
		
		return lString.toString();
	}
	
	/**
	 * Returns just the wind data from this observations object. Live wind speed and 10 and 2 minute
	 * average and maximum readings
	 * @return Returns a string containing just the wind information
	 */
	public String toStringWind() {
		
		StringBuilder lString = new StringBuilder();
						
		if (cWindDirLast > NO_VALUE) lString.append(String.format("Wind: %s at %.1f %s, ", dirUnit(cWindDirLast), 
			getWindPpeedLast(), 
			speedUnit()));
		
		if (cWindDirScalarAvgLast_10_min > NO_VALUE) lString.append(String.format("Wind 10 min: %s at %.1f %s (Gust: %s at %.1f %s), ", 
			dirUnit(cWindDirScalarAvgLast_10_min),
			getWindSpeedAvgLast_10_min(), speedUnit(),
			dirUnit(cWindDirAtHiSpeedLast_10_min),
			getWindSpeedHiLast_10_min(), speedUnit()));
		
		if (cWindDirScalarAvg_last_2_min > NO_VALUE) lString.append(String.format("Wind 2 min: %s at %.1f %s (Gust: %s at %.1f %s)", 
			dirUnit(cWindDirScalarAvg_last_2_min),
			getWindSpeedAvgLast_2_min(), speedUnit(),
			dirUnit(cWindDirAtHiSpeedLast_2_min),
			getWindSpeedHi_last_2_min(), speedUnit()));
		
		return lString.toString();
	}
	
	
	// Private attributes ---------------------------------------------------------------------------------------------------
	
	// Sensor data
	
	private long cLsid;                      		// logical sensor ID **(no unit)**
	private int cDataStructureType;                 // data structure type **(no unit)**
	private int cTransmitterID;                     // transmitter ID **(no unit)**
	private float cTemp;                            // most recent valid temperature **(°F)**
	private float cHum;                             // most recent valid humidity **(%RH)**
	private float cDewPoint;                        // **(°F)**
	private float cWetBulb;                         // **(°F)**
	private float cHeatIndex;                       // **(°F)**
	private float cWindChill;                       // **(°F)**
	private float cThwIndex;                        // **(°F)**
	private float cThswIndex;                       // **(°F)**
	private float cWindPpeedLast;                   // most recent valid wind speed **(mph)**
	private float cWindDirLast;                     // most recent valid wind direction **(°degree)**
	private float cWindSpeedAvgLast_1_min;          // average wind speed over last 1 min **(mph)**
	private float cWindDirScalarAvg_last_1_min;     // scalar average wind direction over last 1 min **(°degree)**
	private float cWindSpeedAvgLast_2_min;          // average wind speed over last 2 min **(mph)**
	private float cWindDirScalarAvg_last_2_min;     // scalar average wind direction over last 2 min **(°degree)**
	private float cWindSpeedHi_last_2_min;          // maximum wind speed over last 2 min **(mph)**
	private float cWindDirAtHiSpeedLast_2_min;      // gust wind direction over last 2 min **(°degree)**
	private float cWindSpeedAvgLast_10_min;         // average wind speed over last 10 min **(mph)**
	private float cWindDirScalarAvgLast_10_min;     // scalar average wind direction over last 10 min **(°degree)**
	private float cWindSpeedHiLast_10_min;          // maximum wind speed over last 10 min **(mph)**
	private float cWindDirAtHiSpeedLast_10_min;     // gust wind direction over last 10 min **(°degree)**
	private float cRainSize;                        // rain collector type/size **(0: Reserved, 1: 0.01", 2: 0.2 mm, 3:  0.1 mm, 4: 0.001")**
	private float cRainRateLast;                    // most recent valid rain rate **(counts/hour)**
	private float cRainRateHi;                      // highest rain rate over last 1 min **(counts/hour)**
	private float cRainfallLast_15_min;             // total rain count over last 15 min **(counts)**
	private float cRainRateHiLast_15_min;           // highest rain rate over last 15 min **(counts/hour)**
	private float cRainfallLast_60_min;             // total rain count for last 60 min **(counts)**
	private float cRainfallLast_24_hr;              // total rain count for last 24 hours **(counts)**
	private float cRainStorm;                       // total rain count since last 24 hour long break in rain **(counts)**
	private long cRainStormStartAt;                 // UNIX timestamp of current rain storm start **(seconds)**
	private float cSolarRad;                        // most recent solar radiation **(W/m²)**
	private float cUvIndex;                         // most recent UV index **(Index)**
	private int cRxState;                           // configured radio receiver state **(no unit)**
	private int cTransBatteryFlag;                  // transmitter battery status flag **(no unit)**
	private float cRrainfallDaily;                    // total rain count since local midnight **(counts)**
	private float cRainfallMonthly;                   // total rain count since first of month at local midnight **(counts)**
	private float cRainfallYear;                      // total rain count since first of user-chosen month at local midnight **(counts)**
	private float cRainStormLast;                     // total rain count since last 24 hour long break in rain **(counts)**
	private long cRainStormLastStartAt;             // UNIX timestamp of last rain storm start **(sec)**
	private long cRainStormLastEndAt;               // UNIX timestamp of last rain storm end **(sec)**
	private float cSoilTemp1;                       // most recent valid soil temp slot 1 **(°F)**
	private float cTemp_3;                          // most recent valid soil temp slot 3 **(°F)**
	private float cTemp_4;                          // most recent valid soil temp slot 4 **(°F)**
	private float cTemp_2;                          // most recent valid soil temp slot 2 **(°F)**
	private float cMoistSoil1;                      // most recent valid soil moisture slot 1 **(|cb|)**
	private float cMpistSoil2;                      // most recent valid soil moisture slot 2 **(|cb|)**
	private float cMoistSoil3;                      // most recent valid soil moisture slot 3 **(|cb|)**
	private float cMoistSoil4;                      // most recent valid soil moisture slot 4 **(|cb|)**
	private float cWetleaf1;                        // most recent valid leaf wetness slot 1 **(no unit)**
	private float cWetleaf2;                        // most recent valid leaf wetness slot 2 **(no unit)**
	private float cInTemp;                          // most recent valid inside temp **(°F)**
	private float cInHum;                           // most recent valid inside humidity **(%RH)**
	private float cInDewPoint;                      // **(°F)**
	private float cInHeatIndex;                     // **(°F)**
	private float cBarSeaLevel;                     // most recent bar sensor reading with elevation adjustment **(inches)**
	private float cBarTrend;                        // current 3 hour bar trend **(inches)**
	private float cBarAbsolute;                     // raw bar sensor reading **(inches)**
	
	// Configuration data
	
	
	private String cDateFormat;
	private Unit_Temp cDefTempUnit;
	private Unit_WindSpeed cDefWindSpUnit;
	private Unit_WindDirection cDefWindDirUnit;
	private Unit_Barometer cDefBarUnit;
	private Unit_Rain cDefRainUnit;
	private Unit_Rain cRainSizeDefUnit;
	
	// These are units that the sensors are typically set to 
	
	private Unit_Temp cInputTempUnit;
	private Unit_WindSpeed cInputWindSpUnit;
	private Unit_WindDirection cInputWindDirUnit;
	private Unit_Barometer cInputBarUnit;
	private Unit_Rain cInputRainUnit;
	private Unit_Rain cRainSizeInputUnit;
	
	// These units are the desired output units
	
	private Unit_Temp cOutputTempUnit;
	private Unit_WindSpeed cOutputWindSpUnit;
	private Unit_WindDirection cOutputWindDirUnit;
	private Unit_Barometer cOutputBarUnit;
	private Unit_Rain cOutputRainUnit;
	private Unit_Rain cRainSizeOutputUnit;
	
	
}
