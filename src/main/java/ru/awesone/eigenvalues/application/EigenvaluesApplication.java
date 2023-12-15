package ru.awesone.eigenvalues.application;

import org.apache.commons.math3.linear.*;
import ru.awesone.eigenvalues.solver.EigenvalueSolver;
import ru.awesone.eigenvalues.solver.RotationMethodSolver;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class EigenvaluesApplication {

  public static final RealMatrix MATRIX = new Array2DRowRealMatrix(new double[][]{
          {-1.48213, -0.03916, 1.08254},
          {-0.03916, 1.13958, 0.01617},
          {1.08254, 0.01617, -1.48271}
  });

  public static final double EPSILON = 1e-6;

  public static void main(String[] args) {
    run(MATRIX, EPSILON);
  }

  public static void run(RealMatrix matrix, double epsilon) {

    System.out.println("------ ТЕМА 3: ПРОБЛЕМА СОБСТВЕННЫХ ЗНАЧЕНИЙ ------");
    System.out.println("-------------- ВАРИАНТ 3, МАТРИЦА 3 ---------------");

    runRotationMethod(matrix, epsilon);

  }

  private static void runRotationMethod(RealMatrix matrix, double epsilon) {
    EigenvalueSolver eigenvalueSolver = new RotationMethodSolver(epsilon);
    eigenvalueSolver.solve(matrix);
    int n = matrix.getRowDimension();

    List<Double> eigenvalues = eigenvalueSolver.getEigenvalues();
    List<RealVector> eigenvectors = eigenvalueSolver.getEigenvectors();

    System.out.println("\n\nНайденные собственные значения и их вектора: \n");
    for (int i = 0; i < n; i++) {
      System.out.printf("%d значение: %f; вектор: ", i, eigenvalues.get(i));
      System.out.println(Arrays.toString(eigenvectors.get(i).toArray()));
    }

    EigenDecomposition decomposition = new EigenDecomposition(matrix);
    System.out.println("\n\nТочные собственные значения и их вектора: \n");
    for (int i = 0; i < n; i++) {
      System.out.printf("%d точное значение: %f; вектор: ", i, decomposition.getRealEigenvalues()[i]);
      System.out.println(Arrays.toString(decomposition.getEigenvector(i).toArray()));
    }
  }
}
