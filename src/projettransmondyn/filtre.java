/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projettransmondyn;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author mehdi
 */
public class filtre {

    private final SimpleStringProperty indicateur;
    private final SimpleStringProperty operation;
    private final SimpleStringProperty valeur;

    @Override
    public String toString() {
        return "filtre{" + "indicateur=" + indicateur + ", operation=" + operation + ", valeur=" + valeur + '}';
    }

    public filtre(String indicateur, String operation, String valeur) {
        this.indicateur = new SimpleStringProperty(indicateur);
        this.operation = new SimpleStringProperty(operation);
        this.valeur = new SimpleStringProperty();
    }

    public SimpleStringProperty getIndicateur() {
        return indicateur;
    }

    public SimpleStringProperty getOperation() {
        return operation;
    }

    public SimpleStringProperty getValeur() {
        return valeur;
    }

}
