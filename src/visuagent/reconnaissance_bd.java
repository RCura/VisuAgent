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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
