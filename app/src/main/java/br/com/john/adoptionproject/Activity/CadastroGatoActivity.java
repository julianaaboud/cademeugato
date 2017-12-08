package br.com.john.adoptionproject.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import br.com.john.adoptionproject.Entidades.Gato;
import br.com.john.adoptionproject.R;

public class CadastroGatoActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText caracteristica;
    private Button cadastro;
    private EditText telefone;
    private String lat;
    private String lon;
    private LatLng loc;
    private Bundle bundle;
    private String token;
    private FirebaseDatabase database;
    private Gato gato;
    private FusedLocationProviderClient mFusedLocationClient;
    private final int REQUEST_CAMERA = 1;
    private final int REQUEST_PERMISSION_CAMERA = 2;
    private StorageReference appStorage;
    private ImageView camera;
    private ImageView fotoGato = null;
    private Uri downloadUri;
    private Uri uri;


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
        camera = (ImageView) findViewById(R.id.fotoImageView);
        telefone = findViewById(R.id.telefone);
        fotoGato = (ImageView) findViewById(R.id.fotoImageView1);
        appStorage = FirebaseStorage.getInstance().getReference();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            loc = new LatLng(location.getLatitude(), location.getLongitude());
                            lat = String.valueOf(location.getLatitude());
                            lon = String.valueOf(location.getLongitude());
                            Log.d(TAG, "currentLocation lat " + lat);
                            Log.d(TAG, "currentLocation lon " + lon);
                        } else {
                            Toast.makeText(CadastroGatoActivity.this, "Localização não encontrada", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (caracteristica.getText().toString().equals("") || telefone.getText().toString().equals("")) {
                    Toast.makeText(CadastroGatoActivity.this, "Preencha as características e o telefone", Toast.LENGTH_SHORT).show();
                } else if (uri == null) {
                    Toast.makeText(CadastroGatoActivity.this, "Por favor, tire uma foto do gatinho", Toast.LENGTH_SHORT).show();
                } else {
                    gato = new Gato();
                    gato.setCaracteristicas(caracteristica.getText().toString());
                    gato.setDonoGato(token);
                    gato.setLatLng(loc);
                    gato.setTelefone(telefone.getText().toString());
                    gato.salvar(getApplicationContext(), gato);
                    StorageReference filepath = appStorage.child("ProfilePics");
                    filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            downloadUri = taskSnapshot.getDownloadUrl();
                            Picasso.with(CadastroGatoActivity.this).load(downloadUri).fit().centerCrop().into(fotoGato);
                            Toast.makeText(CadastroGatoActivity.this, "Upload de Imagem OK", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Toast.makeText(getApplication(), "Sucesso ao cadastrar o Gato", Toast.LENGTH_LONG).show();
                    finish();

                }
            }
        });


    }


    public void takePic(View view) {
       /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
        }
        else{
            startActivityForResult( new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
        }*/

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            uri = data.getData();
            //  StorageReference filepath = appStorage.child("ProfilePics").child(uri.getLastPathSegment());


        }
    }


}
