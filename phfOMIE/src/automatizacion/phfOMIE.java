


package automatizacion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

public class phfOMIE {
	
	public static boolean exists(URL url) {
		try {
			HttpURLConnection.setFollowRedirects(false);
			// note : you may also need
			//        HttpURLConnection.setInstanceFollowRedirects(false)
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void Procesar(String[] args) {
		
		System.out.println("Empezando...");
		// carga los ficheros phf desde OMIE
		
		String nombreFichero = null;
		String nombreFicheroSiguiente = null;
		URL url = null;
		URL urlSiguiente = null;
		int año = 0, mes = 0;
		
		Calendar fechaActual = Calendar.getInstance();
		
		for (año = 2001; año <= fechaActual.get(Calendar.YEAR); año++) {
			for (mes = 1; mes <= 12; mes++) {
				if ((año == fechaActual.get(Calendar.YEAR)) && (mes > fechaActual.get(Calendar.MONTH))) mes = 12;
				try {
					nombreFichero = "phf_" + Integer.toString(año) + String.format("%02d", mes) + ".zip";
					if(mes==12) {
						nombreFicheroSiguiente = "phf_" + Integer.toString(año+1) + String.format("%02d",01) + ".zip";
					}else {
						nombreFicheroSiguiente = "phf_" + Integer.toString(año) + String.format("%02d", mes+1) + ".zip";
					}
					System.out.println("Creando URL: " + nombreFichero);
					url = new URL("http://www.omie.es/datosPub/phf/" + nombreFichero);
					urlSiguiente = new URL("http://www.omie.es/datosPub/phf/" + nombreFicheroSiguiente);
				} catch (MalformedURLException e) {
					System.out.println("Error creando URL " + nombreFichero);
					e.printStackTrace();
				}
				if (exists(url)) {
					System.out.println("Creando fichero: " + nombreFichero);
					File ficheroLocal = new File("WebContent/phf/" + nombreFichero);
					if (!ficheroLocal.exists()) {
						try {
							System.out.println("Copiando fichero: " + nombreFichero);
							FileUtils.copyURLToFile(url, ficheroLocal);
							
							// descomprime el fichero
							String zipFilePath = "WebContent/phf/" + nombreFichero;
					        String destDirectory = "WebContent/phf/unzip";
					        UnzipUtility unzipper = new UnzipUtility();
					        try {
					        	System.out.println("Descomprimiendo fichero: " + nombreFichero);
					            unzipper.unzip(zipFilePath, destDirectory);
					        } catch (Exception ex) {
					            // some errors occurred
					            ex.printStackTrace();
					        }
						} catch (IOException e) {
							System.out.println("Error copiando fichero: " + nombreFichero);
							e.printStackTrace();
						}
					} else {
						System.out.println("Existe fichero: " + nombreFichero);
						if(!exists(urlSiguiente)) {
							try {
								System.out.println("Actualizando ultimo fichero: " + nombreFichero);
								FileUtils.copyURLToFile(url, ficheroLocal);
								
								// descomprime el fichero
								String zipFilePath = "WebContent/phf/" + nombreFichero;
						        String destDirectory = "WebContent/phf/unzip";
						        UnzipUtility unzipper = new UnzipUtility();
						        try {
						        	System.out.println("Descomprimiendo fichero: " + nombreFichero);
						            unzipper.unzip(zipFilePath, destDirectory);
						        } catch (Exception ex) {
						            // some errors occurred
						            ex.printStackTrace();
						        }
							} catch (IOException e) {
								System.out.println("Error copiando fichero: " + nombreFichero);
								e.printStackTrace();
							}
						}
					}
				} else {
					System.out.println("URL no existe: " + nombreFichero);
				}
			}
		}

	}
}
