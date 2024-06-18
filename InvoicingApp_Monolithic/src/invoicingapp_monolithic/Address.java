/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invoicingapp_monolithic;

import com.invoicingapp.bbdd.*;
import java.sql.* ;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frede
 */
public class Address {

    private String street, stNumber, apt, cp, city, state, country;
    private int idAddress;
    
    public Address(){}
    
    public Address(String street, String stNumber, String apt, String cp, String city, String state, String country){
        this.apt=apt;
        this.city=city;
        this.country=country;
        this.cp=cp;
        this.stNumber=stNumber;
        this.state=state;
        this.street=street;
    }

    /**
    * Adds this address instance to the Database.
    * Sets the idAddress from the new register in the DB
    */
    public void addToDB(){
        ConnectionDB con=new ConnectionDB();
        String queryInsert="INSERT INTO Address (street, stNumber, apt, cp, city, state, country) values('"+street+"','"+stNumber+"','"+apt+"','"+cp+"','"+city+"','"+state+"','"+country+"')";
        String queryGetId="SELECT MAX(idAddress) FROM Address";
        String queryGetIds="SELECT idAddress FROM Address;";
        ResultSet result=null;
        boolean exists=false;
        
        con.openConnection();
        
        //verify the address does not already exist in DB
        result=con.getResultSet(queryGetIds);
        try{
            while(result.next()){
                if(idAddress==result.getInt(1)){
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
                idAddress=result.getInt(1);
            } catch (SQLException ex) {
                Logger.getLogger(Address.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        con.closeConnection();
    }
    
    /**
    * Gets the address from the Database and create an instance of that address.
    * @param id The idAddress of the address to get from DB
    */
    protected void getFromDB (int id){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT * FROM Address WHERE idAddress="+id;
        ResultSet result=null;
         
        con.openConnection();
        result=con.getResultSet(query);
        try {
            if(result.next()){
                idAddress=result.getInt(1);
                street=result.getString(2);
                stNumber=result.getString(3);
                apt=result.getString(4);
                cp=result.getString(5);
                city=result.getString(6);
                state=result.getString(7);
                country=result.getString(8);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Address.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    /**
    * Cleans the table Address in the DB.
    */
    protected static void deleteAllFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Address";
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
    }
    
    /**
    * Deletes this address instance from the DB. 
    */
    protected void deleteFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Address WHERE idAddress="+idAddress;
        
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
    }
    
    /**
    * Updates this address instance in the DB.
    * @param field The field to update
    * @param newValue The new value for the field to update
    */
    public void updateDB(String field, String newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Address SET "+field+"= '"+newValue+"' where idAddress="+idAddress;
        
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
        getFromDB(idAddress);
    }
    
    /**
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street the street to set
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @return the stNumber
     */
    public String getStNumber() {
        return stNumber;
    }

    /**
     * @param stNumber the stNumber to set
     */
    public void setStNumber(String stNumber) {
        this.stNumber = stNumber;
    }

    /**
     * @return the apt
     */
    public String getApt() {
        return apt;
    }

    /**
     * @param apt the apt to set
     */
    public void setApt(String apt) {
        this.apt = apt;
    }

    /**
     * @return the cp
     */
    public String getCp() {
        return cp;
    }

    /**
     * @param cp the cp to set
     */
    public void setCp(String cp) {
        this.cp = cp;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the idAddress
     */
    public int getIdAddress() {
        return idAddress;
    }

    /**
     * @param id the idAddress to set
     */
    public void setIdAddress(int id) {
        this.idAddress = id;
    }
    
}
