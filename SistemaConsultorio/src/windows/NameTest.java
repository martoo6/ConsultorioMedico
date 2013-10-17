package windows;

import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import java.awt.Font;
import javax.swing.JMenuBar;
import javax.swing.JButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import objects.CHOld;
import objects.Patient;

import access.DbAccess;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ScrollPaneConstants;
import javax.swing.BoxLayout;

import java.awt.Component;
import java.awt.Dimension;

public class NameTest {
	
	private DbAccess accesorDB = DbAccess.getInstance(); 
	private Patient mainPatient = new Patient();
	private boolean patientSelected=false;
	private JTextArea tmpTxt;
	private JButton tmpBoton;
	private ArrayList<Patient> lstPatients;
	
	private DocumentListener searchListener = new DocumentListener() {
		
		@Override
		public void removeUpdate(DocumentEvent arg0) {
			search();
		}
		
		@Override
		public void insertUpdate(DocumentEvent arg0) {
			search();
		}
		
		@Override
		public void changedUpdate(DocumentEvent arg0) {
			search();
		}
		
		private void search(){
			ArrayList<Patient> patients = new ArrayList<>();
			String search = txtSearch.getText();
			if(search.length()!=0){
				for(Patient tmpPatient :lstPatients){
					if(tmpPatient.getName().contains(search)||tmpPatient.getLastName().contains(search)){
						patients.add(tmpPatient);
					}
				}
				setPatients(patients);
			}else{
				setPatients(lstPatients);
			}
		}
	};
	
	private ActionListener newCHNew = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Date date = new Date();
			
			String query="Insert into CHNew (id_patient,date,desc) values ('"+mainPatient.getId_patient()+"','"+Long.toString(date.getTime())+"','"+tmpTxt.getText()+"')";
			accesorDB.executeNoReturn(query);
			showPatient();
		}
	};
	
	private ActionListener newPatient = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			enableAll();
			
			txtName.setText("");
			txtLastName.setText("");
			txtSocialSecurity.setText("");
			txtPlan.setText("");
			txtFather.setText("");
			txtMother.setText("");
			txtPhone.setText("");
			txtAddress.setText("");
			txtCellphone.setText("");
			txtMail.setText("");
			txtBrothers.setText("");
			
			updatePatient(mainPatient);
			accesorDB.executeNoReturn("BEGIN TRANSACTION");
			String query="Insert into Patient (name, lastName, socialSecurity, plan, father, mother, brothers, phone, cellPhone, address, mail) values (";
			query+="'"+mainPatient.getName();
			query+="','"+mainPatient.getLastName();
			query+="','"+mainPatient.getSocialSecurity();
			query+="','"+mainPatient.getPlan();
			query+="','"+mainPatient.getFather();
			query+="','"+mainPatient.getMother();
			query+="','"+mainPatient.getBrothers();
			query+="',"+mainPatient.getPhoneNumber();
			query+=","+mainPatient.getCellPhone();
			query+=",'"+mainPatient.getAddress();
			query+="','"+mainPatient.getMail()+"')";
			accesorDB.executeNoReturn(query);
			
			
			DefaultTableModel tm = accesorDB.execute("Select MAX(id_patient) from Patient");
			mainPatient.setId_patient((int) tm.getValueAt(0, 0));
			
			query = "Insert into VacinePatient (id_vacine,date,observations,id_patient) Select id_vacine,'','',"+mainPatient.getId_patient()+" from Vacine";
			accesorDB.executeNoReturn(query);
			accesorDB.executeNoReturn("COMMIT TRANSACTION");
		}
	};
	
	private DocumentListener updatePatient = new DocumentListener() {
		
		@Override
		public void removeUpdate(DocumentEvent arg0) {
			update();
		}
		
		@Override
		public void insertUpdate(DocumentEvent arg0) {
			update();
		}
		
		@Override
		public void changedUpdate(DocumentEvent arg0) {
			update();
		}
		
		private void update(){
			if(!patientSelected){
				updatePatient(mainPatient);
				String query="Update Patient set ";
				query+="name='"+mainPatient.getName()+"'";
				query+=", lastName='"+mainPatient.getLastName()+"'";
				query+=", socialSecurity='"+mainPatient.getSocialSecurity()+"'";
				query+=", plan='"+mainPatient.getPlan()+"'";
				query+=", father='"+mainPatient.getFather()+"'";
				query+=", mother='"+mainPatient.getMother()+"'";
				query+=", brothers='"+mainPatient.getBrothers()+"'";
				query+=", phone='"+mainPatient.getPhoneNumber()+"'";
				query+=", cellPhone='"+mainPatient.getCellPhone()+"'";
				query+=", address='"+mainPatient.getAddress()+"'";
				query+=", mail='"+mainPatient.getMail()+"' where id_patient="+mainPatient.getId_patient();
				accesorDB.executeNoReturn(query);

				//TODO: Encontrar la lista de poder actualizar el nombre en la lista sin problemas es problematico
				//Se dispara un listener el cual me tira index negativo dando error
				//setPatients(lstPatients);
				lst_Patients.revalidate();
			}
		}
	};

	private ListSelectionListener showPatient= new ListSelectionListener() {
		
		@Override
		public void valueChanged(ListSelectionEvent e) {
			enableAll();
			showPatient();
		}
	};
	
	private JFrame frame;
	private JTextField txtName;
	private JTextField txtSocialSecurity;
	private JTextField txtPlan;
	private JTextField txtFather;
	private JTextField txtMother;
	private JTextField txtPhone;
	private JTextField txtAddress;
	private JTextField txtCellphone;
	private JTextField txtMail;
	private JTextArea txtBrothers;
	private JList<Patient> lst_Patients;
	private JTextField txtSearch;
	private JTextField txtLastName;
	private JPanel chPanel;
	private JButton btnVerPlanillaVacunaciones;
	private JButton btnVerTablaDe;
	private JButton btnAgregarHistoriaClinica;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					NameTest window = new NameTest();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public NameTest() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		accesorDB.initilaize("./db.sqlite");
		lstPatients=loadPatients();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 738, 611);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		lst_Patients = new JList<Patient>();
		lst_Patients.setBounds(10, 37, 110, 499);
		frame.getContentPane().add(lst_Patients);
		setPatients(lstPatients);
		lst_Patients.addListSelectionListener(showPatient);
		
		txtName = new JTextField();
		txtName.setEnabled(false);
		txtName.setBounds(193, 6, 110, 20);
		frame.getContentPane().add(txtName);
		txtName.setColumns(10);
		txtName.getDocument().addDocumentListener(updatePatient);
		
		JLabel lblObraSocial = new JLabel("Obra Social");
		lblObraSocial.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblObraSocial.setBounds(130, 37, 61, 14);
		frame.getContentPane().add(lblObraSocial);
		
		JLabel lblPlan = new JLabel("Plan");
		lblPlan.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblPlan.setBounds(130, 60, 26, 14);
		frame.getContentPane().add(lblPlan);
		
		JLabel lblPadre = new JLabel("Padre");
		lblPadre.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblPadre.setBounds(313, 37, 36, 14);
		frame.getContentPane().add(lblPadre);
		
		JLabel lblMadre = new JLabel("Madre");
		lblMadre.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblMadre.setBounds(313, 60, 36, 14);
		frame.getContentPane().add(lblMadre);
		
		JLabel lblHermanos = new JLabel("Hermanos");
		lblHermanos.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblHermanos.setBounds(529, 9, 48, 14);
		frame.getContentPane().add(lblHermanos);
		
		txtSocialSecurity = new JTextField();
		txtSocialSecurity.setEnabled(false);
		txtSocialSecurity.setBounds(193, 34, 110, 20);
		frame.getContentPane().add(txtSocialSecurity);
		txtSocialSecurity.setColumns(10);
		txtSocialSecurity.getDocument().addDocumentListener(updatePatient);
		
		txtPlan = new JTextField();
		txtPlan.setEnabled(false);
		txtPlan.setColumns(10);
		txtPlan.setBounds(193, 60, 110, 20);
		frame.getContentPane().add(txtPlan);
		txtPlan.getDocument().addDocumentListener(updatePatient);
		
		txtFather = new JTextField();
		txtFather.setEnabled(false);
		txtFather.setColumns(10);
		txtFather.setBounds(359, 34, 160, 20);
		frame.getContentPane().add(txtFather);
		txtFather.getDocument().addDocumentListener(updatePatient);
		
		txtMother = new JTextField();
		txtMother.setEnabled(false);
		txtMother.setColumns(10);
		txtMother.setBounds(359, 60, 160, 20);
		frame.getContentPane().add(txtMother);
		txtMother.getDocument().addDocumentListener(updatePatient);
		
		JLabel lblHisotriaClinica = new JLabel("Hisotria Clinica");
		lblHisotriaClinica.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblHisotriaClinica.setBounds(130, 138, 73, 14);
		frame.getContentPane().add(lblHisotriaClinica);
		
		JLabel lblTelefono = new JLabel("Telefono");
		lblTelefono.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblTelefono.setBounds(130, 85, 61, 14);
		frame.getContentPane().add(lblTelefono);
		
		txtPhone = new JTextField();
		txtPhone.setEnabled(false);
		txtPhone.setColumns(10);
		txtPhone.setBounds(193, 85, 110, 20);
		frame.getContentPane().add(txtPhone);
		txtPhone.getDocument().addDocumentListener(updatePatient);
		
		JLabel lblDirecion = new JLabel("Direcci\u00F3n");
		lblDirecion.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblDirecion.setBounds(313, 85, 48, 14);
		frame.getContentPane().add(lblDirecion);
		
		txtAddress = new JTextField();
		txtAddress.setEnabled(false);
		txtAddress.setColumns(10);
		txtAddress.setBounds(359, 85, 160, 20);
		frame.getContentPane().add(txtAddress);
		txtAddress.getDocument().addDocumentListener(updatePatient);
		
		JLabel lblCelular = new JLabel("Celular");
		lblCelular.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblCelular.setBounds(130, 110, 61, 14);
		frame.getContentPane().add(lblCelular);
		
		txtCellphone = new JTextField();
		txtCellphone.setEnabled(false);
		txtCellphone.setColumns(10);
		txtCellphone.setBounds(193, 110, 110, 20);
		frame.getContentPane().add(txtCellphone);
		txtCellphone.getDocument().addDocumentListener(updatePatient);
		
		JLabel lblMail = new JLabel("Mail");
		lblMail.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblMail.setBounds(313, 110, 48, 14);
		frame.getContentPane().add(lblMail);
		
		txtMail = new JTextField();
		txtMail.setEnabled(false);
		txtMail.setColumns(10);
		txtMail.setBounds(359, 110, 160, 20);
		frame.getContentPane().add(txtMail);
		txtMail.getDocument().addDocumentListener(updatePatient);
		
		txtSearch = new JTextField();
		txtSearch.setColumns(10);
		txtSearch.setBounds(10, 6, 110, 20);
		frame.getContentPane().add(txtSearch);
		txtSearch.getDocument().addDocumentListener(searchListener);
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNombre.setBounds(130, 9, 61, 14);
		frame.getContentPane().add(lblNombre);
		
		JLabel lblApellido = new JLabel("Apellido");
		lblApellido.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblApellido.setBounds(313, 9, 61, 14);
		frame.getContentPane().add(lblApellido);
		
		txtLastName = new JTextField();
		txtLastName.setEnabled(false);
		txtLastName.setColumns(10);
		txtLastName.setBounds(359, 6, 160, 20);
		frame.getContentPane().add(txtLastName);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(580, 4, 132, 126);
		frame.getContentPane().add(scrollPane);
		
		txtBrothers = new JTextArea();
		txtBrothers.setEnabled(false);
		scrollPane.setViewportView(txtBrothers);
		txtBrothers.getDocument().addDocumentListener(updatePatient);
		
		JScrollPane chPanelScroll = new JScrollPane();
		chPanelScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chPanelScroll.setBounds(130, 163, 582, 373);
		frame.getContentPane().add(chPanelScroll);
		
		chPanel = new JPanel();
		Dimension dim = new Dimension();
		dim.width=582;
		dim.height=10000;
		chPanel.setMaximumSize(new Dimension(500, 10000));
		chPanelScroll.setViewportView(chPanel);
		chPanel.setLayout(new BoxLayout(chPanel, BoxLayout.Y_AXIS));
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JButton btnNuevoPaciente = new JButton("Nuevo Paciente");
		btnNuevoPaciente.addActionListener(newPatient);
		menuBar.add(btnNuevoPaciente);
		
		btnVerPlanillaVacunaciones = new JButton("Ver Planilla Vacunaciones");
		btnVerPlanillaVacunaciones.setEnabled(false);
		btnVerPlanillaVacunaciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VacineTable frame = new VacineTable(mainPatient);
				frame.setVisible(true);
			}
		});
		menuBar.add(btnVerPlanillaVacunaciones);
		
		btnVerTablaDe = new JButton("Ver Tabla de Percentilos");
		btnVerTablaDe.setEnabled(false);
		menuBar.add(btnVerTablaDe);
		
		btnAgregarHistoriaClinica = new JButton("Agregar Historia Clinica Vieja");
		btnAgregarHistoriaClinica.setEnabled(false);
		btnAgregarHistoriaClinica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(frame);
				CHOld.newOldClinicHistory(fc.getSelectedFile(),mainPatient.getId_patient());
				showPatient();
			}
		});
		menuBar.add(btnAgregarHistoriaClinica);
	}

	private void updatePatient(Patient patient) {
		patient.setName(txtName.getText());
		patient.setLastName(txtLastName.getText());						
		patient.setSocialSecurity(txtSocialSecurity.getText());
		patient.setPlan(txtPlan.getText());
		patient.setFather(txtFather.getText());
		patient.setMother(txtMother.getText());
		patient.setBrothers(txtBrothers.getText());
		patient.setPhoneNumber(DbAccess.convToInt(txtPhone.getText()));
		patient.setCellPhone(DbAccess.convToLong(txtCellphone.getText()));
		patient.setAddress(txtAddress.getText());
		patient.setMail(txtMail.getText());
	}
	
	private ArrayList<Patient> loadPatients(){
		String query = "Select id_patient, name, lastName, socialSecurity, plan, father, mother, brothers, phone, cellPhone, address, mail from Patient order by name, lastName";
		DefaultTableModel tm= accesorDB.execute(query);
		ArrayList<Patient> patients = new ArrayList<>();
		Patient tmpPatient;
		for(int i=0;i<tm.getRowCount();i++){
			tmpPatient = new Patient();
			tmpPatient.setId_patient(DbAccess.toInt(tm.getValueAt(i, 0)));
			tmpPatient.setName(DbAccess.toString(tm.getValueAt(i, 1)));
			tmpPatient.setLastName(DbAccess.toString(tm.getValueAt(i, 2)));				
			tmpPatient.setSocialSecurity(DbAccess.toString(tm.getValueAt(i, 3)));
			tmpPatient.setPlan(DbAccess.toString(tm.getValueAt(i, 4)));
			tmpPatient.setFather(DbAccess.toString(tm.getValueAt(i, 5)));
			tmpPatient.setMother(DbAccess.toString(tm.getValueAt(i, 6)));
			tmpPatient.setBrothers(DbAccess.toString(tm.getValueAt(i, 7)));
			tmpPatient.setPhoneNumber(DbAccess.toLong(tm.getValueAt(i, 8)));
			tmpPatient.setCellPhone(DbAccess.toLong(tm.getValueAt(i, 9)));
			tmpPatient.setAddress(DbAccess.toString(tm.getValueAt(i, 10)));
			tmpPatient.setMail(DbAccess.toString(tm.getValueAt(i, 11)));
			patients.add(tmpPatient);
		}
		return patients;
	}
	
	private void setPatients( ArrayList<Patient> patients){
		DefaultListModel<Patient> lm = new DefaultListModel<Patient>();
		for(Patient tmpPatient: patients){			
			lm.addElement(tmpPatient);
		}
		lst_Patients.setModel(lm);
	}
	
	private void showPatient(){
		mainPatient = lst_Patients.getModel().getElementAt(lst_Patients.getSelectedIndex());
		patientSelected=true;
		txtName.setText(mainPatient.getName());
		txtLastName.setText(mainPatient.getLastName());
		txtSocialSecurity.setText(mainPatient.getSocialSecurity());
		txtPlan.setText(mainPatient.getPlan());
		txtFather.setText(mainPatient.getFather());
		txtMother.setText(mainPatient.getMother());
		txtPhone.setText(DbAccess.toString(mainPatient.getPhoneNumber()));
		txtAddress.setText(mainPatient.getAddress());
		txtCellphone.setText(DbAccess.toString(mainPatient.getCellPhone()));
		txtMail.setText(mainPatient.getMail());
		txtBrothers.setText(mainPatient.getBrothers());
		//Muestra hisotrias clinicas viejas
		String query="Select path from Patient P, CHOld C where P.id_patient=C.id_patient and P.id_patient='"+mainPatient.getId_patient()+"' order by date asc";
		DefaultTableModel tm= accesorDB.execute(query);
		chPanel.removeAll();
		System.out.println(tm.getRowCount());
		for(int i=0;i<tm.getRowCount();i++){
			//System.out.println(tm.getValueAt(i, 0).toString());
			createImage(tm.getValueAt(i, 0).toString(),chPanel);
		}
		//Muestra hisotrias clinicas nuevas
		query="Select date, desc from Patient P, CHNew C where P.id_patient=C.id_patient and P.id_patient='"+mainPatient.getId_patient()+"' order by date asc";
		tm= accesorDB.execute(query);
		for(int i=0;i<tm.getRowCount();i++){
			Date date = new Date((long) tm.getValueAt(i, 0));
			String strDate = new SimpleDateFormat("dd/MM/yyyy").format(date).toString();
			JLabel tmpLbl = new JLabel(DbAccess.toString(strDate));
			chPanel.add(tmpLbl);
			JTextArea tmpTxt = new JTextArea(tm.getValueAt(i, 1).toString());
			tmpTxt.setAlignmentX(Component.LEFT_ALIGNMENT);
			tmpTxt.setEnabled(false);
			chPanel.add(tmpTxt);
		}
		//Muestra texto para agregar nueva historia clinica
		chPanel.add(new JLabel("Nueva historia"));
		tmpTxt = new JTextArea();
		tmpTxt.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		chPanel.add(tmpTxt);
		tmpBoton=new JButton("Guardar");
		tmpBoton.setBounds(0, 0, 100, 100);
		tmpBoton.setSize(100, 400);
		tmpBoton.addActionListener(newCHNew);
		
		chPanel.add(tmpBoton);
		
		chPanel.revalidate();
		chPanel.repaint();
		
		patientSelected=false;
	}
	
	private void createImage(String path, JPanel chPanel){
		BufferedImage myPicture = null;
		try {
			System.out.println(new File(Paths.get(path).toString()));
			myPicture = ImageIO.read(new File(Paths.get(path).toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int width=chPanel.getWidth();
		int height=(myPicture.getHeight()*chPanel.getWidth())/myPicture.getWidth();
		JLabel picLabel = new JLabel(new ImageIcon( myPicture.getScaledInstance(width, height, 0) ));
		chPanel.add(picLabel);
	}

	private void enableAll() {
		txtName.setEnabled(true);
		txtLastName.setEnabled(true);
		txtSocialSecurity.setEnabled(true);
		txtPlan.setEnabled(true);
		txtFather.setEnabled(true);
		txtMother.setEnabled(true);
		txtPhone.setEnabled(true);
		txtAddress.setEnabled(true);
		txtCellphone.setEnabled(true);
		txtMail.setEnabled(true);
		txtBrothers.setEnabled(true);
		btnVerPlanillaVacunaciones.setEnabled(true);
		btnVerTablaDe.setEnabled(true);
		btnAgregarHistoriaClinica.setEnabled(true);
	}
}
