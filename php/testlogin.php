<?php 
$username = "s2436109";
$password = "s2436109";
$database = "d2436109";
$link = mysqli_connect("127.0.0.1", $username, $password, $database);


;
$Email = $_REQUEST["Email"];
$Password = $_REQUEST["Password"];

//if Volunteer


	$sql_query = " select * from Customer where Email like '$Email' and Password like '$Password' ";
	$sql_query2 = " select * from Volunteer where Email like '$Email' and Password like '$Password' ";



	//This executes the query and store the output in $result 
	$result = mysqli_query($link,$sql_query);
	$result2 = mysqli_query($link,$sql_query2);
	//if condition checks whether the credentials exist in the database;
	if(mysqli_num_rows($result) >0){
		
		

		$output = array();

			while ($row=$result->fetch_assoc()){
			$output[]=$row;
			}
			echo json_encode($output);


	}

	else if(mysqli_num_rows($result2) > 0){
		// if (mysqli_num_rows($result2) > 0){
			$output = array();

			while ($row=$result2->fetch_assoc()){
			$output[]=$row;
			}
			echo json_encode($output);
		// }
		
	}
	else{
		$status ="ok";
		$result_code = 0;
		echo json_encode(array('status'=>$status,'result'=>$result_code));
		}

	


// if Customer
// else{

// 	$sql_query = "select Email from Customer where Email like '$Email' and Password like '$Password' ; ";
// 	//This executes the query and store the output in $result 
// 	$result = mysqli_query($link,$sql_query);
// 	//if condition checks whether the credentials exist in the database;
// 	if(mysqli_num_rows($result) > 0){
		
// 		$status= "ok";
// 		$result_code= 1;
// 		echo json_encode(array('status'=>$status,'result'=>$result_code));
// 	}
// 	else{
// 		$status ="ok";
// 		$result_code = 0;
// 		echo json_encode(array('status'=>$status,'result'=>$result_code));
// 	}
// }
mysqli_close($link);
 ?>



