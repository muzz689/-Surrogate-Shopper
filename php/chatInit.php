<?php
$username = "s2436109";
$password = "s2436109";
$database = "d2436109";
$CustID = $_REQUEST['CustomerID'];
$VolID = $_REQUEST['VolunteerID'];

$link = mysqli_connect("127.0.0.1", $username, $password, $database);
$query = "select * from Message where CustomerID=$CustID and VolunteerID=$VolID;";

$res = mysqli_query($link, $query);
$output = array();

if(mysqli_num_rows($res) > 0){
    $output = array();
    while($row = $res->fetch_assoc()){
    $output[] = $row;
    }
}else{
    $status = array('Status'=>"No current messages!");
    $today = array('Date'=>date("Y-m-d H:i:s"));

    array_push($output,$status);
    array_push($output,$today);
}

echo json_encode($output);
?>
