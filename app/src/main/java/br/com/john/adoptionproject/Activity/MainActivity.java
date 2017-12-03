package br.com.john.adoptionproject.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import br.com.john.adoptionproject.DAO.ConfiguracaoFirebase;
import br.com.john.adoptionproject.Entidades.Users;
import br.com.john.adoptionproject.R;

public class MainActivity extends AppCompatActivity {

    private EditText etxtEmail, etxtSenha;
    private Button btnLogin;
    private TextView txtCadastrarLink;
    private FirebaseAuth appAuth;
    private Users users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
                    Toast.makeText(MainActivity.this, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void abrirTelaPrincipal(){
        Intent intentAbrirTelaPrincipal = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intentAbrirTelaPrincipal);
    }

    public void abrirCadastroUsuario(){
        Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
        startActivity(intent);
    }
}
