package com.dlc.hashcode;

import com.google.common.base.Preconditions;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * $Id: $
 * <p/>
 * Author : Vasyl Vaskul (basilboli)
 * Created: 11/02/16
 */
public class Main {

  public static void main(String[] args) {

    System.out.println("Args : " + args);
    String inputFileName = null;
    String outputFileName = null;

    if (args.length == 1) {
      inputFileName = args[0];
      outputFileName = args[0] + ".result";
    } else {
      System.out.println("Using default input/output");
      inputFileName = "small2.in";
      outputFileName = inputFileName + ".result";
    }

    System.out.println("Filename : " + inputFileName);
    ProblemInput input = new ProblemInput(inputFileName);
    System.out.println("Input : ");
    input.print();

    List<ProblemSolution> solutions = new ArrayList<>();

    // 1
    doBruteForce(input, solutions);

    ProblemSolution winner = findBest(solutions);

    writeToOutput(outputFileName, winner);

  }

  private static ProblemSolution findBest(List<ProblemSolution> solutions) {
    System.out.print("Found solutions: ");
    for (ProblemSolution ps : solutions) {
      System.out.print(ps.getCommands().size() + " ");
    }
    System.out.println();

    ProblemSolution winner = Collections.max(solutions);
    System.out.println("Winner solution :");
    System.out.println(winner.getCommands().size());
    return winner;
  }


  private static void writeToOutput(String outputFileName, ProblemSolution winner) {
    try {
      FileOutputStream fs = new FileOutputStream(outputFileName);
      fs.write(winner.toString().getBytes());
      fs.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void doBruteForce(ProblemInput input, List<ProblemSolution> solutions) {
    ProblemSolution solution = new ProblemSolution(input);
    solution.bruteForce();
    solution.print();
    Preconditions.checkArgument(solution.isSolution(), "Wrong strategy!");
    solutions.add(solution);
  }
}
