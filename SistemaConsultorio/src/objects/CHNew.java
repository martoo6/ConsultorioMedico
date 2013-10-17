package objects;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class CHNew extends CH{
	private String desc;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public void show(JPanel panel) {
		JTextArea text = new JTextArea();
		text.setText(desc);
		panel.add(text);
	}
}
