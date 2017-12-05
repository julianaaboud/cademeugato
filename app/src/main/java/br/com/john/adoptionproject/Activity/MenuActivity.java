package br.com.john.adoptionproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import br.com.john.adoptionproject.R;

public class MenuActivity extends AppCompatActivity {

    private Button btnMeuPerfil;
    private CardView option1CardView;
    private CardView option2CardView;
    private Bundle bundle;
    private String token;
    private static final String TAG = MenuActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bundle = getIntent().getExtras();
        token = bundle.getString("token");
        Log.d (TAG, "token do bundle " + token);

        option1CardView = (CardView) findViewById(R.id.option1_card_view);
        option2CardView = (CardView) findViewById(R.id.option2_card_view);
        btnMeuPerfil = (Button) findViewById(R.id.btnMeuPerfil);

        option1CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        option2CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, CadastroGatoActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });

        btnMeuPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenuActivity.this, PerfilActivity.class);
                startActivity(intent);

            }
        });





    }


}
