package no.westerdals.student.gruppe6.pokemonapp;

/**
 * Created by OleFredrik on 23.05.2016.
 */
public class MyPokemon {

    private int pokemonID;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyPokemon)) return false;

        MyPokemon myPokemon = (MyPokemon) o;

        return getPokemonID() == myPokemon.getPokemonID() && getNfcID().equals(myPokemon.getNfcID());

    }

    @Override
    public int hashCode() {
        int result = getPokemonID();
        result = 31 * result + getNfcID().hashCode();
        return result;
    }

    public MyPokemon(int pokemonID, String nfcID, String name, String imageURL) {
        setPokemonID(pokemonID);
        setNfcID(nfcID);
        setName(name);
        setImageURL(imageURL);
    }

    public MyPokemon(int pokemonID, String nfcID, String name) {
        setPokemonID(pokemonID);
        setNfcID(nfcID);
        setName(name);
    }

    public int getPokemonID() {
        return pokemonID;
    }

    public void setPokemonID(int pokemonID) {
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
