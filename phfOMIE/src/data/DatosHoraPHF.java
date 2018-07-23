package data;

public class DatosHoraPHF {
	
	int año;
	int mes;
	int dia;
	int hora;
	long time;
	int mercado;
	double potencia;
	String dateTime;
	String unidad;
	String codigo;


	public DatosHoraPHF() {
		año = 0;
		mes = 0;
		dia = 0;
		hora = 0;
		time = 0;
		mercado = 0;
		potencia = 0;
	}

	public int getAño() {
		return año;
	}

	public void setAño(int año) {
		this.año = año;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public int getDia() {
		return dia;
	}

	public void setDia(int dia) {
		this.dia = dia;
	}

	public int getHora() {
		return hora;
	}

	public void setHora(int hora) {
		this.hora = hora;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getMercado() {
		return mercado;
	}

	public void setMercado(int mercado) {
		this.mercado = mercado;
	}

	public double getPotencia() {
		return potencia;
	}

	public void setPotencia(double potencia) {
		this.potencia = potencia;
	}
	
	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
}
