<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.io.*"%>
<%@ page import="java.time.Instant"%>
<%@ page import="org.jfree.chart.ChartFactory"%>
<%@ page import="org.jfree.chart.ChartUtilities"%>
<%@ page import="org.jfree.chart.JFreeChart"%>
<%@ page import="org.jfree.chart.plot.PlotOrientation"%>
<%@ page import="org.jfree.data.*"%>
<%@ page import="org.jfree.data.jdbc.JDBCCategoryDataset"%>
<%@ page import="org.jfree.data.category.DefaultCategoryDataset"%>
<%@ page import="org.jfree.chart.renderer.category.CategoryItemRenderer"%>
<%@ page import="org.jfree.chart.plot.CategoryPlot"%>
<%@ page import="org.jfree.chart.plot.XYPlot"%>
<%@ page import="org.jfree.data.xy.XYSeries"%>
<%@ page import="org.jfree.data.xy.XYSeriesCollection"%>
<%@ page import="org.jfree.chart.axis.DateAxis"%>
<%@ page import="java.awt.Color"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.SortedMap"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="data.DatosHoraPHF"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>

<html>
<head>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<div class="InfoGeneral">
		<h3>INFORMACION GENERAL</h3>
		<form id="formUpload" action="phfOMIE.jsp" method="post">
			<input type="text" value="" name="uploadText" /> <input type="text"
				value="FechaInicio" name="FechaInicio" /> <input type="text"
				value="FechaFinal" name="FechaFinal" /> <input type="submit"
				value="Procesar datos" />
		</form>
	</div>
	<%
		
		String unidad = (String) session.getAttribute("uploadText");
		String line = "";
        String cvsSplitBy = ";";
        DatosHoraPHF datosHoraPHF;
        String rutaCSV = "C:\\Users\\josum\\eclipse-workspace\\phfOMIE\\WebContent\\csv\\PHF_" + unidad + ".csv";	
		String rutaJPG = "images/phfOMIE.jpg?dummy=" + Long.toString(Instant.now().toEpochMilli());
        BufferedReader br = new BufferedReader(new FileReader(rutaCSV));
        datosHoraPHF=new DatosHoraPHF();
		final XYSeries series = new XYSeries("RamdomData");
		Date dat;
		
        while ((line = br.readLine()) != null) {

            // use comma as separator
            String[] phf = line.split(cvsSplitBy);
            datosHoraPHF.setDateTime(phf[0]);
            datosHoraPHF.setAño(Integer.parseInt(phf[1].replace(" ","")));
            datosHoraPHF.setMes(Integer.parseInt(phf[2].replace(" ","")));
            datosHoraPHF.setDia(Integer.parseInt(phf[3].replace(" ","")));
            datosHoraPHF.setHora(Integer.parseInt(phf[4].replace(" ","")));
            datosHoraPHF.setMercado(Integer.parseInt(phf[5].replace(" ","")));
            datosHoraPHF.setTime(Long.parseLong(phf[6].replace(" ","")));
            datosHoraPHF.setPotencia(Float.parseFloat(phf[7].replace(" ","")));
            
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			dat = sdf.parse(phf[0]);

			// dataset.addValue((double) datos.energia.get(i), "Potencia", dateTime);
			series.add(dat.getTime(),(double)datosHoraPHF.getPotencia());
				
        }
        
        XYSeriesCollection dataset = new XYSeriesCollection(series);

		// Genera el grafico
		JFreeChart chart = ChartFactory.createXYAreaChart(unidad, "Dias", "Potencia", dataset,
				PlotOrientation.VERTICAL, false, true, false);
		XYPlot plot = (XYPlot) chart.getPlot();

		DateAxis xAxis = new DateAxis("Date");

		plot.setDomainAxis(xAxis);
		br.close();
		
		// Guarda el grafico
		ChartUtilities.saveChartAsJPEG(
				new File("C:\\Users\\josum\\eclipse-workspace\\images\\phfOMIE.jpg"),
				chart, 700, 700);
		
	%>
	<div id="phfOMIE">
		<h3>POTENCIA</h3>
		<h4>
			(consumo por factura)</h4>
		<div id="Graf">
			<img src="<%=rutaJPG%>" height="700" width="700">
		</div>
	</div>
	<a href="csv/PHF_<%=unidad%>.csv">descarga CSV</a>

</body>
</html>