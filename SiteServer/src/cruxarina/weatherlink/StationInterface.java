/**
 * This interface specifies the required methods and outputs for a station connection implementation. It is up 
 * to the developer to fill in the gaps with how the implementation connects to a station and gets this data. 
 * 
 * @author Anthony Gibbons (BEngSoft MIEAust) Cruxarina Solutions
 */

package cruxarina.weatherlink;

public interface StationInterface {

	// CONSTANTS
	
	public static final float FLOAT_ERROR = (float)-9999.9;
	public static final int INT_ERROR = -9999;
	public static final long LONG_ERROR = -9999;
	
	/**
	 * Updates all of the weather observation data
	 * @return	True if all observation data was successfully updated, false if there were issues, such as connection issues
	 */
	public boolean refresh();
	
	/**
	 * Updates weather observation data more frequently for rapid update data from the station where the update 
	 * interval is quick
	 * @return	True if connection was successful and data updated, false if there were connection issues, such as
	 * 			a timeout or wrong connection information
	 */
	public boolean refreshLive();
	
	/**
	 * If data was successfully received from the device then this will return true. If there were 
	 * connection issues with the device, then this function will return false.
	 * @return	True if connection successful, false if not.
	 */
	public boolean isConnected();
	
	/**
	 * Returns a string containing the details of the most recent error
	 * @return	String containing error message
	 */
	public String lastError();
	
	/**
	 * Gets the sensor ID
	 * @return The sensor ID
	 */
	public long getLsid();
	/** 
	 * Gets the data structure type, which is specific to groups of sensors.
	 * @return The Data Structure Type
	 */
	public int getDataStructureType();
	/**
	 * Gets the ID of a sensor suite 
	 * @return The Transmitter ID
	 */
	public int getTransmitterID();
	/**
	 * Gets the outside temperature
	 * @return The outside temperature
	 */
	public float getTemp();
	/**
	 * Gets the humidity as percentage
	 * @return The humidity
	 */
	public float getHum();
	/**
	 * Gets the Dew point 
	 * @return The Dew Point
	 */
	public float getDewPoint();
	/**
	 * Gets the wet bulb temperature 
	 * @return 	The Wet Bulb temperature
	 */
	public float getWetBulb();
	/**
	 * Gets the heat index 
	 * @return The Heat Index
	 */
	public float getHeatIndex();
	/** 
	 * Gets the wind chill temperature 
	 * @return The Wind Chill temperature
	 */
	public float getWindChill();
	/**
	 * Gets the THW index 
	 * @return The THW Index
	 */
	public float getThwIndex();
	/**
	 * Gets the THSW index
	 * @return The THSW Index
	 */
	public float getThswIndex();
	/**
	 * Gets the most recent wind speed 
	 * @return The most recent wind speed detected.
	 */
	public float getWindPpeedLast();
	/** Gets the most recent/current Wind Direction in degrees from North 
	 * Going clockwise
	 * @return The most recent/current Wind Direction
	 */
	public float getWindDirLast();
	/** 
	 * Gets the average wind speed over the last minute
	 * @return The average wind speed over the last minute
	 */
	public float getWindSpeedAvgLast_1_min();
	/**
	 * Returns the average wind direction over the last one minute in degrees
	 * @return The average wind direction over the last 1 minute
	 */
	public float getWindDirScalarAvg_last_1_min();
	/** 
	 * Gets the average wind speed over the last two minutes
	 * @return The average wind speed over the last 2 minutes
	 */
	public float getWindSpeedAvgLast_2_min();
	/**
	 * Returns the average wind direction over the last two minutes in degrees
	 * @return The wind direction over the last 2 minutes
	 */
	public float getWindDirScalarAvg_last_2_min();
	/**
	 * Gets the highest wind speed reached in the last 2 minutes 
	 * @return The highest wind speed reached in the last 2 minutes 
	 */
	public float getWindSpeedHi_last_2_min();
	/** 
	 * Gets the Wind Direction in degrees when blowing at highest speed in the last 2 minutes
	 * @return The Wind Direction at highest speed in last 2 minutes in degrees 
	 */
	public float getWindDirAtHiSpeedLast_2_min();
	/**
	 * Gets the average wind speed during the last 10 minutes
	 * @return The average wind speed during the last 10 minutes 
	 * 	 
	 */
	public float getWindSpeedAvgLast_10_min();
	/**
	 * Returns the average scalar wind direction during the last 10 minutes in degrees
	 * @return The average scalar wind direction 
	 */
	public float getWindDirScalarAvgLast_10_min();
	/**
	 * Gets the maximum wind speed during the last 10 minutes 
	 * @return The maximum wind speed during the last 10 minutes
	 */
	public float getWindSpeedHiLast_10_min();
	/**
	 * Gets the wind direction of the maximum wind speed during the last 10 minutes in degrees
	 * @return The wind direction of the maximum wind speed during the last 10 minutes in degrees
	 */
	public float getWindDirAtHiSpeedLast_10_min();
	/**
	 * Gets the size/volume of the rain tipping bucket/spoon installed in the sensor suite. 
	 * @return The tipping bucket/spoon volume
	 */
	public int getRainSize();
	/**
	 * Returns the current rain rate at units per hour 
	 * @return The current rain rate
	 */
	public float getRainRateLast();
	/**
	 * Returns the highest rain rate over one minute
	 * @return The highest rain rate over one minute
	 */
	public float getRainRateHi();
	/**
	 * Returns the rainfall during the last 15 minutes 
	 * @return The rainfall during the last 15 minutes
	 */
	public float getRainfallLast_15_min();
	/**
	 * Gets the highest rain rate during the last 15 minutes
	 * @return The highest rain rate during the last 15 minutes
	 */
	public float getRainRateHiLast_15_min();
	/** 
	 * Gets the rainfall during the last 60 minutes
	 * @return The rainfall during the last 60 minutes
	 */
	public float getRainfallLast_60_min();
	/**
	 * Gets the rainfall during the last 24 hours
	 * @return The rainfall during the last 24 hours
	 */
	public float getRainfallLast_24_hr();
	/**
	 * Gets the total amount of rain during the current rain storm event 
	 * @return The total amount of rain during the current rain storm event
	 */
	public float getRainStorm();
	/**
	 * Gets the date and time the rain storm/event started in seconds since start
	 * of 1970
	 * @return Seconds since 1970 when rain storm started (UNIX timestamp)
	 */
	public long getRainStormStartAt();
	/**
	 * Gets the solar radiation
	 * @return The solar radiation
	 */
	public float getSolarRad();
	/**
	 * Gets the UV index
	 * @return The UV Index
	 */
	public float getUvIndex();
	/**
	 * Gets the configured radio receiver state
	 * @param	The ID of the transmitter/sensor suite
	 * @return 	The radio receiver state
	 */
	public int getRxState(int transmitterID);
	/**
	 * Gets the status of the battery
	 * @param	The ID of the transmitter/sensor suite
	 * @return 	The status of the battery flag
	 */
	public int getTransBatteryFlag(int transmitterID);
	/**
	 * Gets the daily rainfall 
	 * @return The daily rainfall
	 */ 
	public int getRrainfallDaily();
	/**
	 * Gets the monthly rainfall 
	 * @return The monthly rainfall
	 */
	public int getRainfallMonthly();
	/**
	 * Gets the yearly rainfall 
	 * @return The Yearly Rainfall
	 */
	public int getRainfallYear();
	/**
	 * Gets the total amount of rain during the last rain storm event 
	 * @return Total amount of rain during the last rain storm event
	 */
	public int getRainStormLast();
	/**
	 * Gets the UNIX time stamp in seconds indicating when the last rain
	 * storm started
	 * @return UNIX time stamp of when the last rain storm started
	 */
	public long getRainStormLastStartAtSeconds();
	/**
	 * Gets the UNIX time stamp in seconds indicating when the last rain
	 * storm ended
	 * @return UNIX time stamp of when the last rain storm ended
	 */
	public long getRainStormLastEndAtSeconds();
	/** 
	 * Gets the soil temperature for sensor slot 1
	 * @return The soil temperature
	 */
	public float getSoilTemp1();
	/** 
	 * Gets the soil temperature for sensor slot 2
	 * @return The soil temperature
	 */
	public float getTemp_2();
	/** 
	 * Gets the soil temperature for sensor slot 3
	 * @return The soil temperature
	 */
	public float getTemp_3();
	/** 
	 * Gets the soil temperature for sensor slot 4
	 * @return The soil temperature
	 */
	public float getTemp_4();
	/**
	 * Gets the soil moisture for slot 1
	 * @return The Soil Moisture
	 */
	public float getMoistSoil1();
	/**
	 * Gets the soil moisture for slot 2
	 * @return The Soil Moisture
	 */
	public float getMpistSoil2();
	/**
	 * Gets the soil moisture for slot 3
	 * @return The Soil Moisture
	 */
	public float getMoistSoil3();
	/**
	 * Gets the soil moisture for slot 4
	 * @return The Soil Moisture
	 */
	public float getMoistSoil4();
	/**
	 * Gets the leaf wetness for slot 1
	 * @return The leaf wetness
	 */
	public float getWetleaf1();
	/**
	 * Gets the leaf wetness for slot 2
	 * @return The leaf wetness
	 */
	public float getWetleaf2();
	/**
	 * Gets the indoor temperature
	 * @return The indoor temperature
	 */
	public float getInTemp();
	/**
	 * Gets the indoor humidity
	 * @return The indoor humidity
	 */
	public float getInHum();
	/**
	 * Gets the indoor dew point
	 * @return The indoor dew point
	 */
	public float getInDewPoint();
	/**
	 * Gets the indoor heat index
	 * @return The indoor heat index
	 */
	public float getInHeatIndex();
	/**	 
	 * Gets the barometer reading with elevation adjusted
	 * @return The barometer reading
	 */
	public float getBarSeaLevel();
	/**
	 * Gets the current 3 hour barometer trend
	 * @return The barometer trend
	 */
	public float getBarTrend();
	/**
	 * Gets the raw barometer reading
	 * @return the raw barometer reading
	 */
	public float getBarAbsolute();
	
}
