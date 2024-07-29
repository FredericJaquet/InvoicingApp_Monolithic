/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invoicingapp_monolithic;

import com.invoicingapp.bbdd.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frede
 */
public class ContactPerson {
    private String firstname, middlename, lastname, email, role;
    private int idContactPerson,idCompany;
    
    public ContactPerson(){};
    
    public ContactPerson(String firstname, String middlename, String lastname, String role, String email, int idCompany){
        this.email=email;
        this.firstname=firstname;
        this.idCompany=idCompany;
        this.lastname=lastname;
        this.middlename=middlename;
        this.role=role;
    }
    
    /**
    * Adds this contactPerson instance to the Database.
    * Sets the idContactPerson from the new register in the DB
    */
    public void addToDB(){
        ConnectionDB con=new ConnectionDB();
        String queryInsert="INSERT INTO ContactPerson (firstname, middlename, lastname, role, email, idCompany) values('"+firstname+"','"+middlename+"','"+lastname+"','"+getRole()+"','"+email+"',"+idCompany+")";
        String queryGetId="SELECT MAX(idContactPerson) FROM ContactPerson";
        ResultSet result=null;
        
        con.openConnection();
        con.executeUpdate(queryInsert);
        result=con.getResultSet(queryGetId);
        try {
            result.next();
            setIdContactPerson(result.getInt(1));
        } catch (SQLException ex) {
            Logger.getLogger(ContactPerson.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    /**
    * Gets the contactPerson from the Database and create an instance of that contactPerson.
    * @param id The idContactPerson of the contactPerson to get from DB
    */
    protected void getFromDB (int id){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT * FROM ContactPerson WHERE idContactPerson="+id;
        ResultSet result=null;
         
        con.openConnection();
        result=con.getResultSet(query);
        try {
            if(result.next()){
                idContactPerson=result.getInt(1);
                firstname=result.getString(2);
                middlename=result.getString(3);
                lastname=result.getString(4);
                role=result.getString(5);
                email=result.getString(6);
                idCompany=result.getInt(7);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Address.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    /**
    * Cleans the table ContactPerson in the DB.
    */
    protected static void deleteAllFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM ContactPerson";
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
    }
    
    /**
    * Deletes this contactPerson instance from the DB. 
    */
    public void deleteFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM ContactPerson WHERE idContactPerson="+getIdContactPerson();
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
    }
    
    /**
    * Updates this contactPerson instance in the DB.
    * @param field The field to update
    * @param newValue The new value for the field to update
    */
    public void updateDB(String field, String newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE ContactPerson SET "+field+"= '"+newValue+"' where idContactPerson="+getIdContactPerson();
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(getIdContactPerson());
    }
    
    public void updateDB(String field, int newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE ContactPerson SET "+field+"= '"+newValue+"' where idContactPerson="+getIdContactPerson();
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(getIdContactPerson());
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the middlename
     */
    public String getMiddlename() {
        return middlename;
    }

    /**
     * @param middlename the middlename to set
     */
    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    /**
     * @return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
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
     * @return the idContactPerson
     */
    public int getIdContactPerson() {
        return idContactPerson;
    }

    /**
     * @param idContactPerson the idContactPerson to set
     */
    public void setIdContactPerson(int idContactPerson) {
        this.idContactPerson = idContactPerson;
    }

    /**
     * @return the idCompany
     */
    public int getIdCompany() {
        return idCompany;
    }

    /**
     * @param idCompany the idCompany to set
     */
    public void setIdCompany(int idCompany) {
        this.idCompany = idCompany;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }
    
}
