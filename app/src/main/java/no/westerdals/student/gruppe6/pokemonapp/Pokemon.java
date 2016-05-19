package no.westerdals.student.gruppe6.pokemonapp;

/**
 * Created by OleFredrik on 19.05.2016.
 */
public class Pokemon {
    private String _id;
    private String name;
    private double lat;
    private double lng;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
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

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
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
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    public Pokemon(String _id, String name, double lat, double lng) {
        this._id = _id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

}
