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
import java.util.Collections;
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
        
        con.openConnection();
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
        Collections.sort(customers);
    }
    
    public void getCustomersWithPendingOrdersFromDB(){
        boolean control=false;
        ArrayList<Customer>newCustomers=new ArrayList();
        ConnectionDB con=new ConnectionDB();
        String query="SELECT Customer.idCustomer FROM Customer JOIN CustomProv ON (Customer.idCustomProv=CustomProv.idCustomProv) JOIN Orders ON (CustomProv.idCustomProv=Orders.idCustomProv) AND Orders.billed=0 GROUP BY Customer.idCustomer;";
        ResultSet result=null;
        Customer customer=new Customer();
        InvoiceCustomer invoice=new InvoiceCustomer();
        
        con.openConnection();
        result=con.getResultSet(query);
        try{
            while(result.next()){
                int a=result.getInt(1);
                control=false;
                invoice=new InvoiceCustomer();
                invoice.setDocNumber("Pendientes");
                invoice.setDocDate(LocalDate.now());
                for(int i=0;i<customers.size();i++){
                    if(customers.get(i).getIdCustomer()==a){
                        control=true;
                        customers.get(i).getOrdersFromDB(2);
                        invoice.getOrders().addAll(customers.get(i).getOrders());
                        customers.get(i).addInvoice(invoice);
                    }
                }
                if(!control){
                    customer=new Customer();
                    customer.getFromDB(result.getInt(1));
                    customer.getOrdersFromDB(2);
                    invoice.getOrders().addAll(customer.getOrders());
                    customer.addInvoice(invoice);
                    newCustomers.add(customer);
                }                   
            }
            customers.addAll(newCustomers);
        } catch (SQLException ex) {
            Logger.getLogger(ReportInvoiceCustomer.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        Collections.sort(customers);
    }
    
    public double getTotalInvoices(){
        double totalInvoices=0;
        
        for (int i=0;i<customers.size();i++){
            for(int j=0;j<customers.get(i).getInvoices().size();j++){
                totalInvoices=totalInvoices+customers.get(i).getInvoices().get(j).getTotal();
            }
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

    /**
     * @return the customers
     */
    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    /**
     * @param customers the customers to set
     */
    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }

}
