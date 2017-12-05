package br.com.john.adoptionproject.Entidades;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

import br.com.john.adoptionproject.DAO.ConfiguracaoFirebase;

/**
 * Created by Juliana on 04/12/2017.
 */

public class Gato {
    private String caracteristicas, donoGato;
    private static Gato instance;
    private static List<Gato> gatoList = new ArrayList<>();

    public Gato(){}

    public void salvar(final Context context, final Gato gato){
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase = referenciaFirebase.child("gato").push();
        referenciaFirebase.setValue(this, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(context, "Sucesso ao cadastrar o Gato", Toast.LENGTH_LONG).show();
                getGatoList().add(gato);
            }
        });
    }


    public Gato(String caracteristicas, String donoGato) {
        this.caracteristicas = caracteristicas;
        this.donoGato = donoGato;
    }

    @Exclude
    public static Gato getInstance (){

        return instance == null ? instance = new Gato() : instance;
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

    public static List<Gato> getGatoList() {
        return gatoList;
    }

    public static void setOngList(List<Gato> gatoList) {
        Gato.gatoList = gatoList;
    }

}

