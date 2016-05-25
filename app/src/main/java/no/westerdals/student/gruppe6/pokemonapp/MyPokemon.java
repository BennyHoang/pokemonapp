package no.westerdals.student.gruppe6.pokemonapp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by OleFredrik on 23.05.2016.
 */
public class MyPokemon {

    private String pokemonID;
    private String nfcID;
    private String name;
    private String imageURL;

    @Override
    public String toString() {
        return "MyPokemon{" +
                "pokemonID=" + pokemonID +
                ", nfcID='" + nfcID + '\'' +
                ", name='" + name + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }

    public boolean comparePokemon(Pokemon pokemon){
        return pokemon.getName().equals(this.getName());
    }
/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyPokemon)) return false;

        MyPokemon myPokemon = (MyPokemon) o;

        return getPokemonID() == myPokemon.getPokemonID() && getNfcID().equals(myPokemon.getNfcID());

    }
*/

    public MyPokemon(String pokemonID, String nfcID, String name, String imageURL) {
        setPokemonID(pokemonID);
        setNfcID(nfcID);
        setName(name);
        setImageURL(imageURL);
    }

    public MyPokemon(String pokemonID, String nfcID, String name) {
        setPokemonID(pokemonID);
        setNfcID(nfcID);
        setName(name);
    }

    public MyPokemon(JSONObject jsonObject) throws JSONException {
        setPokemonID(jsonObject.getString("_id"));
        setNfcID(jsonObject.getString("id"));
        setName(jsonObject.getString("name"));
        setImageURL(jsonObject.getString("imageUrl"));
    }

    public String getPokemonID() {
        return pokemonID;
    }

    public void setPokemonID(String pokemonID) {
        this.pokemonID = pokemonID;
    }

    public String getNfcID() {
        return nfcID;
    }

    public void setNfcID(String nfcID) {
        this.nfcID = nfcID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
