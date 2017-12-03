package br.com.john.adoptionproject.Helper;

import android.app.SharedElementCallback;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by John on 03/12/2017.
 */

public class Preferencias {

    private Context context;
    private SharedPreferences preferences;
    private String NOME_ARQUIVO = "adoptionProject.Preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;
    private final String CHAVE_IDENTIFICADOR = "identificarUsuarioLogado", CHAVE_NOME = "nomeUsuarioLogado";

    public Preferencias(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(NOME_ARQUIVO, MODE);

        editor = preferences.edit();
    }

    public void salvarUsuarioPreferencias (String identificadorUsuario, String nomeUsuario){

        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.putString(CHAVE_NOME, nomeUsuario);

    }

    public String getIdentificador(){
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }

    public String getNome(){
        return preferences.getString(CHAVE_NOME, null);
    }

}
