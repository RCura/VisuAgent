/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projettransmondyn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 *
 * @author mehdi
 */
public class creationdb {

    public creationdb() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:visuAgent3.db");

            /////////////////////
            stmt = c.createStatement();

            stmt.executeUpdate("drop table  if exists Experimentation");
            stmt.executeUpdate("drop table  if exists Replication");
            stmt.executeUpdate("drop table  if exists Step");
            stmt.executeUpdate("drop table  if exists avoir_continent");
            stmt.executeUpdate("drop table  if exists continent");

            stmt.executeUpdate("CREATE  TABLE  Experimentation "
                    + "(ID_Experimentation  INT PRIMARY KEY     NOT NULL,"
                    + " proba_innovation_sensitivity           double, "
                    + " proba_birth_sensitivity            double, "
                    + " Energy_accumulation_per_timestep_sensitivity            double) ");

            String sql = "CREATE  TABLE  Replication "
                    + "(ID_replication INT      NOT NULL,"
                    + " ID_experimentation           double NOT NULL, "
                    + " filtre           int , "
                    + "PRIMARY KEY(ID_replication,ID_experimentation),"
                    + "FOREIGN KEY(ID_experimentation) REFERENCES Experimentation(ID_Experimentation)) ";

            stmt.executeUpdate(sql);

            stmt = c.createStatement();
            String sql2 = "CREATE  TABLE Step "
                    + "(num_step                  INT     NOT NULL,"
                    + " AgeMoyenGroupes           double, "
                    + " randomWalkDist            double, "
                    + " nombreGroupesAyantVecu    double, "
                    + "distanceTotaleParcourue    double ,"
                    + " nombreGroupesAyantTraverse     double, "
                    + " maxTechAtteint            double, "
                    + "nombreGroupesALaFin        double, "
                    + "nombreCellulesOccupees     double, "
                    + "nombreRegionsOccupees      double, "
                    + " distanceMoyenneDepart     double, "
                    + " distanceMinDepart         double, "
                    + " distanceMaxDepart         double, "
                    + " distanceStandardDeviationDepart    double, "
                    + " nombreGroupesSurlIle     double, "
                    + " meantech                 double, "
                    + " mintech                  double, "
                    + " mediantech               double, "
                    + " standard_deviationtech   double, "
                    + "eventBirth                double, "
                    + "eventDeath                double, "
                    + "eventTechPlus             double, "
                    + "eventTechDiffusion        double, "
                    + "eventTechMinus            double, "
                    + "eventMoveNoReason         double, "
                    + "eventMoveAndAlone         double, "
                    + "eventMoveAndTwoMore       double, "
                    + "eventDontMove             double, "
                    + "eventGoToSea              double, "
                    + "eventBackToContinent      double, "
                    + "eventIslandArrival        double, "
                    + " id_Replication  INT            not null, "
                    + "PRIMARY KEY(num_step,ID_Replication),"
                    + "FOREIGN KEY(id_Replication) REFERENCES Replication(ID_replication)) ";

            stmt.executeUpdate(sql2);

            stmt = c.createStatement();
            stmt.executeUpdate("CREATE  TABLE  Continent "
                    + "(ID_continent INT PRIMARY KEY     NOT NULL)");
            stmt = c.createStatement();
            stmt.executeUpdate("CREATE  TABLE  Avoir_continent "
                    + "(ID_continent INT   NOT NULL,"
                    + "num_step INT      NOT NULL,"
                    + "population  double,"
                    + "ressources  double,"
                    + "ID_replication INT      NOT NULL,"
                    + "PRIMARY KEY(ID_continent,num_step,ID_replication))"
            );

            stmt.close();
            c.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

}
