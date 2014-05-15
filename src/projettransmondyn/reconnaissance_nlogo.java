/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projettransmondyn;

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
