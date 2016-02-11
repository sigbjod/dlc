package com.dlc.hashcode;

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
    return Math.round(Math.sqrt(((int)Math.pow(Math.abs(x1-x2), 2)) + ((int) Math.pow(Math.abs(y1-y2), 2))));
  }

  static void goDroneGo() {
    // take first order

  }


  public static void main(String[] args) {
    // load input

    // loadInput ();

    // pilot the drone
    goDroneGo();

  }
}
