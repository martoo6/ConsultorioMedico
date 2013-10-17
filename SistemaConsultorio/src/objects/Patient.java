package objects;

public class Patient {
	private String name;
	private String lastName;
	private String socialSecurity;
	private String plan;
	private String father;
	private String mother;
	private String brothers;
	private String mail;
	private long phoneNumber;
	private long cellPhone;
	private int id_patient;
	private String address;
	
	
	public String toString(){
		return this.name+", "+this.lastName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getSocialSecurity() {
		return socialSecurity;
	}
	public void setSocialSecurity(String socialSecurity) {
		this.socialSecurity = socialSecurity;
	}
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	public String getFather() {
		return father;
	}
	public void setFather(String father) {
		this.father = father;
	}
	public String getMother() {
		return mother;
	}
	public void setMother(String mother) {
		this.mother = mother;
	}
	public String getBrothers() {
		return brothers;
	}
	public void setBrothers(String brother) {
		this.brothers = brother;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public long getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public long getCellPhone() {
		return cellPhone;
	}
	public void setCellPhone(long cellPhone) {
		this.cellPhone = cellPhone;
	}
	public int getId_patient() {
		return id_patient;
	}
	public void setId_patient(int id_patient) {
		this.id_patient = id_patient;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
