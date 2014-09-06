<?php
	class DB { 
     
		protected $db_name = 'clasificasas'; 
		protected $db_user = 'root'; 
		protected $db_pass = 'root'; 
		protected $db_host = 'localhost'; 
		 
		public function connect() { 
		 
			$connect_db = new mysqli( $this->db_host, $this->db_user, $this->db_pass, $this->db_name ); 
			 
			if ( mysqli_connect_errno() ) { 
				echo "Connection failed: %s\n", mysqli_connect_error(); 
				exit(); 
			} 
			return $connect_db; 
		} 
		
		public function login($connect_db,$username, $password ) { 
			 
			$result = mysqli_query($connect_db,"SELECT * FROM usuario WHERE usuario = '$username' AND cont = '$password' "); 
			 
			if ( mysqli_num_rows($result) == 1 ) { 
				return true; 
			} else { 
				return false; 
			} 
		}
		
		public function buscarporusuario($connect_db,$username) { 
			 
			$result = mysqli_query($connect_db,"SELECT * FROM usuario WHERE usuario = '$username' "); 
			 
			if ( mysqli_num_rows($result) == 1 ) { 
				return true; 
			} else { 
				return false; 
			} 
		}
		
		public function obteneridusuario($connect_db,$username) { 
			$line ="";
			$result = mysqli_query($connect_db,"SELECT idusuario FROM usuario WHERE usuario = '$username' "); 
			 
			if ( mysqli_num_rows($result) == 1 ) { 
				
				while($obj = $result->fetch_object()){ 
					$line=$obj->idusuario;
				}
				return $line;
			} else { 
				return $line; 
			} 
		}

		public function crearusuario($connect_db, $username, $password, $mobil, $nombres, $apellidos, $correo ) { 
			 
			$result = mysqli_query($connect_db,"INSERT INTO usuario (usuario,cont,mobil,nombres,apellidos,correo) VALUES ('$username','$password','$mobil','$nombres',
									'$apellidos','$correo')"); 
		 
			if ($result) { 
				//return true;
						return "";
			} else { 
				return mysqli_error($connect_db);
				//return false; 
			} 
		}

		public function crearcasa($connect_db, $idusuario, $latitud, $longitud, $cuartos, $pisos, $descripcion, $costo, $fotoportada, $terreno, $construccion, $direccion, 
								$ventaoalquiler,$ciudad,$banos ) { 
			$idcasapublicada = -1;
			$result = mysqli_query($connect_db,"INSERT INTO casapublicada (latitud,longitud,cuartos,pisos,descripcion,costo,fotoportada,terreno,construccion,direccion,
												ventaoalquiler,idusuario,ciudad,banos) VALUES ('$latitud','$longitud','$cuartos','$pisos','$descripcion','$costo','$fotoportada',
												'$terreno','$construccion','$direccion','$ventaoalquiler','$idusuario','$ciudad','$banos')"); 
			
		 
			if ($result) { 
				$idcasapublicada= mysqli_insert_id($connect_db);
				return $idcasapublicada; 
			} else { 
				//echo mysqli_error($connect_db);
				return $idcasapublicada; 
			} 
		}
		
		public function buscarcasa($connect_db, $usuario, $ciudad, $ventaoalquiler, $costo ) { 
			 
			$result = mysqli_query($connect_db,"SELECT costo, direccion, cuartos, banos, pisos, descripcion, idcasapublicada 
												FROM casapublicada 
												WHERE (ciudad = '$ciudad') AND (costo BETWEEN 0 AND '$costo') AND (ventaoalquiler = '$ventaoalquiler') "); 
			 
			if ( mysqli_num_rows($result) >= 1 ) { 
				return $result; 
			} else { 
				return ""; 
			} 
		}
		
		public function casasfavoritasporusuario ($connect_db,$usuario){
			$result = mysqli_query($connect_db, "SELECT cp.ciudad, cp.costo, cp.idcasapublicada
												FROM usuario u , casafavorita cf , casapublicada cp 
												WHERE u.usuario = '$usuario' AND u.idusuario = cf.idusuario AND cf.idcasapublicada = cp.idcasapublicada ");
			
			if ( mysqli_num_rows($result) >= 1 ) { 
				return $result; 
			} else { 
				return ""; 
			}
		}
		
		public function ingresarcasafavorita($connect_db,$idcasapublicada,$usuario){
			$idcasafavorita = -1;
			$result = mysqli_query($connect_db, "INSERT INTO casafavorita (idusuario, idcasapublicada) 
												VALUES ((SELECT idusuario FROM usuario WHERE usuario = '$usuario'),'$idcasapublicada')");
			if ($result) { 
				$idcasafavorita= mysqli_insert_id($connect_db);
				return $idcasafavorita; 
			} else { 
				//echo mysqli_error($connect_db);
				return $idcasafavorita; 
			} 
		}
		
	}
?>