<?php
		
	$response = array();
	
	//si se envio o no los parametros
	if (isset($_POST['idcasapublicada'])){
		$idcasapublicada = $_POST['idcasapublicada'];
		
		require_once ("db.php");
		$obj=new DB();
		$connect_db = $obj->connect();
	
		$result = $obj->obtenerdatosdecasa($connect_db,$idcasapublicada);
		
		while ($row = mysqli_fetch_array($result)) { 
            $casaspublicada[] = $row; 
        } 
		
		if($result){
			$response["success"]=1;
			$response["casaspublicada"]=$casaspublicada;
			$response["message"]= "casas encontradas";
			echo json_encode($response);
		}
		else{
			$response["success"]=0;
			$response["casaspublicada"]="";
			$response["message"]= "ups, no hay ninguna casa publicada por el usuario";
			echo json_encode($response);
		}
	}
	else{
		$response["success"]=0;
		$response["casaspublicada"]="";
		$response["message"]= "ups, falta algun campo";
		echo json_encode($response);
	}

?>