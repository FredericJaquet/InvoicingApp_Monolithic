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
public class Provider extends CustomProv{ 
    
    private int idProvider;
    private ArrayList<InvoiceProvider> invoicesProvider=new ArrayList();
    private ArrayList<PurchaseOrder> pos=new ArrayList();
    
    public Provider(){}
    
    public Provider(String street, String stNumber, String apt, String cp, String city, String state, String country, String vatNumber, String comName, String legalName, String email, String web, double defaultVAT, double defaultRetention, boolean enabled, boolean europe){
        super(street, stNumber, apt, cp, city, state, country, vatNumber, comName, legalName, email, web, defaultVAT, defaultRetention, enabled, europe);
    }
    
    /**
    * Adds this provider instance to the Database.
    * Sets the idProvider from the new register in the DB
    */
    @Override
    public void addToDB(){
        ConnectionDB con=new ConnectionDB();
        String queryInsert;
        String queryGetId="SELECT MAX(idProvider) FROM Provider";
        ResultSet result=null;
        
        super.addToDB();
        
        queryInsert="INSERT INTO Provider(idCustomProv) VALUES("+getIdCustomProv()+")";
        
        con.openConnection();
        con.executeUpdate(queryInsert);
        result=con.getResultSet(queryGetId);
        try {
            result.next();
            idProvider=result.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    /**
    * Gets the provider from the Database and create an instance of that provider.
    * @param id The idProvider of the provider to get from DB
    */
    @Override
    public void getFromDB (int id){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT * FROM Provider WHERE idProvider="+id;
        ResultSet result=null;
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            if(result.next()){
                idProvider=result.getInt(1);
                super.getFromDB(result.getInt(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    public static ArrayList<Provider> getAllProvidersFromDB(){
        ArrayList<Provider> list=new ArrayList();
        String query="SELECT idProvider FROM Provider;";
        ConnectionDB con=new ConnectionDB();
        ResultSet result=null;
        Provider provider=new Provider();
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            while(result.next()){
                provider=new Provider();
                provider.getFromDB(result.getInt(1));
                list.add(provider);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public static ArrayList<Provider> getAllProvidersFromDB(int enabled){
        ArrayList<Provider> list=new ArrayList();
        String query="SELECT idProvider FROM Provider";
        ConnectionDB con=new ConnectionDB();
        ResultSet result=null;
        Provider provider=new Provider();
        
        switch(enabled){
            case(1):query=query.concat(" JOIN CustomProv ON (Provider.idCustomProv=CustomProv.idCustomProv) WHERE enabled=true;");break;
            case(2):query=query.concat(" JOIN CustomProv ON (Provider.idCustomProv=CustomProv.idCustomProv) WHERE enabled=false;");break;
            case(3):query=query.concat(";");
        }
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            while(result.next()){
                provider=new Provider();
                provider.getFromDB(result.getInt(1));
                list.add(provider);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    /**
    * Cleans the table Provider in the DB.
    */
    public static void deleteAllFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Provider";
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
    }
    
    /**
    * Deletes this provider instance from the DB. 
    */
    @Override
    public void deleteFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM Provider WHERE idProvider="+idProvider;
                
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
    }
    
    /**
    * Updates this provider instance from the DB.
    * @param field The field to update
    * @param newValue The new value for the field to update
    */
    @Override
    public void updateDB(String field, String newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Provider SET "+field+"= '"+newValue+"' WHERE idProvider="+idProvider;
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
            con.executeUpdate(query);
            con.closeConnection();
            getFromDB(idProvider);
        }
    }
    
    @Override
    public void updateDB(String field, int newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE Provider SET "+field+"= "+newValue+" WHERE idProvider="+idProvider;
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
            con.executeUpdate(query);
            con.closeConnection();
            getFromDB(idProvider);
        }
    }
    
    /**
    * Gets a Map of idCustomer and comName of all Customers from DB
     * @return 
    */
    public static Map<Integer,Provider> getMap(){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT Provider.idProvider FROM Provider;";
        ResultSet result=null;
        Map<Integer, Provider> mapProviders=new HashMap();
        Provider provider=new Provider();
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            while(result.next()){
                provider=new Provider();
                provider.getFromDB(result.getInt(1));
                mapProviders.put(result.getInt(1),provider);        
            }
        }catch (SQLException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
        return mapProviders;
    }
    
    /**
    * Gets the VAT total of the invoices for this provider instance for 
    * the time frame strating on the initialDate and ending on the finalDate
    * @return the VAT total
    */
    public double getTotalVATProvider(){
        double totalVAT=0;
        
        for(int i=0;i<invoicesProvider.size();i++){
            totalVAT=totalVAT+invoicesProvider.get(i).getTotalVAT();
        }
        return totalVAT;
    }
    
    /**
    * Gets the total of Withholding of all the invoices for this provider instance for 
    * the time frame starting on the initialDate and ending on the finalDate
    * @return The total of withholding
    */
    public double getTotalWithholdingProvider(){
        double totalWithholding=0;
        
        for(int i=0;i<invoicesProvider.size();i++){
            totalWithholding=totalWithholding+invoicesProvider.get(i).getTotalWithholding();
        }
        return totalWithholding;
    }
    
    /**
    * gets all the invoices of this provider instance for the time frame 
    * starting on the initialDate and ending on the finalDate.
    * Adds the invoices to the invoices list
    * @param initialDate
    * @param finalDate 
    */
    public void getInvoicesFromDB(LocalDate initialDate, LocalDate finalDate){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT InvoiceProvider.idInvoiceProvider FROM InvoiceProvider JOIN Document ON (Document.idDocument = InvoiceProvider.idDocument) JOIN DocumentOrders ON (Document.idDocument=DocumentOrders.idDocument) JOIN Orders ON (DocumentOrders.idOrders=Orders.idOrders) WHERE Orders.idCustomProv="+getIdCustomProv()+" AND (Document.docDate BETWEEN '"+initialDate.toString()+"' AND '"+finalDate.toString()+"') GROUP BY InvoiceProvider.idInvoiceProvider;";
        InvoiceProvider invoiceProvider = new InvoiceProvider();
        ResultSet result=null;
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            while(result.next()){
                invoiceProvider.getFromDB(result.getInt(1));
                invoicesProvider.add(invoiceProvider);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    /**
    * gets all the invoices of this customer
    */
    public void getInvoicesFromDB(){ 
        ConnectionDB con=new ConnectionDB();
        String query="SELECT InvoiceProvider.idInvoiceProvider FROM InvoiceProvider JOIN Document ON (Document.idDocument = InvoiceProvider.idDocument) JOIN DocumentOrders ON (Document.idDocument=DocumentOrders.idDocument) JOIN Orders ON (DocumentOrders.idOrders=Orders.idOrders) WHERE Orders.idCustomProv="+getIdCustomProv()+" GROUP BY idInvoiceProvider;";
        InvoiceProvider invoiceProvider= new InvoiceProvider();
        ResultSet result=null;
        con.openConnection();
        result=con.getResultSet(query);
        invoicesProvider.clear();
        try {
            while(result.next()){
                invoiceProvider=new InvoiceProvider();
                invoiceProvider.getFromDB(result.getInt(1));
                invoicesProvider.add(invoiceProvider);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
    * gets all the quotes of this customer instance
    */
    public void getPOsFromDB(){ 
        ConnectionDB con=new ConnectionDB();
        String query="SELECT PurchaseOrder.idPurchaseOrder FROM PurchaseOrder JOIN Document ON (Document.idDocument = PurchaseOrder.idDocument) JOIN DocumentOrders ON (Document.idDocument=DocumentOrders.idDocument) JOIN Orders ON (DocumentOrders.idOrders=Orders.idOrders) WHERE Orders.idCustomProv="+getIdCustomProv()+" GROUP BY idQuotes";
        PurchaseOrder po= new PurchaseOrder();
        ResultSet result=null;
        con.openConnection();
        result=con.getResultSet(query);
        
        pos.clear();
        try {
            while(result.next()){
                po=new PurchaseOrder();
                po.getFromDB(result.getInt(1));
                pos.add(po);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Provider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<InvoiceProvider> getInvoices(){
        return invoicesProvider;
    }
    
    /**
     * @return the idProvider
     */
    public int getIdProvider() {
        return idProvider;
    }

    /**
     * @param id the id to set
     */
    public void setIdProvider(int id) {
        this.idProvider = id;
    }

}
