package org.wfw.chart.data;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.SimpleCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.wfw.chart.Result;

import java.util.ArrayList;
import java.util.List;

public class SimpleCurveFitterData {


    /**
     *
     * f(x) = 1 + 2x + 3x^2
     *
     * @return
     */
    public static double[][] curveScatters() {
        List<double[]> data = new ArrayList<>();
        for (double x = 0; x <= 20; x += 1) {
            double y = 1 + 2 * x + 3 * x * x;
            y += Math.random() * 60 - 10; // 随机数
            double[] xy = {x, y};
            data.add(xy);
        }

        return data.stream().toArray(double[][]::new);
    }

    public static Result curveFit(double[][] data) {
        ParametricUnivariateFunction function = new PolynomialFunction.Parametric();/*多项式函数*/
        double[] guess = {1, 2, 3}; /*猜测值 依次为 常数项、1次项、二次项*/

        // 初始化拟合
        SimpleCurveFitter curveFitter = SimpleCurveFitter.create(function,guess);

        // 添加数据点
        WeightedObservedPoints observedPoints = new WeightedObservedPoints();
        for (double[] point : data) {
            observedPoints.add(point[0], point[1]);
        }
        /*
         * best 为拟合结果
         * 依次为 常数项、1次项、二次项
         * 对应 y = a + bx + cx^2 中的 a, b, c
         * */
        double[] best = curveFitter.fit(observedPoints.toList());

        /*
        * 根据拟合结果重新计算
        * */
        List<double[]> fitData = new ArrayList<>();
        for (double[] datum : data) {
            double x = datum[0];
            double y = best[0] + best[1] * x + best[2] * x * x; // y = a + bx + cx^2
            double[] xy = {x, y};
            fitData.add(xy);
        }


        StringBuilder func = new StringBuilder();
        func.append("f(x) =");
        func.append(best[0] > 0 ? " " : " - ");
        func.append(Math.abs(best[0]));
        func.append(best[1] > 0 ? " + " : " - ");
        func.append(Math.abs(best[1]));
        func.append("x");
        func.append(best[2] > 0 ? " + " : " - ");
        func.append(Math.abs(best[2]));
        func.append("x^2");

        return new Result(fitData.stream().toArray(double[][]::new), func.toString());
    }
}
