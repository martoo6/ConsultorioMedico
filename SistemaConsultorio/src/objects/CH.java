package objects;

import java.sql.Date;

import javax.swing.JPanel;

public abstract class CH {
	private int id_patient;
	private Date date;
	public int getId_patient() {
		return id_patient;
	}
	public void setId_patient(int id_patient) {
		this.id_patient = id_patient;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public abstract void show(JPanel panel);
}
