<?php
    /**
     * This script retrieves the http response from a server attempting to avoid mechanisms that prevent
     * web scraping. It can be usefull in situations where interaction with a web api is required, especially
     * on the client side using JavaScript where normally the browser will produce CORS errors. 
     * 
     * Usage: getApiReponse.php?url=http%3A%2F%2Fwww.mysite.com
     * Parameters:
     * 
     * url: A url encoded string containing the url for the api
     * ref: optional referer url used to trick server into thinking page was refered locally
     * host: optional host header information
     * timeout: how long to wait for a response from the server. 10 Seconds is the default
     * 
     * @author  Mr. Anthony J Gibbons, Cruxarina Solutions
     */

    // Default Parameters
    // Timeout in seconds
    define("DEFAULT_TIMEOUT","10");
    // User agent
    define("USER_AGENT","Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:88.0) Gecko/20100101 Firefox/88.0");
    
    $lRef = "";
    $lHost = "";
    $lTimeout = DEFAULT_TIMEOUT;

    // Get parameters from the request
    if (array_key_exists("url",$_REQUEST)) {$lURL = $_REQUEST['url'];} else {echo "Invalid URL";}
    if (array_key_exists("ref",$_REQUEST)) {$lRef = $_REQUEST['ref'];}
    if (array_key_exists("host",$_REQUEST)) {$lHost = $_REQUEST['host'];}
    if (array_key_exists("timeout",$_REQUEST)) {$lTimeout = $_REQUEST['timeout'];}
    
    // Set up specliased header information
    $lHeader[0] = "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
    $lHeader[] = "Accept-Language: en-US,en;q=0.5";
    $lHeader[] = "Connection: keep-alive";
    if (strlen($lHost) > 0) $lHeader[] = "Host: " . $lHost;
    $lHeader[] = "Upgrade-Insecure-Requests: 1";
    
    try {    

        // Initialise curl
        $lCurl = curl_init();

        // Set standard header options
        curl_setopt($lCurl, CURLOPT_URL, $lURL);
        curl_setopt($lCurl, CURLOPT_ENCODING, "gzip, deflate");
        if (strlen($lRef) > 0) curl_setopt($lCurl, CURLOPT_REFERER, $lRef);
        curl_setopt($lCurl, CURLOPT_USERAGENT, USER_AGENT);
        curl_setopt($lCurl, CURLOPT_HTTPHEADER, $lHeader);
        curl_setopt($lCurl, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($lCurl, CURLOPT_TIMEOUT, 10);
        
        // Execute and return response
        echo curl_exec($lCurl);

        // Close Curl
        curl_close($lCurl);

    } catch (Exception $e) {
        
        // Return error
        echo $e->toString();
        
    }

?>