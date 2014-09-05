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

		public function crearusuario($connect_db, $username, $password, $mobil, $nombres, $apellidos, $correo ) { 
			 
			$result = mysqli_query($connect_db,"INSERT INTO usuario (usuario,cont,mobil,nombres,apellidos,correo) VALUES ('$username','$password','$mobil','$nombres','$apellidos','$correo')"); 
		 
			if ($result) { 
				return true; 
			} else { 
				//die('Error: ' . mysqli_error($connect_db));
				return false; 
			} 
		}	
	}
?>