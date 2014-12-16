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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.controlsfx.dialog.Dialogs;

/**
 * FXML Controller class
 *
 * @author mehdi
 */
public class AlimenterController implements Initializable {

    @FXML
    private GridPane principal_grid;
    @FXML
    private ListView<String> experimentation_list;
    @FXML
    private ListView<String> replication_list;
    @FXML
    private ListView<String> pasdetemps_list;

    @FXML
    private ListView<String> checkListView;
    private ListView<String> espace_listview;
    @FXML
    private Button ok;
    @FXML
    private TextField delimiteur;
    private Map<Integer, String> attributs = new HashMap<Integer, String>();
    private Map<Integer, String> spatials = new HashMap<Integer, String>();
    private BufferedReader br;
    private BufferedReader br2;
    private String[] valeurs;
    private String[] valeurs2;
    @FXML
    private ProgressBar alimentation_progressbar;
    public Connection c;
    public Statement stmt = null;

    @FXML
    private Button source_attributaire;
    @FXML
    private Button source_spatial;
    @FXML
    private Button source_shape;
    private int max_attribut, max_spatial;
    private File fileshape = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        final ObservableList<String> strings = FXCollections.observableArrayList();
        final ObservableList<String> strings2 = FXCollections.observableArrayList();
        espace_listview = new ListView<>(strings2);

        checkListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        checkListView.getItems().clear();
        checkListView.getItems().addAll(strings);
        //checkListView.getItems().addAll(new reconnaissance_nlogo().construction());
    }

    private void sourceonaction(ActionEvent event) {
        /*
         FileChooser fileChooser = new FileChooser();
         fileChooser.setTitle("Fichier de simulation");
          
         fileChooser.getExtensionFilters().addAll(
         new FileChooser.ExtensionFilter("NetLogo", "*.nlogo")
                   
         );
            
            
         File file = fileChooser.showOpenDialog(checkListView.getScene().getWindow());
            
         if (file != null) {
               
         try {
         File newfile = new File("synchrone.nlogo");
         new copierfichier().copyFile(file, newfile);
         } catch (IOException ex) {
         Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
         }
         }  
         */

    }

    @FXML
    private void add_experimentation_onaction(ActionEvent event) {

        ObservableList<String> potential = FXCollections.observableArrayList(checkListView.getSelectionModel().getSelectedItems());
        if (potential != null) {
            experimentation_list.getItems().addAll(potential);
            checkListView.getItems().removeAll(potential);
            checkListView.getSelectionModel().clearSelection();
        }

    }

    @FXML
    private void add_replication_onaction(ActionEvent event) {
        ObservableList<String> potential = FXCollections.observableArrayList(checkListView.getSelectionModel().getSelectedItems());
        if (potential != null) {
            replication_list.getItems().addAll(potential);
            checkListView.getItems().removeAll(potential);
            checkListView.getSelectionModel().clearSelection();
        }

    }

    @FXML
    private void add_pasdetemps_onaction(ActionEvent event) {
        ObservableList<String> potential = FXCollections.observableArrayList(checkListView.getSelectionModel().getSelectedItems());
        if (potential != null) {
            pasdetemps_list.getItems().addAll(potential);
            checkListView.getItems().removeAll(potential);
            checkListView.getSelectionModel().clearSelection();
        }
    }

    public void creationdb() {

        try {

            try {
                stmt.executeUpdate("drop table  if exists Experimentation");
                stmt.executeUpdate("drop table  if exists Replication");
                stmt.executeUpdate("drop table  if exists Step");
                stmt.executeUpdate("drop table  if exists avoir_continent");
                stmt.executeUpdate("drop table  if exists continent");

                String S = creationchaineSQL(experimentation_list.getItems());
                stmt.executeUpdate("CREATE  TABLE  Experimentation "
                        + "(ID_Experimentation  INT PRIMARY KEY     NOT NULL,"
                        + S.substring(0, S.length() - 1)
                        + ")");

                String sql = "CREATE  TABLE  Replication "
                        + "(ID_replication INT      NOT NULL,"
                        + " ID_experimentation           double NOT NULL, "
                        + creationchaineSQL(replication_list.getItems())
                        + "PRIMARY KEY(ID_replication,ID_experimentation),"
                        + "FOREIGN KEY(ID_experimentation) REFERENCES Experimentation(ID_Experimentation)) ";
                stmt.executeUpdate(sql);

                String sql2 = "CREATE  TABLE Step "
                        + "(num_step                  INT     NOT NULL,"
                        + " id_Replication  INT            not null, "
                        + " ID_experimentation           double NOT NULL, "
                        + creationchaineSQL(pasdetemps_list.getItems())
                        + "PRIMARY KEY(num_step,ID_Replication,ID_experimentation),"
                        + "FOREIGN KEY(id_Replication) REFERENCES Replication(ID_replication)) ";

                stmt.executeUpdate(sql2);

                stmt.executeUpdate("CREATE  TABLE  Continent "
                        + "(ID_continent INT PRIMARY KEY     NOT NULL )");
                stmt = c.createStatement();
                stmt.executeUpdate("CREATE  TABLE  Avoir_continent "
                        + "(ID_continent INT   NOT NULL,"
                        + "num_step INT      NOT NULL,"
                        + "ID_replication INT      NOT NULL,"
                        + "ID_Experimentation INT      NOT NULL,"
                        + creationchaineSQL(espace_listview.getItems())
                        + "PRIMARY KEY(ID_continent,num_step,ID_replication,ID_Experimentation))"
                );
                System.out.println(espace_listview.getItems().isEmpty());
            } catch (SQLException e) {
                System.err.println(e.toString());
            }

            System.err.println("arrivé1");
            /*
             /////////////////////////////////////////
             //creation à partir d'un fichier .nlogo//
             /////////////////////////////////////////
             HeadlessWorkspace ws = HeadlessWorkspace.newInstance();
             ws.open("synchrone.nlogo");
             //ws.openString(org.nlogo.util.Utils.url2String("/turtles.nlogo"));
             // ws.command("cro 8 [ fd 5 ]");
             ws.command("RESET-TICKS");
             ws.command("setup"); 
             System.err.println("arrivé2");
             Task task = new Task<Void>() {
             @Override protected Void call() throws Exception {
             int iterations;
             for (iterations = 1; iterations < Integer.valueOf(100); iterations++) {
             if (isCancelled()) {
             break;
             }
            
             ws.command("go");
             System.err.println("arrivé3");
             // org.nlogo.api.Turtle turtle =(org.nlogo.api.Turtle) ws.world().turtles().agent(3);
             //System.out.println("[xcor] of turtle 3 = " + turtle.xcor());
         
             final int numstep = iterations;
             //  final double valeur = (double )ws.report(indicateursynchrone.getValue());
             // new AxT().updatechartSynchrone(stmt, iterations, (double) ws.report(indicateursynchrone.getValue()), ac);
             //Thread.sleep((long) vitesse_silder.getValue());
             try {
             String experimentation_sql = "'1'," +creationrequetee(experimentation_list);
             String replication_sql = "'1',"+"'1'," +creationrequetee(replication_list) ; 
             String pas_temps_sql =  numstep+",'1'," +creationrequetee(pasdetemps_list);       
             System.err.println("experimentation_sql :"+experimentation_sql); 
             System.err.println("replication_sql :"+replication_sql); 
             System.err.println("pas_temps_sql :"+pas_temps_sql); 
             stmt.executeUpdate("INSERT OR IGNORE  INTO Experimentation  VALUES("+experimentation_sql.substring(0, experimentation_sql.length()-1)+");");
             stmt.executeUpdate("INSERT OR IGNORE  INTO replication  VALUES("+replication_sql.substring(0, replication_sql.length()-1)+");");
             stmt.executeUpdate("INSERT OR IGNORE  INTO Step  VALUES("+pas_temps_sql.substring(0,pas_temps_sql.length()-1)+");");
             } catch (Exception e) {
             System.err.println(e.toString());
             }
          
            
             System.err.println("arrivé4");
           
             }
       
             return null;
             }
             protected void done(){  
             System.err.println("fin");
             try {
             c.commit();
             c.close();
           
             } catch (SQLException ex) {
             Logger.getLogger(AlimenterController.class.getName()).log(Level.SEVERE, null, ex);
             }
             }  
             private String creationrequetee(ListView<String>  list) throws CompilerException, LogoException{
      
             String requete ="";
             for (int i = 0; i < list.getItems().size(); i++) {
             String element = list.getItems().get(i);
          
           
             requete = requete +"'"+(double)ws.report(element)+"',";
            
            
             }
             return requete;
             }
             };  
                
             Thread th = new Thread(task);
             th.start();  

             */
            ////////////////////////////////////////       
            // creation à partir d'un fichier txt //
            ////////////////////////////////////////

            Thread th = new Thread(task);
            th.start();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    private String creationchaineSQL(ObservableList<String> chaine) {
        String chaineSQL = "";
        for (Iterator<String> it = chaine.iterator(); it.hasNext();) {
            String string = it.next().replace(" ", "_").replace("-", "_");
            chaineSQL = chaineSQL + string + " double,";

        }

        return chaineSQL;
    }

    @FXML
    private void okOnaction(ActionEvent event) {
        if (fileshape != null) {
            Path source = Paths.get(fileshape.getPath());
            File mynew = new File("shape/shape.shp");
            if (mynew.exists()) {
                mynew.delete();
            }

            Path destination = Paths.get(fileshape.getPath());

            try {
                if (source.equals(destination)) {
                    //showerrorwindow("Ce Fichier est le même que celui utilisé par VisuAgent");
                    Files.copy(source, destination);
                } else {
                    Files.copy(source, destination);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        File olddb = new File("visuAgent.db");
        olddb.delete();
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:visuAgent.db");
            stmt = c.createStatement();

            c.setAutoCommit(false);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AlimenterController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AlimenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

            creationdb();
        } else {
            showerrorwindow("Veuillez introduire un fichier shape");
        }

    }

    private String creationrequete(ListView<String> list, Map<Integer, String> map) {

        String requete = "";
        for (int i = 0; i < list.getItems().size(); i++) {
            String element = list.getItems().get(i);

            requete = requete + find_element(element, map);

        }
        return requete;
    }

    private String find_element(String element, Map<Integer, String> map) {
        String s = "";
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            Integer integer = entry.getKey();
            String string = entry.getValue();
            //System.out.println(string);
            if (string.equals(element)) {

                s = "'" + valeurs[integer].replace("\"", "") + "',";
            }

        }
        return s;
    }

    @FXML
    private void sourc_attributaireeonaction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sorties de Simulation (attributaires)");

        //fileChooser.getExtensionFilters().addAll(
        //        new FileChooser.ExtensionFilter("Text", "*.txt"),
        //        new FileChooser.ExtensionFilter("Csv", "*.csv")
        //);

        File file = fileChooser.showOpenDialog(source_attributaire.getScene().getWindow());
        try {
            max_attribut = calculat_nb_ligne(file);
            System.out.println(max_attribut);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AlimenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (file != null) {
            try {
                br = new BufferedReader(new FileReader(file));
                String line;
                line = br.readLine();
                String[] valeurs = line.split(delimiteur.getText());
                for (int i = 0; i < valeurs.length; i++) {
                    String string = valeurs[i].replace("\"", "");
                    if (!(string.equals("ID_Experience") || string.equals("ID_Simulation") || string.equals("ID_Temps") || string.equals("ID_Espace"))) {
                        checkListView.getItems().add(string);
                    }

                    attributs.put(i, string);
                }
                //br.close(); 
            } catch (Exception e) {
                System.out.println(e.toString());

            }
        }

    }

    @FXML
    private void source_spatialonaction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sorties de Simulation (spatiales)");

        //fileChooser.getExtensionFilters().addAll(
        //        new FileChooser.ExtensionFilter("Text", "*.txt"),
        //        new FileChooser.ExtensionFilter("Csv", "*.csv")
        //);

        File file = fileChooser.showOpenDialog(source_attributaire.getScene().getWindow());

        try {
            max_spatial = calculat_nb_ligne(file);
            System.out.println(max_spatial);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AlimenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (file != null) {

            try {

                br2 = new BufferedReader(new FileReader(file));
                System.err.println(file.length());
                String line;
                line = br2.readLine();

                String[] valeurs2 = line.split(delimiteur.getText());

                for (int i = 0; i < valeurs2.length; i++) {

                    String string = valeurs2[i].replace("\"", "");

                    spatials.put(i, string);
                    if (string.equals("ID_Experience") || string.equals("ID_Simulation") || string.equals("ID_Temps") || string.equals("ID_Espace")) {

                    } else {
                        System.err.println(string);
                        espace_listview.getItems().add(string);
                    }
                }

            } catch (Exception e) {

                System.err.println(e.toString());

            }
        }
    }

    @FXML
    private void source_shapeonaction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Shape file");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Shape", "*.shp")
        );

        fileshape = fileChooser.showOpenDialog(source_attributaire.getScene().getWindow());

    }

    class update extends Thread {

        public void run(int L) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    alimentation_progressbar.setProgress(L / 160000);
                    System.out.println(L);
                }
            });

        }
    }

    public boolean globalstracking(String line) {
        boolean trouve = false;
        for (int i = 0; i < line.length(); i++) {

            String string = valeurs[i];

        }

        return false;
    }
    Task task_spatial = new Task<Void>() {

        @Override
        protected Void call() throws Exception {

            try {
                int L = 2;
                String line;
                while ((line = br2.readLine()) != null) {
                    if (L > 1) {
                        valeurs = line.split(delimiteur.getText());
                    }

                    String num_continent = find_element("ID_Espace", spatials);
                    String num_replication = find_element("ID_Simulation", spatials);
                    String num_pas_temps = find_element("ID_Temps", spatials);
                    String num_experimentation = find_element("ID_Experience", spatials);

                    String Continent_sql = num_continent;
                    String Avoir_continent_sql = num_continent + num_pas_temps + num_replication + num_experimentation + creationrequete(espace_listview, spatials);

        //   stmt.executeUpdate("INSERT OR IGNORE  INTO Experimentation  VALUES("+experimentation_sql.substring(0, experimentation_sql.length()-1)+");");
                    //   stmt.executeUpdate("INSERT OR IGNORE  INTO replication  VALUES("+replication_sql.substring(0, replication_sql.length()-1)+");");
                    //   stmt.executeUpdate("INSERT OR IGNORE  INTO Step  VALUES("+pas_temps_sql.substring(0,pas_temps_sql.length()-1)+");");
                    stmt.executeUpdate("INSERT OR IGNORE  INTO Continent  VALUES(" + Continent_sql.substring(0, Continent_sql.length() - 1) + ");");
                    stmt.executeUpdate("INSERT OR IGNORE  INTO Avoir_continent  VALUES(" + Avoir_continent_sql.substring(0, Avoir_continent_sql.length() - 1) + ");");
                    stmt.close();

                    L++;
                    final double progress = L;
                    Platform.runLater(new Runnable() {

                        public void run() {

                            alimentation_progressbar.setProgress(progress / max_spatial);

                        }
                    });

                }
                br.close();
                c.commit();
                c.close();
                alimentation_progressbar.setVisible(false);

            } catch (Exception e) {
                System.out.println(e.toString());

            }

            return null;
        }

        protected void done() {
            this.cancel();
            Platform.runLater(new Runnable() {

                public void run() {

                    try {
                        Stage stage = (Stage) source_attributaire.getScene().getWindow();
                        stage.close();

                        new VisuAgent().start(new Stage());
                    } catch (Exception ex) {
                        Logger.getLogger(AlimenterController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });

        }
    };
    Task task = new Task<Void>() {

        @Override
        protected Void call() throws Exception {

            try {

                int L = 2;
                String line;
                while ((line = br.readLine()) != null) {
                    if (L > 1) {
                        valeurs = line.split(delimiteur.getText());
                    }

                    String num_experimentation = find_element("ID_Experience", attributs);
                    String num_replication = find_element("ID_Simulation", attributs);
                    String num_pas_temps = find_element("ID_Temps", attributs);

                    String experimentation_sql = num_experimentation + creationrequete(experimentation_list, attributs);
                    String replication_sql = num_replication + num_experimentation + creationrequete(replication_list, attributs);
                    String pas_temps_sql = num_pas_temps + num_replication + num_experimentation + creationrequete(pasdetemps_list, attributs);
                    stmt.executeUpdate("INSERT OR IGNORE  INTO Experimentation  VALUES(" + experimentation_sql.substring(0, experimentation_sql.length() - 1) + ");");
                    stmt.executeUpdate("INSERT OR IGNORE  INTO replication  VALUES(" + replication_sql.substring(0, replication_sql.length() - 1) + ");");
                    stmt.executeUpdate("INSERT OR IGNORE  INTO Step  VALUES(" + pas_temps_sql.substring(0, pas_temps_sql.length() - 1) + ");");

                    stmt.close();
                    L++;
                    final double progress = L;

                    Platform.runLater(new Runnable() {

                        public void run() {

                            alimentation_progressbar.setProgress(progress / max_attribut);
                                //System.out.println(task.getProgress() );

                        }
                    });

                }
                br.close();

            } catch (Exception e) {
                System.out.println(e.toString());

            }

            return null;
        }

        protected void done() {
            Thread th = new Thread(task_spatial);
            th.start();
        }
    };

    public int calculat_nb_ligne(File file) throws FileNotFoundException {
        BufferedReader max = new BufferedReader(new FileReader(file));
        int L = 0;
        String line;
        try {
            while ((line = max.readLine()) != null) {
                L++;
            }
            max.close();
        } catch (IOException ex) {
            Logger.getLogger(AlimenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return L;
    }

    public void showerrorwindow(String msg) {
        org.controlsfx.control.action.Action response = Dialogs.create()
                .owner(delimiteur.getScene().getWindow())
                .title("Erreur")
                .masthead("Erreur de saisie")
                .message(msg)
                .showError();
    }
}
