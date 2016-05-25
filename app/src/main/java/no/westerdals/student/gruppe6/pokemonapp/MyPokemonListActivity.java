package no.westerdals.student.gruppe6.pokemonapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
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
                        intent.putExtra("Pokemon", myPokemons);
                        startActivity(intent);
                    }
                }

                /*
                String selectedFromList = (String) (lv.getItemAtPosition(position));
                final ArrayList<MyPokemon> myPokemons = new MyPokemonManager(getApplicationContext()).getMyPokemonList();
                for(MyPokemon p : myPokemons){
                    if(selectedFromList.equals(p.getName())){
                        showPopup(p.getName(), p.getImageURL());
                    }
                }
                */
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

    private PopupWindow pwindow;

    private void showPopup(String name, String image) {
        LayoutInflater inflater = (LayoutInflater) MyPokemonListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.screen_popup, (ViewGroup) findViewById(R.id.popup_element));
        pwindow = new PopupWindow(layout, 700, 370, true);
        pwindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
        TextView pokemon_name = (TextView) layout.findViewById(R.id.pokemon_name);
        ImageView pokemon_image = (ImageView) layout.findViewById(R.id.pokemon_image);
        pokemon_name.setText(name);
        pokemon_image.setImageURI(Uri.parse(image));

    }


}
