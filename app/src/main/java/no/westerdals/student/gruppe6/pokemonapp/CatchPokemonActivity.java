package no.westerdals.student.gruppe6.pokemonapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    */
    String apiUrl = "https://locations.lehmann.tech/pokemon/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch_pokemon);

        btnSubmitId = (Button) findViewById(R.id.btnSubmitId);
        responseTextView = (TextView) findViewById(R.id.responseTextView);
        editText = (EditText) findViewById(R.id.editText);
        final Context context;
        btnSubmitId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAndDisplayData(editText);
            }

        });


    }

    void displayHttpResponse(CharSequence text){
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

                        switch(statusCode){
                            case 200:
                                return "already created";
                            case 201:
                                return "CATCHED NEW POKEMON! :)";
                        }


                        //Reads the input
                        // TODO: append to JSON Object
                        InputStream inputStream = connection.getInputStream();
                        Scanner scanner = new Scanner(inputStream);

                        StringBuilder stringBuilder = new StringBuilder();
                        while (scanner.hasNextLine()) {
                            stringBuilder.append(scanner.nextLine());
                        }

                        return "STATUS: 2xx";
                    } catch (FileNotFoundException e) {


                        switch(statusCode){
                            case 401:
                                return "Unauthorized, check your Token plz";
                            case 404:
                                return "Please sumbit ID";
                            case 420:
                                return "Wrong ID";
                        }
                        return "other status msg: " + statusCode + " = " + connection.getResponseCode();
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