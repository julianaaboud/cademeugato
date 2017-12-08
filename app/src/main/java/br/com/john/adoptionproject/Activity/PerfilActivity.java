package br.com.john.adoptionproject.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import br.com.john.adoptionproject.DAO.ConfiguracaoFirebase;
import br.com.john.adoptionproject.Entidades.Users;
import br.com.john.adoptionproject.R;

public class PerfilActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    //private static final int RESULT_LOAD_IMAGE = 1;
    private Button btnGravarFoto;
    private Button btnEditarPerfil;
    private ImageView ivFotoPerfil;
    private Bundle bundle;
    private String token;
    private StorageReference appStorage;
    private Users users;
    private EditText nome, sobrenome, email;
    DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();

    private static final String TAG = PerfilActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        bundle = getIntent().getExtras();
        token = bundle.getString("token");

        Log.d(TAG, "onCreateView token:" + token);

        nome = (EditText) findViewById(R.id.etxtPerfilNome);
        sobrenome = (EditText) findViewById(R.id.etxtPerfilSobrenome);
        email = (EditText) findViewById(R.id.etxtPerfilEmail);
        btnEditarPerfil = (Button) findViewById(R.id.btnEditarPerfil);

        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users.setName(nome.getText().toString());
                users.setSurname(sobrenome.getText().toString());
                users.setEmail(email.getText().toString());
                Log.d(TAG, "token" + token);
                DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
                referenciaFirebase.child("usuario").child(token).setValue(users);
                Toast.makeText(PerfilActivity.this, "Alteração efetuada com sucesso", Toast.LENGTH_LONG).show();
            }

        });

        referenciaFirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (DataSnapshot dataSnapshot){
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot = dataSnapshot.child("usuario");

                    Iterable<DataSnapshot> userChildren = snapshot.getChildren();

                    for (DataSnapshot user : userChildren) {
                        if (user.getKey().equals(token)) {
                            String key = user.getValue().toString();
                            //Log.d(TAG, "onDataChange key: " + key);

                            users = user.getValue(Users.class);
                            //Log.d(TAG, "onDataChange key: " + usuario.getNome());

                            nome.setText(users.getName());
                            sobrenome.setText(users.getSurname());
                            email.setText(users.getEmail());
                        }
                    }
                }
            }

            @Override
            public void onCancelled (DatabaseError databaseError){

            }
        });


        appStorage = FirebaseStorage.getInstance().getReference();


        btnGravarFoto = (Button) findViewById(R.id.btnGravarFoto);
        ivFotoPerfil = (ImageView) findViewById(R.id.ivFotoPerfil);

        btnGravarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            StorageReference filepath = appStorage.child("ProfilePics").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();

                    Picasso.with(PerfilActivity.this).load(downloadUri).fit().centerCrop().into(ivFotoPerfil);

                    Toast.makeText(PerfilActivity.this, "Imagem salva com sucesso", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
