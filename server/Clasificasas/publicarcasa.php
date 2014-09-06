<?php
		
	$response = array();
	
	//si se envio o no los parametros
	if (isset($_POST['usuario']) && isset($_POST['latitud']) && isset($_POST['longitud']) && isset($_POST['cuartos']) 
		&& isset($_POST['pisos']) && isset($_POST['descripcion']) && isset($_POST['costo']) && isset($_POST['fotoportada']) 
		&& isset($_POST['terreno'])&& isset($_POST['construccion'])&& isset($_POST['direccion']) && isset($_POST['ventaoalquiler']) 
		&& isset($_POST['ciudad'])&& isset($_POST['banos'])){
		
		$usuario = $_POST['usuario'];
		$latitud= $_POST['latitud'];
		$longitud = $_POST['longitud'];
		$cuartos = $_POST['cuartos'];
		$pisos = $_POST['pisos'];
		$descripcion = $_POST['descripcion'];
		$fotoportada = $_POST['fotoportada'];
		$costo = $_POST['costo'];
		$terreno = $_POST['terreno'];
		$construccion=$_POST['construccion'];
		$direccion = $_POST['direccion'];
		$ventaoalquiler = $_POST['ventaoalquiler'];
		$ciudad = $_POST['ciudad'];
		$banos = $_POST['banos'];
		/*connection*/
		require_once ("db.php");
		$obj=new DB();
		$connect_db = $obj->connect();
		$idusuario = $obj->obteneridusuario($connect_db,$usuario);
				
		//si existe el usuario o no
		if($idusuario!=""){
		
			$result = $obj->crearcasa($connect_db, $idusuario, $latitud, $longitud, $cuartos, $pisos, 
									$descripcion, $costo, $fotoportada, $terreno, $construccion, $direccion, $ventaoalquiler,$ciudad,$banos);
										
			if($result!=-1){
				$response["success"]=1;
				$response["idcasa"]=$result;
				$response["message"]= "casa publicada";
				echo json_encode($response);
			}
			else{
				$response["success"]=0;
				$response["idcasa"]="";
				$response["message"]= "ups, casa no creada";
				echo json_encode($response);
			}
		}
		else{
			$response["success"]=0;
			$response["idcasa"]="";
			$response["message"]= "ups, usuario no valido";
			echo json_encode($response);
		}
	}
	else{
		$response["success"]=0;
		$response["idcasa"]="";
		$response["message"]= "ups, falta algun campo";
		echo json_encode($response);
	}

?>