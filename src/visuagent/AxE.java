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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author mehdi
 */
public class AxE {

    double dragBaseX = 0, dragBaseY = 0;
    double dragBase2X = 0, dragBase2Y = 0;

    public AxE() {
    }

    private void zoom(double d, Group map) {
        map.scaleXProperty().set(map.scaleXProperty().get() * d);
        map.scaleYProperty().set(map.scaleYProperty().get() * d);
    }

    public Group creationMap(Statement stmt, String indicateur, int replication, String experimentation, int step, Color Colorpicker, String agregation_exp) throws IOException {

        ArrayList<Double> continent = new ArrayList<Double>();
        Double maxindicateur = 0.0;
        Color currentColor = Color.WHITE;

        try {

            Iterator it = continent.iterator();
            ResultSet Max = null;
            if (experimentation.equals("Toute")) {
                Max = stmt.executeQuery("SELECT Max(" + indicateur + ") FROM avoir_continent where  id_Replication =" + replication + "  and num_step =" + step);
            } else {
                Max = stmt.executeQuery("SELECT Max(" + indicateur + ") FROM avoir_continent where  id_Replication =" + replication + " AND id_experimentation =" + experimentation + " and num_step =" + step);
            }

            while (Max.next()) {
                maxindicateur = Max.getDouble(1);
            }
            ResultSet rs = null;
            if (experimentation.equals("Toute")) {
                rs = stmt.executeQuery("SELECT id_continent," + agregation_exp + "(" + indicateur + ") as " + indicateur + "  FROM avoir_continent where  id_Replication =" + replication + " and num_step =" + step + " group by id_continent");
            } else {
                rs = stmt.executeQuery("SELECT id_continent," + indicateur + " FROM avoir_continent where  id_Replication =" + replication + " AND id_experimentation =" + experimentation + " and num_step =" + step);
            }

            while (rs.next()) {
                continent.add(rs.getDouble(indicateur));

            }
            rs.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        Group parent = new Group();
        Group root = new Group();
        Group map1 = new Group();
        Group map = new Group();
        Group texts = new Group();
        Scene scene;

        scene = new Scene(root, 300, 250, Color.LIGHTBLUE);
        Color[] colors = new Color[]{Color.YELLOW, Color.RED, Color.ORANGE, Color.VIOLET, Color.CHOCOLATE, Color.YELLOW, Color.AZURE};

        File file = new File("shape/pegem2_region.shp");
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();
        SimpleFeatureCollection cf = featureSource.getFeatures();
        SimpleFeatureIterator featuresIterator = cf.features();
        Coordinate[] coords;
        Geometry polygon;
        Point centroid;
        Bounds bounds;
        while (featuresIterator.hasNext()) {
            SimpleFeature o = featuresIterator.next();;
            String name = (String) o.getAttribute("descriptio");
            int id = (int) o.getAttribute("ID");
            Object geometry = o.getDefaultGeometry();

            if (geometry instanceof MultiPolygon) {
                MultiPolygon multiPolygon = (MultiPolygon) geometry;
                if (id + 1 >= continent.toArray().length || continent.get(id) == 0) {
                    currentColor = Color.WHITE;
                } else {

                    double nbclasse = 6;
                    for (double i = 1; i < nbclasse + 1; i++) {
                        double facteurmax = i / nbclasse;
                        double facteurmin = (i - 1) / nbclasse;

                        if (continent.get(id) <= maxindicateur * facteurmax && continent.get(id) > maxindicateur * facteurmin) {
                            currentColor = new Color(Colorpicker.getRed(), Colorpicker.getGreen(), Colorpicker.getBlue(), (i / nbclasse));
                        }

                    }

                }

                centroid = multiPolygon.getCentroid();
                final Text text = new Text(name);
                bounds = text.getBoundsInLocal();
                text.getTransforms().add(new Translate(centroid.getX(), centroid.getY()));
                text.getTransforms().add(new Scale(0.1, -0.1));
                text.getTransforms().add(new Translate(-bounds.getWidth() / 2., bounds.getHeight() / 2.));
                texts.getChildren().add(text);

                for (int geometryI = 0; geometryI < multiPolygon.getNumGeometries(); geometryI++) {

                    polygon = multiPolygon.getGeometryN(geometryI);
                    coords = polygon.getCoordinates();
                    Path path = new Path();
                    path.setStrokeWidth(0.05);
                   //currentColor = (currentColor+1)%colors.length;
                    // path.setFill(colors[currentColor]);
                    path.setFill(currentColor);
                    path.getElements().add(new MoveTo(coords[0].x, coords[0].y));
                    for (int i = 1; i < coords.length; i++) {
                        path.getElements().add(new LineTo(coords[i].x, coords[i].y));
                    }
                    path.getElements().add(new LineTo(coords[0].x, coords[0].y));

                    path.setId(String.valueOf(id));
                    map1.getChildren().add(path);

                }
            }
        }

        map.translateXProperty().set(300);
        map.translateYProperty().set(150);
        map.scaleXProperty().set(30);
        map.scaleYProperty().set(-30);
      //  map.setTranslateX(10);
        //          map.setTranslateY(10);

        map.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                map.setCursor(Cursor.MOVE);
                dragBaseX = map.translateXProperty().get();
                dragBaseY = map.translateYProperty().get();
                dragBase2X = event.getSceneX();
                dragBase2Y = event.getSceneY();
            }
        });
        map.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                map.setTranslateX(dragBaseX + (event.getSceneX() - dragBase2X));
                map.setTranslateY(dragBaseY + (event.getSceneY() - dragBase2Y));
            }
        });
        map.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                map.setCursor(Cursor.DEFAULT);
            }
        });
        map.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double translateX = event.getDeltaX();
                double translateY = event.getDeltaY();

                if (translateY > 0) {
                    zoom(1.4, map);
                }
                if (translateY < 0) {
                    zoom(1. / 1.4, map);
                }

            }
        });

        map.getChildren().add(map1);
        map.getChildren().add(texts);

        root.getChildren().add(map);
//        root.add(map, 0, 0);

        VBox vbox = new VBox();
        final Button plus = new Button("+");
        plus.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                zoom(1.4, map);
            }
        });
        vbox.getChildren().add(plus);
        final Button minus = new Button("-");
        minus.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                zoom(1. / 1.4, map);
            }
        });
        vbox.getChildren().add(minus);
       // root.getChildren().add(vbox);

        parent.getChildren().add(root);
        //primaryStage.setScene(scene);
        //  primaryStage.setVisible(true);

     // map.setAutoSizeChildren(true);
        // root.setAutoSizeChildren(true);
        return map;
    }

    public Group modificationmap(Statement stmt, Group map, String indicateur, int replication, String experimentation, int step, Color Colorpicker, String agregation_exp) {

        Group newgroup = map;
        Group pp = (Group) newgroup.getChildren().get(0);

        Double maxindicateur = 0.0;
        Color currentColor = Color.WHITE;

        try {

            ResultSet Max = null;
            if (experimentation.equals("Toute")) {
                Max = stmt.executeQuery("SELECT Max(" + indicateur + ") FROM avoir_continent where  id_Replication =" + replication + "  and num_step =" + step);
            } else {
                Max = stmt.executeQuery("SELECT Max(" + indicateur + ") FROM avoir_continent where  id_Replication =" + replication + " AND id_experimentation =" + experimentation + " and num_step =" + step);
            }

            while (Max.next()) {
                maxindicateur = Max.getDouble(1);
            }
            ResultSet rs = null;
            if (experimentation.equals("Toute")) {
                rs = stmt.executeQuery("SELECT id_continent," + agregation_exp + "(" + indicateur + ") as " + indicateur + "  FROM avoir_continent where  id_Replication =" + replication + " and num_step =" + step + " group by id_continent");
            } else {
                rs = stmt.executeQuery("SELECT id_continent," + indicateur + " FROM avoir_continent where  id_Replication =" + replication + " AND id_experimentation =" + experimentation + " and num_step =" + step);
            }

            while (rs.next()) {
                double nbclasse = 6;

                for (double i = 1; i < nbclasse + 1; i++) {

                    double facteurmax = i / nbclasse;
                    double facteurmin = (i - 1) / nbclasse;

                    if (rs.getInt("id_continent") < 11) {

                        if (rs.getDouble(indicateur) == 0.0) {
                            ((Path) pp.lookup("#" + String.valueOf(rs.getInt("id_continent")))).setFill(Color.WHITE);
                        } else {
                            if (rs.getDouble(indicateur) <= maxindicateur * facteurmax && rs.getDouble(indicateur) > maxindicateur * facteurmin) {
                                currentColor = new Color(Colorpicker.getRed(), Colorpicker.getGreen(), Colorpicker.getBlue(), (i / nbclasse));

                                ((Path) pp.lookup("#" + String.valueOf(rs.getInt("id_continent")))).setFill(currentColor);
                            }
                        }
                    }

                }

            }

            rs.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return newgroup;

    }
}
