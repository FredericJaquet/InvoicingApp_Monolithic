/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invoicingapp_monolithic;

import java.util.ArrayList;

/**
 *
 * @author frede
 */
public class ReportPendingPerMonth {
    private ArrayList<InvoiceCustomer> invoices=new ArrayList();
    private int month, year;
    
    public ReportPendingPerMonth(int month, int year){
        this.month=month;
        this.year=year;
    }
    
    public void addInvoice(InvoiceCustomer invoice){
        invoices.add(invoice);
    }

    /**
     * @return the invoices
     */
    public ArrayList<InvoiceCustomer> getInvoices() {
        return invoices;
    }

    /**
     * @param invoices the invoices to set
     */
    public void setInvoices(ArrayList<InvoiceCustomer> invoices) {
        this.invoices = invoices;
    }

    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }
    



    
    
    
}
