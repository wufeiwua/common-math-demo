package org.wfw.math;

import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.SimpleCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

import java.util.Arrays;

/**
 *
 * 非线性拟合 - 一元多项式拟合
 *
 */
public class SimpleCurveFitterDemo {
    // 已知函数 y = 1 + 2x + 3x^2
    public double func(double x) {
        return 1 + 2 * x + 3 * x * x;
    }

    // 生成待拟合数据
    public double[][] getPoints() {
        double[][] xy = new double[100][2];
        for (int x = 0; x < 100; x++) {
            xy[x][0] = x; // x
            xy[x][1] = func(x); // y
        }
        return xy;
    }

    public void curveFitter() {
        double[][] points = getPoints();/*待拟合数据*/
        ParametricUnivariateFunction function = new PolynomialFunction.Parametric();/*多项式函数*/
        double[] guess = {1, 2, 3}; /*猜测值 依次为 常数项、1次项、二次项*/

        // 初始化拟合
        SimpleCurveFitter curveFitter = SimpleCurveFitter.create(function,guess);

        // 添加数据点。带权重的点，我的理解这个点权重越大，拟合出来的曲线会更靠近这个点
        WeightedObservedPoints observedPoints = new WeightedObservedPoints();
        for (double[] point : points) {
            observedPoints.add(point[0], point[1]);
        }
        /*
        * best 为拟合结果
        * 依次为 常数项、1次项、二次项
        * 对应 y = a + bx + cx^2 中的 a, b, c
        * */
        double[] best = curveFitter.fit(observedPoints.toList());
        System.out.println(Arrays.toString(best));
    }

    public static void main(String[] args) {
        SimpleCurveFitterDemo simpleCurveFitterDemo = new SimpleCurveFitterDemo();
        simpleCurveFitterDemo.curveFitter();
    }
}
