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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frede
 */
public class Customer extends CustomProv {
    
    private String invoicingMethod, payMethod;
    private int duedate;
    private int idCustomer;
    private ArrayList<InvoiceCustomer> invoicesCustomer=new ArrayList();
    
    public Customer(){}
    
    public Customer(String street, String stNumber, String apt, String cp, String city, String state, String country, String vatNumber, String comName, String legalName, String email, String web, double defaultVAT, double defaultRetention, boolean enabled, boolean europe, String invoicingMethod, int duedate, String payMethod, int idCustomProv){
        super(street, stNumber, apt, cp, city, state, country, vatNumber, comName, legalName, email, web, defaultVAT, defaultRetention, enabled, europe);
        this.duedate=duedate;
        this.invoicingMethod=invoicingMethod;
        this.payMethod=payMethod;
        setIdCustomProv(idCustomProv);
    }
    
    /**
    * Adds this customer instance to the Database.
    * Sets the idCustomer from the new register in the DB
    */
    @Override
    public void addToDB(){
        ConnectionDB con=new ConnectionDB();
        String queryInsert;
        String queryGetId="SELECT MAX(idCustomer) FROM Customer";
        ResultSet result=null;
        
        super.addToDB();
        
        queryInsert="INSERT INTO Customer (invoicingMethod, dueDate, payMethod, idCustomProv) values('"+invoicingMethod+"',"+duedate+",'"+payMethod+"',"+getIdCustomProv()+")";
        
        con.openConnection();
        con.noReturnQuery(queryInsert);
        result=con.getResultSet(queryGetId);
        try {
            result.next();
            idCustomer=result.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    /**
    * Gets the customer from the Database and create an instance of that customer.
    * @param id The idCustomer of the customer to get from DB
    */
    @Override
    public void getFromDB (int id){
        
        ConnectionDB con=new ConnectionDB();
        String query="SELECT * FROM Customer WHERE idCustomer="+id;
        ResultSet result=null;
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            if(result.next()){
                idCustomer=result.getInt(1);
                invoicingMethod=result.getString(2);
                duedate=result.getInt(3);
                payMethod=result.getString(4);
                super.getFromDB(result.getInt(5));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    /**
    * Cleans the table Customer in the DB.
    */
    public static void deleteAllFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Customer";
        
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();    
    }
    
    /**
    * Deletes this customer instance from the DB. 
    */
    @Override
    public void deleteFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Customer WHERE idCustomer="+idCustomer;
                
        con.openConnection();
        con.noReturnQuery(query);
        con.closeConnection();
    }
    
    /**
    * Updates this customer instance in the DB.
    * @param field The field to update
    * @param newValue The new value for the field to update
    */
    @Override
    public void updateDB(String field, String newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Customer SET "+field+"= '"+newValue+"' WHERE idCustomer="+idCustomer;
        String message="Si modificas esta cuenta, se modificarán sus datos en todos los documents anteriores. Quieres continuar S/N";
        String answer;
        Boolean confirmation=false;
        Scanner scan=new Scanner(System.in);
        
        System.out.println(message);
        answer=scan.nextLine();
        
        if(answer.equals("S")){
            confirmation=true;            
        }
        
        if(confirmation){
            con.openConnection();
            con.noReturnQuery(query);
            con.closeConnection();
            getFromDB(idCustomer);
        }
    }
    
    @Override
    public void updateDB(String field, int newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Customer SET "+field+"= "+newValue+" WHERE idCustomer="+idCustomer;
        String message="Si modificas esta cuenta, se modificarán sus datos en todos los documents de esta cuenta anteriores. Quieres continuar S/N";
        String answer;
        Boolean confirmation=false;
        Scanner scan=new Scanner(System.in);
        
        System.out.println(message);
        answer=scan.nextLine();
        
        if(answer.equals("S")){
            confirmation=true;            
        }
        
        if(confirmation){
            con.openConnection();
            con.noReturnQuery(query);
            con.closeConnection();
            getFromDB(idCustomer);
        }
    }
    
    /**
    * Gets a Map of idCustomer and comName of all Customers from DB
    */
    public static Map<Integer,Customer> getMap(){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT Customer.idCustomer FROM Customer;";
        ResultSet result=null;
        Map<Integer, Customer> mapCustomers=new HashMap();
        Customer customer=new Customer();
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            while(result.next()){
                customer=new Customer();
                customer.getFromDB(result.getInt(1));
                mapCustomers.put(result.getInt(1),customer);        
            }
        }catch (SQLException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
        return mapCustomers;
    }
    
    /**
    * Gets the VAT total of all the invoices for this customer instance for 
    * the time frame strating on the initialDate and ending on the finalDate
    * @return the VAT total
    */
    public double getTotalVATCustomer(){
        double totalVAT=0;
        
        for(int i=0;i<invoicesCustomer.size();i++){
            totalVAT=totalVAT+invoicesCustomer.get(i).getTotalVAT();
        }

        return totalVAT;
    }
    
    /**
    * Gets the total of Withholding of all the invoices for this customer instance for 
    * the time frame starting on the initialDate and ending on the finalDate
    * @return the total of Withholding 
    */
    public double getTotalWithholdingCustomer(){
        double totalWithholding=0;
        
        for(int i=0;i<invoicesCustomer.size();i++){            
            totalWithholding=totalWithholding+invoicesCustomer.get(i).getTotalWithholding();
        }
        return totalWithholding;
    }
    
    /**
    * gets all the invoices of this customer instance for the time frame 
    * starting on the initialDate and ending on the finalDate.
    * @param initialDate
    * @param finalDate 
    */
    public void getInvoicesFromDB(LocalDate initialDate, LocalDate finalDate){ 
        ConnectionDB con=new ConnectionDB();
        String query="SELECT InvoiceCustomer.idInvoiceCustomer FROM InvoiceCustomer JOIN Document ON (Document.idDocument = InvoiceCustomer.idDocument) JOIN DocumentOrders ON (Document.idDocument=DocumentOrders.idDocument) JOIN Orders ON (DocumentOrders.idOrders=Orders.idOrders) WHERE Orders.idCustomProv="+getIdCustomProv()+" AND (Document.docDate BETWEEN '"+initialDate.toString()+"' AND '"+finalDate.toString()+"') GROUP BY InvoiceCustomer.idInvoiceCustomer;";
        InvoiceCustomer invoiceCustomer= new InvoiceCustomer();
        ResultSet result=null;
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            while(result.next()){
                invoiceCustomer.getFromDB(result.getInt(1));
                invoicesCustomer.add(invoiceCustomer);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the invoicingMethod
     */
    public String getInvoicingMethod() {
        return invoicingMethod;
    }

    /**
     * @param invoicingMethod the invoicingMethod to set
     */
    public void setInvoicingMethod(String invoicingMethod) {
        this.invoicingMethod = invoicingMethod;
    }

    /**
     * @return the payMethod
     */
    public String getPayMethod() {
        return payMethod;
    }

    /**
     * @param payMethod the payMethod to set
     */
    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    /**
     * @return the duedate
     */
    public int getDuedate() {
        return duedate;
    }

    /**
     * @param duedate the duedate to set
     */
    public void setDuedate(int duedate) {
        this.duedate = duedate;
    }

    /**
     * @return the id
     */
    public int getIdCustomer() {
        return idCustomer;
    }

    /**
     * @param id the id to set
     */
    public void setIdCustomer(int id) {
        this.idCustomer = id;
    }
    
}
