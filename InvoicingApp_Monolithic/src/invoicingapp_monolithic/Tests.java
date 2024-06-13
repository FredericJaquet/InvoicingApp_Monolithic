/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invoicingapp_monolithic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author frede
 */
public class Tests {
    
    int option;
    String answer;
    Scanner scan=new Scanner(System.in);
    Company company=new Company();
    Users user=new Users();
    Customer customer=new Customer();
    Provider provider=new Provider();
    Scheme scheme=new Scheme();
    ArrayList<Orders> orders=new ArrayList();
    InvoiceCustomer invoiceCustomer=new InvoiceCustomer();
    InvoiceProvider invoiceProvider=new InvoiceProvider();
    Quotes quote=new Quotes();
    PurchaseOrder po=new PurchaseOrder();
    
    public void start(){
        do{
            System.out.println("""
                               ¿Qué acción deseas realizar?
                               1. Crear un usuario nuevo
                               2. Crear un cliente nuevo
                               3. Crear un proveedor nuevo
                               4. Crear un esquema nuevo
                               5. Crear un pedido Cliente nuevo
                               6. Crear un pedido Proveedor nuevo
                               7. Crear una factura cliente
                               8. Crear una factura Proveedor
                               9. Crear un presupuesto
                               10. Crear una Order de Pedido
                               11. Salir""");
        
            option=scan.nextInt();
            scan.nextLine();
            
            switch(option){
                case(1): user=createNewUser();break;
                case(2): customer=createNewCustomer();break;
                case(3): provider=createNewProvider();break;
                case(4): scheme=createNewScheme();break;
                case(5): orders.add(createNewOrderCustomer());break;
                case(6): orders.add(createNewOrderProvider());break;
                case(7): invoiceCustomer=createNewInvoiceCustomer();break;
                case(8): invoiceProvider=createNewInvoiceProvider();break;
                case(9): quote=createNewQuote();break;
                case(10): po=createNewPO();
            }
            
        }while(option!=11);
    }
    
    public Users createNewUser(){
        Users user=new Users();
        Phone phone=new Phone();
        ContactPerson contact=new ContactPerson();
        
        System.out.println("Quieres añadir una persona de contacto? (S/N)");
        answer=scan.nextLine();
        do{
            if(answer.equals("S")){
                contact=new ContactPerson();
                System.out.println("Indica los detalles del contacto:");
                System.out.println("Nombre:");
                contact.setFirstname(scan.nextLine());
                System.out.println("Apellido 1: ");
                contact.setMiddlename(scan.nextLine());
                System.out.println("Apellido 2: ");
                contact.setLastname(scan.nextLine());
                System.out.println("Función: ");
                contact.setRole(scan.nextLine());
                System.out.println("Email: ");
                contact.setEmail(scan.nextLine());
                user.addContactPerson(contact);
                
                System.out.println("Quieres añadir otro contacto? (S/N)");
                answer=scan.nextLine();
            }
        }while(answer.equals("S"));
                
        System.out.println("Indica los detalles de la nueva empresa:");
        System.out.println("Nombre comercial:");
        user.setComName(scan.nextLine());
        System.out.println("Nombre fiscal: ");
        user.setLegalName(scan.nextLine());
        System.out.println("CIF: ");
        user.setVatNumber(scan.nextLine());
        System.out.println("email: ");
        user.setEmail(scan.nextLine());
        System.out.println("web");
        user.setWeb(scan.nextLine());
        
        System.out.println("Indica los detalles del nuevo Usuario:");
        System.out.println("Nombre de usuario:");
        user.setUserName(scan.nextLine());
        System.out.println("contraseña");
        user.setPasswd(scan.nextLine());
        
        System.out.println("Quieres añadir una dirección? (S/N)");
        if(scan.nextLine().equals("S")){
            user.setAddress();
            System.out.println("Indica los detalles de la nueva dirección:");
            System.out.println("Calle:");
            user.getAddress().setStreet(scan.nextLine());
            System.out.println("Número: ");
            user.getAddress().setStNumber(scan.nextLine());
            System.out.println("Piso: ");
            user.getAddress().setApt(scan.nextLine());
            System.out.println("Código Postal: ");
            user.getAddress().setCp(scan.nextLine());
            System.out.println("Ciudad: ");
            user.getAddress().setCity(scan.nextLine());
            System.out.println("Provincia: ");
            user.getAddress().setState(scan.nextLine());
            System.out.println("País: ");
            user.getAddress().setCountry(scan.nextLine());
        }
        
        System.out.println("Quieres añadir un tlf? (S/N)");
        answer=scan.nextLine();
        do{
            if(answer.equals("S")){
                phone=new Phone();
                System.out.println("Indica los detalles del teléfono:");
                System.out.println("phoneNumber:");
                phone.setPhoneNumber(scan.nextLine());
                System.out.println("Kind: ");
                phone.setKind(scan.nextLine());
                user.addPhone(phone);
                System.out.println("Quieres añadir otro tlf? (S/N)");
                answer=scan.nextLine();
            }
        }while(answer.equals("S"));
        
        user.addToDB();
        
        return user;
    }
    
    public Customer createNewCustomer(){
        Customer customer=new Customer();
        Phone phone=new Phone();
        ContactPerson contact=new ContactPerson();
    
        System.out.println("Quieres añadir una persona de contacto? (S/N)");
        answer=scan.nextLine();
        do{
            if(answer.equals("S")){
                contact=new ContactPerson();
                System.out.println("Indica los detalles del contacto:");
                System.out.println("Nombre:");
                contact.setFirstname(scan.nextLine());
                System.out.println("Apellido 1: ");
                contact.setMiddlename(scan.nextLine());
                System.out.println("Apellido 2: ");
                contact.setLastname(scan.nextLine());
                System.out.println("Función: ");
                contact.setRole(scan.nextLine());
                System.out.println("Email: ");
                contact.setEmail(scan.nextLine());
                customer.addContactPerson(contact);
                
                System.out.println("Quieres añadir otro contacto? (S/N)");
                answer=scan.nextLine();
            }
        }while(answer.equals("S"));
                
        System.out.println("Indica los detalles de la nueva empresa:");
        System.out.println("Nombre comercial:");
        customer.setComName(scan.nextLine());
        System.out.println("Nombre fiscal: ");
        customer.setLegalName(scan.nextLine());
        System.out.println("CIF: ");
        customer.setVatNumber(scan.nextLine());
        System.out.println("email: ");
        customer.setEmail(scan.nextLine());
        System.out.println("web");
        customer.setWeb(scan.nextLine());
        
        System.out.println("Indica los detalles del nuevo Cliente:");
        System.out.println("IVA por defecto:");
        customer.setDefaultVAT(scan.nextDouble());
        scan.nextLine();
        System.out.println("Retención por defecto:");
        customer.setDefaultWithholding(scan.nextDouble());
        scan.nextLine();
        System.out.println("Está activado? (S/N)");
        answer=scan.nextLine();
        if(answer.equals("S")){
            customer.setEnabled(true);
        }else{customer.setEnabled(false);}
        System.out.println("Está en Europa? (S/N)");
        answer=scan.nextLine();
        if(answer.equals("S")){
            customer.setEurope(true);
        }else{customer.setEurope(false);}
        
        System.out.println("Metodo de envio de factura:");
        customer.setInvoicingMethod(scan.nextLine());
        System.out.println("Plazo de pago:");
        customer.setDuedate(scan.nextInt());        
        System.out.println("Metodo de pago:");
        customer.setPayMethod(scan.nextLine());        
        
        System.out.println("Quieres añadir una dirección? (S/N)");
        if(scan.nextLine().equals("S")){
            customer.setAddress();
            System.out.println("Indica los detalles de la nueva dirección:");
            System.out.println("Calle:");
            customer.getAddress().setStreet(scan.nextLine());
            System.out.println("Número: ");
            customer.getAddress().setStNumber(scan.nextLine());
            System.out.println("Piso: ");
            customer.getAddress().setApt(scan.nextLine());
            System.out.println("Código Postal: ");
            customer.getAddress().setCp(scan.nextLine());
            System.out.println("Ciudad: ");
            customer.getAddress().setCity(scan.nextLine());
            System.out.println("Provincia: ");
            customer.getAddress().setState(scan.nextLine());
            System.out.println("País: ");
            customer.getAddress().setCountry(scan.nextLine());
        }
        
        System.out.println("Quieres añadir un tlf? (S/N)");
        answer=scan.nextLine();
        do{
            if(answer.equals("S")){
                phone=new Phone();
                System.out.println("Indica los detalles del teléfono:");
                System.out.println("phoneNumber:");
                phone.setPhoneNumber(scan.nextLine());
                System.out.println("Kind: ");
                phone.setKind(scan.nextLine());
                customer.addPhone(phone);
                System.out.println("Quieres añadir otro tlf? (S/N)");
                answer=scan.nextLine();
            }
        }while(answer.equals("S"));
        
        customer.addToDB();
        
        return customer;
    }
    
    public Provider createNewProvider(){
  
        Provider provider=new Provider();
        Phone phone=new Phone();
        ContactPerson contact=new ContactPerson();
    
        System.out.println("Quieres añadir una persona de contacto? (S/N)");
        answer=scan.nextLine();
        do{
            if(answer.equals("S")){
                contact=new ContactPerson();
                System.out.println("Indica los detalles del contacto:");
                System.out.println("Nombre:");
                contact.setFirstname(scan.nextLine());
                System.out.println("Apellido 1: ");
                contact.setMiddlename(scan.nextLine());
                System.out.println("Apellido 2: ");
                contact.setLastname(scan.nextLine());
                System.out.println("Función: ");
                contact.setRole(scan.nextLine());
                System.out.println("Email: ");
                contact.setEmail(scan.nextLine());
                customer.addContactPerson(contact);
                
                System.out.println("Quieres añadir otro contacto? (S/N)");
                answer=scan.nextLine();
            }
        }while(answer.equals("S"));
                
        System.out.println("Indica los detalles de la nueva empresa:");
        System.out.println("Nombre comercial:");
        provider.setComName(scan.nextLine());
        System.out.println("Nombre fiscal: ");
        provider.setLegalName(scan.nextLine());
        System.out.println("CIF: ");
        provider.setVatNumber(scan.nextLine());
        System.out.println("email: ");
        provider.setEmail(scan.nextLine());
        System.out.println("web");
        provider.setWeb(scan.nextLine());
        
        System.out.println("Indica los detalles del nuevo Cliente:");
        System.out.println("IVA por defecto:");
        provider.setDefaultVAT(scan.nextDouble());
        scan.nextLine();
        System.out.println("Retención por defecto:");
        provider.setDefaultWithholding(scan.nextDouble());
        scan.nextLine();
        System.out.println("Está activado? (S/N)");
        answer=scan.nextLine();
        if(answer.equals("S")){
            provider.setEnabled(true);
        }else{provider.setEnabled(false);}
        System.out.println("Está en Europa? (S/N)");
        answer=scan.nextLine();
        if(answer.equals("S")){
            provider.setEurope(true);
        }else{provider.setEurope(false);}

        System.out.println("Quieres añadir una dirección? (S/N)");
        if(scan.nextLine().equals("S")){
            provider.setAddress();
            System.out.println("Indica los detalles de la nueva dirección:");
            System.out.println("Calle:");
            provider.getAddress().setStreet(scan.nextLine());
            System.out.println("Número: ");
            provider.getAddress().setStNumber(scan.nextLine());
            System.out.println("Piso: ");
            provider.getAddress().setApt(scan.nextLine());
            System.out.println("Código Postal: ");
            provider.getAddress().setCp(scan.nextLine());
            System.out.println("Ciudad: ");
            provider.getAddress().setCity(scan.nextLine());
            System.out.println("Provincia: ");
            provider.getAddress().setState(scan.nextLine());
            System.out.println("País: ");
            provider.getAddress().setCountry(scan.nextLine());
        }
        
        System.out.println("Quieres añadir un tlf? (S/N)");
        answer=scan.nextLine();
        do{
            if(answer.equals("S")){
                phone=new Phone();
                System.out.println("Indica los detalles del teléfono:");
                System.out.println("phoneNumber:");
                phone.setPhoneNumber(scan.nextLine());
                System.out.println("Kind: ");
                phone.setKind(scan.nextLine());
                provider.addPhone(phone);
                System.out.println("Quieres añadir otro tlf? (S/N)");
                answer=scan.nextLine();
            }
        }while(answer.equals("S"));
        
        provider.addToDB();
        return provider;
    }
    
    public Scheme createNewScheme(){
        Scheme scheme=new Scheme();
        SchemeLine line=new SchemeLine();
        int idCustomProv;
        
        System.out.println("Indica la cuenta para la cual desdeas crear un Esquema de facturación:");
        idCustomProv=scan.nextInt();
        scan.nextLine();
        System.out.println("Indica el nombre:");
        scheme.setName(scan.nextLine());
        System.out.println("Indica el precio unitario:");
        scheme.setPrice(scan.nextDouble());
        scan.nextLine();
        System.out.println("Indica el sector");
        scheme.setField(scan.nextLine());
        System.out.println("Indica el idioma de origen:");
        scheme.setSourceLanguage(scan.nextLine());
        System.out.println("Indica el idioma de destino:");
        scheme.setTargetLanguage(scan.nextLine());
        
        System.out.println("Quieres añadir lineas al esquema? (S/N)");
        answer=scan.nextLine();
         do{
            if(answer.equals("S")){
                line=new SchemeLine();
                System.out.println("Indica los detalles de la línea:");
                System.out.println("descripción:");
                line.setDescription(scan.nextLine());
                System.out.println("Descuento: ");
                line.setDiscount(scan.nextDouble());
                scan.nextLine();
                scheme.addLine(line);
                System.out.println("Quieres añadir otra linea? (S/N)");
                answer=scan.nextLine();
            }
        }while(answer.equals("S"));
        
        scheme.addToDB();
        return scheme;
    }
    
    public Orders createNewOrderCustomer(){
        Orders order=new Orders();
        Map<Integer, Customer> mapCustomers=Customer.getMap();
        Map<Integer, String> mapScheme;
        Integer idCustomer, idScheme;
        Customer customer=new Customer();
        Scheme scheme=new Scheme();
        Iterator it;
        
        it = mapCustomers.keySet().iterator();

        while(it.hasNext()){
            idCustomer = (Integer)it.next();
            System.out.println("Clave: " + idCustomer + " -> Nombre Comercial: " + mapCustomers.get(idCustomer).getComName());
        }
        System.out.println("Para qué cuenta quieres crear el pedido");
        idCustomer=scan.nextInt();
        scan.nextLine();
        customer=mapCustomers.get(idCustomer);
        
        mapScheme=Scheme.getMap(customer.getIdCustomProv());
        
        it = mapScheme.keySet().iterator();
        if(it.hasNext()){
            System.out.println("Esta cuenta tiene esquema de facturación, indica cual quieres utilizar (0=ninguno)");
            while(it.hasNext()){
                idScheme = (Integer)it.next();
                System.out.println("Clave: " + idScheme + " -> Nombre Comercial: " + mapScheme.get(idScheme));
            }
            idScheme=scan.nextInt();
            scan.nextLine();
        }else{
            idScheme=0;
        }
        
        System.out.println("Indica los detalles del pedido");
        System.out.println("Descripción:");
        order.setDescription(scan.nextLine());
        System.out.println("Fecha:");
        order.setDateOrder(scan.nextLine());
        
        if(idScheme!=0){
            scheme.getFromDB(idScheme);
            if(scheme.getPrice()!=0){
                order.setPricePerUnit(scheme.getPrice());
            }else{
                System.out.println("Precio unitario:");
                order.setPricePerUnit(scan.nextDouble());
                scan.nextLine();
            }
            if(scheme.getUnits()!=null){
                order.setUnits(scheme.getUnits());
            }else {
                System.out.println("Unidades:");
                order.setUnits(scan.nextLine());
            }
            if(scheme.getField()!=null){
                order.setFieldName(scheme.getField());
            }else{
                System.out.println("Sector:");
                order.setFieldName(scan.nextLine());
            }
            if(scheme.getSourceLanguage()!=null){
                order.setSourceLanguage(scheme.getSourceLanguage());
            }else{
                System.out.println("Idioma de origen:");
                order.setSourceLanguage(scan.nextLine());
            }
            if(scheme.getTargetLanguage()!=null){
                order.setTargetLanguage(scheme.getTargetLanguage());
            }else{
                System.out.println("Idioma de destino");
                order.setTargetLanguage(scan.nextLine());
            }
        }else{
            System.out.println("Precio unitario:");
            order.setPricePerUnit(scan.nextDouble());
            scan.nextLine();
            System.out.println("Unidades:");
            order.setUnits(scan.nextLine());
            System.out.println("Sector:");
            order.setFieldName(scan.nextLine());
            System.out.println("Idioma de origen:");
            order.setSourceLanguage(scan.nextLine());
            System.out.println("Idioma de destino:");
            order.setTargetLanguage(scan.nextLine());
        }
        order.setBilled(false);
        order.setIdCustomProv(customer.getIdCustomProv());
        order.setItems(addItems(order.getIdOrders(), scheme));
        
        order.addToDB();
        
        return order;
    }
    
    public Orders createNewOrderProvider(){
        Orders order=new Orders();
        Map<Integer, Provider> mapProviders=Provider.getMap();
        Map<Integer, String> mapScheme;
        Iterator it;
        Integer idProvider, idScheme;
        Provider provider=new Provider();
        Scheme scheme=new Scheme();
        
        it = mapProviders.keySet().iterator();

        while(it.hasNext()){
            idProvider = (Integer)it.next();
            System.out.println("Clave: " + idProvider + " -> Nombre Comercial: " + mapProviders.get(idProvider).getComName());
        }
        System.out.println("Para qué cuenta quieres crear el pedido");
        idProvider=scan.nextInt();
        scan.nextLine();
        
        provider=mapProviders.get(idProvider);
        
        mapScheme=Scheme.getMap(provider.getIdCustomProv());
        
        it = mapScheme.keySet().iterator();
        if(it.hasNext()){
            System.out.println("Esta cuenta tiene esquema de facturación, indica cual quieres utilizar (0=ninguno)");
            while(it.hasNext()){
                idScheme = (Integer)it.next();
                System.out.println("Clave: " + idScheme + " -> Nombre: " + mapScheme.get(idScheme));
            }
            idScheme=scan.nextInt();
            scan.nextLine();
        }else{
            idScheme=0;
        }
        
        System.out.println("Indica los detalles del pedido");
        System.out.println("Descripción:");
        order.setDescription(scan.nextLine());
        System.out.println("Fecha:");
        order.setDateOrder(scan.nextLine());
        
        if(idScheme!=0){
            scheme.getFromDB(idScheme);
            if(scheme.getPrice()!=0){
                order.setPricePerUnit(scheme.getPrice());
            }else{
                System.out.println("Precio unitario:");
                order.setPricePerUnit(scan.nextDouble());
                scan.nextLine();
            }
            if(scheme.getUnits()!=null){
                order.setUnits(scheme.getUnits());
            }else {
                System.out.println("Unidades:");
                order.setUnits(scan.nextLine());
            }
            if(scheme.getField()!=null){
                order.setFieldName(scheme.getField());
            }else{
                System.out.println("Sector:");
                order.setFieldName(scan.nextLine());
            }
            if(scheme.getSourceLanguage()!=null){
                order.setSourceLanguage(scheme.getSourceLanguage());
            }else{
                System.out.println("Idioma de origen:");
                order.setSourceLanguage(scan.nextLine());
            }
            if(scheme.getTargetLanguage()!=null){
                order.setTargetLanguage(scheme.getTargetLanguage());
            }else{
                System.out.println("Idioma de destino");
                order.setTargetLanguage(scan.nextLine());
            }
        }else{
            System.out.println("Precio unitario");
            order.setPricePerUnit(scan.nextDouble());
            scan.nextLine();
            System.out.println("Unidades:");
            order.setUnits(scan.nextLine());
            System.out.println("Sector:");
            order.setFieldName(scan.nextLine());
            System.out.println("Idioma de origen:");
            order.setSourceLanguage(scan.nextLine());
            System.out.println("Idioma de destino:");
            order.setTargetLanguage(scan.nextLine());
        }
        
        order.setBilled(false);
        order.setIdCustomProv(provider.getIdCustomProv());
        order.setItems(addItems(order.getIdOrders(), scheme));
        
        order.addToDB();
        
        return order;
    }
    
    public ArrayList<Item> addItems(int idOrders, Scheme scheme){
        ArrayList<Item> items=new ArrayList();
        ArrayList<SchemeLine>lines=scheme.getLines();
        Item item;
        
        System.out.println("Introduce los datos de cada linea:");
        if(scheme==null){
            do{
                item=new Item();
                System.out.println("Descripción:");
                item.setDescription(scan.nextLine());
                System.out.println("Cantidad:");
                item.setQuantity(scan.nextDouble());
                scan.nextLine();
                System.out.println("Descuento:");
                item.setDiscount(scan.nextDouble());
                scan.nextLine();
                item.setIdOrders(idOrders);
                items.add(item);
                System.out.println("Quieres añadir otra linea? (S/N)");
            }while(scan.nextLine().equals("S"));
        }else{
            for(int i=0;i<lines.size();i++){
                item=new Item();
                item.setDescription(lines.get(i).getDescription());
                System.out.print(item.getDescription()+" ");
                item.setDiscount(lines.get(i).getDiscount());
                System.out.println(item.getDiscount());
                System.out.println("Cantidad:");
                item.setQuantity(scan.nextDouble());
                scan.nextLine();
                item.setIdOrders(idOrders);
                items.add(item);
            }
        }
        return items;
    }
    
    public InvoiceCustomer createNewInvoiceCustomer(){
        InvoiceCustomer invoiceCustomer=new InvoiceCustomer();
        Map<Integer, Customer> mapCustomers=Customer.getMap();
        Map<Integer, Orders> mapOrders;
        Iterator it;
        Integer idCustomer,idOrders;
        Customer customer=new Customer();
        ChangeRate changeRate=new ChangeRate();
        ArrayList<Orders>orders=new ArrayList();
    
        it = mapCustomers.keySet().iterator();

        while(it.hasNext()){
            idCustomer = (Integer)it.next();
            System.out.println("Clave: " + idCustomer + " -> Nombre Comercial: " + mapCustomers.get(idCustomer).getComName());
        }
        System.out.println("Para qué cuenta quieres crear el pedido");
        idCustomer=scan.nextInt();
        scan.nextLine();
        customer=mapCustomers.get(idCustomer);
        invoiceCustomer.setCustomer(customer);
        
        user.getFromDB(1);
        invoiceCustomer.setUser(user);
        changeRate.getFromDB(1);
        invoiceCustomer.setChangeRate(changeRate);
        
        System.out.println("Indica los detalles de la factura:");
        System.out.println("Numéro:");
        invoiceCustomer.setDocNumber(scan.nextLine());
        System.out.println("Fecha:");
        invoiceCustomer.setDocDate(scan.nextLine());
        invoiceCustomer.setTitle("FACTURA");
        if(customer.getDefaultVAT()==0){
            System.out.println("IVA:");
            invoiceCustomer.setVat(scan.nextDouble());
            scan.nextLine();
        }else{
            invoiceCustomer.setVat(customer.getDefaultVAT());
        }
        if(customer.getDefaultWithholding()==0){
            System.out.println("Retención:");
            invoiceCustomer.setWithholding(scan.nextDouble());
            scan.nextLine();
        }else{
            invoiceCustomer.setWithholding(customer.getDefaultWithholding());
        }
        invoiceCustomer.setPaid(false);
        
        mapOrders=Orders.getMap(customer.getIdCustomProv());
        
        it = mapOrders.keySet().iterator();
        
        while(it.hasNext()){
            idOrders=(Integer)it.next();
            System.out.println(mapOrders.get(idOrders).toString());
        }
        
        System.out.println("Cuales son los pedidos que quieres añadir a la factura: (0=salir)");
        do{
            idOrders=scan.nextInt();
            scan.nextLine();
            if(idOrders!=0){
                orders.add(mapOrders.get(idOrders));
            }
        }while(idOrders!=0);
        
        invoiceCustomer.setOrders(orders);
              
        invoiceCustomer.addToDB();
        
        return invoiceCustomer;
    }
    
    public InvoiceProvider createNewInvoiceProvider(){
        InvoiceProvider invoiceProvider=new InvoiceProvider();
        Map<Integer, Provider> mapProviders=Provider.getMap();
        Map<Integer, Orders> mapOrders;
        Iterator it;
        Integer idProvider,idOrders;
        Provider provider=new Provider();
        ChangeRate changeRate=new ChangeRate();
        ArrayList<Orders>orders=new ArrayList();
    
        it = mapProviders.keySet().iterator();

        while(it.hasNext()){
            idProvider = (Integer)it.next();
            System.out.println("Clave: " + idProvider + " -> Nombre Comercial: " + mapProviders.get(idProvider).getComName());
        }
        System.out.println("Para qué cuenta quieres crear el pedido");
        idProvider=scan.nextInt();
        scan.nextLine();
        provider=mapProviders.get(idProvider);
        invoiceProvider.setProvider(provider);
        
        user.getFromDB(1);
        invoiceProvider.setUser(user);
        changeRate.getFromDB(1);
        invoiceProvider.setChangeRate(changeRate);
        
        System.out.println("Indica los detalles de la factura:");
        System.out.println("Numéro:");
        invoiceProvider.setDocNumber(scan.nextLine());
        System.out.println("Fecha:");
        invoiceProvider.setDocDate(scan.nextLine());
        invoiceProvider.setTitle("FACTURA");
        if(provider.getDefaultVAT()==0){
            System.out.println("IVA:");
            invoiceProvider.setVat(scan.nextDouble());
            scan.nextLine();
        }else{
            invoiceProvider.setVat(provider.getDefaultVAT());
        }
        if(provider.getDefaultWithholding()==0){
            System.out.println("Retención:");
            invoiceProvider.setWithholding(scan.nextDouble());
            scan.nextLine();
        }else{
            invoiceProvider.setWithholding(provider.getDefaultWithholding());
        }
        
        mapOrders=Orders.getMap(provider.getIdCustomProv());
        
        it = mapOrders.keySet().iterator();
        
        while(it.hasNext()){
            idOrders=(Integer)it.next();
            System.out.println(mapOrders.get(idOrders).toString());
        }
        
        System.out.println("Cuales son los pedidos que quieres añadir a la factura: (0=salir)");
        do{
            idOrders=scan.nextInt();
            scan.nextLine();
            if(idOrders!=0){
                orders.add(mapOrders.get(idOrders));
            }
        }while(idOrders!=0);
        
        invoiceProvider.setOrders(orders);
              
        invoiceProvider.addToDB();
    
        return invoiceProvider;
    }
    
    public Quotes createNewQuote(){
        Quotes quote=new Quotes();
        Map<Integer, Customer> mapCustomers=Customer.getMap();
        Map<Integer, Orders> mapOrders;
        Iterator it;
        Integer idCustomer,idOrders;
        Customer customer=new Customer();
        ChangeRate changeRate=new ChangeRate();
        ArrayList<Orders>orders=new ArrayList();
    
        it = mapCustomers.keySet().iterator();

        while(it.hasNext()){
            idCustomer = (Integer)it.next();
            System.out.println("Clave: " + idCustomer + " -> Nombre Comercial: " + mapCustomers.get(idCustomer).getComName());
        }
        System.out.println("Para qué cuenta quieres crear el pedido");
        idCustomer=scan.nextInt();
        scan.nextLine();
        customer=mapCustomers.get(idCustomer);
        quote.setCustomer(customer);
        
        user.getFromDB(1);
        quote.setUser(user);
        changeRate.getFromDB(1);
        quote.setChangeRate(changeRate);
        
        System.out.println("Indica los detalles del Presupuesto:");
        System.out.println("Numéro:");
        quote.setDocNumber(scan.nextLine());
        System.out.println("Fecha:");
        quote.setDocDate(scan.nextLine());
        quote.setTitle("PRESUPUESTO");
        if(customer.getDefaultVAT()==0){
            System.out.println("IVA:");
            quote.setVat(scan.nextDouble());
            scan.nextLine();
        }else{
            quote.setVat(customer.getDefaultVAT());
        }
        System.out.println("Indica las instrucciones de pago:");
        quote.setNotePayment(scan.nextLine());
        System.out.println("Indica el plazo de entrega:");
        quote.setNoteDelivery(scan.nextLine());
        
        mapOrders=Orders.getMap(customer.getIdCustomProv());
        
        it = mapOrders.keySet().iterator();
        
        while(it.hasNext()){
            idOrders=(Integer)it.next();
            System.out.println(mapOrders.get(idOrders).toString());
        }
        
        System.out.println("Cuales son los pedidos que quieres añadir a la factura: (0=salir)");
        do{
            idOrders=scan.nextInt();
            scan.nextLine();
            if(idOrders!=0){
                orders.add(mapOrders.get(idOrders));
            }
        }while(idOrders!=0);
        
        quote.setOrders(orders);
              
        quote.addToDB();
        return quote;
    } 
    
    public PurchaseOrder createNewPO(){
        PurchaseOrder po=new PurchaseOrder();
        Map<Integer, Provider> mapProviders=Provider.getMap();
        Map<Integer, Orders> mapOrders;
        Iterator it;
        Integer idProvider,idOrders;
        Provider provider=new Provider();
        ChangeRate changeRate=new ChangeRate();
        ArrayList<Orders>orders=new ArrayList();
    
        it = mapProviders.keySet().iterator();

        while(it.hasNext()){
            idProvider = (Integer)it.next();
            System.out.println("Clave: " + idProvider + " -> Nombre Comercial: " + mapProviders.get(idProvider).getComName());
        }
        System.out.println("Para qué cuenta quieres crear el pedido");
        idProvider=scan.nextInt();
        scan.nextLine();
        provider=mapProviders.get(idProvider);
        invoiceProvider.setProvider(provider);
        
        user.getFromDB(1);
        po.setUser(user);
        changeRate.getFromDB(1);
        po.setChangeRate(changeRate);
        
        System.out.println("Indica los detalles de la factura:");
        System.out.println("Numéro:");
        po.setDocNumber(scan.nextLine());
        System.out.println("Fecha:");
        po.setDocDate(scan.nextLine());
        po.setTitle("Orden de Pedido");
        if(provider.getDefaultVAT()==0){
            System.out.println("IVA:");
            po.setVat(scan.nextDouble());
            scan.nextLine();
        }else{
            po.setVat(provider.getDefaultVAT());
        }
        System.out.println("Indica la fecha de entrega:");
        po.setDeadline(LocalDate.parse(scan.nextLine()));
        mapOrders=Orders.getMap(provider.getIdCustomProv());
        
        it = mapOrders.keySet().iterator();
        
        while(it.hasNext()){
            idOrders=(Integer)it.next();
            System.out.println(mapOrders.get(idOrders).toString());
        }
        
        System.out.println("Cuales son los pedidos que quieres añadir al Presupuesto: (0=salir)");
        do{
            idOrders=scan.nextInt();
            scan.nextLine();
            if(idOrders!=0){
                orders.add(mapOrders.get(idOrders));
            }
        }while(idOrders!=0);
        
        po.setOrders(orders);
              
        po.addToDB();
    
        return po;
        
    }
}
