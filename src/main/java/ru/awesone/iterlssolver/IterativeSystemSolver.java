package ru.awesone.iterlssolver;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.List;
import java.util.logging.Logger;
import ru.awesone.Matrix;

/**
 * Решатель линейных систем (матричных уравнений x = Hx + g) итеративными методами.
 */
public class IterativeSystemSolver {

  static Logger log = Logger.getLogger(IterativeSystemSolver.class.getName());
  private final Matrix matrixH;
  private final List<Double> vectorG;
  private List<Double> result;
  private double epsilon = 1e-3;

  /**
   * Создание решателя линейных систем вида x = Hx + g.
   *
   * @param hMatrix матрица H
   * @param gVector вектор g
   */
  public IterativeSystemSolver(Matrix hMatrix, List<Double> gVector) {
    matrixH = hMatrix;
    vectorG = gVector;
  }

  /**
   * Создание решателя линейных систем вида x = Hx + g.
   *
   * @param hMatrix матрица H
   * @param gVector вектор g
   * @param eps точность для поиска решения
   */
  public IterativeSystemSolver(Matrix hMatrix, List<Double> gVector, double eps) {
    this(hMatrix, gVector);
    epsilon = eps;
  }

  public void simpleIterationMethod() {
    // TODO: 16.10.2023 4 - метод простых итераций
    throw new UnsupportedOperationException();
  }

  public void seidelMethod() {
    // TODO: 16.10.2023 5 - метод Зейделя
    throw new UnsupportedOperationException();
  }

  public void upperRelaxationMethod() {
    // TODO: 16.10.2023 7 - метод верхней релаксации
    throw new UnsupportedOperationException();
  }

  public double getSpectralRadius() {
    // TODO: 16.10.2023 6 - получение спектрального радиуса
    throw new UnsupportedOperationException();
  }

  /**
   * Проверка на сходимость итеративных методов.
   *
   * @return true - если сходится, false - если нет
   */
  public boolean isConverge() {
    if (matrixH.getMatrixNorm() < 1) return TRUE;
    return FALSE;
  }



}
