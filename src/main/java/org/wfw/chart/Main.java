package org.wfw.chart;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.wfw.chart.data.CustomizeCurveFitterDemo;
import org.wfw.chart.data.LinearRegressionData;
import org.wfw.chart.data.SimpleCurveFitterData;

/**
 * Date: 2021.03.27 <br>
 * Title: <br>
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author WUFEIWUA
 * @version 0.0.1
 * @since 0.0.1
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) {
        stage.setWidth(780);
        stage.setHeight(600);
        Scene scene = getScene(stage);
        stage.setScene(scene);
        stage.setTitle("Loading...");
        stage.show();
    }

    public Scene getScene(Stage stage) {
        Scene scene = new Scene(new Group());

        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

        webEngine.getLoadWorker().stateProperty()
                .addListener((ov,oldState,newState) -> {
                    if (newState == Worker.State.SUCCEEDED) {
                        stage.setTitle(webEngine.getLocation());
                    }
                });

        String index = Main.class.getResource("./html/index.html").toExternalForm();
        webEngine.load(index);
        JSObject win = (JSObject) webEngine.executeScript("window");


        Button button1 = new Button("线性拟合");
        button1.setOnAction(event -> {
            try {
                double[][] scatters = LinearRegressionData.linearScatters();
                Result result = LinearRegressionData.linearFit(scatters);
                double[][] fit = result.data;
                win.call("drawLine",
                        toJSONStr(scatters),
                        toJSONStr(fit),
                        result.func);
                // webEngine.executeScript("show()");//执行js函数
                // win.eval("line()");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button button2 = new Button("一元多项式函数");
        button2.setOnAction(event -> {
            try {
                double[][] scatters = SimpleCurveFitterData.curveScatters();
                Result result = SimpleCurveFitterData.curveFit(scatters);
                double[][] fit = result.data;
                win.call("drawLine",
                        toJSONStr(scatters),
                        toJSONStr(fit),
                        result.func);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button button3 = new Button("自定义函数");
        button3.setOnAction(event -> {
            try {
                double[][] scatters = CustomizeCurveFitterDemo.customizeFuncScatters();
                Result result = CustomizeCurveFitterDemo.customizeFuncFit(scatters);
                double[][] fit = result.data;
                win.call("drawLine",
                        toJSONStr(scatters),
                        toJSONStr(fit),
                        result.func);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 不支持 WebGL
        // Button button4 = new Button("多元多项式");
        // button4.setOnAction(event -> {
        //     try {
        //         double[][] scatters = MultipleLinearRegressionData.multiVarPolyScatters();
        //         win.call("draw3DLine");
        //     } catch (JSException e) {
        //         e.printStackTrace();
        //     }
        // });


        VBox stackPane = new VBox();
        HBox hBox = new HBox();
        hBox.getChildren().addAll(button1, button2, button3);

        stackPane.setSpacing(20);
        stackPane.getChildren().addAll(browser, hBox);
        scene.setRoot(stackPane);
        return scene;
    }

    public String toJSONStr(double[][] points) {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (int i = 0; i < points.length; i++) {
            double[] point = points[i];
            builder.append('[');
            builder.append(point[0]);
            builder.append(',');
            builder.append(point[1]);
            builder.append(']');
            builder.append(i == points.length - 1 ? "" : ',');
        }
        builder.append(']');
        return builder.toString();
    }



}


