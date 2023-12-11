package ru.awesone.eigenvalues.solver;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;
import java.util.Vector;

public interface EigenvalueSolver {
  void solve(RealMatrix matrix);

  List<Double> getEigenvalues();

  List<RealVector> getEigenvectors();
}
