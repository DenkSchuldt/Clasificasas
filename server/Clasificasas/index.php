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
	
	<h1>Publicar casa</h1>
	<form action="publicarcasa.php" method="post">
		usuario: <input type="text" name="usuario"><br>
		latitud: <input type="text" name="latitud"><br>
		longitud: <input type="text" name="longitud"><br>
		cuartos: <input type="text" name="cuartos"><br>
		pisos: <input type="text" name="pisos"><br>
		descripcion: <input type="text" name="descripcion"><br>
		costo: <input type="text" name="costo"><br>
		fotoportada: <input type="text" name="fotoportada"><br>
		terreno: <input type="text" name="terreno"><br>
		construccion: <input type="text" name="construccion"><br>
		direccion: <input type="text" name="direccion"><br>
		ventaoalquiler: <input type="text" name="ventaoalquiler"><br>
		ciudad: <input type="text" name="ciudad"><br>
		banos: <input type="text" name="banos"><br>
		<input type="submit">
	</form>
	
	<h1>Buscar casa</h1>
	<form action="buscarcasa.php" method="post">
		usuario: <input type="text" name="usuario"><br>
		ciudad: <input type="text" name="ciudad"><br>
		costo: <input type="text" name="costo"><br>
		venta o alquiler: <input type="text" name="ventaoalquiler"><br>
		<input type="submit">
	</form>
	
	<html>
		<h1>Subir foto</h1>

		<form action="upload_file.php" method="post"
		enctype="multipart/form-data">
		<label for="file">Filename:</label>
		<input type="file" name="uploaded_file" id="uploaded_file"><br>
		<input type="submit" name="submit" value="Submit">
		</form>

	<h1>Buscar casa</h1>
	<form action="casaspublicadasporusuario.php" method="post">
		usuario: <input type="text" name="usuario"><br>
		<input type="submit">
	</form>
	
	<h1>datos de casa</h1>
	<form action="obtenerdatosdecasa.php" method="post">
		idcasapublicada: <input type="text" name="idcasapublicada"><br>
		<input type="submit">
	</form>
	
	<h1>Obtener max y min</h1>
	<form action="obtenermaxymin.php" method="post">
		<input type="submit">
	</form>
	
	</body>
</html>
