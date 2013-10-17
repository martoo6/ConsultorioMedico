package objects;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import access.DbAccess;

public class CHOld extends CH{
	private static DbAccess accesorDB = DbAccess.getInstance();
	private String path;
	@Override
	public void show(JPanel panel) {
		BufferedImage myPicture = null;
		try {
			myPicture = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int width=panel.getWidth();
		int height=(myPicture.getHeight()*panel.getWidth())/myPicture.getWidth();
		JLabel picLabel = new JLabel(new ImageIcon( myPicture.getScaledInstance(width, height, 0) ));
		panel.add(picLabel);
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public static void newOldClinicHistory(File selectedFile, int id_patient){
		Path source = selectedFile.toPath();
		Date date = new Date();
		String name= Long.toString(date.getTime());
		Path tarjet = new File("./OCH/"+name).toPath();
		try {
			Files.copy(source, tarjet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String query = "Insert into CHOld (id_patient,date,path) values ("+id_patient+",'"+date.getTime()+"','"+tarjet.toAbsolutePath().toString()+"')";
		accesorDB.executeNoReturn(query);
	}
}
