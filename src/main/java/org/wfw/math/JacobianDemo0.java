package org.wfw.math;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.fitting.leastsquares.MultivariateJacobianFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.Pair;

public class JacobianDemo0 {
    public static void main(String[] args) {

        final double[] x_Data = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        final double[] F_x = new double[]{2, 4, 6, 8, 11, 13, 14, 15, 18};

        // f(x) = kx + b
        MultivariateJacobianFunction jacobianFunction = point -> {
            double k = point.getEntry(0);
            double b = point.getEntry(1);

            ArrayRealVector value = new ArrayRealVector(F_x.length);
            // !!! 需要拟合 k 和 b 两个参数，所以矩阵的列是 2  !!!
            Array2DRowRealMatrix jacobian = new Array2DRowRealMatrix(F_x.length, 2);

            // 转为雅可比矩阵
            for (int i = 0; i < F_x.length; i++) {
                double x = x_Data[i];

                // f(k,b) = kx + b
                double fx = k * x + b;
                value.setEntry(i, fx);

                // 分别对 k 和 b 求偏导转为雅可比矩阵
                jacobian.setEntry(i, 0, x); // ∂f/∂k = x
                jacobian.setEntry(i, 1, 1); // ∂f/∂b = 1

            }
            return new Pair<>(value, jacobian);
        };


        // 猜测值
        double[] initialGuess = {1, 1};

        // f(x) 的值
        RealVector targetValues = new ArrayRealVector(F_x);

        // 转为最小二乘问题
        LeastSquaresProblem problem = new LeastSquaresBuilder()
                .start(initialGuess)
                .model(jacobianFunction)
                .target(targetValues)
                .lazyEvaluation(false)
                .maxEvaluations(Integer.MAX_VALUE)
                .maxIterations(Integer.MAX_VALUE)
                .build();

        // 使用 LM 计算结果
        LevenbergMarquardtOptimizer optimizer = new LevenbergMarquardtOptimizer();
        LeastSquaresOptimizer.Optimum optimize = optimizer.optimize(problem);

        // 拟合结果
        double[] array = optimize.getPoint().toArray();

        // 计算 R^2
        double rms = optimize.getRMS();
        double yMean = StatUtils.mean(F_x);
        double yVar = StatUtils.variance(F_x, yMean);
        double rr = 1 - rms * rms / yVar;

        System.out.println("R^2 = " + rr);

        double k = array[0];
        double b = array[1];
        for (int i = 0; i < F_x.length; i++) {
            double x = x_Data[i];
            // 根据拟合结果计算原有值
            double y = k * x + b;
            System.out.println("f(x)=" + y + ", y_Data_i =" + F_x[i]);
        }
    }
}
