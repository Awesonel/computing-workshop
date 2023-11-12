package ru.awesone.iterlssolver;

import java.util.Arrays;
import java.util.List;
import ru.awesone.Matrix;
import ru.awesone.lssolver.LinearSystemSolver;

public class IterativeSystemSolverApp {

  public static final Matrix MATRIX = new Matrix(new double[][]{
      {1, 2, 3},
      {4, 8, 1},
      {2, 3, 5}});
  public static final List<Double> VECTOR = Arrays.asList(
      17.0,
      24.0,
      28.0);

  public static IterativeSystemSolver transformEquation() {
    // TODO: 16.10.2023 2 - трансформация уравнения к необходимому виду

    throw new UnsupportedOperationException();
  }

  private List<Double> getGaussSolution() {
    return (new LinearSystemSolver(MATRIX, VECTOR))
        .gaussianSchemePrincipalElementSelectionByColumn();
  }

  private int aLotOfEstimations() {
    // TODO: 16.10.2023 ну там типа много оценок всяких надо,
    //  я ещё не разобрался, поэтому пока что пусть будет так, потом сделаю это
    throw new UnsupportedOperationException();
  }

}
