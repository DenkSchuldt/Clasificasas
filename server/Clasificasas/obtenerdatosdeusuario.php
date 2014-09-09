<?php
		
	$response = array();
	
	//si se envio o no los parametros
	if (isset($_POST['usuario']) ){
		$usuario = $_POST['usuario'];
		
		require_once ("db.php");
		$obj=new DB();
		$connect_db = $obj->connect();
	
		$result = $obj->obtenerdatosdeusuario($connect_db,$usuario);
		
		while ($row = mysqli_fetch_array($result)) { 
            $datosusuario[] = $row; 
        } 
		
		if($result){
			$response["success"]=1;
			$response["datosusuario"]=$datosusuario;
			$response["message"]= "usuario encontrado";
			echo json_encode($response);
		}
		else{
			$response["success"]=0;
			$response["datosusuario"]="";
			$response["message"]= "ups, no hay nadie con ese usuario";
			echo json_encode($response);
		}
	}
	else{
		$response["success"]=0;
		$response["datosusuario"]="";
		$response["message"]= "ups, falta algun campo";
		echo json_encode($response);
	}

?>