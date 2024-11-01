package com.rp_grf.jrmadeiras.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rp_grf.jrmadeiras.Tabelas.Fotos;
import com.rp_grf.jrmadeiras.Tabelas.Romaneio_ent;

import java.util.ArrayList;

public class BancoFotos extends SQLiteOpenHelper  {

    private static final String DB_NAME = "banco_fotos";

    private static final int DB_VERSION = 1;

    private static final String TABLE_FOTOS = "Fotos";

    private static final String codigo_registro = "codigo_registro";
    private static final String nome_arquivo = "nome_arquivo";
    private static final String caminho_foto = "caminho_foto";

    public BancoFotos(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String criarFotos =
                "CREATE TABLE " + TABLE_FOTOS + "(" +
                        codigo_registro + " VARCHAR," +
                        nome_arquivo + " VARCHAR," +
                        caminho_foto + " VARCHAR" +
                        ")";

        sqLiteDatabase.execSQL(criarFotos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql_fotos = "DROP TABLE IF EXISTS " + TABLE_FOTOS;
        sqLiteDatabase.execSQL(sql_fotos);

        onCreate(sqLiteDatabase);
    }

    //Apaga a tabela e chama o onCreate novamente
    public void limparBanco() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String dropSQL_Fotos = "DROP TABLE IF EXISTS " + TABLE_FOTOS;
        sqLiteDatabase.execSQL(dropSQL_Fotos);

        onCreate(sqLiteDatabase);
    }

    //Grava a agenda e o caminho das fotos
    public void setFotos(String registro, String nome, String caminho){
        ContentValues values = new ContentValues();
        values.put("codigo_registro", registro);
        values.put("nome_arquivo", nome);
        values.put("caminho_foto", caminho);

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.insert(TABLE_FOTOS, null, values);
    }

    //Retorna a agenda e o caminho das fotos
    public ArrayList getFotos() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_FOTOS;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        cursor.moveToFirst();

        ArrayList<Fotos> lista_fotos = new ArrayList<Fotos>();

        if (cursor.moveToFirst()) {
            do {
                Fotos tab_fotos = new Fotos();

                tab_fotos.setCodigo_registro(cursor.getString(cursor.getColumnIndex("codigo_registro")));
                tab_fotos.setNome_arquivo(cursor.getString(cursor.getColumnIndex("nome_arquivo")));
                tab_fotos.setCaminho_foto(cursor.getString(cursor.getColumnIndex("caminho_foto")));

                lista_fotos.add(tab_fotos);

            } while (cursor.moveToNext());
        }

        return lista_fotos;
    }

    //Atualiza na tabela
    public void updateFotos(Fotos tab_fotos) {

        ContentValues values = new ContentValues();
        values.put("codigo_registro", tab_fotos.getCodigo_registro());
        values.put("nome_arquivo", tab_fotos.getNome_arquivo());
        values.put("caminho_foto", tab_fotos.getCaminho_foto());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.insert(TABLE_FOTOS, null, values);

        sqLiteDatabase.update(TABLE_FOTOS, values, "codigo_registro= " + tab_fotos.getCodigo_registro(), null);

    }

}
