/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projettransmondyn;

import static java.lang.Thread.yield;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javax.swing.JOptionPane;

/**
 *
 * @author mehdi
 */
public class AxT {

    private String title;
    private XYChart.Series serie = new XYChart.Series();

    public AxT() {

    }

    public AreaChart creationchart(Statement stmt, String indicateur, int replication, String experimentation, String requete, String agregation_exp) {

        NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final AreaChart<Number, Number> ac
                = new AreaChart<Number, Number>(xAxis, yAxis);
        ac.setTitle("");
        xAxis.setLabel("Temps");
        yAxis.setLabel(indicateur);
        XYChart.Series series = new XYChart.Series();

        if (experimentation.equals("Toute")) {
            series.setName("E :" + experimentation + "(" + agregation_exp + ")" + "/R:" + replication + "/" + indicateur);
        } else {
            series.setName("E :" + experimentation + "/R:" + replication + "/" + indicateur);
        }

        try {
            ResultSet rs = null;
            if (experimentation.equals("Toute")) {

                rs = stmt.executeQuery("SELECT num_step," + agregation_exp + "(" + indicateur + ") FROM Step  where  id_Replication =" + replication + " " + requete + " group by num_step ;");

            } else {
                rs = stmt.executeQuery("SELECT num_step," + indicateur + " FROM Step  where ID_experimentation= " + experimentation + " and  id_Replication =" + replication + " " + requete + "  ;");

            }
            while (rs.next()) {

                series.getData().add(new XYChart.Data(rs.getInt("num_step"), rs.getDouble(2)));

                yield();

            }
            rs.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        ac.getData().addAll(series);
        ac.setTitle(indicateur + " x Temps");

        return ac;
    }

    public void modificationchart(AreaChart chart, int replication, String experimentation, boolean iteratif, String requete, String agregation_exp) {

        XYChart.Series series1 = new XYChart.Series();

        if (experimentation.equals("Toute")) {
            series1.setName("E :" + experimentation + "(" + agregation_exp + ")" + "/R:" + replication + "/" + chart.getYAxis().getLabel());
        } else {
            series1.setName("E :" + experimentation + "/R:" + replication + "/" + chart.getYAxis().getLabel());
        }

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:visuAgent.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            ResultSet rs = null;

            if (experimentation.equals("Toute")) {

                rs = stmt.executeQuery("SELECT num_step," + agregation_exp + "(" + chart.getYAxis().getLabel() + ") FROM Step  where  id_Replication =" + replication + " " + requete + " group by num_step ;");

            } else {
                rs = stmt.executeQuery("SELECT num_step," + chart.getYAxis().getLabel() + " FROM Step  where ID_experimentation= " + experimentation + " and  id_Replication =" + replication + " " + requete + "  ;");

            }
            while (rs.next()) {

                series1.getData().add(new XYChart.Data(rs.getInt("num_step"), rs.getDouble(2)));

            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        if (iteratif) {
            chart.getData().clear();
        }
        chart.getData().add(series1);

    }

    public void updatechartSynchrone(Statement stmt, int num_step, double indicateur, AreaChart chart) {

        ((XYChart.Series) chart.getData().get(0)).getData().add(new XYChart.Data(num_step, indicateur));

    }
}
