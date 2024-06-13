/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invoicingapp_monolithic;

import com.invoicingapp.bbdd.ConnectionDB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frede
 */
public class SchemeLine {
    private String description;
    private double discount;
    private int idSchemeLine, idScheme;
    
    public SchemeLine(){}
    
    public SchemeLine(String description, double discount){
        this.description=description;
        this.discount=discount;
    }
    
    /**
    * Adds this schemeLine instance to the Database.
    * Sets the idSchemeLine from the new register in the DB
    */
    protected void addToDB(){
        ConnectionDB con=new ConnectionDB();
        String queryInsert="INSERT INTO SchemeLine (descrip, discount, idScheme) values('"+description+"',"+discount+","+idScheme+")";
        String queryGetId="SELECT MAX(idSchemeLine) FROM SchemeLine";
        ResultSet result=null;
        
        con.openConnection();
        con.noReturnQuery(queryInsert);
        result=con.getResultSet(queryGetId);
        try {
            result.next();
            idSchemeLine=result.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(SchemeLine.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    /**
    * Gets the schemeLine from the Database and create an instance of that schemeLine.
    * @param id The idSchemeLine of the schemeLine to get from DB
    */
    protected void getFromDB (int id){
        
        ConnectionDB con=new ConnectionDB();
        String query="SELECT * FROM SchemeLine WHERE idSchemeLine="+id;
        ResultSet result=null;
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            if(result.next()){
                idSchemeLine=result.getInt(1);
                description=result.getString(2);
                discount=result.getDouble(3);
                idScheme=result.getInt(4);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SchemeLine.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    /**
    * Cleans the table SchemeLine in the DB.
    */
    public static void deleteAllFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM SchemeLine";
        
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
    }
    
    /**
    * Deletes this schemeLine instance from the DB. 
    */
    public void deleteFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM SchemeLine WHERE idSchemeLine="+idSchemeLine;
                
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
    }
    
    /**
    * Updates this schemeline instance in the DB.
    * @param field The field to update
    * @param newValue The new value for the field to update
    */
    public void updateDB(String field, String newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE SchemeLine SET "+field+"= '"+newValue+"' WHERE idSchemeLine="+idSchemeLine;
        
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
        getFromDB(idSchemeLine);
    }
    
    public void updateDB(String field, int newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE SchemeLine SET "+field+"= "+newValue+" WHERE idSchemeLine="+idSchemeLine;
        
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
        getFromDB(idSchemeLine);
    }

    public void updateDB(String field, double newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE SchemeLine SET "+field+"= "+newValue+" WHERE idSchemeLine="+idSchemeLine;
        
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
        getFromDB(idSchemeLine);
    }
        
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the discount
     */
    public double getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    /**
     * @return the idSchemeLine
     */
    public int getIdSchemeLine() {
        return idSchemeLine;
    }

    /**
     * @param id the id to set
     */
    public void setIdSchemeLine(int id) {
        this.idSchemeLine = id;
    }
    
    public void setIdScheme(int id){
        this.idScheme=id;
    }
    
    public int getIdScheme(){
        return this.idScheme;
    }
    
}
