/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invoicingapp_monolithic;

/**
 *
 * @author frede
 */
public class Currency{
    private String symbol,abreviation;
    
    public Currency(String symbol, String abreviation){
        this.abreviation=abreviation;
        this.symbol=symbol;
    }

    @Override
    public String toString(){
        return symbol+" - "+abreviation;
    }
    
    /**
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * @return the abreviation
     */
    public String getAbreviation() {
        return abreviation;
    }

    /**
     * @param abreviation the abreviation to set
     */
    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
    }
    
}
