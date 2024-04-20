<?php
$username = "s2436109";
$password = "s2436109";
$database = "d2436109";

$link = mysqli_connect("127.0.0.1", $username, $password, $database);

$CustID = $_REQUEST['CustomerID'];
$VolunteerID = $_REQUEST['VolunteerID'];
$raw = $_REQUEST['Items'];
$Owner = $_REQUEST['Owner'];

$sg = json_decode($raw);
$msg = $sg->Content;

// We want to create a new Request
$create_req = "insert into Message(VolunteerID,CustomerID,Content,Owner) Values($VolunteerID,$CustID,'$msg','$Owner');";
$res = mysqli_query($link, $create_req);
if($res){
    $id = mysqli_insert_id($link);
    echo json_encode(array('status'=>"Added Successfully!",'MessageID'=>$id));
}else{
echo "Failed to add to database!";
}
mysqli_close($link);
?>
