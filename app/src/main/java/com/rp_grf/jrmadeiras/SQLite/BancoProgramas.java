package com.rp_grf.jrmadeiras.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rp_grf.jrmadeiras.Tabelas.Programas;

import java.util.ArrayList;

public class BancoProgramas extends SQLiteOpenHelper {

    private static final String DB_NAME = "banco_programas";

    private static final int DB_VERSION = 1;

    private static final String TABLE = "Programas";

    private static final String COD_MOD = "cod_mod";
    private static final String COD_PRG = "cod_prg";
    private static final String DES_PRG = "des_prg";
    private static final String NUM_SEQ = "num_seq";
    private static final String PER_PRG = "per_prg";

    public BancoProgramas(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createSQL =
                "CREATE TABLE " + TABLE + "(" +
                        COD_MOD + " VARCHAR," +
                        COD_PRG + " VARCHAR," +
                        DES_PRG + " VARCHAR," +
                        NUM_SEQ + " VARCHAR," +
                        PER_PRG + " VARCHAR" +
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
    public void setProgramas(Programas tab_programas){
        ContentValues values = new ContentValues();
        values.put(COD_MOD, tab_programas.getCod_mod());
        values.put(COD_PRG, tab_programas.getCod_prg());
        values.put(DES_PRG, tab_programas.getDes_prg());
        values.put(NUM_SEQ, tab_programas.getNum_seq());
        values.put(PER_PRG, tab_programas.getPer_prg());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.insert(TABLE, null, values);
    }

    //Retorna os dados da tabela
    public ArrayList getProgramas() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        cursor.moveToFirst();

        ArrayList<Programas> lista_programas = new ArrayList<Programas>();

        if (cursor.moveToFirst()){
            do {
                Programas tab_programas = new Programas();

                tab_programas.setCod_mod(cursor.getString(cursor.getColumnIndex(COD_MOD)));
                tab_programas.setCod_prg(cursor.getString(cursor.getColumnIndex(COD_PRG)));
                tab_programas.setDes_prg(cursor.getString(cursor.getColumnIndex(DES_PRG)));
                tab_programas.setNum_seq(cursor.getString(cursor.getColumnIndex(NUM_SEQ)));
                tab_programas.setPer_prg(cursor.getString(cursor.getColumnIndex(PER_PRG)));

                lista_programas.add(tab_programas);

            } while (cursor.moveToNext());
        }

        return lista_programas;
    }
}
