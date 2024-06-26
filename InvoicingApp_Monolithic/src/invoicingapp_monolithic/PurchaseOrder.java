/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invoicingapp_monolithic;

import com.invoicingapp.bbdd.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frede
 */
public class PurchaseOrder extends Document{
    
    private int idPurchaseOrder;
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
        
        queryInsert="INSERT INTO PurchaseOrder (deadline, idDocument) values('"+deadline.toString()+"',"+getIdDocument()+")";
        con.openConnection();
        con.noReturnQuery(queryInsert);
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
                super.getFromDB(result.getInt(3));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        queryGetIdProvider="SELECT Provider.idProvider FROM Provider JOIN CustomProv ON (CustomProv.idCustomProv=Provider.idCustomProv) WHERE CustomProv.idCustomprov="+getOrders().get(1).getIdCustomProv();
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
    
    /**
    * Cleans the table PurchaseOrder in the DB.
    */
    public static void deleteAllFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM PurchaseOrder";
        
        con.openConnection();
        con.noReturnQuery(query);
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
        con.noReturnQuery(query);
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
        con.noReturnQuery(query);
        con.closeConnection();
        getFromDB(idPurchaseOrder);   
    }
    
    @Override
    public void updateDB(String field, int newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE PurchaseOrder SET "+field+"= "+newValue+" WHERE idPurchaseOrder="+idPurchaseOrder;
        
        System.out.println(query);
        
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
        getFromDB(idPurchaseOrder);
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
     * @param idProvider of the Provider to set
     */
    public void setProvider(int idProvider) {
        provider.getFromDB(idProvider);
    }
    
}
