/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projettransmondyn;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javafx.concurrent.Task;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.dialog.Dialogs;
import org.nlogo.api.CompilerException;
import org.nlogo.api.*;

import org.nlogo.headless.HeadlessWorkspace;

/**
 *
 * @author mehdi
 */
public class HomeController implements Initializable {

    private Label label;
    @FXML
    private Font x3;
    @FXML
    private Color x4;
    @FXML
    private ComboBox<String> experimentation;
    @FXML
    private Button alimenter;
    private ComboBox<String> replication;
    @FXML
    private TextField num_replication;
    @FXML
    public ComboBox<String> indicateur;
    @FXML
    private Button go;
    @FXML
    private Font x5;
    @FXML
    private SegmentedButton segbutton;
    private ToggleButton unereplication;
    private ToggleButton toutereplication;
    @FXML
    private ComboBox<String> indicateur1_AxA;
    @FXML
    private ComboBox<String> indicateur2_AxA;
    @FXML
    private CheckBox AxT_checkbox;
    @FXML
    private CheckBox AxA_checkbox;
    @FXML
    private CheckBox AxE_checkbox;
    @FXML
    private Button print;
    @FXML
    private SegmentedButton réplications_segbutton;
    @FXML
    private ToggleButton iteratif_togglebutton;
    @FXML
    private ToggleGroup toogleGroupe1;
    @FXML
    private ToggleButton noniteratif_togglebutton;
    @FXML
    private TabPane tabmenu;
    @FXML
    private TabPane TabAxt;
    @FXML
    private TabPane TabAxE;
    @FXML
    private ComboBox<String> indicateur_AxE;
    @FXML
    private TextField temps_AxE;
    @FXML
    private Slider slider_Axt_replication;
    @FXML
    private TabPane TabAxA;
    @FXML
    private Slider slider_AxA_replication;
    @FXML
    private TextField slider_replication_AxA_label;
    @FXML
    private ColorPicker AxE_colorpicker;
    @FXML
    private Slider slider_AxE_temps;
    @FXML
    private Label slider_temps_AxE_label;
    @FXML
    private Button printAxE;
    Timer timer;
    Toolkit toolkit;
    @FXML
    private ToggleButton PLAY_aXe;
    @FXML
    private ToggleButton iteratif_togglebutton_AxT;
    @FXML
    private ToggleButton noniteratif_togglebutton_AxT;
    @FXML
    private ToggleButton iteratif_togglebutton_AxE;
    @FXML
    private ToggleButton noniteratif_togglebutton_AxE;
    @FXML
    private SplitPane split_Body;
    @FXML
    private SplitPane split_child;
    @FXML
    private VBox Axt;
    @FXML
    private VBox AxE;
    @FXML
    private VBox AxA;
    @FXML
    private CheckMenuItem checkmenuitem_Axt;
    @FXML
    private CheckMenuItem checkmenuitem_AxA;
    @FXML
    private CheckMenuItem checkmenuitem_AxE;
    @FXML
    private ComboBox<String> agregation_AxT;
    @FXML
    private ToggleGroup toogleGroupeAxT;
    @FXML
    private ToggleGroup toogleGroupeAxE;
    @FXML
    private Button add_axt;
    @FXML
    private ComboBox<String> agregation_AxA_indicateur1;
    public Connection c;
    public Statement stmt = null;
    @FXML
    private Button sourcesynchrone;
    @FXML
    private ComboBox<String> indicateursynchrone;
    @FXML
    private ComboBox<String> indicateur1_AxAsynchrone;
    @FXML
    private ComboBox<String> indicateur2_AxAsynchrone;
    @FXML
    private ColorPicker AxE_colorpickersynchrone;
    @FXML
    private ComboBox<String> indicateur_AxEsynchrone;
    @FXML
    private Label slider_replication_Axt_label;
    @FXML
    private Button gosynchrone;
    @FXML
    private CheckBox AxT_checkboxsynchrone;
    @FXML
    private CheckBox AxA_checkboxsynchrone;
    @FXML
    private CheckBox AxE_checkboxsynchrone;
    private double temp;
    @FXML
    private Slider vitesse_silder;
    @FXML
    private TextArea temps_synchrone;
    @FXML
    private Label type_agregation_label;
    @FXML
    private VBox vbox_principal;
    @FXML
    private Button filtre_button;
    @FXML
    private GridPane filtre_grid;
    @FXML
    private ListView<String> liste_filtre;
    @FXML
    private TextField valeur_filtre;
    @FXML
    private Button ajouter_filtre;
    @FXML
    private ComboBox<String> indicateur_filtre;
    @FXML
    private ComboBox<String> operateur_filtre;
    private double max_replication;
    private double max_step;
    private double min_replication;
    private double min_step;
    @FXML
    private Button supprimer_filtre;
    @FXML
    private Button nouveau_filtre;
    @FXML
    private ComboBox<String> Agregation_Exp;
    @FXML
    private Text a_propos_droit;
    @FXML
    private Button add_axa;
    @FXML
    private Hyperlink mywebsite;
   
 

    ////////////////////
    // initialisation //
    ////////////////////
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.err.println(new String[] {"dsf","fds"});
        //Vérification de l’existence de la BD + chargement des composantes graphiques  
        load_controls();
        File db = new File("visuAgent.db");
       //Je lance une requête lourde pour rafraichir la base de données, puisque SQLite met dans certains cas 
        //un peu de temps pour répondre à la première requête

        if (db.exists()) {
            load_data_controls();
            try {
                new rafraichirbd().run();
            } catch (IOException | CompilerException | LogoException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    ///////////////
    // Événements //
    ///////////////
    private void alimenteronaction(ActionEvent event) throws IOException {

        //chargement du module Alimentation de Visu-Agent    
        Stage stage = (Stage) indicateur.getScene().getWindow();
        stage.close();
        final Stage myDialog = new Stage();
        myDialog.initModality(Modality.NONE);
        Parent page = FXMLLoader.load(getClass().getResource("alimenter.fxml"));
        Scene myDialogScene = new Scene(page);
        myDialog.setScene(myDialogScene);
        myDialog.getIcons().add(new Image("image/ViZuAgent-icon.png"));
        myDialog.show();

    }

    private void replicationonaction(ActionEvent event) {
        if (replication.getValue().contains("Une")) {
            num_replication.setDisable(false);

        } else {
            num_replication.setText(null);
            num_replication.setDisable(true);
        }

    }

    @FXML
    private void goonaction(ActionEvent event) throws IOException{
        if (unereplication.isSelected()) {

            if (AxE_checkbox.isSelected()) {

                if (experimentation.getValue().equals("Toute") && Agregation_Exp.getValue().equals("")) {
                    showerrorwindow("Merci de choisir un type d'agrégation des expérimentations");

                } else {
                    newAxE a = new newAxE();
                    a.start();
                }
            }
            if (AxT_checkbox.isSelected()) {
                if (experimentation.getValue().equals("Toute") && Agregation_Exp.getValue().equals("")) {

                    Tab t = new Tab("E : Toute/" + " R:" + indicateur.getValue() + "/" + indicateur.getValue());
                    AreaChart aXt = null;
                    if (agregation_AxT.getValue() == "") {
                        aXt = new AxT().creationchart(stmt, (String) indicateur.getValue(), Integer.parseInt(num_replication.getText()), "1", get_filtre_requete(), Agregation_Exp.getValue());
                    } else {
                        aXt = new agregationAxT().creationchart(stmt, (String) agregation_AxT.getValue(), (String) indicateur.getValue(), num_replication.getText(), "1", true, Agregation_Exp.getValue(), get_filtre_requete());
                    }
                    t.setContent(aXt);
                    TabAxt.getTabs().add(0, t);
                    SingleSelectionModel<Tab> selectionModel = TabAxt.getSelectionModel();
                    selectionModel.select(t);

                    for (int i = 2; i < experimentation.getItems().size() - 1; i++) {
                        if (agregation_AxT.getValue() == "") {
                            aXt = new AxT().creationchart(stmt, (String) indicateur.getValue(), Integer.parseInt(num_replication.getText()), String.valueOf(i), get_filtre_requete(), Agregation_Exp.getValue());
                        } else {
                            aXt = new agregationAxT().creationchart(stmt, (String) agregation_AxT.getValue(), (String) indicateur.getValue(), num_replication.getText(), String.valueOf(i), true, Agregation_Exp.getValue(), get_filtre_requete());
                        }
                        XYChart.Series ne = (XYChart.Series) (aXt.getData().get(0));
                        ((AreaChart) TabAxt.getSelectionModel().getSelectedItem().getContent()).getData().add(ne);
                    }

                } else {
                    newAxt a = new newAxt();
                    a.start();
                }
            }

            if (AxA_checkbox.isSelected()) {
                if (experimentation.getValue().equals("Toute") && Agregation_Exp.getValue().equals("")) {
                    showerrorwindow("Merci de choisir un type d'agrégation des expérimentations");

                } else {
                    newAxA a = new newAxA();
                    a.start();
                }
            }
        } else {
            if (AxT_checkbox.isSelected()) {

                if (experimentation.getValue().equals("Toute") && Agregation_Exp.getValue().equals("")) {
                  if(agregation_AxT.getValue().equals("")){
                      showerrorwindow("Veuillez introduire un agrégateur des simulations");
                  }else{
                  Tab t = new Tab("E :" + experimentation.getValue() + " " + agregation_AxT.getValue() + " R:toute " + indicateur.getValue());
                    t.setContent(new agregationAxT().creationchart(stmt, (String) agregation_AxT.getValue(), (String) indicateur.getValue(), "", "1", false, Agregation_Exp.getValue(), get_filtre_requete()));
                    TabAxt.getTabs().add(0, t);
                    SingleSelectionModel<Tab> selectionModel = TabAxt.getSelectionModel();
                    selectionModel.select(t);

                    for (int i = 2; i < experimentation.getItems().size() - 1; i++) {
                        XYChart.Series ne = (XYChart.Series) ((AreaChart) new agregationAxT().creationchart(stmt, (String) agregation_AxT.getValue(), (String) indicateur.getValue(), num_replication.getText(), String.valueOf(i), false, Agregation_Exp.getValue(), get_filtre_requete())).getData().get(0);
                        ((AreaChart) TabAxt.getSelectionModel().getSelectedItem().getContent()).getData().add(ne);
                    }
                  }
                    

                } else {
                    if(agregation_AxT.getValue().equals("")){
                      showerrorwindow("Veuillez introduire un agrégateur des simulations");
                  }else{
                    Tab t = new Tab("E :" + experimentation.getValue() + " " + agregation_AxT.getValue() + " R:toute " + indicateur.getValue());
                    t.setContent(new agregationAxT().creationchart(stmt, (String) agregation_AxT.getValue(), (String) indicateur.getValue(), "", experimentation.getValue(), false, Agregation_Exp.getValue(), get_filtre_requete()));
                    TabAxt.getTabs().add(0, t);
                    SingleSelectionModel<Tab> selectionModel = TabAxt.getSelectionModel();
                    selectionModel.select(t); //select by object
                    }
                }

            }
            if (AxA_checkbox.isSelected()) {

                Tab t = new Tab("E :" + experimentation.getValue() + " " + agregation_AxA_indicateur1.getValue() + " R:toute " + indicateur1_AxA.getValue());
                t.setContent(new agregationAxA().creationchart(stmt, (String) agregation_AxA_indicateur1.getValue(), (String) indicateur1_AxA.getValue(), "", experimentation.getValue(), false, Agregation_Exp.getValue(), get_filtre_requete()));
                TabAxA.getTabs().add(0, t);
                SingleSelectionModel<Tab> selectionModel = TabAxA.getSelectionModel();
                selectionModel.select(t);

            }

            if (AxE_checkbox.isSelected()) {

            }
        }

    }

    @FXML
    private void printonaction(ActionEvent event) throws IOException {
        saveimage(TabAxA);
    }

    @FXML
    private void printAxEonaction(ActionEvent event) throws IOException {
        saveimage(TabAxE);
    }

    @FXML
    private void printAxtonaction(ActionEvent event) throws IOException {
        saveimage(TabAxt);
    }

    @FXML
    private void play_axe_onaction(ActionEvent event) {
        if (PLAY_aXe.isSelected()) {
            Task task = new Task<Void>() {

                @Override
                protected Void call() throws Exception {

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    return null;
                }
            };
            Thread th = new Thread(task);
            th.start();
            TabAxE.setCursor(Cursor.WAIT);
            timer = new Timer();
            timer.schedule(
                    new TimerTask() {
                        int i = (int) slider_AxE_temps.getValue();

                        @Override

                        public void run() {

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    if (i >= max_step) {
                                        timer.cancel();
                                        PLAY_aXe.selectedProperty().set(false);
                                    } else {
                                        slider_AxE_temps.setValue(i);
                                        i++;
                                    }
                                }
                            });
                        }
                    }, 0, 1000);
        } else {
            TabAxE.setCursor(Cursor.DEFAULT);
            timer.cancel();
        }
    }

    @FXML
    private void close_Axt(ActionEvent event) {
        split_child.getItems().remove(Axt);
        checkmenuitem_Axt.setSelected(false);
        if (split_child.getItems().isEmpty()) {
            split_Body.getItems().remove(split_child);
        }
    }

    @FXML
    private void close_AxE(ActionEvent event) {

        split_child.getItems().remove(AxE);
        checkmenuitem_AxE.setSelected(false);
        if (split_child.getItems().isEmpty()) {
            split_Body.getItems().remove(split_child);
        }
    }

    @FXML
    private void closeAxA(ActionEvent event) {

        split_Body.getItems().remove(AxA);
        checkmenuitem_AxA.setSelected(false);
    }

    @FXML
    private void checkmenuitem_Axt_onaction(ActionEvent event) {
        if (checkmenuitem_Axt.isSelected()) {
            if (!split_Body.getItems().contains(split_child)) {
                split_Body.getItems().add(0, split_child);
                split_Body.setDividerPosition(0, 0.35);
            }
            split_child.getItems().add(0, Axt);
            split_child.setDividerPosition(0, 0.6);
        } else {
            split_child.getItems().remove(0);
        }
    }

    @FXML
    private void checkmenuitem_AxA_onaction(ActionEvent event) {
        if (checkmenuitem_AxA.isSelected()) {
            split_Body.getItems().add(1, AxA);
            split_Body.setDividerPosition(0, 0.35);
        } else {
            split_Body.getItems().remove(1);
        }
    }

    @FXML
    private void checkmenuitem_AxE_onaction(ActionEvent event) {
        if (checkmenuitem_AxE.isSelected()) {
            if (!split_Body.getItems().contains(split_child)) {
                split_Body.getItems().add(0, split_child);
                split_Body.setDividerPosition(0, 0.35);
            }
            split_child.getItems().add(AxE);
            split_child.setDividerPosition(0, 0.6);
        } else {
            split_child.getItems().remove(1);
        }
    }

    @FXML
    private void add_axt_onaction(ActionEvent event) throws IOException {

        if (unereplication.isSelected()) {
           
            try {
                addaxt(Integer.parseInt(num_replication.getText()), false);
            } catch (Exception e) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, e);
            }

        } else {
            if (experimentation.getValue().equals("Toute") && Agregation_Exp.getValue().equals("")) {
                for (int i = 1; i < experimentation.getItems().size() - 1; i++) {
                    XYChart.Series ne = (XYChart.Series) ((AreaChart) new agregationAxT().creationchart(stmt, (String) agregation_AxT.getValue(), (String) indicateur.getValue(), num_replication.getText(), String.valueOf(i), false, Agregation_Exp.getValue(), get_filtre_requete())).getData().get(0);
                    ((AreaChart) TabAxt.getSelectionModel().getSelectedItem().getContent()).getData().add(ne);
                }

            } else {
                if (agregation_AxT.getValue() == "") {

                } else {
                    XYChart.Series ne = (XYChart.Series) ((AreaChart) new agregationAxT().creationchart(stmt, (String) agregation_AxT.getValue(), (String) indicateur.getValue(), num_replication.getText(), experimentation.getValue(), false, Agregation_Exp.getValue(), get_filtre_requete())).getData().get(0);
                    ((AreaChart) TabAxt.getSelectionModel().getSelectedItem().getContent()).getData().add(ne);
                }
            }
        }

    }

    @FXML
    private void sourcesynchroneonaction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Fichier de simulation");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("NetLogo", "*.nlogo")
        );

        File file = fileChooser.showOpenDialog(alimenter.getScene().getWindow());

        if (file != null) {
            File newfile = new File("NetLogo/synchrone.nlogo");
            System.err.println("newfile "+newfile.getPath());
            System.err.println("source "+newfile.getPath());
            Path source = Paths.get(file.getPath());
            Path destination = Paths.get(newfile.getPath());

            if (newfile.exists()) {
                newfile.delete();
            }

            try {
                //new copierfichier().copyFile(file,newfile);
                Files.copy(source, destination);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        indicateursynchrone.getItems().clear();
        indicateur1_AxAsynchrone.getItems().clear();
        indicateur2_AxAsynchrone.getItems().clear();
        ObservableList<String> listesynchrone = new reconnaissance_nlogo().construction();
        indicateursynchrone.getItems().addAll(listesynchrone);
         indicateur1_AxAsynchrone.getItems().addAll(listesynchrone);
        indicateur2_AxAsynchrone.getItems().addAll(listesynchrone);
    }

    @FXML
    private void gosynchroneonaction(ActionEvent event) throws IOException, CompilerException, LogoException {
        if (temps_synchrone.getText().equals("")) {
            showerrorwindow("Veuillez introduire une durée de simulation");
        } else {
            newAxtsynchrone a = new newAxtsynchrone();
            a.run();
        }
        if (AxE_checkboxsynchrone.isSelected()) {
            showerrorwindow("La visualisation synchrone de l'espace n'est pas encore implémentée");
        }
    }

    @FXML
    private void filtre_buttononaction(ActionEvent event) {
        if (vbox_principal.getChildren().contains(filtre_grid)) {
            vbox_principal.getChildren().remove(filtre_grid);
        } else {
            vbox_principal.getChildren().add(3, filtre_grid);
        }

    }

    @FXML
    private void supprimer_filtreonaction(ActionEvent event) {
        ObservableList<String> potential = FXCollections.observableArrayList(liste_filtre.getSelectionModel().getSelectedItems());
        for (Iterator<String> it = potential.iterator(); it.hasNext();) {
            String string = it.next();
            System.err.println(string);
            liste_filtre.getItems().remove(string);
        }
        liste_filtre.getSelectionModel().clearSelection();

    }

    @FXML
    private void nouveau_filtreonaction(ActionEvent event) {
        liste_filtre.getItems().clear();
        liste_filtre.getSelectionModel().clearSelection();
    }

    public void saveimage(TabPane Tab) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.setInitialFileName(Tab.getSelectionModel().getSelectedItem().getText().replace("/", "_").replace(":", "_"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        File file = fileChooser.showSaveDialog(Tab.getScene().getWindow());

        if (file != null) {

            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);

            //WritableImage image = TabAxt.snapshot(params, null);
            WritableImage image = (Tab.getSelectionModel().getSelectedItem().getContent()).snapshot(params, null);

            try {

                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @FXML
    private void ajouter_filtreonaction(ActionEvent event) {
        liste_filtre.getItems().add(indicateur_filtre.getValue().toString() + " " + operateur_filtre.getValue().toString() + " " + valeur_filtre.getText());
    }

    @FXML
    private void add_axa_onaction(ActionEvent event) {
        if (toutereplication.isSelected()) {
            XYChart.Series ne = (XYChart.Series) ((AreaChart) new agregationAxA().creationchart(stmt, (String) agregation_AxA_indicateur1.getValue(), (String) indicateur1_AxA.getValue(), "", experimentation.getValue(), false, Agregation_Exp.getValue(), get_filtre_requete())).getData().get(0);
            ((AreaChart) TabAxA.getSelectionModel().getSelectedItem().getContent()).getData().add(ne);
        } else {

        }

    }

    @FXML
    private void Help_onaction(ActionEvent event) throws IOException {
      //  Desktop desktop = Desktop.getDesktop();
      //  desktop.open(new File("Help/Help.pdf"));
             new Open_file_all_systems().openURL("Help/Help.pdf");
    }

    @FXML
    private void admin_onaction(ActionEvent event) {
        tabmenu.getSelectionModel().select(2);
    }

    @FXML
    private void apropo_onaction(ActionEvent event) throws IOException {
       /* Desktop desktop = Desktop.getDesktop();
        desktop.open(new File("About/About.pdf"));*/
        new Open_file_all_systems().openURL("About/About.pdf");
    }

    @FXML
    private void Quitter_onaction(ActionEvent event) {
        try {
            Stage stage = (Stage) slider_AxA_replication.getScene().getWindow();
            stage.close();

        } catch (Exception ex) {
            Logger.getLogger(AlimenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void mywebsite_onaction(ActionEvent event) throws IOException, URISyntaxException {
      /*  if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(new URI("http://www.mehdiboukhechba.com/"));
        }
              */
        new Open_Browser_all_systems().openURL("http://www.mehdiboukhechba.com/");
    }

    @FXML
    private void Nouveau_onaction(ActionEvent event) {
        
        try {
            Stage stage = (Stage) slider_AxA_replication.getScene().getWindow();
            stage.close();

            new ProjetTransMondyn().start(new Stage());
        } catch (Exception ex) {
            Logger.getLogger(AlimenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void Fermer_onaction(ActionEvent event) {
        try {
            Stage stage = (Stage) slider_AxA_replication.getScene().getWindow();
            stage.close();

        } catch (Exception ex) {
            Logger.getLogger(AlimenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void Licence_onaction(ActionEvent event) {
    }

    /////////////
    // classes //
    /////////////
    class newAxt extends Thread {

        public void run() {

            Tab t = new Tab("E" + experimentation.getValue() + " R:" + num_replication.getText() + " " + indicateur.getValue());
            Task task = new Task<Void>() {
                public AreaChart aXt = null;

                @Override
                protected Void call() throws Exception {
                    Statement stmtaxt = c.createStatement();

                    if (agregation_AxT.getValue() == "") {
                     
                        aXt = new AxT().creationchart(stmtaxt, (String) indicateur.getValue(), Integer.parseInt(num_replication.getText()), experimentation.getValue(), get_filtre_requete(), Agregation_Exp.getValue());
                    } else {
                        System.err.println("exp : " + experimentation.getValue());
                        aXt = new agregationAxT().creationchart(stmt, (String) agregation_AxT.getValue(), (String) indicateur.getValue(), num_replication.getText(), experimentation.getValue(), true, Agregation_Exp.getValue(), get_filtre_requete());
                    }

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            t.setContent(aXt);
                            TabAxt.getTabs().add(0, t);
                            SingleSelectionModel<Tab> selectionModel = TabAxt.getSelectionModel();
                            selectionModel.select(t);
                        }
                    });
                    return null;

                }

                protected void done() {
                    this.cancel();
                }

            };

            Thread th = new Thread(task);

            th.start();

        }
    }

    class newAxA extends Thread {

        @Override
        public void run() {
            Tab t2 = new Tab("E" + experimentation.getValue() + " R:" + num_replication.getText() + " " + indicateur1_AxA.getValue() + "/" + indicateur2_AxA.getValue());

            Task task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    Statement stmtaxa = c.createStatement();
                    ScatterChart<Number, Number> aXa = new AxA().creationchart(stmtaxa, (String) indicateur1_AxA.getValue(), (String) indicateur2_AxA.getValue(), Integer.parseInt(num_replication.getText()), experimentation.getValue(), max_step, get_filtre_requete(), Agregation_Exp.getValue());

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            t2.setContent(aXa);
                            TabAxA.getTabs().add(0, t2);
                            SingleSelectionModel<Tab> selectionModel = TabAxA.getSelectionModel();
                            selectionModel.select(t2);

                        }
                    });
                    return null;
                }

                protected void done() {
                    this.cancel();
                }
            };

            Thread th = new Thread(task);

            th.start();

        }
    }

    class newAxE extends Thread {

        public void run() {
            Tab t = new Tab("E :" + experimentation.getValue() + " R:" + num_replication.getText() + " " + indicateur_AxE.getValue());
            Task task = new Task<Void>() {

                @Override
                protected Void call() throws Exception {

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            Statement stmtaxe;
                            Group Map = null;
                            try {
                                stmtaxe = c.createStatement();
                                Map = new AxE().creationMap(stmtaxe, (String) indicateur_AxE.getValue(), Integer.parseInt(num_replication.getText()), experimentation.getValue(), Integer.parseInt(temps_AxE.getText()), AxE_colorpicker.getValue(), Agregation_Exp.getValue());
                            } catch (SQLException | IOException ex) {
                                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            t.setContent(Map);
                            TabAxE.getTabs().add(0, t);
                            SingleSelectionModel<Tab> selectionModel = TabAxE.getSelectionModel();
                            selectionModel.select(t);
                        }
                    });
                    return null;
                }

                protected void done() {
                    this.cancel();
                }
            };

            Thread th = new Thread(task);
            th.start();

        }
    }

    class newAxtsynchrone {

        double valeuraxt;
        double valeuraxa1;
        double valeuraxa2;
        AreaChart<Number, Number> ac;
        ScatterChart<Number, Number> as;

        public void run() throws IOException, CompilerException, LogoException {
            if (AxT_checkboxsynchrone.isSelected()) {
                NumberAxis xAxis = new NumberAxis();
                final NumberAxis yAxis = new NumberAxis();
                ac = new AreaChart<Number, Number>(xAxis, yAxis);
                ac.setTitle("");
                xAxis.setLabel("Temps");
                yAxis.setLabel((String) indicateursynchrone.getValue());
                XYChart.Series series = new XYChart.Series();
                series.setName("R:Synchrone");
                ac.getData().add(series);
                ac.setTitle((String) indicateursynchrone.getValue() + " x Temps");
                Tab t = new Tab(indicateursynchrone.getValue() + "/Synchrone");
                t.setContent(ac);
                TabAxt.getTabs().add(0, t);
                SingleSelectionModel<Tab> selectionModel = TabAxt.getSelectionModel();
                selectionModel.select(t);
            }
            if (AxA_checkboxsynchrone.isSelected()) {
                
                NumberAxis xAxisS = new NumberAxis();
                final NumberAxis yAxisS = new NumberAxis();
                as = new ScatterChart<Number, Number>(xAxisS, yAxisS);
                as.setTitle("");
                xAxisS.setLabel(indicateur1_AxAsynchrone.getValue());
                yAxisS.setLabel(indicateur2_AxAsynchrone.getValue());
                XYChart.Series series = new XYChart.Series();
                series.setName("R:Synchrone");
                as.getData().add(series);
                as.setTitle(indicateur1_AxAsynchrone.getValue() + " x " + indicateur2_AxAsynchrone.getValue());
                Tab t = new Tab(indicateur1_AxAsynchrone.getValue() + " x " + indicateur2_AxAsynchrone.getValue() + "/Synchrone");
                t.setContent(as);
                TabAxA.getTabs().add(0, t);
                SingleSelectionModel<Tab> selectionModel = TabAxA.getSelectionModel();
                selectionModel.select(t);
            }

            HeadlessWorkspace ws = HeadlessWorkspace.newInstance();
            ws.open("NetLogo/synchrone.nlogo");
            //ws.openString(org.nlogo.util.Utils.url2String("/turtles.nlogo"));
            // ws.command("cro 8 [ fd 5 ]");
            ws.command("RESET-TICKS");

            ws.command("setup");
            Task task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    int iterations;
                    for (iterations = 1; iterations < Integer.valueOf(temps_synchrone.getText()); iterations++) {
                        if (isCancelled()) {
                            break;
                        }

                        ws.command("go");
                        final int numstep = iterations;

                        if (AxT_checkboxsynchrone.isSelected()) {
                            valeuraxt = (double) ws.report(indicateursynchrone.getValue());
                        }
                        if (AxA_checkboxsynchrone.isSelected()) {
                            valeuraxa1 = (double) ws.report(indicateur1_AxAsynchrone.getValue());
                            valeuraxa2 = (double) ws.report(indicateur2_AxAsynchrone.getValue());
                        }

                        Thread.sleep((long) vitesse_silder.getValue());

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                if (AxT_checkboxsynchrone.isSelected()) {
                                    ((XYChart.Series) ac.getData().get(0)).getData().add(new XYChart.Data(numstep, valeuraxt));
                                }
                                if (AxA_checkboxsynchrone.isSelected()) {
                                    ((XYChart.Series) as.getData().get(0)).getData().add(new XYChart.Data(valeuraxa1, valeuraxa2));

                                }
                            }
                        });

                    }

                    return null;
                }

                protected void done() {
                    this.cancel();
                    try {
                        ws.dispose();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            Thread th = new Thread(task);
            th.start();
        }
    }

    class rafraichirbd {

        public void run() throws IOException, CompilerException, LogoException {
            Task task = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    

                    Class.forName("org.sqlite.JDBC");
                    Connection c = DriverManager.getConnection("jdbc:sqlite:visuAgent.db");
                    c.setAutoCommit(false);
                    Statement stmtt = c.createStatement();
                    
                    stmtt.executeQuery("select max(num_step) from avoir_continent ");
                    //rs.close();
                    stmtt.close();
                    

                    return null;
                }
            };

            //start the background task
            Thread th = new Thread(task);
            // th.setDaemon(true);
            
            th.start();

        }
    }

    //////////////
    // Méthodes //
    //////////////
    public void load_controls() {
        liste_filtre.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        slider_AxA_replication.setTooltip(new Tooltip(String.valueOf(slider_AxA_replication.getValue())));

        slider_replication_AxA_label.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (newPropertyValue) {
             
                } else {
                    slider_AxA_replication.setValue(Double.valueOf(slider_replication_AxA_label.getText()));

                }
            }
        });

        vbox_principal.getChildren().remove(filtre_grid);

        AxE_colorpicker.setValue(Color.DARKGREEN);
        unereplication = new ToggleButton("Une");

        toutereplication = new ToggleButton("Toute");
        segbutton.getButtons().addAll(unereplication, toutereplication);
        segbutton.getStyleClass().add(SegmentedButton.STYLE_CLASS_DARK);

        unereplication.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (unereplication.isSelected()) {
                    add_axa.setDisable(true);
                    num_replication.setDisable(false);
                    agregation_AxA_indicateur1.setDisable(true);
                    indicateur2_AxA.setDisable(false);
                    type_agregation_label.setText("Agrégateur de temps");
                    slider_AxA_replication.setDisable(false);
                    slider_Axt_replication.setDisable(false);
                } else {

                    add_axa.setDisable(false);
                    num_replication.setDisable(true);
                    agregation_AxA_indicateur1.setDisable(false);
                    indicateur2_AxA.setDisable(true);

                }
            }
        });
        toutereplication.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (toutereplication.isSelected()) {
                    add_axa.setDisable(false);
                    slider_AxA_replication.setDisable(true);
                    slider_Axt_replication.setDisable(true);
                    indicateur2_AxA.setDisable(true);
                    num_replication.setDisable(true);
                    agregation_AxA_indicateur1.setDisable(false);
                    type_agregation_label.setText("Agrégateur des simulations");
                } else {

                    slider_AxA_replication.setDisable(false);
                    slider_Axt_replication.setDisable(false);
                    indicateur2_AxA.setDisable(false);
                    num_replication.setDisable(true);
                    agregation_AxA_indicateur1.setDisable(false);

                }
            }
        });
        experimentation.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (experimentation.getValue().equals("Toute")) {
                    Agregation_Exp.setDisable(false);
                    indicateur2_AxA.setDisable(true);

                } else {
                    Agregation_Exp.setDisable(true);
                    indicateur2_AxA.setDisable(false);

                }
            }
        });

        slider_AxA_replication.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                if (old_val.intValue() != new_val.intValue()) {
                    slider_replication_AxA_label.setText(String.valueOf(new_val.intValue()));
                    new AxA().modificationchart(stmt, (ScatterChart) TabAxA.getSelectionModel().getSelectedItem().getContent(), Integer.valueOf(slider_replication_AxA_label.getText()), Integer.valueOf(experimentation.getValue()), iteratif_togglebutton.isSelected(), max_step, get_filtre_requete());
                }

            }
        });

        slider_Axt_replication.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                if (old_val.intValue() != new_val.intValue()) {
                    slider_replication_Axt_label.setText(String.valueOf(new_val.intValue()));
                    addaxt(new_val.intValue(), iteratif_togglebutton_AxT.isSelected());

                }
            }
        });
        slider_AxE_temps.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                if (old_val.intValue() != new_val.intValue()) {
                    slider_temps_AxE_label.setText(String.valueOf(new_val.intValue()));
                    Task task = new Task<Void>() {
                        final Group g = new AxE().modificationmap(stmt, (Group) TabAxE.getSelectionModel().getSelectedItem().getContent(), (String) indicateur_AxE.getValue(), Integer.parseInt(num_replication.getText()), experimentation.getValue(), Integer.parseInt(slider_temps_AxE_label.getText()), AxE_colorpicker.getValue(), Agregation_Exp.getValue());

                        @Override
                        protected Void call() throws Exception {
                            TabAxE.getSelectionModel().getSelectedItem().setContent(g);
                            return null;
                        }
                    };
                    Thread th = new Thread(task);
                    th.start();
                }

            }
        });
        a_propos_droit.setText("VisuaAgent a été développée par Mehdi Boukhechba (mehdiboukhechba.com) au sein de l'UMR Géocité-CNRS");
        a_propos_droit.setVisible(false);
        unereplication.setSelected(true);
    }

    public void load_data_controls() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:visuAgent.db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ObservableList<String> liste = new reconnaissance_bd().construction(stmt, "step");
        ObservableList<String> listesynchrone = new reconnaissance_nlogo().construction();
        System.err.println("vide ?" + liste.isEmpty());
        setMax_replication();
        setMax_step();
        setMin_replication();
        setMin_step();
        slider_AxE_temps.setMax(max_step);
        slider_AxA_replication.setMax(max_replication);
        slider_Axt_replication.setMax(max_replication);
        slider_AxE_temps.setMin(min_step);
        //clear data
        indicateur.getItems().clear();
        indicateursynchrone.getItems().clear();
        indicateur1_AxAsynchrone.getItems().clear();
        indicateur2_AxAsynchrone.getItems().clear();
        indicateur_AxEsynchrone.getItems().clear();
        indicateur1_AxA.getItems().clear();
        indicateur2_AxA.getItems().clear();
        indicateur_filtre.getItems().clear();
        operateur_filtre.getItems().clear();
        indicateur_AxE.getItems().clear();
        agregation_AxT.getItems().clear();
        agregation_AxA_indicateur1.getItems().clear();

        experimentation.getItems().clear();

        //add data       
        indicateur.getItems().addAll(liste);
        indicateursynchrone.getItems().addAll(listesynchrone);
        indicateur1_AxAsynchrone.getItems().addAll(listesynchrone);
        indicateur2_AxAsynchrone.getItems().addAll(listesynchrone);
        indicateur_AxEsynchrone.getItems().addAll(listesynchrone);

        indicateur1_AxA.getItems().addAll(liste);

        indicateur2_AxA.getItems().addAll(liste);
        indicateur_filtre.getItems().addAll(liste);
        operateur_filtre.getItems().addAll("=", "!=", "<", ">", "<=", ">=");
        ObservableList<String> liste_AxE = new reconnaissance_bd().construction(stmt, "Avoir_continent");

        indicateur_AxE.getItems().addAll(liste_AxE);
        indicateur_AxE.getItems().removeAll("num_step", "ID_continent", "ID_replication", "ID_Experimentation");
        agregation_AxT.getItems().addAll("", "somme", "moyenne", "écart type", "min", "max", "coefficient de variation");
        agregation_AxT.getSelectionModel().select("");
        agregation_AxA_indicateur1.getItems().addAll("somme", "moyenne", "écart type", "min", "max", "coefficient de variation");
        agregation_AxA_indicateur1.getSelectionModel().select("moyenne");
        Agregation_Exp.getItems().addAll("", "avg", "min", "max", "sum");
        Agregation_Exp.getSelectionModel().select("");
        experimentation.getItems().add("Toute");
        try {

            ResultSet rs = stmt.executeQuery("Select  id_experimentation from experimentation;");
            while (rs.next()) {
                Double d = rs.getDouble(1);
                experimentation.getItems().add(rs.getString("id_experimentation"));

            }

            rs.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        num_replication.setText("1");
        num_replication.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("") || (newValue.matches("\\d+") && Double.valueOf(newValue) >= min_replication && Double.valueOf(newValue) <= max_replication)) {
                    // int value = Integer.parseInt(newValue));
                } else {
                    showerrorwindow("Merci de vérifier les valeurs saisies ,ce champ n'accepte que des valeurs numériques entre " + (int) min_replication + " et " + (int) max_replication + "");
                    num_replication.setText(oldValue);
                }
            }
        });
        temps_AxE.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("") || (newValue.matches("\\d+") && Double.valueOf(newValue) >= min_step && Double.valueOf(newValue) <= max_step)) {
                    // int value = Integer.parseInt(newValue));
                } else {
                    showerrorwindow("Merci de vérifier les valeurs saisies ,ce champ n'accepte que des valeurs numériques entre " + (int) min_step + " et " + (int) max_step + " ");
                    temps_AxE.setText(oldValue);
                }
            }
        });
        temps_synchrone.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("") || newValue.matches("\\d+")) {
                    // int value = Integer.parseInt(newValue));
                } else {
                    showerrorwindow("Merci de vérifier les valeurs saisies ,ce champ n'accepte que des valeurs numériques");
                    temps_synchrone.setText(oldValue);
                }
            }
        });

    }

    public double getMax_replication() {
        return max_replication;
    }

    public void setMax_replication() {

        try {

            ResultSet rs = stmt.executeQuery("Select max(id_replication) from replication where id_experimentation = 1;");
            while (rs.next()) {
                Double d = rs.getDouble(1);

                this.max_replication = d;
            }

            rs.close();
            // stmt.close();
            // c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    public double getMax_step() {
        return max_step;
    }

    public void setMin_step() {

        try {

            ResultSet rs = stmt.executeQuery("Select min(num_step) from step where id_replication = 1;");
            while (rs.next()) {
                Double d = rs.getDouble(1);

                this.min_step = d;
            }

            rs.close();
            // stmt.close();
            // c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    public double getMin_step() {
        return min_step;
    }

    public void setMax_step() {

        try {

            ResultSet rs = stmt.executeQuery("Select max(num_step) from step where id_replication = 1;");
            while (rs.next()) {
                Double d = rs.getDouble(1);

                this.max_step = d;
            }

            rs.close();
            // stmt.close();
            // c.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    public double getMin_replication() {
        return min_replication;
    }

    public void setMin_replication() {

        try {

            ResultSet rs = stmt.executeQuery("Select min(id_replication) from replication where id_experimentation = 1;");
            while (rs.next()) {
                Double d = rs.getDouble(1);

                this.min_replication = d;
            }

            rs.close();
            // stmt.close();
            // c.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    public String get_filtre_requete() {
        String requete = "and";
        if (!liste_filtre.getItems().isEmpty()) {
            for (int i = 0; i < liste_filtre.getItems().size(); i++) {
                String object = liste_filtre.getItems().get(i).replace(",", ".");
                requete = requete + " " + object + " and";
            }
            requete = requete.substring(0, requete.length() - 3);
        } else {
            requete = "";
        }
        System.err.println(requete);
        return requete;
    }

    public void addaxt(int new_val, boolean iteratif) {

        // new AxT().modificationchart((AreaChart) TabAxt.getSelectionModel().getSelectedItem().getContent(),Integer.valueOf(slider_replication_Axt_label.getText()),experimentation.getValue(),iteratif_togglebutton_AxT.isSelected(),get_filtre_requete(),Agregation_Exp.getValue());
        System.err.println("esss0");
        if (experimentation.getValue().equals("Toute") && Agregation_Exp.getValue().equals("")) {
            AreaChart aXt = null;
            for (int i = 1; i < experimentation.getItems().size() - 1; i++) {
                if (agregation_AxT.getValue() == "") {
                    aXt = new AxT().creationchart(stmt, (String) indicateur.getValue(), new_val, String.valueOf(i), get_filtre_requete(), Agregation_Exp.getValue());
                } else {
                    aXt = new agregationAxT().creationchart(stmt, (String) agregation_AxT.getValue(), (String) indicateur.getValue(), String.valueOf(new_val), String.valueOf(i), true, Agregation_Exp.getValue(), get_filtre_requete());
                }
                XYChart.Series ne = (XYChart.Series) (aXt.getData().get(0));
                ((AreaChart) TabAxt.getSelectionModel().getSelectedItem().getContent()).getData().add(ne);
            }

        } else {
            XYChart.Series ne = null;
            if (agregation_AxT.getValue().equals("")) {
                System.err.println("esss");
                ne = (XYChart.Series) ((AreaChart) new AxT().creationchart(stmt, (String) indicateur.getValue(), new_val, experimentation.getValue(), get_filtre_requete(), Agregation_Exp.getValue())).getData().get(0);
            } else {
                System.err.println("esss2");
                ne = (XYChart.Series) ((AreaChart) new agregationAxT().creationchart(stmt, (String) agregation_AxT.getValue(), (String) indicateur.getValue(), String.valueOf(new_val), experimentation.getValue(), true, Agregation_Exp.getValue(), get_filtre_requete())).getData().get(0);

            }
            System.err.println("esss3");
            // ne.setName(ne.getName()+(String) indicateur.getValue() ); 
            if (iteratif) {
                ((AreaChart) TabAxt.getSelectionModel().getSelectedItem().getContent()).getData().clear();
            }
            ((AreaChart) TabAxt.getSelectionModel().getSelectedItem().getContent()).getData().add(ne);
        }

    }

    public void showerrorwindow(String msg) {
        org.controlsfx.control.action.Action response = Dialogs.create()
                .owner(TabAxA.getScene().getWindow())
                .title("Erreur")
                .masthead("Erreur de saisie")
                .message(msg)
                .showError();
    }
     public void showmessagewindow(String msg) {
        org.controlsfx.control.action.Action response = Dialogs.create()
                .owner(TabAxA.getScene().getWindow())
                .title("Erreur")
                .masthead("Erreur de saisie")
                .message(msg)
                .showError();
    }
}
