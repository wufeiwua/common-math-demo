package org.wfw.chart.data;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import java.util.ArrayList;
import java.util.List;

/**
 * 多元多项式
 * 形如 f(x1,x2) = y = a + b*x1 + c*x2 的形式
 */
public class MultipleLinearRegressionData {


    /**
     * 生成随机数
     */
    public static double[][] randomX() {
        List<double[]> data = new ArrayList<>();
        for (double i = 0; i < 10; i += 0.1) {
            double x1 = Math.cos(i);
            double x2 = Math.sin(i);
            data.add(new double[]{x1, x2});
        }
        // List[List[Double
        return data.stream().toArray(double[][]::new);
    }

    /**
     * f(x1,x2) = y = a + b * x1 + c * sin(x2)
     * @param arr
     * @return
     */
    public static double[] randomY(double[][] arr) {
        if (arr != null && arr.length > 0) {
            int len = arr.length;
            double[] y = new double[len];
            for (int i = 0; i < len; i++) {
                // f(x1,x2) = y = 20 + x1 + 12 * sin(x2)
                double[] x = arr[i];
                y[i] = functionConstructorY(x);
            }
            return y;
        }
        return null;
    }

    /**
     * 已知的函数为: f(x1,x2) = y = 20 + 2 * x1 + 12 * sin(x2)
     * 即：f(x1,x2) = y = a + b * x1 + c * sin(x2) 中
     * a = 20, b = 2, c = 12
     * @param x
     * @return
     */
    public static double functionConstructorY(double[] x) {
        double x1 = x[0], x2 = x[1];
        return 20 + 2 * x1 + 12 * Math.sin(x2);
    }

    /**
     * f(x1,x2) = y = a + b * x1 + c * sin(x2)
     * @param ct 拟合的常数项（系数）。对应 a,b,c
     * @param x x 的值。对应 x1,x2
     * @return
     */
    public static double functionValueY(double[] ct, double[] x) {
        double a = ct[0], b = ct[1], c = ct[2];
        double x1 = x[0], x2 = x[1];
        return a + b * x1 + c * Math.sin(x2);
    }



    /**
     * 多元多项式数据
     * 已知： f(x1,x2) = y = a + b * x1 + c * sin(x2)
     * @return
     * arr[0] 对应所有的 y 的值
     * arr[1] 对应所有的 x1 的值
     * arr[2] 对应所有的 x2 的值
     */
    public static double[][] multiVarPolyScatters() {
        double[][] x = randomX();
        double[] y = randomY(x);
        OLSMultipleLinearRegression ols = new OLSMultipleLinearRegression();
        ols.newSampleData(y, x);
        // ct 即为拟合结果
        double[] ct = ols.estimateRegressionParameters();
        double v1 = ols.calculateRSquared();
        double v = ols.calculateAdjustedRSquared();


        double[] valueY = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            // 重新计算 y 的值。与原有构造的 y 对比
            valueY[i] = functionValueY(ct, x[i]);
        }

        double[][] data = new double[x.length][3];// x1, x2, y
        for (int i = 0; i < valueY.length; i++) {
            // ==================== x1 ====== x2 ======= y ====
            data[i] = new double[]{x[i][0], x[i][1], valueY[i]};
        }
        return data;
    }

    public static void main(String[] args) {
        double[][] doubles = multiVarPolyScatters();
        System.out.println(toJSONStr(doubles));

        System.out.println();
    }

    public static String toJSONStr(double[][] points) {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (int i = 0; i < points.length; i++) {
            double[] point = points[i];
            builder.append('[');
            builder.append(point[0]); // x1
            builder.append(',');
            builder.append(point[1]); // x2
            builder.append(',');
            builder.append(point[2]); // x3
            builder.append(']');
            builder.append(i == points.length - 1 ? "" : ',');
        }
        builder.append(']');
        return builder.toString();
    }
}
