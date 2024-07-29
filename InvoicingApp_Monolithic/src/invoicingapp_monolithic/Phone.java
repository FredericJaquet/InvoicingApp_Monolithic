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
public class Phone {
    
    private String phoneNumber, kind;
    private int idCompany;
    
    public Phone(){}
    
    public Phone(String phoneNumber, String kind, int idCompany){
        this.kind=kind;
        this.phoneNumber=phoneNumber;
        this.idCompany=idCompany;
    }
    
    /**
    * Adds this phone instance to the Database.
    */
    public void addToDB(){
        ConnectionDB con=new ConnectionDB();
        String queryInsert="INSERT INTO Phone (phoneNumber, kind, idCompany) values('"+phoneNumber+"','"+kind+"',"+idCompany+")";
        ResultSet result=null;
        
        con.openConnection();
        con.executeUpdate(queryInsert);
        con.closeConnection();
    }
    
    /**
    * Gets the phone from the Database and create an instance of that phone.
    * @param phoneNumber The phoneNumber of the phone to get from DB
    */
    public void getFromDB (String phoneNumber){
        
        ConnectionDB con=new ConnectionDB();
        String query="SELECT * FROM Phone WHERE phoneNumber LIKE '"+phoneNumber+"'";
        ResultSet result=null;
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            if(result.next()){
                this.phoneNumber=result.getString(1);
                kind=result.getString(2);
                idCompany=result.getInt(3);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Phone.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    /**
    * Cleans the table Phone in the DB.
    */
    public static void deleteAllFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Phone";
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
    }
    
    /**
    * Deletes this phone instance from the DB. 
    */
    public void deleteFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Phone WHERE phoneNumber LIKE '"+phoneNumber+"'";
                
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
    }
    
    /**
     * Updates this phone instance in the DB.
     * @param field
     * @param newValue
     */
    public void updateDB(String field, String newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE phone SET "+field+"= '"+newValue+"' WHERE phoneNumber LIKE '"+phoneNumber+"'";
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(phoneNumber); 
    }
    
    public void updateDB(String field, int newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Phone SET "+field+"= "+newValue+" WHERE phoneNumber LIKE '"+phoneNumber+"'";
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(phoneNumber);
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the kind
     */
    public String getKind() {
        return kind;
    }

    /**
     * @param kind the kind to set
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * @return the idCompany
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
