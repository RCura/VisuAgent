/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projettransmondyn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 *
 * @author mehdi
 */
public class agregationAxT {

    public agregationAxT() {
    }

    public AreaChart creationchart(Statement stmt, String fonction, String indicateur, String replication, String experimentation, boolean type_agregation, String agregation_exp, String requete) {

        NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final AreaChart<Number, Number> ac
                = new AreaChart<Number, Number>(xAxis, yAxis);
        ac.setTitle("");
        xAxis.setLabel("Temps");
        yAxis.setLabel(indicateur);
        XYChart.Series series = null;

        switch (fonction) {
            case "somme":
                series = somme(stmt, fonction, indicateur, replication, experimentation, type_agregation, agregation_exp, requete);
                break;
            case "max":
                series = max(stmt, fonction, indicateur, replication, experimentation, type_agregation, agregation_exp, requete);
                break;
            case "min":
                series = min(stmt, fonction, indicateur, replication, experimentation, type_agregation, agregation_exp, requete);
                break;
            case "moyenne":
                series = moyenne(stmt, fonction, indicateur, replication, experimentation, type_agregation, agregation_exp, requete);
                break;
            case "écart type":
                series = ecart_type(stmt, fonction, indicateur, replication, experimentation, type_agregation, agregation_exp, requete);
                break;
            case "coefficient de variation":
                series = coef_variation(stmt, fonction, indicateur, replication, experimentation, type_agregation, agregation_exp, requete);
                break;
        }

        ac.getData().addAll(series);
        ac.setTitle(indicateur + " x Temps");

        String rep = null;
        if (type_agregation) {
            rep = replication + "(" + fonction + ")";
        } else {
            rep = "Toute" + "(" + fonction + ")";
        }
        if (experimentation.equals("Toute")) {
            series.setName("E :" + experimentation + "(" + agregation_exp + ")" + "/R:" + rep + "/" + indicateur);
        } else {
            series.setName("E :" + experimentation + "/R:" + rep + "/" + indicateur);
        }

        return ac;
    }

    public XYChart.Series somme(Statement stmt, String fonction, String indicateur, String replication, String experimentation, boolean type_agregation, String agregation_exp, String requete) {
        XYChart.Series series = new XYChart.Series();

        try {

            ResultSet rs = null;
            if (experimentation.equals("Toute")) {
                if (requete.equals("")) {
                    rs = stmt.executeQuery("select num_step," + agregation_exp + "(agregation) from (SELECT id_experimentation,num_step,sum(" + indicateur + ")as agregation FROM Step   group by num_step,id_experimentation)group by num_step  ;");
                } else {
                    rs = stmt.executeQuery("select num_step," + agregation_exp + "(agregation) from (SELECT id_experimentation,num_step,sum(" + indicateur + ")as agregation FROM Step where  " + requete.substring(3) + " group by num_step,id_experimentation)group by num_step  ;");
                }
            } else {
                if (type_agregation) {
                    rs = stmt.executeQuery("SELECT num_step,sum(" + indicateur + ") FROM Step  natural join replication where ID_experimentation= " + experimentation + " and id_replication=" + replication + " " + requete + " ;");
           // rs = stmt.executeQuery( "SELECT num_step,sum(num_step) FROM Step where id_replication=2 ;" );

                } else {
                    rs = stmt.executeQuery("SELECT num_step,sum(" + indicateur + ") FROM Step natural join replication where ID_experimentation= " + experimentation + " " + requete + " group by num_step  ;");

                }
            }

            while (rs.next()) {
                series.getData().add(new XYChart.Data(rs.getInt("num_step"), rs.getDouble(2)));
            }

            rs.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return series;
    }

    public XYChart.Series max(Statement stmt, String fonction, String indicateur, String replication, String experimentation, boolean type_agregation, String agregation_exp, String requete) {
        XYChart.Series series = new XYChart.Series();

        try {

            ResultSet rs = null;

            if (experimentation.equals("Toute")) {
                if (requete.equals("")) {
                    rs = stmt.executeQuery("select num_step," + agregation_exp + "(agregation) from (SELECT id_experimentation,num_step,max(" + indicateur + ")as agregation FROM Step   group by num_step,id_experimentation)group by num_step  ;");
                } else {
                    rs = stmt.executeQuery("select num_step," + agregation_exp + "(agregation) from (SELECT id_experimentation,num_step,max(" + indicateur + ")as agregation FROM Step  where  " + requete.substring(3) + " group by num_step,id_experimentation)group by num_step  ;");
                }
            } else {

                if (type_agregation) {
                    rs = stmt.executeQuery("SELECT num_step,max(" + indicateur + ") FROM Step  where ID_experimentation= " + experimentation + " and id_replication=" + replication + " " + requete + " ;");
           // rs = stmt.executeQuery( "SELECT num_step,sum(num_step) FROM Step where id_replication=2 ;" );

                } else {
                    rs = stmt.executeQuery("SELECT num_step,max(" + indicateur + ") FROM Step   where ID_experimentation= " + experimentation + " " + requete + " group by num_step  ;");
                }
            }
            while (rs.next()) {

                series.getData().add(new XYChart.Data(rs.getInt("num_step"), rs.getDouble(2)));
            }

            rs.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return series;
    }

    public XYChart.Series min(Statement stmt, String fonction, String indicateur, String replication, String experimentation, boolean type_agregation, String agregation_exp, String requete) {
        XYChart.Series series = new XYChart.Series();

        try {

            ResultSet rs = null;
            if (experimentation.equals("Toute")) {
                if (requete.equals("")) {
                    rs = stmt.executeQuery("select num_step," + agregation_exp + "(agregation) from (SELECT id_experimentation,num_step,MIN(" + indicateur + ")as agregation FROM Step   group by num_step,id_experimentation)group by num_step  ;");
                } else {
                    rs = stmt.executeQuery("select num_step," + agregation_exp + "(agregation) from (SELECT id_experimentation,num_step,MIN(" + indicateur + ")as agregation FROM Step  where  " + requete.substring(3) + " group by num_step,id_experimentation)group by num_step  ;");
                }
            } else {
                if (type_agregation) {
                    rs = stmt.executeQuery("SELECT num_step,min(" + indicateur + ") FROM Step  where ID_experimentation= " + experimentation + " and id_replication=" + replication + " " + requete + " ;");
           // rs = stmt.executeQuery( "SELECT num_step,sum(num_step) FROM Step where id_replication=2 ;" );

                } else {
                    rs = stmt.executeQuery("SELECT num_step,min(" + indicateur + ") FROM Step  where ID_experimentation= " + experimentation + " " + requete + "  group by num_step  ;");
                }
            }
            while (rs.next()) {

                series.getData().add(new XYChart.Data(rs.getInt("num_step"), rs.getDouble(2)));

            }
            rs.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return series;
    }

    public XYChart.Series moyenne(Statement stmt, String fonction, String indicateur, String replication, String experimentation, boolean type_agregation, String agregation_exp, String requete) {
        XYChart.Series series = new XYChart.Series();

        try {

            ResultSet rs = null;
            if (experimentation.equals("Toute")) {
                if (requete.equals("")) {
                    rs = stmt.executeQuery("select num_step," + agregation_exp + "(agregation) from (SELECT id_experimentation,num_step,avg(" + indicateur + ")as agregation FROM Step   group by num_step,id_experimentation)group by num_step  ;");
                } else {
                    rs = stmt.executeQuery("select num_step," + agregation_exp + "(agregation) from (SELECT id_experimentation,num_step,avg(" + indicateur + ")as agregation FROM Step  where  " + requete.substring(3) + " group by num_step,id_experimentation)group by num_step  ;");
                }
            } else {
                if (type_agregation) {
                    rs = stmt.executeQuery("SELECT num_step,avg(" + indicateur + ") FROM Step  where ID_experimentation= " + experimentation + " and id_replication=" + replication + " " + requete + "  ;");
           // rs = stmt.executeQuery( "SELECT num_step,sum(num_step) FROM Step where id_replication=2 ;" );

                } else {
                    rs = stmt.executeQuery("SELECT num_step,avg(" + indicateur + ") FROM Step  where ID_experimentation= " + experimentation + " " + requete + " group by num_step  ;");
                }
            }
            while (rs.next()) {

                series.getData().add(new XYChart.Data(rs.getInt("num_step"), rs.getDouble(2)));

            }
            rs.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return series;
    }

    public XYChart.Series ecart_type(Statement stmt, String fonction, String indicateur, String replication, String experimentation, boolean type_agregation, String agregation_exp, String requete) {
        XYChart.Series series = new XYChart.Series();

        try {

            ResultSet rs = null;
            if (experimentation.equals("Toute")) {
                if (requete.equals("")) {
                    rs = stmt.executeQuery("select num_step," + agregation_exp + "(agregation) from (SELECT id_experimentation,num_step,avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + ") as agregation FROM Step   group by num_step,id_experimentation)group by num_step  ;");
                } else {
                    rs = stmt.executeQuery("select num_step," + agregation_exp + "(agregation) from (SELECT id_experimentation,num_step,avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + ")as agregation FROM Step  where  " + requete.substring(3) + " group by num_step,id_experimentation)group by num_step  ;");
                }
            } else {
                if (type_agregation) {
                    rs = stmt.executeQuery("SELECT num_step,avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + ")  FROM Step  where ID_experimentation= " + experimentation + " and id_replication=" + replication + " " + requete + "  ;");
           // rs = stmt.executeQuery( "SELECT num_step,sum(num_step) FROM Step where id_replication=2 ;" );

                } else {
                    rs = stmt.executeQuery("SELECT num_step,avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + ")  FROM Step  where ID_experimentation= " + experimentation + " " + requete + " group by num_step   ;");
                }
            }
            while (rs.next()) {

                series.getData().add(new XYChart.Data(rs.getInt("num_step"), Math.sqrt(rs.getDouble(2))));
            }

            rs.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return series;
    }

    public XYChart.Series coef_variation(Statement stmt, String fonction, String indicateur, String replication, String experimentation, boolean type_agregation, String agregation_exp, String requete) {
        XYChart.Series series = new XYChart.Series();

        try {

            ResultSet rs = null;
            if (experimentation.equals("Toute")) {
                if (requete.equals("")) {
                    rs = stmt.executeQuery("select num_step," + agregation_exp + "(agregation) from (SELECT id_experimentation,num_step,(avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + "))/(Avg(" + indicateur + ")*Avg(" + indicateur + "))as agregation FROM Step   group by num_step,id_experimentation)group by num_step  ;");
                } else {
                    rs = stmt.executeQuery("select num_step," + agregation_exp + "(agregation) from (SELECT id_experimentation,num_step,(avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + "))/(Avg(" + indicateur + ")*Avg(" + indicateur + "))as agregation FROM Step  where  " + requete.substring(3) + " group by num_step,id_experimentation)group by num_step  ;");
                }
            } else {
                if (type_agregation) {
                    rs = stmt.executeQuery("SELECT num_step,(avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + "))/(Avg(" + indicateur + ")*Avg(" + indicateur + "))  FROM Step  where ID_experimentation= " + experimentation + " and id_replication=" + replication + " " + requete + "  ;");

                } else {
                    rs = stmt.executeQuery("SELECT num_step,(avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + "))/(Avg(" + indicateur + ")*Avg(" + indicateur + "))  FROM Step  where ID_experimentation= " + experimentation + " " + requete + " group by num_step   ;");
                }
            }
            while (rs.next()) {

                series.getData().add(new XYChart.Data(rs.getInt("num_step"), rs.getDouble(2)));

            }

            rs.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return series;
    }
}
