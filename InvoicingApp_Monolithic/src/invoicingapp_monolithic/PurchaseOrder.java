/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invoicingapp_monolithic;

import com.invoicingapp.bbdd.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frede
 */
public class PurchaseOrder extends Document{
    
    private int idPurchaseOrder;
    private int status=0;
    private String comName;
    private LocalDate deadline;
    private Provider provider=new Provider();
    
    
    public PurchaseOrder(){}
    
    public PurchaseOrder(String docNumber, String title, double vat, LocalDate docDate, LocalDate deadline){
        super(docNumber, title, vat, docDate);
        this.deadline=deadline;
    }
    
    /**
    * Adds this purchaseOrder instance to the Database.
    * Sets the idPurchaseOrder from the new register in the DB
    */
    @Override
    public void addToDB(){
        ConnectionDB con=new ConnectionDB();
        String queryInsert;
        String queryGetId="SELECT MAX(idPurchaseOrder) FROM PurchaseOrder";
        ResultSet result=null;
        
        super.addToDB();
        
        queryInsert="INSERT INTO PurchaseOrder (deadline, status, idDocument) values('"+deadline.toString()+"',"+status+","+getIdDocument()+")";
        con.openConnection();
        con.executeUpdate(queryInsert);
        result=con.getResultSet(queryGetId);
        try {
            result.next();
            idPurchaseOrder=result.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        con.closeConnection();
    }
    
    /**
    * Gets the purchaseOrder from the Database and create an instance of that purchaseorder.
    * Gets the related document and provider instance from the DB.
    * @param id The idPurchaseOrder of the purchaseOrder to get from DB
    */
    @Override
    public void getFromDB (int id){
        
        ConnectionDB con=new ConnectionDB();
        String query="SELECT * FROM PurchaseOrder WHERE idPurchaseOrder="+id;
        String queryGetIdProvider;
        ResultSet result=null;
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            if(result.next()){
                idPurchaseOrder=result.getInt(1);
                deadline=LocalDate.parse(result.getString(2));
                status=result.getInt(3);
                super.getFromDB(result.getInt(4));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        queryGetIdProvider="SELECT Provider.idProvider FROM Provider JOIN CustomProv ON (CustomProv.idCustomProv=Provider.idCustomProv) WHERE CustomProv.idCustomprov="+getOrders().get(0).getIdCustomProv();
        result=con.getResultSet(queryGetIdProvider);
        try{
            if(result.next()){
                provider.getFromDB(result.getInt(1));
            }
        }catch (SQLException ex) {
            Logger.getLogger(InvoiceCustomer.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    public static ArrayList<PurchaseOrder> getAllPOsFromDB(int accepted){
        ArrayList<PurchaseOrder> list=new ArrayList();
        String query="SELECT idPurchaseOrder FROM PurchaseOrder WHERE status="+accepted;
        ConnectionDB con=new ConnectionDB();
        ResultSet result=null;
        PurchaseOrder po=new PurchaseOrder();
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            while(result.next()){
                po=new PurchaseOrder();
                po.getFromDB(result.getInt(1));
                list.add(po);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public static ArrayList<PurchaseOrder> getAllPOsFromDB(){
        ArrayList<PurchaseOrder> list=new ArrayList();
        String query="SELECT idPurchaseOrder FROM PurchaseOrder";
        ConnectionDB con=new ConnectionDB();
        ResultSet result=null;
        PurchaseOrder po=new PurchaseOrder();
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            while(result.next()){
                po=new PurchaseOrder();
                po.getFromDB(result.getInt(1));
                list.add(po);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    /**
    * Cleans the table PurchaseOrder in the DB.
    */
    public static void deleteAllFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM PurchaseOrder";
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
    }
    
    /**
    * Deletes this purchaseOrder instance from the DB. 
    */
    @Override
    public void deleteFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM PurchaseOrder WHERE idPurchaseOrder="+idPurchaseOrder;
                
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
    }
    
    /**
     * Updates this purchaseOrder in the DB.
     * @param field
     * @param newValue
     */
    @Override
    public void updateDB(String field, String newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE PurchaseOrder SET "+field+"= '"+newValue+"' WHERE idPurchaseOrder="+idPurchaseOrder;
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idPurchaseOrder);   
    }
    
    @Override
    public void updateDB(String field, int newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE PurchaseOrder SET "+field+"= "+newValue+" WHERE idPurchaseOrder="+idPurchaseOrder;
        
        System.out.println(query);
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idPurchaseOrder);
    }
    
    public static String getLastPONumber(){
        ConnectionDB con=new ConnectionDB();
        ResultSet result=null;
        String lastNumber="";
        String query="SELECT docNumber "
                + "FROM document JOIN PurchaseOrder "
                + "ON(document.idDocument=PurchaseOrder.idDocument) "
                + "WHERE docDate=(SELECT docDate FROM Document "
                + "JOIN PurchaseOrder ON(Document.idDocument=PurchaseOrder.idDocument)"
                + "ORDER BY docDate DESC LIMIT 1);";
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            if(result.next()){
                lastNumber=result.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceCustomer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lastNumber;
    }

    /**
     * @return the idPurchaseOrder
     */
    public int getIdPurchaseOrder() {
        return idPurchaseOrder;
    }

    /**
     * @param idPurchaseOrder the idPurchaseOrder to set
     */
    public void setIdPurchaseOrder(int idPurchaseOrder) {
        this.idPurchaseOrder = idPurchaseOrder;
    }

    /**
     * @return the deadeline
     */
    public LocalDate getDeadline() {
        return deadline;
    }

    /**
     * @param deadline the deadeline to set
     */
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    /**
     * @return the Provider
     */
    public Provider Provider() {
        return provider;
    }

    /**
     * @param provider the Provider to set
     */
    public void setProvider(Provider provider) {
        this.provider=provider;
    }
    
    /**
     * @return the comName
     */
    @Override
    public String getComName() {
        return comName;
    }
    
    @Override
    public void setComName(){
        comName=provider.getComName();
    }
    
}
