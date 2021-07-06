<?php 

require_once 'Common.php';
require_once 'WSASEPP_Const.php';
require_once 'WeatherUnits_Const.php';


class Observations extends Common {

    // Default constructor

    function __construct() {
		parent::__construct();
		
		$this->cDateFormat = "d M Y g:i a";
		$this->cInputTempUnit = UNIT_TEMP_FAHRENHEIT;
		$this->cInputWindSpUnit = UNIT_WINDSPEED_MPH;
		$this->cInputWindDirUnit = UNIT_WINDDIRECTION_DEGREES;
		$this->cInputBarUnit = UNIT_BAROMETER_INCHES;
		$this->cInputRainUnit = UNIT_RAIN_COUNTS;
		$this->cRainSizeDefUnit = UNIT_RAIN_MM;
		
		$this->cTempPrec = UNIT_PREC_TEMP;
		$this->cWindSpPrec = UNIT_PREC_WINDSPEED;
		$this->cBarPrec = UNIT_PREC_BAR;
		$this->cRainPrec = UNIT_PREC_RAIN;
		$this->cHumPrec = UNIT_PREC_HUM;

		// Set all of the observation values to an 'empty value' 
		// 0 doesn't make sense as 0 is legitimate as a weather observation.
		// null causes errors.
		
		$this->cLsid = NULL;                      		
		$this->cDataStructureType = NULL;                 
		$this->cTransmitterID = NULL;                     
		$this->cTemp = NULL;                            
		$this->cHum = NULL;                             
		$this->cDewPoint = NULL;                        
		$this->cWetBulb = NULL;                         
		$this->cHeatIndex = NULL;                       
		$this->cWindChill = NULL;                       
		$this->cThwIndex = NULL;                        
		$this->cThswIndex = NULL;                       
		$this->cWindPpeedLast = NULL;                   
		$this->cWindDirLast = NULL;                     
		$this->cWindSpeedAvgLast_1_min = NULL;          
		$this->cWindDirScalarAvg_last_1_min = NULL;     
		$this->cWindSpeedAvgLast_2_min = NULL;          
		$this->cWindDirScalarAvg_last_2_min = NULL;     
		$this->cWindSpeedHi_last_2_min = NULL;          
		$this->cWindDirAtHiSpeedLast_2_min = NULL;      
		$this->cWindSpeedAvgLast_10_min = NULL;         
		$this->cWindDirScalarAvgLast_10_min = NULL;     
		$this->cWindSpeedHiLast_10_min = NULL;          
		$this->cWindDirAtHiSpeedLast_10_min = NULL;     
		$this->cRainSize = NULL;                        
		$this->cRainRateLast = NULL;                    
		$this->cRainRateHi = NULL;                      
		$this->cRainfallLast_15_min = NULL;             
		$this->cRainRateHiLast_15_min = NULL;           
		$this->cRainfallLast_60_min = NULL;             
		$this->cRainfallLast_24_hr = NULL;              
		$this->cRainStorm = NULL;                       
		$this->cRainStormStartAt = NULL;                 
		$this->cSolarRad = NULL;                        
		$this->cUvIndex = NULL;                         
		$this->cRxState = NULL;                           
		$this->cTransBatteryFlag = NULL;                  
		$this->cRrainfallDaily = NULL;                  
		$this->cRainfallMonthly = NULL;                 
		$this->cRainfallYear = NULL;                    
		$this->cRainStormLast = NULL;                   
		$this->cRainStormLastStartAt = NULL;             
		$this->cRainStormLastEndAt = NULL;               
		$this->cSoilTemp1 = NULL;                       
		$this->cTemp_3 = NULL;                          
		$this->cTemp_4 = NULL;                          
		$this->cTemp_2 = NULL;                          
		$this->cMoistSoil1 = NULL;                      
		$this->cMpistSoil2 = NULL;                      
		$this->cMoistSoil3 = NULL;                      
		$this->cMoistSoil4 = NULL;                      
		$this->cWetleaf1 = NULL;                        
		$this->cWetleaf2 = NULL;                        
		$this->cInTemp = NULL;                          
		$this->cInHum = NULL;                           
		$this->cInDewPoint = NULL;                      
		$this->cInHeatIndex = NULL;                     
		$this->cBarSeaLevel = NULL;                     
		$this->cBarTrend = NULL;                        
		$this->cBarAbsolute = NULL;   
		$this->cPrevPacketLatency = NULL;
		$this->cObservationReceiveTime = NULL;

	}

	// Public Methods -------------------------------------------------------------------------------------------------------

	// Observation data getters and setters -----------------------------------------------------------------------------------------
	
	/**
	 * Gets the sensor ID
	 * @return The sensor ID
	 */
	public function getLsid() {
		return $this->cLsid;
	}
	/**
	 * Sets the sensor ID
	 * @param lsid	The sensor ID
	 */
	public function setLsid($lsid) {
		$this->cLsid = $lsid;
	}
	/** 
	 * Gets the data structure type, which is specific to groups of sensors.
	 * @return The Data Structure Type
	 */
	public function getDataStructureType() {
		return $this->cDataStructureType;
	}	
	/**
	 * Sets the data structure type. 
	 * @param dataStructureType The Data Structure Type
	 */
	public function setDataStructureType($dataStructureType) {
		$this->cDataStructureType = $dataStructureType;
	}
	/**
	 * Gets the ID of a sensor suite 
	 * @return The Transmitter ID
	 */
	public function getTransmitterID() {
		return $this->cTransmitterID;
	}
	/**
	 * Sets the ID of a sensor suite 
	 * @param transmitterID The Transmitter ID to set
	 */
	public function setTransmitterID($transmitterID) {
		$this->cTransmitterID = $transmitterID;
	}
		/**
	 * Gets the outside temperature
	 * @return The outside temperature
	 */
	public function getTemp() {
		return $this->convertTemp($this->cTemp, $this->cInputTempUnit, $this->cOutputTempUnit);
	}
	/**
	 * Sets the outside temperature
	 * @param temp The value of the outside temperature to set
	 */
	public function setTemp($temp) {
		$this->cTemp = $temp;
	}
	/**
	 * Gets the apparent feels like temperature according to  the Australian Bureau of Meteorology citing  
	 * Norms of apparent temperature in Australia, Aust. Met. Mag., 1994, Vol 43, 1-161.
	 * @return	The apparent temperature
	 */
	public function getAppTemp() {
		return $this->convertTemp(($this->calcAppTemp($this->convertTemp($this->cTemp, $this->cInputTempUnit, UNIT_TEMP_CELSIUS), $this->cHum, $this->convertWindSpeed($this->cWindSpeedAvgLast_10_min, $this->cInputWindSpUnit, UNIT_WINDSPEED_MPS))), UNIT_TEMP_CELSIUS, $this->cOutputTempUnit);
	}
	/**
	 * Gets the humidity as percentage
	 * @return The humidity
	 */
	public function getHum() {
		return round($this->cHum, $this->cHumPrec);
	}
	/**
	 * Sets the humidity as a percentage
	 * @param hum The humidity as percentage
	 */
	public function setHum($hum) {
		$this->cHum = $hum;
	}
	/**
	 * Gets the Dew point 
	 * @return The Dew Point
	 */
	public function getDewPoint() {
		return $this->convertTemp($this->cDewPoint, $this->cInputTempUnit, $this->cOutputTempUnit);
	}
	/** 
	 * Sets the Dew Point 
	 * @param dewPoint The Dew Point to set
	 */
	public function setDewPoint($dewPoint) {
		$this->cDewPoint = $dewPoint;
	}
	/**
	 * Gets the wet bulb temperature 
	 * @return 	The Wet Bulb temperature
	 */
	public function getWetBulb() {
		return $this->convertTemp($this->cWetBulb, $this->cInputTempUnit, $this->cOutputTempUnit);
	}
	/**
	 * Sets the wet bulb temperature 
	 * @param wetBulb The Wet Bulb temperature to set
	 */
	public function setWetBulb($wetBulb) {
		$this->cWetBulb = $wetBulb;
	}
	/**
	 * Gets the heat index 
	 * @return The Heat Index
	 */
	public function getHeatIndex() {
		return $this->convertTemp($this->cHeatIndex, $this->cInputTempUnit, $this->cOutputTempUnit);
	}
	/**
	 * Sets the heat index
	 * @param heatIndex The Heat Index to set
	 */
	public function setHeatIndex($heatIndex) {
		$this->cHeatIndex = $heatIndex;
	}
	/** 
	 * Gets the wind chill temperature 
	 * @return The Wind Chill temperature
	 */
	public function getWindChill() {
		return $this->convertTemp($this->cWindChill, $this->cInputTempUnit, $this->cOutputTempUnit);
	}
	/**
	 * Sets the Wind Chill temperature 
	 * @param windChill The Wind Chill to set
	 */
	public function setWindChill($windChill) {
		$this->cWindChill = $windChill;
	}
	/**
	 * Gets the THW index 
	 * @return The THW Index
	 */
	public function getThwIndex() {
		return $this->convertTemp($this->cThwIndex, $this->cInputTempUnit, $this->cOutputTempUnit);
	}
	/**
	* Sets the THW Index 
	 * @param thwIndex The THW Index to set
	 */
	public function setThwIndex($thwIndex) {
		$this->cThwIndex = $thwIndex;
	}
	/**
	 * Gets the THSW index
	 * @return The THSW Index
	 */
	public function getThswIndex() {
		return $this->convertTemp($this->cThswIndex, $this->cInputTempUnit, $this->cOutputTempUnit);
	}
	/**
	 * Sets the THSW Index
	 * @param thswIndex The THSW Index to set
	 */
	public function setThswIndex($thswIndex) {
		$this->cThswIndex = $thswIndex;
	}
	/**
	 * Gets the most recent wind speed 
	 * @return The most recent wind speed detected.
	 */
	public function getWindPpeedLast() {
		return $this->convertWindSpeed($this->cWindPpeedLast, $this->cInputWindSpUnit, $this->cOutputWindSpUnit);
	}
	/**
	 * Sets the wind speed detected most recently 
	 * @param windPpeedLast The most recent wind speed to set
	 */
	public function setWindPpeedLast($windPpeedLast) {
		$this->cWindPpeedLast = $windPpeedLast;
	}
	/** Gets the most recent/current Wind Direction in degrees from North 
	 * Going clockwise
	 * @return The most recent/current Wind Direction
	 */
	public function getWindDirLast() {
		return $this->cWindDirLast;
	}
	/** Gets the most recent/current Wind Direction as cardinal strings
	 * like N, SW, SE etc. 
	 * @return The most recent/current Wind Direction as cardinal
	 */
	public function getWindDirLastCardinal() {
		return $this->getWindCardinal($this->cWindDirLast);
	}
	/** Sets the most recent/current wind direction in degrees from North
	 * going clockwise
	 * @param windDirLast The current wind direction to set
	 */
	public function setWindDirLast($windDirLast) {
		$this->cWindDirLast = $windDirLast;
	}
	/** 
	 * Gets the average wind speed over the last minute
	 * @return The average wind speed over the last minute
	 */
	public function getWindSpeedAvgLast_1_min() {
		return $this->convertWindSpeed($this->cWindSpeedAvgLast_1_min, $this->cInputWindSpUnit, $this->cOutputWindSpUnit);
	}
	/**
	 * Sets the average wind speed over the last minute
	 * @param windSpeedAvgLast_1_min The average wind speed over the last minute
	 */
	public function setWindSpeedAvgLast_1_min($windSpeedAvgLast_1_min) {
		$this->cWindSpeedAvgLast_1_min = $windSpeedAvgLast_1_min;
	}
	/**
	 * Returns the average wind direction over the last one minute in degrees
	 * @return The average wind direction over the last 1 minute
	 */
	public function getWindDirScalarAvg_last_1_min() {
		return $this->cWindDirScalarAvg_last_1_min;
	}
	/**
	 * Returns the average wind direction over the last one minute in as cardinal string
	 * @return The wind direction over the last 1 minute
	 */
	public function getWindDirScalarAvgCardinal_last_1_min() {
		return $this->getWindCardinal($this->cWindDirScalarAvg_last_1_min);
	}
	/**
	 * Sets the average wind direction over the last minute in degrees
	 * @param windDirScalarAvg_last_1_min the Wind Direction average over last 1 min to set
	 */
	public function setWindDirScalarAvg_last_1_min($windDirScalarAvg_last_1_min) {
		$this->cWindDirScalarAvg_last_1_min = $windDirScalarAvg_last_1_min;
	}
	/** 
	 * Gets the average wind speed over the last two minutes
	 * @return The average wind speed over the last 2 minutes
	 */
	public function getWindSpeedAvgLast_2_min() {
		return $this->convertWindSpeed($this->cWindSpeedAvgLast_2_min, $this->cInputWindSpUnit, $this->cOutputWindSpUnit);
	}
	/**
	 * Sets the average wind speed over the last 2 minutes
	 * @param windSpeedAvgLast_2_min The average wind speed over the last 2 minutes
	 */
	public function setWindSpeedAvgLast_2_min($windSpeedAvgLast_2_min) {
		$this->cWindSpeedAvgLast_2_min = $windSpeedAvgLast_2_min;
	}
	/**
	 * Returns the average wind direction over the last two minutes in degrees
	 * @return The wind direction over the last 2 minutes
	 */
	public function getWindDirScalarAvg_last_2_min() {
		return $this->cWindDirScalarAvg_last_2_min;
	}
	/**
	 * Returns the average wind direction over the last two minutes as cardinal string
	 * @return The wind direction over the last 2 minutes
	 */
	public function getWindDirScalarAvgCardinal_last_2_min() {
		return $this->getWindCardinal($this->cWindDirScalarAvg_last_2_min);
	}
	/**
	 * Sets the average wind direction over the last two minutes in degrees
	 * @param windDirScalarAvg_last_2_min the Wind Direction average over last 2 min to set
	 */
	public function setWindDirScalarAvg_last_2_min($windDirScalarAvg_last_2_min) {
		$this->cWindDirScalarAvg_last_2_min = $windDirScalarAvg_last_2_min;
	}
	/**
	 * Gets the highest wind speed reached in the last 2 minutes 
	 * @return The highest wind speed reached in the last 2 minutes 
	 */
	public function getWindSpeedHi_last_2_min() {
		return $this->convertWindSpeed($this->cWindSpeedHi_last_2_min, $this->cInputWindSpUnit, $this->cOutputWindSpUnit);
	}
	/**
	 * Sets the highest wind speed reached in the last 2 minutes 
	 * @param windSpeedHi_last_2_min The highest wind speed reached in the last 2 minutes
	 */
	public function setWindSpeedHi_last_2_min($windSpeedHi_last_2_min) {
		$this->cWindSpeedHi_last_2_min = $windSpeedHi_last_2_min;
	}
	/** 
	 * Gets the Wind Direction in degrees when blowing at highest speed in the last 2 minutes
	 * @return The Wind Direction at highest speed in last 2 minutes in degrees 
	 */
	public function getWindDirAtHiSpeedLast_2_min() {
		return $this->cWindDirAtHiSpeedLast_2_min;
	}
	/** 
	 * Gets the Wind Direction as a cardinal string when blowing at highest speed in the last 2 minutes
	 * @return The Wind Direction at highest speed in last 2 minutes as cardinal letters 
	 */
	public function getCardinalWindDirAtHiSpeedLast_2_min() {
		return $this->getWindCardinal($this->cWindDirAtHiSpeedLast_2_min);
	}
	/**
	 * Sets the wind direction of the highest speed during the last 2 minutes 
	 * @param windDirAtHiSpeedLast_2_min The wind direction to set
	 */
	public function setWindDirAtHiSpeedLast_2_min($windDirAtHiSpeedLast_2_min) {
		$this->cWindDirAtHiSpeedLast_2_min = $windDirAtHiSpeedLast_2_min;
	}
	/**
	 * Gets the average wind speed during the last 10 minutes
	 * @return The average wind speed during the last 10 minutes 
	 * 	 
	 */
	public function getWindSpeedAvgLast_10_min() {
		return $this->convertWindSpeed($this->cWindSpeedAvgLast_10_min, $this->cInputWindSpUnit, $this->cOutputWindSpUnit);
	}
	/**
	 * Sets the average wind speed during the last 10 minutes 
	 * @param windSpeedAvgLast_10_min The average wind speed over the last 10 minutes to set
	 */
	public function setWindSpeedAvgLast_10_min($windSpeedAvgLast_10_min) {
		$this->cWindSpeedAvgLast_10_min = $windSpeedAvgLast_10_min;
	}
	/**
	 * Returns the average scalar wind direction during the last 10 minutes in degrees
	 * @return The average scalar wind direction 
	 */
	public function getWindDirScalarAvgLast_10_min() {
		return $this->cWindDirScalarAvgLast_10_min;
	}
	/**
	 * Returns the average scalar wind direction during the last 10 minutes as a cardinal string
	 * @return The average scalar wind direction 
	 */
	public function getCardinalWindDirScalarAvgLast_10_min() {
		return $this->getWindCardinal($this->cWindDirScalarAvgLast_10_min);
	}
	/**
	 * Sets the average scalar wind direction during the last 10 minutes in degrees
	 * @param windDirScalarAvgLast_10_min The average scalar wind direction during the last 10 minutes in degrees
	 */
	public function setWindDirScalarAvgLast_10_min($windDirScalarAvgLast_10_min) {
		$this->cWindDirScalarAvgLast_10_min = $windDirScalarAvgLast_10_min;
	}
	/**
	 * Gets the maximum wind speed during the last 10 minutes 
	 * @return The maximum wind speed during the last 10 minutes
	 */
	public function getWindSpeedHiLast_10_min() {
		return $this->convertWindSpeed($this->cWindSpeedHiLast_10_min, $this->cInputWindSpUnit, $this->cOutputWindSpUnit);
	}
	/**
	 * Sets the maximum wind speed during the last 10 minutes 
	 * @param windSpeedHiLast_10_min The maximum speed speed during the last 10 minutes
	 */
	public function setWindSpeedHiLast_10_min($windSpeedHiLast_10_min) {
		$this->cWindSpeedHiLast_10_min = $windSpeedHiLast_10_min;
	}
	/**
	 * Gets the wind direction of the maximum wind speed during the last 10 minutes in degrees
	 * @return The wind direction of the maximum wind speed during the last 10 minutes in degrees
	 */
	public function getWindDirAtHiSpeedLast_10_min() {
		return $this->cWindDirAtHiSpeedLast_10_min;
	}
	/**
	 * Gets the wind direction of the maximum wind speed during the last 10 minutes as a cardinal string, e.g. NW, E, SE etc. 
	 * @return The wind direction of the maximum wind speed during the last 10 minutes as cardinal string
	 */
	public function getCardinalWindDirAtHiSpeedLast_10_min() {
		return $this->getWindCardinal($this->cWindDirAtHiSpeedLast_10_min);
	}
	/**
	 * Sets the wind direction of the maximum wind speed during the last 10 minutes in degrees. 
	 * @param windDirAtHiSpeedLast_10_min The wind direction of the maximum wind speed during the last 10 minutes in degrees.
	 */
	public function setWindDirAtHiSpeedLast_10_min($windDirAtHiSpeedLast_10_min) {
		$this->cWindDirAtHiSpeedLast_10_min = $windDirAtHiSpeedLast_10_min;
	}
	/**
	 * Gets the size/volume of the rain tipping bucket/spoon installed in the sensor suite. 
	 * @return The tipping bucket/spoon volume
	 */
	public function getRainSize() {
		return $this->cRainSize;
	}
	/**
	 * Sets the size or volume of the rain tipping bucket. 0 is invalid, 1 is 0.01in, 2 is 0.2mm, 3 is 0.1mm and 4 is 0.001in.
	 * @param rainSize The size or volume of the rain bucket to set
	 */
	public function setRainSize($rainSize) {
		
		// These are the WeatherLink rain bucket sizes.
		// rain collector type/size **(0: Reserved, 1: 0.01", 2: 0.2 mm, 3:  0.1 mm, 4: 0.001")**
		
		switch ($rainSize) {
		
		case 1:
			$this->cRainSize =  0.01;
			$this->cRainSizeDefUnit = UNIT_RAIN_IN;
			break;
		case 2:
			$this->cRainSize = 0.2;
			$this->cRainSizeDefUnit = UNIT_RAIN_MM;
			break;
		case 3:
			$this->cRainSize = 0.1; 
			$this->cRainSizeDefUnit = UNIT_RAIN_MM;
			break;
		case 4:
			$this->cRainSize = 0.001;
			$this->cRainSizeDefUnit = UNIT_RAIN_IN;
			break;
		default:
			// Invalid value, so set the size of the bucket to 0.2mm as default
			$this->cRainSize = 0.2;
			$this->cRainSizeDefUnit = UNIT_RAIN_MM;
		}
		 
	}
	/**
	 * Returns the current rain rate at units per hour 
	 * @return The current rain rate
	 */
	public function getRainRateLast() {
		return $this->convertRainMeasure($this->cRainRateLast, $this->cInputRainUnit, $this->cOutputRainUnit);
	}
	/**
	 * Sets the current rain rate at units per hour 
	 * @param rainRateLast The current rain rate
	 */
	public function setRainRateLast($rainRateLast) {
		$this->cRainRateLast = $rainRateLast;
	}
	/**
	 * Returns the highest rain rate over one minute
	 * @return The highest rain rate over one minute
	 */
	public function getRainRateHi() {
		return $this->convertRainMeasure($this->cRainRateHi, $this->cInputRainUnit, $this->cOutputRainUnit);
	}
	/**
	 * Sets the highest rain rate over the last minute
	 * @param rainRateHi The highest rain rate to set over the last one minute
	 */
	public function setRainRateHi($rainRateHi) {
		$this->cRainRateHi = $rainRateHi;
	}
	/**
	 * Returns the rainfall during the last 15 minutes 
	 * @return The rainfall during the last 15 minutes
	 */
	public function getRainfallLast_15_min() {
		return $this->convertRainMeasure($this->cRainfallLast_15_min, $this->cInputRainUnit, $this->cOutputRainUnit);
	}
	/**
	 * Sets the rainfall during the last 15 minutes 
	 * @param rainfallLast_15_min The rainfall during the last 15 minutes
	 */
	public function setRainfallLast_15_min($rainfallLast_15_min) {
		$this->cRainfallLast_15_min = $rainfallLast_15_min;
	}
	/**
	 * Gets the highest rain rate during the last 15 minutes
	 * @return The highest rain rate during the last 15 minutes
	 */
	public function getRainRateHiLast_15_min() {
		return $this->convertRainMeasure($this->cRainRateHiLast_15_min, $this->cInputRainUnit, $this->cOutputRainUnit);
	}
	/**
	 * Sets the highest rain rate during the last 15 minutes
	 * @param rainRateHiLast_15_min The highest rain rate during the last 15 minutes to set
	 */
	public function setRainRateHiLast_15_min($rainRateHiLast_15_min) {
		$this->cRainRateHiLast_15_min = $rainRateHiLast_15_min;
	}
	/** 
	 * Gets the rainfall during the last 60 minutes
	 * @return The rainfall during the last 60 minutes
	 */
	public function getRainfallLast_60_min() {
		return $this->convertRainMeasure($this->cRainfallLast_60_min, $this->cInputRainUnit, $this->cOutputRainUnit);
	}
	/**
	 * Sets the rainfall during the last 60 minutes 
	 * @param rainfallLast_60_min The rainfall during the last 60 minutes
	 */
	public function setRainfallLast_60_min($rainfallLast_60_min) {
		$this->cRainfallLast_60_min = $rainfallLast_60_min;
	}
	/**
	 * Gets the rainfall during the last 24 hours
	 * @return The rainfall during the last 24 hours
	 */
	public function getRainfallLast_24_hr() {
		return $this->convertRainMeasure($this->cRainfallLast_24_hr, $this->cInputRainUnit, $this->cOutputRainUnit);
	}
	/**
	 * Sets the rainfall during the last 24 hours 
	 * @param rainfallLast_24_hr The rainfall during the last 24 hours to set
	 */
	public function setRainfallLast_24_hr($rainfallLast_24_hr) {
		$this->cRainfallLast_24_hr = $rainfallLast_24_hr;
	}
	/**
	 * Gets the total amount of rain during the current rain storm event 
	 * @return The total amount of rain during the current rain storm event
	 */
	public function getRainStorm() {
		return $this->convertRainMeasure($this->cRainStorm, $this->cInputRainUnit, $this->cOutputRainUnit);
	}
	/**
	 * Sets the total amount of rain during the current rain storm event
	 * @param rainStorm The total amount of rain during the current rain storm event to set
	 */
	public function setRainStorm($rainStorm) {
		$this->cRainStorm = $rainStorm;
	}
	/**
	 * Gets the date and time the rain storm/event started
	 * @return A string representing the date and time
	 */
	public function getRainStormStartAt() {
		
		return $this->getDateTimeFromUNIXtimeStamp($this->cRainStormStartAt);
	}
	/**
	 * Sets the date and time the rain storm/event started.
	 * @param rainStormStartAt 	The amount of seconds since the start of 1970 to when the 
	 * 							rain storm/event started - this Unix timestamp
	 */
	public function setRainStormStartAt($rainStormStartAt) {
		$this->cRainStormStartAt = $rainStormStartAt;
	}
	/**
	 * Gets the solar radiation
	 * @return The solar radiation
	 */
	public function getSolarRad() {
		return $this->cSolarRad;
	}
	/**
	 * Sets the solar radiation
	 * @param solarRad The solar radiation to set
	 */
	public function setSolarRad($solarRad) {
		$this->cSolarRad = $solarRad;
	}
	/**
	 * Gets the UV index
	 * @return The UV Index
	 */
	public function getUvIndex() {
		return $this->cUvIndex;
	}
	/**
	 * Sets the UV index
	 * @param uvIndex The UV Index to set
	 */
	public function setUvIndex($uvIndex) {
		$this->cUvIndex = $uvIndex;
	}
	/**
	 * Gets the configured radio receiver state
	 * @return The radio receiver state
	 */
	public function getRxState() {
		return $this->cRxState;
	}
	/**
	 * Sets the configured radio receiver state
	 * @param rxState The radio receiver state to set
	 */
	public function setRxState($rxState) {
		$this->cRxState = $rxState;
	}
	/**
	 * Gets the status of the battery
	 * @return The status of the battery flag
	 */
	public function getTransBatteryFlag() {
		return $this->cTransBatteryFlag;
	}
	/**
	 * Sets the status of the battery
	 * @param transBatteryFlag The battery status flag to set
	 */
	public function setTransBatteryFlag($transBatteryFlag) {
		$this->cTransBatteryFlag = $transBatteryFlag;
	}
	/**
	 * Gets the daily rainfall 
	 * @return The daily rainfall
	 */ 
	public function getRrainfallDaily() {
		return $this->convertRainMeasure($this->cRrainfallDaily, $this->cInputRainUnit, $this->cOutputRainUnit);
	}
	/**
	 * Sets the daily rainfall
	 * @param rainfallDaily The daily rainfall to set
	 */
	public function setRrainfallDaily($rainfallDaily) {
		$this->cRrainfallDaily = $rainfallDaily;
	}
	/**
	 * Gets the monthly rainfall 
	 * @return The monthly rainfall
	 */
	public function getRainfallMonthly() {
		return $this->convertRainMeasure($this->cRainfallMonthly, $this->cInputRainUnit, $this->cOutputRainUnit);
	}
	/**
	 * Sets the monthly rainfall 
	 * @param rainfallMonthly The monthly rainfall to set
	 */
	public function setRainfallMonthly($rainfallMonthly) {
		$this->cRainfallMonthly = $rainfallMonthly;
	}
	/**
	 * Gets the yearly rainfall 
	 * @return The Yearly Rainfall
	 */
	public function getRainfallYear() {
		return $this->convertRainMeasure($this->cRainfallYear, $this->cInputRainUnit, $this->cOutputRainUnit);
	}
	/**
	 * Sets the yearly rainfall 
	 * @param rainfallYear The yearly rainfall
	 */
	public function setRainfallYear($rainfallYear) {
		$this->cRainfallYear = $rainfallYear;
	}
	/**
	 * Gets the total amount of rain during the last rain storm event 
	 * @return Total amount of rain during the last rain storm event
	 */
	public function getRainStormLast() {
		return $this->convertRainMeasure($this->cRainStormLast, $this->cInputRainUnit, $this->cOutputRainUnit);
	}
	/**
	 * Sets the total amount of rain during the last rain storm event
	 * @param rainStormLast The total amount of rain during the last rain storm event to set
	 */
	public function setRainStormLast($rainStormLast) {
		$this->cRainStormLast = $rainStormLast;
	}
	/**
	 * Gets the UNIX time stamp in seconds indicating when the last rain
	 * storm started
	 * @return UNIX time stamp of when the last rain storm started
	 */
	public function getRainStormLastStartAtSeconds() {
		return $this->cRainStormLastStartAt;
	}
	/**
	 * Gets a string representing the date and time the last rain storm started
	 * @return The date and time when the last rain storm started
	 */
	public function getRainStormLastStartAt() {
		return $this->getDateTimeFromUNIXtimeStamp($this->cRainStormLastStartAt);
	}
	/**
	 * Sets the date and time when the last rain storm stated using the 
	 * UNIX time stamp in seconds since 1970
	 * @param rainStormLastStartAt The date and time last rain storm started in seconds
	 */
	public function setRainStormLastStartAt($rainStormLastStartAt) {
		$this->cRainStormLastStartAt = $rainStormLastStartAt;
	}
	/**
	 * Gets the UNIX time stamp in seconds indicating when the last rain
	 * storm ended
	 * @return UNIX time stamp of when the last rain storm ended
	 */
	public function getRainStormLastEndAtSeconds() {
		return $this->cRainStormLastEndAt;
	}
	/**
	 * Gets a string representing the date and time the last rain storm ended
	 * @return The date and time when the last rain storm ended
	 */
	public function getRainStormLastEndAt() {
		return $this->getDateTimeFromUNIXtimeStamp($this->cRainStormLastEndAt);
	}
	/**
	 * Sets the date and time when the last rain storm ended using the 
	 * UNIX time stamp in seconds since 1970
	 * @param rainStormLastStartAt The date and time last rain storm ended in seconds
	 */
	public function setRainStormLastEndAt($rainStormLastEndAt) {
		$this->cRainStormLastEndAt = $rainStormLastEndAt;
	}
	
	/** 
	 * Gets the soil temperature for sensor slot 1
	 * @return The soil temperature
	 */
	public function getSoilTemp1() {
		return $this->convertTemp($this->cSoilTemp1, $this->cInputTempUnit, $this->cOutputTempUnit);
	}
	/**
	 * Sets the soil temperature for sensor slot 1
	 * @param The soil temperature to set
	 */
	public function setSoilTemp1($soilTemp1) {
		$this->cSoilTemp1 = $soilTemp1;
	}
	/** 
	 * Gets the soil temperature for sensor slot 2
	 * @return The soil temperature
	 */
	public function getTemp_2() {
		return $this->convertTemp($this->cTemp_2, $this->cInputTempUnit, $this->cOutputTempUnit);
	}
	/**
	 * Sets the soil temperature for sensor slot 2
	 * @param The soil temperature to set
	 */
	public function setTemp_2($temp_2) {
		$this->cTemp_2 = $temp_2;
	}
	/** 
	 * Gets the soil temperature for sensor slot 3
	 * @return The soil temperature
	 */
	public function getTemp_3() {
		return $this->convertTemp($this->cTemp_3, $this->cInputTempUnit, $this->cOutputTempUnit);
	}
	/**
	 * Sets the soil temperature for sensor slot 3
	 * @param The soil temperature to set
	 */
	public function setTemp_3($temp_3) {
		$this->cTemp_3 = $temp_3;
	}
	/** 
	 * Gets the soil temperature for sensor slot 4
	 * @return The soil temperature
	 */
	public function getTemp_4() {
		return $this->convertTemp($this->cTemp_4, $this->cInputTempUnit, $this->cOutputTempUnit);
	}
	/**
	 * Sets the soil temperature for sensor slot 4
	 * @param The soil temperature to set
	 */
	public function setTemp_4($temp_4) {
		$this->cTemp_4 = $temp_4;
	}
	/**
	 * Gets the soil moisture for slot 1
	 * @return The Soil Moisture
	 */
	public function getMoistSoil1() {
		return $this->cMoistSoil1;
	}
	/**
	 * Sets the soil moisture for slot 1
	 * @param moistSoil1 The Soil Moisture to set
	 */
	public function setMoistSoil1($moistSoil1) {
		$this->cMoistSoil1 = $moistSoil1;
	}
	/**
	 * Gets the soil moisture for slot 2
	 * @return The Soil Moisture
	 */
	public function getMpistSoil2() {
		return $this->cMpistSoil2;
	}
	/**
	 * Sets the soil moisture for slot 2
	 * @param moistSoil1 The Soil Moisture to set
	 */
	public function setMpistSoil2($mpistSoil2) {
		$this->cMpistSoil2 = $mpistSoil2;
	}
	/**
	 * Gets the soil moisture for slot 3
	 * @return The Soil Moisture
	 */
	public function getMoistSoil3() {
		return $this->cMoistSoil3;
	}
	/**
	 * Sets the soil moisture for slot 3
	 * @param moistSoil1 The Soil Moisture to set
	 */
	public function setMoistSoil3($moistSoil3) {
		$this->cMoistSoil3 = $moistSoil3;
	}
	/**
	 * Gets the soil moisture for slot 4
	 * @return The Soil Moisture
	 */
	public function getMoistSoil4() {
		return $this->cMoistSoil4;
	}
	/**
	 * Sets the soil moisture for slot 4
	 * @param moistSoil1 The Soil Moisture to set
	 */
	public function setMoistSoil4($moistSoil4) {
		$this->cMoistSoil4 = $moistSoil4;
	}
	/**
	 * Gets the leaf wetness for slot 1
	 * @return The leaf wetness
	 */
	public function getWetleaf1() {
		return $this->cWetleaf1;
	}
	/**
	 * Sets the leaf wetness for slot 1
	 * @param wetleaf1 The leaf wetness to set
	 */
	public function setWetleaf1($wetleaf1) {
		$this->cWetleaf1 = $wetleaf1;
	}
	/**
	 * Gets the leaf wetness for slot 2
	 * @return The leaf wetness
	 */
	public function getWetleaf2() {
		return $this->cWetleaf2;
	}
	/**
	 * Sets the leaf wetness for slot 2
	 * @param wetleaf2 The leaf wetness to set
	 */
	public function setWetleaf2($wetleaf2) {
		$this->cWetleaf2 = $wetleaf2;
	}
	/**
	 * Gets the indoor temperature
	 * @return The indoor temperature
	 */
	public function getInTemp() {
		return $this->convertTemp($this->cInTemp, $this->cInputTempUnit, $this->cOutputTempUnit);
	}
	/**
	 * Sets the indoor temperature
	 * @param inTemp The indoor temperature to set
	 */
	public function setInTemp($inTemp) {
		$this->cInTemp = $inTemp;
	}
	/**
	 * Gets the apparent feels like temperature according to  the Australian Bureau of Meteorology citing  
	 * Norms of apparent temperature in Australia, Aust. Met. Mag., 1994, Vol 43, 1-161.
	 * @return	The apparent temperature
	 */
	public function getInAppTemp() {
		return $this->convertTemp($this->calcAppTemp($this->convertTemp($this->cInTemp, $this->cInputTempUnit, UNIT_TEMP_CELSIUS), $this->cInHum, 0), UNIT_TEMP_CELSIUS, $this->cOutputTempUnit);
	}
	/**
	 * Gets the indoor humidity
	 * @return The indoor humidity
	 */
	public function getInHum() {
		return round($this->cInHum, $this->cHumPrec);
	}
	/**
	 * Sets the indoor humidity
	 * @param inHum The indoor humidity to set
	 */
	public function setInHum($inHum) {
		$this->cInHum = $inHum;
	}
	/**
	 * Gets the indoor dew point
	 * @return The indoor dew point
	 */
	public function getInDewPoint() {
		return $this->convertTemp($this->cInDewPoint, $this->cInputTempUnit, $this->cOutputTempUnit);
	}
	/**
	 * Sets the indoor dew point
	 * @param inDewPoint The indoor dew point to set
	 */
	public function setInDewPoint($inDewPoint) {
		$this->cInDewPoint = $inDewPoint;
	}
	/**
	 * Gets the indoor heat index
	 * @return The indoor heat index
	 */
	public function getInHeatIndex() {
		return $this->convertTemp($this->cInHeatIndex, $this->cInputTempUnit, $this->cOutputTempUnit);
	}
	/**
	 * Sets the indoor heat index
	 * @param inHeatIndex The indoor heat index to set
	 */
	public function setInHeatIndex($inHeatIndex) {
		$this->cInHeatIndex = $inHeatIndex;
	}
	
	/**	 
	 * Gets the barometer reading with elevation adjusted
	 * @return The barometer reading
	 */
	public function getBarSeaLevel() {
		return $this->convertPressure($this->cBarSeaLevel, $this->cInputBarUnit, $this->cOutputBarUnit);
	}
	/**
	 * Sets the barometer reading with elevation adjusted
	 * @param barSeaLevel	The barometer reading to set
	 */
	public function setBarSeaLevel($barSeaLevel) {
		$this->cBarSeaLevel = $barSeaLevel;
	}
	/**
	 * Gets the current 3 hour barometer trend
	 * @return The barometer trend
	 */
	public function getBarTrend() {
		return $this->convertPressure($this->cBarTrend, $this->cInputBarUnit, $this->cOutputBarUnit);
	}
	/**
	 * Sets the current 3 hour barometer trend
	 * @param barTrend	The barometer trend to set
	 */
	public function setBarTrend($barTrend) {
		$this->cBarTrend = $barTrend;
	}
	/**
	 * Gets the raw barometer reading
	 * @return the raw barometer reading
	 */
	public function getBarAbsolute() {
		return $this->convertPressure($this->cBarAbsolute, $this->cInputBarUnit, $this->cOutputBarUnit);
	}
	/**
	 * Sets the raw barometer reading
	 * @param barAbsolute The raw barometer reading to set
	 */
	public function setBarAbsolute($barAbsolute) {
		$this->cBarAbsolute = $barAbsolute;
	}
	/**
	 * Sets the date and time format string. 
	 * @param	format	The string representing the date and time format
	 * @return	Nothing
	 */
	public function setDateTimeFormat($format) {
		$this->cDateFormat = $format;
	}
	/**
	 * Get the date and time format string
	 * @return	FormatString	The string representing the date and time format
	 */
	public function getDateTimeFormat() {
		return $this->cDateFormat;
	}

	/**
	 * Sets the latency of the previous observation data packet.
	 * @param	Integer		Latency in milliseconds
	 */
	public function setPrevPacketLatency($prevPacketLatency) {
		$this->cPrevPacketLatency = $prevPacketLatency;
	}

	/**
	 * Gets the latency of the previous observation data packet.
	 * @return	Integer		Latency in milliseconds
	 */
	public function getPrevPacketLatency() {
		return $this->cPrevPacketLatency;
	}

	/**
	 * Sets the receive time of the observation data packet.
	 * @param	String	Date and Time in YYYY-MM-DD hh:mm:ss format
	 */
	public function setObservationReceiveTime($ObservationReceiveTime) {
		$this->cObservationReceiveTime = $ObservationReceiveTime;
	}

	/**
	 * Gets the receive time of the observation data packet.
	 * @return	String	Date and Time in YYYY-MM-DD hh:mm:ss format
	 */
	public function getObservationReceiveTime() {
		return $this->cObservationReceiveTime;
	}


	// UNITS OF MEASURE HANDLING --------------------------------------------------------------------------------------------

	/** 
	 * Gets the input unit for temperature. This is the unit of measure expected
	 * from the source sensor data.
	 * @return	The unit of measure for temperature used by the source data
	 */
	public function getInputTempUnit() {
		return $this->cInputTempUnit;
	}
	/**
	 * Sets the input unit for temperature. This is the unit of measure expected
	 * from the source sensor data.
	 * @param 	inputTempUnit 	The unit of measure for temperature used by the source data
	 */
	public function setInputTempUnit($inputTempUnit) {
		$this->cInputTempUnit = $inputTempUnit;
	}
	/**
	 * Gets the input unit for wind speed. This is the unit of measure expected
	 * from the source sensor data.
	 * @return 	The unit of measure for wind speed used by the source data
	 */
	public function getInputWindSpUnit() {
		return $this->cInputWindSpUnit;
	}
	/**
	 * Sets the input unit for wind speed. This is the unit of measure expected
	 * from the source sensor data.
	 * @param 	inputWindSpUnit		The unit of measure for wind speed used by the source data
	 */
	public function setInputWindSpUnit($inputWindSpUnit) {
		$this->cInputWindSpUnit = $inputWindSpUnit;
	}
	/**
	 * Gets the input unit for wind direction. This is the unit of measure expected
	 * from the source sensor data.
	 * @return 	The unit of measure for wind speed used by the source data
	 */
	public function getInputWindDirUnit() {
		return $this->cInputWindDirUnit;
	}
	/**
	 * Sets the input unit for wind direction. This is the unit of measure expected
	 * from the source sensor data.
	 * @param 	inputWindDirUnit 	The unit of measure for wind speed used by the source data
	 */
	public function setInputWindDirUnit($inputWindDirUnit) {
		$this->cInputWindDirUnit = $inputWindDirUnit;
	}
	/**
	 * Gets the input unit for barometric pressure. This is the unit of measure expected
	 * from the source sensor data.
	 * @return 	The unit of measure for barometric pressure used by the source data
	 */
	public function getInputBarUnit() {
		return $this->cInputBarUnit;
	}
	/**
	 * Sets the input unit for barometric pressure. This is the unit of measure expected
	 * from the source sensor data.
	 * @param 	inputBarUnit 	The unit of measure for barometric pressure used by the source data
	 */
	public function setInputBarUnit($inputBarUnit) {
		$this->cInputBarUnit = $inputBarUnit;
	}
	/**
	 * Gets the input unit for rain. This is the unit of measure expected
	 * from the source sensor data.
	 * @return 	The unit of measure for rain used by the source data
	 */
	public function getInputRainUnit() {
		return $this->cInputRainUnit;
	}
	/**
	 * Sets the input unit for rain. This is the unit of measure expected
	 * from the source sensor data.
	 * @param 	inputRainUnit 	The unit of measure for rain used by the source data
	 */
	public function setInputRainUnit($inputRainUnit) {
		$this->cInputRainUnit = $inputRainUnit;
	}
	/**
	 * Gets the output unit for temperature. This is the unit of measure to be used
	 * for output from this object. 
	 * @return 	The unit of measure for temperature required for output
	 */
	public function getOutputTempUnit() {
		return $this->cOutputTempUnit;
	}
	/**
	 * Sets the output unit for temperature. This is the unit of measure to be used
	 * for output from this object. 
	 * @param 	outputTempUnit 	The unit of measure for temperature required for output
	 */
	public function setOutputTempUnit($outputTempUnit) {
		$this->cOutputTempUnit = $outputTempUnit;
	}
	/**
	 * Gets the output unit for wind speed. This is the unit of measure to be used
	 * for output from this object. 
	 * @return 	The unit of measure for wind speed required for output
	 */
	public function getOutputWindSpUnit() {
		return $this->cOutputWindSpUnit;
	}
	/**
	 * Sets the output unit for wind speed. This is the unit of measure to be used
	 * for output from this object. 
	 * @param 	outputWindSpUnit 	The unit of measure for wind speed required for output
	 */
	public function setOutputWindSpUnit($outputWindSpUnit) {
		$this->cOutputWindSpUnit = $outputWindSpUnit;
	}
	/**
	 * Gets the output unit for wind direction. This is the unit of measure to be used
	 * for output from this object. 
	 * @return 	The unit of measure for wind direction required for output
	 */
	public function getOutputWindDirUnit() {
		return $this->cOutputWindDirUnit;
	}
	/**
	 * Sets the output unit for wind direction. This is the unit of measure to be used
	 * for output from this object. 
	 * @param 	outputWindDirUnit 	The unit of measure for wind direction required for output
	 */
	public function setOutputWindDirUnit($outputWindDirUnit) {
		$this->cOutputWindDirUnit = $outputWindDirUnit;
	}
	/**
	 * Gets the output unit for barometric pressure. This is the unit of measure to be used
	 * for output from this object. 
	 * @return	The unit of measure for barometric pressure required for output
	 */
	public function getOutputBarUnit() {
		return $this->cOutputBarUnit;
	}
	/**
	 * Sets the output unit for barometric pressure. This is the unit of measure to be used
	 * for output from this object. 
	 * @param 	outputBarUnit 	The unit of measure for barometric pressure required for output
	 */
	public function setOutputBarUnit($outputBarUnit) {
		$this->cOutputBarUnit = $outputBarUnit;
	}
	/**
	 * Sets the output unit for rain. This is the unit of measure to be used
	 * for output from this object. 
	 * @return 	The unit of measure for rain required for output
	 */
	public function getOutputRainUnit() {
		return $this->cOutputRainUnit;
	}
	/**
	 * Sets the output unit for rain. This is the unit of measure to be used
	 * for output from this object. 
	 * @param 	outputRainUnit 	The unit of measure for rain required for output
	 */
	public function setOutputRainUnit($outputRainUnit) {
		$this->cOutputRainUnit = $outputRainUnit;
	}

	/**
	 * Sets the amount of decimal places that should be displayed for temperature units
	 * @param	Integer		The amount of decimal places to show
	 */
	public function setTempPrecision($decimalPlaces) {
		$this->cTempPrec = $decimalPlaces;
	}

	/** Unit Decimal Places, Rounding and Precision properties/getter and setters */
	/** ------------------------------------------------------------------------- */

	/**
	 * Gets the amount of decimal places that should be displayed for temperature units
	 * @return	Integer		The amount of decimal places to show
	 */
	public function getTempPrecision() {
		return $this->cTempPrec;
	}

	/**
	 * Sets the amount of decimal places that should be displayed for wind speed units
	 * @param	Integer		The amount of decimal places to show
	 */
	public function setWindSpeedPrecision($decimalPlaces) {
		$this->cWindSpPrec = $decimalPlaces;
	}

	/**
	 * Gets the amount of decimal places that should be displayed for wind speed units
	 * @return	Integer		The amount of decimal places to show
	 */
	public function getWindSpeedPrecision() {
		return $this->cWindSpPrec;
	}
	
	/**
	 * Sets the amount of decimal places that should be displayed for Barometer units
	 * @param	Integer		The amount of decimal places to show
	 */
	public function setBarometerPrecision($decimalPlaces) {
		$this->cBarPrec = $decimalPlaces;
	}

	/**
	 * Gets the amount of decimal places that should be displayed for Barometer units
	 * @return	Integer		The amount of decimal places to show
	 */
	public function getBarometerPrecision() {
		return $this->cBarPrec;
	}

	/**
	 * Sets the amount of decimal places that should be displayed for Rain units
	 * @param	Integer		The amount of decimal places to show
	 */
	public function setRainPrecision($decimalPlaces) {
		$this->cRainPrec = $decimalPlaces;
	}

	/**
	 * Gets the amount of decimal places that should be displayed for Rain units
	 * @return	Integer		The amount of decimal places to show
	 */
	public function getRainPrecision() {
		return $this->cRainPrec;
	}
	
	/**
	 * Sets the amount of decimal places that should be displayed for Humidity units
	 * @param	Integer		The amount of decimal places to show
	 */
	public function setHumidityPrecision($decimalPlaces) {
		$this->cHumPrec = $decimalPlaces;
	}

	/**
	 * Gets the amount of decimal places that should be displayed for Humidity units
	 * @return	Integer		The amount of decimal places to show
	 */
	public function getHumidityPrecision() {
		return $this->cHumPrec;
	}




	/** --------------------------------------------------------------------------------- */

	/**
	 * Returns a JSON string containing the observations with the correct output units.
	 * @return	String a JSON encoded string containing the observation in this object
	 */
	public function getJSON() {
		
		// Assign the correct values for the required units to the jsonObject
		// associative array

		$jsonObject['Lsid'] = $this->getLsid();                      
		$jsonObject['DataStructureType'] = $this->getDataStructureType();          
		$jsonObject['TransmitterID'] = $this->getTransmitterID();              
		$jsonObject['AppTemp'] = $this->getAppTemp();
		$jsonObject['Temp'] = $this->getTemp();                       
		$jsonObject['Hum'] = $this->getHum();                        
		$jsonObject['DewPoint'] = $this->getDewPoint();                   
		$jsonObject['WetBulb'] = $this->getWetBulb();                    
		$jsonObject['HeatIndex'] = $this->getHeatIndex();                  
		$jsonObject['WindChill'] = $this->getWindChill();                  
		$jsonObject['ThwIndex'] = $this->getThwIndex();                   
		$jsonObject['ThswIndex'] = $this->getThswIndex();                  
		$jsonObject['WindPpeedLast'] = $this->getWindPpeedLast();              
		$jsonObject['WindDirLast'] = ($this->cOutputWindDirUnit==UNIT_WINDDIRECTION_DEGREES)?$this->getWindDirLast():$this->getWindDirLastCardinal();                
		$jsonObject['WindSpeedAvgLast_1_min'] = $this->getWindSpeedAvgLast_1_min();     
		$jsonObject['WindDirScalarAvg_last_1_min'] = ($this->cOutputWindDirUnit==UNIT_WINDDIRECTION_DEGREES)?$this->getWindDirScalarAvg_last_1_min():$this->getWindDirScalarAvgCardinal_last_1_min();
		$jsonObject['WindSpeedAvgLast_2_min'] = $this->getWindSpeedAvgLast_2_min();     
		$jsonObject['WindDirScalarAvg_last_2_min'] = ($this->cOutputWindDirUnit==UNIT_WINDDIRECTION_DEGREES)?$this->getWindDirScalarAvg_last_2_min():$this->getWindDirScalarAvgCardinal_last_2_min();
		$jsonObject['WindSpeedHi_last_2_min'] = $this->getWindSpeedHi_last_2_min();     
		$jsonObject['WindDirAtHiSpeedLast_2_min'] = ($this->cOutputWindDirUnit==UNIT_WINDDIRECTION_DEGREES)?$this->getWindDirAtHiSpeedLast_2_min():$this->getCardinalWindDirAtHiSpeedLast_2_min(); 
		$jsonObject['WindSpeedAvgLast_10_min'] = $this->getWindSpeedAvgLast_10_min();    
		$jsonObject['WindDirScalarAvgLast_10_min'] = ($this->cOutputWindDirUnit==UNIT_WINDDIRECTION_DEGREES)?$this->getWindDirScalarAvglast_10_min():$this->getCardinalWindDirScalarAvgLast_10_min();
		$jsonObject['WindSpeedHiLast_10_min'] = $this->getWindSpeedHiLast_10_min();     
		$jsonObject['WindDirAtHiSpeedLast_10_min'] = ($this->cOutputWindDirUnit==UNIT_WINDDIRECTION_DEGREES)?$this->getWindDirAtHiSpeedLast_10_min():$this->getCardinalWindDirAtHiSpeedLast_10_min(); 
		$jsonObject['RainSize'] = $this->getRainSize();                   
		$jsonObject['RainRateLast'] = $this->getRainRateLast();               
		$jsonObject['RainRateHi'] = $this->getRainRateHi();                 
		$jsonObject['RainfallLast_15_min'] = $this->getRainfallLast_15_min();        
		$jsonObject['RainRateHiLast_15_min'] = $this->getRainRateHiLast_15_min();      
		$jsonObject['RainfallLast_60_min'] = $this->getRainfallLast_60_min();        
		$jsonObject['RainfallLast_24_hr'] = $this->getRainfallLast_24_hr();         
		$jsonObject['RainStorm'] = $this->getRainStorm();                  
		$jsonObject['RainStormStartAt'] = $this->getRainStormStartAt();           
		$jsonObject['SolarRad'] = $this->getSolarRad();                   
		$jsonObject['UvIndex'] = $this->getUvIndex();                    
		$jsonObject['RxState'] = $this->getRxState();                    
		$jsonObject['TransBatteryFlag'] = $this->getTransBatteryFlag();           
		$jsonObject['RainfallDaily'] = $this->getRrainfallDaily();             
		$jsonObject['RainfallMonthly'] = $this->getRainfallMonthly();            
		$jsonObject['RainfallYear'] = $this->getRainfallYear();               
		$jsonObject['RainStormLast'] = $this->getRainStormLast();              
		$jsonObject['RainStormLastStartAt'] = $this->getRainStormLastStartAt();       
		$jsonObject['RainStormLastEndAt'] = $this->getRainStormLastEndAt();         
		$jsonObject['SoilTemp1'] = $this->getSoilTemp1();                  
		$jsonObject['Temp_3'] = $this->getTemp_3();                     
		$jsonObject['Temp_4'] = $this->getTemp_4();                     
		$jsonObject['Temp_2'] = $this->getTemp_2();                     
		$jsonObject['MoistSoil1'] = $this->getMoistSoil1();                 
		$jsonObject['MpistSoil2'] = $this->getMpistSoil2();                 
		$jsonObject['MoistSoil3'] = $this->getMoistSoil3();                 
		$jsonObject['MoistSoil4'] = $this->getMoistSoil4();                 
		$jsonObject['Wetleaf1'] = $this->getWetleaf1();                   
		$jsonObject['Wetleaf2'] = $this->getWetleaf2();  
		$jsonObject['InAppTemp'] = $this->getInAppTemp();                 
		$jsonObject['InTemp'] = $this->getInTemp();                     
		$jsonObject['InHum'] = $this->getInHum();                      
		$jsonObject['InDewPoint'] = $this->getInDewPoint();                 
		$jsonObject['InHeatIndex'] = $this->getInHeatIndex();                
		$jsonObject['BarSeaLevel'] = $this->getBarSeaLevel();                
		$jsonObject['BarTrend'] = $this->getBarTrend();                   
		$jsonObject['BarAbsolute'] = $this->getBarAbsolute();    
		$jsonObject['prev_packet_latency'] = $this->getPrevPacketLatency();                   
		$jsonObject['last_packet_rec_time'] = $this->getObservationReceiveTime();     


		// encode the josn and return it as string
		return json_encode($jsonObject);	
		
	}

	/**
	 * Calculates the apparent temperature according to the Australian Bureau of Meteorology citing  
	 * Norms of apparent temperature in Australia, Aust. Met. Mag., 1994, Vol 43, 1-161. 
	 * @param 	Temp		Temperature in degrees celcius
	 * @param 	Hum			Percentage relative humidity	
	 * @param 	WindSpeed	Wind speed in metres per second (m/s)
	 * @return				The feels like apparent temperature
	 */
	public function calcAppTemp($Temp, $Hum, $WindSpeed) {
		
		$lWaterVapourPressure;
		
		// Work out the Water Vapour Pressure
		$lWaterVapourPressure = ($Hum/100) * 6.105 * exp(17.27 * $Temp/(237.7+$Temp));
		
		// Work out the apparent temperature
		return ($Temp + (0.33*$lWaterVapourPressure) - (0.70*$WindSpeed) - 4.00);
		
	}
	

	// Private Methods ------------------------------------------------------------------------------------------------------

	// Unit conversion functions
	
	/**
	 * Converts between different units of temperature for internal use	
	 * @param Temp	The decimal value of the temperature
	 * @param From	The unit to convert from, use the Unit_Temp constants in WeatherUnits.php
	 * @param To	The unit to convert to, use the Unit_Temp constants in WeatherUnits.php
	 * @return		The converted value as a decimal float
	 */
	private function convertTemp($Temp, $From, $To) {
		
		// Don't perform convertsions on null temperatures
		if ($Temp===null) {return null;}

		$lConvertedTemp = $Temp;
		
		switch ($From) {
		
		case UNIT_TEMP_FAHRENHEIT:
			if ($To == UNIT_TEMP_CELSIUS) {$lConvertedTemp = (($Temp - 32) * (5.0/9.0));}
			break;
		case UNIT_TEMP_CELSIUS:
			if ($To == UNIT_TEMP_FAHRENHEIT) {$lConvertedTemp = (($Temp * (9.0/5.0)) + 32);}
			break;
		}
		
		return round($lConvertedTemp, $this->cTempPrec);
	
	}
	
	/**
	 * Converts between different units of speed for internal use
	 * @param Speed	The decimal value of the speed
	 * @param From	The unit to convert from, use the Unit_WindSpeed constants in WeatherUnits.php
	 * @param To	The unit to convert to, use the Unit_WindSpeed constants in WeatherUnits.php
	 * @return		The converted value as a decimal float
	 */
	private function convertWindSpeed($Speed, $From, $To) {

		// Don't perform conversions on null wind speeds
		if ($Speed === null) {return null;}

		$lConvertedSpeed = $Speed;
		
		switch ($From) {
		case UNIT_WINDSPEED_KPH:
			
			switch ($To) {
			case UNIT_WINDSPEED_MPH:
				$lConvertedSpeed = ($Speed / 1.609);
			break;
			case UNIT_WINDSPEED_KNOTS:
				$lConvertedSpeed = ($Speed / 1.852);
			break;
			case UNIT_WINDSPEED_MPS:	
				$lConvertedSpeed = ($Speed / 3.6);
			break;
			}
			
		break;
		case UNIT_WINDSPEED_MPH:
		
			switch ($To) {
			case UNIT_WINDSPEED_KPH:
				$lConvertedSpeed = ($Speed * 1.609);
			break;
			case UNIT_WINDSPEED_KNOTS:
				$lConvertedSpeed = ($Speed / 1.151);
			break;
			case UNIT_WINDSPEED_MPS:	
				$lConvertedSpeed = ($Speed / 2.237);
			break;
			}
			
		break;
		case UNIT_WINDSPEED_KNOTS:
		
			switch ($To) {
			case UNIT_WINDSPEED_KPH:
				$lConvertedSpeed = ($Speed * 1.852);
			break;
			case UNIT_WINDSPEED_MPH:
				$lConvertedSpeed = ($Speed * 1.151);
			break;
			case UNIT_WINDSPEED_MPS:	
				$lConvertedSpeed = ($Speed / 1.944);
			break;
			}
			
		break;
		case UNIT_WINDSPEED_MPS:	
		
			switch ($To) {
			case UNIT_WINDSPEED_KPH:
				$lConvertedSpeed = ($Speed * 3.6);
			break;
			case UNIT_WINDSPEED_MPH:
				$lConvertedSpeed = ($Speed * 2.237);
			break;
			case UNIT_WINDSPEED_KNOTS:
				$lConvertedSpeed = ($Speed * 1.944);
			break;
			}
			
		break;
		}
		
		return round($lConvertedSpeed, $this->cWindSpPrec);
		
	}
	
	/**
	 * Returns the cardinal wind direction string for a given direction in degrees from North working clockwise
	 * @param 	Direction		Wind Direction in degrees
	 * @return	Direction as N, NNE, ...., NW, NNW etc etc. Returns an empty string if there
	 * 			were issues
	 */
	private function getWindCardinal($Direction) {
		
		if (($Direction >= 0 && $Direction < 11.25)||($Direction > 348.75 && $Direction <= 360)) {
			return "N";
		} else if ($Direction > 11.25 && $Direction < 33.75 ) {
			return "NNE";
		} else if ($Direction > 33.75 && $Direction < 56.25 ) {
			return "NE";
		} else if ($Direction > 56.25 && $Direction < 78.75 ) {
			return "ENE";
		} else if ($Direction > 78.75 && $Direction < 101.25 ) {
			return "E";
		} else if ($Direction > 101.25 && $Direction < 123.75 ) {
			return "ESE";
		} else if ($Direction > 123.75 && $Direction < 146.25 ) {
			return "SE";
		} else if ($Direction > 146.25 && $Direction < 168.75 ) {
			return "SSE";
		} else if ($Direction > 168.75 && $Direction < 191.25 ) {
			return "S";
		} else if ($Direction > 191.25 && $Direction < 213.75 ) {
			return "SSW";
		} else if ($Direction > 213.75 && $Direction < 236.25 ) {
			return "SW";
		} else if ($Direction > 236.255 && $Direction < 258.75 ) {
			return "WSW";
		} else if ($Direction > 258.75 && $Direction < 281.25 ) {
			return "W";
		} else if ($Direction > 281.25 && $Direction < 303.75 ) {
			return "WNW";
		} else if ($Direction > 303.75 && $Direction < 326.25 ) {
			return "NW";
		} else if ($Direction > 326.25 && $Direction < 348.75 ) {
			return "NNW";
		}
		
		return "";
		
	}

	/**
	 * Converts between different units of pressure for internal use
	 * @param PressureValue	The decimal value of the pressure
	 * @param From			The unit to convert from, use the Unit_Barometer constants in WeatherUnits.php
	 * @param To			The unit to convert to, use the Unit_Baronmeter constants in WeatherUnits.php
	 * @return				The converted value as a decimal float
	 */
	private function convertPressure($PressureValue, $From, $To) {
	
		// Don't perform conversions on null pressures
		if ($PressureValue===null) {return null;}

		$lConvertedValue = $PressureValue;
		
		switch ($From) {
		case UNIT_BAROMETER_HPA:
			switch ($To) {
			case UNIT_BAROMETER_INCHES:
					$lConvertedValue = ($PressureValue / 33.864);
				break;
			case UNIT_BAROMETER_MILIMETERS:
					$lConvertedValue = ($PressureValue / 1.333);
				break;
			}
			
			break;
		case UNIT_BAROMETER_INCHES:
			switch ($To) {
			case UNIT_BAROMETER_HPA:
					$lConvertedValue = ($PressureValue * 33.864);
				break;
			case UNIT_BAROMETER_MB:
					$lConvertedValue = ($PressureValue * 33.864);
				break;
			case UNIT_BAROMETER_MILIMETERS:
					$lConvertedValue = ($PressureValue * 25.4);
				break;
			}
			break;
		case UNIT_BAROMETER_MB:
			switch ($To) {
			case UNIT_BAROMETER_INCHES:
					$lConvertedValue = ($PressureValue / 33.864);
				break;
			case UNIT_BAROMETER_MILIMETERS:
					$lConvertedValue = ($PressureValue / 1.333);
				break;
			}
			break;
		case UNIT_BAROMETER_MILIMETERS:
			switch ($To) {
			case UNIT_BAROMETER_HPA:
					$lConvertedValue = ($PressureValue * 1.333);
				break;
			case UNIT_BAROMETER_INCHES:
					$lConvertedValue = ($PressureValue / 25.4);
				break;
			case UNIT_BAROMETER_MB:
					$lConvertedValue = ($PressureValue * 1.333);
				break;
			}
			break;
		}
		
		return round($lConvertedValue, $this->cBarPrec);
		
	}
		
	/**
	 * Converts between different units of rain for internal use
	 * @param RainValue	The decimal value of the rain amount
	 * @param From		The unit to convert from, use the Unit_Rain constants in WeatherUnits.php
	 * @param To		The unit to convert to, use the Unit_Rain constants in WeatherUnits.php
	 * @return			The converted value as a decimal float
	 */
	private function convertRainMeasure($RainValue, $From, $To) {
		
		// Don't perform conversions on null rain values
		if ($RainValue===null) {return null;}

		$lConvertedRain = $RainValue;
		
		switch ($From) {
		
		case UNIT_RAIN_IN:
			if ($To == UNIT_RAIN_MM) {$lConvertedRain = ($RainValue * 25.4);}
			if ($To == UNIT_RAIN_COUNTS) {

				// Need to convert the rain value to counts. Counts represent the number
				// of times the rain tipping bucket tipped. The tipping bucket comes in 
				// various sizes depending on the model of sensor used. This is set in 
				// $this->cRainSize using the default measurement unit for rain. 
				
				// convert $RainValue to default rain unit for this object
				$lConvertedRain = $this->convertRainMeasure($RainValue, UNIT_RAIN_IN, $this->cRainSizeDefUnit);
				
				// divide the rain value by bucket size to get number of counts or tips
				$lConvertedRain = $lConvertedRain / $this->cRainSize;
			}
			break;
		case UNIT_RAIN_MM:
			if ($To == UNIT_RAIN_IN) {$lConvertedRain = ($RainValue / 25.4);}
			if ($To == UNIT_RAIN_COUNTS) {
				
				// convert $RainValue to default rain unit for this object
				$lConvertedRain = $this->convertRainMeasure($RainValue, UNIT_RAIN_MM, $this->cRainSizeDefUnit);
				
				// divide the rain value by bucket size to get number of counts or tips
				$lConvertedRain = $lConvertedRain / $this->cRainSize;
			}
			break;
		case UNIT_RAIN_COUNTS:
			
			if ($To!=UNIT_RAIN_COUNTS) {

				// Multiply the tipping counts by $this->cRainSize to get the amount of rain
				$lConvertedRain = ($RainValue * $this->cRainSize);
				
				// Convert to the required unit
				$lConvertedRain = $this->convertRainMeasure($lConvertedRain, $this->cRainSizeDefUnit, $To);
				
			}
			break;
		}
		
		// Return the value
		return round($lConvertedRain, $this->cRainPrec);
		
	}
	
	/**
	 * Converts between different units of length for internal use
	 * @param ElevationValue	The decimal value of the length
	 * @param From				The unit to convert from, use the Unit_Elevation constants in WeatherUnits.php
	 * @param To				The unit to convert to, use the Unit_Elevation constants in WeatherUnits.php
	 * @return					The converted value as a decimal float
	 */
	private function convertElevation($ElevationValue, $From, $To) {
		
		// Don't perform conversions on null elevation values
		if ($ElevationValue===null) {return null;}

		$lConvertedElevation = $ElevationValue;
		
		switch ($From) {
		
		case UNIT_ELEVATION_FEET:
			if ($To == UNIT_ELEVATION_METERS) {$lConvertedElevation = ($ElevationValue / 3.281);}
			break;
		case UNIT_ELEVATION_METERS:
			if ($To == UNIT_ELEVATION_FEET) {$lConvertedElevation = ($ElevationValue * 3.281);}
			break;
		}
		
		return $lConvertedElevation;
		
	}
			
	// Unit Outputs
	
	/**
	 * Returns a string representing the unit
	 * @return The unit string
	 */
	private function tempUnit() {

		$lUnitOut = "";
		switch ($this->cOutputTempUnit) {
		case UNIT_TEMP_CELSIUS:
				$lUnitOut = "&#8451";
			break;
		case UNIT_TEMP_FAHRENHEIT: 
				$lUnitOut = "&#8457";
			break;
		}
		return $lUnitOut;
	}
	
	/**
	 * Returns a string representing the unit
	 * @return The unit string
	 */
	private function speedUnit() {

		$lUnitOut = "";
		switch ($this->cOutputWindSpUnit) {
		case UNIT_WINDSPEED_KNOTS:
				$lUnitOut = "knots";
			break;
		case UNIT_WINDSPEED_MPH: 
				$lUnitOut = "mph";
			break;
		case UNIT_WINDSPEED_KPH:
				$lUnitOut = "km/h";
			break;
		case UNIT_WINDSPEED_MPS:
				$lUnitOut = "m/s";
			break;
		}
		return $lUnitOut;
	}
	
	/**
	 * Returns the wind direction based on default unit for wind direction
	 * @param 	Direction	The wind direction in degrees
	 * @return	A string representing the wind direction 
	 */
	private function dirUnit($Direction) {
		
		$lUnitOut = "";
		
		switch ($this->cOutputWindDirUnit) {
		
		case UNIT_WINDDIRECTION_CARDINAL:
			$lUnitOut = getWindCardinal($Direction);
			break;
		case UNIT_WINDDIRECTION_DEGREES:
			$lUnitOut = $Direction + "&#8728";
			break;
		}
		
		return $lUnitOut;
		
	}
	
	/**
	 * Returns a string representing the unit
	 * @return The unit string
	 */
	private function barUnit() {

		$lUnitOut = "";
		switch ($this->cOutputBarUnit) {
		case UNIT_BAROMETER_HPA:
				$lUnitOut = "hPa";
			break;
		case UNIT_BAROMETER_INCHES: 
				$lUnitOut = "in";
			break;
		case UNIT_BAROMETER_MB:
				$lUnitOut = "mb";
			break;
		case UNIT_BAROMETER_MILIMETERS:
				$lUnitOut = "mm";
			break;
		}
		return $lUnitOut;
	}
	
	/**
	 * Returns a string representing the unit
	 * @return The unit string
	 */
	private function rainUnit() {

		$lUnitOut = "";
		switch ($this->cOutputRainUnit) {
		case UNIT_RAIN_COUNTS:
				$lUnitOut = "counts";
			break;
		case UNIT_RAIN_IN: 
				$lUnitOut = "in";
			break;
		case UNIT_RAIN_MM:
				$lUnitOut = "mm";
			break;
		}
		return $lUnitOut;
	}

	/**
	 * Returns a string representing the unit
	 * @return The unit string
	 */
	private function rainSizeUnit() {
		$lUnitOut = "";
		switch ($this->cRainSizeDefUnit) {
		case UNIT_RAIN_COUNTS:
				$lUnitOut = "counts";
			break;
		case UNIT_RAIN_IN: 
				$lUnitOut = "in";
			break;
		case UNIT_RAIN_MM:
				$lUnitOut = "mm";
			break;
		}
		return $lUnitOut;
	}

	/**
	 * Returns a string representing the time from the UNIX time in seconds
	 * @param 	Seconds	Unix timestamp in seconds
	 * @return	String 	containing the time and date
	 */
	private function getDateTimeFromUNIXtimeStamp($Seconds, $Dateformat = "") {
		
		// Create a date object
		$lDate = date_create();
		
		// Get the date from the unix timestamp
		date_timestamp_set($lDate, $Seconds);
		
		// Get the date format from this object if none has been supplied as an argument to this function
		$lDateFormat = ((strlen($Dateformat)>0) ? $Dateformat : $this->cDateFormat);

		// Get the string containing the date and time
		return ($Seconds > 0) ? date_format($lDate, $lDateFormat) : "";
		
	}
	
	
    // Private attributes ---------------------------------------------------------------------------------------------------
	
	// Sensor data
	
	protected $cLsid;                      		// logical sensor ID **(no unit)**
	protected $cDataStructureType;                 // data structure type **(no unit)**
	protected $cTransmitterID;                     // transmitter ID **(no unit)**
	protected $cTemp;                            // most recent valid temperature **(°F)**
	protected $cHum;                             // most recent valid humidity **(%RH)**
	protected $cDewPoint;                        // **(°F)**
	protected $cWetBulb;                         // **(°F)**
	protected $cHeatIndex;                       // **(°F)**
	protected $cWindChill;                       // **(°F)**
	protected $cThwIndex;                        // **(°F)**
	protected $cThswIndex;                       // **(°F)**
	protected $cWindPpeedLast;                   // most recent valid wind speed **(mph)**
	protected $cWindDirLast;                     // most recent valid wind direction **(°degree)**
	protected $cWindSpeedAvgLast_1_min;          // average wind speed over last 1 min **(mph)**
	protected $cWindDirScalarAvg_last_1_min;     // scalar average wind direction over last 1 min **(°degree)**
	protected $cWindSpeedAvgLast_2_min;          // average wind speed over last 2 min **(mph)**
	protected $cWindDirScalarAvg_last_2_min;     // scalar average wind direction over last 2 min **(°degree)**
	protected $cWindSpeedHi_last_2_min;          // maximum wind speed over last 2 min **(mph)**
	protected $cWindDirAtHiSpeedLast_2_min;      // gust wind direction over last 2 min **(°degree)**
	protected $cWindSpeedAvgLast_10_min;         // average wind speed over last 10 min **(mph)**
	protected $cWindDirScalarAvgLast_10_min;     // scalar average wind direction over last 10 min **(°degree)**
	protected $cWindSpeedHiLast_10_min;          // maximum wind speed over last 10 min **(mph)**
	protected $cWindDirAtHiSpeedLast_10_min;     // gust wind direction over last 10 min **(°degree)**
	protected $cRainSize;                        // rain collector type/size **(0: Reserved, 1: 0.01", 2: 0.2 mm, 3:  0.1 mm, 4: 0.001")**
	protected $cRainRateLast;                    // most recent valid rain rate **(counts/hour)**
	protected $cRainRateHi;                      // highest rain rate over last 1 min **(counts/hour)**
	protected $cRainfallLast_15_min;             // total rain count over last 15 min **(counts)**
	protected $cRainRateHiLast_15_min;           // highest rain rate over last 15 min **(counts/hour)**
	protected $cRainfallLast_60_min;             // total rain count for last 60 min **(counts)**
	protected $cRainfallLast_24_hr;              // total rain count for last 24 hours **(counts)**
	protected $cRainStorm;                       // total rain count since last 24 hour long break in rain **(counts)**
	protected $cRainStormStartAt;                 // UNIX timestamp of current rain storm start **(seconds)**
	protected $cSolarRad;                        // most recent solar radiation **(W/m²)**
	protected $cUvIndex;                         // most recent UV index **(Index)**
	protected $cRxState;                           // configured radio receiver state **(no unit)**
	protected $cTransBatteryFlag;                  // transmitter battery status flag **(no unit)**
	protected $cRrainfallDaily;                    // total rain count since local midnight **(counts)**
	protected $cRainfallMonthly;                   // total rain count since first of month at local midnight **(counts)**
	protected $cRainfallYear;                      // total rain count since first of user-chosen month at local midnight **(counts)**
	protected $cRainStormLast;                     // total rain count since last 24 hour long break in rain **(counts)**
	protected $cRainStormLastStartAt;             // UNIX timestamp of last rain storm start **(sec)**
	protected $cRainStormLastEndAt;               // UNIX timestamp of last rain storm end **(sec)**
	protected $cSoilTemp1;                       // most recent valid soil temp slot 1 **(°F)**
	protected $cTemp_3;                          // most recent valid soil temp slot 3 **(°F)**
	protected $cTemp_4;                          // most recent valid soil temp slot 4 **(°F)**
	protected $cTemp_2;                          // most recent valid soil temp slot 2 **(°F)**
	protected $cMoistSoil1;                      // most recent valid soil moisture slot 1 **(|cb|)**
	protected $cMpistSoil2;                      // most recent valid soil moisture slot 2 **(|cb|)**
	protected $cMoistSoil3;                      // most recent valid soil moisture slot 3 **(|cb|)**
	protected $cMoistSoil4;                      // most recent valid soil moisture slot 4 **(|cb|)**
	protected $cWetleaf1;                        // most recent valid leaf wetness slot 1 **(no unit)**
	protected $cWetleaf2;                        // most recent valid leaf wetness slot 2 **(no unit)**
	protected $cInTemp;                          // most recent valid inside temp **(°F)**
	protected $cInHum;                           // most recent valid inside humidity **(%RH)**
	protected $cInDewPoint;                      // **(°F)**
	protected $cInHeatIndex;                     // **(°F)**
	protected $cBarSeaLevel;                     // most recent bar sensor reading with elevation adjustment **(inches)**
	protected $cBarTrend;                        // current 3 hour bar trend **(inches)**
	protected $cBarAbsolute;                     // raw bar sensor reading **(inches)**
	
	// Performance data
	
	// The time it took to get data from station and save the observation 
	// record in the observation table in the database for the packet prior 
	// to the current
	protected $cPrevPacketLatency;

	// The date and time the packet was received from the station by the site 
	// server
	protected $cObservationReceiveTime;


	// Configuration data
	
	// These are units that the sensors are typically set to 
	
	protected $cInputTempUnit;
	protected $cInputWindSpUnit;
	protected $cInputWindDirUnit;
	protected $cInputBarUnit;
	protected $cInputRainUnit;
	protected $cRainSizeInputUnit;
	
	// These units are the desired output units
	
	protected $cOutputTempUnit;
	protected $cOutputWindSpUnit;
	protected $cOutputWindDirUnit;
	protected $cOutputBarUnit;
	protected $cOutputRainUnit;
	protected $cRainSizeOutputUnit;

	// The amount of decimal places for units
	protected $cTempPrec;
	protected $cWindSpPrec;
	protected $cBarPrec;
	protected $cRainPrec;
	protected $cHumPrec;
	
	// Internal unit used for the size of the rain collector/spoon/tipping bucket
	protected $cRainSizeDefUnit;

	// The date time format to use
	protected $cDateFormat;

}

?>