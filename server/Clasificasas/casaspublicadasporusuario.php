<?php
		
	$response = array();
	
	//si se envio o no los parametros
	if (isset($_POST['usuario'])){
		$usuario = $_POST['usuario'];
		
		require_once ("db.php");
		$obj=new DB();
		$connect_db = $obj->connect();
	
		$result = $obj->obtenercasaspublicadasporsuario($connect_db,$usuario);
		
		while ($row = mysqli_fetch_array($result)) { 
            $casaspublicadasporusuario[] = $row; 
        } 
		
		if($result){
			$response["success"]=1;
			$response["casaspublicadasporusuario"]=$casaspublicadasporusuario;
			$response["message"]= "casas encontradas";
			echo json_encode($response);
		}
		else{
			$response["success"]=0;
			$response["casaspublicadasporusuario"]="";
			$response["message"]= "ups, no hay ninguna casa publicada por el usuario";
			echo json_encode($response);
		}
	}
	else{
		$response["success"]=0;
		$response["casaspublicadasporusuario"]="";
		$response["message"]= "ups, falta algun campo";
		echo json_encode($response);
	}

?>