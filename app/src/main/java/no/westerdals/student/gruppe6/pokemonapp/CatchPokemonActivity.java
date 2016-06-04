package no.westerdals.student.gruppe6.pokemonapp;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CatchPokemonActivity extends AppCompatActivity {
    private Button btnSubmitId;
    private EditText editText;
    private Context context;
    private String apiUrl;
    /*
    * TEST ID for pokemon:
    * Pikachu: s8f9jwewe89fhalifnln39
    * Pidgeot: fadah89dhadiulabsayub73
    * Groudon: fj9sfoina9briu420
    * */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiUrl = getString(R.string.api_url_catch);
        setContentView(R.layout.activity_catch_pokemon);

        btnSubmitId = (Button) findViewById(R.id.btnSubmitId);
        editText = (EditText) findViewById(R.id.editText);

        detectNFC();
        context = getApplicationContext();
        btnSubmitId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAndDisplayData(editText);
            }
        });
    }

    private void detectNFC() {
        String action = getIntent().getAction();

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);

            MifareUltralight ultralight = MifareUltralight.get(tag);

            try {
                ultralight.connect();
                String nFCID = getPokemonIdFromNFC(ultralight);
                editText.setText(nFCID);
                getAndDisplayData(editText);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(CatchPokemonActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                try {
                    ultralight.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Toast.makeText(CatchPokemonActivity.this, "Couldn't close", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @NonNull
    private String getPokemonIdFromNFC(MifareUltralight ultralight) throws IOException {
        byte[] payload = ultralight.readPages(8);
        return new String(payload);
    }

    void displayHttpResponse(CharSequence text) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }

    void getAndDisplayData(final EditText editText) {
        new AsyncTask<Void, Void, String>() {
            String url = apiUrl + editText.getText();

            @Override
            protected String doInBackground(final Void... params) {
                String returnValue = "";
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestProperty(getString(R.string.token_type), getString(R.string.token_value));
                    final int statusCode = connection.getResponseCode();
                    try {
                        switch (statusCode) {
                            case 200:
                                //Updates DB if entry was deleted
                                uppdateDatabaseAndStartNewActivity(connection);
                                returnValue = getString(R.string.return_value_200);
                                break;
                            case 201:
                                uppdateDatabaseAndStartNewActivity(connection);
                                returnValue = getString(R.string.return_value_201);
                                break;
                            case 401:
                                returnValue = getString(R.string.return_value_401);
                                break;
                            case 404:
                                returnValue = getString(R.string.return_value_404);
                                break;
                            case 420:
                                returnValue = getString(R.string.return_value_402);
                                break;
                        }
                    } catch (FileNotFoundException | JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return returnValue;
            }

            @Override
            protected void onPostExecute(final String response) {
                super.onPostExecute(response);
                displayHttpResponse(response);
            }
        }.execute();
    }

    private void uppdateDatabaseAndStartNewActivity(HttpURLConnection connection) throws IOException, JSONException {
        InputStream inputStream;
        Scanner scanner;
        inputStream = connection.getInputStream();
        scanner = new Scanner(inputStream);

        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
        }

        JSONObject jsonObject = new JSONObject(new String(stringBuilder.toString()));
        MyPokemon Mypokemon = new MyPokemon(jsonObject);
        DBhandler dBhandler = new DBhandler(context);
        dBhandler.addPokemon(Mypokemon);
        Intent intent = new Intent(CatchPokemonActivity.this, MyPokemonListActivity.class);
        startActivity(intent);
    }

}