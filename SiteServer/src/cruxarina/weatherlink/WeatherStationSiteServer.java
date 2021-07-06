package cruxarina.weatherlink;

// import sun.jvm.hotspot.debugger.cdbg.basic.BasicLocalSym;

public class WeatherStationSiteServer extends Common {

	// CONSTRUCTORS
	
	public WeatherStationSiteServer() {
		cLatestObservations = new Observations();
		
	}
	
	public WeatherStationSiteServer(StationInterface StationConnection) {
		this();
		cLatestObservations = new Observations(((StationConnection)(StationConnection)).getConfig());
		cStationConnection = StationConnection;
		
		// Set the log file path
		String lLogFilePath = ((StationConnection)(StationConnection)).getConfig().getConfig("LogFilePath");
		setLogFilePath((lLogFilePath.length() > 0) ? lLogFilePath : "/var/log/wsssd/wsssd.conf");
		
		// Get the data from the station if it can connect
		if (cStationConnection.isConnected()) {getData();}
	}
	
	
	
	// GETTERS AND SETTERS
	
	
	/**
	 * The output unit with wind speed to use
	 * @return	The output unit for wind speed
	 */
	public Observations.Unit_WindSpeed getSpeedUnit() {
		return cSpeedUnit;
	}

	/**	
	 * Sets the output unit for wind speed
	 * @param 	speenUnit	Wind speed unit		
	 */
	public void setSpeedUnit(Observations.Unit_WindSpeed speedUnit) {
		cSpeedUnit = speedUnit;
		cLatestObservations.setOutputWindSpUnit(speedUnit);
	}

	/**
	 * The output unit for barometric pressure to use
	 * @return 	The unit for barometric pressure
	 */
	public Observations.Unit_Barometer getBarUnit() {
		return cBarUnit;
	}

	/**	
	 * The output unit for barometric pressure to use
	 * @param barUnit The unit for barometric pressure
	 */
	public void setBarUnit(Observations.Unit_Barometer barUnit) {
		cBarUnit = barUnit;
		cLatestObservations.setOutputBarUnit(barUnit);
	}

	/**
	 * The output unit for rain to use
	 * @return The unit for rain
	 */
	public Observations.Unit_Rain getRainUnit() {
		return cRainUnit;
	}

	/**
	 * The output unit for rain to use
	 * @param rainUnit The unit for rain
	 */
	public void setRainUnit(Observations.Unit_Rain rainUnit) {
		cRainUnit = rainUnit;
		cLatestObservations.setOutputRainUnit(rainUnit);
	}

	/**
	 * The output unit for wind direction to use
	 * @return The wind direction unit
	 */
	public Observations.Unit_WindDirection getWindDirUnit() {
		return cWindDirUnit;
	}

	/**
	 * The output unit for wind direction to use
	 * @param windDirUnit The wind direction unit
	 */
	public void setWindDirUnit(Observations.Unit_WindDirection windDirUnit) {
		cWindDirUnit = windDirUnit;
		cLatestObservations.setOutputWindDirUnit(windDirUnit);
	}

	/**
	 * The output unit for elevation to use
	 * @return The unit for elevation
	 */
	public Observations.Unit_Elevation getElUnit() {
		return cElUnit;
	}

	/**
	 * The output unit for elevation to use
	 * @param elUnit The unit for elevation
	 */
	public void setElUnit(Observations.Unit_Elevation elUnit) {
		cElUnit = elUnit;
		
	}

	/**
	 * The output unit for temperature to use
	 * @return The unit for temperature
	 */
	public Observations.Unit_Temp getTempUnit() {
		return cTempUnit;
	}

	/**
	 * The output unit for temperature to use
	 * @param tempUnit The unit for temperature
	 */
	public void setTempUnit(Observations.Unit_Temp tempUnit) {
		cTempUnit = tempUnit;
		cLatestObservations.setOutputTempUnit(tempUnit);
	}

	/**
	 * Gets the input unit for wind speed. This is the unit of measure expected
	 * from the source sensor data.
	 * @return 	The unit of measure for wind speed used by the source data
	 */
	public Observations.Unit_WindSpeed getInputSpeedUnit() {
		return cInputSpeedUnit;
	}

	/**
	 * Sets the input unit for wind speed. This is the unit of measure expected
	 * from the source sensor data.
	 * @param 	inputWindSpUnit		The unit of measure for wind speed used by the source data
	 */
	public void setInputSpeedUnit(Observations.Unit_WindSpeed inputSpeedUnit) {
		cInputSpeedUnit = inputSpeedUnit;
		cLatestObservations.setInputWindSpUnit(inputSpeedUnit);
	}

	/**
	 * Gets the input unit for barometric pressure. This is the unit of measure expected
	 * from the source sensor data.
	 * @return 	The unit of measure for barometric pressure used by the source data
	 */
	public Observations.Unit_Barometer getInputBarUnit() {
		return cInputBarUnit;
	}

	/**
	 * Sets the input unit for barometric pressure. This is the unit of measure expected
	 * from the source sensor data.
	 * @param 	inputBarUnit 	The unit of measure for barometric pressure used by the source data
	 */
	public void setInputBarUnit(Observations.Unit_Barometer inputBarUnit) {
		cInputBarUnit = inputBarUnit;
		cLatestObservations.setInputBarUnit(inputBarUnit);
	}

	/**
	 * Gets the input unit for rain. This is the unit of measure expected
	 * from the source sensor data.
	 * @return 	The unit of measure for rain used by the source data
	 */
	public Observations.Unit_Rain getInputRainUnit() {
		return cInputRainUnit;
	}

	/**
	 * Sets the input unit for rain. This is the unit of measure expected
	 * from the source sensor data.
	 * @param 	inputRainUnit 	The unit of measure for rain used by the source data
	 */
	public void setInputRainUnit(Observations.Unit_Rain inputRainUnit) {
		cInputRainUnit = inputRainUnit;
		cLatestObservations.setInputRainUnit(inputRainUnit);
	}

	/**
	 * Gets the input unit for wind direction. This is the unit of measure expected
	 * from the source sensor data.
	 * @return 	The unit of measure for wind speed used by the source data
	 */
	public Observations.Unit_WindDirection getInputWindDirUnit() {
		return cInputWindDirUnit;
	}

	/**
	 * Sets the input unit for wind direction. This is the unit of measure expected
	 * from the source sensor data.
	 * @param 	inputWindDirUnit 	The unit of measure for wind speed used by the source data
	 */
	public void setInputWindDirUnit(Observations.Unit_WindDirection inputWindDirUnit) {
		cInputWindDirUnit = inputWindDirUnit;
		cLatestObservations.setInputWindDirUnit(inputWindDirUnit);
	}

	/**
	 * @return the inputElUnit
	 */
	public Observations.Unit_Elevation getInputElUnit() {
		return cInputElUnit;
	}

	/**
	 * @param inputElUnit the inputElUnit to set
	 */
	public void setInputElUnit(Observations.Unit_Elevation inputElUnit) {
		cInputElUnit = inputElUnit;
	}

	/** 
	 * Gets the input unit for temperature. This is the unit of measure expected
	 * from the source sensor data.
	 * @return	The unit of measure for temperature used by the source data
	 */
	public Observations.Unit_Temp getInputTempUnit() {
		return cInputTempUnit;
	}

	/**
	 * Sets the input unit for temperature. This is the unit of measure expected
	 * from the source sensor data.
	 * @param 	inputTempUnit 	The unit of measure for temperature used by the source data
	 */
	public void setInputTempUnit(Observations.Unit_Temp inputTempUnit) {
		cInputTempUnit = inputTempUnit;
		cLatestObservations.setInputTempUnit(inputTempUnit);
	}
	
	// PUBLIC METHODS
	
	

	/**
	 * Gets the observations data from a supplied weather station connection object. 
	 * The weather station connection object handles the connection to the weather station
	 * and obtains the data from it. 
	 * 
	 * @param 	StationConnection	The object that connects to the station and gets the data
	 * @return	An Observations object containing the data from the weather station. 
	 */
	
	public Observations getData() {
		
		long lLsid = cStationConnection.getLsid(); 
		int lDataStructureType=cStationConnection.getDataStructureType();
		int lTransmitterID=cStationConnection.getTransmitterID();
		float lTemp=cStationConnection.getTemp();
		float lHum = cStationConnection.getHum();
		float lDewPoint = cStationConnection.getDewPoint();
		float lWetBulb = cStationConnection.getWetBulb();
		float lHeatIndex = cStationConnection.getHeatIndex();
		float lWindChill = cStationConnection.getWindChill();
		float lThwIndex = cStationConnection.getThwIndex();
		float lThswIndex = cStationConnection.getThswIndex();
		float lWindPpeedLast = cStationConnection.getWindPpeedLast();
		float lWindDirLast = cStationConnection.getWindDirLast();
		float lWindSpeedAvgLast_1_min = cStationConnection.getWindSpeedAvgLast_1_min();
		float lWindDirScalarAvg_last_1_min = cStationConnection.getWindDirScalarAvg_last_1_min();
		float lWindSpeedAvgLast_2_min = cStationConnection.getWindSpeedAvgLast_2_min();
		float lWindDirScalarAvg_last_2_min = cStationConnection.getWindDirScalarAvg_last_2_min();
		float lWindSpeedHi_last_2_min = cStationConnection.getWindSpeedHi_last_2_min();
		float lWindDirAtHiSpeedLast_2_min = cStationConnection.getWindDirAtHiSpeedLast_2_min();
		float lWindSpeedAvgLast_10_min = cStationConnection.getWindSpeedAvgLast_10_min();
		float lWindDirScalarAvgLast_10_min = cStationConnection.getWindDirScalarAvgLast_10_min();
		float lWindSpeedHiLast_10_min = cStationConnection.getWindSpeedHiLast_10_min();
		float lWindDirAtHiSpeedLast_10_min = cStationConnection.getWindDirAtHiSpeedLast_10_min();
		int lRainSize = cStationConnection.getRainSize();
		float lRainRateLast = cStationConnection.getRainRateLast();
		float lRainRateHi = cStationConnection.getRainRateHi();
		float lRainfallLast_15_min = cStationConnection.getRainfallLast_15_min();
		float lRainRateHiLast_15_min = cStationConnection.getRainRateHiLast_15_min();
		float lRainfallLast_60_min = cStationConnection.getRainfallLast_60_min();
		float lRainfallLast_24_hr = cStationConnection.getRainfallLast_24_hr();
		float lRainStorm = cStationConnection.getRainStorm();
		long lRainStormStartAt = cStationConnection.getRainStormStartAt();
		float lSolarRad = cStationConnection.getSolarRad();
		float lUvIndex = cStationConnection.getUvIndex();
		int lRxState = cStationConnection.getRxState(1);
		int lTransBatteryFlag = cStationConnection.getTransBatteryFlag(1);
		int lRrainfallDaily = cStationConnection.getRrainfallDaily();
		int lRainfallMonthly = cStationConnection.getRainfallMonthly();
		int lRainfallYear = cStationConnection.getRainfallYear();
		int lRainStormLast = cStationConnection.getRainStormLast();
		long lRainStormLastStartAt = cStationConnection.getRainStormLastStartAtSeconds();
		long lRainStormLastEndAt = cStationConnection.getRainStormLastEndAtSeconds();
		float lSoilTemp1 = cStationConnection.getSoilTemp1();
		float lTemp_3 = cStationConnection.getTemp_2();
		float lTemp_4 = cStationConnection.getTemp_3();
		float lTemp_2 = cStationConnection.getTemp_4();
		float lMoistSoil1 = cStationConnection.getMoistSoil1();
		float lMpistSoil2 = cStationConnection.getMpistSoil2();
		float lMoistSoil3 = cStationConnection.getMoistSoil3();
		float lMoistSoil4 = cStationConnection.getMoistSoil4();
		float lWetleaf1 = cStationConnection.getWetleaf1();
		float lWetleaf2 = cStationConnection.getWetleaf2();
		float lInTemp = cStationConnection.getInTemp();
		float lInHum = cStationConnection.getInHum();
		float lInDewPoint = cStationConnection.getInDewPoint();
		float lInHeatIndex = cStationConnection.getInHeatIndex();
		float lBarSeaLevel = cStationConnection.getBarSeaLevel();
		float lBarTrend = cStationConnection.getBarTrend();
		float lBarAbsolute = cStationConnection.getBarAbsolute();
		
		
		if (lLsid!=StationInterface.LONG_ERROR) {cLatestObservations.setLsid(lLsid);}                      
		if (lDataStructureType!=StationInterface.INT_ERROR) {cLatestObservations.setDataStructureType(lDataStructureType);}                 
		if (lTransmitterID!=StationInterface.INT_ERROR) {cLatestObservations.setTransmitterID(lTransmitterID);}                     
		if (lTemp!=StationInterface.FLOAT_ERROR) {cLatestObservations.setTemp(lTemp);}                            
		if (lHum!=StationInterface.FLOAT_ERROR) {cLatestObservations.setHum(lHum);}                             
		if (lDewPoint!=StationInterface.FLOAT_ERROR) {cLatestObservations.setDewPoint(lDewPoint);}                        
		if (lWetBulb!=StationInterface.FLOAT_ERROR) {cLatestObservations.setWetBulb(lWetBulb);}                         
		if (lHeatIndex!=StationInterface.FLOAT_ERROR) {cLatestObservations.setHeatIndex(lHeatIndex);}                       
		if (lWindChill!=StationInterface.FLOAT_ERROR) {cLatestObservations.setWindChill(lWindChill);}                       
		if (lThwIndex!=StationInterface.FLOAT_ERROR) {cLatestObservations.setThwIndex(lThwIndex);}                        
		if (lThswIndex!=StationInterface.FLOAT_ERROR) {cLatestObservations.setThswIndex(lThswIndex);}                       
		if (lWindPpeedLast!=StationInterface.FLOAT_ERROR) {cLatestObservations.setWindPpeedLast(lWindPpeedLast);}                   
		if (lWindDirLast!=StationInterface.FLOAT_ERROR) {cLatestObservations.setWindDirLast(lWindDirLast);}                     
		if (lWindSpeedAvgLast_1_min!=StationInterface.FLOAT_ERROR) {cLatestObservations.setWindSpeedAvgLast_1_min(lWindSpeedAvgLast_1_min);}          
		if (lWindDirScalarAvg_last_1_min!=StationInterface.FLOAT_ERROR) {cLatestObservations.setWindDirScalarAvg_last_1_min(lWindDirScalarAvg_last_1_min);}     
		if (lWindSpeedAvgLast_2_min!=StationInterface.FLOAT_ERROR) {cLatestObservations.setWindSpeedAvgLast_2_min(lWindSpeedAvgLast_2_min);}          
		if (lWindDirScalarAvg_last_2_min!=StationInterface.FLOAT_ERROR) {cLatestObservations.setWindDirScalarAvg_last_2_min(lWindDirScalarAvg_last_2_min);}     
		if (lWindSpeedHi_last_2_min!=StationInterface.FLOAT_ERROR) {cLatestObservations.setWindSpeedHi_last_2_min(lWindSpeedHi_last_2_min);}          
		if (lWindDirAtHiSpeedLast_2_min!=StationInterface.FLOAT_ERROR) {cLatestObservations.setWindDirAtHiSpeedLast_2_min(lWindDirAtHiSpeedLast_2_min);}      
		if (lWindSpeedAvgLast_10_min!=StationInterface.FLOAT_ERROR) {cLatestObservations.setWindSpeedAvgLast_10_min(lWindSpeedAvgLast_10_min);}         
		if (lWindDirScalarAvgLast_10_min!=StationInterface.FLOAT_ERROR) {cLatestObservations.setWindDirScalarAvgLast_10_min(lWindDirScalarAvgLast_10_min);}     
		if (lWindSpeedHiLast_10_min!=StationInterface.FLOAT_ERROR) {cLatestObservations.setWindSpeedHiLast_10_min(lWindSpeedHiLast_10_min);}          
		if (lWindDirAtHiSpeedLast_10_min!=StationInterface.FLOAT_ERROR) {cLatestObservations.setWindDirAtHiSpeedLast_10_min(lWindDirAtHiSpeedLast_10_min);}     
		if (lRainSize!=StationInterface.FLOAT_ERROR) {cLatestObservations.setRainSize(lRainSize);}                        
		if (lRainRateLast!=StationInterface.FLOAT_ERROR) {cLatestObservations.setRainRateLast(lRainRateLast);}                    
		if (lRainRateHi!=StationInterface.FLOAT_ERROR) {cLatestObservations.setRainRateHi(lRainRateHi);}                      
		if (lRainfallLast_15_min!=StationInterface.FLOAT_ERROR) {cLatestObservations.setRainfallLast_15_min(lRainfallLast_15_min);}             
		if (lRainRateHiLast_15_min!=StationInterface.FLOAT_ERROR) {cLatestObservations.setRainRateHiLast_15_min(lRainRateHiLast_15_min);}           
		if (lRainfallLast_60_min!=StationInterface.FLOAT_ERROR) {cLatestObservations.setRainfallLast_60_min(lRainfallLast_60_min);}             
		if (lRainfallLast_24_hr!=StationInterface.FLOAT_ERROR) {cLatestObservations.setRainfallLast_24_hr(lRainfallLast_24_hr);}              
		if (lRainStorm!=StationInterface.FLOAT_ERROR) {cLatestObservations.setRainStorm(lRainStorm);}                       
		if (lRainStormStartAt!=StationInterface.LONG_ERROR) {cLatestObservations.setRainStormStartAt(lRainStormStartAt);}                 
		if (lSolarRad!=StationInterface.FLOAT_ERROR) {cLatestObservations.setSolarRad(lSolarRad);}                        
		if (lUvIndex!=StationInterface.FLOAT_ERROR) {cLatestObservations.setUvIndex(lUvIndex);}                         
		if (lRxState!=StationInterface.INT_ERROR) {cLatestObservations.setRxState(lRxState);}                           
		if (lTransBatteryFlag!=StationInterface.INT_ERROR) {cLatestObservations.setTransBatteryFlag(lTransBatteryFlag);}                  
		if (lRrainfallDaily!=StationInterface.FLOAT_ERROR) {cLatestObservations.setRrainfallDaily(lRrainfallDaily);}                  
		if (lRainfallMonthly!=StationInterface.FLOAT_ERROR) {cLatestObservations.setRainfallMonthly(lRainfallMonthly);}                 
		if (lRainfallYear!=StationInterface.FLOAT_ERROR) {cLatestObservations.setRainfallYear(lRainfallYear);}                    
		if (lRainStormLast!=StationInterface.INT_ERROR) {cLatestObservations.setRainStormLast(lRainStormLast);}                   
		if (lRainStormLastStartAt!=StationInterface.LONG_ERROR) {cLatestObservations.setRainStormLastStartAt(lRainStormLastStartAt);}             
		if (lRainStormLastEndAt!=StationInterface.LONG_ERROR) {cLatestObservations.setRainStormLastEndAt(lRainStormLastEndAt);}               
		if (lSoilTemp1!=StationInterface.FLOAT_ERROR) {cLatestObservations.setSoilTemp1(lSoilTemp1);}                       
		if (lTemp_3!=StationInterface.FLOAT_ERROR) {cLatestObservations.setTemp_2(lTemp_3);}                          
		if (lTemp_4!=StationInterface.FLOAT_ERROR) {cLatestObservations.setTemp_3(lTemp_4);}                          
		if (lTemp_2!=StationInterface.FLOAT_ERROR) {cLatestObservations.setTemp_4(lTemp_2);}                          
		if (lMoistSoil1!=StationInterface.FLOAT_ERROR) {cLatestObservations.setMoistSoil1(lMoistSoil1);}                      
		if (lMpistSoil2!=StationInterface.FLOAT_ERROR) {cLatestObservations.setMpistSoil2(lMpistSoil2);}                      
		if (lMoistSoil3!=StationInterface.FLOAT_ERROR) {cLatestObservations.setMoistSoil3(lMoistSoil3);}                      
		if (lMoistSoil4!=StationInterface.FLOAT_ERROR) {cLatestObservations.setMoistSoil4(lMoistSoil4);}                      
		if (lWetleaf1!=StationInterface.FLOAT_ERROR) {cLatestObservations.setWetleaf1(lWetleaf1);}                        
		if (lWetleaf2!=StationInterface.FLOAT_ERROR) {cLatestObservations.setWetleaf2(lWetleaf2);}                        
		if (lInTemp!=StationInterface.FLOAT_ERROR) {cLatestObservations.setInTemp(lInTemp);}                          
		if (lInHum!=StationInterface.FLOAT_ERROR) {cLatestObservations.setInHum(lInHum);}                           
		if (lInDewPoint!=StationInterface.FLOAT_ERROR) {cLatestObservations.setInDewPoint(lInDewPoint);}                      
		if (lInHeatIndex!=StationInterface.FLOAT_ERROR) {cLatestObservations.setInHeatIndex(lInHeatIndex);}                     
		if (lBarSeaLevel!=StationInterface.FLOAT_ERROR) {cLatestObservations.setBarSeaLevel(lBarSeaLevel);}                     
		if (lBarTrend!=StationInterface.FLOAT_ERROR) {cLatestObservations.setBarTrend(lBarTrend);}                        
		if (lBarAbsolute!=StationInterface.FLOAT_ERROR) {cLatestObservations.setBarAbsolute(lBarAbsolute);}                     

		return cLatestObservations;
		
	}
	
	/**
	 * Sends a HTTP request to the Weatherlink Live device and gets the JSON object from it. It then deserializes the
	 * JSON object and updates all of the observation data
	 * @return	True if connection was successful and observation data successfully updated, otherwise false if there were issues. 
	 */
	public boolean refreshObservations() {
		if (!cStationConnection.refresh()) {
			logError("Could not refresh observations. " + cStationConnection.lastError());
			return false;
		}
		return true;
	}
	
	/**
	 * Looks for UDP packets on the network from the WeatherLink live device containing live update
	 * data and updates some of the observation data.
	 * @return	True if packets were found, False if timed out or if UDP broadcast couldn't be started.
	 */
	public boolean refreshLiveObservations() {
		if (!cStationConnection.refreshLive()) {
			logError("Could not obtain live data from device. " + cStationConnection.lastError());
			return false;
		}
		return true;
	}
	
	// PRIVATE METHODS
	
	// The latest observations
	Observations cLatestObservations;
	
	// The Weather Station Connection Interface
	StationInterface cStationConnection;
	
	// PRIVATE ATTRIBUTES
	
	// Output Units
	
	private Observations.Unit_WindSpeed cSpeedUnit;
	private Observations.Unit_Barometer cBarUnit;
	private Observations.Unit_Rain cRainUnit;
	private Observations.Unit_WindDirection cWindDirUnit;
	private Observations.Unit_Elevation cElUnit;
	private Observations.Unit_Temp cTempUnit;
	
	private Observations.Unit_WindSpeed cInputSpeedUnit;
	private Observations.Unit_Barometer cInputBarUnit;
	private Observations.Unit_Rain cInputRainUnit;
	private Observations.Unit_WindDirection cInputWindDirUnit;
	private Observations.Unit_Elevation cInputElUnit;
	private Observations.Unit_Temp cInputTempUnit;
	
}
