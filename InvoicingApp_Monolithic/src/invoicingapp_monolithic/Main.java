/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package invoicingapp_monolithic;

import com.invoicingapp.bbdd.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 *
 * @author frederic
 */
public class Main extends Application{


    @Override
    public void init(){
        Database.createUser();
        Database.createDataBase();
        Database.createTables();
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root= FXMLLoader.load(getClass().getResource("/com/invoicingapp/javafx/viewLogin.fxml"));
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void stop(){
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        launch();
        /*Database.createUser();
        Database.createDataBase();
        Database.createTables();
        
        Tests test=new Tests();
        
        test.start();*/
        
        //PHONE
        /*Phone phone= new Phone();
        String phoneNumber;
             
        System.out.println("Indica los detalles del teléfono:");
        System.out.println("phoneNumber:");
        phone.setPhoneNumber(scan.nextLine());
        System.out.println("Kind: ");
        phone.setKind(scan.nextLine());
        System.out.println("idCompany: ");
        phone.setIdCompany(scan.nextInt());
        scan.nextLine();
        
        phone.addToDB();
        
        System.out.println("El teléfono es (antes de insertar):");
        System.out.println(phone.getPhoneNumber()+" "
                +phone.getKind()+" "
                +phone.getIdCompany());
        
        phone.getFromDB("+32 92 454 831");
        
        System.out.println("El teléfono es (después de insertar):");
        System.out.println(phone.getPhoneNumber()+" "
                +phone.getKind()+" "
                +phone.getIdCompany());
        
        System.out.println("Qué teléfono quieres cambiar:");
        phoneNumber=scan.nextLine();

        System.out.println("Qué campo quieres cambiar:");
        field=scan.nextLine();
        System.out.println("Nuevo valor:");
        phone.getFromDB(phoneNumber);
        phone.updateDB(field,scan.nextLine());
        
        phone.getFromDB(phoneNumber);
        System.out.println("El teléfono es (antes de eliminar de insertar):");
        System.out.println(phone.getPhoneNumber()+" "
                +phone.getKind()+" "
                +phone.getIdCompany());
        
        System.out.println("Dale para continuar");
        scan.nextLine();
        
        phone.deleteFromDB();*/

        //PURCHASEORDER
        /*PurchaseOrder purchaseOrder= new PurchaseOrder();
             
        System.out.println("Indica los detalles del presupuesto:");
        System.out.println("deadline:");
        purchaseOrder.setDeadline(scan.nextInt());
        scan.nextLine();
        System.out.println("idDocument: ");
        purchaseOrder.setIdDocument(scan.nextInt());
        scan.nextLine();
        System.out.println("idProvider: ");
        purchaseOrder.setIdProvider(scan.nextInt());
        scan.nextLine();
        
        purchaseOrder.addToDB();
        
        System.out.println("El Orden de Pedido es (antes de insertar):");
        System.out.println(purchaseOrder.getIdPurchaseOrder()+" "
                +purchaseOrder.getDeadline()+" "
                +purchaseOrder.getIdDocument()+" "
                +purchaseOrder.getIdProvider());
        
        purchaseOrder.getFromDB(1);
        
        System.out.println("El Orden de Pedido es (después de insertar):");
        System.out.println(purchaseOrder.getIdPurchaseOrder()+" "
                +purchaseOrder.getDeadline()+" "
                +purchaseOrder.getIdDocument()+" "
                +purchaseOrder.getIdProvider());
        
        System.out.println("Qué Orden de Pedido quieres cambiar:");
        id=scan.nextInt();
        scan.nextLine();

        System.out.println("Qué campo quieres cambiar:");
        field=scan.nextLine();
        System.out.println("Nuevo valor:");
        purchaseOrder.getFromDB(id);
        purchaseOrder.updateDB(field,scan.nextLine());
        
        purchaseOrder.getFromDB(id);
        System.out.println("El Orden de Pedido es (antes de eliminar de insertar):");
        System.out.println(purchaseOrder.getIdPurchaseOrder()+" "
                +purchaseOrder.getDeadline()+" "
                +purchaseOrder.getIdDocument()+" "
                +purchaseOrder.getIdProvider());
        
        System.out.println("Dale para continuar");
        scan.nextLine();
        
        purchaseOrder.deleteFromDB();*/
        
        
        //QUOTES
        /*Quotes quotes= new Quotes();
             
        System.out.println("Indica los detalles del presupuesto:");
        System.out.println("Nota plazo de envio:");
        quotes.setNoteDelivery(scan.nextLine());
        System.out.println("Nota metodo pago: ");
        quotes.setNotePayment(scan.nextLine());
        System.out.println("idDocument: ");
        quotes.setIdDocument(scan.nextInt());
        scan.nextLine();
        System.out.println("idCustomer: ");
        quotes.setIdCustomer(scan.nextInt());
        scan.nextLine();
        
        quotes.addToDB();
        
        System.out.println("El presupuesto es (antes de insertar):");
        System.out.println(quotes.getIdQuotes()+" "
                +quotes.getNoteDelivery()+" "
                +quotes.getNotePayment()+" "
                +quotes.getIdDocument()+" "+
                +quotes.getIdCustomer());
        
        quotes.getFromDB(1);
        
        System.out.println("El presupuesto es (después de insertar):");
        System.out.println(quotes.getIdQuotes()+" "
                +quotes.getNoteDelivery()+" "
                +quotes.getNotePayment()+" "
                +quotes.getIdDocument()+" "+
                +quotes.getIdCustomer());
        
        System.out.println("Qué presupuesto quieres cambiar:");
        id=scan.nextInt();
        scan.nextLine();

        System.out.println("Qué campo quieres cambiar:");
        field=scan.nextLine();
        System.out.println("Nuevo valor:");
        quotes.getFromDB(id);
        quotes.updateDB(field,scan.nextLine());
        
        quotes.getFromDB(id);
        System.out.println("El presupuesto es (antes de eliminar de insertar):");
        System.out.println(quotes.getIdQuotes()+" "
                +quotes.getNoteDelivery()+" "
                +quotes.getNotePayment()+" "
                +quotes.getIdDocument()+" "+
                +quotes.getIdCustomer());
        
        System.out.println("Dale para continuar");
        scan.nextLine();
        
        quotes.deleteFromDB();*/
        
        //INVOICEPROVIDER
        /*InvoiceProvider invoiceProvider= new InvoiceProvider();
             
        System.out.println("Indica los detalles de la Factura:");
        System.out.println("Retención:");
        invoiceProvider.setRetention(scan.nextDouble());
        scan.nextLine();
        System.out.println("idDocument: ");
        invoiceProvider.setIdDocument(scan.nextInt());
        scan.nextLine();
        System.out.println("idProvider: ");
        invoiceProvider.setIdProvider(scan.nextInt());
        scan.nextLine();
        
        invoiceProvider.addToDB();
        
        System.out.println("La factura es (antes de insertar):");
        System.out.println(invoiceProvider.getIdInvoiceProvider()+" "
                +invoiceProvider.getRetention()+" "
                +invoiceProvider.getIdDocument()+" "
                +invoiceProvider.getIdProvider());
        
        invoiceProvider.getFromDB(1);
        
        System.out.println("La factura es (después de insertar):");
        System.out.println(invoiceProvider.getIdInvoiceProvider()+" "
                +invoiceProvider.getRetention()+" "
                +invoiceProvider.getIdDocument()+" "
                +invoiceProvider.getIdProvider());
        
        System.out.println("Qué factura quieres cambiar:");
        id=scan.nextInt();
        scan.nextLine();

        System.out.println("Qué campo quieres cambiar:");
        field=scan.nextLine();
        System.out.println("Nuevo valor:");
        invoiceProvider.getFromDB(id);
        invoiceProvider.updateDB(field,scan.nextLine());
        
        invoiceProvider.getFromDB(id);
        System.out.println("La factura es (antes de eliminar de insertar):");
        System.out.println(invoiceProvider.getIdInvoiceProvider()+" "
                +invoiceProvider.getRetention()+" "
                +invoiceProvider.getIdDocument()+" "
                +invoiceProvider.getIdProvider());
        
        System.out.println("Dale para continuar");
        scan.nextLine();
        
        invoiceProvider.deleteFromDB();*/
        
        //INVOICECUSTOMER
        /*InvoiceCustomer invoiceCustomer= new InvoiceCustomer();
             
        System.out.println("Indica los detalles de la Factura:");
        System.out.println("Retención:");
        invoiceCustomer.setRetention(scan.nextDouble());
        scan.nextLine();
        System.out.println("Pagada: ");
        if(scan.nextLine().equals("S"))invoiceCustomer.setPaid(true);
        System.out.println("idDocument: ");
        invoiceCustomer.setIdDocument(scan.nextInt());
        scan.nextLine();
        System.out.println("IdCostumer:");
        invoiceCustomer.setIdCustomer(scan.nextInt());
        scan.nextLine();
        
        invoiceCustomer.addToDB();
        
        System.out.println("La factura es (antes de insertar):");
        System.out.println(invoiceCustomer.getIdInvoiceCustomer()+" "
                +invoiceCustomer.getRetention()+" "
                +invoiceCustomer.isPaid()+" "
                +invoiceCustomer.getIdDocument()+" "
                +invoiceCustomer.getIdCustomer());
        
        invoiceCustomer.getFromDB(1);
        
        System.out.println("La factura es (después de insertar):");
        System.out.println(invoiceCustomer.getIdInvoiceCustomer()+" "
                +invoiceCustomer.getRetention()+" "
                +invoiceCustomer.isPaid()+" "
                +invoiceCustomer.getIdDocument()+" "
                +invoiceCustomer.getIdCustomer());
        
        System.out.println("Qué factura quieres cambiar:");
        id=scan.nextInt();
        scan.nextLine();

        System.out.println("Qué campo quieres cambiar:");
        field=scan.nextLine();
        System.out.println("Nuevo valor:");
        invoiceCustomer.getFromDB(id);
        invoiceCustomer.updateDB(field,scan.nextLine());
        
        invoiceCustomer.getFromDB(id);
        System.out.println("La factura es (antes de eliminar de insertar):");
        System.out.println(invoiceCustomer.getIdInvoiceCustomer()+" "
                +invoiceCustomer.getRetention()+" "
                +invoiceCustomer.isPaid()+" "
                +invoiceCustomer.getIdDocument()+" "
                +invoiceCustomer.getIdCustomer());
        
        System.out.println("Dale para continuar");
        scan.nextLine();
        
        invoiceCustomer.deleteFromDB();*/
        
        //DOCUMENT
        /*Document document= new Document();
             
        System.out.println("Indica los detalles del nuevo documento:");
        System.out.println("Número:");
        document.setDocNumber(scan.nextLine());
        System.out.println("Fecha: ");
        document.setDocDate(scan.nextLine());
        System.out.println("Titulo: ");
        document.setTitle(scan.nextLine());
        System.out.println("IVA:");
        document.setVat(scan.nextDouble());
        scan.nextLine();
        System.out.println("id User: ");
        document.setIdUsers(scan.nextInt());
        scan.nextLine();
        
        System.out.println("id ChangeRate: ");
        document.setIdChangeRate(scan.nextInt());
        scan.nextLine();
        
        document.addToDB();
        
        System.out.println("El documento es (antes de insertar):");
        System.out.println(document.getIdDocument()+" "
                +document.getDocNumber()+" "
                +document.getDocDate()+" "
                +document.getTitle()+" "
                +document.getVat()+" "
                +document.getIdUsers()+" "
                +document.getIdChangeRate());
        
        document.getFromDB(1);
        
        System.out.println("El documento es (después de insertar):");
        System.out.println(document.getIdDocument()+" "
                +document.getDocNumber()+" "
                +document.getDocDate()+" "
                +document.getTitle()+" "
                +document.getVat()+" "
                +document.getIdUsers()+" "
                +document.getIdChangeRate());
        
        System.out.println("Qué documento quieres cambiar:");
        id=scan.nextInt();
        scan.nextLine();

        System.out.println("Qué campo quieres cambiar:");
        field=scan.nextLine();
        System.out.println("Nuevo valor:");
        document.getFromDB(id);
        document.updateDB(field,scan.nextLine());
        
        document.getFromDB(id);
        System.out.println("El documento es (antes de eliminar de insertar):");
        System.out.println(document.getIdDocument()+" "
                +document.getDocNumber()+" "
                +document.getDocDate()+" "
                +document.getTitle()+" "
                +document.getVat()+" "
                +document.getIdUsers()+" "
                +document.getIdChangeRate());
        
        System.out.println("Dale para continuar");
        scan.nextLine();
        
        document.deleteFromDB();*/
        
        
        //CUSTOMER
        /*Customer customer= new Customer();
             
        System.out.println("Indica los detalles de la nueva cuenta:");
        System.out.println("Methodo de envio de la factura:");
        customer.setInvoicingMethod(scan.nextLine());
        System.out.println("Plazo de pago: ");
        customer.setDeadline(scan.nextInt());
        scan.nextLine();
        System.out.println("Methodo de pago: ");
        customer.setPayMethod(scan.nextLine());
        System.out.println("id CustomProv: ");
        customer.setIdCustomProv(scan.nextInt());
        scan.nextLine();
        
        customer.addToDB();
        
        System.out.println("La cuenta es (antes de insertar):");
        System.out.println(customer.getIdCustomer()+" "
                +customer.getInvoicingMethod()+" "
                +customer.getDeadline()+" "
                +customer.getPayMethod()+" "
                +customer.getIdCustomProv());
        
        customer.getFromDB(1);
        
        System.out.println("La cuenta es (después de insertar):");
        System.out.println(customer.getIdCustomer()+" "
                +customer.getInvoicingMethod()+" "
                +customer.getDeadline()+" "
                +customer.getPayMethod()+" "
                +customer.getIdCustomProv());
        
        System.out.println("Qué cuenta quieres cambiar:");
        id=scan.nextInt();
        scan.nextLine();

        customer.getFromDB(id);
        System.out.println("Qué campo quieres cambiar:");
        field=scan.nextLine();
        System.out.println("Nuevo valor:");
        customer.updateDB(field,scan.nextLine());
        
        customer.getFromDB(id);
        System.out.println("La cuenta es (antes de eliminar de insertar):");
        System.out.println(customer.getIdCustomer()+" "
                +customer.getInvoicingMethod()+" "
                +customer.getDeadline()+" "
                +customer.getPayMethod()+" "
                +customer.getIdCustomProv());
        
        System.out.println("Dale para continuar");
        scan.nextLine();
        
        customer.deleteFromDB();*/
        
        //CUSTOMPROV
        /*CustomProv customProv= new CustomProv();
             
        System.out.println("Indica los detalles de la nueva cuenta:");
        System.out.println("IVA por defecto:");
        customProv.setDefaultVAT(scan.nextDouble());
        System.out.println("Retención por Defecto: ");
        customProv.setDefaultRetention(scan.nextDouble());
        scan.nextLine();
        System.out.println("Es Europeo S/N: ");
        if(scan.nextLine().equals("S")){
            customProv.setEurope(true);
        }else{
        customProv.setEurope(false);
        }
        
        System.out.println("Está habilitado S/N: ");
        if(scan.nextLine().equals("S")){
            customProv.setEnabled(true);
        }else{
        customProv.setEnabled(false);
        }
        System.out.println("id Company: ");
        customProv.setIdCompany(scan.nextInt());
        scan.nextLine();
        
        customProv.addToDB();
        
        System.out.println("La cuenta es (antes de insertar):");
        System.out.println(customProv.getIdCustomProv()+" "
                +customProv.getDefaultVAT()+" "
                +customProv.getDefaultRetention()+" "
                +customProv.isEurope()+" "
                +customProv.isEnabled()+" "
                +customProv.getIdCompany());
        
        customProv.getFromDB(1);
        
        System.out.println("La cuenta es (después de insertar):");
        System.out.println(customProv.getIdCustomProv()+" "
                +customProv.getDefaultVAT()+" "
                +customProv.getDefaultRetention()+" "
                +customProv.isEurope()+" "
                +customProv.isEnabled()+" "
                +customProv.getIdCompany());
        
        System.out.println("Qué cuenta quieres cambiar:");
        id=scan.nextInt();
        scan.nextLine();
        customProv.getFromDB(id);
        System.out.println("Qué campo quieres cambiar:");
        field=scan.nextLine();
        System.out.println("Nuevo valor:");
        customProv.updateDB(field,scan.nextLine());
        
        customProv.getFromDB(id);
        System.out.println("La cuenta es (después de insertar):");
        System.out.println(customProv.getIdCustomProv()+" "
                +customProv.getDefaultVAT()+" "
                +customProv.getDefaultRetention()+" "
                +customProv.isEurope()+" "
                +customProv.isEnabled()+" "
                +customProv.getIdCompany());
        
        System.out.println("·Dale para continuar");
        scan.nextLine();
        
        customProv.deleteFromDB();*/
        
        //COMPANY
        /*Company company=new Company();
       
        System.out.println("Indica los detalles de la nueva empresa:");
        System.out.println("Nombre comercial:");
        company.setComName(scan.nextLine());
        System.out.println("Nombre fiscal: ");
        company.setLegalName(scan.nextLine());
        System.out.println("CIF: ");
        company.setVatNumber(scan.nextLine());
        System.out.println("Nombre: ");
        company.setFirstName(scan.nextLine());
        System.out.println("Apellido 1: ");
        company.setMiddleName(scan.nextLine());
        System.out.println("Apellido 2: ");
        company.setLastname(scan.nextLine());
        System.out.println("email: ");
        company.setEmail(scan.nextLine());
        System.out.println("web");
        company.setWeb(scan.nextLine());
        company.setIdAddress(6);
        
        company.addToDB();
        System.out.println("La empresa es (antes de insertar):");
        System.out.println(company.getIdCompany()+" "
                +company.getComName()+" "
                +company.getLegalName()+" "
                +company.getVatNumber()+" "
                +company.getFirstName()+" "
                +company.getMiddleName()+" "
                +company.getLastname()+" "
                +company.getEmail()+" "
                +company.getWeb()+" "
                +company.getIdAddress());
        
        company.getFromDB(1);
        System.out.println("La empresa es (después de insertar):");
        System.out.println(company.getIdCompany()+" "
                +company.getComName()+" "
                +company.getLegalName()+" "
                +company.getVatNumber()+" "
                +company.getFirstName()+" "
                +company.getMiddleName()+" "
                +company.getLastname()+" "
                +company.getEmail()+" "
                +company.getWeb()+" "
                +company.getIdAddress());
        
        System.out.println("Qué empresa quieres cambiar:");
        id=scan.nextInt();
        scan.nextLine();
        company.getFromDB(id);
        System.out.println("Qué campo quieres cambiar:");
        field=scan.nextLine();
        System.out.println("Nuevo valor:");
        company.updateDB(field,scan.nextLine());
        
        company.getFromDB(id);
        System.out.println("La empresa es (antes de insertar):");
        System.out.println(company.getIdCompany()+" "
                +company.getComName()+" "
                +company.getLegalName()+" "
                +company.getVatNumber()+" "
                +company.getFirstName()+" "
                +company.getMiddleName()+" "
                +company.getLastname()+" "
                +company.getEmail()+" "
                +company.getWeb()+" "
                +company.getIdAddress());
        
        company.deleteFromDB();*/
        
        //CHANGERATE
        /*ChangeRate changeRate=new ChangeRate();
        int id;
        String field;
        
        System.out.println("Indica los detalles del nuevo cambio:");
        System.out.println("Divisa 1:");
        changeRate.setCurrency1(scan.nextLine());
        System.out.println("Divisa 2: ");
        changeRate.setCurrency2(scan.nextLine());
        System.out.println("Cambio: ");
        changeRate.setRate(scan.nextDouble());
        scan.nextLine();
                
        changeRate.addToDB();
        
        System.out.println("El cambio es (antes de insertar:");
        System.out.println(changeRate.getIdChangeRate()+" "
                +changeRate.getCurrency1()+" "
                +changeRate.getCurrency2()+" "
                +changeRate.getRate());
        
        changeRate.getFromDB(1);
        
        System.out.println("El cambio es (despues de insertar):");
        System.out.println(changeRate.getIdChangeRate()+" "
                +changeRate.getCurrency1()+" "
                +changeRate.getCurrency2()+" "
                +changeRate.getRate());
        
        System.out.println("Qué cambio quieres cambiar:");
        id=scan.nextInt();
        scan.nextLine();
        changeRate.getFromDB(id);
        System.out.println("Qué campo quieres cambiar:");
        field=scan.nextLine();
        System.out.println("Nuevo valor:");
        changeRate.updateDB(field,scan.nextDouble());
        
        System.out.println("El cambio es (antes de actualizar):");
        System.out.println(changeRate.getIdChangeRate()+" "
                +changeRate.getCurrency1()+" "
                +changeRate.getCurrency2()+" "
                +changeRate.getRate());
        
        changeRate.getFromDB(id);
        System.out.println("El cambio es (despues de actualizar):");
        System.out.println(changeRate.getIdChangeRate()+" "
                +changeRate.getCurrency1()+" "
                +changeRate.getCurrency2()+" "
                +changeRate.getRate());
        
        changeRate.deleteFromDB();*/
        
        
        // ADDRESS
        /*Address address=new Address();
        System.out.println("Indica los detalles de la nueva dirección:");
        System.out.println("Calle:");
        address.setStreet(scan.nextLine());
        System.out.println("Número: ");
        address.setStNumber(scan.nextLine());
        System.out.println("Piso: ");
        address.setApt(scan.nextLine());
        System.out.println("Código Postal: ");
        address.setCp(scan.nextLine());
        System.out.println("Ciudad: ");
        address.setCity(scan.nextLine());
        System.out.println("Provincia: ");
        address.setState(scan.nextLine());
        System.out.println("País: ");
        address.setCountry(scan.nextLine());
        
        address.addToDB();*/
        
        /*address.getFromDB(8);
        
        System.out.println("La dirección es:");
        System.out.println(address.getIdAddress()+" "
                +address.getStreet()+" "
                +address.getStNumber()+" "
                +address.getApt()+" "
                +address.getCp()+" "
                +address.getCity()+" "
                +address.getState()+" "
                +address.getCountry());
        
        address.deleteFromDB();*/
    }   


    /*@Override
    public void init(){
        Database.createUser();
        Database.createDataBase();
        Database.createTables();
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root= FXMLLoader.load(getClass().getResource("/com/invoicingapp/javafx/viewLogin.fxml"));
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void stop(){
        
    }*/

    
}
