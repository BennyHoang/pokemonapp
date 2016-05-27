package no.westerdals.student.gruppe6.pokemonapp;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

public class CatchPokemonActivity extends AppCompatActivity {
    private Button btnSubmitId;
    private EditText editText;
    private Context context;
    /*
    * TEST ID for pokemon:
    * Pikachu: s8f9jwewe89fhalifnln39
    * Pidgeot: fadah89dhadiulabsayub73
    * Groudon: fj9sfoina9briu420
    * */
    String apiUrl = "https://locations.lehmann.tech/pokemon/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch_pokemon);

        btnSubmitId = (Button) findViewById(R.id.btnSubmitId);
        editText = (EditText) findViewById(R.id.editText);

        detectNFC();
        context = getApplicationContext();
        Toast.makeText(CatchPokemonActivity.this, "onCreate done", Toast.LENGTH_SHORT).show();
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

    //Insane hack to get rid of NFC flags in string.
    @NonNull
    private String getPokemonIdFromNFC(MifareUltralight ultralight) throws IOException {
        byte[] payload = ultralight.readPages(8);
        return new String(payload);

//        StringBuilder builder = new StringBuilder();
//        String output = new String(payload, Charset.forName("US-ASCII"));
//        builder.append(output);
//        //builder.deleteCharAt(0);
//        payload = ultralight.readPages(12);
//        byte[] bytes = new byte[16];
//        for(int i = 0; i < payload.length; i++)
//        {
//            if(payload[i] == -2) break;
//                bytes[i] = payload[i];
//        }
//
//        builder.append(new String(bytes, Charset.forName("US-ASCII")));
//        //builder.deleteCharAt(0);
//        //builder.deleteCharAt(1);
//        return builder.toString();
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
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestProperty("X-Token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.ImdydXBwZSA2Ig.ZWVrv8AWDiH_X358jZ6IYrNgMXDq1B7UvbyiDoEP2Q0");
                    final int statusCode = connection.getResponseCode();
                    try {
                        InputStream inputStream = connection.getInputStream();
                        Scanner scanner = new Scanner(inputStream);

                        switch (statusCode) {
                            case 200:
                                //TODO: Move to case 201 after testing!
                                uppdateDatabaseAndStartNewActivity(connection);
                                return "already created";
                            case 201:
                                //Reads the input
                                //TODO: append to JSON Object
                                //TODO: Create method for inputstream
                                uppdateDatabaseAndStartNewActivity(connection);
                                return "CATCHED NEW POKEMON! :)";
                        }
                        return "other status msg: " + statusCode + " = " + connection.getResponseCode();
                        //return new StringBuilder().append("GOT ERROR WITH CODE: ").append(connection.getResponseCode()).append(" with message: ").append(connection.getResponseMessage()).toString();
                    } catch (FileNotFoundException e) {

                        switch (statusCode) {
                            case 401:
                                return "Unauthorized, check your Token plz";
                            case 404:
                                return "Please sumbit ID";
                            case 420:
                                return "Wrong ID";
                        }
                         return "other status msg: " + statusCode + " = " + connection.getResponseCode();
                       // return new StringBuilder().append("GOT ERROR WITH CODE: ").append(connection.getResponseCode()).append(" with message: ").append(connection.getResponseMessage()).toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                //TODO: Wat is this method supposed to return! Se: http://viralswarm.s3.amazonaws.com/wp-content/uploads/2014/11/AYch4Io.jpg
                return "Wat";
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