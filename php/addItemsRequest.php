<?php
$username = "s2436109";
$password = "s2436109";
$database = "d2436109";

$link = mysqli_connect("127.0.0.1", $username, $password, $database);
$CustID = $_REQUEST['CustomerID'];
$raw = $_REQUEST['Items'];
$raw2 = $_REQUEST['Instruction'];
//$json = file_get_contents('php://input');

$data_ins = json_decode($raw2,false);
$instruction = $data_ins->Message;
$update_instruction = "update Customer set Instruction='$instruction' where CustomerID=$CustID;";
mysqli_query($link,$update_instruction);

$data = json_decode($raw);

// We want to create a new Request
$create_req = "insert into Request(VolunteerID,CustomerID) Values(null,$CustID);";
mysqli_query($link, $create_req);

$get_ReqID = "Select RequestID from Request where CustomerID like $CustID";
$res = mysqli_query($link,$get_ReqID);
while($row = $res->fetch_assoc()){
    $ReqID = $row["RequestID"];
}

for ($i=0; $i < sizeof($data); $i++) { 
    $tempObj = $data[$i];
    $Content = $tempObj->Content;
    $Quantity = $tempObj->Quantity;
    $quest = "insert into Item(RequestID,Content,Quantity) Values($ReqID,'$Content',$Quantity);";
    mysqli_query($link,$quest);
}

mysqli_close($link);
?>
