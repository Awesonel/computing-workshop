package ru.awesone.task1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 * Решатель линейных систем (матричных уравнений Ax = b).
 */
public class LinearSystemSolver {

  static Logger log = Logger.getLogger(LinearSystemSolver.class.getName());
  private final Matrix matrixA;
  private final List<Double> vectorB;
  private List<Double> result;
  private List<Double> discrepancy;

  /**
   * Создание решателя уравнения Ax = b.
   *
   * @param matrix матрица A
   * @param vector вектор b
   */
  public LinearSystemSolver(Matrix matrix, List<Double> vector) {
    matrixA = matrix;
    vectorB = vector;
  }

  private static void calculateNextIteration(int iterationNumber, int vectorSize,
      double[][] currIter, double[][] prevIter) {
    for (int j = iterationNumber; j <= vectorSize; j++) {
      currIter[iterationNumber][j] =
          prevIter[iterationNumber][j] / prevIter[iterationNumber][iterationNumber];
    }

    for (int i = iterationNumber + 1; i < vectorSize; i++) {
      for (int j = iterationNumber; j <= vectorSize; j++) {
        currIter[i][j] =
            prevIter[i][j] - currIter[iterationNumber][j] * prevIter[i][iterationNumber];
      }
    }

    for (int i = 0; i < currIter.length; i++) {
      System.arraycopy(currIter[i], 0, prevIter[i], 0, currIter[0].length);
    }
  }

  /**
   * Нахождение решения матричного уравнения Ax = b по схеме Гаусса единственного деления.
   *
   * @return вектор x - решение уравнения Ax = b
   */
  public List<Double> gaussianSchemeSingularDivision() {

    double[] res = new double[vectorB.size()];

    // Первый шаг - прямой ход
    double[][] prevIter = Matrix.getExpandedMatrix(matrixA, vectorB).getMatrixArray();
    double[][] currIter = new double[prevIter.length][prevIter[0].length];
    int n = prevIter.length;

    for (int k = 0; k < n; k++) {

      if (Math.abs(prevIter[k][k]) < 1e-6) {
        log.log(Level.WARNING,
            "Итерация №{0}: слишком малый ведущий элемент: могут быть сильные потери точности!",
            k);
      }
      calculateNextIteration(k, n, currIter, prevIter);

    }

    // Обратный ход

    for (int i = n - 1; i >= 0; i--) {
      double sum = 0;
      for (int j = i + 1; j < n; j++) {

        sum += currIter[i][j] * res[j];

      }

      res[i] = currIter[i][n] - sum;

    }
    this.result = Arrays.stream(res).boxed().toList();

    calculateDiscrepancy();

    return this.result;
  }

  /**
   * Нахождение решения матричного уравнения Ax = b по схеме Гаусса с выбором главного элемента по
   * столбцу.
   *
   * @return вектор x - решение уравнения Ax = b
   */
  public List<Double> gaussianSchemePrincipalElementSelectionByColumn() {

    double[] res = new double[vectorB.size()];

    // Первый шаг - прямой ход
    double[][] prevIter = Matrix.getExpandedMatrix(matrixA, vectorB).getMatrixArray();
    double[][] currIter = new double[prevIter.length][prevIter[0].length];
    int n = prevIter.length;

    for (int k = 0; k < n; k++) {

      // Выбор ведущего элемента по столбцу

      int maxI = k;
      for (int i = k; i < n; i++) {

        if (Math.abs(prevIter[i][k]) > Math.abs(prevIter[maxI][k])) {
          maxI = i;
        }

      }

      // Замена местами столбцов и строк матрицы предыдущей итерации

      Matrix newPrevIterMatrix = new Matrix(prevIter).swapTwoColumnsAndTwoRows(0, 0,
          maxI, k);
      for (int i = 0; i < prevIter.length; i++) {
        System.arraycopy(newPrevIterMatrix.getMatrixArray()[i], 0, prevIter[i], 0,
            prevIter[0].length);
      }

      // Проверка на "не малый" ведущий элемент
      if (Math.abs(prevIter[k][k]) < 1e-6) {
        log.log(Level.WARNING,
            "Итерация №{0}: слишком малый ведущий элемент: могут быть сильные потери точности!",
            k);
      }

      calculateNextIteration(k, n, currIter, prevIter);
    }

    // Обратный ход

    for (int i = n - 1; i >= 0; i--) {
      double sum = 0;
      for (int j = i + 1; j < n; j++) {

        sum += currIter[i][j] * res[j];

      }

      res[i] = currIter[i][n] - sum;

    }

    this.result = Arrays.stream(res).boxed().toList();

    calculateDiscrepancy();

    return this.result;
  }

  /**
   * Нахождение решения матричного уравнения Ax = b путём LU-разложения.
   *
   * @return вектор x - решение уравнения Ax = b
   */
  public List<Double> decompositionLuMethod() {

    int n = vectorB.size();

    double[][] matrixL = new double[n][n];
    double[][] matrixU = new double[n][n];
    double[][] matrixArrayA = new double[n][n];
    double[] vectorY = new double[n];
    double[] res = new double[n];

    for (int i = 0; i < n; i++) {
      matrixArrayA[i] = Arrays.copyOf(this.matrixA.getMatrixArray()[i], n);
    }

    // (1) Вычисление матриц L и U

    for (int i = 0; i < n; i++) {

      int finalI = i;

      for (int j = i; j < n; j++) {

        int finalJ = j;

        // Вычисление L[j,i]
        double firstSum = IntStream.range(0, i)
            .mapToDouble(k -> matrixL[finalJ][k] * matrixU[k][finalI]).sum();

        matrixL[j][i] = matrixArrayA[j][i] - firstSum;

        // Вычисление U[i,j]
        double secondSum = IntStream.range(0, i)
            .mapToDouble(k -> matrixL[finalI][k] * matrixU[k][finalJ]).sum();

        matrixU[i][j] = (matrixArrayA[i][j] - secondSum) / matrixL[i][i];
      }

      double sumY = IntStream.range(0, i)
          .mapToDouble(k -> matrixL[finalI][k] * vectorY[k]).sum();

      // одновременно с этим - вычисляем вектор y - решение уравнения Ly = b
      vectorY[i] = (vectorB.get(i) - sumY) / matrixL[i][i];

    }

    // (2) Решение Ux = y - находим вектор x

    for (int i = n - 1; i >= 0; i--) {
      double sum = 0;
      for (int j = i + 1; j < n; j++) {

        sum += matrixU[i][j] * res[j];

      }

      res[i] = vectorY[i] - sum;

    }
    this.result = Arrays.stream(res).boxed().toList();

    calculateDiscrepancy();

    return this.result;
  }

  private void calculateDiscrepancy() {
    List<Double> discrepancies = new ArrayList<>();
    List<Double> matrixMultipliedByResult = this.matrixA.multiplicationByVector(this.result);

    for (int i = 0; i < result.size(); i++) {
      discrepancies.add(vectorB.get(i) - matrixMultipliedByResult.get(i));
    }

    this.discrepancy = discrepancies;

  }

  public List<Double> getResult() {
    return result;
  }

  public List<Double> getDiscrepancy() {
    return discrepancy;
  }

}
