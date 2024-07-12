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
public class InvoiceProvider extends Document{
    private double withholding;
    private int idInvoiceProvider;
    private Provider provider=new Provider();
    private BankAccount bankAccount=new BankAccount();
    
    public InvoiceProvider(){}
    
    public InvoiceProvider(String docNumber, String title, double vat, LocalDate docDate, double retention){
        super(docNumber, title, vat, docDate);
        this.withholding=retention;
    }
     
    /**
    * Adds this invoiceProvider instance to the Database.
    * Sets the idInvoiceProvider from the new register in the DB
    */
    @Override
    public void addToDB(){
        ConnectionDB con=new ConnectionDB();
        String queryInsert;
        String queryGetId="SELECT MAX(idInvoiceProvider) FROM InvoiceProvider";
        ResultSet result=null;
        
        super.addToDB();
        
        queryInsert="INSERT INTO InvoiceProvider (withholding, idDocument,idBankAccount) values("+withholding+","+getIdDocument()+","+bankAccount.getIdBankAccount()+")";
        con.openConnection();
        con.noReturnQuery(queryInsert);
        result=con.getResultSet(queryGetId);
        try {
            result.next();
            idInvoiceProvider=result.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
        setOrdersToBilled();
    }
    
    /**
    * Gets the invoiceProvider from the Database and create an instance of that invoiceProvider.
    * Gets the related document and customer instance from the DB.
    * @param id The idProvider of the provider to get from DB
    */
    @Override
    public void getFromDB (int id){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT * FROM InvoiceProvider WHERE idInvoiceProvider="+id;
        String queryGetIdProvider;
        ResultSet result=null;
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            if(result.next()){
                idInvoiceProvider=result.getInt(1);
                withholding=result.getDouble(2);
                super.getFromDB(result.getInt(3));
                bankAccount.getFromDB(result.getInt(4));
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceProvider.class.getName()).log(Level.SEVERE, null, ex);
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
    * Cleans the table InvoiceProvider in the DB.
    */
    public static void deleteAllFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM InvoiceProvider";
        
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
    }
    
    /**
    * Deletes this invoiceProvider instance from the DB.
    */
    @Override
    public void deleteFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM InvoiceProvider WHERE idInvoiceProvider="+idInvoiceProvider;
                
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
    }
    
    /**
    * Updates this invoiceProvider from the DB.
    * @param field The field to update
    * @param newValue The new value for the field to update
    */
    @Override
    public void updateDB(String field, String newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE InvoiceProvider SET "+field+"= '"+newValue+"' WHERE idInvoiceProvider="+idInvoiceProvider;
        
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
        getFromDB(idInvoiceProvider);
    }
    
    @Override
    public void updateDB(String field, int newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE InvoiceProvider SET "+field+"= "+newValue+" WHERE idInvoiceProvider="+idInvoiceProvider;
        
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
        getFromDB(idInvoiceProvider); 
    }
    
    public void updateDB(String field, double newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE InvoiceProvider SET "+field+"= "+newValue+" WHERE idInvoiceProvider="+idInvoiceProvider;
        
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
        getFromDB(idInvoiceProvider); 
    }
    
    public double getTotalWithholdingInvoiceProvider(){
        double totalWithholdingInvoiceProvider=0;
        totalWithholdingInvoiceProvider=getTotal()*withholding/100;
        return totalWithholdingInvoiceProvider;
    }

    /**
     * @return the withholding
     */
    public double getWithholding() {
        return withholding;
    }

    /**
     * @param withholding the withholding to set
     */
    public void setWithholding(double withholding) {
        this.withholding = withholding;
    }

    /**
     * @return the id
     */
    public int getIdInvoiceProvider() {
        return idInvoiceProvider;
    }

    /**
     * @param id the id to set
     */
    public void setIdInvoiceProvider(int id) {
        this.idInvoiceProvider = id;
    }

    /**
     * @return the provider
     */
    public Provider getProvider() {
        return provider;
    }

    /**
     * @param provider the provider to set
     */
    public void setProvider(Provider provider) {
        this.provider=provider;
    }

    /**
     * @return the bankAccount
     */
    public BankAccount getBankAccount() {
        return bankAccount;
    }

    /**
     * @param bankAccount the bankAccount to set
     */
    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
     
}
