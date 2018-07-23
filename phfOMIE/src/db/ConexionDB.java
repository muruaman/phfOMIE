package db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.DatosHoraPHF;

public class ConexionDB {
	// Ruta de nuestra base de datos
	private String servidor = "jdbc:mysql://localhost:3306/phfomie?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

	// Nombre de usuario de mysql
	private String username = "root";

	// Clave de usuario de mysql
	private String password = "jms0912";

	// Nuestra librería mysql
	private String driver = "com.mysql.cj.jdbc.Driver";

	// Objeto del tipo Connection para crear la conexión
	private Connection con;

	private String query = " insert into phfomie.datosphfomie (año, mes, dia, hora, time, mercado, potencia, unidad, codigo)"
			+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public void Conexion() {
		try {
			// Cargar drivers de MySQL
			Class.forName(driver);

			// Establecer la conexion con la base de datos
			con = DriverManager.getConnection(servidor, username, password);

			System.out.println("Conexión realizada a la base de datos con éxito.");
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Error!, conexión fallida a la base de datos.");
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return con; // Retorno el objeto Connection
	}

	public void WriteDB() throws Exception {
		System.out.println("Empezando...");
		// carga los ficheros phf desde OMIE

		int año, mes, dia;

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();

		Statement st = con.createStatement();

		// Buscamos ultima entrada de la base de datos
		String datequery = "SELECT * FROM phfomie.datosphfomie ORDER BY time DESC LIMIT 1";

		ResultSet rs = st.executeQuery(datequery);
		if (rs.next()) {
			// String del primer dia
			String startDateString = Integer.toString(rs.getInt("año")) + "-" + String.format("%02d", rs.getInt("mes"))
					+ "-" + String.format("%02d", rs.getInt("dia"));
			start.set(rs.getInt("año"), rs.getInt("mes"), rs.getInt("dia"), rs.getInt("hora"), 0);

			// Borramos todos los datos del ultimo dia
			String deletequery = "DELETE FROM phfomie.datosphfomie WHERE time > '" + start.getTimeInMillis() + "'";

			start.add(Calendar.MONTH, -1);

			st.execute(deletequery);

		} else {

			String startDateString = "2001-01-01";
			System.out.println(startDateString);
			start.set(2001, 01, 01, 0, 0);
		}

		Date endDate = new Date();
		end.setTime(endDate);
		end.add(Calendar.DATE, 1);
		/*
		 * if (ps.execute()) {
		 * System.out.println("Base de datos eliminada correctamente"); }
		 */

		for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {

			String dateS = formatter.format(date);
			String[] dateSA = dateS.split("-");
			año = Integer.parseInt(dateSA[0]);
			mes = Integer.parseInt(dateSA[1]);
			dia = Integer.parseInt(dateSA[2]);

			System.out.println("Procesando..." + año + "/" + mes + "/" + dia);

			SortedMap<String, DatosHoraPHF> datosPHF = new TreeMap<String, DatosHoraPHF>();

			for (int mercado = 1; mercado <= 6; mercado++) {

				String nombreFicheroCSV = "phf_" + Integer.toString(año) + String.format("%02d", mes)
						+ String.format("%02d", dia) + String.format("%02d", mercado) + ".1";
				File ficheroLocal = new File(
						"C:\\Users\\josum\\eclipse-workspace\\phfOMIE\\WebContent\\phf\\unzip\\" + nombreFicheroCSV);
				System.out.println("Buscando fichero: " + nombreFicheroCSV);
				if (ficheroLocal.exists()) {
					String line = "";
					System.out.println("---");
					try {
						BufferedReader br = new BufferedReader(new FileReader(ficheroLocal));
						System.out.println("Abriendo fichero: " + nombreFicheroCSV);
						while (!((line = br.readLine()).equals("*"))) {
							String[] camposLinea = line.split(";");

							if (!(camposLinea[0].equals("PHF"))) {
								System.out.println("Unidad encontrada: " + line);

								if (dia == Integer.parseInt(camposLinea[2])) {
									int _año = Integer.parseInt(camposLinea[0]);
									int _mes = Integer.parseInt(camposLinea[1]);
									int _dia = Integer.parseInt(camposLinea[2]);
									int _hora = Integer.parseInt(camposLinea[3]);
									int _mercado = Integer.parseInt(camposLinea[4]);
									double _potencia = Double.parseDouble(camposLinea[6]);
									String _codigo = Integer.toString(_año) + "/" + String.format("%02d", _mes) + "/"
											+ String.format("%02d", _dia) + " " + String.format("%02d", _hora)
											+ ":00:00";
									String _unidad = camposLinea[5];
									String _key = _codigo + " " + _unidad;

									System.out.println(_key);

									if (true == datosPHF.containsKey(_key)) {
										DatosHoraPHF datosHoraPHFAux = datosPHF.get(_key);
										if (_mercado > datosHoraPHFAux.getMercado()) {
											datosHoraPHFAux.setMercado(_mercado);
											;
											datosHoraPHFAux.setPotencia(_potencia);
											datosPHF.put(_key, datosHoraPHFAux);
										}
									} else {
										DatosHoraPHF datosHoraPHFAux = new DatosHoraPHF();
										datosHoraPHFAux.setAño(_año);
										datosHoraPHFAux.setMes(_mes);
										datosHoraPHFAux.setDia(_dia);
										datosHoraPHFAux.setHora(_hora);
										Calendar diayhora = Calendar.getInstance();
										diayhora.set(_año, _mes, _dia, _hora, 0);
										datosHoraPHFAux.setTime(diayhora.getTimeInMillis());
										datosHoraPHFAux.setMercado(_mercado);
										datosHoraPHFAux.setPotencia(_potencia);
										datosHoraPHFAux.setUnidad(_unidad);
										datosHoraPHFAux.setCodigo(_codigo);
										datosPHF.put(_key, datosHoraPHFAux);

									}
								}
							}
						}
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			// the mysql insert statement
			for (Map.Entry<String, DatosHoraPHF> entry : datosPHF.entrySet()) {
				DatosHoraPHF datosHoraAux = entry.getValue();
				System.out.println("Fecha y hora: " + entry.getKey() + " Mercado: " + datosHoraAux.getMercado()
						+ " Potencia: " + datosHoraAux.getPotencia());

				PreparedStatement ps = con.prepareStatement(query);

				ps.setInt(1, datosHoraAux.getAño());
				ps.setInt(2, datosHoraAux.getMes());
				ps.setInt(3, datosHoraAux.getDia());
				ps.setInt(4, datosHoraAux.getHora());
				ps.setLong(5, datosHoraAux.getTime());
				ps.setInt(6, datosHoraAux.getMercado());
				ps.setDouble(7, datosHoraAux.getPotencia());
				ps.setString(8, datosHoraAux.getUnidad());
				ps.setString(9, datosHoraAux.getCodigo());

				ps.execute();

			}
		}
	}

	public boolean ReadDB(String unidad, String fechaInicio, String fechaFinal) {

		System.out.println("Empezando...");
		// carga los ficheros phf desde OMIE

		boolean encontrado = false;

		String rutaJPG = "C:\\Users\\josum\\eclipse-workspace\\phfOMIE\\WebContent\\csv\\PHF_" + unidad + ".csv";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaJPG))) {
			if (fechaFinal.matches("\\d{4}-\\d{2}-\\d{2}") && fechaInicio.matches("\\d{4}-\\d{2}-\\d{2}")) {
				Date startDate = formatter.parse(fechaInicio);
				Date endDate = formatter.parse(fechaFinal);
				start.setTime(startDate);
				start.add(Calendar.DATE, 31);
				end.setTime(endDate);
				end.add(Calendar.DATE, 31);
			} else {
				Date startDate = formatter.parse("2001-01-01");
				Date endDate = new Date();
				start.setTime(startDate);
				start.add(Calendar.DATE, 1);
				end.setTime(endDate);
				end.add(Calendar.DATE, 1);
			}



			String query = "SELECT * FROM phfomie.datosphfomie WHERE unidad='" + unidad + "' AND time > '"
					+ start.getTimeInMillis() + "' AND time < '" + end.getTimeInMillis() + "'";

			Statement st = con.createStatement();

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {

				encontrado = true;

				DatosHoraPHF datosHoraAux = new DatosHoraPHF();
				datosHoraAux.setAño(rs.getInt("año"));
				datosHoraAux.setMes(rs.getInt("mes"));
				datosHoraAux.setDia(rs.getInt("dia"));
				datosHoraAux.setHora(rs.getInt("hora"));
				datosHoraAux.setTime(rs.getLong("time"));
				datosHoraAux.setMercado(rs.getInt("mercado"));
				datosHoraAux.setPotencia(rs.getDouble("potencia"));
				datosHoraAux.setUnidad(rs.getString("unidad"));
				datosHoraAux.setDateTime(rs.getString("codigo"));

				String s = datosHoraAux.getDateTime() + "; " + datosHoraAux.getAño() + "; " + datosHoraAux.getMes()
						+ "; " + datosHoraAux.getDia() + "; " + datosHoraAux.getHora() + "; "
						+ datosHoraAux.getMercado() + "; " + datosHoraAux.getTime() + "; " + datosHoraAux.getPotencia()
						+ "\n";

				writer.write(s, 0, s.length());
			}

			st.close();
			System.out.println("CSV creado");

		} catch (IOException | SQLException | ParseException x) {
			System.err.format("IOException: %s%n", x);
		}

		if (!encontrado) {
			File fichero = new File(rutaJPG);
			System.out.println("No ha datos de esta unidad en este intervalo de tiempo - CSV vacio creado");
		}
		
		return encontrado;
	}

	public void CloseDB() {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
