/* 
 * Copyright (C) 2014 Mehdi Boukhechba
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package visuagent;

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
public class agregationAxA {

    public agregationAxA() {
    }

    public AreaChart creationchart(Statement stmt, String fonction, String indicateur, String replication, String experimentation, boolean type_agregation, String agregation_exp, String requete) {

        NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final AreaChart<Number, Number> ac
                = new AreaChart<Number, Number>(xAxis, yAxis);
        ac.setTitle("");
        xAxis.setLabel("Replications");
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

        if (experimentation.equals("Toute")) {
            if (agregation_exp.equals("")) {
                ac.setTitle(indicateur + " x experimentation");
                xAxis.setLabel("experimentation");
            } else {

                ac.setTitle(indicateur + " x Replications");
            }
            series.setName("E :" + experimentation + "(" + agregation_exp + ")" + "/R: Toute(" + fonction + ")" + "/" + indicateur);
        } else {
            series.setName("E :" + experimentation + "/R: Toute(" + fonction + ")" + indicateur);
            ac.setTitle(indicateur + " x Replications");
        }
        ac.getData().addAll(series);

        return ac;
    }

    public XYChart.Series somme(Statement stmt, String fonction, String indicateur, String replication, String experimentation, boolean type_agregation, String agregation_exp, String requete) {
        XYChart.Series series = new XYChart.Series();

        try {

            ResultSet rs = null;
            if (experimentation.equals("Toute")) {
                if (agregation_exp.equals("")) {
                    if (requete.equals("")) {
                        rs = stmt.executeQuery("SELECT ID_experimentation,sum(" + indicateur + ")as agregation FROM Step  group by ID_experimentation;");
                    } else {
                        rs = stmt.executeQuery("SELECT ID_experimentation,sum(" + indicateur + ")as agregation FROM Step  where " + requete.substring(3) + " group by ID_experimentation ;");
                    }
                } else {
                    if (requete.equals("")) {
                        rs = stmt.executeQuery("select ID_replication," + agregation_exp + "(agregation) from (SELECT ID_experimentation,id_replication,sum(" + indicateur + ")as agregation FROM Step  group by id_replication,ID_experimentation)group by ID_replication  ;");
                    } else {
                        rs = stmt.executeQuery("select ID_replication," + agregation_exp + "(agregation) from (SELECT ID_experimentation,id_replication,sum(" + indicateur + ")as agregation FROM Step  where " + requete.substring(3) + " group by id_replication,ID_experimentation)group by ID_replication  ;");
                    }
                }

            } else {
                if (type_agregation) {
                    rs = stmt.executeQuery("SELECT id_replication,sum(" + indicateur + ") FROM Step   where ID_experimentation= " + experimentation + " and id_replication=" + replication + " " + requete + " ;");
           // rs = stmt.executeQuery( "SELECT num_step,sum(num_step) FROM Step where id_replication=2 ;" );

                } else {
                    rs = stmt.executeQuery("SELECT id_replication,sum(" + indicateur + ") FROM Step  where ID_experimentation= " + experimentation + " " + requete + " group by id_replication  ;");

                }
            }
            while (rs.next()) {
                series.getData().add(new XYChart.Data(rs.getInt(1), rs.getDouble(2)));
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
                if (agregation_exp.equals("")) {
                    if (requete.equals("")) {
                        rs = stmt.executeQuery("SELECT ID_experimentation,MAX(" + indicateur + ")as agregation FROM Step  group by ID_experimentation;");
                    } else {
                        rs = stmt.executeQuery("SELECT ID_experimentation,MAX(" + indicateur + ")as agregation FROM Step  where " + requete.substring(3) + " group by ID_experimentation ;");
                    }
                } else {
                    if (requete.equals("")) {
                        rs = stmt.executeQuery("select ID_replication," + agregation_exp + "(agregation) from (SELECT ID_experimentation,id_replication,max(" + indicateur + ")as agregation FROM Step  group by id_replication,ID_experimentation)group by ID_replication  ;");
                    } else {
                        rs = stmt.executeQuery("select ID_replication," + agregation_exp + "(agregation) from (SELECT ID_experimentation,id_replication,max(" + indicateur + ")as agregation FROM Step  where " + requete.substring(3) + " group by id_replication,ID_experimentation)group by ID_replication  ;");
                    }
                }
            } else {

                if (type_agregation) {
                    rs = stmt.executeQuery("SELECT id_replication,max(" + indicateur + ") FROM Step  where ID_experimentation= " + experimentation + " and id_replication=" + replication + " " + requete + " ;");
           // rs = stmt.executeQuery( "SELECT num_step,sum(num_step) FROM Step where id_replication=2 ;" );

                } else {
                    rs = stmt.executeQuery("SELECT id_replication,max(" + indicateur + ") FROM Step   where ID_experimentation= " + experimentation + " " + requete + " group by id_replication  ;");
                }
            }
            while (rs.next()) {

                series.getData().add(new XYChart.Data(rs.getInt(1), rs.getDouble(2)));
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
                if (agregation_exp.equals("")) {
                    if (requete.equals("")) {
                        rs = stmt.executeQuery("SELECT ID_experimentation,min(" + indicateur + ")as agregation FROM Step  group by ID_experimentation;");
                    } else {
                        rs = stmt.executeQuery("SELECT ID_experimentation,min(" + indicateur + ")as agregation FROM Step  where " + requete.substring(3) + " group by ID_experimentation ;");
                    }
                } else {
                    if (requete.equals("")) {
                        rs = stmt.executeQuery("select ID_replication," + agregation_exp + "(agregation) from (SELECT ID_experimentation,id_replication,min(" + indicateur + ")as agregation FROM Step  group by id_replication,ID_experimentation)group by ID_replication  ;");
                    } else {
                        rs = stmt.executeQuery("select ID_replication," + agregation_exp + "(agregation) from (SELECT ID_experimentation,id_replication,min(" + indicateur + ")as agregation FROM Step  where " + requete.substring(3) + " group by id_replication,ID_experimentation)group by ID_replication  ;");
                    }
                }
            } else {
                if (type_agregation) {
                    rs = stmt.executeQuery("SELECT id_replication,min(" + indicateur + ") FROM Step  where ID_experimentation= " + experimentation + " and id_replication=" + replication + " " + requete + "  ;");
           // rs = stmt.executeQuery( "SELECT num_step,sum(num_step) FROM Step where id_replication=2 ;" );

                } else {
                    rs = stmt.executeQuery("SELECT id_replication,min(" + indicateur + ") FROM Step  where ID_experimentation= " + experimentation + " " + requete + " group by id_replication  ;");
                }
            }
            while (rs.next()) {

                series.getData().add(new XYChart.Data(rs.getInt(1), rs.getDouble(2)));

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
                if (agregation_exp.equals("")) {
                    if (requete.equals("")) {
                        rs = stmt.executeQuery("SELECT ID_experimentation,avg(" + indicateur + ")as agregation FROM Step  group by ID_experimentation;");
                    } else {
                        rs = stmt.executeQuery("SELECT ID_experimentation,avg(" + indicateur + ")as agregation FROM Step  where " + requete.substring(3) + " group by ID_experimentation ;");
                    }
                } else {
                    if (requete.equals("")) {
                        rs = stmt.executeQuery("select ID_replication," + agregation_exp + "(agregation) from (SELECT ID_experimentation,id_replication,avg(" + indicateur + ")as agregation FROM Step  group by id_replication,ID_experimentation)group by ID_replication  ;");
                    } else {
                        rs = stmt.executeQuery("select ID_replication," + agregation_exp + "(agregation) from (SELECT ID_experimentation,id_replication,avg(" + indicateur + ")as agregation FROM Step  where " + requete.substring(3) + " group by id_replication,ID_experimentation)group by ID_replication  ;");
                    }
                }
            } else {
                if (type_agregation) {
                    rs = stmt.executeQuery("SELECT num_step,avg(" + indicateur + ") FROM Step  where ID_experimentation= " + experimentation + " and id_replication=" + replication + "  ;");
           // rs = stmt.executeQuery( "SELECTnum_step num_step,sum(num_step) FROM Step where id_replication=2 ;" );

                } else {
                    rs = stmt.executeQuery("SELECT id_replication,avg(" + indicateur + ") FROM Step  where ID_experimentation= " + experimentation + " " + requete + " group by id_replication  ;");
                }
            }
            while (rs.next()) {

                series.getData().add(new XYChart.Data(rs.getInt(1), rs.getDouble(2)));

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
                if (agregation_exp.equals("")) {
                    if (requete.equals("")) {
                        rs = stmt.executeQuery("SELECT ID_experimentation,avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + ")as agregation FROM Step  group by ID_experimentation;");
                    } else {
                        rs = stmt.executeQuery("SELECT ID_experimentation,avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + ")as agregation FROM Step  where " + requete.substring(3) + " group by ID_experimentation ;");
                    }
                } else {
                    if (requete.equals("")) {
                        rs = stmt.executeQuery("select ID_replication," + agregation_exp + "(agregation) from (SELECT ID_experimentation,id_replication,avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + ")as agregation FROM Step  group by id_replication,ID_experimentation)group by ID_replication  ;");
                    } else {
                        rs = stmt.executeQuery("select ID_replication," + agregation_exp + "(agregation) from (SELECT ID_experimentation,id_replication,avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + ")as agregation FROM Step  where " + requete.substring(3) + " group by id_replication,ID_experimentation)group by ID_replication  ;");
                    }
                }
            } else {
                if (type_agregation) {
                    rs = stmt.executeQuery("SELECT id_replication,avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + ")  FROM Step  where ID_experimentation= " + experimentation + " and id_replication=" + replication + " " + requete + " ;");
           // rs = stmt.executeQuery( "SELECT num_step,sum(num_step) FROM Step where id_replication=2 ;" );

                } else {
                    rs = stmt.executeQuery("SELECT id_replication,avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + ")  FROM Step  where ID_experimentation= " + experimentation + " " + requete + " group by id_replication   ;");
                }
            }
            while (rs.next()) {

                series.getData().add(new XYChart.Data(rs.getInt(1), Math.sqrt(rs.getDouble(2))));
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
                if (agregation_exp.equals("")) {
                    if (requete.equals("")) {
                        rs = stmt.executeQuery("SELECT ID_experimentation,(avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + "))/(Avg(" + indicateur + ")*Avg(" + indicateur + "))as agregation FROM Step  group by ID_experimentation;");
                    } else {
                        rs = stmt.executeQuery("SELECT ID_experimentation,(avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + "))/(Avg(" + indicateur + ")*Avg(" + indicateur + "))as agregation FROM Step  where " + requete.substring(3) + " group by ID_experimentation ;");
                    }
                } else {
                    if (requete.equals("")) {
                        rs = stmt.executeQuery("select ID_replication," + agregation_exp + "(agregation) from (SELECT ID_experimentation,id_replication,(avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + "))/(Avg(" + indicateur + ")*Avg(" + indicateur + "))as agregation FROM Step  group by id_replication,ID_experimentation)group by ID_replication  ;");
                    } else {
                        rs = stmt.executeQuery("select ID_replication," + agregation_exp + "(agregation) from (SELECT ID_experimentation,id_replication,(avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + "))/(Avg(" + indicateur + ")*Avg(" + indicateur + "))as agregation FROM Step  where " + requete.substring(3) + " group by id_replication,ID_experimentation)group by ID_replication  ;");
                    }
                }
            } else {
                if (type_agregation) {
                    rs = stmt.executeQuery("SELECT id_replication,(avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + "))/(Avg(" + indicateur + ")*Avg(" + indicateur + "))  FROM Step  where ID_experimentation= " + experimentation + " and id_replication=" + replication + " " + requete + "  ;");

                } else {
                    rs = stmt.executeQuery("SELECT id_replication,(avg(" + indicateur + "*" + indicateur + ")-Avg(" + indicateur + ")*Avg(" + indicateur + "))/(Avg(" + indicateur + ")*Avg(" + indicateur + "))  FROM Step  where ID_experimentation= " + experimentation + " " + requete + " group by id_replication  ;");
                }

            }
            while (rs.next()) {

                series.getData().add(new XYChart.Data(rs.getInt(1), rs.getDouble(2)));

            }

            rs.close();

        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return series;
    }
}
