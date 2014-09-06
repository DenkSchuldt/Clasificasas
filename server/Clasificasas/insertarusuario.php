<?php
		
	$response = array();
	
	//si se envio o no los parametros
	if (isset($_POST['usuario']) && isset($_POST['cont']) && isset($_POST['mobil']) && isset($_POST['nombres']) && isset($_POST['apellidos']) && isset($_POST['correo'])){
		$usuario = $_POST['usuario'];
		$cont = $_POST['cont'];
		$mobil = $_POST['mobil'];
		$nombres = $_POST['nombres'];
		$apellidos = $_POST['apellidos'];
		$correo = $_POST['correo'];
		
		/*connection*/
		require_once ("db.php");
		$obj=new DB();
		$connect_db = $obj->connect();
		
		$usuarioexiste = $obj->buscarporusuario($connect_db,$usuario);
		if(!$usuarioexiste){
			//result
			$result = $obj->crearusuario($connect_db, $usuario, $cont, $mobil, $nombres, $apellidos, $correo );
			
			//si existe el usuario o no
			if($result==""){
				$response["success"]=1;
				$response["usuario"]=$usuario;
				$response["message"]= "usuario creado";
				echo json_encode($response);
			}
			else{
				$response["success"]=0;
				$response["usuario"]="";
				$response["message"]= $result;
				echo json_encode($response);
			}
		}
		else{
			$response["success"]=0;
			$response["usuario"]="";
			$response["message"]= "ups, el usuario ya existe";
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