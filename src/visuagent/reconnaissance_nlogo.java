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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author mehdi
 */
public class reconnaissance_nlogo {

    public reconnaissance_nlogo() {

    }

    public ObservableList<String> construction() {
        ObservableList<String> data = FXCollections.observableArrayList();

        boolean recherche = false;
        String fichier = "NetLogo/synchrone.nlogo";
        File file = new File(fichier);

        if (file != null) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                boolean trouve = true;
                //line=br.readLine();                
                while ((line = br.readLine()) != null && trouve) {

                    if (line.contains("globals")) {
                        recherche = true;
                    }

                    if (line.contains("]")) {
                        if (recherche) {
                            trouve = false;
                        }
                        recherche = false;
                    }

                    if (recherche && !line.contains(";")) {

                        String[] valeurs = line.replace("[", " ").split(" ");
                        for (int i = 0; i < valeurs.length; i++) {
                            String string = valeurs[i];
                            if (!string.isEmpty() && !string.toLowerCase().equals("globals")) {

                                data.add(string);
                            }

                        }

                    }

                }
                br.close();
            } catch (Exception e) {
                System.out.println(e.toString());

            }
        }

        return data;

    }

}
