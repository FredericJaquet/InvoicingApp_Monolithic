/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invoicingapp_monolithic;

import com.invoicingapp.bbdd.ConnectionDB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frede
 */
public class BankAccount {
    
    private int idBankAccount,idCompany;
    private String iban,swift,holder,branch;
    
    public BankAccount(){};

    public void addToDB(){
        ConnectionDB con=new ConnectionDB();
        String query="INSERT INTO BankAccount(iban,swift,holder,branch,idCompany) VALUES('"+iban+"','"+swift+"','"+holder+"','"+branch+"',"+idCompany+")";
        String queryGetId="SELECT MAX(idBankAccount) FROM BankAccount";
        ResultSet result=null;
        
        con.openConnection();
        con.executeUpdate(query);
        result=con.getResultSet(queryGetId);
        try{
            result.next();
            idBankAccount=result.getInt(1);
        }catch (SQLException ex) {
            Logger.getLogger(BankAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
    }
    
    public void getFromDB(int id){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT * FROM BankAccount WHERE idBankAccount="+id;
        ResultSet result=null;
        
        con.openConnection();
        result=con.getResultSet(query);
        try{
            if(result.next()){
                idBankAccount=result.getInt(1);
                iban=result.getString(2);
                swift=result.getString(3);
                holder=result.getString(4);
                branch=result.getString(5);
                idCompany=result.getInt(6);
            }
        }catch (SQLException ex) {
            Logger.getLogger(BankAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    public static void deleteAllFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM BankAccount";
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();    
    }
    
    public void deleteFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM BankAccount WHERE idBankAccount="+idBankAccount;
                
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection();
    }
    
    /**
     * @return the idBankAccount
     */
    public int getIdBankAccount() {
        return idBankAccount;
    }

    /**
     * @param idBankAccount the idBankAccount to set
     */
    public void setIdBankAccount(int idBankAccount) {
        this.idBankAccount = idBankAccount;
    }

    /**
     * @return the idCompany
     */
    public int getIdCompany() {
        return idCompany;
    }

    /**
     * @param idCompany the idCompany to set
     */
    public void setIdCompany(int idCompany) {
        this.idCompany = idCompany;
    }

    /**
     * @return the iban
     */
    public String getIban() {
        return iban;
    }

    /**
     * @param iban the iban to set
     */
    public void setIban(String iban) {
        this.iban = iban;
    }

    /**
     * @return the swift
     */
    public String getSwift() {
        return swift;
    }

    /**
     * @param swift the swift to set
     */
    public void setSwift(String swift) {
        this.swift = swift;
    }

    /**
     * @return the holder
     */
    public String getHolder() {
        return holder;
    }

    /**
     * @param holder the holder to set
     */
    public void setHolder(String holder) {
        this.holder = holder;
    }

    /**
     * @return the branch
     */
    public String getBranch() {
        return branch;
    }

    /**
     * @param branch the branch to set
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }
    
    
    
}
