<?php
$username = "s2436109";
$password = "s2436109";
$database = "d2436109";

$link = mysqli_connect("127.0.0.1", $username, $password, $database);

$CustomerID = $_REQUEST['CustomerID'];
$RequestID = $_REQUEST['RequestID'];


$sql_selectitem = "select  * from Item where RequestID like '$RequestID';";
$sql_query = "select Fname,Sname,Address,Instruction from Customer where CustomerID like '$CustomerID'; ";

$result = mysqli_query($link,$sql_selectitem);
$result2 = mysqli_query($link,$sql_query);
$output = array();
$output2 = array();

while ($row=$result->fetch_assoc()){
        $output[]=$row;
}
while ($row=$result2->fetch_assoc()){
        $output2[]=$row;
}

        echo json_encode(array('ItemArray'=>$output,'Customer'=>$output2));




