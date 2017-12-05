package br.com.john.adoptionproject.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;

import br.com.john.adoptionproject.Entidades.Gato;
import br.com.john.adoptionproject.Entidades.Users;
import br.com.john.adoptionproject.R;

public class CadastroGatoActivity extends AppCompatActivity {

    private EditText caracteristica;
    private Button cadastro;
    private String donoGato;
    private Users usuario;
    private Bundle bundle;
    private String token;
    private FirebaseDatabase database;
    private Gato gato;

    private static final String TAG = CadastroGatoActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_gato);

        bundle = getIntent().getExtras();
        token = bundle.getString("token");
        Log.d(TAG, "token no cadastro " + token);
        caracteristica = findViewById(R.id.caracteristica);
        cadastro = (Button) findViewById(R.id.btn_cadastra_gato);

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gato = new Gato();
                gato.setCaracteristicas(caracteristica.getText().toString());
                gato.setDonoGato(token);
                gato.salvar(getApplicationContext(), gato);
            }
        });

        }


    public void takePic (View view){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
        }
        else{
            startActivityForResult( new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
        }
    }

    private final int REQUEST_CAMERA = 1;
    private final int REQUEST_PERMISSION_CAMERA = 2;

}
