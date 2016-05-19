package no.westerdals.student.gruppe6.pokemonapp;

/**
 * Created by OleFredrik on 19.05.2016.
 */
public class Pokemon {
    private String id;
    private String name;
    private double lat;
    private double lang;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pokemon)) return false;

        Pokemon pokemon = (Pokemon) o;

        return getId().equals(pokemon.getId());

    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lang=" + lang +
                '}';
    }

    public Pokemon(String id, String name, double lat, double lang) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lang = lang;
    }

}
