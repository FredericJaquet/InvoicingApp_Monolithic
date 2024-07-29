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

/**
 *
 * @author frede
 */
public class ChangeRate {
    
    private String currency1, currency2;
    private double rate;
    private int idChangeRate;
    
    public ChangeRate(){}
    
    public ChangeRate(String currency1, String currency2, double rate){
        this.currency1=currency1;
        this.currency2=currency2;
        this.rate=rate;
    }
    
    /**
    * Adds this changeRate instance to the Database.
    * Sets the idChangeRate from the new register in the DB
    */
    public void addToDB(){
        ConnectionDB con=new ConnectionDB();
        String queryInsert="INSERT INTO ChangeRate (currency1, currency2, rate) values('"+currency1+"','"+currency2+"',"+rate+")";
        String queryGetId="SELECT MAX(idChangeRate) FROM ChangeRate";
        ResultSet result=null;
        
        con.openConnection();
        con.executeUpdate(queryInsert);
        result=con.getResultSet(queryGetId);
        try {
            result.next();
            idChangeRate=result.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(ChangeRate.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    /**
    * Gets the changeRate from the Database and create an instance of that changeRate.
    * @param id The idChangeRate of the changeRate to get from DB
    */
    public void getFromDB (int id){
        
        ConnectionDB con=new ConnectionDB();
        String query="SELECT * FROM ChangeRate WHERE idChangeRate="+id;
        ResultSet result=null;
            
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            if(result.next()){
                currency1=result.getString(2);
                currency2=result.getString(3);
                rate=result.getDouble(4);
                idChangeRate=result.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChangeRate.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    public static ArrayList<ChangeRate> getAllChangeRatesFromDB(){
        ArrayList<ChangeRate> list=new ArrayList();
        String query="SELECT idChangeRate FROM ChangeRate;";
        ConnectionDB con=new ConnectionDB();
        ResultSet result=null;
        ChangeRate rate=new ChangeRate();
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try{
            while(result.next()){
                rate=new ChangeRate();
                rate.getFromDB(result.getInt(1));
                list.add(rate);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChangeRate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;        
    }
    
    /**
    * Cleans the table ChangeRate in the DB.
    * Sets the idChangeRate of all documents to null
    */
    public static void deleteAllFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM ChangeRate";
        String queryUpdateDocument="UPDATE Document SET idChangeRate=null";
        
        con.openConnection();
        con.executeUpdate(queryUpdateDocument);
        con.executeUpdate(query);
        con.closeConnection();
    }
    
    /**
    * Deletes this changeRate instance from the DB.
    * Sets the idChangeRate of the related document to null
    */
    public void deleteFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM ChangeRate where idChangeRate="+idChangeRate;
        String queryGetIdDocument="SELECT idDocument FROM Document WHERE idChangeRate="+idChangeRate;
        String queryUpdateDocument="UPDATE Document SET idChangeRate=null WHERE idDocument=";
        ResultSet result=null;
        int idDocument;
        Document document=new Document();
        
        con.openConnection();
        result=con.getResultSet(queryGetIdDocument);
        try{
            if(result.next()){
                idDocument=result.getInt(1);
                queryUpdateDocument=queryUpdateDocument+idDocument;
                con.executeUpdate(queryUpdateDocument);
                document.getFromDB(idDocument);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChangeRate.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.executeUpdate(query);
        con.closeConnection();
    }
    
    /**
    * Updates this changeRate instance in the DB.
    * @param field The field to update
    * @param newValue The new value for the field to update
    */
    public void updateDB(String field, String newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE ChangeRate SET "+field+"= '"+newValue+"' where idChangeRate="+idChangeRate;
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idChangeRate);
    }
    
    public void updateDB(String field, double newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE ChangeRate SET "+field+"= "+newValue+" where idChangeRate="+idChangeRate;
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idChangeRate);
    }
    
    /**
     * @return the currency1
     */
    public String getCurrency1() {
        return currency1;
    }

    /**
     * @param currency1 the currency1 to set
     */
    public void setCurrency1(String currency1) {
        this.currency1 = currency1;
    }

    /**
     * @return the currency2
     */
    public String getCurrency2() {
        return currency2;
    }

    /**
     * @param currency2 the currency2 to set
     */
    public void setCurrency2(String currency2) {
        this.currency2 = currency2;
    }

    /**
     * @return the rate
     */
    public double getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * @return the id
     */
    public int getIdChangeRate() {
        return idChangeRate;
    }

    /**
     * @param id the id to set
     */
    public void setIdChangeRate(int id) {
        this.idChangeRate = id;
    }
    
    @Override
    public String toString(){
        return currency1+" -> "+currency2+": "+String.format("%.4f",rate);
    }
    
    public static void setDefaultChangeRate(){
        ChangeRate changeRate=new ChangeRate();
        ConnectionDB con=new ConnectionDB();
        String queryInsert="INSERT INTO ChangeRate(currency1, currency2, rate) VALUES('€','€', 1);";
        
        changeRate.getFromDB(1);
        
        if(changeRate.getIdChangeRate()==0){
            con.openConnection();
            con.executeUpdate(queryInsert);
            con.closeConnection();
        }
    }
    
}
