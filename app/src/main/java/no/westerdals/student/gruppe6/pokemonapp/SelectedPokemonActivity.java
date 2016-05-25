package no.westerdals.student.gruppe6.pokemonapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;

public class SelectedPokemonActivity extends AppCompatActivity {
    private TextView pokemon_name;
    private MyPokemon myPokemon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_pokemon);
        Intent i = getIntent();
        String id = i.getExtras().getString("Pokemon");
        myPokemon = new MyPokemonManager(getApplicationContext()).getPokemonById(id);
        pokemon_name = (TextView) findViewById(R.id.pokemon_name);
        pokemon_name.setText(myPokemon.getName());
        new DownloadImageTask((ImageView) findViewById(R.id.pokemon_image)).execute(myPokemon.getImageURL());
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
