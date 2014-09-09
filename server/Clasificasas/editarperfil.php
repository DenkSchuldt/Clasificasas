<?php
		
	$response = array();
	
	//si se envio o no los parametros
	if (isset($_POST['usuario']) && isset($_POST['cont']) && isset($_POST['mobil']) && isset($_POST['nombres']) && isset($_POST['apellidos']) && isset($_POST['correo'])){
		$usuario = $_POST['usuario'];
		$cont= $_POST['cont'];
		$mobil = $_POST['mobil'];
		$nombres = $_POST['nombres'];
		$apellidos = $_POST['apellidos'];
		$correo = $_POST['correo'];
		
		require_once ("db.php");
		$obj=new DB();
		$connect_db = $obj->connect();
	
		$result = $obj->editarperfil($connect_db,$usuario, $cont, $mobil, $nombres, $apellidos,$correo);
				
		if($result){
			$response["success"]=1;
			$response["message"]= "perfil editado";
			echo json_encode($response);
		}
		else{
			$response["success"]=0;
			$response["message"]= "perfil no editado";
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