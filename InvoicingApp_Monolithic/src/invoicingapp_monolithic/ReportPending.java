/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invoicingapp_monolithic;

import com.invoicingapp.bbdd.ConnectionDB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frede
 */
public class ReportPending {
    private ArrayList<InvoiceCustomer> invoices=new ArrayList();
    private ArrayList<ReportPendingPerMonth> invoicesPerMonth=new ArrayList();
    
    public ReportPending(){
        getAllUnpaid();
        getInvoicesPerMonth();
    }
    
    private void getAllUnpaid(){
        ConnectionDB con=new ConnectionDB();
        ResultSet result=null;
        String query="SELECT idInvoiceCustomer FROM InvoiceCustomer WHERE paid=0";
        InvoiceCustomer invoice;
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try{
            while(result.next()){
                invoice=new InvoiceCustomer();
                invoice.getFromDB(result.getInt(1));
                invoice.setComparableValueToDuedate();
                invoices.add(invoice);
            }
        }catch (SQLException ex) {
            Logger.getLogger(ReportPending.class.getName()).log(Level.SEVERE, null, ex);
        }
        Collections.sort(invoices);
    }
    
    private void getInvoicesPerMonth(){
        ReportPendingPerMonth report=null;
        int month,year;
        
        for(int i=0;i<invoices.size();i++){
            month=invoices.get(i).getDuedate().getMonthValue();
            year=invoices.get(i).getDuedate().getYear();
            if(i==0){
                report=new ReportPendingPerMonth(month,year);
                report.addInvoice(invoices.get(i));
            }else{
                if((year==invoices.get(i-1).getDuedate().getYear())&&(month==invoices.get(i-1).getDuedate().getMonthValue())){
                    report.addInvoice(invoices.get(i));
                }else{
                    invoicesPerMonth.add(report);
                    report=new ReportPendingPerMonth(month,year);
                    report.addInvoice(invoices.get(i));
                }
            }
        }
    }
    
    private double getNetTotal(){
        double total=0;
        
        for(int i=0; i<invoices.size();i++){
            total=total+invoices.get(i).getTotal();
        }
        
        return total;
    }
    
    private double getTotalToPay(){
        double total=0;
        
        for(int i=0; i<invoices.size();i++){
            total=total+invoices.get(i).getTotalToPay();
        }
        
        return total;
    }
    
}
