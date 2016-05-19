package no.westerdals.student.gruppe6.pokemonapp;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

public class PokeMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Pokemon> pokemons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera

      /*  LatLng sydney = new LatLng(pokemons.get(1).getLat(), pokemons.get(1).getLat());
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

           // LatLng sydney = new LatLng(34, 20);
           // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
           // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        /*--------------------------------------------------------------------------------------------------------*/
        new AsyncTask<Void, Void, String>() {
            // private final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //progressDialog.setMessage("Loading contacts");
                //progressDialog.show();
            }

            @Override
            protected String doInBackground(final Void... params) {
                try {
                    // Sleep for a couple of seconds to demo the progress dialog on fast connections.
                    // Can be safely removed!
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    //Log.e("MainActivity", "Something went wrong while sleeping…", e);
                }

                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL("https://locations.lehmann.tech/locations").openConnection();
                    Scanner scanner = new Scanner(connection.getInputStream());

                    String json = "";

                    while (scanner.hasNextLine()) {
                        json += scanner.nextLine();
                    }

                    return json;
                    //return null;
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
                for (Pokemon pokemon : pokemons ) {
                    LatLng pos = new LatLng(pokemon.getLat(), pokemon.getLng());
                    mMap.addMarker(new MarkerOptions().position(pos).title(pokemon.getName()));
                }
                //LatLng pos = new LatLng(pokemons.get(0).getLat(), pokemons.get(0).getLng());
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
                //displayContacts(contacts);

            }
        }.execute();



    }
}


