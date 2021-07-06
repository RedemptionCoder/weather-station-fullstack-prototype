<?php

/**
 * Contains all of the constants used through the WSASEP Project.
 * 
 * @author Anthony Gibbons
 */

 // Constants used for formatting output of observation data
define("OUTPUT_STRING_ESSENTIAL",18);
define("OUTPUT_STRING_WIND",19);
define("OUTPUT_STRING_RAIN",20);
define("OUTPUT_STRING_ALL",21);
define("OUTPUT_STRING_DATAFILE",22);

// Constants used for so called null values from the weather station
// as 0 is a legitimate number from a weather data context

// define("FLOAT_ERROR", -9999.9);
// define("INT_ERROR", -9999);
// define("LONG_ERROR", -9999);
// define("NO_VALUE", -999);
define("NO_VALUE_TO_STRING", "---");

// Database definition information

define("DATABASE_NAME","WStationApp");
define("TABLE_NAME_OBSERVATIONS","Observations");
define("TABLE_NAME_OBSERVATIONS_ARCHIVE","ObservationsArchive");

// MYSQL connection info
define("DATABASE_SERVER","localhost");
define("DATABASE_USERNAME","dbuser");
define("DATABASE_PASSWORD","password");

// MYSQL related constants
define("MYSQL_DATETIME_FORMAT","Y-m-d H:i:s");
define("MYSQL_DATETIME_FORMAT_NULL_VALUE","0000-00-00 00:00:00");

// Unit precision settings
define("UNIT_PREC_TEMP",1);
define("UNIT_PREC_WINDSPEED",1);
define("UNIT_PREC_BAR",1);
define("UNIT_PREC_RAIN",1);
define("UNIT_PREC_HUM",1);

?>