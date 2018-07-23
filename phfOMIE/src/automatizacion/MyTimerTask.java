package automatizacion;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Timer;
import java.util.TimerTask;

import db.ConexionDB;

class MyTimerTask {

	public static void main(String arglist[]) {
		Timer timer;
		timer = new Timer();
		ConexionDB con = new ConexionDB();
		
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				
				//phfOMIE.Procesar(arglist);
				
				try {
					con.Conexion();
					con.WriteDB();
					con.CloseDB();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		// Empezamos dentro de 10ms y luego lanzamos la tarea cada 1 dia
		timer.schedule(task, 10, 86400000);
	}
}
