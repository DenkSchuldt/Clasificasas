<?php
	echo "hola";
	
	

?>

<html>
	<body>
	<!--Form 1 probar login-->
	<h1>Probar login</h1>
	<form action="login.php" method="post">
		usuario: <input type="text" name="usuario"><br>
		contrasena: <input type="text" name="cont"><br>
		<input type="submit">
	</form>
	
	<h1>Insertar usuario</h1>
	<form action="insertarusuario.php" method="post">
		usuario: <input type="text" name="usuario"><br>
		contrsena: <input type="text" name="cont"><br>
		mobil: <input type="text" name="mobil"><br>
		nombres: <input type="text" name="nombres"><br>
		apellidos: <input type="text" name="apellidos"><br>
		correo: <input type="text" name="correo"><br>
		<input type="submit">
	</form>
	</body>
</html>
