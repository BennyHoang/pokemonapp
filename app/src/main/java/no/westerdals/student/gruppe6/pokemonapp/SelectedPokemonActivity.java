package no.westerdals.student.gruppe6.pokemonapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SelectedPokemonActivity extends AppCompatActivity {
    private TextView pokemon_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_pokemon);
        Intent i = getIntent();
        String id = i.getExtras().getString("Pokemon");
        pokemon_id = (TextView) findViewById(R.id.pokemon_id);

        pokemon_id.setText(id);
    }
}
