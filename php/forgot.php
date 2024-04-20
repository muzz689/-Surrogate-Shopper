<?php 
$username = "s2436109";
$password = "s2436109";
$database = "d2436109";
$link = mysqli_connect("127.0.0.1", $username, $password, $database);



$Email = $_REQUEST["Email"];
$NewPassword = $_REQUEST["NewPassword"];

	$sql_query = " select * from Customer where Email like '$Email' ";
	$sql_query2 = " select * from Volunteer where Email like '$Email' ";



	//This executes the query and store the output in $result 
	$result = mysqli_query($link,$sql_query);
	$result2 = mysqli_query($link,$sql_query2);
	//if condition checks whether the credentials exist in the database;
	if(mysqli_num_rows($result) >0){
		$sql_updatepass = "Update Customer set Password='$NewPassword' where Email like '$Email' ";
		
	mysqli_query($link,$sql_updatepass);

	$status ="Password updated for Customer Success";
	echo json_encode(array('status'=>$status));


	}

	else if(mysqli_num_rows($result2) > 0){
		$sql_updatepass = "Update Volunteer set Password='$NewPassword' where Email like '$Email' ";
		mysqli_query($link,$sql_updatepass);
		$status = "Password updated for Volunteer Success";

		echo json_encode(array('status'=>$status));
		
	}
	else{
		$status ="ok";
		$result_code = 0;
		echo json_encode(array('status'=>$status,'result'=>$result_code));
		}

?>
