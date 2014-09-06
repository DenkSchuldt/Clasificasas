<?php
		
	$response = array();
	
	//si se envio o no los parametros
	if (isset($_POST['usuario'])){
		$usuario = $_POST['usuario'];
		
		require_once ("db.php");
		$obj=new DB();
		$connect_db = $obj->connect();
	
		$result = $obj->casasfavoritasporusuario ($connect_db,$usuario);
		
		while ($row = mysqli_fetch_array($result)) { 
            $casasfavoritas[] = $row; 
        } 
		
		if($result){
			$response["success"]=1;
			$response["casasfavoritas"]=$casasfavoritas;
			$response["message"]= "casas encontradas";
			echo json_encode($response);
		}
		else{
			$response["success"]=0;
			$response["casafavoritas"]="";
			$response["message"]= "ups, no hay ninguna casa con esas caracteristicas";
			echo json_encode($response);
		}
	}
	else{
		$response["success"]=0;
		$response["casafavoritas"]="";
		$response["message"]= "ups, falta algun campo";
		echo json_encode($response);
	}

?>