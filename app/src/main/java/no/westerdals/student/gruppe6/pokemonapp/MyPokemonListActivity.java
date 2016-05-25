package no.westerdals.student.gruppe6.pokemonapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

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

    private ArrayList<String> parsePokemon(ArrayList<MyPokemon> list) {
        ArrayList<String> tempList = new ArrayList<>();
        for (MyPokemon p : list) {
            tempList.add(p.getName() + " " + p.getPokemonID());
        }
        return tempList;
    }

    private PopupWindow pwindow;

    private void showPopup() {
        LayoutInflater inflater = (LayoutInflater) MyPokemonListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.screen_popup, (ViewGroup) findViewById(R.id.popup_element));
        pwindow = new PopupWindow(layout, 300, 370, true);
        pwindow.showAtLocation(layout, Gravity.CENTER, 0, 0);


    }

}
