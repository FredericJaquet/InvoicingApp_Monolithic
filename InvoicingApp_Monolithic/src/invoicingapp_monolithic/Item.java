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
public class Item {
    
    private String description;
    private double discount, quantity;
    private int idItem, idOrders;
    
    public Item(){}
    
    public Item(String description, double quantity, double discount){
        this.description=description;
        this.discount=discount;
        this.quantity=quantity;
    }
    
    /**
    * Adds this item instance to the Database.
    * Sets the idItem from the new register in the DB
    */
    protected void addToDB(){
        ConnectionDB con=new ConnectionDB();
        String queryInsert="INSERT INTO Item (descrip, qty, discount, idOrders) values('"+description+"',"+quantity+","+discount+","+idOrders+")";
        String queryGetId="SELECT MAX(idItem) FROM Item";
        ResultSet result=null;
        
        con.openConnection();
        con.noReturnQuery(queryInsert);
        result=con.getResultSet(queryGetId);
        try {
            result.next();
            idItem=result.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    /**
    * Gets the item from the Database and create an instance of that item.
    * @param id The idItem of the item to get from DB
    */
    protected void getFromDB (int id){
        
        ConnectionDB con=new ConnectionDB();
        String query="SELECT * FROM Item WHERE idItem="+id;
        ResultSet result=null;
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            if(result.next()){
                idItem=result.getInt(1);
                description=result.getString(2);
                quantity=result.getDouble(3);
                discount=result.getDouble(4);
                idOrders=result.getInt(5);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    /**
    * Cleans the table Item in the DB.
    */
    public static void deleteAllFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Item";
        
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
    }
    
    /**
    * Deletes this item instance from the DB. 
    */
    public void deleteFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Item WHERE idItem="+idItem;
                
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
    }
    
    /**
    * Updates this item instance in the DB.
    * @param field The field to update
    * @param newValue The new value for the field to update
    */
    public void updateDB(String field, String newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Item SET "+field+"= '"+newValue+"' WHERE idItem="+idItem;
        
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
        getFromDB(idItem);
    }
    
    public void updateDB(String field, int newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Item SET "+field+"= "+newValue+" WHERE idItem="+idItem;
        
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
        getFromDB(idItem); 
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
     * @return the quantity
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the id
     */
    public int getIdItem() {
        return idItem;
    }

    /**
     * @param id the id to set
     */
    public void setIdItem(int id) {
        this.idItem = id;
    }

    /**
     * @return the idOrders
     */
    public int getIdOrders() {
        return idOrders;
    }

    /**
     * @param idOrders the idOrders to set
     */
    public void setIdOrders(int idOrders) {
        this.idOrders = idOrders;
    }
    
}
