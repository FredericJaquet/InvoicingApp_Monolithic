/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invoicingapp_monolithic;

import com.invoicingapp.bbdd.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frede
 */
public class Scheme {
    
    private String name, sourceLanguage, targetLanguage, field, units;
    private double price;
    private int idScheme, idCustomProv;
    private ArrayList<SchemeLine> lines=new ArrayList();
    
    public Scheme(){}
    
    public Scheme(String name, double price, String sourceLanguage, String targetLanguage, String field, String units){
        this.field=field;
        this.name=name;
        this.price=price;
        this.sourceLanguage=sourceLanguage;
        this.targetLanguage=targetLanguage;
        this.units=units;
    }
    
     /**
    * Adds this schem instance to the Database.
    * Sets the idScheme from the new register in the DB
    * Sets idScheme of the shcemeLine to "this.idSchem" and adds the schemeLines to the DB.
    */
    public void addToDB(){
        ConnectionDB con=new ConnectionDB();
        String queryInsert="INSERT INTO Scheme (schemeName, price, units, fieldName, sourceLanguage, targetLanguage) values('"+name+"',"+price+",'"+units+"','"+field+"','"+sourceLanguage+"','"+targetLanguage+"'"+");";
        String queryinsertSchemeCustomProv;
        
        String queryGetId="SELECT MAX(idScheme) FROM Scheme";
        ResultSet result=null;
        
        con.openConnection();
        con.executeUpdate(queryInsert);
        result=con.getResultSet(queryGetId);
        try {
            result.next();
            idScheme=result.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        }
        queryinsertSchemeCustomProv="INSERT INTO SchemeCustomProv (idCustomProv, idScheme) VALUES("+idCustomProv+","+idScheme+");";
        con.executeUpdate(queryinsertSchemeCustomProv);
        
        con.closeConnection();
        
        for(int i=0;i<lines.size();i++){
            lines.get(i).setIdScheme(idScheme);
            lines.get(i).addToDB();
        }
    }
    
    /**
    * Gets the scheme from the Database and create an instance of that scheme.
    * Adds all the related lines to the lines list.
    * @param id The idScheme of the scheme to get from DB
    */
    public void getFromDB (int id){
        
        ConnectionDB con=new ConnectionDB();
        String query="SELECT * FROM Scheme WHERE idScheme="+id;
        String queryIdCustomProv;
        ResultSet result=null;
            
        con.openConnection();
        result=con.getResultSet(query);
    
        try {
            if(result.next()){
                idScheme=result.getInt(1);
                name=result.getString(2);
                price=result.getDouble(3);
                units=result.getString(4);
                field=result.getString(5);
                sourceLanguage=result.getString(6);
                targetLanguage=result.getString(7);
                getLinesFromDB();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        queryIdCustomProv="SELECT idCustomProv FROM SchemeCustomProv WHERE idScheme="+idScheme;
        
        result=con.getResultSet(queryIdCustomProv);
        try {
            if(result.next()){
                idCustomProv=result.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        con.closeConnection();
    }
    
    /**
    * Cleans the table Scheme in the DB.
    * Cleans the table SchemeLine.
    */
    public static void deleteAllFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Scheme";
        
        SchemeLine.deleteAllFromDB();
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
    }
    
    /**
    * Deletes this scheme instance and the related schemeLines instances from DB
    */
    public void deleteFromDB(){
        ConnectionDB con=new ConnectionDB();
        String queryScheme="DELETE FROM Scheme WHERE idScheme="+idScheme+";";
        String queryCustomProv="DELETE FROM SchemeCustomProv WHERE idScheme="+idScheme+";";
        String queryGetIdLine="SELECT idSchemeLine FROM SchemeLine WHERE idScheme="+idScheme+";";
        int idLine;
        SchemeLine line=new SchemeLine();
        ResultSet result=null;
        
        con.openConnection();
        
        result=con.getResultSet(queryGetIdLine);
        try {
            while(result.next()){
                idLine=result.getInt(1);
                line.getFromDB(idLine);
                line.deleteFromDB();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        con.executeUpdate(queryCustomProv);
        con.executeUpdate(queryScheme);
        con.closeConnection();
    }
    
    /**
    * Updates this scheme instance from the DB.
    * @param field The field to update
    * @param newValue The new value for the field to update
    */
    public void updateDB(String field, String newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Scheme SET "+field+"= '"+newValue+"' where idScheme="+idScheme;
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idScheme);
    }
    
    public void updateDB(String field, int newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Scheme SET "+field+"= "+newValue+" where idScheme="+idScheme;
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idScheme);
    }
        
    public void updateDB(String field, double newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Scheme SET "+field+"= "+newValue+" where idScheme="+idScheme;
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idScheme);
    }
    
    /**
     * Gets the schemeLines of this scheme instance and
     * add them to the lines list.
     */
    public void getLinesFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT idSchemeLine FROM SchemeLine WHERE idScheme="+idScheme;
        ResultSet result=null;
        int idLine;
        SchemeLine line=new SchemeLine();
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try{
            while(result.next()){
                line=new SchemeLine();
                idLine=result.getInt(1);
                line.getFromDB(idLine);
                lines.add(line);
            }
        }catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    /**
    * add the schemeLine to the lines list
    * @param line 
    */
    public void addLine(SchemeLine line){
        lines.add(line);
    }

    public static Map<Integer,String> getMap(int idCustomProv){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT Scheme.idScheme, Scheme.schemeName FROM Scheme JOIN SchemeCustomProv ON (Scheme.idScheme=SchemeCustomProv.idScheme) WHERE SchemeCustomProv.idCustomProv="+idCustomProv;
        ResultSet result=null;
        Map<Integer, String> mapScheme=new HashMap();
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            while(result.next()){
                mapScheme.put(result.getInt(1),result.getString(2));        
            }
        }catch (SQLException ex) {
            Logger.getLogger(Scheme.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
        return mapScheme;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the sourceLanguage
     */
    public String getSourceLanguage() {
        return sourceLanguage;
    }

    /**
     * @param sourceLanguage the sourceLanguage to set
     */
    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    /**
     * @return the targetLanguage
     */
    public String getTargetLanguage() {
        return targetLanguage;
    }

    /**
     * @param targetLanguage the targetLanguage to set
     */
    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    /**
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(String field) {
        this.field = field;
    }
    
    /**
     * @return the units
     */
    public String getUnits() {
        return units;
    }

    /**
     * @param units the field to set
     */
    public void setUnits(String units) {
        this.units = units;
    }

    /**
     * @return the idScheme
     */
    public int getIdScheme() {
        return idScheme;
    }

    /**
     * @param id the id to set
     */
    public void setIdScheme(int id) {
        this.idScheme = id;
    }

    /**
     * @return the lines
     */
    public ArrayList<SchemeLine> getLines() {
        return lines;
    }

    /**
     * @return the idCustomProv
     */
    public int getIdCustomProv() {
        return idCustomProv;
    }

    /**
     * @param idCustomProv the idCustomProv to set
     */
    public void setIdCustomProv(int idCustomProv) {
        this.idCustomProv = idCustomProv;
    }

}
