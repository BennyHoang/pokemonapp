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
    * TODO: Create Satuts messages for Status on catched pokemons
    * 200 = Catched
    * 201 = NEW CATCH
    * 420 = No
    * */
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

    void displayHttpResponse( CharSequence text){
        Context context = getApplicationContext();

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
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

                    try {
                        InputStream inputStream = connection.getInputStream();
                        Scanner scanner = new Scanner(inputStream);

                        StringBuilder stringBuilder = new StringBuilder();
                        while (scanner.hasNextLine()) {
                            stringBuilder.append(scanner.nextLine());
                        }

                        return stringBuilder.toString();
                    } catch (FileNotFoundException e) {

                        final int statusCode = connection.getResponseCode();
                        switch(statusCode){
                            case 420:
                                return "Wrong ID";

                        }
                        return "GOT ERROR WITH CODE: " + connection.getResponseCode() + " with message: " + connection.getResponseMessage();
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