package org.wfw.chart.data;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.SimpleCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.wfw.chart.Result;

import java.util.ArrayList;
import java.util.List;

public class CustomizeCurveFitterDemo {

    static class MyFunction implements ParametricUnivariateFunction {
        public double value(double x, double ... parameters) {
            double a = parameters[0];
            double b = parameters[1];
            double c = parameters[2];
            double d = parameters[3];
            return d + ((a - d) / (1 + Math.pow(x / c, b)));
        }

        public double[] gradient(double x, double ... parameters) {
            double a = parameters[0];
            double b = parameters[1];
            double c = parameters[2];
            double d = parameters[3];

            double[] gradients = new double[4];
            double den = 1 + Math.pow(x / c, b);

            gradients[0] = 1 / den; // 对 a 求导

            gradients[1] = -((a - d) * Math.pow(x / c, b) * Math.log(x / c)) / (den * den); // 对 b 求导

            gradients[2] = (b * Math.pow(x / c, b - 1) * (x / (c * c)) * (a - d)) / (den * den); // 对 c 求导

            gradients[3] = 1 - (1 / den); // 对 d 求导

            return gradients;

        }
    }

    /**
     *
     * <pre>
     *     f(x) = d + ((a - d) / (1 + Math.pow(x / c, b)))
     *     a = 1500
     *     b = 0.95
     *     c = 65
     *     d = 35000
     * </pre>
     *
     * @return
     */
    public static double[][] customizeFuncScatters() {
        MyFunction function = new MyFunction();
        List<double[]> data = new ArrayList<>();
        for (double x = 7; x <= 10000; x *= 1.5) {
            double y = function.value(x, 1500, 0.95, 65, 35000);
            y += Math.random() * 5000 - 2000; // 随机数
            double[] xy = {x, y};
            data.add(xy);
        }
        return data.stream().toArray(double[][]::new);
    }

    public static Result customizeFuncFit(double[][] scatters) {
        // if (true) {
        //     return new Result(new double[][]{}, "");
        // }
        ParametricUnivariateFunction function = new MyFunction();/*多项式函数*/
        double[] guess = {1500, 0.95, 65, 35000}; /*猜测值 依次为 a b c d 。必须和 gradient 方法返回数组对应*/

        // 初始化拟合
        SimpleCurveFitter curveFitter = SimpleCurveFitter.create(function,guess);

        // 添加数据点
        WeightedObservedPoints observedPoints = new WeightedObservedPoints();
        for (double[] point : scatters) {
            observedPoints.add(point[0], point[1]);
        }
        /*
         * best 为拟合结果 对应 a b c d
         * 可能会出现无法拟合的情况
         * 需要合理设置初始值
         * */
        double[] best = curveFitter.fit(observedPoints.toList());
        double a = best[0];
        double b = best[1];
        double c = best[2];
        double d = best[3];

        List<double[]> fitData = new ArrayList<>();
        for (double[] datum : scatters) {
            double x = datum[0];
            double y = function.value(x, a, b, c, d);
            double[] xy = {x, y};
            fitData.add(xy);
        }

        // f(x) = d + ((a - d) / (1 + Math.pow(x / c, b)))
        StringBuilder func = new StringBuilder();
        func.append("f(x) =");
        func.append(d > 0 ? " " : " - ");
        func.append(Math.abs(d));
        func.append(" ((");
        func.append(a > 0 ? "" : "-");
        func.append(Math.abs(a));
        func.append(d > 0 ? " - " : " + ");
        func.append(Math.abs(d));
        func.append(" / (1 + ");
        func.append("(x / ");
        func.append(c > 0 ? "" : " - ");
        func.append(Math.abs(c));
        func.append(") ^ ");
        func.append(b > 0 ? " " : " - ");
        func.append(Math.abs(b));

        return new Result(fitData.stream().toArray(double[][]::new), func.toString());
    }
}
