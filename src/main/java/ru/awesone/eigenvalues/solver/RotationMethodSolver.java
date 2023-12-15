package ru.awesone.eigenvalues.solver;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RotationMethodSolver implements EigenvalueSolver {

  private List<Double> eigenvalues;
  private List<RealVector> eigenvectors;
  private double epsilon;

  public RotationMethodSolver(double epsilon) {
    this.epsilon = epsilon;
  }
  
  @Override
  public void solve(RealMatrix matrix) {

    RealMatrix eigenvectorsMatrix = MatrixUtils.createRealIdentityMatrix(matrix.getRowDimension());

    RealMatrix currentMatrixA = new Array2DRowRealMatrix(matrix.getData());


    MaxElemData maxElemData = selectMaxUpperElem(currentMatrixA);


    while (Math.abs(maxElemData.maxElem) >= epsilon) {
//      System.out.println(currentMatrixA);
//      System.out.println(maxElemData.maxElem);
//      System.out.println(maxElemData.i);
//      System.out.println(maxElemData.j);
      double cos = calculateCos(maxElemData, currentMatrixA);
      double sin = calculateSin(maxElemData, currentMatrixA);
      currentMatrixA = recalculateA(currentMatrixA, maxElemData, cos, sin);
      eigenvectorsMatrix = recalculateX(eigenvectorsMatrix, maxElemData, cos, sin);
      maxElemData = selectMaxUpperElem(currentMatrixA);
    }

    eigenvalues = new ArrayList<>();
    eigenvectors = new ArrayList<>();

    for (int i = 0; i < currentMatrixA.getRowDimension(); i++) {
      eigenvalues.add(currentMatrixA.getEntry(i, i));
      eigenvectors.add(eigenvectorsMatrix.getColumnVector(i));
    }

  }

  private RealMatrix recalculateX(RealMatrix eigenvectorsMatrix, MaxElemData maxElemData, double cos, double sin) {

    double[][] matrixArrV = new double[eigenvectorsMatrix.getRowDimension()][eigenvectorsMatrix.getColumnDimension()];
    int iK = maxElemData.i;
    int jK = maxElemData.j;

    for (int i = 0; i < matrixArrV.length; i++) {
      for (int j = 0; j < matrixArrV.length; j++) {
        if (i == j && i != iK && i != jK) {
          matrixArrV[i][j] = 1;
        } else if (i == j) {
          matrixArrV[i][j] = cos;
        } else if ((i != iK && i != jK) || (j != iK && j != jK)) {
          matrixArrV[i][j] = 0;
        } else if (i == iK && j == jK) {
          matrixArrV[i][j] = -sin;
        } else {
          matrixArrV[i][j] = sin;
        }
      }
    }

    RealMatrix matrixV = new Array2DRowRealMatrix(matrixArrV);
    return eigenvectorsMatrix.multiply(matrixV);
  }

  private RealMatrix recalculateA(RealMatrix currentMatrixA, MaxElemData maxElemData, double cos, double sin) {

//    for (int i = 0; i < 3; i++) {
//      System.out.println(Arrays.toString(currentMatrixA.getRow(i)));
//    }
//    System.out.println(maxElemData.i);
//    System.out.println(maxElemData.j);
//    System.out.println(cos);
//    System.out.println(sin);


    double[][] matrixArrV = new double[currentMatrixA.getRowDimension()][currentMatrixA.getColumnDimension()];
    int iK = maxElemData.i;
    int jK = maxElemData.j;

    for (int i = 0; i < matrixArrV.length; i++) {
      for (int j = 0; j < matrixArrV.length; j++) {
        if (i == j && i != iK && i != jK) {
          matrixArrV[i][j] = 1;
        } else if (i == j) {
          matrixArrV[i][j] = cos;
        } else if ((i != iK && i != jK) || (j != iK && j != jK)) {
          matrixArrV[i][j] = 0;
        } else if (i == iK && j == jK) {
          matrixArrV[i][j] = -sin;
        } else {
          matrixArrV[i][j] = sin;
        }
      }
    }

    RealMatrix matrixV = new Array2DRowRealMatrix(matrixArrV);

//    for (int i = 0; i < 3; i++) {
//      System.out.println(Arrays.toString(matrixV.getRow(i)));
//    }

    return matrixV.transpose().multiply(currentMatrixA).multiply(matrixV);
  }

//  private RealMatrix recalculateX(RealMatrix eigenvectorsMatrix, MaxElemData maxElemData, double cos, double sin) {
//    double[][] newIter = new double[eigenvectorsMatrix.getRowDimension()][eigenvectorsMatrix.getColumnDimension()];
//    int iK = maxElemData.i;
//    int jK = maxElemData.j;
//
//    for (int i = 0; i < newIter.length; i++) {
//      for (int j = 0; j < newIter.length; j++) {
//        if (j != iK && j != jK) newIter[i][j] = eigenvectorsMatrix.getEntry(i, j);
//
//        else if (j == iK && i != iK && i != jK) newIter[i][j] =
//                cos * eigenvectorsMatrix.getEntry(i, iK)
//                        + sin * eigenvectorsMatrix.getEntry(i, jK);
//
//        else if (j == jK && i != iK && i != jK) newIter[i][j] =
//                -sin * eigenvectorsMatrix.getEntry(i, iK)
//                        + cos * eigenvectorsMatrix.getEntry(i, jK);
//
//        else if (i == iK && j == iK) newIter[i][j] =
//                Math.pow(cos, 2) * eigenvectorsMatrix.getEntry(iK, iK)
//                        + 2 * cos * sin * eigenvectorsMatrix.getEntry(iK, jK)
//                        + Math.pow(sin, 2) * eigenvectorsMatrix.getEntry(jK, jK);
//
//        else if (i == jK && j == jK) newIter[i][j] =
//                Math.pow(sin, 2) * eigenvectorsMatrix.getEntry(iK, iK)
//                        - 2 * cos * sin * eigenvectorsMatrix.getEntry(iK, jK)
//                        + Math.pow(cos, 2) * eigenvectorsMatrix.getEntry(jK, jK);
//
//        else newIter[i][j] = 0;
//      }
//    }
//
//    return new Array2DRowRealMatrix(newIter);
//  }
//
//  private RealMatrix recalculateA(RealMatrix currentMatrixA, MaxElemData maxElemData, double cos, double sin) {
//
//    double[][] newIter = new double[currentMatrixA.getRowDimension()][currentMatrixA.getColumnDimension()];
//    int iK = maxElemData.i;
//    int jK = maxElemData.j;
//
//    for (int i = 0; i < newIter.length; i++) {
//      for (int j = 0; j < newIter.length; j++) {
//        if (i != iK && i != jK) newIter[i][j] = currentMatrixA.getEntry(i, j);
//
//        else if (i == iK && j != iK && j != jK) newIter[i][j] =
//                cos * currentMatrixA.getEntry(i, iK)
//                        + sin * currentMatrixA.getEntry(i, jK);
//
//        else if (i == jK && j != iK && j != jK) newIter[i][j] =
//                -sin * currentMatrixA.getEntry(i, iK)
//                        + cos * currentMatrixA.getEntry(i, jK);
//
//        else if (i == iK && j == iK) newIter[i][j] =
//                Math.pow(cos, 2) * currentMatrixA.getEntry(iK, iK)
//                        + 2 * cos * sin * currentMatrixA.getEntry(iK, jK)
//                        + Math.pow(sin, 2) * currentMatrixA.getEntry(jK, jK);
//
//        else if (i == jK && j == jK) newIter[i][j] =
//                Math.pow(sin, 2) * currentMatrixA.getEntry(iK, iK)
//                        - 2 * cos * sin * currentMatrixA.getEntry(iK, jK)
//                        + Math.pow(cos, 2) * currentMatrixA.getEntry(jK, jK);
//
//        else newIter[i][j] = 0;
//      }
//    }
//
//    return new Array2DRowRealMatrix(newIter);
//  }



  private double calculateSin(MaxElemData maxElemData, RealMatrix matrix) {
    double temp = calculateFraction(maxElemData, matrix);

    temp = Math.sqrt((1 - temp) / 2.0);

    double sign = Math.signum(
            matrix.getEntry(maxElemData.i, maxElemData.j)
                    * (matrix.getEntry(maxElemData.i, maxElemData.i) - matrix.getEntry(maxElemData.j, maxElemData.j)));

    return sign * temp;
  }

  private double calculateCos(MaxElemData maxElemData, RealMatrix matrix) {
    double temp = calculateFraction(maxElemData, matrix);

    return Math.sqrt((1 + temp) / 2.0);
  }

  private static double calculateFraction(MaxElemData maxElemData, RealMatrix matrix) {
    double d = Math.sqrt(
            Math.pow(matrix.getEntry(maxElemData.i, maxElemData.i) - matrix.getEntry(maxElemData.j, maxElemData.j), 2)
                    + 4 * Math.pow(matrix.getEntry(maxElemData.i, maxElemData.j) , 2));

    return Math.abs(
            matrix.getEntry(maxElemData.i, maxElemData.i) - matrix.getEntry(maxElemData.j, maxElemData.j))
            / d;
  }

  private MaxElemData selectMaxUpperElem(RealMatrix matrix) {

    double sum = 0;
    for (int i = 0; i < matrix.getRowDimension(); i++) {
      for (int j = i + 1; j < matrix.getColumnDimension(); j++) {
        sum += Math.pow(matrix.getEntry(i, j), 2);
      }
    }
//    System.out.println(sum);

    double maxElem = 0;
    int maxI = -1;
    int maxJ = -1;

    for (int i = 0; i < matrix.getRowDimension(); i++) {
      for (int j = i + 1; j < matrix.getColumnDimension(); j++) {
        double currentElem = matrix.getEntry(i, j);
        if (Math.abs(currentElem) >= Math.abs(maxElem)) {
          maxElem = currentElem;
          maxI = i;
          maxJ = j;
        }
      }
    }

    return new MaxElemData(maxElem, maxI, maxJ);
  }

  @Override
  public List<Double> getEigenvalues() {
    return eigenvalues;
  }

  @Override
  public List<RealVector> getEigenvectors() {
    return eigenvectors;
  }


  private record MaxElemData(double maxElem, int i, int j) {
  }
}
