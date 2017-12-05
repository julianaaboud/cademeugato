package br.com.john.adoptionproject.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import br.com.john.adoptionproject.R;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText etxtCadNome, etxtCadSobrenome, etxtCadEmail, etxtCadSenha, etxtCadConfirmaSenha;
    private ImageView fotoImageView;
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
        fotoImageView = (ImageView) findViewById(R.id.fotoImageView);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (etxtCadSenha.getText().toString().equals(etxtCadConfirmaSenha.getText().toString())) {
                    users = new Users();
                    users.setName(etxtCadNome.getText().toString());
                    users.setSurname(etxtCadSobrenome.getText().toString());
                    users.setEmail(etxtCadEmail.getText().toString());
                    users.setPassword(etxtCadSenha.getText().toString());
                    if (rbMasculino.isChecked()) {
                        users.setSex("Masculino");
                    } else {
                        users.setSex("Feminino");
                    }
                    cadastrarUsuario();
                } else {
                    Toast.makeText(CadastroUsuarioActivity.this, "As senhas não são iguais", Toast.LENGTH_LONG).show();
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            fotoImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startActivityForResult( new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
        }
    }


    private void cadastrarUsuario() {
        appAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        appAuth.createUserWithEmailAndPassword(
                users.getEmail(),
                users.getPassword()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CadastroUsuarioActivity.this, "Usuário cadastrado com sucesso", Toast.LENGTH_LONG).show();
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    users.setId(usuarioFirebase.getUid());
                    users.salvar();

                    abrirLoginUsuario();
                } else {
                    String erroExcecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte, contendo no mínimo 8 caracteres de letras e números";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "E-mail inválido";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "E-mail já cadastrado!";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroUsuarioActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void abrirLoginUsuario() {
        Intent intent = new Intent(CadastroUsuarioActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
