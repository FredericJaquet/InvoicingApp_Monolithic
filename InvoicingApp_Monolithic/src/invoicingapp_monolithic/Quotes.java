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
public class Quotes extends Document{
    
    private int idQuotes, status;
    private String noteDelivery, notePayment;
    private String comName;
    private Customer customer=new Customer();
    
    public Quotes(){}
    
    public Quotes(String docNumber, String title, double vat, LocalDate docDate, String noteDelivery, String notePayment){
        super(docNumber, title, vat, docDate);
        this.noteDelivery=noteDelivery;
        this.notePayment=notePayment;
    }
    
    /**
    * Adds this quotes instance to the Database.
    * Sets the idQuotes from the new register in the DB
    */
    @Override
    public void addToDB(){
        ConnectionDB con=new ConnectionDB();
        String queryInsert;
        String queryGetId="SELECT MAX(idQuotes) FROM Quotes";
        ResultSet result=null;
        
        super.addToDB();
        
        queryInsert="INSERT INTO Quotes (noteDelivery, notePayment, status, idDocument) values('"+noteDelivery+"','"+notePayment+"',"+status+","+getIdDocument()+")";
        con.openConnection();
        con.executeUpdate(queryInsert);
        result=con.getResultSet(queryGetId);
        try {
            result.next();
            idQuotes=result.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Quotes.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    /**
    * Gets the quotes from the Database and create an instance of that quotes.
    * Gets the related document and customer instance from the DB.
    * @param id The idQuotes of the quotes to get from DB
    */
    @Override
    public void getFromDB (int id){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT * FROM Quotes WHERE idQuotes="+id;
        String queryGetIdCustomer;
        ResultSet result=null;
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            if(result.next()){
                idQuotes=result.getInt(1);
                noteDelivery=result.getString(2);
                notePayment=result.getString(3);
                status=result.getInt(4);
                super.getFromDB(result.getInt(5));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Quotes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        queryGetIdCustomer="SELECT Customer.idCustomer FROM Customer JOIN CustomProv ON (CustomProv.idCustomProv=Customer.idCustomProv) WHERE CustomProv.idCustomprov="+getOrders().get(0).getIdCustomProv();
        result=con.getResultSet(queryGetIdCustomer);
        try{
            if(result.next()){
                customer.getFromDB(result.getInt(1));
            }
        }catch (SQLException ex) {
            Logger.getLogger(InvoiceCustomer.class.getName()).log(Level.SEVERE, null, ex);
        }  
        con.closeConnection();
    }
    
    public static ArrayList<Quotes> getAllQuotesFromDB(int accepted){
        ArrayList<Quotes> list=new ArrayList();
        String query="SELECT idQuotes FROM Quotes WHERE status="+accepted;
        ConnectionDB con=new ConnectionDB();
        ResultSet result=null;
        Quotes quote=new Quotes();
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            while(result.next()){
                quote=new Quotes();
                quote.getFromDB(result.getInt(1));
                list.add(quote);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Quotes.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
        return list;
    }
    
    public static ArrayList<Quotes> getAllQuotesFromDB(){
        ArrayList<Quotes> list=new ArrayList();
        String query="SELECT idQuotes FROM Quotes";
        ConnectionDB con=new ConnectionDB();
        ResultSet result=null;
        Quotes quote=new Quotes();
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            while(result.next()){
                quote=new Quotes();
                quote.getFromDB(result.getInt(1));
                list.add(quote);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Quotes.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
        return list;
    }
    
    /**
    * Cleans the table Quotes in the DB.
    */
    public static void deleteAllFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Quotes";
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        
    }
    
    /**
    * Deletes this quotes instance from the DB. 
    */
    @Override
    public void deleteFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Quotes WHERE idQuotes="+idQuotes;
                
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
    }
    
    /**
     * Updates this quotes in the DB.
     * @param field
     * @param newValue
     */
    @Override
    public void updateDB(String field, String newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Quotes SET "+field+"= '"+newValue+"' WHERE idQuotes="+idQuotes;
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idQuotes);
    }
    
    @Override
    public void updateDB(String field, int newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Quotes SET "+field+"= "+newValue+" WHERE idQuotes="+idQuotes;
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
        getFromDB(idQuotes);
    }

    public static String getLastQuoteNumber(){
        ConnectionDB con=new ConnectionDB();
        ResultSet result=null;
        String lastNumber="";
        String query="SELECT docNumber "
                + "FROM document JOIN Quotes "
                + "ON(document.idDocument=Quotes.idDocument) "
                + "WHERE docDate=(SELECT docDate FROM Document "
                + "JOIN Quotes ON(Document.idDocument=Quotes.idDocument)"
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
        con.closeConnection();
        
        return lastNumber;
    }
    
    /**
     * @return the noteDelivery
     */
    public String getNoteDelivery() {
        return noteDelivery;
    }

    /**
     * @param noteDelivery the noteDelivery to set
     */
    public void setNoteDelivery(String noteDelivery) {
        this.noteDelivery = noteDelivery;
    }

    /**
     * @return the notePayment
     */
    public String getNotePayment() {
        return notePayment;
    }

    /**
     * @param notePayment the notePayment to set
     */
    public void setNotePayment(String notePayment) {
        this.notePayment = notePayment;
    }

    /**
     * @return the idQuotes
     */
    public int getIdQuotes() {
        return idQuotes ;
    }

    /**
     * @param idQuotes the id to set
     */
    public void setIdQuotes(int idQuotes) {
        this.idQuotes = idQuotes;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the idCustomer of the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer= customer;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
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
        comName=customer.getComName();
    }
    
}
