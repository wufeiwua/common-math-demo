package org.wfw.math;


import org.apache.commons.math3.stat.regression.RegressionResults;
import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 *
 * 拟合线性函数
 *      f(x) = kx + b
 *
 *
 *
 */
public class LinearRegressionDemo {


    /**
     * points[0] == x 存放 x 值
     * points[1] == y 存放 y 值
     */
    private double[][] points;

    /**
     *
     *
     * 初始化点集合 200 个点
     *
     * 假设 k = 2 , b = 3
     * 则: f(x) = 2x + 3
     *
     */
    public LinearRegressionDemo() {
        points = new double[200][2];
        for (int i = 0; i < 200; i++) {
            points[i][0] = i; // x
            points[i][1] = 2 * i + 3; // y
        }
    }

    public void regression() {
        SimpleRegression regression = new SimpleRegression();
        regression.addData(points); // 数据集
        /*
        * RegressionResults 中是拟合的结果
        * 其中重要的几个参数如下：
        *   parameters:
        *      0: b
        *      1: k
        *   globalFitInfo
        *      0: 平方误差之和, SSE
        *      1: 平方和, SST
        *      2: R 平方, RSQ
        *      3: 均方误差, MSE
        *      4: 调整后的 R 平方, adjRSQ
        *
        * */
        RegressionResults results = regression.regress();

        System.out.println("b = "+results.getParameterEstimate(0));
        System.out.println("k = "+results.getParameterEstimate(1));
        System.out.println("r^2 = "+results.getRSquared());
        System.out.println("adjusted r^2 = "+results.getAdjustedRSquared());
    }

    public static void main(String[] args) {
        LinearRegressionDemo regressionDemo = new LinearRegressionDemo();
        regressionDemo.regression();
    }
}
