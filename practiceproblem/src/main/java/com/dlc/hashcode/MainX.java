package com.dlc.hashcode;

import com.google.common.base.Joiner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * $Id: $
 * <p/>
 * Author : Vasyl Vaskul (basilboli)
 * Created: 11/02/16
 */
public class MainX {

  int rows, columns, dronesNo, maxTurns, maxPayload, warehouseNo, productTypesNo, ordersNo;

  int[][] droneXY; // drone coordinates[14][0] gives x coord for 14 drone

  int[] droneTurnsState; // droneTurnsState[0][0]

  int[] productTypeWeights;

  int[][] warehouseXY;

  int[][] warehouseStock;

  int[][] ordersXY;

  int[][] orders; // product items to deliver by order

  List<String> commands;


  public MainX(ProblemInputX input) {
    this.rows = input.getNumberOfRows();
    this.columns = input.getNumberOfColumns();
    this.dronesNo = input.getNumberOfDrones();
    this.maxTurns = input.getSimulationDeadline();
    this.maxPayload = input.getDroneMaxLoad();
    this.warehouseNo = input.getWarehouseXY().length;
    this.productTypesNo = input.getProductTypeWeights().length;
    this.ordersNo = input.getOrdersXY().length;
    this.droneXY = new int[dronesNo][2];
    this.droneTurnsState = new int[dronesNo];
    this.productTypeWeights = input.getProductTypeWeights();
    this.warehouseXY = input.getWarehouseXY();
    this.warehouseStock = input.getWarehouseStock();
    this.ordersXY = input.getOrdersXY();
    this.orders = input.getOrders();

    // commands
    commands = new ArrayList<>();

    // initial coordinates of the drones
    for (int i = 0; i < dronesNo; i++) {
      droneXY[i][0] = warehouseXY[0][0];
      droneXY[i][1] = warehouseXY[0][1];
    }
  }

  // ​load command
  void load(int drone, int warehouse, int productType, int itemsNo) {

    // fail if warehouse doesn't have enough items
    if (warehouseStock[warehouse][productType] < itemsNo) {
      System.out.println(String.format("No stock for product %s at %s", productType, warehouse));
      System.exit(1);
    }

    String command = String.format("%s L %s %s %s", drone, warehouse, productType, itemsNo);
    System.out.println(command);
    commands.add(command);
    // update warehouse stock
    warehouseStock[warehouse][productType] -= itemsNo;
  }

  // ​deliver command
  void deliver(int drone, int order, int productType, int itemsNo) {
    String command = String.format("%s D %s %s %s", drone, order, productType, itemsNo);
    System.out.println(command);
    commands.add(command);

    // update orders stock
    warehouseStock[order][productType] += itemsNo;
  }

  // wait command
  void wait(int drone, int turns) {
    String command = String.format("%s W %s ", drone, turns);
    System.out.println(command);
    commands.add(command);

    // wait n turns?
  }

  // move drone to point x,y
  void moveTo(int drone, int x, int y) {
    droneXY[drone][0] = x;
    droneXY[drone][1] = y;
  }

  // update drone state
  void updateTurns(int drone, int turns) {
    droneTurnsState[drone] += turns;
  }

  //
  static long  distanceBetween (int x1, int y1, int x2, int y2 ) {
    return Math.round(Math.sqrt(((int) Math.pow(Math.abs(x1 - x2), 2)) + ((int) Math.pow(Math.abs(y1 - y2), 2))));
  }

  void goDroneGo() {
    // take first order
    load(0, 0, 0, 1);

  }

  public String getCommands() {
    return commands.size() + "\n" + Joiner.on("\n").join(commands) + "\n";
  }

  private void writeToOutput(String outputFileName) {
    try {
      FileOutputStream fs = new FileOutputStream(outputFileName);
      fs.write(getCommands().getBytes());
      fs.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public static void main(String[] args) {
    // load input

    String inputFileName, outputFileName;
    if (args.length == 1) {
      inputFileName = args[0];
      outputFileName = args[0] + ".result";
    } else {
      inputFileName = "simple.in";
      outputFileName = inputFileName + ".result";
    }

    ProblemInputX input = new ProblemInputX(inputFileName);

    MainX solution = new MainX(input);

    // pilot the drone
    solution.goDroneGo();

    solution.writeToOutput(outputFileName);

  }
}
