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
    
    public static final int PENDING=0;
    public static final int ACCEPTED=1;
    public static final int REJECTED=2;
    private String noteDelivery, notePayment;
    private int idQuotes;
    private Customer customer=new Customer();
    private boolean accepted;
    private String currency;
    private String comName;
    
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
        
        queryInsert="INSERT INTO Quotes (noteDelivery, notePayment, currency, accepted, idDocument) values('"+noteDelivery+"','"+notePayment+"','"+currency+"',"+accepted+","+getIdDocument()+")";
        con.openConnection();
        con.noReturnQuery(queryInsert);
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
                currency=result.getString(4);
                accepted=result.getBoolean(5);
                super.getFromDB(result.getInt(6));
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
        String query="SELECT idQuotes FROM Quotes WHERE accepted="+accepted;
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
        return list;
    }
    
    /**
    * Cleans the table Quotes in the DB.
    */
    public static void deleteAllFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Quotes";
        
        con.openConnection();
        con.noReturnQuery(query);
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
        con.noReturnQuery(query);
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
        con.noReturnQuery(query);
        con.closeConnection();
        getFromDB(idQuotes);
    }
    
    @Override
    public void updateDB(String field, int newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Quotes SET "+field+"= "+newValue+" WHERE idQuotes="+idQuotes;
        
        con.openConnection();
        con.noReturnQuery(query);
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
     * @return the accepted
     */
    public boolean isAccepted() {
        return accepted;
    }

    /**
     * @param accepted the accepted to set
     */
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
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
     * @return the comName
     */
    public String getComName() {
        return comName;
    }
    
    public void setComName(){
        comName=customer.getComName();
    }
    
}
