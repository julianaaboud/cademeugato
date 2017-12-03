package br.com.john.adoptionproject.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import br.com.john.adoptionproject.DAO.ConfiguracaoFirebase;
import br.com.john.adoptionproject.Entidades.Users;
import br.com.john.adoptionproject.Helper.Base64Custom;
import br.com.john.adoptionproject.Helper.Preferencias;
import br.com.john.adoptionproject.R;

public class CadastroActivity extends AppCompatActivity {

    private EditText etxtCadNome, etxtCadSobrenome, etxtCadEmail, etxtCadSenha, etxtCadConfirmaSenha;
    private RadioButton rbMasculino, rbFeminino;
    private Button btnCadastrar;
    private Users users;
    private FirebaseAuth appAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        etxtCadNome = (EditText) findViewById(R.id.etxtCadNome);
        etxtCadSobrenome = (EditText) findViewById(R.id.etxtCadSobrenome);
        etxtCadEmail = (EditText) findViewById(R.id.etxtCadEmail);
        etxtCadSenha = (EditText) findViewById(R.id.etxtCadSenha);
        etxtCadConfirmaSenha = (EditText) findViewById(R.id.etxtCadConfirmaSenha);
        rbMasculino = (RadioButton) findViewById(R.id.rbMasculino);
        rbFeminino = (RadioButton) findViewById(R.id.rbFeminino);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);


        btnCadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View v){
                if (etxtCadSenha.getText().toString().equals(etxtCadConfirmaSenha.getText().toString())){
                    users = new Users();
                    users.setName(etxtCadNome.getText().toString());
                    users.setSurname(etxtCadSobrenome.getText().toString());
                    users.setEmail(etxtCadEmail.getText().toString());
                    users.setPassword(etxtCadSenha.getText().toString());

                    if (rbMasculino.isChecked()){

                        users.setSex("Masculino");

                    }
                    else{
                        users.setSex("Feminino");
                    }

                    cadastrarUsuario();

                }
                else {
                    Toast.makeText(CadastroActivity.this, "As senhas não são iguais", Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    private void cadastrarUsuario(){

        appAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        appAuth.createUserWithEmailAndPassword(
                users.getEmail(),
                users.getPassword()
        ).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso", Toast.LENGTH_LONG).show();

                    String identificadorUsuario = Base64Custom.codificarBase64(users.getEmail());

                    FirebaseUser usuarioFirebase = task.getResult().getUser();

                    users.setId(identificadorUsuario);
                    users.salvar();

                    Preferencias preferencias = new Preferencias(CadastroActivity.this);
                    preferencias.salvarUsuarioPreferencias(identificadorUsuario, users.getName());

                    abrirLoginUsuario();
                }
                else {
                    String erroExcecao = "";

                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthWeakPasswordException e){

                        erroExcecao = "Digite uma senha mais forte, contendo no mínimo 8 caracteres de letras e números";
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){

                        erroExcecao = "O e-mail digitado é inválido, digite um novo e-mail";
                    }
                    catch (FirebaseAuthUserCollisionException e){

                        erroExcecao = "Este e-mail já está cadastrado no sistema";
                    }
                    catch (Exception e){

                        erroExcecao = "Erro ao efetuar o cadastro";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, "Erro: "+ erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void abrirLoginUsuario (){
        Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
