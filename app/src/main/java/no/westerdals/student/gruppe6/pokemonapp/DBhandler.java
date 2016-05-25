package no.westerdals.student.gruppe6.pokemonapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OleFredrik on 23.05.2016.
 */
public class DBhandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PokemonDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_POKEMON = "CAUGHT_POKEMON";
    private static final String KEY_ID = "ID";
    private static final String KEY_NFC_ID = "NFC_ID";
    private static final String KEY_POKEMON_NAME = "NAME";
    private static final String KEY_IMAGE_URL = "IMAGE_URL";


    public DBhandler(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        String CREATE_POKEMON_TABLE = "CREATE TABLE " + TABLE_POKEMON + "("
                + KEY_ID + " TEXT PRIMARY KEY," + KEY_NFC_ID + " TEXT,"
                + KEY_POKEMON_NAME + " TEXT, " + KEY_IMAGE_URL + ")";
        db.execSQL(CREATE_POKEMON_TABLE);
    }

    public ArrayList<MyPokemon> getAllMyPokemons() {
        ArrayList<MyPokemon> myPokemonList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_POKEMON + " ORDER BY NAME;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) do {
            MyPokemon myPokemon = new MyPokemon(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
            myPokemonList.add(myPokemon);
        } while (cursor.moveToNext());

        db.close();
        // return player list
        return myPokemonList;
    }

    public MyPokemon getPokemon(String key) {
        String selectQuery = "SELECT  * FROM " + TABLE_POKEMON + " WHERE " + KEY_ID + "='" + key +"';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        cursor.moveToFirst();
        MyPokemon myPokemon = new MyPokemon(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
        cursor.moveToNext();

        db.close();
        return myPokemon;
    }

    public void addPokemon(MyPokemon myPokemon) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, myPokemon.getPokemonID());
        values.put(KEY_NFC_ID, myPokemon.getNfcID());
        values.put(KEY_POKEMON_NAME, myPokemon.getName());
        values.put(KEY_IMAGE_URL, myPokemon.getImageURL());


        // Inserting Row
        db.insert(TABLE_POKEMON, null, values);
        db.close(); // Closing database connection
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POKEMON);
        // Create tables again
        onCreate(db);
    }
}