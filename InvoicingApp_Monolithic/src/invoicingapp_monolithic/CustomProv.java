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
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author frede
 */
public class CustomProv extends Company{
    public static int ENABLED=1;
    public static int DISABLED=2;
    public static int BILLED=1;
    public static int NOTBILLED=2;
    public static int ALL=3;
    private double defaultVAT, defaultWithholding;
    private boolean enabled, europe;
    private int idCustomProv;
    private ArrayList<Scheme> schemes=new ArrayList();
    private ArrayList<Orders> orders=new ArrayList();
    
    public CustomProv(){}
    
    public CustomProv(String street, String stNumber, String apt, String cp, String city, String state, String country, String vatNumber, String comName, String legalName, String email, String web, double defaultVAT, double defaultRetention, boolean enabled, boolean europe){
        super(street, stNumber, apt, cp, city, state, country, vatNumber, comName, legalName, email, web);
        this.defaultWithholding=defaultRetention;
        this.defaultVAT=defaultVAT;
        this.enabled=enabled;
        this.europe=europe;
    }
    
    public CustomProv(String street, String stNumber, String apt, String cp, String city, String state, String country, String vatNumber, String comName, String legalName, String email, String web, double defaultVAT, double defaultRetention, boolean enabled, boolean europe, int idCompany){
        super(street, stNumber, apt, cp, city, state, country, vatNumber, comName, legalName, email, web);
        this.defaultWithholding=defaultRetention;
        this.defaultVAT=defaultVAT;
        this.enabled=enabled;
        this.europe=europe;
        setIdCompany(idCompany);
    }
    
    public CustomProv(String street, String stNumber, String apt, String cp, String city, String state, String country, String vatNumber, String comName, String legalName, String email, String web, double defaultVAT, double defaultRetention, boolean europe){
        super(street, stNumber, apt, cp, city, state, country, vatNumber, comName, legalName, email, web);
        this.defaultWithholding=defaultRetention;
        this.defaultVAT=defaultVAT;
        this.enabled=true;
        this.europe=europe;
    }
    
    /**
    * Adds this customProv instance to the Database.
    * Sets the idCustomProv from the new register in the DB
    */
    @Override
    protected void addToDB(){
        ConnectionDB con=new ConnectionDB();
        String queryInsert;
        String queryGetId="SELECT MAX(idCustomProv) FROM CustomProv";
        ResultSet result=null;
        
        super.addToDB();
        
        queryInsert="INSERT INTO CustomProv (defaultVAT, defaultWithholding, europe, enabled, idCompany) values("+defaultVAT+","+defaultWithholding+","+europe+","+enabled+","+getIdCompany()+")";
        
        con.openConnection();
        con.executeUpdate(queryInsert);
        result=con.getResultSet(queryGetId);
        try {
            result.next();
            idCustomProv=result.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(CustomProv.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
        for(int i=0;i<schemes.size();i++){
            schemes.get(i).setIdCustomProv(idCustomProv);
            schemes.get(i).addToDB();
        }
        
    }
    
    /**
    * Gets the customProv from the Database and create an instance of that customProv.
    * @param id The idCustomProv of the customProv to get from DB
    */
    @Override
    protected void getFromDB (int id){
        ConnectionDB con=new ConnectionDB();
        String query="SELECT * FROM customProv WHERE idCustomProv="+id;
        ResultSet result=null;
          
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            if(result.next()){
                idCustomProv=result.getInt(1);
                defaultVAT=result.getDouble(2);
                defaultWithholding=result.getDouble(3);
                europe=result.getBoolean(4);
                enabled=result.getBoolean(5);
                super.getFromDB(result.getInt(6));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomProv.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
        getSchemesFromDB();
    }
    
    public static ArrayList<CustomProv> getAllCustomProvFromDB(){
        ArrayList<CustomProv> list=new ArrayList();
        String query="SELECT idCustomProv FROM CustomProv;";
        ConnectionDB con=new ConnectionDB();
        ResultSet result=null;
        CustomProv customProv=new CustomProv();
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            while(result.next()){
                customProv=new CustomProv();
                customProv.getFromDB(result.getInt(1));
                list.add(customProv);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomProv.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
        Collections.sort(list);
        
        return list;
    }
    
    public static ArrayList<CustomProv> getAllCustomProvFromDB(int enabled){
        ArrayList<CustomProv> list=new ArrayList();
        String query="SELECT idCustomProv FROM CustomProv";
        ConnectionDB con=new ConnectionDB();
        ResultSet result=null;
        CustomProv customProv=new CustomProv();
        
        switch(enabled){
            case(1):query=query.concat(" WHERE enabled=true;");break;
            case(2):query=query.concat(" WHERE enabled=false;");break;
            case(3):query=query.concat(";");
        }
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            while(result.next()){
                customProv=new CustomProv();
                customProv.getFromDB(result.getInt(1));
                list.add(customProv);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomProv.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
        Collections.sort(list);
        
        return list;
    }
    
    public ArrayList<Orders> getOrdersFromDB(){
        String query="SELECT idOrders FROM Orders WHERE idCustomProv="+idCustomProv;
        ConnectionDB con=new ConnectionDB();
        ResultSet result=null;
        Orders order=new Orders();
        
        orders.clear();
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            while(result.next()){
                order=new Orders();
                order.getFromDB(result.getInt(1));
                orders.add(order);
            }
        }catch (SQLException ex) {
            Logger.getLogger(CustomProv.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
        return orders;
    }
    
    public ArrayList<Orders> getOrdersFromDB(int billed){
        String query="SELECT idOrders FROM Orders WHERE idCustomProv="+idCustomProv;
        ConnectionDB con=new ConnectionDB();
        ResultSet result=null;
        Orders order=new Orders();
        
        orders.clear();
        
        switch(billed){
            case(1):query=query.concat(" AND billed=true;");break;
            case(2):query=query.concat(" AND billed=false;");break;
            case(3):query=query.concat(";");
        }
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            while(result.next()){
                order=new Orders();
                order.getFromDB(result.getInt(1));
                orders.add(order);
            }
        }catch (SQLException ex) {
            Logger.getLogger(CustomProv.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        con.closeConnection();
        return orders;
    }
    
    /**
    * Cleans the table CustomProv in the DB.
    * Cleans the table Customer in the DB.
    * Cleans the table Provider in the DB.
    */
    public static void deleteAllFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM CustomProv";
                       
        Customer.deleteAllFromDB();
        Provider.deleteAllFromDB();
        
        con.openConnection();
        con.executeUpdate(query);
        con.closeConnection(); 
    }
    
    /**
    * Deletes this customProv instance and the related customer and provider
    * instances from the DB. 
    */
    @Override
    public void deleteFromDB(){
        ConnectionDB con=new ConnectionDB();
        String query="DELETE FROM CustomProv WHERE idCustomProv="+idCustomProv;
        String queryGetCustomerId="SELECT idCustomer FROM Customer WHERE idCustomProv="+idCustomProv;
        String queryGetIdProvider="SELECT idProvider FROM Provider WHERE idCustomProv="+idCustomProv;
        int idCustomer, idProvider;
        Customer customer=new Customer();
        Provider provider=new Provider();
        ResultSet result=null;
        
        con.openConnection();
        
        result=con.getResultSet(queryGetCustomerId);
        
        try {
            while(result.next()){
                idCustomer=result.getInt(1);
                customer.getFromDB(idCustomer);
                customer.deleteFromDB();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomProv.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        result=con.getResultSet(queryGetIdProvider);
        try {
            while(result.next()){
                idProvider=result.getInt(1);
                provider.getFromDB(idProvider);
                provider.deleteFromDB();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomProv.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        con.executeUpdate(query);
        con.closeConnection();
    }
    
    /**
    * Updates this customProv instance in the DB.
    * @param field The field to update
    * @param newValue The new value for the field to update
    */
    @Override
    public void updateDB(String field, String newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE CustomProv SET "+field+"= '"+newValue+"' WHERE idCustomProv="+idCustomProv;
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
            getFromDB(idCustomProv);
        }
    }
    
    @Override
    public void updateDB(String field, int newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE CustomProv SET "+field+"= "+newValue+" WHERE idCustomProv="+idCustomProv;
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
            getFromDB(idCustomProv);
        }
    }
    
    public void updateDB(String field, double newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE CustomProv SET "+field+"= "+newValue+" WHERE idCustomProv="+idCustomProv;
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
            getFromDB(idCustomProv);
        }
    }
    
    public void updateDB(String field, boolean newValue){
        ConnectionDB con=new ConnectionDB();
        String query="UPDATE CustomProv SET "+field+"= "+newValue+" WHERE idCustomProv="+idCustomProv;
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
            getFromDB(idCustomProv);
        }
    }

    public double getTotalCustomProv(LocalDate initialDate, LocalDate finalDate){
        ConnectionDB con=new ConnectionDB();
        double totalCustomprov=0;
        String query="SELECT Orders.idOrders FROM Orders JOIN DocumentOrders ON (DocumentOrders.idOrders=Orders.idOrders) JOIN Document ON(DocumentOrders.idDocument=Document.idDocument) WHERE Orders.idCustomProv="+idCustomProv+" AND (Document.docDate BETWEEN "+initialDate.toString()+" AND "+finalDate.toString()+") AND Orders.billed=1;";
        ResultSet result=null;
        Orders order=new Orders();
        
        con.openConnection();
        result=con.getResultSet(query);
        try{
            while(result.next()){
                order=new Orders();
                order.getFromDB(result.getInt(1));
                totalCustomprov=totalCustomprov+order.getTotalOrder();
            }
        }catch(SQLException ex){
            Logger.getLogger(CustomProv.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
        
        return totalCustomprov;
    }
    
    /**
    * add the scheme to the list schemes
    * @param scheme 
    */
    public void addScheme(Scheme scheme){
        scheme.setIdCustomProv(idCustomProv);
        schemes.add(scheme);
    }
    
    public void getSchemesFromDB(){
        ConnectionDB con=new ConnectionDB();
        ResultSet result=null;
        String query="SELECT idScheme FROM SchemeCustomProv WHERE idCustomProv="+idCustomProv;
        Scheme scheme=new Scheme();
        
        con.openConnection();
        result=con.getResultSet(query);
        
        try {
            while(result.next()){
                scheme=new Scheme();
                scheme.getFromDB(result.getInt(1));
                schemes.add(scheme);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomProv.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.closeConnection();
    }
    
    /**
     * @return the schemes list
     */
    public ArrayList<Scheme> getSchemes(){
        return schemes;
    }
    
    /**
     * @return the defaultVAT
     */
    public double getDefaultVAT() {
        return defaultVAT;
    }

    /**
     * @param defaultVAT the defaultVAT to set
     */
    public void setDefaultVAT(double defaultVAT) {
        this.defaultVAT = defaultVAT;
    }

    /**
     * @return the defaultWithholding
     */
    public double getDefaultWithholding() {
        return defaultWithholding;
    }

    /**
     * @param defaultwithholding the defaultWithholding to set
     */
    public void setDefaultWithholding(double defaultwithholding) {
        this.defaultWithholding = defaultwithholding;
    }

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the europe
     */
    public boolean isEurope() {
        return europe;
    }

    /**
     * @param europe the europe to set
     */
    public void setEurope(boolean europe) {
        this.europe = europe;
    }

    /**
     * @return the id
     */
    public int getIdCustomProv() {
        return idCustomProv;
    }

    /**
     * @param id the id to set
     */
    public void setIdCustomProv(int id) {
        this.idCustomProv = id;
    }

    /**
     * @return the orders
     */
    public ArrayList<Orders> getOrders() {
        return orders;
    }

    /**
     * @param orders the orders to set
     */
    public void setOrders(ArrayList<Orders> orders) {
        this.orders = orders;
    }

}
