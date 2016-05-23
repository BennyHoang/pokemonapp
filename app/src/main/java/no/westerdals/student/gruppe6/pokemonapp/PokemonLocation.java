package no.westerdals.student.gruppe6.pokemonapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by OleFredrik on 19.05.2016.
 */
public class PokemonLocation {
    private ArrayList<Pokemon> pokemons;
    private GoogleMap map;

    public ArrayList<Pokemon> getPokemons() {
        return pokemons;
    }

    public PokemonLocation() {
        pokemons = new ArrayList<Pokemon>();
        getData();
    }

    public void getData() {
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
                    HttpURLConnection connection = (HttpURLConnection) new URL("https://locations.lehmann.tech/locations").openConnection();
                    Scanner scanner = new Scanner(connection.getInputStream());

                    StringBuilder json = new StringBuilder();

                    while (scanner.hasNextLine()) {
                        json.append(scanner.nextLine());
                    }

                    return json.toString();
                    //return null;
                } catch (IOException e) {
                    throw new RuntimeException("Encountered a problem while downloading pokemon's", e);
                }
            }

            @Override
            protected void onPostExecute(final String json) {
                super.onPostExecute(json);

                //progressDialog.cancel();
                //final List<Contact> contacts = contactsFromJson(json);
                pokemons = new Gson().fromJson(json.toString(), new TypeToken<List<Pokemon>>(){}.getType());
                //displayContacts(contacts);

            }
        }.execute();
    }

}
