/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invoicingapp_monolithic;

import java.time.LocalDate;

/**
 *
 * @author frede
 */
public class ReportInvoice {
    
    private LocalDate initialDate, finalDate;
    private String title;
    
    protected ReportInvoice(LocalDate initialDate, LocalDate finalDate){
        this.initialDate=initialDate;
        this.finalDate=finalDate;
    }
       
    protected void setdates(LocalDate initialDate, LocalDate finalDate){
        this.initialDate=initialDate;
        this.finalDate=finalDate;
    }
    
    protected LocalDate getInitialDate(){
        return initialDate;
    }
    
    protected LocalDate getFinalDate(){
        return finalDate;
    }
    
    protected void setTitle(String title){
            this.title=title;
    }
    
    protected String getTitle(){
        return title;
    }
}
