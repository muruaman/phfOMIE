<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="css/style.css">
		<title>File upload</title>
	</head>
	<body>
		<div class="Upload">
			<h1>Introduzca la unidad:</h1>
			<form id="formUpload" action="phfOMIE.jsp">
				<input type="text" value="Unidad" name="uploadText"/>
				<input type="text" value="FechaInicio" name="FechaInicio"/>
				<input type="text" value="FechaFinal" name="FechaFinal"/>
				<input type="submit" value="Procesar datos"/>
			</form>
			<p>Introduce la fecha en formato aaaa-mm-dd</p>
			<p>Si no introduces fecha se procesa todos los datos desde 2001</p>
			<a href ="csv/lista_unidades.pdf">lista de unidades</a> 
		</div>
	</body>
</html>