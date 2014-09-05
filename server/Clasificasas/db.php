<?php
	class DB { 
     
		protected $db_name = 'clasificasas'; 
		protected $db_user = 'root'; 
		protected $db_pass = 'root'; 
		protected $db_host = 'localhost'; 
				 
		// Open a connect to the database.  
		// Make sure this is called on every page that needs to use the database. 
		 
		public function connect() { 
		 
			$connect_db = new mysqli( $this->db_host, $this->db_user, $this->db_pass, $this->db_name ); 
			 
			if ( mysqli_connect_errno() ) { 
				echo "Connection failed: %s\n", mysqli_connect_error(); 
				exit(); 
			} 
			echo "success";
			return $connect_db; 
			 
		} 
		
		public function login($connect_db,$username, $password ) { 
         
        $result = mysqli_query($connect_db,"SELECT * FROM usuario WHERE usuario = '$username' AND cont = '$password' "); 
         
        if ( mysqli_num_rows($result) == 1 ) { 
			echo "usuario registrado";
            /*$_SESSION[ "user" ] = serialize( new User( mysqli_fetch_assoc( $result ) ) ); 
            $_SESSION[ 'login_time' ] = time(); 
            $_SESSION[ 'logged_in' ] = 1; */
            return true; 
        } else { 
			echo "usuario no registrado";
            return false; 
        } 
    } 

	}
?>