package com.example.protrack.applicationpages;

import com.example.protrack.database.ProductDBTable;
import javafx.collections.ObservableList;

interface Observer {
    void update(ObservableList<ProductDBTable> ProductTable);
}

public class ProductControllerObserver implements Observer {

    private String ProductControllerName;

    public ProductControllerObserver (String name) {
        ProductControllerName = name;
    }

    @Override
    public void update(ObservableList<ProductDBTable> ProductTable) {
        System.out.println(ProductControllerName + " received updated list: " + ProductTable.toString());
    }
}
