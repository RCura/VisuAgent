/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projettransmondyn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mehdi
 */
public class alimentationdb {

    public alimentationdb() {
        String chaine = "";
        String fichier = "C:\\Users\\mehdi\\Documents\\NetBeansProjects\\Transmondyn\\src\\transmondyn\\resultat.csv";

        Connection c = null;
        Statement stmt = null;

        //lecture du fichier texte	
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:visuAgent3.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            for (int i = 0; i < 12; i++) {
                stmt.executeUpdate("INSERT OR IGNORE  INTO Continent  VALUES('" + i + "')");
        //(ID,proba_innovation_sensitivity,proba_birth_sensitivity,Energy_accumulation_per_timestep_sensitivity)
                // stmt.close();
            }
            int L = 1;
            BufferedReader br = new BufferedReader(new FileReader(fichier));
            String line;
            while ((line = br.readLine()) != null) {
                if (L > 1) {
                    String[] valeurs = line.split(",");

                    stmt.executeUpdate("INSERT OR IGNORE  INTO Experimentation  VALUES('" + Double.parseDouble(valeurs[59].replace("\"", "")) + "','" + Double.parseDouble(valeurs[1].replace("\"", "")) + "','" + Double.parseDouble(valeurs[2].replace("\"", "")) + "','" + Double.parseDouble(valeurs[3].replace("\"", "")) + "');");
                    stmt.executeUpdate("INSERT OR IGNORE  INTO Replication  VALUES('" + Integer.parseInt(valeurs[0].replace("\"", "")) + "','" + Double.parseDouble(valeurs[59].replace("\"", "")) + "',0)");
        //(ID,proba_innovation_sensitivity,proba_birth_sensitivity,Energy_accumulation_per_timestep_sensitivity)
                    // stmt.close();

                    String sql = "INSERT OR IGNORE INTO Step "
                            + "VALUES (" + Integer.parseInt(valeurs[4].replace("\"", ""))
                            + "                    ," + Double.parseDouble(valeurs[5].replace("\"", ""))
                            + "                    ," + Double.parseDouble(valeurs[6].replace("\"", ""))
                            + "                    ," + Double.parseDouble(valeurs[7].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[8].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[9].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[10].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[11].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[12].replace("\"", ""))
                            + "                ," + Double.parseDouble(valeurs[13].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[14].replace("\"", ""))
                            + "                ," + Double.parseDouble(valeurs[15].replace("\"", ""))
                            + "                ," + Double.parseDouble(valeurs[16].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[17].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[18].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[19].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[20].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[21].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[22].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[47].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[48].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[49].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[50].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[51].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[52].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[53].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[54].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[55].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[56].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[57].replace("\"", ""))
                            + "               ," + Double.parseDouble(valeurs[58].replace("\"", ""))
                            + "              ," + Integer.parseInt(valeurs[0].replace("\"", ""))
                            + ");";
                    stmt.executeUpdate(sql);
                    //   stmt.close();
                    for (int i = 0; i < 12; i++) {
                        stmt.executeUpdate("INSERT OR IGNORE  INTO Avoir_continent  VALUES('" + i + "','" + Integer.parseInt(valeurs[4].replace("\"", "")) + "','" + Double.parseDouble(valeurs[23 + i].replace("\"", "")) + "','" + Double.parseDouble(valeurs[34 + i].replace("\"", "")) + "','" + Integer.parseInt(valeurs[0].replace("\"", "")) + "')");
        //(ID,proba_innovation_sensitivity,proba_birth_sensitivity,Energy_accumulation_per_timestep_sensitivity)

                    }
                    stmt.close();

                }

                L++;
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e.toString());

        }
        try {
            stmt.close();
            c.commit();
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("fin");
    }

}
