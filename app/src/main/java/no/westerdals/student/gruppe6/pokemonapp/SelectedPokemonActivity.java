package no.westerdals.student.gruppe6.pokemonapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;

public class SelectedPokemonActivity extends AppCompatActivity {
    private TextView pokemon_name;
    private MyPokemon myPokemon;
    private Button btn_maps, btn_catch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_pokemon);
        Intent i = getIntent();
        String id = i.getExtras().getString(getString(R.string.Pokemon));
        myPokemon = new MyPokemonManager(getApplicationContext()).getPokemonById(id);
        pokemon_name = (TextView) findViewById(R.id.pokemon_name);
        pokemon_name.setText(myPokemon.getName());

        btn_maps = (Button) findViewById(R.id.btn_maps);
        btn_catch = (Button) findViewById(R.id.btn_catch);
        new DownloadImageTask((ImageView) findViewById(R.id.pokemon_image)).execute(myPokemon.getImageURL());

        onClickBtnCatchPokemons();
        onClickBtnPokemonMap();
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
                Log.e(getString(R.string.error), e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private void onClickBtnPokemonMap(){
        btn_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectedPokemonActivity.this, PokeMapsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onClickBtnCatchPokemons(){
        btn_catch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectedPokemonActivity.this, CatchPokemonActivity.class);
                startActivity(intent);
            }
        });
    }
}
