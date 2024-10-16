package com.example.protrack;


import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAOImplementation;
import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.productbuild.ProductBuild;
import com.example.protrack.productbuild.ProductBuildDAO;
import com.example.protrack.productorders.ProductOrder;
import com.example.protrack.productorders.ProductOrderDAO;
import com.example.protrack.products.*;
import com.example.protrack.requests.Requests;
import com.example.protrack.requests.RequestsDAO;
import com.example.protrack.supplier.Supplier;
import com.example.protrack.supplier.SupplierDAOImplementation;
import com.example.protrack.timesheets.Timesheets;
import com.example.protrack.timesheets.TimesheetsDAO;
import com.example.protrack.users.ManagerialUser;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.users.WarehouseUser;
import com.example.protrack.warehouseutil.LocationsAndContentsDAO;
import com.example.protrack.warehouseutil.RealWarehouse;
import com.example.protrack.warehouseutil.RealWorkstation;
import com.example.protrack.warehouseutil.partIdWithQuantity;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import com.example.protrack.workorderproducts.WorkOrderProduct;
import com.example.protrack.workorderproducts.WorkOrderProductsDAOImplementation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Main extends Application {

    private static final String TITLE = "ProTrack";
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    public static int getWidth() {
        return WIDTH;
    }

    public static int getHeight() {
        return HEIGHT;
    }

    public static void main(String[] args) throws SQLException {

        ProductDAO productDAO = new ProductDAO();
        productDAO.createTable();

        PartsDAO partsDAO = new PartsDAO();
        partsDAO.createTable();
        if (partsDAO.isTableEmpty()) {
            partsDAO.newPart(new Parts(50, "TestPart", "Testing", 1, 12.50));
            partsDAO.newPart(new Parts(51, "TestPart2", "Testing2", 2, 6.69));
            partsDAO.newPart(new Parts(1, "AA batteries", "Batteries from Japan", 3, 5.50));
            partsDAO.newPart(new Parts(2, "Wooden Panel", "Panels from Tom's workshop", 1, 3.75));
            partsDAO.newPart(new Parts(3, "Stainless Steel", "Stainless Steel from Bob Industry", 2, 2.10));
            partsDAO.newPart(new Parts(4, "Australium Cables", "Cables from Mann.co", 3, 52.1));
            partsDAO.newPart(new Parts(5, "Glass Panel", "Panels from Bob Industry", 1, 20.50));
        }

        BillOfMaterialsDAO billOfMaterial = new BillOfMaterialsDAO();
        billOfMaterial.createTable();
        if (billOfMaterial.isTableEmpty()) {
            // Product 1: Mining Collision Detector
            billOfMaterial.newRequiredParts(new BillOfMaterials(1, 1, 10)); // 10 units of part ID 1
            billOfMaterial.newRequiredParts(new BillOfMaterials(2, 1, 5));  // 5 units of part ID 2

            // Product 2: Displacement Monitoring Device
            billOfMaterial.newRequiredParts(new BillOfMaterials(1, 2, 8));  // 8 units of part ID 1
            billOfMaterial.newRequiredParts(new BillOfMaterials(3, 2, 12)); // 12 units of part ID 3

            // Product 3: Power Supply
            billOfMaterial.newRequiredParts(new BillOfMaterials(2, 3, 6));  // 6 units of part ID 2
            billOfMaterial.newRequiredParts(new BillOfMaterials(3, 3, 4));  // 4 units of part ID 3
        }

        if (productDAO.isTableEmpty()) {
            long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);

            productDAO.newProduct(new Product(1, "Mining Collision Detector", date, 1.0));
            double price = productDAO.calculateProductPrice(1);
            productDAO.updateProductPrice(1, price);

            productDAO.newProduct(new Product(2, "Displacement Monitoring Device", date, 1.0));
            price = productDAO.calculateProductPrice(2);
            productDAO.updateProductPrice(2, price);

            productDAO.newProduct(new Product(3, "Power Supply", date, 1.0));
            price = productDAO.calculateProductPrice(3);
            productDAO.updateProductPrice(3, price);
        }

        TestRecordDAO testRecordDAO = new TestRecordDAO();
        testRecordDAO.createTable();
        if (testRecordDAO.isTableEmpty()) {
            // Product 1: Mining Collision Detector
            testRecordDAO.newTestRecordStep(new TestRecord(1, 1, 1, "Torque the T10 screw to 5Nm.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecord(2, 1, 2, "Measure the thickness of the metal plate (mm).", "TextEntry", ">5"));
            testRecordDAO.newTestRecordStep(new TestRecord(3, 1, 3, "Power on the device and check the operating voltage (V).", "TextEntry", ">10"));
            testRecordDAO.newTestRecordStep(new TestRecord(4, 1, 4, "Power off the device and put into sleep mode for shipping.", "CheckBox", "NULL"));

            // Product 2: Displacement Monitoring Device
            testRecordDAO.newTestRecordStep(new TestRecord(1, 2, 1, "Inspect the exterior for any visible damage or scratches.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecord(2, 2, 2, "Check that the battery is fully charged.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecord(3, 2, 3, "Test the wireless connection strength (dBm).", "TextEntry", ">60"));
            testRecordDAO.newTestRecordStep(new TestRecord(4, 2, 4, "Verify that the display is working without any dead pixels.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecord(5, 2, 5, "Measure the device temperature after 10 minutes of operation (°C).", "TextEntry", "<40"));
            testRecordDAO.newTestRecordStep(new TestRecord(6, 2, 6, "Ensure all screws are torqued to the specified value (Nm).", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecord(7, 2, 7, "Check that the firmware version is up to date.", "TextEntry", "Latest"));
            testRecordDAO.newTestRecordStep(new TestRecord(8, 2, 8, "Power off the device and package it in the protective casing.", "CheckBox", "NULL"));

            // Product 3: Power Supply
            testRecordDAO.newTestRecordStep(new TestRecord(1, 3, 1, "Verify the serial number matches the product ID label.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecord(2, 3, 2, "Check the power cable for any signs of wear.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecord(3, 3, 3, "Test the main function of the device to ensure it operates correctly.", "CheckBox", "NULL"));
            testRecordDAO.newTestRecordStep(new TestRecord(4, 3, 4, "Ensure all accessory components are included in the packaging.", "CheckBox", "NULL"));
        }

        TimesheetsDAO timesheetsDAO = new TimesheetsDAO();
        timesheetsDAO.createTable();
        if (timesheetsDAO.isTableEmpty()) {
            timesheetsDAO.newTimesheet(new Timesheets(LocalDateTime.of(2024, 10, 1, 10, 00, 00, 00), LocalDateTime.of(2024, 10, 1, 14, 30, 00, 00), 100, 1));
            timesheetsDAO.newTimesheet(new Timesheets(LocalDateTime.of(2024, 9, 1, 10, 00, 00, 00), LocalDateTime.of(2024, 9, 1, 14, 30, 0, 0), 100, 2));
        }

        UsersDAO usersDAO = new UsersDAO();
        usersDAO.createTable();
        if (usersDAO.isTableEmpty()) {
            usersDAO.newUser(new ManagerialUser(100, "John", "Doe", Date.valueOf("1985-01-01"), "john.doe@example.com", "0400125123", "Male", "password"));
            usersDAO.newUser(new ManagerialUser(101, "Alice", "Smith", Date.valueOf("1990-05-15"), "alice.smith@example.com", "0400123113", "Female", "alicepass"));
            usersDAO.newUser(new ManagerialUser(102, "Bob", "Johnson", Date.valueOf("1982-09-23"), "bob.johnson@example.com", "0400123723", "Male", "bobpass"));
            usersDAO.newUser(new WarehouseUser(103, "Charlie", "Brown", Date.valueOf("1978-11-30"), "charlie.brown@example.com", "0400129723", "Male", "charliepass"));
            usersDAO.newUser(new WarehouseUser(104, "Diana", "White", Date.valueOf("1987-03-12"), "diana.white@example.com", "0400123093", "Female", "dianapass"));
            usersDAO.newUser(new ProductionUser(105, "Eve", "Davis", Date.valueOf("1993-07-22"), "eve.davis@example.com", "0400473123", "Female", "evepass"));
            usersDAO.newUser(new ProductionUser(106, "Frank", "Miller", Date.valueOf("1989-12-09"), "frank.miller@example.com", "0400192123", "Male", "frankpass"));
            usersDAO.newUser(new WarehouseUser(107, "Grace", "Harris", Date.valueOf("1992-12-11"), "grace.harris@example.com", "0400987654", "Female", "gracepass"));
            usersDAO.newUser(new ProductionUser(108, "Henry", "Martinez", Date.valueOf("1985-08-05"), "henry.martinez@example.com", "0400543210", "Male", "henrypass"));
            usersDAO.newUser(new WarehouseUser(109, "Isabella", "Garcia", Date.valueOf("1991-06-19"), "isabella.garcia@example.com", "0400789345", "Female", "isabellapass"));
            usersDAO.newUser(new ManagerialUser(110, "Jack", "Wilson", Date.valueOf("1975-02-28"), "jack.wilson@example.com", "0400129876", "Male", "jackpass"));
        }

        List<ProductionUser> productionUsers = usersDAO.getProductionUsers();

        CustomerDAOImplementation customerDAOImplementation = new CustomerDAOImplementation();
        customerDAOImplementation.createTable();
        if (customerDAOImplementation.isTableEmpty()) {
            customerDAOImplementation.addCustomer(new Customer(1, "Jane", "Doe", "jane.doe@example.com", "0400187362", "billingAddress", "shippingAddress", "Active"));
        }
        List<Customer> customers = customerDAOImplementation.getAllCustomers();

        LocationsAndContentsDAO locationsAndContentsDAO = new LocationsAndContentsDAO();
        locationsAndContentsDAO.createTables();

        if (locationsAndContentsDAO.isLocationsTableEmpty()) {
            locationsAndContentsDAO.newWarehouse(new RealWarehouse(0, "Lotus", 5000));
            locationsAndContentsDAO.newWorkstation(new RealWorkstation(1, "Workstation 1", 300));
            locationsAndContentsDAO.newWorkstation(new RealWorkstation(2, "Workstation 2", 40));
            locationsAndContentsDAO.newWorkstation(new RealWorkstation(3, "Workstation 3", 54));
            locationsAndContentsDAO.newWorkstation(new RealWorkstation(4, "Workstation 4", 255));
        }

        partIdWithQuantity testPart1 = new partIdWithQuantity();
        partIdWithQuantity testPart2 = new partIdWithQuantity();
        partIdWithQuantity testPart3 = new partIdWithQuantity();
        partIdWithQuantity testPart4 = new partIdWithQuantity();
        partIdWithQuantity testPart5 = new partIdWithQuantity();
        partIdWithQuantity testPart6 = new partIdWithQuantity();
        partIdWithQuantity testPart7 = new partIdWithQuantity();

        testPart1.partsId = 1;
        testPart1.quantity = 7;
        testPart2.partsId = 3;
        testPart2.quantity = 22;
        testPart3.partsId = 2;
        testPart3.quantity = 12;
        testPart4.partsId = 4;
        testPart4.quantity = 4;
        testPart5.partsId = 5;
        testPart5.quantity = 8;
        testPart6.partsId = 3;
        testPart6.quantity = 30;
        testPart7.partsId = 2;
        testPart7.quantity = 20;

        if (locationsAndContentsDAO.isLocationContentsTableEmpty()) {
            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(0, testPart1);
            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(0, testPart2);
            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(0, testPart3);
            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(0, testPart4);
            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(0, testPart5);

            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(1, testPart1);
            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(1, testPart3);
            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(1, testPart5);
            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(1, testPart6);
            //TODO isn't insertparts suppose to just update the parts in said location?
            // org.sqlite.SQLiteException: [SQLITE_CONSTRAINT_PRIMARYKEY] A PRIMARY KEY constraint failed (UNIQUE constraint failed: locationContents.locationID, locationContents.partID)
            //locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(1, testPart7);

            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(2, testPart2);
            locationsAndContentsDAO.insertPartsIdWithQuantityIntoLocation(2, testPart4);

            //
            //System.out.println("Warning: location Contents table created empty intentionally.");
        }

        RequestsDAO requestsDAO = new RequestsDAO();
        requestsDAO.createTable();

        if (requestsDAO.isTableEmpty()) {
            requestsDAO.newRequest(new Requests(1, 1, 1, 1));
            requestsDAO.newRequest(new Requests(1, 2, 2, 7));
            requestsDAO.newRequest(new Requests(1, 5, 3, 3));
            requestsDAO.newRequest(new Requests(2, 1, 4, 2));
            requestsDAO.newRequest(new Requests(2, 3, 5, 5));
            requestsDAO.newRequest(new Requests(2, 1, 6, 3));
            requestsDAO.newRequest(new Requests(2, 5, 7, 6));
            requestsDAO.newRequest(new Requests(1, 2, 8, 10));
            requestsDAO.newRequest(new Requests(1, 4, 9, 2));
            requestsDAO.newRequest(new Requests(3, 1, 10, 3));
        }

        WorkOrderProductsDAOImplementation workOrderProductsDAOImplementation = new WorkOrderProductsDAOImplementation();
        workOrderProductsDAOImplementation.createTable();

        if (workOrderProductsDAOImplementation.isTableEmpty()) {
            // Products for Work Order ID 1
            WorkOrderProduct product1 = new WorkOrderProduct(1, 1, 1, "Mining Collision Detector", 30, 73.75);
            WorkOrderProduct product2 = new WorkOrderProduct(2, 1, 2, "Displacement Monitoring Device", 32, 69.2);
            WorkOrderProduct product3 = new WorkOrderProduct(3, 1, 3, "Power Supply", 15, 30.9);

            workOrderProductsDAOImplementation.addWorkOrderProduct(product1);
            workOrderProductsDAOImplementation.addWorkOrderProduct(product2);
            workOrderProductsDAOImplementation.addWorkOrderProduct(product3);

            // Products for Work Order ID 2
            WorkOrderProduct product4 = new WorkOrderProduct(1, 2, 1, "Mining Collision Detector", 50, 73.75);
            WorkOrderProduct product5 = new WorkOrderProduct(2, 2, 2, "Displacement Monitoring Device", 12, 69.2);
            WorkOrderProduct product6 = new WorkOrderProduct(3, 2, 3, "Power Supply", 70, 30.9);

            workOrderProductsDAOImplementation.addWorkOrderProduct(product4);
            workOrderProductsDAOImplementation.addWorkOrderProduct(product5);
            workOrderProductsDAOImplementation.addWorkOrderProduct(product6);

            // Products for Work Order ID 3
            WorkOrderProduct product7 = new WorkOrderProduct(1, 3, 1, "Mining Collision Detector", 75, 73.75);
            WorkOrderProduct product8 = new WorkOrderProduct(2, 3, 2, "Displacement Monitoring Device", 20, 69.2);
            WorkOrderProduct product9 = new WorkOrderProduct(3, 3, 3, "Power Supply", 100, 30.9);

            workOrderProductsDAOImplementation.addWorkOrderProduct(product7);
            workOrderProductsDAOImplementation.addWorkOrderProduct(product8);
            workOrderProductsDAOImplementation.addWorkOrderProduct(product9);

            // Products for Work Order ID 4 (May)
            WorkOrderProduct product10 = new WorkOrderProduct(1, 4, 1, "Mining Collision Detector", 40, 73.75);
            WorkOrderProduct product11 = new WorkOrderProduct(2, 4, 2, "Displacement Monitoring Device", 25, 69.2);
            WorkOrderProduct product12 = new WorkOrderProduct(3, 4, 3, "Power Supply", 50, 30.9);

            workOrderProductsDAOImplementation.addWorkOrderProduct(product10);
            workOrderProductsDAOImplementation.addWorkOrderProduct(product11);
            workOrderProductsDAOImplementation.addWorkOrderProduct(product12);

            // Products for Work Order ID 5 (June)
            WorkOrderProduct product13 = new WorkOrderProduct(1, 5, 1, "Mining Collision Detector", 55, 73.75);
            WorkOrderProduct product14 = new WorkOrderProduct(2, 5, 2, "Displacement Monitoring Device", 35, 69.2);
            WorkOrderProduct product15 = new WorkOrderProduct(3, 5, 3, "Power Supply", 20, 30.9);

            workOrderProductsDAOImplementation.addWorkOrderProduct(product13);
            workOrderProductsDAOImplementation.addWorkOrderProduct(product14);
            workOrderProductsDAOImplementation.addWorkOrderProduct(product15);

            // Products for Work Order ID 6 (July)
            WorkOrderProduct product16 = new WorkOrderProduct(1, 6, 1, "Mining Collision Detector", 90, 73.75);
            WorkOrderProduct product17 = new WorkOrderProduct(2, 6, 2, "Displacement Monitoring Device", 10, 69.2);
            WorkOrderProduct product18 = new WorkOrderProduct(3, 6, 3, "Power Supply", 30, 30.9);

            workOrderProductsDAOImplementation.addWorkOrderProduct(product16);
            workOrderProductsDAOImplementation.addWorkOrderProduct(product17);
            workOrderProductsDAOImplementation.addWorkOrderProduct(product18);
        }

        WorkOrdersDAOImplementation wdao = new WorkOrdersDAOImplementation(productionUsers, customers);
        wdao.createTable();
        if (wdao.isTableEmpty()) {
            wdao.createWorkOrder(new WorkOrder(1, productionUsers.getFirst(), customers.getFirst(), LocalDateTime.of(2024, 9, 15, 10, 0), LocalDateTime.of(2024, 9, 15, 10, 0).plusDays(7), "shipAdd", "Pending", 40.87));
            wdao.createWorkOrder(new WorkOrder(2, productionUsers.getFirst(), customers.getFirst(), LocalDateTime.of(2024, 8, 15, 10, 0), LocalDateTime.of(2024, 8, 15, 10, 0).plusDays(7), "shipAdd", "Pending", 40.87));
            wdao.createWorkOrder(new WorkOrder(3, productionUsers.getFirst(), customers.getFirst(), LocalDateTime.of(2024, 10, 15, 10, 0), LocalDateTime.of(2024, 10, 15, 10, 0).plusDays(7), "shipAdd", "Pending", 40.87));

            // Create Work Orders for May, June, and July
            wdao.createWorkOrder(new WorkOrder(4, productionUsers.getFirst(), customers.getFirst(), LocalDateTime.of(2024, 5, 15, 10, 0), LocalDateTime.of(2024, 5, 15, 10, 0).plusDays(7), "shipAdd", "Pending", 40.87));
            wdao.createWorkOrder(new WorkOrder(5, productionUsers.getFirst(), customers.getFirst(), LocalDateTime.of(2024, 6, 15, 10, 0), LocalDateTime.of(2024, 6, 15, 10, 0).plusDays(7), "shipAdd", "Pending", 40.87));
            wdao.createWorkOrder(new WorkOrder(6, productionUsers.getFirst(), customers.getFirst(), LocalDateTime.of(2024, 7, 15, 10, 0), LocalDateTime.of(2024, 7, 15, 10, 0).plusDays(7), "shipAdd", "Pending", 40.87));
        }


        ProductBuildDAO productBuildDAO = new ProductBuildDAO();
        productBuildDAO.createTable();

        if (productBuildDAO.isTableEmpty()) {
            productBuildDAO.newProductBuild(new ProductBuild(500, 1, 0.00F, 1));
            productBuildDAO.newProductBuild(new ProductBuild(501, 1, 0.00F, 1));
            productBuildDAO.newProductBuild(new ProductBuild(502, 1, 0.00F, 1));
            productBuildDAO.newProductBuild(new ProductBuild(503, 2, 0.00F, 2));
            productBuildDAO.newProductBuild(new ProductBuild(504, 2, 0.00F, 2));
            productBuildDAO.newProductBuild(new ProductBuild(505, 3, 0.00F, 3));
            productBuildDAO.newProductBuild(new ProductBuild(506, 3, 0.00F, 3));

        }

        ProductOrderDAO productOrderDAO = new ProductOrderDAO();
        productOrderDAO.createTable();

        if (productOrderDAO.isTableEmpty()) {
            productOrderDAO.newProductOrder(new ProductOrder(1, 1, 3, 1));
            productOrderDAO.newProductOrder(new ProductOrder(2, 2, 2, 1));
            productOrderDAO.newProductOrder(new ProductOrder(3, 3, 1, 1));

        }

        SupplierDAOImplementation supplierDAOImplementation = new SupplierDAOImplementation();
        supplierDAOImplementation.createTable();

        if (supplierDAOImplementation.getAllSuppliers().isEmpty()) {
            supplierDAOImplementation.addSupplier(new Supplier(0, "Supplier1", "suppler1@email.com", "6130289348", "billAdd", "shipAdd", 4.7));
            supplierDAOImplementation.addSupplier(new Supplier(0, "Supplier2", "supplier2@email.com", "6123456789", "billingAddress2", "shippingAddress2", 10.0));
            supplierDAOImplementation.addSupplier(new Supplier(0, "Supplier3", "supplier3@email.com", "6145678901", "billingAddress3", "shippingAddress3", 15.4));
        }
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
        scene.getStylesheets().add(stylesheet);
        stage.setTitle(TITLE);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("Images/application_logo.png")));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setMaximized(false);
        stage.show();
    }
}