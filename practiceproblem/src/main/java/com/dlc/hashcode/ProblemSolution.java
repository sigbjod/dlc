package com.dlc.hashcode;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/*
 * Solution for Painting problem
 *
 * @author basilboli, @date 2/7/16 7:40 PM
 */
public class ProblemSolution implements Comparable<ProblemSolution> {

  private int dimX;
  private int dimY;

  private int[][] actualBoard;
  private int[][] targetBoard;

  private List<String> commands;

  private static int NOT_VISITED = -1;
  private static int VISITED     = 0;
  private static int PAINTED     = 1;


  public ProblemSolution(ProblemInput input) {
    dimX = input.getNumberOfRows();
    dimY = input.getNumberOfColumns();
    targetBoard = input.getPattern();
    actualBoard = new int[dimX][dimY];
    fillItWith(NOT_VISITED);
    commands = new ArrayList<>();
  }

  // ERASE_CELL R​​C​​- clears the cell [R​,​C​]​.
  public void eraseCell(int r, int c) {
    String command = String.format("ERASE_CELL %s %s", r, c);
    System.out.println(command);
    commands.add(command);

    actualBoard[r][c] = VISITED;
  }

  // PAINT_SQUARE R​C​S​- paints all cells within the square of (2S + 1) × (2S + 1) dimensions centered at [R​,​C​]​
  public void paintSquare(int x, int y, int depth) {

    if (!isPaintSquarePossible(x, y, depth)) {
      System.out.println(String.format("Wrong input for PAINT_SQUARE %s %s %s", x, y, depth));
      System.exit(1);
    }

    String command = String.format("PAINT_SQUARE %s %s %s", x, y, depth);
    System.out.println(command);
    commands.add(command);

    for (int i = x - depth; i <= x + depth; i++) {
      for (int j = y - depth; j <= y + depth; j++) {
        actualBoard[i][j] = PAINTED;
      }
    }
  }

  private boolean isPaintSquarePossible(int i, int j, int d) {
    return i - d >= 0 && i + d <= dimX - 1 && j - d >= 0 && j + d <= dimY - 1;
  }

  // visits cell
  // we need it to know which cell we have already visited

  public void markAsVisited(int r, int c) {
    actualBoard[r][c] = VISITED;
  }

  // PAINT_LINE R​1 C​1 R​2 C​2 - paints all cells in a horizontal or vertical run between [R​1,​C​1]​and [R​2,​ C2]​
  // at least one of the two has to be true: R​1​= R​2​or/and C​1​= C​2
  public void paintLine(int r1, int c1, int r2, int c2) {
    String command = String.format("PAINT_LINE %s %s %s %s", r1, c1, r2, c2);
    System.out.println(command);
    commands.add(command);

    if (r1 != r2 && c1 != c2) {
      System.err.println("Wrong input for command PAINT_LINE");
      System.exit(1);
    }

    if (r1 == r2) {
      for (int j = c1; j <= c2; j++) {
        actualBoard[r1][j] = PAINTED;
      }
    } else if (c1 == c2) {
      for (int i = r1; i <= r2; i++) {
        actualBoard[i][c1] = PAINTED;
      }
    }
  }

  public List<String> getCommands() {
    return commands;
  }

  // returns true if solution if found
  public boolean isSolution() {
    return Arrays.deepEquals(targetBoard, actualBoard);
  }

  public void print() {
    for (int i = 0; i < dimX; i++) {
      for (int j = 0; j < dimY; j++) {
        System.out.print(actualBoard[i][j] == 1 ? "#" : actualBoard[i][j] == -1 ? "-" : ".");
      }
      System.out.println();
    }
  }

  public void fillItWith(int value) {
    for (int i = 0; i < dimX; i++) {
      for (int j = 0; j < dimY; j++) {
        actualBoard[i][j] = value;
      }
    }
  }

  @Override
  public String toString() {
    return commands.size() + "\n" + Joiner.on("\n").join(commands) + "\n";
  }

  @Override
  public int compareTo(ProblemSolution o) {
    return o.getCommands().size() - this.getCommands().size();
  }

  /**
   * strategy 1 (brute-force): painting using one-cell squares
   *
   * @return
   */
  void bruteForce() {
    System.out.println("Brute-force strategy :");
    for (int i = 0; i < dimX; i++) {
      for (int j = 0; j < dimY; j++) {
        if (shouldBePainted(i, j)) {
          paintSquare(i, j, 0);
        } else {
          markAsVisited(i, j);
        }
      }
    }
  }

  private boolean isVisited(int i, int j) {
    return actualBoard[i][j] >= 0;
  }

  private boolean isNotVisited(int i, int j) {
    return actualBoard[i][j] < 0;
  }

  private boolean shouldBePainted(int i, int j) {
    return targetBoard[i][j] > 0;
  }
}
