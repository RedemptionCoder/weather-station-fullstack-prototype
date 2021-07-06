<?php

/**
 * Contains a lot of common functionality used for Cruxarina projects
 * Included functionalities
 * <ul>
 * <li>Error Logging</li>
 * <li>Message Logging</li>
 * </ul>
 * 
 * @author Anthony Gibbons
 */
 
class Common {

    // CONSTRUCTORS

    function __construct() {
        $this->cErrorLog = array();
        $this->cMessages = array();
        $this->cLastError = "";
        $this->cLastMessage = "";
    }

    // SETTERS AND GETTERS

    // PUBLIC METHODS

    /** 
    * Adds an error message to the class' error log
    * @param    Message    A string containing an error message
    */
    public function logError($Message) {
        
        array_push($this->cErrorLog, $Message);
        $this->cLastError = $Message;
    }

    /**
    * Adds a message to the class' message log
    * @param Message	A string containing an message
    */
    public function logMessage($Message) {
        array_push($this->cMessages, $Message);
        $this->cLastMessage = $Message;
    }

    /**
    * Gets the last error that was logged
    * @return The last error message
    */
    public function lastError() {
        return $this->cLastError;
    }

    /**
    * Returns the last message that was logged
    * @return The last message
    */
    public function lastMessage() {
        return $this->cLastMessage;
    }
    
    
    // PRIVATE METHODS

    // ATTRIBUTES

    // The list of errors that may have occurred
    private $cErrorLog;
    
    // The last error logged
    private $cLastError;
    
    // The list of messages that may have been generated
    private $cMessages;
    
    // The last message that was logged
    private $cLastMessage;

}

?>