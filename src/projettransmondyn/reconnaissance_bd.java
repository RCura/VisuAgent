/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projettransmondyn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

/**
 *
 * @author mehdi
 */
public class reconnaissance_bd {

    public reconnaissance_bd() {
    }

    public ObservableList<String> construction(Statement stmt, String table) {
        ObservableList<String> data = FXCollections.observableArrayList();
        String[] chaine = null;

        try {

            ResultSet rs = stmt.executeQuery("pragma table_info(" + table + ");");
            while (rs.next()) {
                Double d = rs.getDouble(1);
                data.add(rs.getString(2));

            }
            rs.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return data;

    }

}
