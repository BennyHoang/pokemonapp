package no.westerdals.student.gruppe6.pokemonapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PokeMapsActivity extends FragmentActivity implements OnMapReadyCallback,  ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;
    private ArrayList<Pokemon> pokemons;
    private Button btnAddPokemon;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnAddPokemon = (Button) findViewById(R.id.btnAddPokemon);
        btnAddPokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PokeMapsActivity.this, CatchPokemonActivity.class);
                startActivity(intent);
            }
        });
    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(final Void... params) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL("https://locations.lehmann.tech/locations").openConnection();
                    Scanner scanner = new Scanner(connection.getInputStream());
                    StringBuilder json = new StringBuilder();

                    while (scanner.hasNextLine()) {
                        json.append(scanner.nextLine());
                    }
                    return json.toString();
                } catch (IOException e) {
                    throw new RuntimeException("Encountered a problem while downloading contacts", e);
                }
            }

            @Override
            protected void onPostExecute(final String json) {
                super.onPostExecute(json);

                //progressDialog.cancel();
                //final List<Contact> contacts = contactsFromJson(json);
                pokemons = new Gson().fromJson(json.toString(), new TypeToken<List<Pokemon>>(){}.getType());
                ArrayList<MyPokemon> myPokemons = new MyPokemonManager(getApplicationContext()).getMyPokemonList();
                for (Pokemon pokemon : pokemons ) {
                    boolean alreadyInDB = false;

                    for(MyPokemon p : myPokemons)
                        if (p.comparePokemon(pokemon)) alreadyInDB = true;


                    LatLng pos = new LatLng(pokemon.getLat(), pokemon.getLng());
                    if(alreadyInDB) {
                        mMap.addMarker(
                                new MarkerOptions()
                                        .position(pos)
                                        .title(pokemon.getName())
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                        .snippet("Kåt")
                        );
                    } else {
                        mMap.addMarker(
                                new MarkerOptions()
                                        .position(pos)
                                        .title(pokemon.getName())
                        );
                    }
                }
                LatLng pos = new LatLng(pokemons.get(0).getLat(), pokemons.get(0).getLng());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
                mMap.animateCamera(zoom);
            }
        }.execute();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }
}


