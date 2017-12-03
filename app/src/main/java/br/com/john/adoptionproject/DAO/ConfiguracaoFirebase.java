package br.com.john.adoptionproject.DAO;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by John on 02/12/2017.
 */

public class ConfiguracaoFirebase {

    private static DatabaseReference refFirebase;

    private static FirebaseAuth appAuth;

    public static DatabaseReference getFirebase(){

        if (refFirebase == null){
            refFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return refFirebase;

    }


    public static FirebaseAuth getFirebaseAutenticacao(){

        if (appAuth == null){
            appAuth = FirebaseAuth.getInstance();
        }
        return appAuth;
    }

}