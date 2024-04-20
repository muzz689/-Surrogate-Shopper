<?php
$username = "s2436109";
$password = "s2436109";
$database = "d2436109";

$VolunteerID = $_REQUEST["VolunteerID"];
$RequestID = $_REQUEST["RequestID"];

$link = mysqli_connect("127.0.0.1", $username, $password, $database);
$sql_query = "update Request set VolunteerID='$VolunteerID' where RequestID like '$RequestID';";
$result = mysqli_query($link,$sql_query);

$status = "Success";
echo json_encode(array('status'=>$status));

?>
