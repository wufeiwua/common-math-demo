package org.wfw.math;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.fitting.leastsquares.MultivariateJacobianFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.Pair;

public class JacobianDemo1 {

    public static void main(String[] args) {

        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        double[] z = {12, 24, 40, 60, 84};

        //  f(x,y) = a + b*x^c + d*y^e
        MultivariateJacobianFunction function = new MultivariateJacobianFunction() {
            @Override
            public Pair<RealVector, RealMatrix> value(final RealVector point) {
                double a = point.getEntry(0);
                double b = point.getEntry(1);
                double c = point.getEntry(2);
                double d = point.getEntry(3);
                double e = point.getEntry(4);
                RealVector value = new ArrayRealVector(x.length);

                // 因为未知数有5个，所以矩阵的列是 5
                RealMatrix jacobian = new Array2DRowRealMatrix(x.length, 5);
                for (int i = 0; i < x.length; i++) {
                    // f(a,b,c,d,e) = a + b*x^c + d*y^e
                    double val = a + b * Math.pow(x[i], c) + d * Math.pow(y[i], e);
                    value.setEntry(i, val);
                    // 分别对 a,b,c,d,e 求偏导数
                    jacobian.setEntry(i, 0, 1.0);
                    jacobian.setEntry(i, 1, Math.pow(x[i], c));
                    jacobian.setEntry(i, 2, b * Math.log(x[i]) * Math.pow(x[i], c));
                    jacobian.setEntry(i, 3, Math.pow(y[i], e));
                    jacobian.setEntry(i, 4, d * Math.log(y[i]) * Math.pow(y[i], e));
                }
                return new Pair<RealVector, RealMatrix>(value, jacobian);
            }
        };

        // 猜测值
        double[] initialGuess = {1, 1, 1, 1, 1};


        // z 的值
        RealVector targetValues = new ArrayRealVector(z);

        // 定义最小二乘问题
        LeastSquaresProblem problem = new LeastSquaresBuilder()
                .start(initialGuess)
                .model(function)
                .target(targetValues)
                .lazyEvaluation(false)
                .maxEvaluations(Integer.MAX_VALUE)
                .maxIterations(Integer.MAX_VALUE)
                .build();

        // 使用 LM 算法拟合
        LevenbergMarquardtOptimizer optimizer = new LevenbergMarquardtOptimizer();
        LeastSquaresOptimizer.Optimum optimize = optimizer.optimize(problem);

        // 拟合结果
        double[] array = optimize.getPoint().toArray();


        // 计算 R^2
        double rms = optimize.getRMS();
        double yMean = StatUtils.mean(z);
        double yVar = StatUtils.variance(z, yMean);
        double rr = 1 - rms * rms / yVar;

        System.out.println(rr);

        double a = array[0];
        double b = array[1];
        double c = array[2];
        double d = array[3];
        double e = array[4];
        for (int i = 0; i < z.length; i++) {
            double dz = a + b * Math.pow(x[i], c) + d * Math.pow(y[i], e);
            System.out.println("f(x,y)=" + dz + ", z_i =" + z[i]);
        }

    }
}

