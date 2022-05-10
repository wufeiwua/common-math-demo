package org.web;

import org.wfw.chart.Result;

public class Ret {
    private Result fitRes;
    private double[][] scatters;

    public Ret(double[][] scatters, Result fitRes) {
        this.scatters = scatters;
        this.fitRes = fitRes;
    }

    public Result getFitRes() {
        return fitRes;
    }

    public void setFitRes(Result fitRes) {
        this.fitRes = fitRes;
    }

    public double[][] getScatters() {
        return scatters;
    }

    public void setScatters(double[][] scatters) {
        this.scatters = scatters;
    }
}
