/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projettransmondyn;

import java.util.ArrayList;

/**
 *
 * @author mehdi
 */
public class continent {

    private int id;
    private int replication;
    private int experimentation;
    private int temps;
    private ArrayList<ArrayList<String>> attribut;

    public continent(int id, int replication, int experimentation, int temps, ArrayList<ArrayList<String>> attribut) {
        this.id = id;
        this.replication = replication;
        this.experimentation = experimentation;
        this.temps = temps;
        this.attribut = attribut;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReplication(int replication) {
        this.replication = replication;
    }

    public void setExperimentation(int experimentation) {
        this.experimentation = experimentation;
    }

    public void setTemps(int temps) {
        this.temps = temps;
    }

    public void setAttribut(ArrayList<ArrayList<String>> attribut) {
        this.attribut = attribut;
    }

    public int getId() {
        return id;
    }

    public int getReplication() {
        return replication;
    }

    public int getExperimentation() {
        return experimentation;
    }

    public int getTemps() {
        return temps;
    }

    public ArrayList<ArrayList<String>> getAttribut() {
        return attribut;
    }

}
