/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invoicingapp_monolithic;

import com.invoicingapp.bbdd.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frede
 */
public class Orders {
    public static final int CUSTOMERS=1;
    public static final int PROVIDERS=2;
    private String description, units, fieldName, sourceLanguage, targetLanguage;
    private double pricePerUnit;
    private boolean billed;
    private boolean selected=false;
    private int idOrders, idCustomProv;
    private LocalDate dateOrder;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private ArrayList<Item> items=new ArrayList();
    
    public Orders(){}
    
    public Orders(String description, String units, String fieldName, String sourceLanguage, String targetLanguage, double pricePerUnit, boolean billed, LocalDate dateOrder){
        this.billed=billed;
        this.dateOrder=dateOrder;
        this.description=description;
        this.fieldName=fieldName;
        this.pricePerUnit=pricePerUnit;
        this.sourceLanguage=sourceLanguage;
        this.targetLanguage=targetLanguage;
        this.units=units;
    }
    
    public Orders(String description, String units, String fieldName, String sourceLanguage, String targetLanguage, double pricePerUnit, LocalDate dateOrder){
        this.billed=false;
        this.dateOrder=dateOrder;
        this.description=description;
        this.fieldName=fieldName;
        this.pricePerUnit=pricePerUnit;
        this.sourceLanguage=sourceLanguage;
        this.targetLanguage=targetLanguage;
        this.units=units;
    }
    
    /**
    * Adds this order instance to the Database.
    * Sets the idOrder from the new register in the DB
    * Sets idOrders of the items to "this.idOrders" and adds the items to the DB.
    */
    public void addToDB(){
        ConnectionDB con=new ConnectionDB();
        String queryInsert="INSERT INTO Orders (descrip, dateOrder, pricePerUnit, units, billed, fieldName, sourceLanguage, targetLanguage, idCustomProv) values('"+description+"','"+dateOrder.toString()+"',"+pricePerUnit+",'"+units+"',"+billed+",'"+fieldName+"','"+sourceLanguage+"','"+targetLanguage+"',"+idCustomProv+")";
        String queryGetId="SELECT MAX(idOrders) FROM Orders";
        ResultSet result=null;
        
        con.openConnection();
        con.executeUpdate(queryInsert);
        result=con.getResultSet(queryGetId);
        try {
            result.next();
            idOrders=result.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
        for(int i=0;i<items.size();i++){
            items.get(i).setIdOrders(idOrders);
            items.get(i).addToDB();//añadir el idOrders
        }
    }
    
    /**
    * Gets the order from the Database and create an instance of that order.
    * Adds all the related items to the items list.
    * @param id The idOrders of the order to get from DB
    */
    public void getFromDB (int id){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT * FROM Orders WHERE idOrders="+id;
        ResultSet result=null;
            
        con.openConnection();
        result=con.getResultSet(query);
    
        try {
            if(result.next()){
                idOrders=result.getInt(1);
                description=result.getString(2);
                dateOrder=LocalDate.parse(result.getString(3));
                pricePerUnit=result.getDouble(4);
                units=result.getString(5);
                billed=result.getBoolean(6);
                fieldName=result.getString(7);
                sourceLanguage=result.getString(8);
                targetLanguage=result.getString(9);
                idCustomProv=result.getInt(10);
                getItemsFromDB();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    public static ArrayList<Orders> getAllOrdersFromDB(){
        ArrayList<Orders> list=new ArrayList();
        ConnectionDB con=new ConnectionDB();
        String query="SELECT idOrders FROM Orders";
        ResultSet result=null;
        Orders order=new Orders();
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            while(result.next()){
                order=new Orders();
                order.getFromDB(result.getInt(1));
                list.add(order);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
        return list;
    }
    
    public static ArrayList<Orders> getAllOrdersFromDB(int CompanyKind){
        ArrayList<Orders> list=new ArrayList();
        ConnectionDB con=new ConnectionDB();
        String query="SELECT idOrders FROM Orders WHERE idCustomProv IN (SELECT idCustomProv FROM";
        ResultSet result=null;
        Orders order=new Orders();
        
        switch(CompanyKind){
            case(CUSTOMERS):query=query.concat(" Customer);");break;
            case(PROVIDERS):query=query.concat(" Provider);");break;
        }
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            while(result.next()){
                order=new Orders();
                order.getFromDB(result.getInt(1));
                list.add(order);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
        return list;
    }
    
    /**
    * Cleans the table Orders in the DB.
    * Cleans the table Item.
    */
    public static void deleteAllFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="delete from Orders";
        
        Item.deleteAllFromDB();
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
    }
    
    /**
    * Deletes this order instance and the related items instances from DB
    */
    public void deleteFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Orders WHERE idOrders="+idOrders;
        String queryDocumentOrders="DELETE FROM documentOrders WHERE idOrders="+idOrders;
        String queryGetIdItem="SELECT idItem FROM Item WHERE idOrders="+idOrders;
        int idItem;
        Item item=new Item();
        ResultSet result=null;
        
        con.openConnection();
        
        result=con.getResultSet(queryGetIdItem);
        try {
            while(result.next()){
                idItem=result.getInt(1);
                item.getFromDB(idItem);
                item.deleteFromDB();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        con.executeUpdate(queryDocumentOrders);
        con.executeUpdate(query);
        con.closeConnection();
    }
    
    /**
    * Updates this order instance from the DB.
    * @param field The field to update
    * @param newValue The new value for the field to update
    */
    public void updateDB(String field, String newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Orders SET "+field+"= '"+newValue+"' where idOrders="+idOrders;
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idOrders);
    }
    
    public void updateDB(String field, boolean newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Orders SET "+field+"= "+newValue+" where idOrders="+idOrders;
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idOrders);
    }
    
    public void updateDB(String field, double newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Orders SET "+field+"= "+newValue+" where idOrders="+idOrders;
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idOrders);
    }
    
    public void updateDB(String field, int newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Orders SET "+field+"= "+newValue+" where idOrders="+idOrders;
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idOrders);
    }
    
    /**
     * Gets the items of this order instance and
     * add them to the items list.
     */
    public void getItemsFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT idItem FROM Item WHERE Item.idOrders="+idOrders;
        ResultSet result=null;
        int idItem;
        Item item=new Item();
        
        con.openConnection();
        result=con.getResultSet(query);
        items.clear();
        try{
            while(result.next()){
                item=new Item();
                idItem=result.getInt(1);
                item.getFromDB(idItem);
                items.add(item);
            }
        }catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    public static Map<Integer,Orders> getMap(int idCustomProv){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT idOrders FROM Orders WHERE idCustomProv="+idCustomProv+" AND billed="+0;
        ResultSet result=null;
        Map<Integer,Orders> mapOrders=new HashMap();
        Orders order=new Orders();
        
        con.openConnection();
        result=con.getResultSet(query);
    
        try {
            while(result.next()){
                order=new Orders();
                order.getFromDB(result.getInt(1));
                mapOrders.put(result.getInt(1),order);
            }
        }catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
        return mapOrders;
    }
    
    public CustomProv getCompany(){
        CustomProv company=new CustomProv();
        company.getFromDB(idCustomProv);
        
        return company;        
    }
    
    public String getComName(){
        ConnectionDB con=new ConnectionDB();
        ResultSet result=null;
        String query="SELECT comName FROM Company JOIN CustomProv ON(Company.idCompany=CustomProv.idCompany) WHERE CustomProv.idCustomProv="+idCustomProv;
        String comName="";
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try{
            if(result.next()){
                comName=result.getString(1);
            }
        }catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
        return comName;
    }
    
    @Override
    public String toString(){
        return idOrders+" "+description+" "+dateOrder.toString();
    }
    /**
    * Gets the net total of this order instance
    * @return the net total for this order instance
    */
    public double getTotalOrder(){
        double totalOrder=0;
                 
        for(int i=0;i<items.size();i++){
            totalOrder=totalOrder+(items.get(i).getQuantity()*(1-(items.get(i).getDiscount()/100))*pricePerUnit);
        }
        
        return totalOrder;
    }
    
    public String getTotalString(){
        return String.format("%.2f€",getTotalOrder());
    }
    
    public double getQuantity(){
        double quantity=0;
        for(int i=0;i<items.size();i++){
            quantity=quantity+items.get(i).getQuantity();
        }
        return quantity;
    }
    
    /**
    * add the item to the items list
    * @param item 
    */
    public void addItem(Item item){
        items.add(item);
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
     * @return the units
     */
    public String getUnits() {
        return units;
    }

    /**
     * @param units the units to set
     */
    public void setUnits(String units) {
        this.units = units;
    }

    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @param fieldName the fieldName to set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
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
     * @return the pricePerUnit
     */
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    /**
     * @param pricePerUnit the pricePerUnit to set
     */
    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    /**
     * @return the billed
     */
    public boolean isBilled() {
        return billed;
    }

    /**
     * @param billed the billed to set
     */
    public void setBilled(boolean billed) {
        this.billed = billed;
    }

    /**
     * @return the items
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    /**
     * @return the idOrders
     */
    public int getIdOrders() {
        return idOrders;
    }

    /**
     * @param id the id to set
     */
    public void setIdOrders(int id) {
        this.idOrders = id;
    }

    /**
     * @return the idCustomProv
     */
    public int getIdCustomProv() {
        return idCustomProv;
    }

    /**
     * @param idCustomProv the idDocument to set
     */
    public void setIdCustomProv(int idCustomProv) {
        this.idCustomProv = idCustomProv;
    }

    /**
     * @return the dateOrder
     */
    public LocalDate getDateOrder() {
        return dateOrder;
    }
    
    public String getDateString(){
        return dateOrder.format(formatter);
    }

    /**
     * @param dateOrder the dateOrder to set
     */
    public void setDateOrder(LocalDate dateOrder) {
        this.dateOrder = dateOrder;
    }

    /**
     * @param items the items to set
     */
    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
}
