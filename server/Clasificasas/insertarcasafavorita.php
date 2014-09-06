<?php
		
	$response = array();
	
	//si se envio o no los parametros
	if (isset($_POST['usuario']) && isset($_POST['idcasapublicada'])){
		
	$usuario = $_POST['usuario'];
	$idcasapublicada= $_POST['idcasapublicada'];
	/*connection*/
	require_once ("db.php");
	$obj=new DB();
	$connect_db = $obj->connect();
		
	$result = $obj->ingresarcasafavorita($connect_db,$idcasapublicada,$usuario)
									
		if($result!=-1){
			$response["success"]=1;
			$response["idcasafavorita"]=$result;
			$response["message"]= "casa publicada";
			echo json_encode($response);
		}
		else{
			$response["success"]=0;
			$response["idcasafavorita"]="";
			$response["message"]= "ups, casa no creada";
			echo json_encode($response);
		}
	}
	else{
		$response["success"]=0;
		$response["idcasafavorita"]="";
		$response["message"]= "ups, usuario no valido";
		echo json_encode($response);
	}
	
	

?>