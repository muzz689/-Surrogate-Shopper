

<?php
$username = "s2436109";
$password = "s2436109";
$database = "d2436109";

$link = mysqli_connect("127.0.0.1", $username, $password, $database);

$VolunteerID = $_REQUEST['VolunteerID'];
$sql_query = "select * from Request";;

$result = mysqli_query($link,$sql_query);
if(mysqli_num_rows($result)>0){



        $sql_query = "select * from Request where VolunteerID like '$VolunteerID';";
        $result = mysqli_query($link,$sql_query);
        if(mysqli_num_rows($result)>0){

                

                $output = array();
                while($row=$result->fetch_assoc()){
                        $ReqID = $row["RequestID"];
                        $CustID = $row["CustomerID"];
                }

                //Now that i have the ReqID i do sql query to get the items
                $sql_query= "select * from Item where RequestID like $ReqID";
                
                $result = mysqli_query($link,$sql_query);


                $output = array();
                while ($row=$result->fetch_assoc()){
                                $output[]=$row;
                }
                array_push($output,array('CustID'=>$CustID));
                                echo json_encode($output);



        }
        else{

        $sql_query = "select Customer.CustomerID,Request.RequestID,Fname,Sname from Customer,Request where Customer.CustomerID=Request.CustomerID and Request.VolunteerID IS NULL;";
        $result = mysqli_query($link,$sql_query);
                $output = array();
                while ($row=$result->fetch_assoc())
                {
                        $output[]=$row;
                }
                echo json_encode($output);
        }
}
else{
        $status = "No orders";
        echo json_encode(array('status'=>$status));
}

        ?>






