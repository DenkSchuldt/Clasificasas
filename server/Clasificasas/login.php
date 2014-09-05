<?php
		
	$response = array();
	
	//si se envio o no los parametros
	if (isset($_POST['usuario']) && isset($_POST['cont'])){
		$usuario = $_POST['usuario'];
		$cont = $_POST['cont'];
		
		/*connection*/
		require_once ("db.php");
		$obj=new DB();
		$connect_db = $obj->connect();
	
		//result
		$result = $obj->login($connect_db,$usuario,$cont);
		
		//si existe el usuario o no
		if($result){
			$response["success"]=1;
			$response["usuario"]=$usuario;
			$response["message"]= "usuario registrado";
			echo json_encode($response);
		}
		else{
			$response["success"]=0;
			$response["usuario"]="";
			$response["message"]= "ups, usuario no registrado";
			echo json_encode($response);
		}
	}
	else{
		$response["success"]=0;
		$response["usuario"]="";
		$response["message"]= "ups, falta algun campo";
		echo json_encode($response);
	}

?>

