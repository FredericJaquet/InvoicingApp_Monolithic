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
public class Document {
    public static final int PENDING=0;
    public static final int ACCEPTED=1;
    public static final int REJECTED=2;
    public static int PAID=1;
    public static int NOTPAID=2;
    public static int ALL=3;
    private String docNumber,language,totalString,currency,comName;
    private double vat;
    private int idDocument;
    private LocalDate docDate;
    private Users user=new Users();
    private Company company=new Company();
    private ChangeRate changeRate=new ChangeRate();
    private ArrayList<Orders> orders=new ArrayList();
    
    
    public Document(){
        changeRate.getFromDB(1);
    }
    
    public Document(String docNumber, String language, double vat, LocalDate docDate){
        this.docDate=docDate;
        this.docNumber=docNumber;
        this.language=language;
        this.vat=vat;
    }
    
    /**
    * Adds this document instance to the Database.
    * Sets the idDocument from the new register in the DB
    * Sets billed of the order to "true" and update the order in DB
    * Sets idDocument of the order to "this.idDocument" and update the order in the DB.
    */
    protected void addToDB(){
        ConnectionDB con=new ConnectionDB();
        String queryInsert="INSERT INTO Document (docNumber, docDate, language, vat, currency, idUsers, idChangeRate) values('"+docNumber+"','"+docDate.toString()+"','"+language+"',"+vat+",'"+currency+"',"+user.getIdUsers()+","+changeRate.getIdChangeRate()+")";
        String queryGetId="SELECT MAX(idDocument) FROM Document";
        String QueryDocumentOrders;
        ResultSet result=null;
        
        con.openConnection();
        con.executeUpdate(queryInsert);
        result=con.getResultSet(queryGetId);
        try {
            result.next();
            idDocument=result.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i=0;i<orders.size();i++){
            QueryDocumentOrders="INSERT INTO DocumentOrders(idDocument, idOrders) VALUES("+idDocument+","+orders.get(i).getIdOrders()+");";
            con.executeUpdate(QueryDocumentOrders);
        }
        con.closeConnection();
    }
    
    /**
    * Gets the document from the Database and create an instance of that document.
    * Create an user and a changeRate instance for this Document.
    * Adds all the related orders to the orders list.
    * @param id The idDocument of the document to get from DB
    */
    protected void getFromDB (int id){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT * FROM Document WHERE idDocument="+id;
        ResultSet result=null;
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            if(result.next()){
                idDocument=result.getInt(1);
                docNumber=result.getString(2);
                docDate=LocalDate.parse(result.getString(3));
                language=result.getString(4);
                vat=result.getDouble(5);
                currency=result.getString(6);
                user.setIdUsers(result.getInt(7));
                user.getFromDB(user.getIdUsers());
                changeRate.setIdChangeRate(result.getInt(8));
                changeRate.getFromDB(changeRate.getIdChangeRate());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        getOrdersFromDB();
    }
    
    /**
    * Cleans the table Document in the DB.
    * Cleans the table InvoiceCustomer in the DB.
    * Cleans the table InvoiceProvider in the DB.
    * Cleans the table Quotes in the DB.
    * Set all billed Orders to false
    */
    public static void deleteAllFromDB(){
        
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Document";
        String queryOrders="UPDATE Orders SET billed=false, idDocument=null";
                
        InvoiceCustomer.deleteAllFromDB();
        InvoiceProvider.deleteAllFromDB();
        Quotes.deleteAllFromDB();
        PurchaseOrder.deleteAllFromDB();
        
        con.openConnection();
        con.executeUpdate(query);
        con.executeUpdate(queryOrders);
        con.closeConnection();
    }
    
    /**
    * Deletes this document instance and the related invoiceCustomer, invoiceProvider
    * and quotes instances from the DB. 
    * Set billed of orders to "false" and update the orders in DB.
    * Set IdDocument of orders to "null" update the orders in DB.
    */
    public void deleteFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="delete from Document where idDocument="+idDocument;
        String queryGetIdInvoiceCustomer="SELECT idInvoiceCustomer FROM InvoiceCustomer WHERE idDocument="+idDocument;
        String queryGetIdInvoiceProvider="SELECT IdInvoiceProvider FROM InvoiceProvider WHERE idDocument="+idDocument;
        String queryGetIdQuotes="SELECT IdQuotes FROM Quotes WHERE idDocument="+idDocument;
        String queryGetIdPO="SELECT idPurchaseOrder FROM PurchaseOrder WHERE iDDocument="+idDocument;
        int idInvoiceCustomer, idInvoiceProvider, idQuotes, idPo;
        InvoiceCustomer invoiceCustomer=new InvoiceCustomer();
        InvoiceProvider invoiceProvider=new InvoiceProvider();
        Quotes quote=new Quotes();
        PurchaseOrder po=new PurchaseOrder();
        ResultSet result=null;
        
        con.openConnection();
                
        result=con.getResultSet(queryGetIdInvoiceCustomer);
        try {
            while(result.next()){
                idInvoiceCustomer=result.getInt(1);
                invoiceCustomer.getFromDB(idInvoiceCustomer);
                invoiceCustomer.deleteFromDB();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        result=con.getResultSet(queryGetIdInvoiceProvider);
        try {
            while(result.next()){
                idInvoiceProvider=result.getInt(1);
                invoiceProvider.getFromDB(idInvoiceProvider);
                invoiceProvider.deleteFromDB();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        result=con.getResultSet(queryGetIdQuotes);
        try {
            while(result.next()){
                idQuotes=result.getInt(1);
                quote.getFromDB(idQuotes);
                quote.deleteFromDB();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        result=con.getResultSet(queryGetIdPO);
        try {
            while(result.next()){
                idPo=result.getInt(1);
                po.getFromDB(idPo);
                po.deleteFromDB();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.executeUpdate(query);
        con.closeConnection();
        
        for(int i=0;i<orders.size();i++){
            orders.get(i).setBilled(false);
            //orders.get(i).setIdDocument(0);
            orders.get(i).updateDB("billed",false);
            orders.get(i).updateDB("idDocument",null);
        }
    }
    
    /**
    * Updates this document instance from the DB.
    * @param field The field to update
    * @param newValue The new value for the field to update
    */
    public void updateDB(String field, String newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Document SET "+field+"= '"+newValue+"' where idDocument="+idDocument;
                
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idDocument);
    }
    
    public void updateDB(String field, Double newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Document SET "+field+"= '"+newValue+"' where idDocument="+idDocument;
                
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idDocument);  
    }
    
    public void updateDB(String field, LocalDate newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Document SET "+field+"= '"+newValue+"' where idDocument="+idDocument;
                
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idDocument);
    }
    
    public void updateDB(String field, int newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Document SET "+field+"= '"+newValue+"' where idDocument="+idDocument;
                
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idDocument);   
    }
    
    /**
    * Gets the net total of this document instance
    * @return the net total for this document instance
    */
    public double getTotal(){
        double totalDocument=0;
        
        for(int i=0;i<orders.size();i++){
            totalDocument=totalDocument+orders.get(i).getTotalOrder();
        }
        
        return totalDocument;
    }
    
    public void setTotalString(){
        totalString=String.format("%.2f"+changeRate.getCurrency1(),getTotal());
    }
    
    public String getTotalString(){
        return totalString;
    }
    
    /**
    * Gets the VAT total of this document instance
    * @return the VAT total for this document instance
    */
    public double getTotalVAT(){
        double totalVATDocument=0;
        
        totalVATDocument=getTotal()*vat/100;
        
        return totalVATDocument;
    }
    
    /**
     * Gets the orders of this document instance and
     * add them to the orders list.
     */
    public void getOrdersFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT Orders.idOrders FROM Orders JOIN DocumentOrders ON(Orders.idOrders=DocumentOrders.idOrders) WHERE DocumentOrders.idDocument="+idDocument;
        ResultSet result=null;
        int idOrders;
        Orders order=new Orders();
        
        orders.clear();
        con.openConnection();
        result=con.getResultSet(query);
        
        try{
            while(result.next()){
                order=new Orders();
                idOrders=result.getInt(1);
                order.getFromDB(idOrders);
                orders.add(order);
            }
        }catch (SQLException ex) {
            Logger.getLogger(Orders.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void setOrdersToBilled(){
        for(int i=0;i<orders.size();i++){
            orders.get(i).setBilled(true);
            orders.get(i).updateDB("billed",true);
        }
    }
    
    /**
    * add the order to the orders list
    * @param order 
    */
    public void addOrder(Orders order){
        orders.add(order);
    }
    
    public void setOrders(ArrayList<Orders> orders){
        this.orders=orders;
    }
    
    /**
     * @return the orders list
     */
    public ArrayList<Orders> getOrders(){
        return orders;
    }
    
    /**
     * @return the docNumber
     */
    public String getDocNumber() {
        return docNumber;
    }

    /**
     * @param docNumber the docNumber to set
     */
    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the vat
     */
    public double getVat() {
        return vat;
    }

    /**
     * @param vat the vat to set
     */
    public void setVat(double vat) {
        this.vat = vat;
    }

    /**
     * @return the docDate
     */
    public LocalDate getDocDate() {
        return docDate;
    }

    /**
     * @param date the docDate to set
     */
    public void setDocDate(String date) {
        this.docDate = LocalDate.parse(date);
    }
    
    /**
     * @param date the docDate to set
     */
    public void setDocDate(LocalDate date) {
        this.docDate = date;
    }

    /**
     * @return the user
     */
    public Users getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(Users user) {
        this.user = user;
    }

    /**
     * @return the changeRate
     */
    public ChangeRate getChangeRate() {
        return changeRate;
    }

    /**
     * @param changeRate the changeRate to set
     */
    public void setChangeRate(ChangeRate changeRate) {
        this.changeRate = changeRate;
    }
    
    
    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return the idDocument
     */
    public int getIdDocument() {
        return idDocument;
    }

    /**
     * @param idDocument the id_Document to set
     */
    public void setIdDocument(int idDocument) {
        this.idDocument = idDocument;
    }
    
    public void setComName(){
        comName=company.getComName();
    }
     
    public String getComName(){
        return comName;
    }

    /**
     * @return the company
     */
    public Company getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(Company company) {
        this.company = company;
    }
    
}
