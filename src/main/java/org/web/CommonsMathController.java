package org.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wfw.chart.Result;
import org.wfw.chart.data.CustomizeCurveFitterDemo;
import org.wfw.chart.data.LinearRegressionData;
import org.wfw.chart.data.MultipleLinearRegressionData;
import org.wfw.chart.data.SimpleCurveFitterData;

@Controller
public class CommonsMathController {

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public String redirect() {
        return "index";
    }

    @ResponseBody
    @GetMapping("linear")
    public Ret linear() {
        double[][] scatters = LinearRegressionData.linearScatters();
        Result result = LinearRegressionData.linearFit(scatters);
        return new Ret(scatters, result);
    }

    @ResponseBody
    @GetMapping("simple")
    public Ret simple(){
        double[][] scatters = SimpleCurveFitterData.curveScatters();
        Result result = SimpleCurveFitterData.curveFit(scatters);
        return new Ret(scatters, result);
    }

    @ResponseBody
    @GetMapping("customize")
    public Ret customize(){
        double[][] scatters = CustomizeCurveFitterDemo.customizeFuncScatters();
        Result result = CustomizeCurveFitterDemo.customizeFuncFit(scatters);
        return new Ret(scatters, result);
    }

    @ResponseBody
    @GetMapping("multiple")
    public Ret multiple(){
        double[][] scatters = MultipleLinearRegressionData.multiScatters();
        // 拆分数据
        double[][] xy = new double[scatters.length][2];
        double[] z = new double[xy.length];
        for (int i = 0; i < scatters.length; i++) {
            xy[i] = new double[]{scatters[i][0], scatters[i][1]};
            z[i] = scatters[i][2];
        }
        Result result = MultipleLinearRegressionData.multiFit(xy, z);
        return new Ret(scatters, result);
    }


}
