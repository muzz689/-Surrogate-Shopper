<?php

$username = "s2436109";
$password = "s2436109";
$database = "d2436109";

$link = mysqli_connect("127.0.0.1", $username, $password, $database);

$CustomerID = $_REQUEST['CustomerID'];


$sql_query = "select * from Request where CustomerID like '$CustomerID' ;";
$result = mysqli_query($link,$sql_query);

//Check if its volunteer
if(mysqli_num_rows($result)>0){
        
        while($row=$result->fetch_assoc()){
                $ReqID = $row["RequestID"];
                $VolID = $row["VolunteerID"];
        }
        $sql_query= "select * from Item where RequestID like $ReqID";
        $result = mysqli_query($link,$sql_query);
        $output = array();

        while ($row=$result->fetch_assoc()){
                $output[]=$row;
        }
                array_push($output,array('VolID'=>$VolID));
                echo json_encode($output);

 
}
else{
        $status = "No orders bruh";
        echo json_encode(array('status'=>$status));
}



?>
