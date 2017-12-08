package br.com.john.adoptionproject.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
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
    private ImageView ivFotoPerfil;
    private Bundle bundle;
    private String token;
    private StorageReference appStorage;
    private Users usuario;
    private EditText nome, sobrenome, email;
    private static final String TAG = PerfilActivity.class.getName();
    DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        bundle = getIntent().getExtras();
        token = bundle.getString("token");

        nome = (EditText) findViewById(R.id.etxtPerfilNome);
        sobrenome = (EditText) findViewById(R.id.etxtPerfilSobrenome);
        email = (EditText) findViewById(R.id.etxtPerfilEmail);

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

                            usuario = user.getValue(Users.class);
                            //Log.d(TAG, "onDataChange key: " + usuario.getNome());

                            nome.setText(usuario.getName());
                            sobrenome.setText(usuario.getSurname());
                            email.setText(usuario.getEmail());
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
