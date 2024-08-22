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
public class ReportInvoiceProvider extends ReportInvoice{
    
    private ArrayList<Provider> providers=new ArrayList();
    
    public ReportInvoiceProvider(LocalDate initialDate, LocalDate finalDate){
        super(initialDate,finalDate);
    }
    
    public void getProvidersFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT Provider.idProvider FROM Provider JOIN CustomProv ON (Provider.idCustomProv=CustomProv.idCustomProv) JOIN Orders ON (CustomProv.idCustomProv=Orders.idCustomProv) JOIN DocumentOrders ON (Orders.idOrders=DocumentOrders.idOrders) JOIN Document ON(DocumentOrders.idDocument=Document.idDocument) WHERE (docDate BETWEEN '"+getInitialDate().toString()+"' AND '"+getFinalDate().toString()+"') AND Orders.billed=1 GROUP BY Provider.idProvider";
        ResultSet result=null;
        Provider provider=new Provider();
        
        con.openConnection();
        result=con.getResultSet(query);
        try{
            while(result.next()){
                provider=new Provider();
                provider.getFromDB(result.getInt(1));
                provider.getInvoicesFromDB(getInitialDate(), getFinalDate());
                providers.add(provider);                
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReportInvoiceProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        Collections.sort(providers);
    }
    
    public void getProvidersWithPendingOrdersFromDB(){
        boolean control=false;
        ArrayList<Provider>newProviders=new ArrayList();
        ConnectionDB con=new ConnectionDB();
        String query="SELECT Customer.idCustomer FROM Customer JOIN CustomProv ON (Customer.idCustomProv=CustomProv.idCustomProv) JOIN Orders ON (CustomProv.idCustomProv=Orders.idCustomProv) AND Orders.billed=0 GROUP BY Customer.idCustomer;";
        ResultSet result=null;
        Provider provider=new Provider();
        InvoiceProvider invoice=new InvoiceProvider();
        
        con.openConnection();
        result=con.getResultSet(query);
        try{
            while(result.next()){
                int a=result.getInt(1);
                control=false;
                invoice=new InvoiceProvider();
                invoice.setDocNumber("Pendientes");
                invoice.setDocDate(LocalDate.now());
                for(int i=0;i<providers.size();i++){
                    if(providers.get(i).getIdProvider()==a){
                        control=true;
                        providers.get(i).getOrdersFromDB(2);
                        invoice.getOrders().addAll(providers.get(i).getOrders());
                        providers.get(i).addInvoice(invoice);
                    }
                }
                if(!control){
                    provider=new Provider();
                    provider.getFromDB(result.getInt(1));
                    provider.getOrdersFromDB(2);
                    invoice.getOrders().addAll(provider.getOrders());
                    provider.addInvoice(invoice);
                    newProviders.add(provider);
                }                   
            }
            providers.addAll(newProviders);
        } catch (SQLException ex) {
            Logger.getLogger(ReportInvoiceProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        Collections.sort(providers);
    }
    
    public double getTotalInvoices(){
        double totalInvoices=0;
        
        for (int i=0;i<providers.size();i++){
            for(int j=0;j<providers.get(i).getInvoices().size();j++){
                totalInvoices=totalInvoices+providers.get(i).getInvoices().get(j).getTotal();
            }
        }
    
        return totalInvoices;
    }
    
    public double getTotalVAT(){
        double totalVAT=0;
        
        for (int i=0;i<providers.size();i++){
            totalVAT=totalVAT+providers.get(i).getTotalVATProvider();
        }
        
        return totalVAT;
    }
    
    public double getTotalWithholding(){
        double totalWithholding=0;
        
        for (int i=0;i<providers.size();i++){
            totalWithholding=totalWithholding+providers.get(i).getTotalWithholdingProvider();
        }
        
        return totalWithholding;
    }
    
    public Provider getProvider(int i){
        return providers.get(i);
    }
    
    public void addProvider(Provider provider){
        providers.add(provider);
    }

    /**
     * @return the providers
     */
    public ArrayList<Provider> getProviders() {
        return providers;
    }

    /**
     * @param providers the providers to set
     */
    public void setProviders(ArrayList<Provider> providers) {
        this.providers = providers;
    }

}
