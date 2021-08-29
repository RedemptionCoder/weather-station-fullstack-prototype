/** App Constants */

// The frequency to check for new weather observation data
const REFRESH_INTERVAL = 2.5;

// The period of time over which to calculate the average latency
const LAT_AVERAGING_PERIOD = 60;

// The amount of times last updated time is allowed to be the same for offline status
const CONNECTION_LOSS_TOLERANCE = 24;

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
                $scope.RainRateLast = (!jsonData['RainRateLast']) ? "0.0":jsonData['RainRateLast'];               
                $scope.RainRateHi = (!jsonData['RainRateHi']) ? "0.0":jsonData['RainRateHi'];                 
                $scope.RainfallLast_15_min = (!jsonData['RainfallLast_15_min']) ? "0.0":jsonData['RainfallLast_15_min'];        
                $scope.RainRateHiLast_15_min = (!jsonData['RainRateHiLast_15_min']) ? "0.0":jsonData['RainRateHiLast_15_min'];      
                $scope.RainfallLast_60_min = (!jsonData['RainfallLast_60_min']) ? "0.0":jsonData['RainfallLast_60_min'];        
                $scope.RainfallLast_24_hr = (!jsonData['RainfallLast_24_hr']) ? "0.0":jsonData['RainfallLast_24_hr'];         
                $scope.RainStorm = (!jsonData['RainStorm']) ? "0.0":jsonData['RainStorm'];                  
                $scope.RainStormStartAt = (!jsonData['RainStormStartAt']) ? "---":jsonData['RainStormStartAt'];           
                $scope.SolarRad = (!jsonData['SolarRad']) ? "---":jsonData['SolarRad'];                   
                $scope.UvIndex = (!jsonData['UvIndex']) ? "---":jsonData['UvIndex'];                    
                $scope.RainfallDaily = (!jsonData['RainfallDaily']) ? "0.0":jsonData['RainfallDaily'];             
                $scope.RainfallMonthly = (!jsonData['RainfallMonthly']) ? "0.0":jsonData['RainfallMonthly'];            
                $scope.RainfallYear = (!jsonData['RainfallYear']) ? "0.0":jsonData['RainfallYear'];               
                $scope.RainStormLast = (!jsonData['RainStormLast']) ? "0.0":jsonData['RainStormLast'];              
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
            $scope.WindDirLast = (parseFloat($scope.WindPpeedLast)>0) ? jsonData['WindDirLast']:"CALM";         

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
        $scope.bomHeadingTitle = "---";
        
        $scope.errorOutput = "No Current Errors";

        $scope.AppTemp = "---";
        $scope.bomAppTemp = "---";
        
        $scope.Temp = "---";                       
        $scope.bomTemp = "---";                       

        $scope.Hum = "---";                        
        $scope.bomHum = "---";                        
        
        $scope.DewPoint = "---";                   
        $scope.bomDewPoint = "---";                   
        
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
        $scope.bomWindSpeedAvgLast_10_min = "---";    
        
        $scope.WindDirScalarAvgLast_10_min = "---";
        $scope.bomWindDirScalarAvgLast_10_min = "---";
        
        $scope.WindSpeedHiLast_10_min = "---";     
        $scope.bomWindSpeedHiLast_10_min = "---";     
        
        $scope.WindDirAtHiSpeedLast_10_min = "---"; 
        $scope.RainRateLast = "0.0";               
        $scope.RainRateHi = "0.0";                 
        $scope.RainfallLast_15_min = "0.0";        
        $scope.RainRateHiLast_15_min = "0.0";      
        $scope.RainfallLast_60_min = "0.0";        
        $scope.RainfallLast_24_hr = "0.0";         
        $scope.RainStorm = "0.0";                  
        $scope.RainStormStartAt = "---";           
        $scope.SolarRad = "---";                   
        $scope.UvIndex = "---";                    
        
        $scope.RainfallDaily = "0.0";             
        $scope.bomRainfallDaily = "---";             
        
        $scope.RainfallMonthly = "0.0";            
        $scope.RainfallYear = "0.0";               
        $scope.RainStormLast = "0.0";              
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
        $scope.bomBarSeaLevel = "---";                

    }

    /**
     * Gets data from the Australian Bureau of Meteorology and loads it into the comparison blocks 
     * on the page. 
     */
    $scope.getBomData = function() {

        $http.get("getApiResponse.php?url="+encodeURI(BOM_COMPARISON_STATION_URL)).then(function(response) {
            
            $scope.bomData = response.data;

            // Load the bom data into scope variables
            $scope.loadBomData($scope.bomData);
            
            // Get the timestamp set
            $scope.bomTimestamp = $scope.bomData.observations.data[0].aifstime_utc;

            // If the view is in comparison mode, the data may be out of date, therefore refresh the data
            if ($scope.isComparing) {
                $scope.displayBomData();
            }

        },
        function(response) {});
        
    }

    /**
         * Takes the JSON string from the Bureau of Meteorology and extracts the data from it and places
         * it into bom scope variables. 
         * 
         * @param {String} jsonData 
         */
    $scope.loadBomData = function(jsonData) {
            
        try {

            $scope.bomHeadingTitle = jsonData.observations.data[0].name + 
                " at " + jsonData.observations.data[0].local_date_time + " (bom.gov.au)";
            $scope.bomAppTemp = jsonData.observations.data[0].apparent_t;
            $scope.bomTemp = jsonData.observations.data[0].air_temp;
            $scope.bomHum = jsonData.observations.data[0].rel_hum;
            $scope.bomWindSpeedAvgLast_10_min = jsonData.observations.data[0].wind_spd_kmh;
            $scope.bomWindSpeedHiLast_10_min = jsonData.observations.data[0].gust_kmh;
            $scope.bomWindDirScalarAvgLast_10_min = jsonData.observations.data[0].wind_dir;
            $scope.bomBarSeaLevel = jsonData.observations.data[0].press_msl;
            $scope.bomRainfallDaily = jsonData.observations.data[0].rain_trace;
            $scope.bomDewPoint = jsonData.observations.data[0].dewpt;
            $scope.bomTimestamp = jsonData.observations.data[0].aifstime_utc;
        
        } catch (e) {return false;}

        return true;

    }

    /**
     * Takes the bom scope data and sets the scope variables which binds to the
     * weather display blocks on the page
     * 
     */
    $scope.displayBomData = function() {
        
        // Clear all of the view elements for bom data.
        $scope.initialiseView();

        try {

            $scope.HeadingTitle = $scope.bomHeadingTitle;
            $scope.AppTemp = $scope.bomAppTemp;
            $scope.Temp = $scope.bomTemp;
            $scope.Hum = $scope.bomHum;
            $scope.WindSpeedAvgLast_10_min = $scope.bomWindSpeedAvgLast_10_min;
            $scope.WindSpeedHiLast_10_min = $scope.bomWindSpeedHiLast_10_min;
            $scope.WindDirScalarAvgLast_10_min = $scope.bomWindDirScalarAvgLast_10_min;
            $scope.BarSeaLevel = $scope.bomBarSeaLevel;
            $scope.RainfallDaily = $scope.bomRainfallDaily;
            $scope.DewPoint = $scope.bomDewPoint;
        
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

        // Show the modal
        // Check error state, because we don't want this modal 
        // to keep popping up. 
        if (!$scope.isErrorState) $("#errorModal").modal();
        
        // This will display the error modal
        $scope.isErrorState = true;
        
    }
    /**
     * Clears the error message bar and removes is from display.
     */
    $scope.clearErrors = function() {

        $scope.errorOutput="No Errors";

         // Remove the modal
         $("#errorModal").modal('hide');

        $scope.isErrorState = false;

    }

    /**
     * Changes the background colour of the performance display bar to something 
     * that indicates an issue.
     */
    $scope.displayPerformanceIssueState = function() {
        $scope.performanceBackgroundColour = {"background-color":"rgba(255, 60, 0, 0.58)","color":"white","transition":"background-color 10s, color 1s"};
        $scope.latencyStyles = {"color":"#ffef00","transition":"color 1s"};
    }

    /**
     * Sets the background colour of hte performance display bar back to normal
     */
    $scope.removePerformanceIssueState = function() {
        $scope.performanceBackgroundColour = {"background-color":"#ffffff40","color":"midnightblue","transition":"background-color 10s, color 1s"};
        $scope.latencyStyles = {"color":"midnightblue","transition":"color 1s"};
    }

    /**
     * This function returns a string containing the date and time of the last packet receive time
     * but will only return the time as a string if the last packet receive time falls on the same 
     * date. If the last packet send receive time is on a different date, then it will return a string 
     * with both the date and time. 
     * 
     * @param {string}  DateString  A date and time string in yyyy-mm-dd hh:mm:ss format.
     * @return String   Just time if current date, otherwise date and time if different date
     */
    $scope.getLastPacketDateTime = function(DateString) {

        try {

            // Get the date portion
            const dateTimeParts = DateString.split(" ");
        
            // Check that there are 2 elements for date and time
            if (dateTimeParts.length > 1) {
                // get date part
                var datePart = dateTimeParts[0];
                // get time part
                var timePart = dateTimeParts[1];

                // get current date and time
                let lCurrentDate = new Date();
                // set time portion to zero for current date
                lCurrentDate.setHours(0,0,0,0);
                
                // Get the last packet rec time as date object
                let lRecDateTime = new Date(datePart)
                // set the time portion to zero as well
                lRecDateTime.setHours(0,0,0,0);

                // Check if the rec date time is same as current date
                if (lCurrentDate.toUTCString()==lRecDateTime.toUTCString()) {
                    return timePart; 
                } else {
                    return DateString;
                }

            } else {
                // There was something wrong with the date string, so just return it as it is
                return DateString;
            }

        } catch (err) {
            
            // DateString not in the expected format, so just return it
            return DateString
        }

    }

    /**
     * Converts the string containing the date and time in yyyy-mm-dd hh:mm:ss format into a Date object. 
     * This function solves the problem with some browsers not supporting
     * the date and time in this format as a parameter of the constructor for the Date object. 
     * 
     * @returns A Date object   The date object representing the date and time since receiving the last packet
     */
    $scope.getLastPacketRecTimeDateObject = function(dateString) {

        try {

            // Get the date portion
            const lDateTimeParts = dateString.split(" ");
        
            // Check that there are 2 elements for date and time
            if (lDateTimeParts.length > 1) {
                // get date part
                var lDatePart = lDateTimeParts[0];
                // get time part
                var lTimePart = lDateTimeParts[1];

                // Split the date parameters
                const lDateParts = lDatePart.split("-");

                if (lDateParts.length > 2) {

                    // Split the time parameters
                    const lTimeParts = lTimePart.split(":");

                    if (lTimeParts.length > 2) {
                        
                        // Create the new date object
                        lDate = new Date(lDateParts[0], (lDateParts[1]-1), lDateParts[2], lTimeParts[0], lTimeParts[1], lTimeParts[2]);

                        // Return the created date
                        return lDate

                    }
                }

            }

        } catch (err) {}

        // There was something wrong with the date string, so just attempt the date constructor
        return new Date(dateString);

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

        // Get the amount of time since last packet was received
        let lTimeSinceLastPacket = lEndTime - $scope.getLastPacketRecTimeDateObject(JsonObject['last_packet_rec_time']);

        // Calculate possible down server count
        $ConnectionPerformance.PossibleDownSiteServerCount = (lTimeSinceLastPacket/1000)/REFRESH_INTERVAL;

        if ($ConnectionPerformance.LastRecDate==JsonObject['last_packet_rec_time']) {
            // Increment lost connection counter
            $ConnectionPerformance.PossibleDownSiteServerCount++;
            
            if ($ConnectionPerformance.PossibleDownSiteServerCount>=$ConnectionPerformance.ConnectionIssuesTolerance) {
                // Display last updated
                $scope.obstime = $scope.getLastPacketDateTime(JsonObject['last_packet_rec_time']);
                
                // Change the background colour of the performance bar                
                $scope.displayPerformanceIssueState();
            }

        } else {
            
            // Reset the connection loss counter
            $ConnectionPerformance.PossibleDownSiteServerCount = 0;
            // Display last updated
            $scope.obstime = "LIVE";
            // Add latency value for averaging
            $ConnectionPerformance.addNumber(lTotalTimeTaken);
            // clear errors
            $scope.clearErrors();
            // Restore performance bar background colours            
            $scope.removePerformanceIssueState();
        }

        if ($ConnectionPerformance.PossibleDownSiteServerCount<$ConnectionPerformance.ConnectionLossTolerance) {
            
            $scope.Latency = lTotalTimeTaken + 
                "ms (u" + JsonObject['prev_packet_latency'] + ", " + 
                " d" + $ConnectionPerformance.TimeTaken + ") Avg ("+$ConnectionPerformance.average()+"ms)";    
        
        } else {
            // Display the time as ---
            $scope.Latency = "---";  

            // Display last updated
            $scope.obstime = $scope.getLastPacketDateTime(JsonObject['last_packet_rec_time']);

            // Report error that site server is offline
            $scope.reportError("Site Server Offline", true);
            $scope.removePerformanceIssueState()
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

    // Get the bom comparison data
    $scope.getBomData();

    // Initialise the view
    $scope.initialiseView();

    // Get the weather data straight away first
    $scope.getWeatherData();

    // Start the refresh timer
    $interval($scope.getWeatherData, ($ConnectionPerformance.RefreshInterval * 1000))

});

/**
 * This is to enable the popover for the bom comparisons
 */

$(document).ready(function(){$('[data-toggle="popover"]').popover();});