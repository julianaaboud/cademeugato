package br.com.john.adoptionproject.Entidades;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
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
    private String caracteristicas, donoGato, telefone, foto;
    LatLng latLng;
    private static Gato instance;
    private static List<Gato> gatoList = new ArrayList<>();

    public Gato(){}

    public void salvar(final Context context, final Gato gato){
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase = referenciaFirebase.child("gato").push();
        referenciaFirebase.setValue(this, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                getGatoList().add(gato);
            }
        });
    }


    public Gato(String caracteristicas, String donoGato, String telefone, String foto, LatLng latLng) {
        this.caracteristicas = caracteristicas;
        this.donoGato = donoGato;
        this.telefone = telefone;
        this.foto = foto;
        this.latLng = latLng;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public String toString() {
        return "Gato{" +
                "caracteristicas='" + caracteristicas + '\'' +
                ", donoGato='" + donoGato + '\'' +
                ", telefone='" + telefone + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }

    public static List<Gato> getGatoList() {
        return gatoList;
    }

    public static void setGatoList(List<Gato> gatoList) {
        Gato.gatoList = gatoList;
    }

}


