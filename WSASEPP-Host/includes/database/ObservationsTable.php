<?php
/**
 * This class extends the observations class with its database implementation. An observations class 
 * is passed into the constructor allong with a msqli connection object and the expected table name 
 * in the database. 
 * 
 * Calls to createTable(), updateRecord(), and newRecord() will create a table, update a record, and 
 * create a new record based on what is in the observations object. 
 * 
 * Since the observations object only contains live observations, the observations table will always
 * only have one record. So the database implementation is simple. This class also makes use of 
 * prepared statements to prevent SQL injection attacks. 
 * 
 * @author  Anthony Gibbons
 */


require_once 'includes/Observations.php';
require_once 'includes/Common.php';
require_once 'includes/WSASEPP_Const.php';
require_once 'includes/WeatherUnits_Const.php';


class ObservationsTable extends Observations {

    // Copy constructor

    function __construct ($observations, $dbConnection, $tableName = "") {

        parent::__construct();

        $this->setObservations($observations);

        $this->cObservationsTableName = $tableName;

        $this->cConnection = $dbConnection;

    }

    // Getters and Setters

    /**
     * Sets the name of the observations table in the database
     * @param   observationsTableName   What to call the observations table in the database
     * @return  Nothing
     */
    public function setObservationsTableName($observationsTableName) {
        $this->cObservationsTableName = $observationsTableName;
    }
    /**
     * Gets the name of the observations table in the database
     * @return  observationsTableName   The name of the observations table
     */
    public function getObservationsTableName() {
        return $this->cObservationsTableName;
    }

    /**
     * Sets the mysqli database connection for prepared statement and binding
     * operations
     * @param   dbConnection    The mysqli object
     * @return  Nothing
     */
    public function setDatabaseConnectionObject($dbConnection) {
        $this->cConnection = $dbConnection;
    }

    /**
     * Gets the mysqli object used by this object
     * @return  mysqli  Object containing the database connection object
     */
    public function getDatabaseConnectionObject() {
        return $this->cConnection;
    }

    /**
     * Copies all attributes of an observations object into the attributes of this
     * object.
     * @param   observations    An observations object to copy from
     */
    public function setObservations($observations) {
        $this->cLsid = $observations->cLsid;
        $this->cDataStructureType = $observations->cDataStructureType;
        $this->cTransmitterID = $observations->cTransmitterID;
        $this->cTemp  = $observations->cTemp;
        $this->cHum  = $observations->cHum;
        $this->cDewPoint  = $observations->cDewPoint;
        $this->cWetBulb  = $observations->cWetBulb;
        $this->cHeatIndex  = $observations->cHeatIndex;
        $this->cWindChill  = $observations->cWindChill;
        $this->cThwIndex  = $observations->cThwIndex;
        $this->cThswIndex  = $observations->cThswIndex;
        $this->cWindPpeedLast  = $observations->cWindPpeedLast;
        $this->cWindDirLast  = $observations->cWindDirLast;
        $this->cWindSpeedAvgLast_1_min  = $observations->cWindSpeedAvgLast_1_min;
        $this->cWindDirScalarAvg_last_1_min = $observations->cWindDirScalarAvg_last_1_min;
        $this->cWindSpeedAvgLast_2_min  = $observations->cWindSpeedAvgLast_2_min;
        $this->cWindDirScalarAvg_last_2_min = $observations->cWindDirScalarAvg_last_2_min;
        $this->cWindSpeedHi_last_2_min  = $observations->cWindSpeedHi_last_2_min;
        $this->cWindDirAtHiSpeedLast_2_min =  $observations->cWindDirAtHiSpeedLast_2_min;
        $this->cWindSpeedAvgLast_10_min  = $observations->cWindSpeedAvgLast_10_min;
        $this->cWindDirScalarAvgLast_10_min = $observations->cWindDirScalarAvgLast_10_min;
        $this->cWindSpeedHiLast_10_min  = $observations->cWindSpeedHiLast_10_min;
        $this->cWindDirAtHiSpeedLast_10_min = $observations->cWindDirAtHiSpeedLast_10_min;
        $this->cRainSize  = $observations->cRainSize;
        $this->cRainRateLast  = $observations->cRainRateLast;
        $this->cRainRateHi  = $observations->cRainRateHi;
        $this->cRainfallLast_15_min  = $observations->cRainfallLast_15_min;
        $this->cRainRateHiLast_15_min  = $observations->cRainRateHiLast_15_min;
        $this->cRainfallLast_60_min  = $observations->cRainfallLast_60_min;
        $this->cRainfallLast_24_hr  = $observations->cRainfallLast_24_hr;
        $this->cRainStorm  = $observations->cRainStorm;
        $this->cRainStormStartAt  = $observations->cRainStormStartAt;
        $this->cSolarRad  = $observations->cSolarRad;
        $this->cUvIndex  = $observations->cUvIndex;
        $this->cRxState  = $observations->cRxState;
        $this->cTransBatteryFlag  = $observations->cTransBatteryFlag;
        $this->cRrainfallDaily  = $observations->cRrainfallDaily;
        $this->cRainfallMonthly  = $observations->cRainfallMonthly;
        $this->cRainfallYear  = $observations->cRainfallYear;
        $this->cRainStormLast  = $observations->cRainStormLast;
        $this->cRainStormLastStartAt  = $observations->cRainStormLastStartAt;
        $this->cRainStormLastEndAt  = $observations->cRainStormLastEndAt;
        $this->cSoilTemp1  = $observations->cSoilTemp1;
        $this->cTemp_3  = $observations->cTemp_3;
        $this->cTemp_4  = $observations->cTemp_4;
        $this->cTemp_2  = $observations->cTemp_2;
        $this->cMoistSoil1  = $observations->cMoistSoil1;
        $this->cMpistSoil2  = $observations->cMpistSoil2;
        $this->cMoistSoil3  = $observations->cMoistSoil3;
        $this->cMoistSoil4  = $observations->cMoistSoil4;
        $this->cWetleaf1  = $observations->cWetleaf1;
        $this->cWetleaf2  = $observations->cWetleaf2;
        $this->cInTemp  = $observations->cInTemp;
        $this->cInHum  = $observations->cInHum;
        $this->cInDewPoint  = $observations->cInDewPoint;
        $this->cInHeatIndex  = $observations->cInHeatIndex;
        $this->cBarSeaLevel  = $observations->cBarSeaLevel;
        $this->cBarTrend  = $observations->cBarTrend;
        $this->cBarAbsolute = $observations->cBarAbsolute;
        $this->cPrevPacketLatency = $observations->cPrevPacketLatency;
        $this->cObservationReceiveTime = $observations->cObservationReceiveTime;


        $this->cInputTempUnit = $observations->cInputTempUnit;
	    $this->cInputWindSpUnit = $observations->cInputWindSpUnit;
	    $this->cInputWindDirUnit = $observations->cInputWindDirUnit;
	    $this->cInputBarUnit = $observations->cInputBarUnit;
	    $this->cInputRainUnit = $observations->cInputRainUnit;
	    $this->cRainSizeInputUnit = $observations->cRainSizeInputUnit;
        
	    $this->cOutputTempUnit = $observations->cOutputTempUnit;
	    $this->cOutputWindSpUnit = $observations->cOutputWindSpUnit;
	    $this->cOutputWindDirUnit = $observations->cOutputWindDirUnit;
	    $this->cOutputBarUnit = $observations->cOutputBarUnit;
	    $this->cOutputRainUnit = $observations->cOutputRainUnit;
	    $this->cRainSizeOutputUnit = $observations->cRainSizeOutputUnit;

	    $this->cRainSizeDefUnit = $observations->cRainSizeDefUnit;
        
        $this->cDateFormat = $observations->cDateFormat;
    }

    /**
     * Gets an observations object containing the observations and settings from 
     * this object.
     * @return  Observations    An observations object. 
     */
    public function getObservations() {
        
        $lObservations = new Observations();

        $lObservations->cLsid = $this->cLsid;
        $lObservations->cDataStructureType = $this->cDataStructureType;
        $lObservations->cTransmitterID = $this->cTransmitterID;
        $lObservations->cTemp = $this->cTemp;
        $lObservations->cHum = $this->cHum;
        $lObservations->cDewPoint = $this->cDewPoint;
        $lObservations->cWetBulb = $this->cWetBulb;
        $lObservations->cHeatIndex = $this->cHeatIndex;
        $lObservations->cWindChill = $this->cWindChill;
        $lObservations->cThwIndex = $this->cThwIndex;
        $lObservations->cThswIndex = $this->cThswIndex;
        $lObservations->cWindPpeedLast = $this->cWindPpeedLast;
        $lObservations->cWindDirLast = $this->cWindDirLast;
        $lObservations->cWindSpeedAvgLast_1_min = $this->cWindSpeedAvgLast_1_min;
        $lObservations->cWindDirScalarAvg_last_1_min = $this->cWindDirScalarAvg_last_1_min;
        $lObservations->cWindSpeedAvgLast_2_min = $this->cWindSpeedAvgLast_2_min;
        $lObservations->cWindDirScalarAvg_last_2_min = $this->cWindDirScalarAvg_last_2_min;
        $lObservations->cWindSpeedHi_last_2_min = $this->cWindSpeedHi_last_2_min;
        $lObservations->cWindDirAtHiSpeedLast_2_min = $this->cWindDirAtHiSpeedLast_2_min;
        $lObservations->cWindSpeedAvgLast_10_min = $this->cWindSpeedAvgLast_10_min;
        $lObservations->cWindDirScalarAvgLast_10_min = $this->cWindDirScalarAvgLast_10_min;
        $lObservations->cWindSpeedHiLast_10_min = $this->cWindSpeedHiLast_10_min;
        $lObservations->cWindDirAtHiSpeedLast_10_min = $this->cWindDirAtHiSpeedLast_10_min;
        $lObservations->cRainSize = $this->cRainSize;
        $lObservations->cRainRateLast = $this->cRainRateLast;
        $lObservations->cRainRateHi = $this->cRainRateHi;
        $lObservations->cRainfallLast_15_min = $this->cRainfallLast_15_min;
        $lObservations->cRainRateHiLast_15_min = $this->cRainRateHiLast_15_min;
        $lObservations->cRainfallLast_60_min = $this->cRainfallLast_60_min;
        $lObservations->cRainfallLast_24_hr = $this->cRainfallLast_24_hr;
        $lObservations->cRainStorm = $this->cRainStorm;
        $lObservations->cRainStormStartAt = $this->cRainStormStartAt;
        $lObservations->cSolarRad = $this->cSolarRad;
        $lObservations->cUvIndex = $this->cUvIndex;
        $lObservations->cRxState = $this->cRxState;
        $lObservations->cTransBatteryFlag = $this->cTransBatteryFlag;
        $lObservations->cRrainfallDaily = $this->cRrainfallDaily;
        $lObservations->cRainfallMonthly = $this->cRainfallMonthly;
        $lObservations->cRainfallYear = $this->cRainfallYear;
        $lObservations->cRainStormLast = $this->cRainStormLast;
        $lObservations->cRainStormLastStartAt = $this->cRainStormLastStartAt;
        $lObservations->cRainStormLastEndAt = $this->cRainStormLastEndAt;
        $lObservations->cSoilTemp1 = $this->cSoilTemp1;
        $lObservations->cTemp_3 = $this->cTemp_3;
        $lObservations->cTemp_4 = $this->cTemp_4;
        $lObservations->cTemp_2 = $this->cTemp_2;
        $lObservations->cMoistSoil1 = $this->cMoistSoil1;
        $lObservations->cMpistSoil2 = $this->cMpistSoil2;
        $lObservations->cMoistSoil3 = $this->cMoistSoil3;
        $lObservations->cMoistSoil4 = $this->cMoistSoil4;
        $lObservations->cWetleaf1 = $this->cWetleaf1;
        $lObservations->cWetleaf2 = $this->cWetleaf2;
        $lObservations->cInTemp = $this->cInTemp;
        $lObservations->cInHum = $this->cInHum;
        $lObservations->cInDewPoint = $this->cInDewPoint;
        $lObservations->cInHeatIndex = $this->cInHeatIndex;
        $lObservations->cBarSeaLevel = $this->cBarSeaLevel;
        $lObservations->cBarTrend = $this->cBarTrend;
        $lObservations->cBarAbsolute = $this->cBarAbsolute;
        $lObservations->cPrevPacketLatency = $this->cPrevPacketLatency;
        $lObservations->cObservationReceiveTime = $this->cObservationReceiveTime;


        $lObservations->cInputTempUnit = $this->cInputTempUnit;
        $lObservations->cInputWindSpUnit = $this->cInputWindSpUnit;
        $lObservations->cInputWindDirUnit = $this->cInputWindDirUnit;
        $lObservations->cInputBarUnit = $this->cInputBarUnit;
        $lObservations->cInputRainUnit = $this->cInputRainUnit;
        $lObservations->cRainSizeInputUnit = $this->cRainSizeInputUnit;

        $lObservations->cOutputTempUnit = $this->cOutputTempUnit;
        $lObservations->cOutputWindSpUnit = $this->cOutputWindSpUnit;
        $lObservations->cOutputWindDirUnit = $this->cOutputWindDirUnit;
        $lObservations->cOutputBarUnit = $this->cOutputBarUnit;
        $lObservations->cOutputRainUnit = $this->cOutputRainUnit;
        $lObservations->cRainSizeOutputUnit = $this->cRainSizeOutputUnit;

        $lObservations->cRainSizeDefUnit = $this->cRainSizeDefUnit;

        $lObservations->cDateFormat = $this->cDateFormat;

        return $lObservations;

    }


    // public methods

    /**
     * Returns the SQL query needed to create a table in the database
     * suitable for the Observations class.
     * @param   tableName       The name of the table in the database
     * @param   dbConnection    A connected mysqli object to a mysql database server
     * @return  bool            true If table creation was successfull, false if not
     */
    public static function createTable($tableName, $dbConnection) {

        $lResult = false;

        if ($dbConnection!=NULL) {
            try {

                // The sql statement to create the table

                $lSql = "CREATE TABLE " . $tableName . " (
                    Lsid INT,
                    DataStructureType INT,
                    TransmitterID INT PRIMARY KEY,
                    Temp FLOAT,
                    Hum FLOAT,
                    DewPoint FLOAT,
                    WetBulb FLOAT,
                    HeatIndex FLOAT,
                    WindChill FLOAT,
                    ThwIndex FLOAT,
                    ThswIndex FLOAT,
                    WindPpeedLast FLOAT,
                    WindDirLast FLOAT,
                    WindSpeedAvgLast_1_min FLOAT,
                    WindDirScalarAvg_last_1_min FLOAT,
                    WindSpeedAvgLast_2_min FLOAT,
                    WindDirScalarAvg_last_2_min FLOAT,
                    WindSpeedHi_last_2_min FLOAT,
                    WindDirAtHiSpeedLast_2_min FLOAT,
                    WindSpeedAvgLast_10_min FLOAT,
                    WindDirScalarAvgLast_10_min FLOAT,
                    WindSpeedHiLast_10_min FLOAT,
                    WindDirAtHiSpeedLast_10_min FLOAT,
                    RainSize FLOAT,
                    RainRateLast FLOAT,
                    RainRateHi FLOAT,
                    RainfallLast_15_min FLOAT,
                    RainRateHiLast_15_min FLOAT,
                    RainfallLast_60_min FLOAT,
                    RainfallLast_24_hr FLOAT,
                    RainStorm FLOAT,
                    RainStormStartAt DATETIME,
                    SolarRad FLOAT,
                    UvIndex FLOAT,
                    RxState INT,
                    TransBatteryFlag FLOAT,
                    RrainfallDaily FLOAT,
                    RainfallMonthly FLOAT,
                    RainfallYear FLOAT,
                    RainStormLast FLOAT,
                    RainStormLastStartAt DATETIME,
                    RainStormLastEndAt DATETIME,
                    SoilTemp1 FLOAT,
                    Temp_3 FLOAT,
                    Temp_4 FLOAT,
                    Temp_2 FLOAT,
                    MoistSoil1 FLOAT,
                    MpistSoil2 FLOAT,
                    MoistSoil3 FLOAT,
                    MoistSoil4 FLOAT,
                    Wetleaf1 FLOAT,
                    Wetleaf2 FLOAT,
                    InTemp FLOAT,
                    InHum FLOAT,
                    InDewPoint FLOAT,
                    InHeatIndex FLOAT,
                    BarSeaLevel FLOAT,
                    BarTrend FLOAT,
                    BarAbsolute FLOAT,
                    PrevPacketLatency INT,
                    ObservationReceiveTime DATETIME)";

                // Run the query
                if ($dbConnection->query($lSql) === TRUE) {
                    $lResult = true;
                } else {
                    // Query failed, could be syntax or something else
                    throw new Exception("Query failed. " . $dbConnection->error);
                }   

            } catch (Exception $e) {$lResult = false;}
        } 

        return $lResult;

    }

    /**
     * Updates the current live observations record. If no record exists or if anything
     * else went wrong, the function will return false. Refer to object error logs for details
     * @return  bool  true If record update was successfull, false if not.
     */
    public function updateRecord() {

        // Be negative minded and assume one is related to Murphy who developed Murphy's 
        // law in what can go wrong will go wrong. Lets assume this will fail. 
        $lResult = false;

        // Copying class attributes to local variables might seem clumsy, but there is a reason for this.        
        // The bind_param method expects variables to be passed to it, and not references to variables. 
        // Unfortunately, when attempting to pass the attributes of this class, it is passed as references.
        // the bind_param method does not support passing references to references        
        $lLsid = $this->cLsid;
        $lDataStructureType = $this->cDataStructureType;
        $lTransmitterID = $this->cTransmitterID;
        $lTemp = $this->cTemp;
        $lHum = $this->cHum;
        $lDewPoint = $this->cDewPoint;
        $lWetBulb = $this->cWetBulb;
        $lHeatIndex = $this->cHeatIndex;
        $lWindChill = $this->cWindChill;
        $lThwIndex = $this->cThwIndex;
        $lThswIndex = $this->cThswIndex;
        $lWindPpeedLast = $this->cWindPpeedLast;
        $lWindDirLast = $this->cWindDirLast;
        $lWindSpeedAvgLast_1_min = $this->cWindSpeedAvgLast_1_min;
        $lWindDirScalarAvg_last_1_min = $this->cWindDirScalarAvg_last_1_min;
        $lWindSpeedAvgLast_2_min = $this->cWindSpeedAvgLast_2_min;
        $lWindDirScalarAvg_last_2_min = $this->cWindDirScalarAvg_last_2_min;
        $lWindSpeedHi_last_2_min = $this->cWindSpeedHi_last_2_min;
        $lWindDirAtHiSpeedLast_2_min = $this->cWindDirAtHiSpeedLast_2_min;
        $lWindSpeedAvgLast_10_min = $this->cWindSpeedAvgLast_10_min;
        $lWindDirScalarAvgLast_10_min = $this->cWindDirScalarAvgLast_10_min;
        $lWindSpeedHiLast_10_min = $this->cWindSpeedHiLast_10_min;
        $lWindDirAtHiSpeedLast_10_min = $this->cWindDirAtHiSpeedLast_10_min;
        $lRainSize = $this->cRainSize;
        $lRainRateLast = $this->cRainRateLast;
        $lRainRateHi = $this->cRainRateHi;
        $lRainfallLast_15_min = $this->cRainfallLast_15_min;
        $lRainRateHiLast_15_min = $this->cRainRateHiLast_15_min;
        $lRainfallLast_60_min = $this->cRainfallLast_60_min;
        $lRainfallLast_24_hr = $this->cRainfallLast_24_hr;
        $lRainStorm = $this->cRainStorm;
        $lRainStormStartAt = (($this->getRainStormStartAt())?:MYSQL_DATETIME_FORMAT_NULL_VALUE);
        $lSolarRad = $this->cSolarRad;
        $lUvIndex = $this->cUvIndex;
        $lRxState = $this->cRxState;
        $lTransBatteryFlag = $this->cTransBatteryFlag;
        $lRrainfallDaily = $this->cRrainfallDaily;
        $lRainfallMonthly = $this->cRainfallMonthly;
        $lRainfallYear = $this->cRainfallYear;
        $lRainStormLast = $this->cRainStormLast;
        $lRainStormLastStartAt = (($this->getRainStormLastStartAt())?:MYSQL_DATETIME_FORMAT_NULL_VALUE);
        $lRainStormLastEndAt = (($this->getRainStormLastEndAt())?:MYSQL_DATETIME_FORMAT_NULL_VALUE);
        $lSoilTemp1 = $this->cSoilTemp1;
        $lTemp_3 = $this->cTemp_3;
        $lTemp_4 = $this->cTemp_4;
        $lTemp_2 = $this->cTemp_2;
        $lMoistSoil1 = $this->cMoistSoil1;
        $lMpistSoil2 = $this->cMpistSoil2;
        $lMoistSoil3 = $this->cMoistSoil3;
        $lMoistSoil4 = $this->cMoistSoil4;
        $lWetleaf1 = $this->cWetleaf1;
        $lWetleaf2 = $this->cWetleaf2;
        $lInTemp = $this->cInTemp;
        $lInHum = $this->cInHum;
        $lInDewPoint = $this->cInDewPoint;
        $lInHeatIndex = $this->cInHeatIndex;
        $lBarSeaLevel = $this->cBarSeaLevel;
        $lBarTrend = $this->cBarTrend;
        $lBarAbsolute = $this->cBarAbsolute;
        $lPrevPacketLatency = $this->cPrevPacketLatency;
        $lObservationReceiveTime = $this->cObservationReceiveTime;


        if ($this->cConnection!=NULL) {
            try {

                // Create the prepared statement
                $lStmt = $this->cConnection->prepare("UPDATE " . $this->cObservationsTableName . " SET
                Lsid=?,
                DataStructureType=?,
                Temp=?,
                Hum=?,
                DewPoint=?,
                WetBulb=?,
                HeatIndex=?,
                WindChill=?,
                ThwIndex=?,
                ThswIndex=?,
                WindPpeedLast=?,
                WindDirLast=?,
                WindSpeedAvgLast_1_min=?,
                WindDirScalarAvg_last_1_min=?,
                WindSpeedAvgLast_2_min=?,
                WindDirScalarAvg_last_2_min=?,
                WindSpeedHi_last_2_min=?,
                WindDirAtHiSpeedLast_2_min=?,
                WindSpeedAvgLast_10_min=?,
                WindDirScalarAvgLast_10_min=?,
                WindSpeedHiLast_10_min=?,
                WindDirAtHiSpeedLast_10_min=?,
                RainSize=?,
                RainRateLast=?,
                RainRateHi=?,
                RainfallLast_15_min=?,
                RainRateHiLast_15_min=?,
                RainfallLast_60_min=?,
                RainfallLast_24_hr=?,
                RainStorm=?,
                RainStormStartAt=?,
                SolarRad=?,
                UvIndex=?,
                RxState=?,
                TransBatteryFlag=?,
                RrainfallDaily=?,
                RainfallMonthly=?,
                RainfallYear=?,
                RainStormLast=?,
                RainStormLastStartAt=?,
                RainStormLastEndAt=?,
                SoilTemp1=?,
                Temp_3=?,
                Temp_4=?,
                Temp_2=?,
                MoistSoil1=?,
                MpistSoil2=?,
                MoistSoil3=?,
                MoistSoil4=?,
                Wetleaf1=?,
                Wetleaf2=?,
                InTemp=?,
                InHum=?,
                InDewPoint=?,
                InHeatIndex=?,
                BarSeaLevel=?,
                BarTrend=?,
                BarAbsolute=?,
                PrevPacketLatency=?,
                ObservationReceiveTime=?
                WHERE TransmitterID=?");

                // Check whether successfull
                if ($lStmt === FALSE) {
                    throw new Exception("Could not create prepared statement " . $lStmt->error);
                }

                // bind the parameters to the statement
                $lStmt->bind_param("iiddddddddddddddddddddddddddddsddidddddssdddddddddddddddddisi",
                    $lLsid,
                    $lDataStructureType,
                    $lTemp,
                    $lHum,
                    $lDewPoint,
                    $lWetBulb,
                    $lHeatIndex,
                    $lWindChill,
                    $lThwIndex,
                    $lThswIndex,
                    $lWindPpeedLast,
                    $lWindDirLast,
                    $lWindSpeedAvgLast_1_min,
                    $lWindDirScalarAvg_last_1_min,
                    $lWindSpeedAvgLast_2_min,
                    $lWindDirScalarAvg_last_2_min,
                    $lWindSpeedHi_last_2_min,
                    $lWindDirAtHiSpeedLast_2_min,
                    $lWindSpeedAvgLast_10_min,
                    $lWindDirScalarAvgLast_10_min,
                    $lWindSpeedHiLast_10_min,
                    $lWindDirAtHiSpeedLast_10_min,
                    $lRainSize,
                    $lRainRateLast,
                    $lRainRateHi,
                    $lRainfallLast_15_min,
                    $lRainRateHiLast_15_min,
                    $lRainfallLast_60_min,
                    $lRainfallLast_24_hr,
                    $lRainStorm,
                    $lRainStormStartAt,
                    $lSolarRad,
                    $lUvIndex,
                    $lRxState,
                    $lTransBatteryFlag,
                    $lRrainfallDaily,
                    $lRainfallMonthly,
                    $lRainfallYear,
                    $lRainStormLast,
                    $lRainStormLastStartAt,
                    $lRainStormLastEndAt,
                    $lSoilTemp1,
                    $lTemp_3,
                    $lTemp_4,
                    $lTemp_2,
                    $lMoistSoil1,
                    $lMpistSoil2,
                    $lMoistSoil3,
                    $lMoistSoil4,
                    $lWetleaf1,
                    $lWetleaf2,
                    $lInTemp,
                    $lInHum,
                    $lInDewPoint,
                    $lInHeatIndex,
                    $lBarSeaLevel,
                    $lBarTrend,
                    $lBarAbsolute,
                    $lPrevPacketLatency,
                    $lObservationReceiveTime,
                    $lTransmitterID);

                    //call_user_func_array(array($lStmt, "bind_param"), $lParams); 

                // execute the statement
                if (!$lStmt->execute()) {
                    
                    // Something failed, log the error
                    throw new Exception($lStmt->error);
                } else {
                    
                    if ($lStmt->affected_rows>0) {
                        // Statement was a success, so set the result to true.
                        $lResult = true;
                    } else {

                        // Occasionally, data from the sensor may not have changed and there is nothing to update
                        // therefore it is still successfull if the record exists, if it doesn't exist then the update
                        // query will still return 0 affected rows, so make sure there is a record.
                        if ($this->checkRecordExists()) {
                            $lResult = true;
                        }
                    }
                }

            } catch (Exception $e) {$this->logError($e->getMessage());}
        } else {
            $this->logError("Database is not connected, cannot continue!");
        }

         // return the result
         return $lResult;

    }

    /**
     * Creates a new live observations record. If a record exists or if anything
     * else went wrong, the function will return false. Refer to object error logs for details
     * @return  bool  true If new record was successfull, false if not.
     */
    public function newRecord() {

        // Be negative minded and assume one is related to Murphy who developed Murphy's 
        // law in what can do wrong will go wrong. Lets assume this will fail. 
        $lResult = false;

        // Copying class attributes to local variables might seem clumsy, but there is a reason for this.        
        // The bind_param method expects variables to be passed to it, and not references to variables. 
        // Unfortunately, when attempting to pass the attributes of this class, it is passed as references.
        // the bind_param method does not support passing references to references  
        $lLsid = $this->cLsid;
        $lDataStructureType = $this->cDataStructureType;
        $lTransmitterID = $this->cTransmitterID;
        $lTemp = $this->cTemp;
        $lHum = $this->cHum;
        $lDewPoint = $this->cDewPoint;
        $lWetBulb = $this->cWetBulb;
        $lHeatIndex = $this->cHeatIndex;
        $lWindChill = $this->cWindChill;
        $lThwIndex = $this->cThwIndex;
        $lThswIndex = $this->cThswIndex;
        $lWindPpeedLast = $this->cWindPpeedLast;
        $lWindDirLast = $this->cWindDirLast;
        $lWindSpeedAvgLast_1_min = $this->cWindSpeedAvgLast_1_min;
        $lWindDirScalarAvg_last_1_min = $this->cWindDirScalarAvg_last_1_min;
        $lWindSpeedAvgLast_2_min = $this->cWindSpeedAvgLast_2_min;
        $lWindDirScalarAvg_last_2_min = $this->cWindDirScalarAvg_last_2_min;
        $lWindSpeedHi_last_2_min = $this->cWindSpeedHi_last_2_min;
        $lWindDirAtHiSpeedLast_2_min = $this->cWindDirAtHiSpeedLast_2_min;
        $lWindSpeedAvgLast_10_min = $this->cWindSpeedAvgLast_10_min;
        $lWindDirScalarAvgLast_10_min = $this->cWindDirScalarAvgLast_10_min;
        $lWindSpeedHiLast_10_min = $this->cWindSpeedHiLast_10_min;
        $lWindDirAtHiSpeedLast_10_min = $this->cWindDirAtHiSpeedLast_10_min;
        $lRainSize = $this->cRainSize;
        $lRainRateLast = $this->cRainRateLast;
        $lRainRateHi = $this->cRainRateHi;
        $lRainfallLast_15_min = $this->cRainfallLast_15_min;
        $lRainRateHiLast_15_min = $this->cRainRateHiLast_15_min;
        $lRainfallLast_60_min = $this->cRainfallLast_60_min;
        $lRainfallLast_24_hr = $this->cRainfallLast_24_hr;
        $lRainStorm = $this->cRainStorm;
        $lRainStormStartAt = (($this->getRainStormStartAt())?:MYSQL_DATETIME_FORMAT_NULL_VALUE);
        $lSolarRad = $this->cSolarRad;
        $lUvIndex = $this->cUvIndex;
        $lRxState = $this->cRxState;
        $lTransBatteryFlag = $this->cTransBatteryFlag;
        $lRrainfallDaily = $this->cRrainfallDaily;
        $lRainfallMonthly = $this->cRainfallMonthly;
        $lRainfallYear = $this->cRainfallYear;
        $lRainStormLast = $this->cRainStormLast;
        $lRainStormLastStartAt = (($this->getRainStormLastStartAt())?:MYSQL_DATETIME_FORMAT_NULL_VALUE);
        $lRainStormLastEndAt = (($this->getRainStormLastEndAt())?:MYSQL_DATETIME_FORMAT_NULL_VALUE);
        $lSoilTemp1 = $this->cSoilTemp1;
        $lTemp_3 = $this->cTemp_3;
        $lTemp_4 = $this->cTemp_4;
        $lTemp_2 = $this->cTemp_2;
        $lMoistSoil1 = $this->cMoistSoil1;
        $lMpistSoil2 = $this->cMpistSoil2;
        $lMoistSoil3 = $this->cMoistSoil3;
        $lMoistSoil4 = $this->cMoistSoil4;
        $lWetleaf1 = $this->cWetleaf1;
        $lWetleaf2 = $this->cWetleaf2;
        $lInTemp = $this->cInTemp;
        $lInHum = $this->cInHum;
        $lInDewPoint = $this->cInDewPoint;
        $lInHeatIndex = $this->cInHeatIndex;
        $lBarSeaLevel = $this->cBarSeaLevel;
        $lBarTrend = $this->cBarTrend;
        $lBarAbsolute = $this->cBarAbsolute;
        $lPrevPacketLatency = $this->cPrevPacketLatency;
        $lObservationReceiveTime = $this->cBarAbsolute;

        if ($this->cConnection!=NULL) {

            try {

                // Create the prepared statement
                $lStmt = $this->cConnection->prepare("INSERT INTO " . $this->cObservationsTableName .
                    " (Lsid,
                    DataStructureType,
                    TransmitterID,
                    Temp,
                    Hum,
                    DewPoint,
                    WetBulb,
                    HeatIndex,
                    WindChill,
                    ThwIndex,
                    ThswIndex,
                    WindPpeedLast,
                    WindDirLast,
                    WindSpeedAvgLast_1_min,
                    WindDirScalarAvg_last_1_min,
                    WindSpeedAvgLast_2_min,
                    WindDirScalarAvg_last_2_min,
                    WindSpeedHi_last_2_min,
                    WindDirAtHiSpeedLast_2_min,
                    WindSpeedAvgLast_10_min,
                    WindDirScalarAvgLast_10_min,
                    WindSpeedHiLast_10_min,
                    WindDirAtHiSpeedLast_10_min,
                    RainSize,
                    RainRateLast,
                    RainRateHi,
                    RainfallLast_15_min,
                    RainRateHiLast_15_min,
                    RainfallLast_60_min,
                    RainfallLast_24_hr,
                    RainStorm,
                    RainStormStartAt,
                    SolarRad,
                    UvIndex,
                    RxState,
                    TransBatteryFlag,
                    RrainfallDaily,
                    RainfallMonthly,
                    RainfallYear,
                    RainStormLast,
                    RainStormLastStartAt,
                    RainStormLastEndAt,
                    SoilTemp1,
                    Temp_3,
                    Temp_4,
                    Temp_2,
                    MoistSoil1,
                    MpistSoil2,
                    MoistSoil3,
                    MoistSoil4,
                    Wetleaf1,
                    Wetleaf2,
                    InTemp,
                    InHum,
                    InDewPoint,
                    InHeatIndex,
                    BarSeaLevel,
                    BarTrend,
                    BarAbsolute,
                    PrevPacketLatency,
                    ObservationReceiveTime)
                    VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,
                    ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

                // Check whether successfull
                if ($lStmt===FALSE) {
                    throw new Exception("Could not create prepared statement " . $lStmt->error);
                }

                // bind the parameters to the statement
                $lStmt->bind_param("iiiddddddddddddddddddddddddddddsddidddddssdddddddddddddddddis",
                    $lLsid,
                    $lDataStructureType,
                    $lTransmitterID,
                    $lTemp,
                    $lHum,
                    $lDewPoint,
                    $lWetBulb,
                    $lHeatIndex,
                    $lWindChill,
                    $lThwIndex,
                    $lThswIndex,
                    $lWindPpeedLast,
                    $lWindDirLast,
                    $lWindSpeedAvgLast_1_min,
                    $lWindDirScalarAvg_last_1_min,
                    $lWindSpeedAvgLast_2_min,
                    $lWindDirScalarAvg_last_2_min,
                    $lWindSpeedHi_last_2_min,
                    $lWindDirAtHiSpeedLast_2_min,
                    $lWindSpeedAvgLast_10_min,
                    $lWindDirScalarAvgLast_10_min,
                    $lWindSpeedHiLast_10_min,
                    $lWindDirAtHiSpeedLast_10_min,
                    $lRainSize,
                    $lRainRateLast,
                    $lRainRateHi,
                    $lRainfallLast_15_min,
                    $lRainRateHiLast_15_min,
                    $lRainfallLast_60_min,
                    $lRainfallLast_24_hr,
                    $lRainStorm,
                    $lRainStormStartAt,
                    $lSolarRad,
                    $lUvIndex,
                    $lRxState,
                    $lTransBatteryFlag,
                    $lRrainfallDaily,
                    $lRainfallMonthly,
                    $lRainfallYear,
                    $lRainStormLast,
                    $lRainStormLastStartAt,
                    $lRainStormLastEndAt,
                    $lSoilTemp1,
                    $lTemp_3,
                    $lTemp_4,
                    $lTemp_2,
                    $lMoistSoil1,
                    $lMpistSoil2,
                    $lMoistSoil3,
                    $lMoistSoil4,
                    $lWetleaf1,
                    $lWetleaf2,
                    $lInTemp,
                    $lInHum,
                    $lInDewPoint,
                    $lInHeatIndex,
                    $lBarSeaLevel,
                    $lBarTrend,
                    $lBarAbsolute,
                    $lPrevPacketLatency,
                    $lObservationReceiveTime);

                // execute the statement
                if (!$lStmt->execute()) {
                    
                    // Something failed, log the error
                    throw new Exception($lStmt->error);
                } else {
                    
                    // Statement was a success, so set the result to true.
                    $lResult = true;
                }

            } catch (Exception $e) {$this->logError($e->getMessage());}
        } else {
            $this->logError("Database is not connected, cannot continue!");
        }
        // return the result
        return $lResult;

    }

    /**
     * Gets the observation record from the database assuming a default transmitter ID of 1
     * Loads the result into this object.
     * @param   keyIndex    The transmitter ID of the observations record
     * @return  bool        true If successful, false if there were issues.
     */
    public function getRecord($keyIndex = 1) {

        // Be negative minded and assume one is related to Murphy who developed Murphy's 
        // law in what can do wrong will go wrong. Lets assume this will fail. 
        $lResult = false;
        
        if ($this->cConnection!=NULL) {

            try {

                // Create the prepared statement
                $lStmt = $this->cConnection->prepare("SELECT 
                    Lsid,
                    DataStructureType,
                    TransmitterID,
                    Temp,
                    Hum,
                    DewPoint,
                    WetBulb,
                    HeatIndex,
                    WindChill,
                    ThwIndex,
                    ThswIndex,
                    WindPpeedLast,
                    WindDirLast,
                    WindSpeedAvgLast_1_min,
                    WindDirScalarAvg_last_1_min,
                    WindSpeedAvgLast_2_min,
                    WindDirScalarAvg_last_2_min,
                    WindSpeedHi_last_2_min,
                    WindDirAtHiSpeedLast_2_min,
                    WindSpeedAvgLast_10_min,
                    WindDirScalarAvgLast_10_min,
                    WindSpeedHiLast_10_min,
                    WindDirAtHiSpeedLast_10_min,
                    RainSize,
                    RainRateLast,
                    RainRateHi,
                    RainfallLast_15_min,
                    RainRateHiLast_15_min,
                    RainfallLast_60_min,
                    RainfallLast_24_hr,
                    RainStorm,
                    RainStormStartAt,
                    SolarRad,
                    UvIndex,
                    RxState,
                    TransBatteryFlag,
                    RrainfallDaily,
                    RainfallMonthly,
                    RainfallYear,
                    RainStormLast,
                    RainStormLastStartAt,
                    RainStormLastEndAt,
                    SoilTemp1,
                    Temp_3,
                    Temp_4,
                    Temp_2,
                    MoistSoil1,
                    MpistSoil2,
                    MoistSoil3,
                    MoistSoil4,
                    Wetleaf1,
                    Wetleaf2,
                    InTemp,
                    InHum,
                    InDewPoint,
                    InHeatIndex,
                    BarSeaLevel,
                    BarTrend,
                    BarAbsolute,
                    PrevPacketLatency,
                    ObservationReceiveTime
                    FROM " . $this->cObservationsTableName . " WHERE TransmitterID = " . $keyIndex);

                // Check whether successfull
                if ($lStmt===FALSE) {
                    throw new Exception("Could not create prepared statement " . $lStmt->error);
                }
  

                // execute the statement
                if (!$lStmt->execute()) {
                    
                    // Something failed, log the error
                    throw new Exception($lStmt->error);
                } 

                // bind the parameters to the statement
                if (!$lStmt->bind_result(
                    $lLsid,
                    $lDataStructureType,
                    $lTransmitterID,
                    $lTemp,
                    $lHum,
                    $lDewPoint,
                    $lWetBulb,
                    $lHeatIndex,
                    $lWindChill,
                    $lThwIndex,
                    $lThswIndex,
                    $lWindPpeedLast,
                    $lWindDirLast,
                    $lWindSpeedAvgLast_1_min,
                    $lWindDirScalarAvg_last_1_min,
                    $lWindSpeedAvgLast_2_min,
                    $lWindDirScalarAvg_last_2_min,
                    $lWindSpeedHi_last_2_min,
                    $lWindDirAtHiSpeedLast_2_min,
                    $lWindSpeedAvgLast_10_min,
                    $lWindDirScalarAvgLast_10_min,
                    $lWindSpeedHiLast_10_min,
                    $lWindDirAtHiSpeedLast_10_min,
                    $lRainSize,
                    $lRainRateLast,
                    $lRainRateHi,
                    $lRainfallLast_15_min,
                    $lRainRateHiLast_15_min,
                    $lRainfallLast_60_min,
                    $lRainfallLast_24_hr,
                    $lRainStorm,
                    $lRainStormStartAt,
                    $lSolarRad,
                    $lUvIndex,
                    $lRxState,
                    $lTransBatteryFlag,
                    $lRrainfallDaily,
                    $lRainfallMonthly,
                    $lRainfallYear,
                    $lRainStormLast,
                    $lRainStormLastStartAt,
                    $lRainStormLastEndAt,
                    $lSoilTemp1,
                    $lTemp_3,
                    $lTemp_4,
                    $lTemp_2,
                    $lMoistSoil1,
                    $lMpistSoil2,
                    $lMoistSoil3,
                    $lMoistSoil4,
                    $lWetleaf1,
                    $lWetleaf2,
                    $lInTemp,
                    $lInHum,
                    $lInDewPoint,
                    $lInHeatIndex,
                    $lBarSeaLevel,
                    $lBarTrend,
                    $lBarAbsolute,
                    $lPrevPacketLatency,
                    $lObservationReceiveTime)) {
                        throw new Exception("Could not bind the result. " . $lStmt->error);
                    }

                    $lFetchResult = $lStmt->fetch(); 
                    if ($lFetchResult == false) {
                        throw new Exception("Could not fetch the result. " . $lStmt->error); 
                    } else {

                        // Copying class attributes to local variables might seem clumsy, but there is a reason for this.        
                        // The bind_param method expects variables to be passed to it, and not references to variables. 
                        // Unfortunately, when attempting to pass the attributes of this class, it is passed as references.
                        // the bind_param method does not support passing references to references  
                        $this->cLsid = $lLsid;
                        $this->cDataStructureType = $lDataStructureType;
                        $this->cTransmitterID = $lTransmitterID;
                        $this->cTemp = $lTemp;
                        $this->cHum = $lHum;
                        $this->cDewPoint = $lDewPoint;
                        $this->cWetBulb = $lWetBulb;
                        $this->cHeatIndex = $lHeatIndex;
                        $this->cWindChill = $lWindChill;
                        $this->cThwIndex = $lThwIndex;
                        $this->cThswIndex = $lThswIndex;
                        $this->cWindPpeedLast = $lWindPpeedLast;
                        $this->cWindDirLast = $lWindDirLast;
                        $this->cWindSpeedAvgLast_1_min = $lWindSpeedAvgLast_1_min;
                        $this->cWindDirScalarAvg_last_1_min = $lWindDirScalarAvg_last_1_min;
                        $this->cWindSpeedAvgLast_2_min = $lWindSpeedAvgLast_2_min;
                        $this->cWindDirScalarAvg_last_2_min = $lWindDirScalarAvg_last_2_min;
                        $this->cWindSpeedHi_last_2_min = $lWindSpeedHi_last_2_min;
                        $this->cWindDirAtHiSpeedLast_2_min = $lWindDirAtHiSpeedLast_2_min;
                        $this->cWindSpeedAvgLast_10_min = $lWindSpeedAvgLast_10_min;
                        $this->cWindDirScalarAvgLast_10_min = $lWindDirScalarAvgLast_10_min;
                        $this->cWindSpeedHiLast_10_min = $lWindSpeedHiLast_10_min;
                        $this->cWindDirAtHiSpeedLast_10_min = $lWindDirAtHiSpeedLast_10_min;
                        $this->cRainSize = $lRainSize;
                        $this->cRainRateLast = $lRainRateLast;
                        $this->cRainRateHi = $lRainRateHi;
                        $this->cRainfallLast_15_min = $lRainfallLast_15_min;
                        $this->cRainRateHiLast_15_min = $lRainRateHiLast_15_min;
                        $this->cRainfallLast_60_min = $lRainfallLast_60_min;
                        $this->cRainfallLast_24_hr = $lRainfallLast_24_hr;
                        $this->cRainStorm = $lRainStorm;
                        try {$this->setRainStormStartAt(($lRainStormStartAt==MYSQL_DATETIME_FORMAT_NULL_VALUE)?NULL:date_timestamp_get(date_create($lRainStormStartAt)));}
                        catch (Exception $e) {$this->logError("Failed to convert time into timestamp" . $e->getMessage()); $this->setRainStormStartAt(NULL);}
                        $this->cSolarRad = $lSolarRad;
                        $this->cUvIndex = $lUvIndex;
                        $this->cRxState = $lRxState;
                        $this->cTransBatteryFlag = $lTransBatteryFlag;
                        $this->cRrainfallDaily = $lRrainfallDaily;
                        $this->cRainfallMonthly = $lRainfallMonthly;
                        $this->cRainfallYear = $lRainfallYear;
                        $this->cRainStormLast = $lRainStormLast;
                        try {$this->setRainStormLastStartAt(($lRainStormLastStartAt==MYSQL_DATETIME_FORMAT_NULL_VALUE)?NULL:date_timestamp_get(date_create($lRainStormLastStartAt)));}
                        catch (Exception $e) {$this->logError("Failed to convert time into timestamp" . $e->getMessage()); $this->setRainStormLastStartAt(NULL);}
                        try {$this->setRainStormLastEndAt(($lRainStormLastEndAt==MYSQL_DATETIME_FORMAT_NULL_VALUE)?NULL:date_timestamp_get(date_create($lRainStormLastEndAt)));}
                        catch (Exception $e) {$this->logError("Failed to convert time into timestamp" . $e->getMessage()); $this->setRainStormLastEndAt(NULL);}
                        $this->cSoilTemp1 = $lSoilTemp1;
                        $this->cTemp_3 = $lTemp_3;
                        $this->cTemp_4 = $lTemp_4;
                        $this->cTemp_2 = $lTemp_2;
                        $this->cMoistSoil1 = $lMoistSoil1;
                        $this->cMpistSoil2 = $lMpistSoil2;
                        $this->cMoistSoil3 = $lMoistSoil3;
                        $this->cMoistSoil4 = $lMoistSoil4;
                        $this->cWetleaf1 = $lWetleaf1;
                        $this->cWetleaf2 = $lWetleaf2;
                        $this->cInTemp = $lInTemp;
                        $this->cInHum = $lInHum;
                        $this->cInDewPoint = $lInDewPoint;
                        $this->cInHeatIndex = $lInHeatIndex;
                        $this->cBarSeaLevel = $lBarSeaLevel;
                        $this->cBarTrend = $lBarTrend;
                        $this->cBarAbsolute = $lBarAbsolute;
                        $this->cPrevPacketLatency = $lPrevPacketLatency;
                        $this->cObservationReceiveTime = $lObservationReceiveTime;

                        // Everything ran as expected no issues.
                        $lResult = true;

                    }

            } catch (Exception $e) {$this->logError($e->getMessage());}
        } else {
            $this->logError("Database is not connected, cannot continue!");
        }
        // return the result
        return $lResult;

    }

    /**
     * Checks the observations table in the database to see if there is a record with this object's
     * TransmitterID.
     * @param   keyIndex    The transmitter ID if this object hasn't been set yet (optional)
     * @return  bool        true If a record was found, false If not. 
     */
    public function checkRecordExists($keyIndex = -1) {
        
        if ($this->cTransmitterID!=null) {
            $keyIndex = $this->cTransmitterID;
        }

        try {
            if ($lResult = $this->cConnection->query("SELECT * FROM " . $this->cObservationsTableName . " WHERE TransmitterID = " . $keyIndex)) {
                if ($lResult->num_rows>0) {
                    return true;
                }
            }
        } catch (Exception $e) {
            $this->logError($e->getMessage());
        }

        return false;

    }

    // private attributes

    // what to call the table in the database
    private $cObservationsTableName;

    // The sql connection object
    private $cConnection;

}

?>