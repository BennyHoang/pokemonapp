package no.westerdals.student.gruppe6.pokemonapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MyPokemonListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pokemon_list);

        ArrayList<String> myPokemons = parsePokemon(new MyPokemonManager(getApplicationContext()).getMyPokemonList());


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, myPokemons);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);
    }

    private ArrayList<String> parsePokemon(ArrayList<MyPokemon> list){
        ArrayList<String> tempList = new ArrayList<>();
        for (MyPokemon p : list) {
            tempList.add(p.getName() + " " + p.getPokemonID());
        }
        return tempList;
    }

}
