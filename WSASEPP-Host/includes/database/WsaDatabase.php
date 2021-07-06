<?php 

require_once 'includes/Common.php';
require_once 'ObservationsTable.php';

// These constants have database definition related stuff that are needed
// for convenience
require_once 'includes/WSASEPP_Const.php';

class WsaDatabase extends Common {

    // Constructors
    function __construct() {
        parent::__construct();
        $this->cServer = DATABASE_SERVER;
        $this->cUsername = DATABASE_USERNAME;
        $this->cPassword = DATABASE_PASSWORD;
        $this->cConnection = NULL;

        // Database definition information
        $this->cDatabaseName = DATABASE_NAME;
        $this->cObservationsTableName = TABLE_NAME_OBSERVATIONS;
        $this->cObservationsArchiveTableName = TABLE_NAME_OBSERVATIONS_ARCHIVE;

        // Turn all warnings into errors for propper error trapping
        mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
       
        // Connect to the database
        $this->connect();

    }

    function __destruct() {
        $this->disconnect();
    }

    // Setters and Getters

    /**
     * Sets the server. 
     * 
     * @param   serverAddress   This could be a FQDN or an IP address
     * @return  Nothing
     */
    public function setServer($serverAddress) {
        $this->cServer = $serverAddress;
    }
    /**
     * Gets the server
     * @return  serverAddress   FQDN or IP Address of the server
     */
    public function getServer() {
        return $this->cServer;
    }

    /**
     * Sets the username of a user with access to the database
     * @param   username    The username of a user
     * @return  Nothing
     */
    public function setUsername($username) {
        $this->cUsername= $username;
    }
    /**
     * Gets the username of a user with access to the database
     * @return  username    The username of a user
     */
    public function gerUsername() {
        return $this->cUsername;
    }

    /**
     * Sets the password of a user with access to the database
     * @param   password    The password of the user
     * @return  Nothing
     */
    public function setPassword($password) {
        $this->cPassword = $password;
    }
    /**
     * Gets the password the user with access to the database
     * @return  password    The password of the user
     */
    public function getPassword() {
        return $this->cPassword;
    }

    /**
     * Sets the name of the database
     * @param   databaseName    The name of the database to use
     * @return  Nothing
     */
    public function setDatabaseName($databaseName) {
        $this->cDatabaseName = $databaseName;
    }
    /**
     * Gets the name of the database
     * @return  DatabaseName    The name of the database
     */
    public function getDatabaseName() {
        return $this->cDatabaseName;
    }
    
    // Public Methods 


    /**
     * Saves the supplied object to the correct table in the database
     * @param   object  The object to save. 
     * @return  result  true If successfull, false if not. Refer class logs for errors.
     */
    public function save($object) {
                
        switch (get_class($object)) {
            case Observations::class:
                if ($this->saveObservations($object)) {return true;}
                break;
            
        }

        return false;
    }

    /**
     * Loads the supplied object from the correct table in the database
     * @param   object  The object to load. 
     * @return  result  true If successfull, false if not. Refer class logs for errors.
     */
    public function load(&$object, $primaryKey = "") {
        if ($this->cConnection==NULL) {
            $this->logError("Failed to connect to the database.");
            return false;
        }

        switch (get_class($object)) {
            case Observations::class:
                if ($this->loadObservations($object)) {return true;}
                break;
        }

        return false;

    }

    // Private Methods 

    // Database Connection and Creation methods

    /**
     * Attempts to connect to the server. If unsuccessful an error
     * will be logged in the object's error log.
     * @return  true If successfull, or false if failed
     */
    private function connectServer() {
        
        try {

        // Create the connection
        $this->cConnection = new mysqli($this->cServer, $this->cUsername, $this->cPassword);

        } catch (Exception $e) {
            // Connection failed
            $this->logError("Failed to connect to server: " . $e->getMessage());
            return false;
        }
            
        // Connection successfull
        return true;
    }

    /**
     * Attempts to connect to the database. If the database doesn't exists, it will try and create it. 
     * 
     * @return  true If successfull, or false if failed
     */
    public function connect() {

        try {
            
            // Attempt to connect to the database
            $this->cConnection = new mysqli($this->cServer, $this->cUsername, $this->cPassword, $this->cDatabaseName);
            
        // Check whether database connection was successfull
        } catch (Exception $e) {
            
            // If not able to connect to the server, there is no point in proceeding any further.
            if (!$this->connectServer()) {return false;}

            $this->logMessage("Database does not exist: " . $e->getMessage());
            $this->logMessage("Attempting to create the database now.");

            if (!$this->createDatabase()) {
                $this->logError("Could not create the database: " . $this->lastError());
                return false;
            }
            
            try {

                // Attempt to connect to the database again
                $this->cConnectionn = new mysqli($this->cServer, $this->cUsername, $this->cPassword, $this->cDatabaseName);
            
            } catch (Exception $e) {
                    // Check whether database connection was successfull
                    $this->logError("Database created but could still not connect: " . $e->getMessage());
                    return false;
                
            }

        }

        // Successfully connected to the database
        return true;

    }


    /**
     * Creates the database
     * @return  true    If successfull, otherwise false if it failed
     */
    private function createDatabase() {

        // Connect to the server
        if (!$this->connectServer()) {return false;}

        // Create the database
        $lSql = "CREATE DATABASE " . DATABASE_NAME;

        if ($this->cConnection->query($lSql) === TRUE) {
            $this->logMessage("Database was successfully created.");
        } else {
            $this->logError("Failed to create the database: " . $this->cConnection->error());
            return false;
        }

        return true;
        
    }

    /**
     * Gets the affected rows of the last query from the mysqli->info field.
     * @return  int     The number of rows affected by the last query 
     */
    private function checkAffectedRows() {

        // Need to check affected rows. It was found that trying to compare or obtain
        // the value of mysqli->affected_rows is unreliable, as for some reason as 
        // soon as this attribute is accessed, it is turned into a -1 before even 
        // getting a chance to do anything with the actual value. It's stupid. Instead
        // the string in mysqli->info is more consistent and doesn't exhibit this stupid
        // behavour. 

        if (strlen($this->cConnection->info)>0) {
            
            // Split the string up to get to the desired value
            $lInfoElements = explode(":", $this->cConnection->info);
            
            // Get the first integer value and remove the rest of the string 
            // using the space as a delimiter
            if (count($lInfoElements) > 1) {
                
                $lInfoElements = explode(" ", trim($lInfoElements[1]));

                if (count($lInfoElements)>0) {
                    
                    // Remove all whitespace and just retain the integer value
                    $lAffectedRows = trim($lInfoElements[0]); 

                        // The first value after the colon (:) always contains how many
                        // rows are affected
                        return $lAffectedRows;

                } else {
                    $this->logMessage("Unexpected value in mysqli->info: " . $this->cConnection->info);
                    return false; 
                }
            } else {
                $this->logMessage("Unexpected value in mysqli->info: " . $this->cConnection->info);
                return false;
            }
        } else {
            $this->logMessage("Info attribute in mysqli object was empty");
            return false;
        } 

    }

    /**
     * Runs SQL commands where resultsets are not expected, such as UPDATE, INSERT, etc.
     * @param   sqlQuery    The query to run
     * @return  bool        true, If more one or more rows were affected, false otherwise
     */
    private function runSQL($sqlQuery, $checkAffectedRows = true) {
        
        $lResult = false;

        // Connect to the server
        if (!$this->connect()) {return false;}

            try {
                // Run the query
                if ($this->cConnection->query($sqlQuery) === TRUE) {
                    
                    // Check query result

                    // Some queries doesn't have to have affected rows checked. 
                    if (!$checkAffectedRows) {
                        
                        // Query was successfull
                        $lResult = true;

                    } else {
                        
                        if ($this->checkAffectedRows() > 0) {
                            $lResult = true;
                        }
                        
                    }
                } else {
                    // Query failed, could be syntax or something else
                    throw new Exception("Query failed. " . $this->cConnection->error);
                }
            } catch (Exception $e) {
                $this->logError($e->getMessage());
            }
            
        return $lResult;
    }
    
    /**
     * Disconnects from the database
     */
    private function disconnect() {

        // Close the database connection
        $this->cConnection->close();

    }
    
    // Live Observations Table methods

    /**
     * Saves the live observations record to the database. This function will create a new record
     * if there are no existing live observations records. It will also create the live observations
     * table if it does not exist.
     * @param   observations    The live observations record to save
     * @return  Result          true If successfull, false if not. Errors logged in object.
     */
    private function saveObservations($observations) {
        
        // Make sure the server is connected and the database is selected
        if (!$this->connect()) {return false;}

        // This is the datetime format that mysql requires
        $observations->setDateTimeFormat(MYSQL_DATETIME_FORMAT);
        
        // Create the observations table object
        $ObservationsDbTable = new ObservationsTable($observations, $this->cConnection, $this->cObservationsTableName);

        // Update the live observations record
        if ($ObservationsDbTable->updateRecord()) {
            
            $this->logMessage("Table for Live Observations successfully updated");
                
        } else {
            
            // Record may not exist, so attempt to create a new record
            $this->logMessage("Inserting Live Observations as new record");
            
            if ($ObservationsDbTable->newRecord()) {
                $this->logMessage("Table for Live Observations successfully inserted as new record");
            } else {

                // The live observations table may not exist, so create one
                $this->logMessage("Attempting to create live observations table");

                if (ObservationsTable::createTable($this->cObservationsTableName, $this->cConnection)) {
                    
                    $this->logMessage("Observations table successfully created");    

                    // Talbe create was successfull, so insert the record
                    if (!$ObservationsDbTable->newRecord()) {
                      
                        // Reason unknown return false and debug
                        $this->logError("Failed to create, insert or update live observations table. " . $ObservationsDbTable->lastError());
                        return false;

                    } 

                } else {

                    // Reason unknown return false and debug
                    $this->logError("Failed to create, insert or update live observations table. " . $ObservationsDbTable->lastError());
                    return false;

                }
            }

        }

        // Successfully created the table 
        $this->logMessage("Successfully updated the observations table");
        return true;
    }

    /**
     * Gets the latest observations from the database
     * @param   Observations        The observations object to load with the data
     * @return  boolean             true If observations were successfully loaded, false If not. 
     */
    private function loadObservations(&$observations) {

        // Create the observations table object
        $lObservationsTable = new ObservationsTable($observations, $this->cConnection, $this->cObservationsTableName);

        // Load the latest observation record from the database
        if ($lObservationsTable->getRecord()) {

            // Set the observations with the retrieved data
            $observations = $lObservationsTable->getObservations();
            
        } else {
            $this->logError("Failed to load observations data from the database. " . $lObservationsTable->lastError());
            return false;
        }

        return true;

    }  
    
    // Private Attributes

    // Database server connection info
    private $cServer;
    private $cUsername;
    private $cPassword; 

    // Database definition information
    private $cDatabaseName;
    private $cObservationsTableName;
    private $cObservationsArchiveTableName;

    // The server connection object
    private $cConnection;
}

?>