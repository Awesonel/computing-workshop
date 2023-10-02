package ru.awesone.lssolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LinearSystemSolverTest {

  @Test
  void testExpandedMatrix() {
    double[][] matrixArr = {
        {2, 4, 8},
        {3, 10, 7},
        {3, 3, 19}};
    Matrix matrix = new Matrix(matrixArr);
    List<Double> vector = new ArrayList<>();
    vector.add(30.0);
    vector.add(40.0);
    vector.add(50.0);

    double[][] actual = Matrix.getExpandedMatrix(matrix, vector).getMatrixArray();
    double[][] expected = {
        {2, 4, 8, 30},
        {3, 10, 7, 40},
        {3, 3, 19, 50}};

    Assertions.assertEquals(new Matrix(expected), new Matrix(actual));
    Assertions.assertEquals(Arrays.deepToString(expected), Arrays.deepToString(actual));
  }

  @Test
  void testMultiplicationByVector() {
    double[][] matrixArr = {
        {6, 3},
        {2, 4}};
    List<Double> vector = new ArrayList<>();
    vector.add(3.0);
    vector.add(1.0);

    List<Double> expected = new ArrayList<>();
    expected.add(21.0);
    expected.add(10.0);

    Matrix matrix = new Matrix(matrixArr);

    List<Double> actual = matrix.multiplicationByVector(vector);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  void testSwapColumnsAndRows() {
    Matrix testMatrix = new Matrix(new double[][]{
        {1, 2, 3, 4, 5},
        {6, 7, 8, 9, 10},
        {11, 12, 13, 14, 15},
        {16, 17, 18, 19, 20},
    });

    Matrix expected = new Matrix(new double[][]{
        {1, 2, 5, 4, 3},
        {16, 17, 20, 19, 18},
        {11, 12, 15, 14, 13},
        {6, 7, 10, 9, 8},
    });

    Matrix actual = testMatrix.swapTwoColumnsAndTwoRows(2, 4, 1, 3);

    Assertions.assertEquals(expected, actual);
    Assertions.assertEquals(Arrays.deepToString(expected.getMatrixArray()).replace("], ", "]\n"),
        Arrays.deepToString(actual.getMatrixArray()).replace("], ", "]\n"));
  }

  @Test
  void testInverseSimple() {
    Matrix testMatrix = new Matrix(new double[][]{
        {1, 2, 0},
        {0, 1, 0},
        {0, 3, 1}
    });

    Matrix expected = new Matrix(new double[][]{
        {1, -2, 0},
        {0, 1, 0},
        {0, -3, 1}
    });

    Matrix actual = testMatrix.getInverse();

    Assertions.assertEquals(expected, actual);

  }

  @Test
  void testInverseComplex() {
    Matrix testMatrix = new Matrix(new double[][]{
        {1, -2, 4},
        {0, 1, 2},
        {6, -1, 1}
    });

    Matrix expected = new Matrix(new double[][]{
        {-1.0 / 15.0, 2.0 / 45.0, 8.0 / 45.0},
        {-4.0 / 15.0, 23.0 / 45.0, 2.0 / 45.0},
        {2.0 / 15.0, 11.0 / 45.0, -1.0 / 45.0}
    });

    Matrix actual = testMatrix.getInverse();

    Assertions.assertEquals(expected, actual);

  }

}
