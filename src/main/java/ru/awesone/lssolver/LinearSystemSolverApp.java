package ru.awesone.lssolver;

import dnl.utils.text.table.TextTable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import ru.awesone.Matrix;

public class LinearSystemSolverApp {

  public static final Matrix MATRIX = new Matrix(new double[][]{
      {1, 2, 3},
      {4, 8, 1},
      {2, 3, 5}});
  public static final List<Double> VECTOR = Arrays.asList(
      17.0,
      24.0,
      28.0);

  public static void main(String[] args) {
    testGaussianSchemeSingularDivision();
    testGaussianSchemePrincipalElementSelectionByColumn();
    testLuDecompositionMethod();
    findInverse();
  }

  private static void findInverse() {

    System.out.println();
    System.out.println("----------- НАХОЖДЕНИЕ ОБРАТНОЙ МАТРИЦЫ -----------");
    System.out.println("------------------- ВАРИАНТ 12 --------------------");

    System.out.println();
    System.out.println("Матрица A:");
    IntStream.range(0, MATRIX.getRowsNum())
        .forEach(i -> System.out.println(Arrays.toString(MATRIX.getMatrixArray()[i])));

    System.out.println("Матрица, обратная к матрице А:");
    Matrix inverseMatrix = MATRIX.getInverse();
    IntStream.range(0, inverseMatrix.getRowsNum())
        .forEach(i -> System.out.println(Arrays.toString(inverseMatrix.getMatrixArray()[i])));

  }

  /**
   * Решение системы при помощи схемы Гаусса единственного деления.
   */
  private static void testGaussianSchemeSingularDivision() {
    System.out.println();
    System.out.println("----- ТЕСТ СХЕМЫ ГАУССА ЕДИНСТВЕННОГО ДЕЛЕНИЯ -----");
    System.out.println("------------------- ВАРИАНТ 12 --------------------");

    LinearSystemSolver solver = new LinearSystemSolver(MATRIX, VECTOR);
    List<Double> answer = solver.gaussianSchemeSingularDivision();

    printMatrixAndVector();

    String[] columns = {"Решение", "Невязка"};
    String[][] table = new String[answer.size()][2];

    for (int i = 0; i < answer.size(); i++) {
      table[i][0] = String.format("%.12f", answer.get(i));
      table[i][1] = String.format("%.4e", solver.getDiscrepancy().get(i));
    }

    TextTable tt = new TextTable(columns, table);
    tt.printTable();
  }

  /**
   * Решение системы при помощи схемы Гаусса c выбором ведущего элемента по столбцу.
   */
  private static void testGaussianSchemePrincipalElementSelectionByColumn() {
    System.out.println();
    System.out.println("-- ТЕСТ СХЕМЫ ГАУССА С ВЫБОРОМ ВЕДУЩЕГО ЭЛЕМЕНТА --");
    System.out.println("------------------ ВАРИАНТ 12 ---------------------");

    LinearSystemSolver solver = new LinearSystemSolver(MATRIX, VECTOR);
    List<Double> answer = solver.gaussianSchemePrincipalElementSelectionByColumn();

    printMatrixAndVector();

    String[] columns = {"Решение", "Невязка"};
    String[][] table = new String[answer.size()][2];

    for (int i = 0; i < answer.size(); i++) {
      table[i][0] = String.format("%.12f", answer.get(i));
      table[i][1] = String.format("%.4e", solver.getDiscrepancy().get(i));
    }

    TextTable tt = new TextTable(columns, table);
    tt.printTable();
  }

  /**
   * Решение системы при помощи LU-разложения.
   */
  private static void testLuDecompositionMethod() {
    System.out.println();
    System.out.println("--------------- ТЕСТ LU-РАЗЛОЖЕНИЯ ----------------");
    System.out.println("------------------- ВАРИАНТ 12 --------------------");

    LinearSystemSolver solver = new LinearSystemSolver(MATRIX, VECTOR);
    List<Double> answer = solver.decompositionLuMethod();

    printMatrixAndVector();

    String[] columns = {"Решение", "Невязка"};
    String[][] table = new String[answer.size()][2];

    for (int i = 0; i < answer.size(); i++) {
      table[i][0] = String.format("%.12f", answer.get(i));
      table[i][1] = String.format("%.4e", solver.getDiscrepancy().get(i));
    }

    TextTable tt = new TextTable(columns, table);
    tt.printTable();
  }

  private static void printMatrixAndVector() {
    System.out.println();
    System.out.println("Матрица A:");
    IntStream.range(0, MATRIX.getRowsNum())
        .forEach(i -> System.out.println(Arrays.toString(MATRIX.getMatrixArray()[i])));
    System.out.println("Вектор b:");
    System.out.println(VECTOR);
    System.out.println();
  }

}
