<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.io.*"%>
<%@ page import="java.time.Instant"%>

<%@ page import="java.awt.Color"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.SortedMap"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="data.DatosHoraPHF"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Date"%>
<%@ page import="db.ConexionDB"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.Statement"%>



<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Titulo</title>
</head>
<body>
	<div id="wait">
		<p>Procesando datos...</p>

		<%
			// carga los ficheros phf desde OMIE

			boolean encontrado = false;
			String unidad = (String) request.getParameter("uploadText");
			String fechaInicio = (String) request.getParameter("FechaInicio");
			String fechaFinal = (String) request.getParameter("FechaFinal");
			session.setAttribute("uploadText", unidad);

			ConexionDB con=new ConexionDB();
			con.Conexion();
			encontrado=con.ReadDB(unidad, fechaInicio, fechaFinal);
			con.CloseDB();
			
			if(!encontrado){
				out.println("<p>No ha datos de esta unidad en este intervalo de tiempo</p><p>CSV vacio creado</p>");
			}else{
				out.println("<p>CSV creado</p>");
			}

			
		%>
		<br />
	</div>
	<script>
		alert("Finished processing.");
		document.getElementById("wait").innerHTML = "Here are the results...";
		window.location = "graficas.jsp";
	</script>
</body>
</html>
