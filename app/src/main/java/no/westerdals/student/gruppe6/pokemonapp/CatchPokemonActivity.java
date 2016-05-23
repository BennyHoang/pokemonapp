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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;

public class CatchPokemonActivity extends AppCompatActivity {
    private Button btnSubmitId;
    private TextView responseTextView;
    private EditText editText;
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
        responseTextView = (TextView) findViewById(R.id.responseTextView);
        editText = (EditText) findViewById(R.id.editText);

        String action = getIntent().getAction();

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);

            MifareUltralight ultralight = MifareUltralight.get(tag);

            try {
                ultralight.connect();
                String nFCID = getPokemonIdFromNFC(ultralight);
                editText.setText(nFCID);
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
        Toast.makeText(CatchPokemonActivity.this, "onCreate done", Toast.LENGTH_SHORT).show();
        btnSubmitId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAndDisplayData(editText);
            }

        });
    }

    //Insane hack to get rid of NFC flags in string.
    @NonNull
    private String getPokemonIdFromNFC(MifareUltralight ultralight) throws IOException {
        byte[] payload = ultralight.readPages(6);
        StringBuilder builder = new StringBuilder();
        String output = new String(payload, Charset.forName("US-ASCII"));
        builder.append(output);
        builder.deleteCharAt(0);
        payload = ultralight.readPages(10);
        byte[] bytes = new byte[16];
        for(int i = 0; i < payload.length; i++)
        {
            if(payload[i] == -2) break;
                bytes[i] = payload[i];
        }

        builder.append(new String(bytes, Charset.forName("US-ASCII")));
        return builder.toString();
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
                                return "already created";
                            case 201:
                                //Reads the input
                                //TODO: append to JSON Object
                                //TODO: Create method for inputstream
                                inputStream = connection.getInputStream();
                                scanner = new Scanner(inputStream);

                                StringBuilder stringBuilder = new StringBuilder();
                                while (scanner.hasNextLine()) {
                                    stringBuilder.append(scanner.nextLine());
                                }

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
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }

            @Override
            protected void onPostExecute(final String response) {
                super.onPostExecute(response);
                responseTextView.setText(response);
                displayHttpResponse(response);
            }
        }.execute();
    }

}