package br.com.john.adoptionproject.Activity;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import br.com.john.adoptionproject.Manifest;
import br.com.john.adoptionproject.R;

public class MenuActivity extends AppCompatActivity {

    private Button btnMeuPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnMeuPerfil = (Button) findViewById(R.id.btnMeuPerfil);

        btnMeuPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenuActivity.this, PerfilActivity.class);
                startActivity(intent);

            }
        });


    }


}
