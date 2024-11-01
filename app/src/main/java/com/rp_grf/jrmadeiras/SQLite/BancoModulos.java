package com.rp_grf.jrmadeiras.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.rp_grf.jrmadeiras.Tabelas.Modulos;

import java.util.ArrayList;

/**
 * Autor: André Castro
 * Data de atualização: 17/07/2020
 */

public class BancoModulos extends SQLiteOpenHelper {

    private static final String DB_NAME = "banco_modulos";

    private static final int DB_VERSION = 1;

    private static final String TABLE = "Modulos";

    private static final String COD_MOD = "cod_mod";
    private static final String NOM_MOD = "nom_mod";
    private static final String NUM_SEQ = "num_seq";
    private static final String PER_MOD = "per_mod";

    public BancoModulos(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createSQL =
                "CREATE TABLE " + TABLE + "(" +
                        COD_MOD + " VARCHAR," +
                        NOM_MOD + " VARCHAR," +
                        NUM_SEQ + " VARCHAR," +
                        PER_MOD + " VARCHAR" +
                        ")";

        sqLiteDatabase.execSQL(createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE;
        sqLiteDatabase.execSQL(sql);

        onCreate(sqLiteDatabase);
    }

    //Apaga a tabela e chama o onCreate novamente
    public void limparBanco() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String dropSQL = "DROP TABLE IF EXISTS " + TABLE;
        sqLiteDatabase.execSQL(dropSQL);

        onCreate(sqLiteDatabase);
    }

    //Grava na tabela
    public void setModulos(Modulos tab_modulos){
        ContentValues values = new ContentValues();
        values.put(COD_MOD, tab_modulos.getCod_mod());
        values.put(NOM_MOD, tab_modulos.getNom_mod());
        values.put(NUM_SEQ, tab_modulos.getNum_seq());
        values.put(PER_MOD, tab_modulos.getPer_mod());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.insert(TABLE, null, values);
    }

    //Retorna os dados da tabela
    public ArrayList getModulos() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        cursor.moveToFirst();

        ArrayList<Modulos> lista_modulos = new ArrayList<Modulos>();

        if (cursor.moveToFirst()){
            do {
                Modulos tab_modulos = new Modulos();

                tab_modulos.setCod_mod(cursor.getString(cursor.getColumnIndex(COD_MOD)));
                tab_modulos.setNom_mod(cursor.getString(cursor.getColumnIndex(NOM_MOD)));
                tab_modulos.setNum_seq(cursor.getString(cursor.getColumnIndex(NUM_SEQ)));
                tab_modulos.setPer_mod(cursor.getString(cursor.getColumnIndex(PER_MOD)));

                lista_modulos.add(tab_modulos);

            } while (cursor.moveToNext());
        }

        return lista_modulos;
    }
}
