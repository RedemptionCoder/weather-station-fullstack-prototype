<?php require "includes/WeatherStationApp.php"; ?>

<html>
<head><title>Test</title></head>
<body>
<?php 

    $lWsa = new WeatherStationApp();


    if (!$lWsa->getObservationsFromRequest()) {
        echo "Failed. " . $lWsa->lastError();
    } else {
        echo "Success";
    }

    /*
    $lJson = file_get_contents('php://input');

    var_dump(json_decode($lJson,true));
    */
?>

</body>
</html>
