<?php
		
	$response = array();
	
	//si se envio o no los parametros
	if (isset($_POST['usuario']) && isset($_POST['cont'])){
		$usuario = $_POST['usuario'];
		$cont = $_POST['cont'];
		
		require_once ("db.php");
		$obj=new DB();
		$connect_db = $obj->connect();
	
		$result = $obj->obtenerdatosdeusuario($connect_db,$usuario, $cont);
		
		while ($row = mysqli_fetch_array($result)) { 
            $datosusuario[] = $row; 
        } 
		
		if($result){
			$response["success"]=1;
			$response["datosusuario"]=$datosusuario;
			$response["message"]= "casas encontradas";
			echo json_encode($response);
		}
		else{
			$response["success"]=0;
			$response["datosusuario"]="";
			$response["message"]= "ups, no hay ninguna casa con esas caracteristicas";
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