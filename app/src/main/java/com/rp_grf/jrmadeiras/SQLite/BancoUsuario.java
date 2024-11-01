package com.rp_grf.jrmadeiras.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rp_grf.jrmadeiras.Tabelas.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Autor: André Castro
 * Data de atualização: 17/07/2020
 */


public class BancoUsuario extends SQLiteOpenHelper {

    private static final String DB_NAME = "banco_usuario";

    private static final int DB_VERSION = 1;

    private static final String TABLE = "Usuario";

    private static final String COD_USU = "cod_usu";
    private static final String SEN_USU = "sen_usu";
    private static final String NOM_USU = "nom_usu";


    private SQLiteDatabase banco;

    public BancoUsuario(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createSQL =
                "CREATE TABLE " + TABLE + "(" +
                        COD_USU + " VARCHAR," +
                        SEN_USU + " VARCHAR," +
                        NOM_USU + " VARCHAR" +
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
    public void setUsuarios(Usuario tab_usuario){
        ContentValues values = new ContentValues();
        values.put(COD_USU, tab_usuario.getCod_usu());
        values.put(SEN_USU, tab_usuario.getSen_usu());
        values.put(NOM_USU, tab_usuario.getNom_usu());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.insert(TABLE, null, values);

    }

    //Retorna os dados da tabela
    public ArrayList getUsuarios() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        cursor.moveToFirst();

        ArrayList<Usuario> lista_usuario = new ArrayList<Usuario>();

        if (cursor.moveToFirst()){
            do {
                Usuario tab_usuario = new Usuario(); // Precisa ser dentro do loop

                tab_usuario.setCod_usu(cursor.getString(cursor.getColumnIndex(COD_USU)));
                tab_usuario.setSen_usu(cursor.getString(cursor.getColumnIndex(SEN_USU)));
                tab_usuario.setNom_usu(cursor.getString(cursor.getColumnIndex(NOM_USU)));

                lista_usuario.add(tab_usuario);

            } while (cursor.moveToNext());
        }

        return lista_usuario;
    }

}
