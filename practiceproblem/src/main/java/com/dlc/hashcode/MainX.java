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

  int[][] droneXY; // drone coordinates ex. droneXY[14][0] gives x coord for 14 drone

  int[] dronePayloadState; // drone payload

  int[][] droneInventoryState; // products loaded on given drone

  int[] droneTurnsState;

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

    dronePayloadState = new int[dronesNo];

    droneInventoryState = new int[dronesNo][productTypesNo];

  }

  // ​load command
  void load(int drone, int warehouse, int productType, int itemsNo) {

    // fail if warehouse doesn't have enough items
    if (warehouseStock[warehouse][productType] < itemsNo) {
      System.out.println(String.format("No stock for product %s at %s", productType, warehouse));
      System.exit(1);
    }

    String command = String.format("%s L %s %s %s", drone, warehouse, productType, itemsNo);
    String humanReadable = "";
    if (isAtWareHouse(drone, warehouse)) {
      humanReadable = String.format("Drone %s:l​oad ​%s items of product %s at warehouse %s.", drone, itemsNo, productType, warehouse);
    } else {
      humanReadable = String.format("Drone %s:fly to warehouse ​%s and load %s items of product %s.", drone, warehouse, itemsNo, productType);
    }
    System.out.println(humanReadable);
    commands.add(command);

    // update warehouse stock
    warehouseStock[warehouse][productType] -= itemsNo;


    // updates drone inventory and payload
    System.out.println(String.format("Drone %s payloaded with product %s with weight : %s ", drone, productType, productTypeWeights[productType]));
    System.out.println(String.format("Drone %s payload before %s", drone, dronePayloadState[drone]));
    dronePayloadState[drone] += productTypeWeights[productType] * itemsNo;
    droneInventoryState[drone][productType] += itemsNo;
    System.out.println(String.format("Drone %s payload after %s", drone, dronePayloadState[drone]));


  }

  boolean isAtWareHouse(int drone, int warehouse) {
    return droneXY[drone][0] == warehouseXY[warehouse][0] && droneXY[drone][1] == warehouseXY[warehouse][1];
  }

  boolean isAtCustomerHouse(int drone, int order) {
    return droneXY[drone][0] == ordersXY[order][0] && droneXY[drone][1] == ordersXY[order][1];
  }

  // ​deliver command
  void deliver(int drone, int order, int productType, int itemsNo) {
    String command = String.format("%s D %s %s %s", drone, order, productType, itemsNo);
    String humanReadable = "";
    if (isAtCustomerHouse(drone, order)) {
      humanReadable = String.format("Drone %s:deliver ​%s items of product %s at customer %s.", drone, itemsNo, productType, order);
    } else {
      humanReadable = String.format("Drone %s:fly to customer ​%s and deliver %s items of product %s.", drone, order, productType, itemsNo);
    }
    System.out.println(humanReadable);
    commands.add(command);

    // update orders stock
    //    orders[order][productType] += itemsNo;

    // update drone's inventory
    System.out.println(String.format("Drone %s  with product %s with weight : %s ", drone, productType, productTypeWeights[productType]));
    System.out.println(String.format("Drone %s payload before %s", drone, dronePayloadState[drone]));
    dronePayloadState[drone] -= productTypeWeights[productType];
    droneInventoryState[drone][productType] -= itemsNo;
    System.out.println(String.format("Drone %s payload after %s", drone, dronePayloadState[drone]));

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
    System.out.println(String.format("Drone is in  : (%s, %s) ", droneXY[drone][0], droneXY[drone][1]));
    System.out.println(String.format("Drone goes to : (%s, %s) ", x, y));
    updateTurns(drone, x, y);
    droneXY[drone][0] = x;
    droneXY[drone][1] = y;

  }

  // update drone state
  void updateTurns(int drone, int destx, int desty) {
    long turns = distanceBetween(droneXY[drone][0], droneXY[drone][1], destx, desty);
    System.out.println(String.format("Drone %s will take the distance %s", drone, turns));
    System.out.println(String.format("Drone %s turns before : %s ", drone, droneTurnsState[drone]));
    droneTurnsState[drone] += turns;
    System.out.println(String.format("Drone %s turns after : %s  ", drone, droneTurnsState[drone]));
  }

  // update drone state
  boolean droneHasTurnsCapacityToWareHouseAndCustomerHouse(int drone, int destwx, int destwy, int destcx, int destcy) {
    long turns1 = distanceBetween(droneXY[drone][0], droneXY[drone][1], destwx, destwy);
    long turns2 = distanceBetween(droneXY[drone][0], droneXY[drone][1], destcx, destcy);
    boolean hasCapacity = droneTurnsState[drone] + turns1 + turns2 <= maxPayload;
    return hasCapacity;
  }

  //
  static long distanceBetween(int x1, int y1, int x2, int y2) {
    return Math.round(Math.sqrt(((int) Math.pow(Math.abs(x1 - x2), 2)) + ((int) Math.pow(Math.abs(y1 - y2), 2))));
  }

  boolean warehouseHasProduct(int warehouse, int product, int itemsNo) {
    return warehouseStock[warehouse][product] - itemsNo >= 0;
  }

  boolean droneHasCapacity(int drone, int product, int itemsNo) {
    return dronePayloadState[drone] + itemsNo * productTypeWeights[product] <= maxPayload;
  }

  boolean droneIsEmpty(int drone) {
    return dronePayloadState[drone] <= 0;
  }


  int getDroneWithLessTurns() {
    int droneWithMinTurns = 0;
    int minTurns = droneTurnsState[0];

    for (int i = 1; i < droneTurnsState.length; i++) {
      if (minTurns > droneTurnsState[i]) {
        minTurns = droneTurnsState[i];
        droneWithMinTurns = i;
      }
    }

    return droneWithMinTurns;
  }

  void goDeliver(int drone, int order) {
    System.out.println("Trying to deliver  ...");
    if (!droneIsEmpty(drone)) {
      for (int i = 0; i < productTypesNo; i++) {
        if (droneInventoryState[drone][i] > 0) {
          // deliver what has been added to drone's inventory
          deliver(drone, order, i, droneInventoryState[drone][i]);
          moveTo(drone, ordersXY[order][0], ordersXY[order][1]);
        }
      }
    } else {
      System.out.println("ERR : Drone is empty!");
    }

  }

  void goDroneGo() {
    // take first order
    // load(0, 0, 0, 1);
    // take first order
    // take the closest drone
    // move all items to the destination

    // take drone with less turns
    int drone = getDroneWithLessTurns();
    System.out.println("Drone with less turns : " + drone);

    // take order0
//    int order = 0;
    int warehouse = 0;

    for (int order = 0; order < 8 ; order++) {
      System.out.println("Tackling order No " + order);
      for (int productType = 0; productType < orders[order].length; productType++) {
        int itemsNo = orders[order][productType];
        if (droneHasCapacity(drone, productType, itemsNo)) {
          if (droneHasTurnsCapacityToWareHouseAndCustomerHouse(drone, warehouseXY[warehouse][0], warehouseXY[warehouse][1], ordersXY[order][0], ordersXY[order][1])) {
            if (warehouseHasProduct(warehouse, productType, itemsNo)) {
              if (itemsNo > 0) {
                load(drone, warehouse, productType, itemsNo);
                moveTo(drone, warehouseXY[warehouse][0], warehouseXY[warehouse][1]);
              }
            } else {
              System.out.println(String.format("ERR : Warehouse has NO more %s items of product %s", itemsNo, productType));
            }
          } else {
            System.out.println("ERR : Drone has NO more TURNS capacity");
            drone = getDroneWithLessTurns();
            System.out.println("New drone is assigned : " + drone);

          }
        } else {
          System.out.println("ERR : Drone has NOT ENOUGH capacity");
          goDeliver(drone, order);
        }
      }
      goDeliver(drone, order);
    }
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
      inputFileName = "mother_of_all_warehouses.in";
//      inputFileName = "simple.in";
      outputFileName = inputFileName + ".result";
    }

    ProblemInputX input = new ProblemInputX(inputFileName);

    MainX solution = new MainX(input);

    // pilot the drone
    solution.goDroneGo();

    solution.writeToOutput(outputFileName);

  }
}
