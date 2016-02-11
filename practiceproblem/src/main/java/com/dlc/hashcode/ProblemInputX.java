package com.dlc.hashcode;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Preconditions;

/**
 * Author : Sigbjorn Dybdahl Created: 11/02/16
 */
public class ProblemInputX {

  private int     numberOfRows, numberOfColumns, numberOfDrones,
      simulationDeadline, droneMaxLoad;

  private int[]   productTypeWeights;

  private int[][] warehouseXY, warehouseStock, ordersXY, orders;

  /**
   * loading input file into expectedPattern
   *
   * @param fileName
   */
  public ProblemInputX(String fileName) {

    try {
      URL pathUrl = ProblemInputX.class.getClassLoader().getResource(fileName);
      Preconditions.checkNotNull(pathUrl);
      String path = pathUrl.getPath();
      List<String> lines = Files.readAllLines(Paths.get(path),
          Charset.forName("ASCII"));
      Iterator iterator = lines.iterator();

      // 1- read numberOfRows, numberOfColumns, numberOfDrones,
      // simulationDeadline, droneMaxLoad
      if (iterator.hasNext()) {
        String firstLine = (String) iterator.next();
        numberOfRows = Integer.valueOf(firstLine.split(" ")[0]);
        numberOfColumns = Integer.valueOf(firstLine.split(" ")[1]);
        numberOfDrones = Integer.valueOf(firstLine.split(" ")[2]);
        simulationDeadline = Integer.valueOf(firstLine.split(" ")[3]);
        droneMaxLoad = Integer.valueOf(firstLine.split(" ")[4]);
      }

      // 2- read products
      if (iterator.hasNext()) {
        String productLine = (String) iterator.next();
        int numberOfProducts = Integer.valueOf(productLine);
        productTypeWeights = new int[numberOfProducts];

        productLine = (String) iterator.next();
        for (int j = 0; j < numberOfProducts; j++) {
          productTypeWeights[j] = Integer.valueOf(productLine.split(" ")[j]);
        }
      }

      // 3- read warehouses
      if (iterator.hasNext()) {
        String warehouseLine = (String) iterator.next();
        int numberOfWarehouses = Integer.valueOf(warehouseLine);
        warehouseXY = new int[numberOfWarehouses][2];
        warehouseStock = new int[numberOfWarehouses][productTypeWeights.length];

        for (int i = 0; i < numberOfWarehouses; i++) {
          warehouseLine = (String) iterator.next();
          warehouseXY[i][0] = Integer.valueOf(warehouseLine.split(" ")[0]);
          warehouseXY[i][1] = Integer.valueOf(warehouseLine.split(" ")[1]);

          warehouseLine = (String) iterator.next();
          for (int j = 0; j < productTypeWeights.length; j++) {
            warehouseStock[i][j] = Integer.valueOf(warehouseLine.split(" ")[j]);
          }
        }
      }

      // 4- read orders
      if (iterator.hasNext()) {
        String customerOrderLine = (String) iterator.next();
        int numberOfCustomerOrders = Integer.valueOf(customerOrderLine);
        ordersXY = new int[numberOfCustomerOrders][2];
        orders = new int[numberOfCustomerOrders][productTypeWeights.length];

        for (int i = 0; i < numberOfCustomerOrders; i++) {
          String coordinates = (String) iterator.next();
          ordersXY[i][0] = Integer.valueOf(coordinates.split(" ")[0]);
          ordersXY[i][1] = Integer.valueOf(coordinates.split(" ")[1]);

          int items = Integer.valueOf((String) iterator.next());
          String[] products = ((String) iterator.next()).split(" ");

          for (int j = 0; j < items; j++) {
            int productNumber = Integer.valueOf(products[j]);
            orders[i][productNumber] += 1;
          }
        }
      }
    } catch (IllegalArgumentException | IOException ex) {
      System.out.println("Error reading file '" + fileName + "'");
    }
  }

  public void print() {
    System.out.println(
        "numberOfRows=" + numberOfRows + ", numberOfColumns=" + numberOfColumns
            + ", numberOfDrones=" + numberOfDrones + ", simulationDeadline="
            + simulationDeadline + ", droneMaxLoad=" + droneMaxLoad);

    System.out.println("products: " + productTypeWeights.length);
    System.out.println("warehouses: " + warehouseXY.length);
    System.out.println("orders: " + ordersXY.length);
  }

  public int getNumberOfRows() {
    return numberOfRows;
  }

  public int getNumberOfColumns() {
    return numberOfColumns;
  }

  public int getNumberOfDrones() {
    return numberOfDrones;
  }

  public void setNumberOfDrones(int numberOfDrones) {
    this.numberOfDrones = numberOfDrones;
  }

  public int getSimulationDeadline() {
    return simulationDeadline;
  }

  public void setSimulationDeadline(int simulationDeadline) {
    this.simulationDeadline = simulationDeadline;
  }

  public int getDroneMaxLoad() {
    return droneMaxLoad;
  }

  public void setDroneMaxLoad(int droneMaxLoad) {
    this.droneMaxLoad = droneMaxLoad;
  }

  public int[] getProductTypeWeights() {
    return productTypeWeights;
  }

  public void setProductTypeWeights(int[] productTypeWeights) {
    this.productTypeWeights = productTypeWeights;
  }

  public int[][] getWarehouseXY() {
    return warehouseXY;
  }

  public void setWarehouseXY(int[][] warehouseXY) {
    this.warehouseXY = warehouseXY;
  }

  public int[][] getWarehouseStock() {
    return warehouseStock;
  }

  public void setWarehouseStock(int[][] warehouseStock) {
    this.warehouseStock = warehouseStock;
  }

  public int[][] getOrdersXY() {
    return ordersXY;
  }

  public void setOrdersXY(int[][] ordersXY) {
    this.ordersXY = ordersXY;
  }

  public int[][] getOrders() {
    return orders;
  }

  public void setOrders(int[][] orders) {
    this.orders = orders;
  }

  public void setNumberOfRows(int numberOfRows) {
    this.numberOfRows = numberOfRows;
  }

  public void setNumberOfColumns(int numberOfColumns) {
    this.numberOfColumns = numberOfColumns;
  }

}
