package com.example.protrack.warehouseutil;

/*
 * This is an internal data structure used by all implementations
 * of the Warehouse and Workstation interfaces to store an in-memory
 * copy of the partsID and quantity pairs retrieved from the Locations
 * and Contents DAO
 */
public class partIdWithQuantity {
    public int partsId;
    public int quantity;
}
