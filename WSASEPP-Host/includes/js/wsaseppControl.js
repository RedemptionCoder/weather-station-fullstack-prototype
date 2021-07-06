/** App Constants */

// The frequency to check for new weather observation data
const REFRESH_INTERVAL = 2.5;

// The period of time over which to calculate the average latency
const LAT_AVERAGING_PERIOD = 60;

// The amount of times last updated time is allowed to be the same for offline status
const CONNECTION_LOSS_TOLERANCE = 5;

// The amount of times last updated time is allowed to be the same with connection issues
const CONNECTION_ISSUES_TOLERANCE = 2;

// The url that will return Bureau of Meteorology station data
const BOM_COMPARISON_STATION_URL = "http://www.bom.gov.au/fwo/IDV60901/IDV60901.94865.json";

// The amount of minutes to wait untill it's time to refresh bom data
const BOM_STATION_REFRESH_INTERVAL = 10;

// The amount of time the comparison data is shown
const COMPARISON_TIMEOUT = 10000;

/**
 * This angular js service contains all of the performance related information of the weather station app 
 * data retrieval.
 */
app.service('$ConnectionPerformance', function() {

    this.RefreshInterval=REFRESH_INTERVAL;
    this.LatAvgPeriod=LAT_AVERAGING_PERIOD;
    this.ConnectionLossTolerance=CONNECTION_LOSS_TOLERANCE;
    this.ConnectionIssuesTolerance=CONNECTION_ISSUES_TOLERANCE;

    // The start time before a request
    this.StartTime = new Date();

    // The last received date for detecting if site server is offline
    this.LastRecDate = "";

    // The time taken to process request in milliseconds
    this.TimeTaken = 0;

    // This gets incremented if the last update time hasn't changed
    this.PossibleDownSiteServerCount = 0;

    // The array containing the numbers
    this.avgArray = new Array();

    // The length of the array. 
    this.length = this.LatAvgPeriod; 

     /**
     * Adds the number to the list of numbers to calculate average
     * @param number    The number to add to the list of numbers.
     */
      this.addNumber = function(number) {
        if (this.avgArray.length > this.length && length > 0) {
            this.avgArray.shift();
        }
        this.avgArray.push(number);
    }

    /**
     * Calculates the average of all of the numbers added. 
     * @returns The average of the numbers added
     */
    this.average = function() {
        let lTotal = 0;
        for (var i = 0; i < this.avgArray.length; i++) {lTotal+=this.avgArray[i];}
        return Math.round(lTotal / this.avgArray.length);
    }

});

/**
 * Starts the whole client side of the application. This is the controller object. 
 */
app.controller("wsaseppControl", function($scope, $http, $interval, $timeout, $ConnectionPerformance) {

    /**
     * Gets live the weather data from the WSASEPP server
     */
    $scope.getWeatherData = function() {
            
        // use Get method as this enables for bookmarking etc.
        // there is no sensitive data here. 

        // Record the start time
        $ConnectionPerformance.StartTime = new Date();

        /*
        if ($http.pendingRequests.length > 0) {
            $scope.reportError("More than one pending request");
        }
        */

        // Open the request object
        $http.get("getLiveObs.php?transmitterID=1&temp=1&speed=3&dir=8&rain=14&bar=12").then(
            function(response) {
                
                $scope.stationData = response.data;
                
                // Display the data returned by the server
                $scope.displayWeatherData($scope.stationData, $ConnectionPerformance);
            },
            function(response) {}
        );
        
    }

    /**
     * Takes the data from the json string and sets the values for the observations
     * on the pagel
     * @param {*} jsonData 
     */
    $scope.displayWeatherData = function(jsonData, performanceData=null) {

        // Go through each value in the JSON object, and if it is a key, 
        // get the value for the key and set the inner HTML of the document
        // element with the same id as the key with the value of the key.
            
        try {
            // If the key is a key, assign the value to the associated
            // HTML element. 
            if (!$scope.isComparing) {
                $scope.AppTemp = (!jsonData['AppTemp']) ? "---":jsonData['AppTemp'];
                $scope.Temp = (!jsonData['Temp']) ? "---":jsonData['Temp'];                       
                $scope.Hum = (!jsonData['Hum']) ? "---":jsonData['Hum'];                        
                $scope.DewPoint = (!jsonData['DewPoint']) ? "---":jsonData['DewPoint'];                   
                $scope.WetBulb = (!jsonData['WetBulb']) ? "---":jsonData['WetBulb'];                    
                $scope.HeatIndex = (!jsonData['HeatIndex']) ? "---":jsonData['HeatIndex'];                  
                $scope.WindChill = (!jsonData['WindChill']) ? "---":jsonData['WindChill'];                  
                $scope.ThwIndex = (!jsonData['ThwIndex']) ? "---":jsonData['WindChill'];                   
                $scope.ThswIndex = (!jsonData['ThswIndex']) ? "---":jsonData['ThswIndex'];                  
                $scope.WindSpeedAvgLast_2_min = (!jsonData['WindSpeedAvgLast_2_min']) ? "---":jsonData['WindSpeedAvgLast_2_min'];     
                $scope.WindDirScalarAvg_last_2_min = (!jsonData['WindDirScalarAvg_last_2_min']) ? "---":jsonData['WindDirScalarAvg_last_2_min'];
                $scope.WindSpeedHi_last_2_min = (!jsonData['WindSpeedHi_last_2_min']) ? "---":jsonData['WindSpeedHi_last_2_min'];     
                $scope.WindDirAtHiSpeedLast_2_min = (!jsonData['WindDirAtHiSpeedLast_2_min']) ? "---":jsonData['WindDirAtHiSpeedLast_2_min']; 
                $scope.WindSpeedAvgLast_10_min = (!jsonData['WindSpeedAvgLast_10_min']) ? "---":jsonData['WindSpeedAvgLast_10_min'];    
                $scope.WindDirScalarAvgLast_10_min = (!jsonData['WindDirScalarAvgLast_10_min']) ? "---":jsonData['WindDirScalarAvgLast_10_min'];
                $scope.WindSpeedHiLast_10_min = (!jsonData['WindSpeedHiLast_10_min']) ? "---":jsonData['WindSpeedHiLast_10_min'];     
                $scope.WindDirAtHiSpeedLast_10_min = (!jsonData['WindDirAtHiSpeedLast_10_min']) ? "---":jsonData['WindDirAtHiSpeedLast_10_min']; 
                $scope.RainRateLast = (!jsonData['RainRateLast']) ? "---":jsonData['RainRateLast'];               
                $scope.RainRateHi = (!jsonData['RainRateHi']) ? "---":jsonData['RainRateHi'];                 
                $scope.RainfallLast_15_min = (!jsonData['RainfallLast_15_min']) ? "---":jsonData['RainfallLast_15_min'];        
                $scope.RainRateHiLast_15_min = (!jsonData['RainRateHiLast_15_min']) ? "---":jsonData['RainRateHiLast_15_min'];      
                $scope.RainfallLast_60_min = (!jsonData['RainfallLast_60_min']) ? "---":jsonData['RainfallLast_60_min'];        
                $scope.RainfallLast_24_hr = (!jsonData['RainfallLast_24_hr']) ? "---":jsonData['RainfallLast_24_hr'];         
                $scope.RainStorm = (!jsonData['RainStorm']) ? "---":jsonData['RainStorm'];                  
                $scope.RainStormStartAt = (!jsonData['RainStormStartAt']) ? "---":jsonData['RainStormStartAt'];           
                $scope.SolarRad = (!jsonData['SolarRad']) ? "---":jsonData['SolarRad'];                   
                $scope.UvIndex = (!jsonData['UvIndex']) ? "---":jsonData['UvIndex'];                    
                $scope.RainfallDaily = (!jsonData['RainfallDaily']) ? "---":jsonData['RainfallDaily'];             
                $scope.RainfallMonthly = (!jsonData['RainfallMonthly']) ? "---":jsonData['RainfallMonthly'];            
                $scope.RainfallYear = (!jsonData['RainfallYear']) ? "---":jsonData['RainfallYear'];               
                $scope.RainStormLast = (!jsonData['RainStormLast']) ? "---":jsonData['RainStormLast'];              
                $scope.RainStormLastStartAt = (!jsonData['RainStormLastStartAt']) ? "---":jsonData['RainStormLastStartAt'];       
                $scope.RainStormLastEndAt = (!jsonData['RainStormLastEndAt']) ? "---":jsonData['RainStormLastEndAt'];         
                $scope.InAppTemp = (!jsonData['InAppTemp']) ? "---":jsonData['InAppTemp'];                 
                $scope.InAppTempBottom = (!jsonData['InAppTemp']) ? "---":jsonData['InAppTemp'];                 
                $scope.InTemp = (!jsonData['InTemp']) ? "---":jsonData['InTemp'];                     
                $scope.InTempBottom = (!jsonData['InTemp']) ? "---":jsonData['InTemp'];                     
                $scope.InHum = (!jsonData['InHum']) ? "---":jsonData['InHum'];                      
                $scope.InHumBottom = (!jsonData['InHum']) ? "---":jsonData['InHum'];                      
                $scope.InDewPoint = (!jsonData['InDewPoint']) ? "---":jsonData['InDewPoint'];                 
                $scope.InHeatIndex = (!jsonData['InHeatIndex']) ? "---":jsonData['InHeatIndex'];                
                $scope.BarSeaLevel = (!jsonData['BarSeaLevel']) ? "---":jsonData['BarSeaLevel'];    
            }            

            $scope.WindPpeedLast = jsonData['WindPpeedLast'];              
            $scope.WindDirLast = jsonData['WindDirLast'];         

            if (performanceData) {$scope.displayServerInfo(jsonData, performanceData);}
            
        } catch (lError) {
            // there will be some json keys with data that will not have a 
            // corresponding document element
            
            $scope.reportError(lError,true);

        }

    }

    /**
     * Initialises the view bind data
     */
    $scope.initialiseView = function() {

        $scope.HeadingTitle = "Altitude Point Cook Live Weather Data";
        $scope.errorOutput = "No Current Errors";

        $scope.AppTemp = "---";
        $scope.Temp = "---";                       
        $scope.Hum = "---";                        
        $scope.DewPoint = "---";                   
        $scope.WetBulb = "---";                    
        $scope.HeatIndex = "---";                  
        $scope.WindChill = "---";                  
        $scope.ThwIndex = "---";                   
        $scope.ThswIndex = "---";                  
        $scope.WindSpeedAvgLast_2_min = "---";     
        $scope.WindDirScalarAvg_last_2_min = "---";
        $scope.WindSpeedHi_last_2_min = "---";     
        $scope.WindDirAtHiSpeedLast_2_min = "---"; 
        $scope.WindSpeedAvgLast_10_min = "---";    
        $scope.WindDirScalarAvgLast_10_min = "---";
        $scope.WindSpeedHiLast_10_min = "---";     
        $scope.WindDirAtHiSpeedLast_10_min = "---"; 
        $scope.RainRateLast = "---";               
        $scope.RainRateHi = "---";                 
        $scope.RainfallLast_15_min = "---";        
        $scope.RainRateHiLast_15_min = "---";      
        $scope.RainfallLast_60_min = "---";        
        $scope.RainfallLast_24_hr = "---";         
        $scope.RainStorm = "---";                  
        $scope.RainStormStartAt = "---";           
        $scope.SolarRad = "---";                   
        $scope.UvIndex = "---";                    
        $scope.RainfallDaily = "---";             
        $scope.RainfallMonthly = "---";            
        $scope.RainfallYear = "---";               
        $scope.RainStormLast = "---";              
        $scope.RainStormLastStartAt = "---";       
        $scope.RainStormLastEndAt = "---";         
        $scope.InAppTemp = "---";                 
        $scope.InAppTempBottom = "---";                 
        $scope.InTemp = "---";                     
        $scope.InTempBottom = "---";                     
        $scope.InHum = "---";                      
        $scope.InHumBottom = "---";                      
        $scope.InDewPoint = "---";                 
        $scope.InHeatIndex = "---";                
        $scope.BarSeaLevel = "---";                

    }

    /**
     * Gets data from the Australian Bureau of Meteorology and loads it into the comparison blocks 
     * on the page. 
     */
    $scope.getBomData = function() {

        $http.get("getApiResponse.php?url="+encodeURI(BOM_COMPARISON_STATION_URL)).then(function(response) {
            
            $scope.bomData = response.data;
            
            // Get the timestamp set
            $scope.bomTimestamp = $scope.bomData.observations.data[0].aifstime_utc;

        },
        function(response) {});
        
    }

    /**
     * Takes the JSON string from the Bureau of Meteorology and extracts the data from it and places
     * it into the comparison blocks on the page. 
     * 
     * @param {String} jsonData 
     */
    $scope.displayBomData = function(jsonData) {
        
        // Clear all of the view elements for bom data.
        $scope.initialiseView();

        try {

            $scope.HeadingTitle = jsonData.observations.data[0].name + 
                " at " + jsonData.observations.data[0].local_date_time + " (bom.gov.au)";
            $scope.AppTemp = jsonData.observations.data[0].apparent_t;
            $scope.Temp = jsonData.observations.data[0].air_temp;
            $scope.Hum = jsonData.observations.data[0].rel_hum;
            $scope.WindSpeedAvgLast_10_min = jsonData.observations.data[0].wind_spd_kmh;
            $scope.WindSpeedHiLast_10_min = jsonData.observations.data[0].gust_kmh;
            $scope.WindDirScalarAvgLast_10_min = jsonData.observations.data[0].wind_dir;
            $scope.BarSeaLevel = jsonData.observations.data[0].press_msl;
            $scope.RainfallDaily = jsonData.observations.data[0].rain_trace;
            $scope.DewPoint = jsonData.observations.data[0].dewpt;
            $scope.bomTimestamp = jsonData.observations.data[0].aifstime_utc;
        
        } catch (e) {}

    }

    /**
     * Creates a date object from a date and time string in the yyyymmddhhmmss format
     * @param {String} dateString Date string in yyyymmddhhmmss format
     * @param {boolean} isUTC default is false, but set to true if dateString is UTC
     * @returns Date    The date object from the date and time string
     */
    $scope.getDateFromFullString = function(dateString, isUTC=false) {

        let lObsTimestamp = new Date();

        // Exract the year portion of the string
        let lYear = parseInt(dateString.substring(0, 4));
        // Extract the month portion of the string
        let lMonth = parseInt(dateString.substring(4, 6));
        // Extract the day portion of the string
        let lDay = parseInt(dateString.substring(6, 8));
        // Extract the hour portion of the string
        let lHour = parseInt(dateString.substring(8, 10));
        // Extract the minute portion of the string
        let lMinute = parseInt(dateString.substring(10, 12));
        // Extract the second portion of the string
        let lSecond = parseInt(dateString.substring(12, 14));


        if (isUTC) {

            lObsTimestamp.setUTCFullYear(lYear, lMonth-1, lDay);
            lObsTimestamp.setUTCHours(lHour,lMinute,lSecond);

        } else {
        
            // Create the date object for the bom timestamp
            lObsTimestamp = new Date(lYear, lMonth-1, lDay, lHour, lMinute, lSecond, 0);
        
        }
        return lObsTimestamp;

    }

    /**
     * Checks if updated observation data is available from bom
     * @returns true If successfull, or false if there were errors, like unexpected data from bom server
     */
    $scope.checkBomUpdates = function() {

        // Get the current date and time as UTC 
        let lCurrentTime = new Date();
        
        // Get the current bom obs timestamp from the data area of the page
        let lObsTimestampStr = $scope.bomTimestamp;

        try {

            // Create the date object for the bom timestamp
            let lObsTimestamp = $scope.getDateFromFullString(lObsTimestampStr,true);
            
            // Determine the difference in current time and observation timestamp
            let lTimeElapsed = lCurrentTime-lObsTimestamp;

            if (lTimeElapsed > (BOM_STATION_REFRESH_INTERVAL*60*1000)) {$scope.getBomData();} 

        } catch (e) {return false;}

        return true;

    }

    /**
     * Switches display of comparison blocks to visible from hidden
     */
    $scope.showComparisonData = function() {
        
        // Check for updated observation data from bom. 
        $scope.checkBomUpdates();

        // Set comparison state to true
        $scope.isComparing = true;

        // Display the bom data
        $scope.displayBomData($scope.bomData);

    }

    /**
     * Switches display of comparison blocks to visible from hidden for a second
     */
    $scope.switchStations = function() {
        
        if ($scope.isComparing) {
            $scope.hideComparisonData();
            $scope.isComparing = false;
        } else {
            $scope.showComparisonData();
            $scope.isComparing = true;
        }

        // Essentiallly reset the timetout counter
        if ($scope.hasOwnProperty('timeoutPromise')) {
            $timeout.cancel($scope.timeoutPromise);
        }

        // Start the interval counter
        $scope.timeoutPromise = $timeout($scope.hideComparisonData, COMPARISON_TIMEOUT);

    }

    /**
     * Hides the comparison data again.
     */
    $scope.hideComparisonData = function() {

        // Set comparison state to false
        $scope.isComparing = false;

        // Reset all view displays
        $scope.initialiseView();

        // Display the station data
        $scope.displayWeatherData($scope.stationData);

    }

    /**
     * Displays an error message below the page title in a red bar
     * @param {*} message   The error message to show
     */
    $scope.reportError = function(message, replace=false, warning=false) {

        if (replace) {
            $scope.errorOutput = message;
        } else {
            $scope.errorOutput+="<br>"+message;
        }

        // This will display the error bar up the top
        $scope.isErrorState = true;

        // Change to a different colour if warning
        $scope.errorBarBackgroundColour = (warning)?{"background-color":"#625454"}:{"background-color":"#c60000"};

    }

    /**
     * Clears the error message bar and removes is from display.
     */
    $scope.clearErrors = function() {

        $scope.errorOutput="&lt;&lt;No Errors&gt;&gt;";
        $scope.isErrorState = false;

    }

    /**
     * Displays server status and latency info such as last updated record and time taken from
     * collecting the data from the weahter station to displaying it on this page. 
     * @param {*} JsonObject 
     */
    $scope.displayServerInfo = function(JsonObject) {

        // Record the completed time
        let lEndTime = new Date();

        // calculate the time taken for the request
        let lTimeTaken = lEndTime - $ConnectionPerformance.StartTime;

        // Calculate the total time taken for the previous packet
        let lTotalTimeTaken = $ConnectionPerformance.TimeTaken + JsonObject['prev_packet_latency'];

        // Add latency value for averaging
        $ConnectionPerformance.addNumber(lTotalTimeTaken);

        if ($ConnectionPerformance.LastRecDate==JsonObject['last_packet_rec_time']) {
            // Increment lost connection counter
            $ConnectionPerformance.PossibleDownSiteServerCount++;
            
            if ($ConnectionPerformance.PossibleDownSiteServerCount>=$ConnectionPerformance.ConnectionIssuesTolerance) {
                // Display last updated
                $scope.obstime = JsonObject['last_packet_rec_time'];
                // Rerpot connectivity issues
                $scope.reportError("Server Bandwidth/Performance Issues", true, true);
            }

        } else {
            // Reset the connection loss counter
            $ConnectionPerformance.PossibleDownSiteServerCount = 0;
            // Display last updated
            $scope.obstime = "LIVE";
            // clear errors
            $scope.clearErrors();
        }

        if ($ConnectionPerformance.PossibleDownSiteServerCount<$ConnectionPerformance.ConnectionLossTolerance) {
            
            if ($ConnectionPerformance.PossibleDownSiteServerCount>=$ConnectionPerformance.ConnectionIssuesTolerance) {
                // Display the time as ---
                $scope.Latency = "---";  
            } else {
                // Display the time taken
                $scope.Latency = lTotalTimeTaken + 
                    "ms (u" + JsonObject['prev_packet_latency'] + ", " + 
                    " d" + $ConnectionPerformance.TimeTaken + ") Avg ("+$ConnectionPerformance.average()+"ms)";    
            }
        } else {
            // Display the time as ---
            $scope.Latency = "---";  
            // Report error that site server is offline
            $scope.reportError("Site Server Offline", true);
        }

        // Update the next previous time taken for this request
        $ConnectionPerformance.TimeTaken = lTimeTaken;

        // Get the last packet receive time date 
        $ConnectionPerformance.LastRecDate = JsonObject['last_packet_rec_time'];

    }

    /**
     * Initialises the app parameters. 
     */
    $scope.initialiseApp = function() {

        // Initialise the bom data timestamp
        $scope.bomTimestamp = "";

        // Initialise server info display
        $scope.Latency = "---";
        $scope.obstime = "---";

        // Initially, comparison is not taking place
        $scope.isComparing = false;

        // Initially there are no errors
        $scope.isErrorState = false;
        $scope.errorBarBackgroundColour = {"background-color" : "#625454"};

    }

    // Initialise the app
    $scope.initialiseApp();

    // Initialise the view
    $scope.initialiseView();

    // Get the weather data straight away first
    $scope.getWeatherData();

    // Get the bom comparison data
    $scope.getBomData();

    // Start the refresh timer
    $interval($scope.getWeatherData, ($ConnectionPerformance.RefreshInterval * 1000))

});