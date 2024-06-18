/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invoicingapp_monolithic;

import com.invoicingapp.bbdd.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Company {
    
    private String vatNumber, comName, legalName, email, web;
    private Address address=new Address();
    private int idCompany;
    private ArrayList<Phone> phones=new ArrayList();
    private ArrayList<ContactPerson> contacts=new ArrayList();
    
    public Company(){}
    
    public Company(String street, String stNumber, String apt, String cp, String city, String state, String country, String vatNumber, String comName, String legalName, String email, String web){
        this.comName=comName;
        this.email=email;
        this.address=new Address(street, stNumber, apt, cp, city, state, country);
        this.legalName=legalName;
        this.vatNumber=vatNumber;
        this.web=web;
    }
    
    /**
    * Adds this company instance to the Database.
    * Sets the idCompany from the new register in the DB
    */
    protected void addToDB(){
        ConnectionDB con=new ConnectionDB();
        ResultSet result=null;
        String queryInsert=null;
        String queryGetId="SELECT MAX(idCompany) FROM Company";
        String queryGetIds="SELECT idCompany FROM Company;";
        boolean exists=false;
        
        address.addToDB();
        
        queryInsert="INSERT INTO Company (vatNumber, comName, legalName, email, web, idAddress) VALUES('"+vatNumber+"','"+comName+"','"+legalName+"','"+email+"','"+web+"',"+address.getIdAddress()+")";
        
        con.openConnection();
        
        //verify the company does not already exist in DB
        result=con.getResultSet(queryGetIds);
        try{
            while(result.next()){
                if(idCompany==result.getInt(1)){
                    exists=true;
                }
            }
        }catch (SQLException ex) {
            Logger.getLogger(Company.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //If company does not exist in DB, insert the company in DB
        if(!exists){
            con.noReturnQuery(queryInsert);
            result=con.getResultSet(queryGetId);
            try {
                result.next();
                idCompany=result.getInt(1);
            } catch (SQLException ex) {
                Logger.getLogger(Company.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        for(int i=0;i<phones.size();i++){
                phones.get(i).setIdCompany(idCompany);
                phones.get(i).addToDB();
        }
            
        for(int i=0;i<contacts.size();i++){
            contacts.get(i).setIdCompany(idCompany);
            contacts.get(i).addToDB();
        }
        con.closeConnection();
    }
    
    /**
    * Gets the company from the Database and create an instance of that company.
    * Gets the address from Database and create an instance of the address
    * Gets the phone list from Database and create an instance for each phone
    * @param id The idCompany of the company to get from DB
    */
    protected void getFromDB (int id){
        
        ConnectionDB con=new ConnectionDB();
        String query="SELECT * FROM Company WHERE idCompany="+id;
        ResultSet result=null;
            
        con.openConnection();
        result=con.getResultSet(query);
    
        try {
            if(result.next()){
                idCompany=result.getInt(1);
                vatNumber=result.getString(2);
                comName=result.getString(3);
                legalName=result.getString(4);
                email=result.getString(5);
                web=result.getString(6);
                address.getFromDB(result.getInt(7));
                getPhonesFromDB();
                getContactPersonFromDB();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Company.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    public static ArrayList<Company> getAllFromDB(){
        ArrayList<Company> list=new ArrayList();
        String query="SELECT * FROM Company;";
        ConnectionDB con=new ConnectionDB();
        ResultSet result=null;
        Company company=new Company();
       
        con.openConnection();
        result=con.getResultSet(query);
       
        try{
            while (result.next()){
                company=new Company();
                company.setIdCompany(result.getInt(1));
                company.setVatNumber(result.getString(2));
                company.setComName(result.getString(3));
                company.setLegalName(result.getString(4));
                company.setEmail(result.getString(5));
                company.setWeb(result.getString(6));
                company.setAddress(result.getInt(7));
                list.add(company);
            }
        }catch (SQLException ex) {
             Logger.getLogger(Company.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
        return list;
    }
    
    /**
    * Cleans the table Company in the DB.
    * Cleans the table CustomProv in the DB.
    * Cleans the table Phone in the DB.
    * Cleans the table Address in the DB.
    */
    public static void deleteAllFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Company";
        
        CustomProv.deleteAllFromDB();
        Users.deleteAllFromDB();
        Phone.deleteAllFromDB();
                
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
        
        Address.deleteAllFromDB();
    }
    
    /**
    * Deletes this company instance and the related address, customProv, users and 
    * phone instances from the DB. 
    */
    public void deleteFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Company WHERE idCompany="+idCompany;
        String queryGetIdCustomProv="SELECT idCustomProv FROM CustomProv WHERE idCompany="+idCompany;
        String queryGetIdUsers="SELECT idUsers FROM Users WHERE idCompany="+idCompany;
        int idCustomProv, idUsers;
        CustomProv customProv=new CustomProv();
        Users user=new Users();
        ResultSet result=null;
        
        con.openConnection();
        
        for(int i=0;i<phones.size();i++){
            phones.get(i).deleteFromDB();
        }
        
        result=con.getResultSet(queryGetIdUsers);
        try {
            while(result.next()){
                idUsers=result.getInt(1);
                user.getFromDB(idUsers);
                user.deleteFromDB();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Company.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        result=con.getResultSet(queryGetIdCustomProv);
        try {
            while(result.next()){
                idCustomProv=result.getInt(1);
                customProv.getFromDB(idCustomProv);
                customProv.deleteFromDB();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Company.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        address.deleteFromDB();
        
        con.noReturnQuery(query);
        con.closeConnection();
    }
    
    /**
    * Updates this company instance in the DB.
    * @param field The field to update
    * @param newValue The new value for the field to update
    */
    public void updateDB(String field, String newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Company SET "+field+"= '"+newValue+"' where idCompany="+idCompany;
        
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
        getFromDB(idCompany);
    }
    
    public void updateDB(String field, int newValue){
        ConnectionDB con=new ConnectionDB();
        String query;
        
        query="UPDATE Company SET "+field+"= "+newValue+" where idCompany="+idCompany;
        
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
        getFromDB(idCompany);
    }
    
    public void getPhonesFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT phoneNumber FROM Phone WHERE idCompany="+idCompany;
        String phoneNumber;
        Phone phone=new Phone();
        ResultSet result=null;
        
        con.openConnection();
        result=con.getResultSet(query);
        try{
            while(result.next()){
                phoneNumber=result.getString(1);
                phone.getFromDB(phoneNumber);
                phones.add(phone);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Company.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    public void getContactPersonFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT idContactPerson FROM ContactPerson WHERE idCompany="+idCompany;
        int idContactPerson;
        ContactPerson contactPerson=new ContactPerson();
        ResultSet result=null;
        
        con.openConnection();
        result=con.getResultSet(query);
        try{
            while(result.next()){
                idContactPerson=result.getInt(1);
                contactPerson.getFromDB(idContactPerson);
                contacts.add(contactPerson);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Company.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
        /**
    * add the contactPerson to the list contacts
    * @param contact 
    */
    public void addContactPerson(ContactPerson contact){
        contacts.add(contact);
    }
    
    public void addContactPerson(String firstname, String middlename, String lastname, String role, String email){
        contacts.add(new ContactPerson(firstname, middlename, lastname, role, email, idCompany));
    }
    
    /**
    * add the phone to the list phones
    * @param phone 
    */
    public void addPhone(Phone phone){
        phones.add(phone);
    }
    
    public void addPhone(String phoneNumber, String kind){
        phones.add(new Phone(phoneNumber, kind, idCompany));
    }
    
    /**
     * @return the phones list
     */
    public ArrayList<Phone> getPhones(){
        return phones;
    }

    /**
     * @return the vatNumber
     */
    public String getVatNumber() {
        return vatNumber;
    }

    /**
     * @param vatNumber the vatNumber to set
     */
    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    /**
     * @return the comName
     */
    public String getComName() {
        return comName;
    }

    /**
     * @param comName the comName to set
     */
    public void setComName(String comName) {
        this.comName = comName;
    }

    /**
     * @return the legalName
     */
    public String getLegalName() {
        return legalName;
    }

    /**
     * @param legalName the legalName to set
     */
    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the web
     */
    public String getWeb() {
        return web;
    }

    /**
     * @param web the web to set
     */
    public void setWeb(String web) {
        this.web = web;
    }

    /**
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    public void setAddress(String street, String stNumber, String apt, String cp, String city, String state, String country) {
        this.address = new Address(street, stNumber, apt, cp, city, state, country);
    }
    
    public void setAddress(int idAddress){
        this.address=new Address();
        address.getFromDB(idAddress);
    }
    
    public void setAddress(){
        this.address=new Address();
    }

    /**
     * @return the id
     */
    public int getIdCompany() {
        return idCompany;
    }

    /**
     * @param id the id to set
     */
    public void setIdCompany(int id) {
        this.idCompany = id;
    }
    
}
