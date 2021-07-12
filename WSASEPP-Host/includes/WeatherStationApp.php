<?php 

// require_once 'WSASEPP_ErrorHandler.php';
require_once 'Common.php';
require_once 'WSASEPP_Const.php';
require_once 'WeatherUnits_Const.php';
require_once 'database/WsaDatabase.php';
require_once 'Observations.php';
require_once 'StationDataReceiver.php';

class WeatherStationApp extends Common {

    // Constructors

    function __construct() {
        
        parent::__construct();

        // create a new observations object
        $this->cLatestObservations = new Observations();

        // The weather station API handler
        $this->cStationDataReceiver = new StationDataReceiver();

        // Database Connection
        $this->cDatabaseConnection = new WsaDatabase();

        // Output Units
	
        $this->cSpeedUnit = UNIT_WINDSPEED_KPH;
        $this->cBarUnit = UNIT_BAROMETER_HPA;
        $this->cRainUnit = UNIT_RAIN_MM;
        $this->cWindDirUnit = UNIT_WINDDIRECTION_CARDINAL;
        $this->cElUnit = UNIT_ELEVATION_METERS;
        $this->cTempUnit = UNIT_TEMP_CELSIUS;
        
        // Input Units
        $this->cInputTempUnit = UNIT_TEMP_FAHRENHEIT;
        $this->cInputWindSpUnit = UNIT_WINDSPEED_MPH;
        $this->cInputWindDirUnit = UNIT_WINDDIRECTION_DEGREES;
        $this->cInputBarUnit = UNIT_BAROMETER_INCHES; 
        $this->cInputRainUnit = UNIT_RAIN_COUNTS;
        $this->cRainSizeDefUnit = UNIT_RAIN_MM;
       
        

    }

    // Getters and Setters
    
    /**
    * The output unit with wind speed to use
    * @return	The output unit for wind speed
    */
   public function getSpeedUnit() {
       return $this->cSpeedUnit;
   }

   /**	
    * Sets the output unit for wind speed
    * @param 	speenUnit	Wind speed unit		
    */
   public function setSpeedUnit($speedUnit) {
       $this->cSpeedUnit = $speedUnit;
       $this->cLatestObservations.setOutputWindSpUnit($speedUnit);
   }

   /**
    * The output unit for barometric pressure to use
    * @return 	The unit for barometric pressure
    */
   public function getBarUnit() {
       return $this->cBarUnit;
   }

   /**	
    * The output unit for barometric pressure to use
    * @param barUnit The unit for barometric pressure
    */
   public function setBarUnit($barUnit) {
       $this->cBarUnit = $barUnit;
       $this->cLatestObservations.setOutputBarUnit($barUnit);
   }

   /**
    * The output unit for rain to use
    * @return The unit for rain
    */
   public function getRainUnit() {
       return $this->cRainUnit;
   }

   /**
    * The output unit for rain to use
    * @param rainUnit The unit for rain
    */
   public function setRainUnit($rainUnit) {
       $this->cRainUnit = $rainUnit;
       $this->cLatestObservations.setOutputRainUnit($rainUnit);
   }

   /**
    * The output unit for wind direction to use
    * @return The wind direction unit
    */
   public function getWindDirUnit() {
       return $this->cWindDirUnit;
   }

   /**
    * The output unit for wind direction to use
    * @param windDirUnit The wind direction unit
    */
   public function setWindDirUnit($windDirUnit) {
       $this->cWindDirUnit = $windDirUnit;
       $this->cLatestObservations.setOutputWindDirUnit($windDirUnit);
   }

   /**
    * The output unit for elevation to use
    * @return The unit for elevation
    */
   public function getElUnit() {
       return $this->cElUnit;
   }

   /**
    * The output unit for elevation to use
    * @param elUnit The unit for elevation
    */
   public function setElUnit($elUnit) {
       $this->cElUnit = $elUnit;
       
   }

   /**
    * The output unit for temperature to use
    * @return The unit for temperature
    */
   public function getTempUnit() {
       return $this->cTempUnit;
   }

   /**
    * The output unit for temperature to use
    * @param tempUnit The unit for temperature
    */
   public function setTempUnit($tempUnit) {
       $this->cTempUnit = $tempUnit;
       $this->cLatestObservations.setOutputTempUnit($tempUnit);
   }

   /**
    * Gets the input unit for wind speed. This is the unit of measure expected
    * from the source sensor data.
    * @return 	The unit of measure for wind speed used by the source data
    */
   public function getInputSpeedUnit() {
       return $this->cInputSpeedUnit;
   }

   /**
    * Sets the input unit for wind speed. This is the unit of measure expected
    * from the source sensor data.
    * @param 	inputWindSpUnit		The unit of measure for wind speed used by the source data
    */
   public function setInputSpeedUnit($inputSpeedUnit) {
       $this->cInputSpeedUnit = $inputSpeedUnit;
       $this->cLatestObservations.setInputWindSpUnit($inputSpeedUnit);
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
       $this->cLatestObservations.setInputBarUnit($inputBarUnit);
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
       $this->cLatestObservations.setInputRainUnit($inputRainUnit);
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
       $this->cLatestObservations.setInputWindDirUnit($inputWindDirUnit);
   }

   /**
    * @return the inputElUnit
    */
   public function getInputElUnit() {
       return $this->cInputElUnit;
   }

   /**
    * @param inputElUnit the inputElUnit to set
    */
   public function setInputElUnit($inputElUnit) {
       $this->cInputElUnit = $inputElUnit;
   }

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
       $this->cLatestObservations.setInputTempUnit($inputTempUnit);
   }
   


    // Public Methods

    /**
     * Gets the observation data from the http request and returns an Observations object with the data
     * 
     * @return  Observations object with the observations data from the request. 
     */
    public function getObservationsFromRequest() {
        
        // Load the observations from the database
        if (!$this->cDatabaseConnection->load($this->cLatestObservations)) {
            $this->logError($this->cDatabaseConnection->lastError());
        }

        // Get the latest observations from sensor data
        $lLsid = $this->cStationDataReceiver->getLsid(); 
		$lDataStructureType=$this->cStationDataReceiver->getDataStructureType();
		$lTransmitterID=$this->cStationDataReceiver->getTransmitterID();
		$lTemp=$this->cStationDataReceiver->getTemp();
		$lHum = $this->cStationDataReceiver->getHum();
		$lDewPoint= $this->cStationDataReceiver->getDewPoint();
		$lWetBulb = $this->cStationDataReceiver->getWetBulb();
		$lHeatIndex = $this->cStationDataReceiver->getHeatIndex();
		$lWindChill = $this->cStationDataReceiver->getWindChill();
		$lThwIndex = $this->cStationDataReceiver->getThwIndex();
		$lThswIndex = $this->cStationDataReceiver->getThswIndex();
		$lWindSpeedLast = $this->cStationDataReceiver->getWindPpeedLast();
		$lWindDirLast = $this->cStationDataReceiver->getWindDirLast();
		$lWindSpeedAvgLast_1_min = $this->cStationDataReceiver->getWindSpeedAvgLast_1_min();
		$lWindDirScalarAvg_last_1_min = $this->cStationDataReceiver->getWindDirScalarAvg_last_1_min();
		$lWindSpeedAvgLast_2_min = $this->cStationDataReceiver->getWindSpeedAvgLast_2_min();
		$lWindDirScalarAvg_last_2_min = $this->cStationDataReceiver->getWindDirScalarAvg_last_2_min();
		$lWindSpeedHi_last_2_min = $this->cStationDataReceiver->getWindSpeedHi_last_2_min();
		$lWindDirAtHiSpeedLast_2_min = $this->cStationDataReceiver->getWindDirAtHiSpeedLast_2_min();
		$lWindSpeedAvgLast_10_min = $this->cStationDataReceiver->getWindSpeedAvgLast_10_min();
		$lWindDirScalarAvgLast_10_min = $this->cStationDataReceiver->getWindDirScalarAvgLast_10_min();
		$lWindSpeedHiLast_10_min = $this->cStationDataReceiver->getWindSpeedHiLast_10_min();
		$lWindDirAtHiSpeedLast_10_min = $this->cStationDataReceiver->getWindDirAtHiSpeedLast_10_min();
		$lRainSize = $this->cStationDataReceiver->getRainSize();
		$lRainRateLast = $this->cStationDataReceiver->getRainRateLast();
		$lRainRateHi = $this->cStationDataReceiver->getRainRateHi();
		$lRainfallLast_15_min = $this->cStationDataReceiver->getRainfallLast_15_min();
		$lRainRateHiLast_15_min = $this->cStationDataReceiver->getRainRateHiLast_15_min();
		$lRainfallLast_60_min = $this->cStationDataReceiver->getRainfallLast_60_min();
		$lRainfallLast_24_hr = $this->cStationDataReceiver->getRainfallLast_24_hr();
		$lRainStorm = $this->cStationDataReceiver->getRainStorm();
		$lRainStormStartAt = $this->cStationDataReceiver->getRainStormStartAt();
		$lSolarRad = $this->cStationDataReceiver->getSolarRad();
		$lUvIndex = $this->cStationDataReceiver->getUvIndex();
		$lRxState = $this->cStationDataReceiver->getRxState(1);
		$lTransBatteryFlag = $this->cStationDataReceiver->getTransBatteryFlag(1);
		$lRrainfallDaily = $this->cStationDataReceiver->getRrainfallDaily();
		$lRainfallMonthly = $this->cStationDataReceiver->getRainfallMonthly();
		$lRainfallYear = $this->cStationDataReceiver->getRainfallYear();
		$lRainStormLast = $this->cStationDataReceiver->getRainStormLast();
		$lRainStormLastStartAt = $this->cStationDataReceiver->getRainStormLastStartAtSeconds();
		$lRainStormLastEndAt = $this->cStationDataReceiver->getRainStormLastEndAtSeconds();
		$lSoilTemp1 = $this->cStationDataReceiver->getSoilTemp1();
		$lTemp_3 = $this->cStationDataReceiver->getTemp_2();
		$lTemp_4 = $this->cStationDataReceiver->getTemp_3();
		$lTemp_2 = $this->cStationDataReceiver->getTemp_4();
		$lMoistSoil1 = $this->cStationDataReceiver->getMoistSoil1();
		$lMpistSoil2 = $this->cStationDataReceiver->getMpistSoil2();
		$lMoistSoil3 = $this->cStationDataReceiver->getMoistSoil3();
		$lMoistSoil4 = $this->cStationDataReceiver->getMoistSoil4();
		$lWetleaf1 = $this->cStationDataReceiver->getWetleaf1();
		$lWetleaf2 = $this->cStationDataReceiver->getWetleaf2();
		$lInTemp = $this->cStationDataReceiver->getInTemp();
		$lInHum = $this->cStationDataReceiver->getInHum();
		$lInDewPoint = $this->cStationDataReceiver->getInDewPoint();
		$lInHeatIndex = $this->cStationDataReceiver->getInHeatIndex();
		$lBarSeaLevel = $this->cStationDataReceiver->getBarSeaLevel();
		$lBarTrend = $this->cStationDataReceiver->getBarTrend();
		$lBarAbsolute = $this->cStationDataReceiver->getBarAbsolute();
		$lObservationReceiveTime = $this->cStationDataReceiver->getTimeReceived();
        $lPrevPacketLatency = $this->cStationDataReceiver->getLatency();

		// Only update the observations if data is available, otherwise keep existing data
		if ($lLsid!==NULL) {$this->cLatestObservations->setLsid($lLsid);}                      
		if ($lDataStructureType!==NULL) {$this->cLatestObservations->setDataStructureType($lDataStructureType);}                 
		if ($lTransmitterID!==NULL) {$this->cLatestObservations->setTransmitterID($lTransmitterID);}                     
		if ($lTemp!==NULL) {$this->cLatestObservations->setTemp($lTemp);}                            
        if ($lHum!==NULL) {$this->cLatestObservations->setHum($lHum);}                             
		if ($lDewPoint!==NULL) {$this->cLatestObservations->setDewPoint($lDewPoint);}                        
		if ($lWetBulb!==NULL) {$this->cLatestObservations->setWetBulb($lWetBulb);}                         
		if ($lHeatIndex!==NULL) {$this->cLatestObservations->setHeatIndex($lHeatIndex);}                       
		if ($lWindChill!==NULL) {$this->cLatestObservations->setWindChill($lWindChill);}                       
		if ($lThwIndex!==NULL) {$this->cLatestObservations->setThwIndex($lThwIndex);}                        
		if ($lThswIndex!==NULL) {$this->cLatestObservations->setThswIndex($lThswIndex);}                       
		               
		if ($lWindSpeedAvgLast_1_min!==NULL) {$this->cLatestObservations->setWindSpeedAvgLast_1_min($lWindSpeedAvgLast_1_min);}          
		if ($lWindDirScalarAvg_last_1_min!==NULL) {$this->cLatestObservations->setWindDirScalarAvg_last_1_min($lWindDirScalarAvg_last_1_min);}     
		if ($lWindSpeedAvgLast_2_min!==NULL) {$this->cLatestObservations->setWindSpeedAvgLast_2_min($lWindSpeedAvgLast_2_min);}          
		if ($lWindDirScalarAvg_last_2_min!==NULL) {$this->cLatestObservations->setWindDirScalarAvg_last_2_min($lWindDirScalarAvg_last_2_min);}     
		if ($lWindSpeedHi_last_2_min!==NULL) {$this->cLatestObservations->setWindSpeedHi_last_2_min($lWindSpeedHi_last_2_min);}          
		if ($lWindDirAtHiSpeedLast_2_min!==NULL) {$this->cLatestObservations->setWindDirAtHiSpeedLast_2_min($lWindDirAtHiSpeedLast_2_min);}      
		if ($lWindSpeedAvgLast_10_min!==NULL) {$this->cLatestObservations->setWindSpeedAvgLast_10_min($lWindSpeedAvgLast_10_min);}         
		if ($lWindDirScalarAvgLast_10_min!==NULL) {$this->cLatestObservations->setWindDirScalarAvgLast_10_min($lWindDirScalarAvgLast_10_min);}     
		if ($lWindSpeedHiLast_10_min!==NULL) {$this->cLatestObservations->setWindSpeedHiLast_10_min($lWindSpeedHiLast_10_min);}          
		if ($lWindDirAtHiSpeedLast_10_min!==NULL) {$this->cLatestObservations->setWindDirAtHiSpeedLast_10_min($lWindDirAtHiSpeedLast_10_min);}     
		if ($lRainSize!==NULL) {$this->cLatestObservations->setRainSize($lRainSize);}                        
		if ($lRainRateHi!==NULL) {$this->cLatestObservations->setRainRateHi($lRainRateHi);}                      
		if ($lRainRateHiLast_15_min!==NULL) {$this->cLatestObservations->setRainRateHiLast_15_min($lRainRateHiLast_15_min);}           
        if ($lRainfallLast_15_min!==NULL) {$this->cLatestObservations->setRainfallLast_15_min($lRainfallLast_15_min);}
        if ($lRainfallLast_60_min!==NULL) {$this->cLatestObservations->setRainfallLast_60_min($lRainfallLast_60_min);}
		if ($lRainfallLast_24_hr!==NULL) {$this->cLatestObservations->setRainfallLast_24_hr($lRainfallLast_24_hr);}

        // This value can have NULL assigned to it because if it not currently raining, the rain storm 
        // would have stopped, and the Rain Storm Start At value would not make any sense.
        $this->cLatestObservations->setRainStormStartAt($lRainStormStartAt);     
        $this->cLatestObservations->setRainStorm($lRainStorm);
        $this->cLatestObservations->setWindPpeedLast($lWindSpeedLast);
		
        // If there is no wind speed, there is no wind direction. So don't set the wind direction
        if ($lWindSpeedLast>0) $this->cLatestObservations->setWindDirLast($lWindDirLast); 
        
        $this->cLatestObservations->setRainRateLast($lRainRateLast);

        $this->cLatestObservations->setObservationReceiveTime($lObservationReceiveTime);
        $this->cLatestObservations->setPrevPacketLatency($lPrevPacketLatency);

        if ($lSolarRad!==NULL) {$this->cLatestObservations->setSolarRad($lSolarRad);}                        
		if ($lUvIndex!==NULL) {$this->cLatestObservations->setUvIndex($lUvIndex);}                         
		if ($lRxState!==NULL) {$this->cLatestObservations->setRxState($lRxState);}                           
		if ($lTransBatteryFlag!==NULL) {$this->cLatestObservations->setTransBatteryFlag($lTransBatteryFlag);}                  
		if ($lRrainfallDaily!==NULL) {$this->cLatestObservations->setRrainfallDaily($lRrainfallDaily);}                  
		if ($lRainfallMonthly!==NULL) {$this->cLatestObservations->setRainfallMonthly($lRainfallMonthly);}                 
		if ($lRainfallYear!==NULL) {$this->cLatestObservations->setRainfallYear($lRainfallYear);}                    
		if ($lRainStormLast!==NULL) {$this->cLatestObservations->setRainStormLast($lRainStormLast);}                   
		if ($lRainStormLastStartAt!==NULL) {$this->cLatestObservations->setRainStormLastStartAt($lRainStormLastStartAt);}             
		if ($lRainStormLastEndAt!==NULL) {$this->cLatestObservations->setRainStormLastEndAt($lRainStormLastEndAt);}               
		if ($lSoilTemp1!==NULL) {$this->cLatestObservations->setSoilTemp1($lSoilTemp1);}                       
		if ($lTemp_3!==NULL) {$this->cLatestObservations->setTemp_2($lTemp_3);}                          
		if ($lTemp_4!==NULL) {$this->cLatestObservations->setTemp_3($lTemp_4);}                          
		if ($lTemp_2!==NULL) {$this->cLatestObservations->setTemp_4($lTemp_2);}                          
		if ($lMoistSoil1!==NULL) {$this->cLatestObservations->setMoistSoil1($lMoistSoil1);}                      
		if ($lMpistSoil2!==NULL) {$this->cLatestObservations->setMpistSoil2($lMpistSoil2);}                      
		if ($lMoistSoil3!==NULL) {$this->cLatestObservations->setMoistSoil3($lMoistSoil3);}                      
		if ($lMoistSoil4!==NULL) {$this->cLatestObservations->setMoistSoil4($lMoistSoil4);}                      
		if ($lWetleaf1!==NULL) {$this->cLatestObservations->setWetleaf1($lWetleaf1);}                        
		if ($lWetleaf2!==NULL) {$this->cLatestObservations->setWetleaf2($lWetleaf2);}                        
		if ($lInTemp!==NULL) {$this->cLatestObservations->setInTemp($lInTemp);}                          
		if ($lInHum!==NULL) {$this->cLatestObservations->setInHum($lInHum);}                           
		if ($lInDewPoint!==NULL) {$this->cLatestObservations->setInDewPoint($lInDewPoint);}                      
		if ($lInHeatIndex!==NULL) {$this->cLatestObservations->setInHeatIndex($lInHeatIndex);}                     
		if ($lBarSeaLevel!==NULL) {$this->cLatestObservations->setBarSeaLevel($lBarSeaLevel);}                     
		if ($lBarTrend!==NULL) {$this->cLatestObservations->setBarTrend($lBarTrend);}                        
		if ($lBarAbsolute!==NULL) {$this->cLatestObservations->setBarAbsolute($lBarAbsolute);}                     

        // Save the observations to the database
        if (!$this->cDatabaseConnection->save($this->cLatestObservations)) {
            $this->logError($this->cDatabaseConnection->lastError());
            return false;
        }
        
        return true;

    }

    /**
     * Gets a JSON object as a string containing the latest live observations reading.
     * @return  String  JSON String containing the live observations. If there was an error, the JSON
     *                  string will contain a key with the name "error" and the value will contain
     *                  a description of the error.
     */
    public function getObservationsAsJSON() {
        
        // Load the observations from the database
        if (!$this->cDatabaseConnection->load($this->cLatestObservations)) {
            $lJSONErrorObject['error'] = $this->cDatabaseConnection->lastError();
            return json_encode($lJSONErrorObject);
        }

        // Get the output units from the request if they exist
        $lOutTempUnit = (array_key_exists("temp", $_REQUEST)) ? $_REQUEST['temp']:"";
        $lOutSpeedUnit = (array_key_exists("speed", $_REQUEST)) ? $_REQUEST['speed']:"";
        $lOutDirUnit = (array_key_exists("dir", $_REQUEST)) ? $lOutDirUnit = $_REQUEST['dir']:"";
        $lOutRainUnit = (array_key_exists("rain", $_REQUEST)) ? $_REQUEST['rain']:"";
        $lOutBarUnit = (array_key_exists("bar", $_REQUEST)) ? $_REQUEST['bar']:"";
        
        // Set the output units for the Latest Observations just retrieved
        (strlen($lOutTempUnit)==0)?:$this->cLatestObservations->setOutputTempUnit($lOutTempUnit);
        (strlen($lOutSpeedUnit)==0)?:$this->cLatestObservations->setOutputWindSpUnit($lOutSpeedUnit);
        (strlen($lOutDirUnit)==0)?:$this->cLatestObservations->setOutputWindDirUnit($lOutDirUnit);
        (strlen($lOutRainUnit)==0)?:$this->cLatestObservations->setOutputRainUnit($lOutRainUnit);
        (strlen($lOutBarUnit)==0)?:$this->cLatestObservations->setOutputBarUnit($lOutBarUnit);
        
        // Get the JSON string from the latest observations
        return $this->cLatestObservations->getJSON();

    }

    // Private Methods

    

    // Private Attributes

    // The most current observations. Initially initialised with the latest obervations
    // from database, then updated from API/http request body
    private $cLatestObservations;

    // The weather station API handler
    private $cStationDataReceiver;

    // The database connection
    private $cDatabaseConnection;

    // Output Units
	
	private $cSpeedUnit;
	private $cBarUnit;
	private $cRainUnit;
	private $cWindDirUnit;
	private $cElUnit;
	private $cTempUnit;

    // Input Units

	private $cInputSpeedUnit;
	private $cInputBarUnit;
	private $cInputRainUnit;
	private $cInputWindDirUnit;
	private $cInputElUnit;
	private $cInputTempUnit;


}


?>