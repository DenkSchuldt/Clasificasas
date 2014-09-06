<?php
		
	$response = array();
	
	//si se envio o no los parametros
	if (isset($_POST['ciudad']) && isset($_POST['costo']) && isset($_POST['usuario']) && isset($_POST['ventaoalquiler'])){
		$usuario = $_POST['usuario'];
		$ciudad = $_POST['ciudad'];
		$ventaoalquiler = $_POST['ventaoalquiler'];
		$costo = $_POST['costo'];
		
		require_once ("db.php");
		$obj=new DB();
		$connect_db = $obj->connect();
	
		$result = $obj->buscarcasa($connect_db, $usuario, $ciudad, $ventaoalquiler, $costo );
		
		while ($row = mysqli_fetch_array($result)) { 
            $casas[] = $row; 
        } 
		
		if($result){
			$response["success"]=1;
			$response["casa"]=$casas;
			$response["message"]= "casas encontradas";
			echo json_encode($response);
		}
		else{
			$response["success"]=0;
			$response["casa"]="";
			$response["message"]= "ups, no hay ninguna casa con esas caracteristicas";
			echo json_encode($response);
		}
	}
	else{
		$response["success"]=0;
		$response["casa"]="";
		$response["message"]= "ups, falta algun campo";
		echo json_encode($response);
	}

?>