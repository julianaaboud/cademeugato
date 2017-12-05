package br.com.john.adoptionproject.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.john.adoptionproject.R;

public class CadastroGatoActivity extends AppCompatActivity {

    private EditText caracteristica;
    private Button cadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_gato);

        caracteristica = (EditText) findViewById(R.id.caracteristica);
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
