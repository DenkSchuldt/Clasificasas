<?php
		
	$response = array();

	require_once ("db.php");
	$obj=new DB();
	$connect_db = $obj->connect();

	$result = $obj-> obtenermaxymin($connect_db);
	
	while ($row = mysqli_fetch_array($result)) { 
		$maxymin[] = $row; 
	} 
	
	if($result){
		$response["success"]=1;
		$response["casaspublicada"]=$maxymin;
		$response["message"]= "maxymin";
		echo json_encode($response);
	}
	else{
		$response["success"]=0;
		$response["casaspublicada"]="";
		$response["message"]= "ups, no hay ninguna casa publicada";
		echo json_encode($response);
	}
?>