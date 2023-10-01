package ru.awesone.task1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Класс матрицы для первого задания.
 */
public class Matrix {

  private final int columnsNum;
  private final int rowsNum;
  private final double[][] matrixArray;

  /**
   * Получение матрицы из двумерного массива.
   *
   * @param arr2D двумерный массив, на основе которого будет сделана матрица
   */
  public Matrix(double[][] arr2D) {
    rowsNum = arr2D.length;
    columnsNum = arr2D[0].length;
    matrixArray = Arrays.copyOf(arr2D, arr2D.length);
  }

  /**
   * Получение расширенной матрицы для системы Ax = b.
   *
   * @param srcMatrix матрица A
   * @param srcVector вектор b
   * @return расширенная матрица
   */
  public static Matrix getExpandedMatrix(Matrix srcMatrix, List<Double> srcVector) {
    double[][] resultArr = new double[srcMatrix.getRowsNum()][srcMatrix.getColumnsNum() + 1];

    for (int i = 0; i < resultArr.length; i++) {
      System.arraycopy(srcMatrix.getMatrixArray()[i], 0, resultArr[i], 0, resultArr[0].length - 1);
      resultArr[i][resultArr[0].length - 1] = srcVector.get(i);
    }

    return new Matrix(resultArr);
  }

  /**
   * Получение обратной матрицы.
   *
   * @return обратная матрица
   */
  public Matrix getInverse() {

    if (this.getRowsNum() != this.getColumnsNum()) {
      throw new IllegalArgumentException("Нельзя найти обратную матрицу для данной!");
    }

    int n = this.getRowsNum();

    double[][] inverseArr = new double[n][n];

    for (int i = 0; i < n; i++) {

      int finalI = i;

      List<Double> elementaryVector = IntStream.range(0, n).mapToDouble(j -> {
        if (finalI != j) {
          return 0;
        } else {
          return 1;
        }
      }).boxed().toList();

      LinearSystemSolver solver = new LinearSystemSolver(this, elementaryVector);
      List<Double> res = solver.decompositionLuMethod();

      for (int j = 0; j < n; j++) {
        inverseArr[j][i] = res.get(j);
      }

    }

    return new Matrix(inverseArr);
  }

  /**
   * Умножение матрицы на вектор.
   *
   * @param vector вектор для произведения
   * @return вектор-результат
   */
  public List<Double> multiplicationByVector(List<Double> vector) {
    List<Double> answer = new ArrayList<>();

    for (int i = 0; i < vector.size(); i++) {
      int finalI = i;
      answer.add(IntStream.range(0, this.getColumnsNum())
          .mapToDouble(j -> this.matrixArray[finalI][j] * vector.get(j)).sum());
    }
    return answer;
  }

  /**
   * Создаёт новую матрицу, которая отличается от исходной тем, что в ней поменяны местами две
   * строки и два столбца.
   *
   * @param firstColumn  первый столбец для обмена местами
   * @param secondColumn второй столбец для обмена местами
   * @param firstRow     первая строка для обмена местами
   * @param secondRow    вторая строка для обмена местами
   * @return матрица, в которой поменяны местами строки и столбцы
   */
  public Matrix swapTwoColumnsAndTwoRows(int firstColumn, int secondColumn, int firstRow,
      int secondRow) {
    double[][] newMatrixArr = new double[this.getRowsNum()][this.getColumnsNum()];

    for (int i = 0; i < newMatrixArr.length; i++) {
      System.arraycopy(this.getMatrixArray()[i], 0, newMatrixArr[i], 0,
          newMatrixArr[0].length);
    }

    System.arraycopy(this.getMatrixArray()[firstRow], 0, newMatrixArr[secondRow], 0,
        newMatrixArr[0].length);
    System.arraycopy(this.getMatrixArray()[secondRow], 0, newMatrixArr[firstRow], 0,
        newMatrixArr[0].length);

    double[] tempFirstColumn = new double[this.getRowsNum()];
    for (int i = 0; i < newMatrixArr.length; i++) {
      tempFirstColumn[i] = newMatrixArr[i][firstColumn];
      newMatrixArr[i][firstColumn] = newMatrixArr[i][secondColumn];
    }
    for (int i = 0; i < newMatrixArr.length; i++) {
      newMatrixArr[i][secondColumn] = tempFirstColumn[i];
    }

    return new Matrix(newMatrixArr);
  }

  public int getColumnsNum() {
    return columnsNum;
  }

  public int getRowsNum() {
    return rowsNum;
  }

  public double[][] getMatrixArray() {
    return matrixArray;
  }

  @Override
  public boolean equals(Object obj) {

    if (obj == null) {
      return false;
    }

    if (this.getClass() != obj.getClass()) {
      return false;
    }

    Matrix matrix = (Matrix) obj;

    for (int i = 0; i < this.getRowsNum(); i++) {
      for (int j = 0; j < this.getColumnsNum(); j++) {
        if (Math.abs(this.getMatrixArray()[i][j] - matrix.getMatrixArray()[i][j]) > 1e-8) {
          return false;
        }
      }
    }

    return true;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

}
