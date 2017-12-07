package br.com.john.adoptionproject.Activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import br.com.john.adoptionproject.Entidades.Gato;
import br.com.john.adoptionproject.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //singleton
    private List<Gato> listGato = Gato.getInstance().getGatoList();

    //para buscar localização atual
    private FusedLocationProviderClient mFusedLocationClient;


    //log
    private static final String TAG = MapsActivity.class.getName();
    //meu mapa
    private GoogleMap mMap;
    //identificador de requisição de permissão do GPS
    private static final int REQUEST_GPS = 1000;
    //identificador da requisição de permissão da câmera
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    //localização atual representada por Location
    private Location currentLocation;
    //localização atual para colocar no mapa (LatLng)
    private LatLng loc = null;
    //permite acesso ao GPS
    private LocationManager locationManager;
    //notificação quando eventos de GPS acontecerem
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Log.d(TAG, "listGato " + listGato);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        currentLocation = location;
                        loc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        Log.d(TAG, "currentLocation location" + currentLocation);
                        if (location != null) {
                            // Logic to handle location object
                        }
                    }
                });

    }


    @Override
    protected void onStart() {
        super.onStart();
        //verifica permissão concedida pelo usuário
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Para exibir coordenadas o app precisa do GPS", Toast.LENGTH_SHORT).show();
            } //pede permissão
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GPS);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    0, locationListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_GPS:
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    }
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String lat, lon;
        LatLng posicao;
        String telefone;
        //Laço para pegar o endereço
        for (Gato gato : listGato) {
            posicao = gato.getLatLng();
            telefone = gato.getTelefone();
          googleMap.addMarker(new MarkerOptions().position(posicao).title(telefone).icon(BitmapDescriptorFactory.defaultMarker()));
        }
    }


}
