<?php

	$response = array();
    $file_path = "uploads/";
	$t=time();
	$nombredelarchivo="";
	
	$ext = pathinfo($_FILES['uploaded_file']['name'], PATHINFO_EXTENSION);
    $file_path = $file_path . basename( $t.".".$ext);
	
    if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
        $nombredelarchivo=$t.".".$ext;
		echo $nombredelarchivo;
    } else{        
		echo "";
    }
	

?>