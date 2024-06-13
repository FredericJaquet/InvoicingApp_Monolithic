/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invoicingapp_monolithic;

import com.invoicingapp.bbdd.ConnectionDB;
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
public class ReportInvoiceCustomer extends ReportInvoice{
    
    private ArrayList<Customer> customers=new ArrayList();
    
    public ReportInvoiceCustomer(LocalDate initialDate, LocalDate finalDate){
        super(initialDate,finalDate);
    }
    
    public void getCustomersFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT Customer.idCustomer FROM Customer JOIN CustomProv ON (Customer.idCustomProv=CustomProv.idCustomProv) JOIN Orders ON (CustomProv.idCustomProv=Orders.idCustomProv) JOIN DocumentOrders ON (Orders.idOrders=DocumentOrders.idOrders) JOIN Document ON(DocumentOrders.idDocument=Document.idDocument) WHERE (docDate BETWEEN '"+getInitialDate().toString()+"' AND '"+getFinalDate().toString()+"') AND Orders.billed=1 GROUP BY Customer.idCustomer;";
        ResultSet result=null;
        Customer customer=new Customer();
        
        result=con.getResultSet(query);
        try{
            while(result.next()){
                customer=new Customer();
                customer.getFromDB(result.getInt(1));
                customer.getInvoicesFromDB(getInitialDate(), getFinalDate());
                customers.add(customer);                
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReportInvoiceCustomer.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    public double getTotalInvoices(){
        double totalInvoices=0;
        
        for (int i=0;i<customers.size();i++){
            totalInvoices=totalInvoices+customers.get(i).getTotalCustomProv(getInitialDate(), getFinalDate());
        }
    
        return totalInvoices;
    }
    
    public double getTotalVAT(){
        double totalVAT=0;
        
        for (int i=0;i<customers.size();i++){
            totalVAT=totalVAT+customers.get(i).getTotalVATCustomer();
        }
        
        return totalVAT;
    }
    
    public double getTotalWithholding(){
        
        double totalWithholding=0;
        
        for (int i=0;i<customers.size();i++){
            totalWithholding=totalWithholding+customers.get(i).getTotalWithholdingCustomer();
        }
        
        return totalWithholding;
    }
    
    public Customer getCustomer(int i){
        return customers.get(i);
    }
    
    public void addCustomer(Customer customer){
        customers.add(customer);
    }

}
