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
import java.sql.Statement;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
//

/**
 *
 * @author mehdi
 */
public class AxA {

    Color[] colors = new Color[]{Color.web("#008299"), Color.web("#2672EC"), Color.web("#8C0095"), Color.web("#5133AB"), Color.web("#AC193D"), Color.web("#D24726"), Color.web("#008A00"), Color.web("#094AB2")};
    int rang = 0;

    public AxA() {
    }

    public ScatterChart<Number, Number> creationchart(Statement stmt, String indicateur1, String indicateur2, int replication, String experimentation, double maxtemps, String requete, String agregation_exp) {
        NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final ScatterChart<Number, Number> ac
                = new ScatterChart<Number, Number>(xAxis, yAxis);
        ac.setTitle("");
        xAxis.setLabel(indicateur2);
        yAxis.setLabel(indicateur1);

        XYChart.Series series = new XYChart.Series();

        if (experimentation.equals("Toute")) {
            series.setName("E :" + experimentation + "(" + agregation_exp + ")" + "/R:" + replication + "/" + indicateur1);
        } else {
            series.setName("E :" + experimentation + "/R:" + replication + "/" + indicateur1 + " - " + indicateur2);
        }

        try {

           
            ResultSet rs = null;
            if (experimentation.equals("Toute")) {
                if (indicateur1 == indicateur2) {
                    rs = stmt.executeQuery("SELECT " + agregation_exp + "(" + indicateur2 + ")as " + indicateur2 + "  FROM Step  where id_Replication =" + replication + " " + requete + " group by num_step ;");
                } else {
                    rs = stmt.executeQuery("SELECT " + agregation_exp + "(" + indicateur2 + ")as " + indicateur2 + " ," + agregation_exp + "(" + indicateur1 + ") as " + indicateur2 + " FROM Step  where  id_Replication =" + replication + "  " + requete + " group by num_step ;");
                }
            } else {
                if (indicateur1 == indicateur2) {
                    rs = stmt.executeQuery("SELECT " + indicateur2 + " FROM Step natural join replication where ID_experimentation= " + experimentation + " and  id_Replication =" + replication + " " + requete + " ;");
                } else {
                    rs = stmt.executeQuery("SELECT " + indicateur2 + "," + indicateur1 + " FROM Step natural join replication where ID_experimentation= " + experimentation + " and  id_Replication =" + replication + "  " + requete + " ;");
                }
            }

            double step = 0;
            double facteur = 0;
            
            /*
            FIXME : can't go backward in this resultset : 
            java.sql.SQLException: Result set type is TYPE_FORWARD_ONLY
            
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }
            */


           
            while (rs.next()) {

                XYChart.Data dt = new XYChart.Data(rs.getDouble(indicateur2), rs.getDouble(indicateur1));
                Rectangle rect1 = new Rectangle(5, 5);
                rect1.setArcHeight(5);
                rect1.setArcWidth(5);

                rect1.setFill(colors[0]);

                dt.setNode(rect1);
                if (maxtemps != 0) {
                    facteur = step / maxtemps;    
                } else {
                    facteur = 1;
                }
                
                dt.getNode().setOpacity(0.2 + (0.8 * facteur));
                series.getData().add(dt);

                step++;

            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        ac.getData().addAll(series);
        ac.setTitle(indicateur1 + "/" + indicateur2);

        return ac;

    }

    public void modificationchart(Statement stmt, ScatterChart chart, int replication, int experimentation, boolean iteratif, double maxtemps, String requete) {

        String[] valeurs = chart.getTitle().split("/");
        String indicateur1 = valeurs[0];
        String indicateur2 = valeurs[1];
        XYChart.Series series1 = new XYChart.Series();

        //  series1.getNode().setStyle("chart-series-area-fill { -fx-fill: transparent; }");
        Paint cc = ((Rectangle) ((XYChart.Data) ((XYChart.Series) chart.getData().get(chart.getData().size() - 1)).getData().get((int) maxtemps - 1)).getNode()).getFill();
        Color newcolor = (Color) cc;
       

        try {

            if (iteratif) {
                chart.getData().clear();

            } else {
            }

            ResultSet rs = null;
            if (indicateur1.equals(indicateur2)) {
                rs = stmt.executeQuery("SELECT " + indicateur2 + " FROM Step natural join replication where ID_experimentation= " + experimentation + " and  id_Replication =" + replication + "  " + requete + "  ;");
            } else {
                rs = stmt.executeQuery("SELECT " + indicateur2 + "," + indicateur1 + " FROM Step natural join replication where ID_experimentation= " + experimentation + " and  id_Replication =" + replication + " " + requete + " ;");
            }
            double step = 0;
            double facteur = 0;
            Color my_color = nextcolor(new Color(newcolor.getRed(), newcolor.getGreen(), newcolor.getBlue(), 1));

            while (rs.next()) {

                XYChart.Data dt = new XYChart.Data(rs.getDouble(indicateur2), rs.getDouble(indicateur1));
                Rectangle rect1 = new Rectangle(5, 5);
                rect1.setArcHeight(5);
                rect1.setArcWidth(5);
                facteur = step / maxtemps;

                rect1.setFill(new Color(my_color.getRed(), my_color.getGreen(), my_color.getBlue(), facteur));

                dt.setNode(rect1);

                series1.getData().add(dt);

                step++;

            }
            rs.close();

            rang++;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        chart.getData().add(series1);

    }

    public Color nextcolor(Color cc) {
        Color my_color = null;
        for (int i = 0; i < colors.length; i++) {
            Color color = colors[i];
            if (color.equals(cc)) {
                if (i == colors.length - 1) {
                    my_color = colors[0];
                } else {
                    my_color = colors[i + 1];
                }
            }

        }
    
        return my_color;
    }
}
