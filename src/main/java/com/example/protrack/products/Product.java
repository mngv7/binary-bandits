package com.example.protrack.products;

import java.sql.Date;

public class Product {

    private Integer productId;
    private String name;
    private Date dateCreated;
    private Integer employeeId;
    private Integer reqPartsId;
    private Integer PIId;
    private String status;

    public Product(Integer productId, String name, Date dateCreated, Integer employeeId, Integer reqPartsId, Integer PIId, String status) {
        this.productId = productId;
        this.name = name;
        this.dateCreated = dateCreated;
        this.employeeId = employeeId;
        this.reqPartsId = reqPartsId;
        this.PIId = PIId;
        this.status = status;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public Integer getReqPartsId() {
        return reqPartsId;
    }

    public Integer getPIId() {
        return PIId;
    }

    public String getStatus() {
        return status;
    }
}
