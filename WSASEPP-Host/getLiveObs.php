<?php require "includes/WeatherStationApp.php"; 

    $lWsa = new WeatherStationApp();

    echo $lWsa->getObservationsAsJSON();
    
?>