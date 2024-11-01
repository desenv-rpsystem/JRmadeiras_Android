package com.rp_grf.jrmadeiras.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rp_grf.jrmadeiras.Tabelas.Deposito;
import com.rp_grf.jrmadeiras.Tabelas.Item;
import com.rp_grf.jrmadeiras.Tabelas.Item_cod_bar;
import com.rp_grf.jrmadeiras.Tabelas.Item_dep;
import com.rp_grf.jrmadeiras.Tabelas.Item_pdc;
import com.rp_grf.jrmadeiras.Tabelas.Usuario;

import java.util.ArrayList;

/**
 * Autor: Rafael Oliveira
 * Data de atualização: 25/11/2020
 */


public class BancoPrecoEstoque extends SQLiteOpenHelper {

    private static final String DB_NAME = "banco_preco_estoque";

    private static final int DB_VERSION = 1;

    private static final String TABLE_ITEM = "Item";

    private static final String ITEM_COD_ITE = "cod_ite";
    private static final String ITEM_NOM_ITE = "nom_ite";
    private static final String ITEM_REF_ITE = "ref_ite";
    private static final String ITEM_NOM_MAR = "nom_mar";
    private static final String ITEM_COD_UNI = "cod_uni";
    private static final String ITEM_VAL_UNI = "val_uni";
    private static final String ITEM_QTD_EST = "qtd_est";

    private static final String TABLE_ITEM_DEP = "Item_dep";

    private static final String ITEM_DEP_COD_ITE = "cod_ite";
    private static final String ITEM_DEP_COD_DEP = "cod_dep";
    private static final String ITEM_DEP_QTD_EST = "qtd_est";
    private static final String ITEM_DEP_NOM_DEP = "nom_dep";

    private static final String TABLE_DEPOSITO = "Deposito";

    private static final String DEPOSITO_COD_DEP = "cod_dep";
    private static final String DEPOSITO_NOM_DEP = "nom_dep";

    private static final String TABLE_ITEM_PDC = "Item_pdc";

    private static final String ITEM_PDC_COD_ITE = "cod_ite";
    private static final String ITEM_PDC_NUM_PDC = "num_pdc";
    private static final String ITEM_PDC_DAT_ENT = "dat_ent";
    private static final String ITEM_PDC_QTD_PEN = "qtd_pen";

    private static final String TABLE_ITEM_COD_BAR = "Item_cod_bar";

    private static final String ITEM_COD_BAR_COD_BAR_ITE = "cod_bar_ite";
    private static final String ITEM_COD_BAR_COD_ITE = "cod_ite";

    public BancoPrecoEstoque(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String criarItem =
                "CREATE TABLE " + TABLE_ITEM + "(" +
                        ITEM_COD_ITE + " INTEGER," +
                        ITEM_NOM_ITE + " VARCHAR," +
                        ITEM_REF_ITE + " VARCHAR," +
                        ITEM_NOM_MAR + " VARCHAR," +
                        ITEM_COD_UNI + " VARCHAR," +
                        ITEM_VAL_UNI + " VARCHAR," +
                        ITEM_QTD_EST + " VARCHAR"  +
                        ")";

        sqLiteDatabase.execSQL(criarItem);

        String criarItem_dep =
                "CREATE TABLE " + TABLE_ITEM_DEP + "(" +
                        ITEM_DEP_COD_ITE + " INTEGER," +
                        ITEM_DEP_COD_DEP + " INTEGER," +
                        ITEM_DEP_QTD_EST + " VARCHAR," +
                        ITEM_DEP_NOM_DEP + " VARCHAR"  +
                        ")";

        sqLiteDatabase.execSQL(criarItem_dep);

        String criarDeposito =
                "CREATE TABLE " + TABLE_DEPOSITO + "(" +
                        DEPOSITO_COD_DEP + " INTEGER," +
                        DEPOSITO_NOM_DEP + " VARCHAR"  +
                        ")";

        sqLiteDatabase.execSQL(criarDeposito);

        String criarItem_pdc =
                "CREATE TABLE " + TABLE_ITEM_PDC + "(" +
                        ITEM_PDC_COD_ITE + " INTEGER," +
                        ITEM_PDC_NUM_PDC + " VARCHAR," +
                        ITEM_PDC_DAT_ENT + " VARCHAR," +
                        ITEM_PDC_QTD_PEN + " VARCHAR"  +
                        ")";

        sqLiteDatabase.execSQL(criarItem_pdc);

        String criarItem_cod_bar =
                "CREATE TABLE " + TABLE_ITEM_COD_BAR + "(" +
                        ITEM_COD_BAR_COD_BAR_ITE + " VARCHAR," +
                        ITEM_COD_BAR_COD_ITE + " INTEGER" +
                        ")";

        sqLiteDatabase.execSQL(criarItem_cod_bar);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String dropItem = "DROP TABLE IF EXISTS " + TABLE_ITEM;
        sqLiteDatabase.execSQL(dropItem);

        String dropItem_dep = "DROP TABLE IF EXISTS " + TABLE_ITEM_DEP;
        sqLiteDatabase.execSQL(dropItem_dep);

        String dropDeposito = "DROP TABLE IF EXISTS " + TABLE_DEPOSITO;
        sqLiteDatabase.execSQL(dropDeposito);

        String dropItem_pdc = "DROP TABLE IF EXISTS " + TABLE_ITEM_PDC;
        sqLiteDatabase.execSQL(dropItem_pdc);

        String dropItem_cod_bar = "DROP TABLE IF EXISTS " + TABLE_ITEM_COD_BAR;
        sqLiteDatabase.execSQL(dropItem_cod_bar);

        onCreate(sqLiteDatabase);
    }

    //Apaga a tabela e chama o onCreate novamente
    public void limparBanco() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String dropItem = "DROP TABLE IF EXISTS " + TABLE_ITEM;
        sqLiteDatabase.execSQL(dropItem);

        String dropItem_dep = "DROP TABLE IF EXISTS " + TABLE_ITEM_DEP;
        sqLiteDatabase.execSQL(dropItem_dep);

        String dropDeposito = "DROP TABLE IF EXISTS " + TABLE_DEPOSITO;
        sqLiteDatabase.execSQL(dropDeposito);

        String dropItem_pdc = "DROP TABLE IF EXISTS " + TABLE_ITEM_PDC;
        sqLiteDatabase.execSQL(dropItem_pdc);

        String dropItem_cod_bar = "DROP TABLE IF EXISTS " + TABLE_ITEM_COD_BAR;
        sqLiteDatabase.execSQL(dropItem_cod_bar);

        onCreate(sqLiteDatabase);
    }


    //Grava na tabela Item
    public void setItem(Item tab_item){
        ContentValues values = new ContentValues();
        values.put(ITEM_COD_ITE, tab_item.getCod_ite());
        values.put(ITEM_NOM_ITE, tab_item.getNom_ite());
        values.put(ITEM_REF_ITE, tab_item.getRef_ite());
        values.put(ITEM_NOM_MAR, tab_item.getNom_mar());
        values.put(ITEM_COD_UNI, tab_item.getCod_uni());
        values.put(ITEM_VAL_UNI, tab_item.getVal_uni());
        values.put(ITEM_QTD_EST, tab_item.getQtd_est());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.insert(TABLE_ITEM, null, values);

    }

    //Retorna os dados da tabela Item
    public ArrayList getItem() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_ITEM;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        cursor.moveToFirst();

        ArrayList<Item> lista_item = new ArrayList<Item>();

        if (cursor.moveToFirst()){
            do {
                Item tab_item = new Item(); // Precisa ser dentro do loop

                tab_item.setCod_ite(cursor.getLong(cursor.getColumnIndex(ITEM_COD_ITE)));
                tab_item.setNom_ite(cursor.getString(cursor.getColumnIndex(ITEM_NOM_ITE)));
                tab_item.setRef_ite(cursor.getString(cursor.getColumnIndex(ITEM_REF_ITE)));
                tab_item.setNom_mar(cursor.getString(cursor.getColumnIndex(ITEM_NOM_MAR)));
                tab_item.setCod_uni(cursor.getString(cursor.getColumnIndex(ITEM_COD_UNI)));
                tab_item.setVal_uni(cursor.getString(cursor.getColumnIndex(ITEM_VAL_UNI)));
                tab_item.setQtd_est(cursor.getString(cursor.getColumnIndex(ITEM_QTD_EST)));

                lista_item.add(tab_item);

            } while (cursor.moveToNext());
        }

        return lista_item;
    }

    //Grava na tabela Item_dep
    public void setItem_dep(Item_dep tab_item_dep){
        ContentValues values = new ContentValues();

        values.put(ITEM_DEP_COD_ITE, tab_item_dep.getCod_ite());
        values.put(ITEM_DEP_COD_DEP, tab_item_dep.getCod_dep());
        values.put(ITEM_DEP_QTD_EST, tab_item_dep.getQtd_est());
        values.put(ITEM_DEP_NOM_DEP, tab_item_dep.getNom_dep());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.insert(TABLE_ITEM_DEP, null, values);

    }

    //Retorna os dados da tabela Item_dep
    public ArrayList getItem_dep() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_ITEM_DEP;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        cursor.moveToFirst();

        ArrayList<Item_dep> lista_item_dep = new ArrayList<Item_dep>();

        if (cursor.moveToFirst()){
            do {
                Item_dep tab_item_dep = new Item_dep(); // Precisa ser dentro do loop

                tab_item_dep.setCod_ite(cursor.getLong(cursor.getColumnIndex(ITEM_DEP_COD_ITE)));
                tab_item_dep.setCod_dep(cursor.getLong(cursor.getColumnIndex(ITEM_DEP_COD_DEP)));
                tab_item_dep.setQtd_est(cursor.getString(cursor.getColumnIndex(ITEM_DEP_QTD_EST)));
                tab_item_dep.setNom_dep(cursor.getString(cursor.getColumnIndex(ITEM_DEP_NOM_DEP)));


                lista_item_dep.add(tab_item_dep);

            } while (cursor.moveToNext());
        }

        return lista_item_dep;
    }

    //Grava na tabela Deposito
    public void setDeposito(Deposito tab_deposito){
        ContentValues values = new ContentValues();

        values.put(DEPOSITO_COD_DEP, tab_deposito.getCod_dep());
        values.put(DEPOSITO_NOM_DEP, tab_deposito.getNom_dep());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.insert(TABLE_DEPOSITO, null, values);

    }

    //Retorna os dados da tabela Deposito
    public ArrayList getDeposito() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_DEPOSITO;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        cursor.moveToFirst();

        ArrayList<Deposito> lista_deposito = new ArrayList<Deposito>();

        if (cursor.moveToFirst()){
            do {
                Deposito tab_deposito = new Deposito(); // Precisa ser dentro do loop

                tab_deposito.setCod_dep(cursor.getLong(cursor.getColumnIndex(DEPOSITO_COD_DEP)));
                tab_deposito.setNom_dep(cursor.getString(cursor.getColumnIndex(DEPOSITO_NOM_DEP)));


                lista_deposito.add(tab_deposito);

            } while (cursor.moveToNext());
        }

        return lista_deposito;
    }

    //Grava na tabela Item_pdc
    public void setItem_pdc(Item_pdc tab_item_pdc){
        ContentValues values = new ContentValues();

        values.put(ITEM_PDC_COD_ITE, tab_item_pdc.getCod_ite());
        values.put(ITEM_PDC_NUM_PDC, tab_item_pdc.getNum_pdc());
        values.put(ITEM_PDC_DAT_ENT, tab_item_pdc.getDat_ent());
        values.put(ITEM_PDC_QTD_PEN, tab_item_pdc.getQtd_pen());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.insert(TABLE_ITEM_PDC, null, values);

    }

    //Retorna os dados da tabela Item_pdc
    public ArrayList getItem_pdc() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_ITEM_PDC;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        cursor.moveToFirst();

        ArrayList<Item_pdc> lista_item_pdc = new ArrayList<Item_pdc>();

        if (cursor.moveToFirst()){
            do {
                Item_pdc tab_item_pdc = new Item_pdc(); // Precisa ser dentro do loop

                tab_item_pdc.setCod_ite(cursor.getLong(cursor.getColumnIndex(ITEM_PDC_COD_ITE)));
                tab_item_pdc.setNum_pdc(cursor.getString(cursor.getColumnIndex(ITEM_PDC_NUM_PDC)));
                tab_item_pdc.setDat_ent(cursor.getString(cursor.getColumnIndex(ITEM_PDC_DAT_ENT)));
                tab_item_pdc.setQtd_pen(cursor.getString(cursor.getColumnIndex(ITEM_PDC_QTD_PEN)));


                lista_item_pdc.add(tab_item_pdc);

            } while (cursor.moveToNext());
        }

        return lista_item_pdc;
    }

    //Grava na tabela Item_cod_bar
    public void setItem_cod_bar(Item_cod_bar tab_item_cod_bar){
        ContentValues values = new ContentValues();

        values.put(ITEM_COD_BAR_COD_BAR_ITE, tab_item_cod_bar.getCod_bar_ite());
        values.put(ITEM_COD_BAR_COD_ITE, tab_item_cod_bar.getCod_ite());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.insert(TABLE_ITEM_COD_BAR, null, values);

    }

    //Retorna os dados da tabela Item_cod_bar
    public ArrayList getItem_cod_bar() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_ITEM_COD_BAR;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        cursor.moveToFirst();

        ArrayList<Item_cod_bar> lista_item_cod_bar = new ArrayList<Item_cod_bar>();

        if (cursor.moveToFirst()){
            do {
                Item_cod_bar tab_item_cod_bar = new Item_cod_bar(); // Precisa ser dentro do loop

                tab_item_cod_bar.setCod_bar_ite(cursor.getString(cursor.getColumnIndex(ITEM_COD_BAR_COD_BAR_ITE)));
                tab_item_cod_bar.setCod_ite(cursor.getLong(cursor.getColumnIndex(ITEM_COD_BAR_COD_ITE)));


                lista_item_cod_bar.add(tab_item_cod_bar);

            } while (cursor.moveToNext());
        }

        return lista_item_cod_bar;
    }
}
