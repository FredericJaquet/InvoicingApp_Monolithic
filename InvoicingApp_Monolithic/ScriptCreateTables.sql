

CREATE TABLE IF NOT EXISTS Address(
idAddress	INT(10) AUTO_INCREMENT,
street		VARCHAR(100),
stNumber	VARCHAR(10),
apt		VARCHAR(25),
cp		VARCHAR(10),
city		VARCHAR(40),
state		VARCHAR(40),
country		VARCHAR(40),
PRIMARY KEY	(idAddress)
);

CREATE TABLE IF NOT EXISTS Company(
idCompany	INT(10) AUTO_INCREMENT,
vatNumber	VARCHAR(25),
comName		VARCHAR(100),
legalName	VARCHAR(100) 	UNIQUE,
email		VARCHAR(100),
web		VARCHAR(100),
idAddress	INT(10) 	UNIQUE,
PRIMARY KEY 	(idCompany),
FOREIGN KEY 	(idAddress) 	REFERENCES address(idAddress) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Phone(
phoneNumber	VARCHAR(25),
kind		VARCHAR(25),
idCompany	INT(10),
PRIMARY KEY 	(phoneNumber),
FOREIGN KEY 	(idCompany) 	REFERENCES Company(idCompany) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS ContactPerson(
idContactPerson	INT(10) AUTO_INCREMENT,
firstname	VARCHAR(40),
middlename	VARCHAR(40),
lastname	VARCHAR(40),
role		VARCHAR(40),
email		VARCHAR(100),
idCompany	INT(10),
PRIMARY KEY	(idContactPerson),
FOREIGN KEY	(idCompany)	REFERENCES Company(idCompany) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Users(
idUsers		INT(10) AUTO_INCREMENT,
userName	VARCHAR(25) 	NOT NULL UNIQUE,
passwd		VARCHAR(100) 	NOT NULL,
idCompany	INT(10) 	NOT NULL,
PRIMARY KEY 	(idUsers),
FOREIGN KEY 	(idCompany) 	REFERENCES Company(idCompany) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS CustomProv(
idCustomProv		INT(10) AUTO_INCREMENT,
defaultVAT		DOUBLE 		DEFAULT 0.21,
defaultWithholding	DOUBLE 		DEFAULT 0.15,
europe			boolean 	DEFAULT 1,
enabled			boolean 	DEFAULT 1,
idCompany		INT(10) 	NOT NULL,
PRIMARY KEY 		(idCustomProv),
FOREIGN KEY 		(idCompany) 	REFERENCES Company(idCompany) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Customer(
idCustomer	INT(10) AUTO_INCREMENT,
invoicingMethod	VARCHAR(100),
duedate 	INT(3),
payMethod	VARCHAR(40),
idCustomProv	INT(10) 	NOT NULL,
PRIMARY KEY 	(idCustomer),
FOREIGN KEY 	(idCustomProv)	REFERENCES CustomProv(idCustomProv) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Provider(
idProvider	INT(10) AUTO_INCREMENT,
idCustomProv	INT(10) 	NOT NULL,
PRIMARY KEY 	(idProvider),
FOREIGN KEY 	(idCustomProv)	REFERENCES CustomProv(idCustomProv) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Scheme(
idScheme	INT(10) AUTO_INCREMENT,
schemeName	VARCHAR(25),
price		DOUBLE,
units		VARCHAR(25),
fieldName	VARCHAR(15),
sourceLanguage	VARCHAR(15),
targetLanguage	VARCHAR(15),
PRIMARY KEY 	(idScheme)
);

CREATE TABLE IF NOT EXISTS SchemeLine(
idSchemeLine	INT(10) AUTO_INCREMENT,
descrip		VARCHAR(25),
discount	DOUBLE,
idScheme	INT(10) 	NOT NULL,
PRIMARY KEY 	(idSchemeLine),
FOREIGN KEY 	(idScheme)	REFERENCES Scheme(idScheme) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS SchemeCustomProv(
idCustomProv	INT(10) AUTO_INCREMENT,
idScheme	INT(10),
PRIMARY KEY 	(idCustomProv, idScheme),
FOREIGN KEY 	(idScheme) 	REFERENCES Scheme(idScheme) ON UPDATE CASCADE,
FOREIGN KEY 	(idCustomProv) 	REFERENCES CustomProv(idCustomProv) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS ChangeRate(
idChangeRate	INT(10) AUTO_INCREMENT, 
currency1	VARCHAR(10) 	DEFAULT '€',
currency2	VARCHAR(10) 	NOT null,
rate		DOUBLE 		DEFAULT 1,
PRIMARY KEY	(idChangeRate)
);

CREATE TABLE IF NOT EXISTS Document(
idDocument	INT(10) AUTO_INCREMENT, 
docNumber	VARCHAR(25),
docDate		DATE,
title		VARCHAR(25),
vat		DOUBLE,
idUsers		INT(10),
idChangeRate	INT(10)		DEFAULT 1,
PRIMARY KEY	(idDocument),
FOREIGN KEY 	(idUsers)	REFERENCES Users(idUsers) ON UPDATE CASCADE,
FOREIGN KEY 	(idChangeRate)	REFERENCES ChangeRate(idChangeRate) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS InvoiceCustomer(
idInvoiceCustomer	INT(10) AUTO_INCREMENT, 
withholding		double,
paid			boolean DEFAULT 0,
idDocument		INT(10) UNIQUE,
PRIMARY KEY		(idInvoiceCustomer),
FOREIGN KEY		(idDocument)	REFERENCES Document(idDocument) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS InvoiceProvider(
idInvoiceProvider	INT(10) AUTO_INCREMENT, 
withholding		double,
idDocument		INT(10) UNIQUE,
PRIMARY KEY		(idInvoiceProvider),
FOREIGN KEY		(idDocument)	REFERENCES Document(idDocument) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Quotes(
idQuotes	INT(10) AUTO_INCREMENT, 
noteDelivery	VARCHAR(100),
notePayment	VARCHAR(100),
idDocument	INT(10) UNIQUE,
PRIMARY KEY	(idQuotes),
FOREIGN KEY 	(idDocument)	REFERENCES Document(idDocument) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS PurchaseOrder(
idPurchaseOrder	INT(10) AUTO_INCREMENT, 
deadline	DATE,
idDocument	INT(10) UNIQUE,
PRIMARY KEY	(idPurchaseOrder),
FOREIGN KEY 	(idDocument)	REFERENCES Document(idDocument) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Orders(
idOrders	INT(10) AUTO_INCREMENT,
descrip		VARCHAR(100),
dateOrder	DATE,
pricePerUnit	DOUBLE,
units		VARCHAR(15),
billed		BOOLEAN DEFAULT 0,
fieldName	VARCHAR(25),
sourceLanguage	VARCHAR(25),
targetLanguage	VARCHAR(25),		
idCustomProv	INT(10),
PRIMARY KEY	(idOrders),
FOREIGN KEY 	(idCustomProv) 	REFERENCES CustomProv(idCustomProv) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS DocumentOrders(
idDocument	INT(10),
idOrders	INT(10),
PRIMARY KEY	(idDocument, idOrders),
FOREIGN KEY	(IdDocument) REFERENCES Document(idDocument) ON UPDATE CASCADE,
FOREIGN KEY	(idOrders)	REFERENCES Orders(idOrders) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Item(
idItem		INT(10) AUTO_INCREMENT, 
descrip		VARCHAR(100),
qty		DOUBLE,
discount	DOUBLE,
idOrders	INT(10),
PRIMARY KEY	(idItem),
FOREIGN KEY	(idOrders)	REFERENCES Orders(idOrders) ON UPDATE CASCADE
);