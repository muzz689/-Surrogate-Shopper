<?php
$username = "s2436109";
$password = "s2436109";
$database = "d2436109";

$link = mysqli_connect("127.0.0.1", $username, $password, $database);

$Fname = $_REQUEST['Fname'];
$Sname = $_REQUEST['Sname'];
$EmailOld = $_REQUEST['EmailOld'];
$EmailNew = $_REQUEST['EmailNew'];
$Password = $_REQUEST['Password'];

$sql_query = "select * from Volunteer where Email like '$EmailOld';"; 
$sql_query2 = "Select * from Volunteer where Email like '$EmailNew' ;";
$sql_query3 = "Select * from Customer where Email like '$EmailNew' ;";


$result2 = mysqli_query($link,$sql_query2);
$result = mysqli_query($link,$sql_query);

//Check if its volunteer
if(mysqli_num_rows($result)>0){

        
        //check for duplication of emails
        if(mysqli_num_rows($result2) == 0)
        {
                        $sql_query = "Update Volunteer set Email ='$EmailNew' , Fname = '$Fname', Sname= '$Sname', Password='$Password' where Email like '$EmailOld' ";
                        mysqli_query($link,$sql_query);

                        $result = mysqli_query($link,$sql_query2);

                        $output = array();
                        while ($row=$result->fetch_assoc()){
                        $output[]=$row;
                        }
                        echo json_encode($output);
                        
                        

        }
        //if we not updating the email
        else if($EmailOld == $EmailNew){
                $sql_query = "Update Volunteer set Email ='$EmailNew' , Fname = '$Fname', Sname= '$Sname', Password='$Password' where Email like '$EmailOld' ";
                        mysqli_query($link,$sql_query);

                        $result = mysqli_query($link,$sql_query2);

                        $output = array();
                        while ($row=$result->fetch_assoc()){
                        $output[]=$row;
                        }
                        echo json_encode($output);
        }
        else{
                $status = "Email already exists";
                $result_code = 1;
                echo json_encode(array('status'=>$status , 'result'=>$result_code));
        }
}

else{
        $result2 = mysqli_query($link,$sql_query3);
        if(mysqli_num_rows($result2) ==0){

                $sql_query = "Update Customer set Email ='$EmailNew' , Fname = '$Fname', Sname= '$Sname', Password='$Password' where Email like '$EmailOld' ";
                mysqli_query($link,$sql_query);

                $result = mysqli_query($link,$sql_query3);

                        $output = array();
                        while ($row=$result->fetch_assoc()){
                        $output[]=$row;
                        }
                        echo json_encode($output);

        }
        else if($EmailOld == $EmailNew){
                $sql_query = "Update Customer set Email ='$EmailNew' , Fname = '$Fname', Sname= '$Sname', Password='$Password' where Email like '$EmailOld' ";
                mysqli_query($link,$sql_query);

                $result = mysqli_query($link,$sql_query3);

                        $output = array();
                        while ($row=$result->fetch_assoc()){
                        $output[]=$row;
                        }
                        echo json_encode($output);
        }
        else{
                $status = "Email already exists";
                $result_code = 1;
                echo json_encode(array('status'=>$status , 'result'=>$result_code));
        }
}




?>
