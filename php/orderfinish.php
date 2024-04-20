<?php
$username = "s2436109";
$password = "s2436109";
$database = "d2436109";

$link = mysqli_connect("127.0.0.1", $username, $password, $database);

$RequestID = $_REQUEST['RequestID'];


$sql_query = "delete from Item where RequestID like '$RequestID' ;";
$sql_query2 = "delete from Request where RequestID like '$RequestID' ;";

mysqli_query($link,$sql_query);
mysqli_query($link,$sql_query2);

$status = "Successfully Deleted";

echo json_encode(array('status'=>$status));


?>
