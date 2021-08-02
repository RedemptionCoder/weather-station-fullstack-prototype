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

/**
 * This object contains all of the performance related information of the weather station app 
 * data retrieval.
 * @param {number} RefreshInterval  The frequency to check for new weather observation data
 * @param {number} LatAvgPeriod The period of time over which to calculate the average latency
 * @param {number} ConnectionLossTolerance The amount of times last updated time is allowed to be the same for offline status
 * @param {number} ConnectionIssuesTolerance The amount of times last updated time is allowed to be the same with connection issues
 */
function ConnectionPerformance(RefreshInterval=REFRESH_INTERVAL,
    LatAvgPeriod=LAT_AVERAGING_PERIOD,
    ConnectionLossTolerance=CONNECTION_LOSS_TOLERANCE,
    ConnectionIssuesTolerance=CONNECTION_ISSUES_TOLERANCE) {

    this.RefreshInterval=RefreshInterval;
    this.LatAvgPeriod=LatAvgPeriod;
    this.ConnectionLossTolerance=ConnectionLossTolerance;
    this.ConnectionIssuesTolerance=ConnectionIssuesTolerance;

    // The start time before a request
    this.StartTime = new Date();

    // The last received date for detecting of site server is offline
    this.LastRecDate = "";

    // The time taken to process request in milliseconds
    this.TimeTaken = 0;

    // This gets incremented if the last update time hasn't changed
    this.PossibleDownSiteServerCount = 0;

    // Contains the list of latencies to be averaged
    this.LatencyAverage = new Averages(LatAvgPeriod);

}

/**
 * Provides a means to calculate the average of a list of numbers. If a new number is added and the 
 * length is specified, then the oldest number or element will be removed before adding the new one. 
 * @param {} length The sample size or amount of numbers to calculate. A length of 0 means there is 
 *                  limiit on the sample size. 
 */
function Averages(length=0) {
    // The array containing the numbers
    this.avgArray = new Array();
    // The length of the array. 
    this.length = length; 

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
}

/**
 * Starts the whole client side of the application
 */
function startWeatherStationApp() {

    let PerformanceData = new ConnectionPerformance();

    // Get the weather data straight away first
    getWeatherData(PerformanceData);

    // Get the bom comparison data
    getBomData();

    // Start the refresh timer
    setInterval(getWeatherData, (PerformanceData.RefreshInterval * 1000), PerformanceData);

}

/**
 * Gets live the weather data from the WSASEPP server
 */
function getWeatherData(performanceData) {
        
    // use Get method as this enables for bookmarking etc.
    // there is no sensitive data here. 
    
    // Create the AJAX request object
    let lWeatherRequest = new XMLHttpRequest();

    lWeatherRequest.performanceData = performanceData;

    // Tell the request object what to do
    lWeatherRequest.onreadystatechange = function() {
               
        if (this.readyState == 4 && this.status == 200) {
            // Display the data returned by the server
            displayWeatherData(this.responseText, this.performanceData);
        }
    }

    // Record the start time
    lWeatherRequest.performanceData.StartTime = new Date();

    // Open the request object
    lWeatherRequest.open("GET", "getLiveObs.php?transmitterID=1&temp=1&speed=3&dir=8&rain=14&bar=12", true);

    // Send the request
    lWeatherRequest.send();
    
}

/**
 * Takes the data from the json string and sets the values for the observations
 * on the pagel
 * @param {*} jsonText 
 */
function displayWeatherData(jsonText, performanceData) {

    // Get the JSON object
    lJsonObject = JSON.parse(jsonText);

    // Go through each value in the JSON object, and if it is a key, 
    // get the value for the key and set the inner HTML of the document
    // element with the same id as the key with the value of the key.
           
    try {
        // If the key is a key, assign the value to the associated
        // HTML element. 

        document.getElementById('AppTemp').innerHTML = lJsonObject['AppTemp'];
        document.getElementById('Temp').innerHTML = lJsonObject['Temp'];                       
        document.getElementById('Hum').innerHTML = lJsonObject['Hum'];                        
        document.getElementById('DewPoint').innerHTML = lJsonObject['DewPoint'];                   
        document.getElementById('WetBulb').innerHTML = lJsonObject['WetBulb'];                    
        document.getElementById('HeatIndex').innerHTML = lJsonObject['HeatIndex'];                  
        document.getElementById('WindChill').innerHTML = lJsonObject['WindChill'];                  
        document.getElementById('ThwIndex').innerHTML = lJsonObject['ThwIndex'];                   
        document.getElementById('ThswIndex').innerHTML = lJsonObject['ThswIndex'];                  
        document.getElementById('WindPpeedLast').innerHTML = lJsonObject['WindPpeedLast'];              
        document.getElementById('WindDirLast').innerHTML = lJsonObject['WindDirLast'];                
        document.getElementById('WindSpeedAvgLast_2_min').innerHTML = lJsonObject['WindSpeedAvgLast_2_min'];     
        document.getElementById('WindDirScalarAvg_last_2_min').innerHTML = lJsonObject['WindDirScalarAvg_last_2_min'];
        document.getElementById('WindSpeedHi_last_2_min').innerHTML = lJsonObject['WindSpeedHi_last_2_min'];     
        document.getElementById('WindDirAtHiSpeedLast_2_min').innerHTML = lJsonObject['WindDirAtHiSpeedLast_2_min']; 
        document.getElementById('WindSpeedAvgLast_10_min').innerHTML = lJsonObject['WindSpeedAvgLast_10_min'];    
        document.getElementById('WindDirScalarAvgLast_10_min').innerHTML = lJsonObject['WindDirScalarAvgLast_10_min'];
        document.getElementById('WindSpeedHiLast_10_min').innerHTML = lJsonObject['WindSpeedHiLast_10_min'];     
        document.getElementById('WindDirAtHiSpeedLast_10_min').innerHTML = lJsonObject['WindDirAtHiSpeedLast_10_min']; 
        document.getElementById('RainRateLast').innerHTML = lJsonObject['RainRateLast'];               
        document.getElementById('RainRateHi').innerHTML = lJsonObject['RainRateHi'];                 
        document.getElementById('RainfallLast_15_min').innerHTML = lJsonObject['RainfallLast_15_min'];        
        document.getElementById('RainRateHiLast_15_min').innerHTML = lJsonObject['RainRateHiLast_15_min'];      
        document.getElementById('RainfallLast_60_min').innerHTML = lJsonObject['RainfallLast_60_min'];        
        document.getElementById('RainfallLast_24_hr').innerHTML = lJsonObject['RainfallLast_24_hr'];         
        document.getElementById('RainStorm').innerHTML = lJsonObject['RainStorm'];                  
        document.getElementById('RainStormStartAt').innerHTML = lJsonObject['RainStormStartAt'];           
        document.getElementById('SolarRad').innerHTML = lJsonObject['SolarRad'];                   
        document.getElementById('UvIndex').innerHTML = lJsonObject['UvIndex'];                    
        document.getElementById('RainfallDaily').innerHTML = lJsonObject['RainfallDaily'];             
        document.getElementById('RainfallMonthly').innerHTML = lJsonObject['RainfallMonthly'];            
        document.getElementById('RainfallYear').innerHTML = lJsonObject['RainfallYear'];               
        document.getElementById('RainStormLast').innerHTML = lJsonObject['RainStormLast'];              
        document.getElementById('RainStormLastStartAt').innerHTML = lJsonObject['RainStormLastStartAt'];       
        document.getElementById('RainStormLastEndAt').innerHTML = lJsonObject['RainStormLastEndAt'];         
        document.getElementById('InAppTemp').innerHTML = lJsonObject['InAppTemp'];                 
        document.getElementById('InAppTempBottom').innerHTML = lJsonObject['InAppTemp'];                 
        document.getElementById('InTemp').innerHTML = lJsonObject['InTemp'];                     
        document.getElementById('InTempBottom').innerHTML = lJsonObject['InTemp'];                     
        document.getElementById('InHum').innerHTML = lJsonObject['InHum'];                      
        document.getElementById('InHumBottom').innerHTML = lJsonObject['InHum'];                      
        document.getElementById('InDewPoint').innerHTML = lJsonObject['InDewPoint'];                 
        document.getElementById('InHeatIndex').innerHTML = lJsonObject['InHeatIndex'];                
        document.getElementById('BarSeaLevel').innerHTML = lJsonObject['BarSeaLevel'];                

        displayServerInfo(lJsonObject, performanceData);
        
    } catch (lError) {
        // there will be some json keys with data that will not have a 
        // corresponding document element
        
        reportError(lError,true);

    }

    // add --- in any element with a null or zero length string

    lSpanElements = document.getElementsByTagName("span");

    for (i=0;i<lSpanElements.length;i++) {
        if (lSpanElements.item(i).innerHTML.length == 0) {
            lSpanElements.item(i).innerHTML = "---";
        }
    }

}

/**
 * Gets data from the Australian Bureau of Meteorology and loads it into the comparison blocks 
 * on the page. 
 */
function getBomData() {

    // Create the AJAX request object
    let lBomRequest = new XMLHttpRequest();

    // Tell the request object what to do
    lBomRequest.onreadystatechange = function() {
               
        if (this.readyState == 4 && this.status == 200) {
            // Display the data returned by the server
            displayBomData(this.responseText);
        }
    }
    
    // Open the request object
    lBomRequest.open("GET", "getApiResponse.php?url="+encodeURI(BOM_COMPARISON_STATION_URL), true);

    // Send the request
    lBomRequest.send();

}

/**
 * Takes the JSON string from the Bureau of Meteorology and extracts the data from it and places
 * it into the comparison blocks on the page. 
 * 
 * @param {String} jsonData 
 */
function displayBomData(jsonData) {
    
    try {

        let lObs = JSON.parse(jsonData);

        document.getElementById('compLocation').innerHTML = lObs.observations.data[0].name + 
            " at " + lObs.observations.data[0].local_date_time + " (bom.gov.au)";
        document.getElementById('CompAppTemp').innerHTML = lObs.observations.data[0].apparent_t;
        document.getElementById('CompTemp').innerHTML = lObs.observations.data[0].air_temp;
        document.getElementById('CompHum').innerHTML = lObs.observations.data[0].rel_hum;
        document.getElementById('CompWindSpeedAvgLast_10_min').innerHTML = lObs.observations.data[0].wind_spd_kmh;
        document.getElementById('CompWindSpeedHiLast_10_min').innerHTML = lObs.observations.data[0].gust_kmh;
        document.getElementById('CompWindDirScalarAvgLast_10_min').innerHTML = lObs.observations.data[0].wind_dir;
        document.getElementById('CompBarSeaLevel').innerHTML = lObs.observations.data[0].press_msl;
        document.getElementById('CompRainfallDaily').innerHTML = lObs.observations.data[0].rain_trace;
        document.getElementById('CompDewPoint').innerHTML = lObs.observations.data[0].dewpt;
        document.getElementById('bomTimestamp').innerHTML = lObs.observations.data[0].aifstime_utc;
    
    } catch (e) {}

}

/**
 * Creates a date object from a date and time string in the yyyymmddhhmmss format
 * @param {String} dateString Date string in yyyymmddhhmmss format
 * @param {boolean} isUTC default is false, but set to true if dateString is UTC
 * @returns Date    The date object from the date and time string
 */
function getDateFromFullString(dateString, isUTC=false) {

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
function checkBomUpdates() {

    // Get the current date and time as UTC 
    let lCurrentTime = new Date();
    
    // Get the current bom obs timestamp from the data area of the page
    let lObsTimestampStr = document.getElementById('bomTimestamp').innerHTML;

    try {

        // Create the date object for the bom timestamp
        let lObsTimestamp = getDateFromFullString(lObsTimestampStr,true);
        
        // Determine the difference in current time and observation timestamp
        let lTimeElapsed = lCurrentTime-lObsTimestamp;

        if (lTimeElapsed > (BOM_STATION_REFRESH_INTERVAL*60*1000)) {getBomData();} 

    } catch (e) {return false;}

    return true;

}

/**
 * Switches display of comparison blocks to visible from hidden
 */
function showComparisonData() {
    
    // Check for updated observation data from bom. 
    checkBomUpdates();

    // Show the comparison remote station elements
    let lCompDataElements = document.getElementsByClassName("CompObs"); 
    for (let lElement of lCompDataElements) {lElement.style.display = "inline";}

    // Hide the local station data elements
    let lOrigDataElements = document.getElementsByClassName("CompOrig"); 
    for (let lElement of lOrigDataElements) {lElement.style.display = "none";}

    // Remove the data from all the other observation blocks, to avoid confusion
    let lLeftOverBlocks = document.getElementsByClassName("obsblock");
    
}

/**
 * Switches display of comparison blocks to visible from hidden for a second
 */
function showComparisonDataBrief() {
    
    // Show the comparison data
    showComparisonData();

    // Show the comparison data for a period of time, then hide it
    setTimeout(hideComparisonData, 1000);

}

/**
 * Hides the comparison data again.
 */
function hideComparisonData() {

    // Hide the comparison remote station elements
    lCompDataElements = document.getElementsByClassName("CompObs"); 
    for (let lElement of lCompDataElements) {lElement.style.display = "none";}

    // Show the local station data elements
    lOrigDataElements = document.getElementsByClassName("CompOrig"); 
    for (let lElement of lOrigDataElements) {lElement.style.display = "inline";}

}

/**
 * Displays an error message below the page title in a red bar
 * @param {*} message   The error message to show
 */
function reportError(message, replace=false, warning=false) {

    if (replace) {
        document.getElementById('errorOutput').innerHTML = message;
    } else {
        document.getElementById('errorOutput').innerHTML+="<br>"+message;
    }
    document.getElementById('errorbar').style.display = "inherit";

    // Change to a different colour if warning
    document.getElementById('errorbar').style.backgroundColor = (warning)?"#625454":"#c60000";

}

/**
 * Clears the error message bar and removes is from display.
 */
function clearErrors() {

    document.getElementById('errorOutput').innerHTML="&lt;&lt;No Errors&gt;&gt;";
    document.getElementById('errorbar').style.display = "none";

}

/**
 * Displays server status and latency info such as last updated record and time taken from
 * collecting the data from the weahter station to displaying it on this page. 
 * @param {*} JsonObject 
 */
function displayServerInfo(JsonObject, performanceData) {

    // Record the completed time
    let lEndTime = new Date();

    // calculate the time taken for the request
    let lTimeTaken = lEndTime - performanceData.StartTime;

    // Calculate the total time taken for the previous packet
    let lTotalTimeTaken = performanceData.TimeTaken + JsonObject['prev_packet_latency'];

    // Add latency value for averaging
    performanceData.LatencyAverage.addNumber(lTotalTimeTaken);

    if (performanceData.LastRecDate==lJsonObject['last_packet_rec_time']) {
        // Increment lost connection counter
        performanceData.PossibleDownSiteServerCount++;
        
        if (performanceData.PossibleDownSiteServerCount>=performanceData.ConnectionIssuesTolerance) {
            // Display last updated
            document.getElementById('obsTime').innerHTML = JsonObject['last_packet_rec_time'];
            // Rerpot connectivity issues
            reportError("Server Bandwidth/Performance Issues", true, true);
        }

    } else {
        // Reset the connection loss counter
        performanceData.PossibleDownSiteServerCount = 0;
        // Display last updated
        document.getElementById('obsTime').innerHTML = "LIVE";
        // clear errors
        clearErrors();
    }

    if (performanceData.PossibleDownSiteServerCount<performanceData.ConnectionLossTolerance) {
        
        if (performanceData.PossibleDownSiteServerCount>=performanceData.ConnectionIssuesTolerance) {
            // Display the time as ---
            document.getElementById('Latency').innerHTML = "---";  
        } else {
            // Display the time taken
            document.getElementById('Latency').innerHTML = lTotalTimeTaken + 
                "ms (u" + JsonObject['prev_packet_latency'] + ", " + 
                " d" + performanceData.TimeTaken + ") Avg ("+performanceData.LatencyAverage.average()+"ms)";    
        }
    } else {
        // Display the time as ---
        document.getElementById('Latency').innerHTML = "---";  
        // Report error that site server is offline
        reportError("Site Server Offline", true);
    }


    // Update the next previous time taken for this request
    performanceData.TimeTaken = lTimeTaken;

    // Get the last packet receive time date 
    performanceData.LastRecDate = lJsonObject['last_packet_rec_time'];

}