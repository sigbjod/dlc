package com.dlc.hashcode;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

/**
 * $Id: $
 * <p/>
 * Author : Vasyl Vaskul (basilboli)
 * Created: 08/02/16
 */
public class ProblemInput {

  private int numberOfRows;
  private int numberOfColumns;

  // representation of  painting
  private int[][] pattern;

  /**
   * loading input file into expectedPattern
   *
   * @param fileName
   */
  public ProblemInput(String fileName) {

    try {
      URL pathUrl = ProblemInput.class.getClassLoader().getResource(fileName);
      Preconditions.checkNotNull(pathUrl);
      String path = pathUrl.getPath();
      List<String> lines = Files.readAllLines(Paths.get(path), Charset.forName("ASCII"));
      Iterator iterator = lines.iterator();

      if (iterator.hasNext()) {
        String firstLine = (String) iterator.next();
        numberOfRows = Integer.valueOf(firstLine.split(" ")[0]);
        numberOfColumns = Integer.valueOf(firstLine.split(" ")[1]);
      }

      pattern = new int[numberOfRows][numberOfColumns];

      while (iterator.hasNext()) {
        for (int i = 0; i < numberOfRows; i++) {
          String line = (String) iterator.next();
          for (int j = 0; j < numberOfColumns; j++) {
            pattern[i][j] = line.substring(j, j + 1).equals("#") ? 1 : 0;
          }
        }
      }

    } catch (IllegalArgumentException | IOException ex) {
      System.out.println("Error reading file '" + fileName + "'");
    }
  }

  public void print() {
    System.out.println(String.format("%s x %s", numberOfRows, numberOfColumns));
    for (int i = 0; i < numberOfRows; i++) {
      for (int j = 0; j < numberOfColumns; j++) {
        System.out.print(pattern[i][j]==1?"#":".");
      }
      System.out.println();
    }
    System.out.println();
  }

  public int getNumberOfRows() {
    return numberOfRows;
  }

  public int getNumberOfColumns() {
    return numberOfColumns;
  }

  public int[][] getPattern() {
    return pattern;
  }
}
