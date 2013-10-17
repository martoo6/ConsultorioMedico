package windows;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import objects.Patient;
import javax.swing.JTable;

import access.DbAccess;

public class VacineTable extends JFrame {
	
	private static final long serialVersionUID = -7675264503548617535L;
	private JPanel contentPane;
	private JTable tblVacines;
	private DefaultTableModel tm;
	private Patient patient;
	private DbAccess accessor = DbAccess.getInstance();
	
	private WindowListener winList = new WindowListener() {
		
		@Override
		public void windowOpened(WindowEvent e) {}
		
		@Override
		public void windowIconified(WindowEvent e) {}
		
		@Override
		public void windowDeiconified(WindowEvent e) {}
		
		@Override
		public void windowDeactivated(WindowEvent e) {}
		
		@Override
		public void windowClosed(WindowEvent e) {}
		
		@Override
		public void windowActivated(WindowEvent e) {}
		
		@Override
		public void windowClosing(WindowEvent e) {
			// TODO GUARDAR DATOS !
			String query, date, observations;
			accessor.executeNoReturn("BEGIN TRANSACTION");
			for(int i=0;i<tm.getRowCount();i++){
				date=tm.getValueAt(i, 2).toString();
				observations=tm.getValueAt(i, 3).toString();
				if(date!=""||observations!=""){
					query="Update VacinePatient set date='"+date+"', observations='"+observations+"' where id_vacine='"+(int)(tm.getValueAt(i, 0))+"' and id_patient='"+patient.getId_patient()+"'";
					accessor.executeNoReturn(query);
				}
			}
			accessor.executeNoReturn("COMMIT TRANSACTION");
			
		}
	};
	
	public VacineTable(Patient patient) {
		this.patient=patient;
		this.addWindowListener(winList);
		this.setTitle("Planilla de vacunaciones de: "+patient.getName()+" "+patient.getLastName());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 557, 661);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		tblVacines = new JTable();
		contentPane.add(tblVacines, BorderLayout.CENTER);
		
		DbAccess accessor=DbAccess.getInstance();
		String query="Select V.id_vacine, V.name, VP.date, VP.observations from Vacine V left join VacinePatient VP on V.id_vacine=VP.id_vacine and VP.id_patient="+patient.getId_patient()+" order by V.name";
		tm = accessor.execute(query);
		tblVacines.setModel(tm);
		tblVacines.removeColumn(tblVacines.getColumnModel().getColumn(0));
	}

}
