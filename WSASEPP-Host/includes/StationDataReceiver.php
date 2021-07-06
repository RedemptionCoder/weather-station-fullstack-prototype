<?php 

require_once 'Common.php';

class StationDataReceiver extends Common {

    // Constructors
    function __construct() {
        parent::__construct();
        $this->getData();
    }

    // Getters and Setters

    // Public Methods

    /**
	 * Gets the sensor ID
	 * @return The sensor ID
	 */
	public function getLsid() { 
		$lResult = $this->getJsonElement("lsid");
		return $lResult; 
	}

    /** 
	 * Gets the data structure type, which is specific to groups of sensors.
	 * @return The Data Structure Type
	 */
	public function getDataStructureType() {
		$lResult = $this->getJsonElement("data_structure_type");
		return $lResult; 
	}

    /**
	 * Gets the ID of a sensor suite 
	 * @return The Transmitter ID
	 */
	public function getTransmitterID() {
		$lResult = $this->getJsonElement("txid");
		return $lResult; 
	}
	/**
	 * Gets the outside temperature
	 * @return The outside temperature
	 */
	public function getTemp() { 
		$lResult = $this->getJsonElement("temp");
		return $lResult; // $lResult;
	}
	/**
	 * Gets the humidity as percentage
	 * @return The humidity
	 */
	public function getHum() {
		$lResult = $this->getJsonElement("hum");
		return $lResult;
	}
	/**
	 * Gets the Dew point 
	 * @return The Dew Point
	 */
	public function getDewPoint() {
		$lResult = $this->getJsonElement("dew_point");
		return $lResult;
	}
	/**
	 * Gets the wet bulb temperature 
	 * @return 	The Wet Bulb temperature
	 */
	public function getWetBulb() {
		$lResult = $this->getJsonElement("wet_bulb");
		return $lResult;
	}
	/**
	 * Gets the heat index 
	 * @return The Heat Index
	 */
	public function getHeatIndex() {
		$lResult = $this->getJsonElement("heat_index");
		return $lResult;
	}
	/** 
	 * Gets the wind chill temperature 
	 * @return The Wind Chill temperature
	 */
	public function getWindChill() {
		$lResult = $this->getJsonElement("wind_chill");
		return $lResult;
	}
	/**
	 * Gets the THW index 
	 * @return The THW Index
	 */
	public function getThwIndex() {
		$lResult = $this->getJsonElement("thw_index");
		return $lResult;
	}
	/**
	 * Gets the THSW index
	 * @return The THSW Index
	 */
	public function getThswIndex() {
		$lResult = $this->getJsonElement("thsw_index");
		return $lResult;
	}
	/**
	 * Gets the most recent wind speed 
	 * @return The most recent wind speed detected.
	 */
	public function getWindPpeedLast() {
		$lResult = $this->getJsonElement("wind_speed_last");
		return $lResult;
	}
	/** Gets the most recent/current Wind Direction in degrees from North 
	 * Going clockwise
	 * @return The most recent/current Wind Direction
	 */
	public function getWindDirLast() {
		$lResult = $this->getJsonElement("wind_dir_last");
		return $lResult;
	}
	/** 
	 * Gets the average wind speed over the last minute
	 * @return The average wind speed over the last minute
	 */
	public function getWindSpeedAvgLast_1_min() {
		$lResult = $this->getJsonElement("wind_speed_avg_last_1_min");
		return $lResult;
	}
	/**
	 * Returns the average wind direction over the last one minute in degrees
	 * @return The average wind direction over the last 1 minute
	 */
	public function getWindDirScalarAvg_last_1_min() {
		$lResult = $this->getJsonElement("wind_dir_scalar_avg_last_1_min");
		return $lResult;
	}
	/** 
	 * Gets the average wind speed over the last two minutes
	 * @return The average wind speed over the last 2 minutes
	 */
	public function getWindSpeedAvgLast_2_min() {
		$lResult = $this->getJsonElement("wind_speed_avg_last_2_min");
		return $lResult;
	}
	/**
	 * Returns the average wind direction over the last two minutes in degrees
	 * @return The wind direction over the last 2 minutes
	 */
	public function getWindDirScalarAvg_last_2_min() {
		$lResult = $this->getJsonElement("wind_dir_scalar_avg_last_2_min");
		return $lResult;
	}
	/**
	 * Gets the highest wind speed reached in the last 2 minutes 
	 * @return The highest wind speed reached in the last 2 minutes 
	 */
	public function getWindSpeedHi_last_2_min() {
		$lResult = $this->getJsonElement("wind_speed_hi_last_2_min");
		return $lResult;
	}
	/** 
	 * Gets the Wind Direction in degrees when blowing at highest speed in the last 2 minutes
	 * @return The Wind Direction at highest speed in last 2 minutes in degrees 
	 */
	public function getWindDirAtHiSpeedLast_2_min() {
		$lResult = $this->getJsonElement("wind_dir_at_hi_speed_last_2_min");
		return $lResult;
	}
	/**
	 * Gets the average wind speed during the last 10 minutes
	 * @return The average wind speed during the last 10 minutes 
	 * 	 
	 */
	public function getWindSpeedAvgLast_10_min() {
		$lResult = $this->getJsonElement("wind_speed_avg_last_10_min");
		return $lResult;
	}
	/**
	 * Returns the average scalar wind direction during the last 10 minutes in degrees
	 * @return The average scalar wind direction 
	 */
	public function getWindDirScalarAvgLast_10_min() {
		$lResult = $this->getJsonElement("wind_dir_scalar_avg_last_10_min");
		return $lResult;
	}
	/**
	 * Gets the maximum wind speed during the last 10 minutes 
	 * @return The maximum wind speed during the last 10 minutes
	 */
	public function getWindSpeedHiLast_10_min() {
		$lResult = $this->getJsonElement("wind_speed_hi_last_10_min");
		return $lResult;
	}
	/**
	 * Gets the wind direction of the maximum wind speed during the last 10 minutes in degrees
	 * @return The wind direction of the maximum wind speed during the last 10 minutes in degrees
	 */
	public function getWindDirAtHiSpeedLast_10_min() {
		$lResult = $this->getJsonElement("wind_dir_at_hi_speed_last_10_min");
		return $lResult;
	}
	/**
	 * Gets the size/volume of the rain tipping bucket/spoon installed in the sensor suite. 
	 * @return The tipping bucket/spoon volume
	 */
	public function getRainSize() {
		$lResult = $this->getJsonElement("rain_size");
		return $lResult;
	}
	/**
	 * Returns the current rain rate at units per hour 
	 * @return The current rain rate
	 */
	public function getRainRateLast() {
		$lResult = $this->getJsonElement("rain_rate_last");
		return $lResult;
	}
	/**
	 * Returns the highest rain rate over one minute
	 * @return The highest rain rate over one minute
	 */
	public function getRainRateHi() {
		$lResult = $this->getJsonElement("rain_rate_hi");
		return $lResult;
	}
	/**
	 * Returns the rainfall during the last 15 minutes 
	 * @return The rainfall during the last 15 minutes
	 */
	public function getRainfallLast_15_min() {
		$lResult = $this->getJsonElement("rainfall_last_15_min");
		return $lResult;
	}
	/**
	 * Gets the highest rain rate during the last 15 minutes
	 * @return The highest rain rate during the last 15 minutes
	 */
	public function getRainRateHiLast_15_min() {
		$lResult = $this->getJsonElement("rain_rate_hi_last_15_min");
		return $lResult;
	}
	/** 
	 * Gets the rainfall during the last 60 minutes
	 * @return The rainfall during the last 60 minutes
	 */
	public function getRainfallLast_60_min() {
		$lResult = $this->getJsonElement("rainfall_last_60_min");
		return $lResult;
	}
	/**
	 * Gets the rainfall during the last 24 hours
	 * @return The rainfall during the last 24 hours
	 */
	public function getRainfallLast_24_hr() {
		$lResult = $this->getJsonElement("rainfall_last_24_hr");
		return $lResult;
	}
	/**
	 * Gets the total amount of rain during the current rain storm event 
	 * @return The total amount of rain during the current rain storm event
	 */
	public function getRainStorm() {
		$lResult = $this->getJsonElement("rain_storm");
		return $lResult;
	}
	/**
	 * Gets the date and time the rain storm/event started in seconds since start
	 * of 1970
	 * @return Seconds since 1970 when rain storm started (UNIX timestamp)
	 */
	public function getRainStormStartAt() {
		$lResult = $this->getJsonElement("rain_storm_start_at");
		return $lResult;
	}
	/**
	 * Gets the solar radiation
	 * @return The solar radiation
	 */
	public function getSolarRad() {
		$lResult = $this->getJsonElement("solar_rad");
		return $lResult;
	}
	/**
	 * Gets the UV index
	 * @return The UV Index
	 */
	public function getUvIndex() {
		$lResult = $this->getJsonElement("uv_index");
		return $lResult;
	}
	/**
	 * Gets the configured radio receiver state
	 * @return The radio receiver state
	 */
	public function getRxState($transmitterID) {
		$lResult = $this->getJsonElement("rx_state", $transmitterID);
		return $lResult;
	}
	/**
	 * Gets the status of the battery
	 * @return The status of the battery flag
	 */
	public function getTransBatteryFlag($transmitterID) {
		$lResult = $this->getJsonElement("trans_battery_flag", $transmitterID);
		return $lResult;
	}
	/**
	 * Gets the daily rainfall 
	 * @return The daily rainfall
	 */ 
	public function getRrainfallDaily() {
		$lResult = $this->getJsonElement("rainfall_daily");
		return $lResult;
	}
	/**
	 * Gets the monthly rainfall 
	 * @return The monthly rainfall
	 */
	public function getRainfallMonthly() {
		$lResult = $this->getJsonElement("rainfall_monthly");
		return $lResult;
	}
	/**
	 * Gets the yearly rainfall 
	 * @return The Yearly Rainfall
	 */
	public function getRainfallYear() {
		$lResult = $this->getJsonElement("rainfall_year");
		return $lResult;
	}
	/**
	 * Gets the total amount of rain during the last rain storm event 
	 * @return Total amount of rain during the last rain storm event
	 */
	public function getRainStormLast() {
		$lResult = $this->getJsonElement("rain_storm_last");
		return $lResult;
	}
	/**
	 * Gets the UNIX time stamp in seconds indicating when the last rain
	 * storm started
	 * @return UNIX time stamp of when the last rain storm started
	 */
	public function getRainStormLastStartAtSeconds() {
		$lResult = $this->getJsonElement("rain_storm_last_start_at");
		return $lResult;
	}
	/**
	 * Gets the UNIX time stamp in seconds indicating when the last rain
	 * storm ended
	 * @return UNIX time stamp of when the last rain storm ended
	 */
	public function getRainStormLastEndAtSeconds() {
		$lResult = $this->getJsonElement("rain_storm_last_end_at");
		return $lResult;
	}
	/** 
	 * Gets the soil temperature for sensor slot 1
	 * @return The soil temperature
	 */
	public function getSoilTemp1() {
		$lResult = $this->getJsonElement("temp_1");
		return $lResult;
	}
	/** 
	 * Gets the soil temperature for sensor slot 2
	 * @return The soil temperature
	 */
	public function getTemp_2() {
		$lResult = $this->getJsonElement("temp_2");
		return $lResult;
	}
	/** 
	 * Gets the soil temperature for sensor slot 3
	 * @return The soil temperature
	 */
	public function getTemp_3() {
		$lResult = $this->getJsonElement("temp_3");
		return $lResult;
	}
	/** 
	 * Gets the soil temperature for sensor slot 4
	 * @return The soil temperature
	 */
	public function getTemp_4() {
		$lResult = $this->getJsonElement("temp_4");
		return $lResult;
	}
	/**
	 * Gets the soil moisture for slot 1
	 * @return The Soil Moisture
	 */
	public function getMoistSoil1() {
		$lResult = $this->getJsonElement("moist_soil_1");
		return $lResult;
	}
	/**
	 * Gets the soil moisture for slot 2
	 * @return The Soil Moisture
	 */
	public function getMpistSoil2() {
		$lResult = $this->getJsonElement("moist_soil_2");
		return $lResult;
	}
	/**
	 * Gets the soil moisture for slot 3
	 * @return The Soil Moisture
	 */
	public function getMoistSoil3() {
		$lResult = $this->getJsonElement("moist_soil_3");
		return $lResult;
	}
	/**
	 * Gets the soil moisture for slot 4
	 * @return The Soil Moisture
	 */
	public function getMoistSoil4() {
		$lResult = $this->getJsonElement("moist_soil_4");
		return $lResult;
	}
	/**
	 * Gets the leaf wetness for slot 1
	 * @return The leaf wetness
	 */
	public function getWetleaf1() {
		$lResult = $this->getJsonElement("wet_leaf_1");
		return $lResult;
	}
	/**
	 * Gets the leaf wetness for slot 2
	 * @return The leaf wetness
	 */
	public function getWetleaf2() {
		$lResult = $this->getJsonElement("wet_leaf_2");
		return $lResult;
	}
	/**
	 * Gets the indoor temperature
	 * @return The indoor temperature
	 */
	public function getInTemp() { 
		$lResult = $this->getJsonElement("temp_in");
		return $lResult;
	}
	/**
	 * Gets the indoor humidity
	 * @return The indoor humidity
	 */
	public function getInHum() {
		$lResult = $this->getJsonElement("hum_in");
		return $lResult;
	}
	/**
	 * Gets the indoor dew point
	 * @return The indoor dew point
	 */
	public function getInDewPoint() {
		$lResult = $this->getJsonElement("dew_point_in");
		return $lResult;
	}
	/**
	 * Gets the indoor heat index
	 * @return The indoor heat index
	 */
	public function getInHeatIndex() {
		$lResult = $this->getJsonElement("heat_index_in");
		return $lResult;
	}
	/**	 
	 * Gets the barometer reading with elevation adjusted
	 * @return The barometer reading
	 */
	public function getBarSeaLevel() {
		$lResult = $this->getJsonElement("bar_sea_level");
		return $lResult;
	}
	/**
	 * Gets the current 3 hour barometer trend
	 * @return The barometer trend
	 */
	public function getBarTrend() {
		$lResult = $this->getJsonElement("bar_trend");
		return $lResult;
	}
	/**
	 * Gets the raw barometer reading
	 * @return the raw barometer reading
	 */
	public function getBarAbsolute() {
		$lResult = $this->getJsonElement("bar_absolute");
		return $lResult;
	}

	/**
	 * Gets the latency in milliseconds the previous observation packet took from receiving the data from 
	 * the weatherstation to saving the data in the app server database. Note this is for the previous 
	 * observation data packet, not the current one.
	 * @return	Integer		Time it took to get the data from station and save to databse in milliseconds
	 */
	public function getLatency() {
		return $this->cPrevPacketLatency;
	}

	/**
	 * Gets the date and time the current observation data packet was received from the weather station
	 * by the site server. 
	 * @return	String	Date and time formatted according to YYYY-MM-DD hh:mm:ss
	 */
	public function getTimeReceived() {
		return $this->cObservationReceiveTime;
	}

    // Private Methods

    /**
     * Gets the JSON data structure from the request body to be decoded properly.
     * 
     * @return  true If data was successfully retrieved, or false if there were issues
     */
    private function getData() {
        
        // Get the JSON data from the request body
        $lJson = file_get_contents('php://input');

        // If there was no data, return false and log error
        if (!$lJson) {
            $this->logError("No weather station JSON data found in request body");
            return false;
        }
        
        // Start to decode the json object
        try {
            $lJsonObject = json_decode($lJson);
            if ($lJsonObject==null) {
                // The data in the body of the request contains nothing
                throw new Exception("Data object is null.");
            }

            // Get the root data object
            try {

                $lData = $lJsonObject->data;

                // The root data object wasn't found but sometimes they can still have the 
                // expected conditions object
                if ($lData==null) {
                    throw new Exception("Data object is null. Conditions object may be present");
                } else {

                    // Assign the expected conditions object to the conditions attribute
                    $this->cConditions = $lData->conditions;

					try {
						// Get the LastPacketReponseTime
						$this->cPrevPacketLatency = $lJsonObject->prev_packet_latency;
					} catch (Exception $e) {
						// For some reason this data is not included in the Json packet
						$this->logError("Latency data not available");
					}

					try {
						// Get the LastPacketReponseTime
						$this->cObservationReceiveTime = $lJsonObject->last_packet_rec_time;
					} catch (Exception $e) {
						// For some reason this data is not included in the Json packet
						$this->logError("Receive time not recorded");
					}

                }

            } catch (Exception $e) {
                
                // Since the conditions object may be present without the data
                // object, try and get the conditions obejct
                try {                
                    
                    // Get the conditions object
                    $this->cConditions = $lJsonObject->conditions;
                    
                    // If the conditions object is not present
                    if ($this->cConditions==null) {
                        throw new Exceptoin("No conditions object present. Data definition out of date");
                    }
                    
                } catch (Exception $e) {
                    
                    // The data retrieval has failed. So log the error and return false
                    $this->logError($e->getMessage());
                    return false;
                
                }
            
            }

        } catch (Exception $e) {
            
            // There was no data in the request body, so it's impossible to proceed
            // Log the error and return false
            
            $this->logError("Could not read data in request body. " + $e->getMessage());
            return false;
        }    
    

        return true;

    }

    /**
	 * Returns the JsonElement given the specified name
	 * @param 	elementName		The name of the Json Element
     * @param   transmitterID   The id of the transmitter - optional only for when data
     *                          associated with a specific transmitter is required
	 * @return	null If no element was found, or the element if found
	 */
	private function getJsonElement($elementName, $transmitterID=-1) {
		
		$lResult = null;

        if ($transmitterID>-1) {
            for ($i = 0; $i < count($this->cConditions); $i++) {
                if (property_exists($this->cConditions[$i], 'txid')) {
					if ($transmitterID==$this->cConditions[$i]->txid) {
						if (property_exists($this->cConditions[$i], $elementName)) {
						
							$lResult = $this->cConditions[$i]->$elementName;
						
							if ($lResult !== null) {
								return $lResult;
							}
						}
					}
				}
            }
        } else {
            for ($i = 0; $i < count($this->cConditions); $i++) {
                
				if (property_exists($this->cConditions[$i], $elementName)) {
                	$lResult = $this->cConditions[$i]->$elementName;
				
					if ($lResult !== null) {
						return $lResult;
					}
				}
            }
        }

		return $lResult;
	}
	
    // Attributes

    // The weather conditions object holding all the readings
    private $cConditions;

	// The time it took to get data from station and save the observation 
	// record in the observation table in the database for the packet prior 
	// to the current
	private $cPrevPacketLatency;

	// The date and time the packet was received from the station by the site 
	// server
	private $cObservationReceiveTime;

}


?>