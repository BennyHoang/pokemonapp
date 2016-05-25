package no.westerdals.student.gruppe6.pokemonapp;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by OleFredrik on 23.05.2016.
 */
public class MyPokemonManager {

    private DBhandler dBhandler;

    public MyPokemonManager(Context context) {
        dBhandler = new DBhandler(context);
    }

    public void addPokemon(MyPokemon myPokemon){
        dBhandler.addPokemon(myPokemon);
    }

    public ArrayList<MyPokemon> getMyPokemonList (){
        return dBhandler.getAllMyPokemons();
    }

    public MyPokemon getPokemonById(String key){
        return dBhandler.getPokemon(key);
    }
}
