package br.com.john.adoptionproject.Entidades;

/**
 * Created by Juliana on 04/12/2017.
 */

public class Gato {
    private String caracteristicas, donoGato;

    public Gato(){}

    public Gato(String caracteristicas, String donoGato) {
        this.caracteristicas = caracteristicas;
        this.donoGato = donoGato;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public String getDonoGato() {
        return donoGato;
    }

    public void setDonoGato(String donoGato) {
        this.donoGato = donoGato;
    }

    @Override
    public String toString() {
        return "Gato{" +
                "caracteristicas='" + caracteristicas + '\'' +
                ", donoGato='" + donoGato + '\'' +
                '}';
    }
}
