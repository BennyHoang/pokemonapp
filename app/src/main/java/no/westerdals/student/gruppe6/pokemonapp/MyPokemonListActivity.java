package no.westerdals.student.gruppe6.pokemonapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MyPokemonListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pokemon_list);

        final ArrayList<String> myPokemons = parsePokemon(new MyPokemonManager(getApplicationContext()).getMyPokemonList());


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, myPokemons);
        final ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromList = (String) (lv.getItemAtPosition(position));
                final ArrayList<MyPokemon> myPokemons = new MyPokemonManager(getApplicationContext()).getMyPokemonList();
                for(MyPokemon p : myPokemons){
                    if(selectedFromList.equals(p.getName())){
                        Intent intent = new Intent(MyPokemonListActivity.this, SelectedPokemonActivity.class);
                        intent.putExtra(getString(R.string.pokemon), p.getPokemonID());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private ArrayList<String> parsePokemon(ArrayList<MyPokemon> list) {
        ArrayList<String> tempList = new ArrayList<>();
        for (MyPokemon p : list) {
            tempList.add(p.getName());
        }
        return tempList;
    }


}
