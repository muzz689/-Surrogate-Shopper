<?php
$username = "s2436109";
$password = "s2436109";
$database = "d2436109";

$User = $_REQUEST["User"];
$Fname = $_REQUEST["Fname"];
$Sname = $_REQUEST["Sname"];
$Email = $_REQUEST["Email"];
$Address = $_REQUEST["Address"];
$Password = $_REQUEST["Password"];

$link = mysqli_connect("127.0.0.1", $username, $password, $database);
//Checks if it is Volunter
if($User == 'volunteer' ){

    $sql_query = "select * from Volunteer where Email = '$Email'";
    $result = mysqli_query($link,$sql_query);
    //Checks if email is already in database
    if(mysqli_num_rows($result)>0){

        $status = "ok";
        $result_code = 0;
        echo json_encode(array('status'=>$status,'result'=>$result_code));
    }
    else{


    	$sql_query = "insert into Volunteer(Fname,Sname,Email,Address,Password) values('$Fname','$Sname','$Email','$Address','$Password')";
    	mysqli_query($link,$sql_query);


        	$status = "ok";
        	$result_code = 1;
        	echo json_encode(array('status'=>$status,'result'=>$result_code));
    	

    	
    	
    }

}
else{



$sql_query = "select * from Customer where Email = '$Email'";
    $result = mysqli_query($link,$sql_query);
    //Checks if email is already in database
    if(mysqli_num_rows($result)>0){

        $status = "ok";
        $result_code = 0;
        echo json_encode(array('status'=>$status,'result'=>$result_code));
    }
    else{


    	$sql_query = "insert into Customer(Fname,Sname,Email,Address,Password) values('$Fname','$Sname','$Email','$Address','$Password')";
    	mysqli_query($link,$sql_query);



        	$status = "ok";
        	$result_code = 1;
        	echo json_encode(array('status'=>$status,'result'=>$result_code));


}   

	



}


mysqli_close($link);
?>
