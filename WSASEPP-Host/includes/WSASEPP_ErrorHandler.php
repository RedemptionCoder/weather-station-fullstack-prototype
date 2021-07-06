<?php

/**
 * The Weather Station App would like to deal with errors traditionally with try catch blocks and 
 * internal app logging. This function enables this to happen. 
 * @param   errno   The error level
 * @param   errstr  The error message
 * @param   errfile The file in which the error occured
 * @param   errline The line on which the error occured
 */
function wsaErrorHandler($errno, $errstr, $errfile, $errline) {
    throw new ErrorException($errstr, 0, $errno, $errfile, $errline);
}


// Set the error handler
set_error_handler('wsaErrorHandler');

?>