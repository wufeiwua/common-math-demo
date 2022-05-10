package org.wfw.chart.data;

import org.apache.commons.math3.stat.regression.RegressionResults;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.wfw.chart.Result;

import java.util.ArrayList;
import java.util.List;

public class LinearRegressionData {

    /**
     *
     * y = kx + b
     * f(x) = 1.5x + 0.5
     *
     * @return
     */
    public static double[][] linearScatters() {
        List<double[]> data = new ArrayList<>();
        for (double x = 0; x <= 10; x += 0.1) {
            double y = 1.5 * x + 0.5;
            y += Math.random() * 4 - 2; // 随机数
            double[] xy = {x, y};
            data.add(xy);
        }
        return data.stream().toArray(double[][]::new);
    }


    public static Result linearFit(double[][] data) {
        List<double[]> fitData = new ArrayList<>();
        SimpleRegression regression = new SimpleRegression();
        regression.addData(data); // 数据集
        RegressionResults results = regression.regress();
        double b = results.getParameterEstimate(0);
        double k = results.getParameterEstimate(1);
        double r2 = results.getRSquared();
        for (double[] datum : data) {
            double[] xy = {datum[0], k * datum[0] + b};
            fitData.add(xy);
        }

        StringBuilder func = new StringBuilder();
        func.append("f(x) =");
        func.append(b >= 0 ? " " : " - ");
        func.append(Math.abs(b));
        func.append(k > 0 ? " + " : " - ");
        func.append(Math.abs(k));
        func.append("x");

        return new Result(fitData.stream().toArray(double[][]::new), func.toString());
    }
}
