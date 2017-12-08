package br.com.john.adoptionproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import br.com.john.adoptionproject.DAO.ConfiguracaoFirebase;
import br.com.john.adoptionproject.Entidades.Gato;
import br.com.john.adoptionproject.Entidades.Users;
import br.com.john.adoptionproject.R;

public class MainActivity extends AppCompatActivity {
    private List<Gato> listGato = Gato.getInstance().getGatoList();
    private EditText etxtEmail, etxtSenha;
    private Button btnLogin;
    private TextView txtCadastrarLink;
    private FirebaseAuth appAuth;
    private Users users;
    private static final String TAG = MainActivity.class.getName();
    final DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //buscaListaDeGato();
        etxtEmail = (EditText) findViewById(R.id.etxtEmail);
        etxtSenha = (EditText) findViewById(R.id.etxtSenha);
        txtCadastrarLink = (TextView) findViewById(R.id.txtCadastrarLink);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (!etxtEmail.getText().toString().equals("") && !etxtSenha.getText().toString().equals("")){
                    users = new Users();
                    users.setEmail(etxtEmail.getText().toString());
                    users.setPassword(etxtSenha.getText().toString());

                    validarLogin();
                }
                else {
                    Toast.makeText(MainActivity.this, "Por favor, insira um e-mail e senha", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtCadastrarLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCadastroUsuario();
            }
        });
    }


    private void validarLogin(){
        appAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        appAuth.signInWithEmailAndPassword(users.getEmail(), users.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    abrirTelaPrincipal();
                }

            }
        });
    }

    public void abrirTelaPrincipal(){
        Intent intentAbrirTelaPrincipal = new Intent(MainActivity.this, MenuActivity.class);
        Log.d(TAG, "token no main" + appAuth.getCurrentUser().getUid());
        intentAbrirTelaPrincipal.putExtra("token", appAuth.getCurrentUser().getUid());
        startActivity(intentAbrirTelaPrincipal);
    }

    public void abrirCadastroUsuario(){
        Intent intent = new Intent(MainActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }



    public void buscaListaDeGato() {
        //Busca no Firebase
        Query buscaQuery = referenciaFirebase.child("gato");

        //Snapshot
        buscaQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Gato gato = postSnapshot.getValue(Gato.class);
                    Log.d(TAG, "gato: " + gato.getCaracteristicas());
                    listGato.add(gato);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
