package com.example.protrack.applicationpages;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAOImplementation;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAO;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.util.List;

public class OrderLabelController {

    @FXML
    private Label workOrderIdLabel;

    @FXML
    private ImageView barcodeImageView;

    @FXML
    private Label customerNameLabel;

    @FXML
    private Label customerPhoneLabel;

    @FXML
    private Label shippingAddressLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Label businessPhoneLabel;

    @FXML
    private Label businessEmailLabel;

    @FXML
    private VBox labelContent;

    // Method to initialize the controller with the required data
    public void setLabelData(int workOrderId) {

        List<Customer> customers = new CustomerDAOImplementation().getAllCustomers();
        List<ProductionUser> productionUsers = new UsersDAO().getProductionUsers();

        WorkOrdersDAO workOrdersDAO = new WorkOrdersDAOImplementation(productionUsers, customers);

        WorkOrder currentWorkOrder = workOrdersDAO.getWorkOrderByOrderId(workOrderId);
        Customer customer = currentWorkOrder.getCustomer();

        workOrderIdLabel.setText(String.valueOf(currentWorkOrder.getWorkOrderId()));
        customerNameLabel.setText(customer.getFirstName() + " " + customer.getLastName());
        customerPhoneLabel.setText(customer.getPhoneNumber());
        shippingAddressLabel.setText(customer.getShippingAddress());
        locationLabel.setText("2 George St, Brisbane City QLD 4000");
        businessPhoneLabel.setText("(07) 3138 2000");
        businessEmailLabel.setText("yourbusiness@protrack.com.au");

        generateBarcode(currentWorkOrder.getWorkOrderId());
        // Set the barcode image
        barcodeImageView.setImage(barcodeImageView.getImage());
    }

    private void generateBarcode(int workOrderId) {
        try {
            QRCodeWriter barcodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = barcodeWriter.encode(String.valueOf(workOrderId), BarcodeFormat.QR_CODE, 300, 300);

            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            Image barcodeImage = SwingFXUtils.toFXImage(bufferedImage, null);

            // Set the generated barcode to the ImageView
            barcodeImageView.setImage(barcodeImage);
        } catch (WriterException e) {
            System.err.println("Error generating barcode: " + e.getMessage());
        }
    }

    /**
     * Handles the print button action by rendering the labelPane to an image and sending it to the printer.
     */
    @FXML
    private void handlePrint() {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            boolean success = printerJob.printPage(labelContent);
            if (success) {
                printerJob.endJob();
            }
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) workOrderIdLabel.getScene().getWindow();
        stage.close();
    }
}

