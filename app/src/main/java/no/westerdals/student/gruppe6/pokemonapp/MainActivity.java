package no.westerdals.student.gruppe6.pokemonapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnYourPokemons;
    private Button btnCatchPokemons;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        btnCatchPokemons = (Button) findViewById(R.id.btnCatchPokemon);
        btnYourPokemons = (Button) findViewById(R.id.btnYourPokemons);

        onClickBtnYourPokemons();
        onClickBtnCatchPokemons();
    }

    //TODO: Replace activity class with something interesting
    private void onClickBtnYourPokemons(){
        btnYourPokemons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyPokemonListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onClickBtnCatchPokemons(){
        btnCatchPokemons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PokeMapsActivity.class);
                startActivity(intent);
            }
        });
    }
}
