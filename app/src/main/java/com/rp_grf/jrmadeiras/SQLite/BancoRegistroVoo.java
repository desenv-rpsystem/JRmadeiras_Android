package com.rp_grf.jrmadeiras.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rp_grf.jrmadeiras.Tabelas.Aerodromo;
import com.rp_grf.jrmadeiras.Tabelas.Aeronave;
import com.rp_grf.jrmadeiras.Tabelas.Cliente;
import com.rp_grf.jrmadeiras.Tabelas.Pre_voo;
import com.rp_grf.jrmadeiras.Tabelas.Tipo_voo;

import java.util.ArrayList;

/**
 * Autor: André Castro
 * Data de atualização: 20/07/2020
 */

public class BancoRegistroVoo extends SQLiteOpenHelper {

    private static final String DB_NAME = "banco_registro_voo";

    private static final int DB_VERSION = 1;

    private static final String TABLE_PRE_VOO = "Pre_voo";

    private static final String PRE_VOO_COD_PRE_VOO = "cod_pre_voo";
    private static final String PRE_VOO_COD_AGE = "cod_age";
    private static final String PRE_VOO_COD_CLI = "cod_cli";
    private static final String PRE_VOO_NOM_CLI = "nom_cli";
    private static final String PRE_VOO_COD_BEM = "cod_bem";
    private static final String PRE_VOO_PRE_BEM = "pre_bem";
    private static final String PRE_VOO_COD_TIP_VOO = "cod_tip_voo";
    private static final String PRE_VOO_NOM_TIP_VOO = "nom_tip_voo";
    private static final String PRE_VOO_COD_AER_ORI = "cod_aer_ori";
    private static final String PRE_VOO_SIG_AER_ORI = "sig_aer_ori";
    private static final String PRE_VOO_COD_AER_DES = "cod_aer_des";
    private static final String PRE_VOO_SIG_AER_DES = "sig_aer_des";
    private static final String PRE_VOO_COD_CLI_INS = "cod_cli_ins";

    private static final String TABLE_CLIENTE = "Usuario";

    private static final String COD_CLI = "cod_cli";
    private static final String NOM_CLI = "nom_cli";
    private static final String NOM_FAN_CLI = "nom_fan_cli";
    private static final String COD_ALT = "cod_alt";
    private static final String DAT_EXA_MED = "dat_exa_med";

    private static final String TABLE_AERONAVE = "Aeronave";

    private static final String COD_BEM = "cod_bem";
    private static final String PRE_BEM = "pre_bem";
    private static final String NOM_BEM = "nom_bem";
    private static final String TIP_BEM = "tip_bem";

    private static final String TABLE_TIPO_VOO = "Tipo_voo";

    private static final String COD_TIP_VOO = "cod_tip_voo";
    private static final String NOM_TIP_VOO = "nom_tip_voo";

    private static final String TABLE_AERODROMO = "Aerodromo";

    private static final String COD_AER = "cod_aer";
    private static final String NOM_AER = "nom_aer";
    private static final String SIG_AER = "sig_aer";

    public BancoRegistroVoo(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String criarPre_voo =
                "CREATE TABLE " + TABLE_PRE_VOO + "(" +
                        PRE_VOO_COD_PRE_VOO + " VARCHAR," +
                        PRE_VOO_COD_AGE + " VARCHAR," +
                        PRE_VOO_COD_CLI + " VARCHAR," +
                        PRE_VOO_NOM_CLI + " VARCHAR," +
                        PRE_VOO_COD_BEM + " VARCHAR," +
                        PRE_VOO_PRE_BEM + " VARCHAR," +
                        PRE_VOO_COD_TIP_VOO + " VARCHAR," +
                        PRE_VOO_NOM_TIP_VOO + " VARCHAR," +
                        PRE_VOO_COD_AER_ORI + " VARCHAR," +
                        PRE_VOO_SIG_AER_ORI + " VARCHAR," +
                        PRE_VOO_COD_AER_DES + " VARCHAR," +
                        PRE_VOO_SIG_AER_DES + " VARCHAR," +
                        PRE_VOO_COD_CLI_INS + " VARCHAR" +
                        ")";

        sqLiteDatabase.execSQL(criarPre_voo);

        String criarCliente =
                "CREATE TABLE " + TABLE_CLIENTE + "(" +
                        COD_CLI + " VARCHAR," +
                        NOM_CLI + " VARCHAR," +
                        NOM_FAN_CLI + " VARCHAR," +
                        COD_ALT + " VARCHAR," +
                        DAT_EXA_MED + " VARCHAR" +
                        ")";

        sqLiteDatabase.execSQL(criarCliente);

        String criarAeronave =
                "CREATE TABLE " + TABLE_AERONAVE + "(" +
                        COD_BEM + " VARCHAR," +
                        PRE_BEM + " VARCHAR," +
                        NOM_BEM + " VARCHAR," +
                        TIP_BEM + " VARCHAR" +
                        ")";

        sqLiteDatabase.execSQL(criarAeronave);

        String criarTipo_voo =
                "CREATE TABLE " + TABLE_TIPO_VOO + "(" +
                        COD_TIP_VOO + " VARCHAR," +
                        NOM_TIP_VOO + " VARCHAR" +
                        ")";

        sqLiteDatabase.execSQL(criarTipo_voo);

        String criarAerodromo =
                "CREATE TABLE " + TABLE_AERODROMO + "(" +
                        COD_AER + " VARCHAR," +
                        NOM_AER + " VARCHAR," +
                        SIG_AER + " VARCHAR" +
                        ")";

        sqLiteDatabase.execSQL(criarAerodromo);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String dropPre_voo = "DROP TABLE IF EXISTS " + TABLE_PRE_VOO;
        sqLiteDatabase.execSQL(dropPre_voo);

        String dropCliente = "DROP TABLE IF EXISTS " + TABLE_CLIENTE;
        sqLiteDatabase.execSQL(dropCliente);

        String dropAeronave = "DROP TABLE IF EXISTS " + TABLE_AERONAVE;
        sqLiteDatabase.execSQL(dropAeronave);

        String dropTipo_voo = "DROP TABLE IF EXISTS " + TABLE_TIPO_VOO;
        sqLiteDatabase.execSQL(dropTipo_voo);

        String dropAerodromo = "DROP TABLE IF EXISTS " + TABLE_AERODROMO;
        sqLiteDatabase.execSQL(dropAerodromo);

        onCreate(sqLiteDatabase);
    }

    //Apaga a tabela e chama o onCreate novamente
    public void limparBanco() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String dropPre_voo = "DROP TABLE IF EXISTS " + TABLE_PRE_VOO;
        sqLiteDatabase.execSQL(dropPre_voo);

        String dropCliente = "DROP TABLE IF EXISTS " + TABLE_CLIENTE;
        sqLiteDatabase.execSQL(dropCliente);

        String dropAeronave = "DROP TABLE IF EXISTS " + TABLE_AERONAVE;
        sqLiteDatabase.execSQL(dropAeronave);

        String dropTipo_voo = "DROP TABLE IF EXISTS " + TABLE_TIPO_VOO;
        sqLiteDatabase.execSQL(dropTipo_voo);

        String dropAerodromo = "DROP TABLE IF EXISTS " + TABLE_AERODROMO;
        sqLiteDatabase.execSQL(dropAerodromo);

        onCreate(sqLiteDatabase);
    }

    //Grava na tabela Pre_voo
    public void setPre_voo(Pre_voo tab_pre_voo) {

        ContentValues values = new ContentValues();
        values.put(PRE_VOO_COD_PRE_VOO, tab_pre_voo.getCod_pre_voo());
        values.put(PRE_VOO_COD_AGE, tab_pre_voo.getCod_age());
        values.put(PRE_VOO_COD_CLI, tab_pre_voo.getCod_cli());
        values.put(PRE_VOO_NOM_CLI, tab_pre_voo.getNom_cli());
        values.put(PRE_VOO_COD_BEM, tab_pre_voo.getCod_bem());
        values.put(PRE_VOO_PRE_BEM, tab_pre_voo.getPre_bem());
        values.put(PRE_VOO_COD_TIP_VOO, tab_pre_voo.getCod_tip_voo());
        values.put(PRE_VOO_NOM_TIP_VOO, tab_pre_voo.getNom_tip_voo());
        values.put(PRE_VOO_COD_AER_ORI, tab_pre_voo.getCod_aer_ori());
        values.put(PRE_VOO_SIG_AER_ORI, tab_pre_voo.getSig_aer_ori());
        values.put(PRE_VOO_COD_AER_DES, tab_pre_voo.getCod_aer_des());
        values.put(PRE_VOO_SIG_AER_DES, tab_pre_voo.getSig_aer_des());
        values.put(PRE_VOO_COD_CLI_INS, tab_pre_voo.getCod_cli_ins());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.insert(TABLE_PRE_VOO, null, values);
    }

    //Retorna os dados da tabela Pre_voo
    public ArrayList getPre_voo() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_PRE_VOO;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        cursor.moveToFirst();

        ArrayList<Pre_voo> lista_pre_voo = new ArrayList<Pre_voo>();

        if (cursor.moveToFirst()) {
            do {
                Pre_voo tab_pre_voo = new Pre_voo();

                tab_pre_voo.setCod_pre_voo(cursor.getLong(cursor.getColumnIndex(PRE_VOO_COD_PRE_VOO)));
                tab_pre_voo.setCod_age(cursor.getLong(cursor.getColumnIndex(PRE_VOO_COD_AGE)));
                tab_pre_voo.setCod_cli(cursor.getLong(cursor.getColumnIndex(PRE_VOO_COD_CLI)));
                tab_pre_voo.setNom_cli(cursor.getString(cursor.getColumnIndex(PRE_VOO_NOM_CLI)));
                tab_pre_voo.setCod_bem(cursor.getLong(cursor.getColumnIndex(PRE_VOO_COD_BEM)));
                tab_pre_voo.setPre_bem(cursor.getString(cursor.getColumnIndex(PRE_VOO_PRE_BEM)));
                tab_pre_voo.setCod_tip_voo(cursor.getLong(cursor.getColumnIndex(PRE_VOO_COD_TIP_VOO)));
                tab_pre_voo.setNom_tip_voo(cursor.getString(cursor.getColumnIndex(PRE_VOO_NOM_TIP_VOO)));
                tab_pre_voo.setCod_aer_ori(cursor.getLong(cursor.getColumnIndex(PRE_VOO_COD_AER_ORI)));
                tab_pre_voo.setSig_aer_ori(cursor.getString(cursor.getColumnIndex(PRE_VOO_SIG_AER_ORI)));
                tab_pre_voo.setCod_aer_des(cursor.getLong(cursor.getColumnIndex(PRE_VOO_COD_AER_DES)));
                tab_pre_voo.setSig_aer_des(cursor.getString(cursor.getColumnIndex(PRE_VOO_SIG_AER_DES)));
                tab_pre_voo.setCod_cli_ins(cursor.getString(cursor.getColumnIndex(PRE_VOO_COD_CLI_INS)));

                lista_pre_voo.add(tab_pre_voo);

            } while (cursor.moveToNext());
        }

        return lista_pre_voo;
    }

    //Atualiza o registro na tabela de Pre_voo
    public void updatePre_voo(Pre_voo tab_pre_voo){
        ContentValues values = new ContentValues();
        //values.put(PRE_VOO_COD_PRE_VOO, tab_pre_voo.getCod_pre_voo());
        values.put(PRE_VOO_COD_AGE, tab_pre_voo.getCod_age());
        values.put(PRE_VOO_COD_CLI, tab_pre_voo.getCod_cli());
        values.put(PRE_VOO_NOM_CLI, tab_pre_voo.getNom_cli());
        values.put(PRE_VOO_COD_BEM, tab_pre_voo.getCod_bem());
        values.put(PRE_VOO_PRE_BEM, tab_pre_voo.getPre_bem());
        values.put(PRE_VOO_COD_TIP_VOO, tab_pre_voo.getCod_tip_voo());
        values.put(PRE_VOO_NOM_TIP_VOO, tab_pre_voo.getNom_tip_voo());
        values.put(PRE_VOO_COD_AER_ORI, tab_pre_voo.getCod_aer_ori());
        values.put(PRE_VOO_SIG_AER_ORI, tab_pre_voo.getSig_aer_ori());
        values.put(PRE_VOO_COD_AER_DES, tab_pre_voo.getCod_aer_des());
        values.put(PRE_VOO_SIG_AER_DES, tab_pre_voo.getSig_aer_des());
        values.put(PRE_VOO_COD_CLI_INS, tab_pre_voo.getCod_cli_ins());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.update(TABLE_PRE_VOO, values, "cod_pre_voo = " + tab_pre_voo.getCod_pre_voo().toString(), null);
    }

    //Grava na tabela Cliente
    public void setCliente(Cliente tab_cliente) {
        ContentValues values = new ContentValues();
        values.put(COD_CLI, tab_cliente.getCod_cli());
        values.put(NOM_CLI, tab_cliente.getNom_cli());
        values.put(NOM_FAN_CLI, tab_cliente.getNom_fan_cli());
        values.put(COD_ALT, tab_cliente.getCod_alt());
        values.put(DAT_EXA_MED, tab_cliente.getDat_exa_med());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.insert(TABLE_CLIENTE, null, values);
    }

    //Retorna os dados da tabela Cliente
    public ArrayList getCliente() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_CLIENTE;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        cursor.moveToFirst();

        ArrayList<Cliente> lista_cliente = new ArrayList<Cliente>();

        if (cursor.moveToFirst()) {
            do {
                Cliente tab_cliente = new Cliente();

                tab_cliente.setCod_cli(cursor.getLong(cursor.getColumnIndex(COD_CLI)));
                tab_cliente.setNom_cli(cursor.getString(cursor.getColumnIndex(NOM_CLI)));
                tab_cliente.setNom_fan_cli(cursor.getString(cursor.getColumnIndex(NOM_FAN_CLI)));
                tab_cliente.setCod_alt(cursor.getString(cursor.getColumnIndex(COD_ALT)));
                tab_cliente.setDat_exa_med(cursor.getString(cursor.getColumnIndex(DAT_EXA_MED)));

                lista_cliente.add(tab_cliente);

            } while (cursor.moveToNext());
        }

        return lista_cliente;
    }

    //Grava na tabela Aeronave
    public void setAeronave(Aeronave tab_aeronave) {
        ContentValues values = new ContentValues();
        values.put(COD_BEM, tab_aeronave.getCod_bem());
        values.put(PRE_BEM, tab_aeronave.getPre_bem());
        values.put(NOM_BEM, tab_aeronave.getNom_bem());
        values.put(TIP_BEM, tab_aeronave.getTip_bem());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.insert(TABLE_AERONAVE, null, values);
    }

    //Retorna os dados da tabela Aeronave
    public ArrayList getAeronave() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_AERONAVE;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        cursor.moveToFirst();

        ArrayList<Aeronave> lista_aeronave = new ArrayList<Aeronave>();

        if (cursor.moveToFirst()) {
            do {
                Aeronave tab_aeronave = new Aeronave();

                tab_aeronave.setCod_bem(cursor.getLong(cursor.getColumnIndex(COD_BEM)));
                tab_aeronave.setPre_bem(cursor.getString(cursor.getColumnIndex(PRE_BEM)));
                tab_aeronave.setNom_bem(cursor.getString(cursor.getColumnIndex(NOM_BEM)));
                tab_aeronave.setTip_bem(cursor.getString(cursor.getColumnIndex(TIP_BEM)));

                lista_aeronave.add(tab_aeronave);

            } while (cursor.moveToNext());
        }

        return lista_aeronave;
    }

    //Grava na tabela Tipo_voo
    public void setTipo_voo(Tipo_voo tab_tipo_voo) {
        ContentValues values = new ContentValues();
        values.put(COD_TIP_VOO, tab_tipo_voo.getCod_tip_voo());
        values.put(NOM_TIP_VOO, tab_tipo_voo.getNom_tip_voo());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.insert(TABLE_TIPO_VOO, null, values);
    }

    //Retorna os dados da tabela Tipo_voo
    public ArrayList getTipo_voo() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_TIPO_VOO;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        cursor.moveToFirst();

        ArrayList<Tipo_voo> lista_tipo_voo = new ArrayList<Tipo_voo>();

        if (cursor.moveToFirst()) {
            do {
                Tipo_voo tab_tipo_voo = new Tipo_voo();

                tab_tipo_voo.setCod_tip_voo(cursor.getLong(cursor.getColumnIndex(COD_TIP_VOO)));
                tab_tipo_voo.setNom_tip_voo(cursor.getString(cursor.getColumnIndex(NOM_TIP_VOO)));

                lista_tipo_voo.add(tab_tipo_voo);

            } while (cursor.moveToNext());
        }

        return lista_tipo_voo;
    }

    //Grava na tabela Aerodromo
    public void setAerodromo(Aerodromo tab_aerodromo) {
        ContentValues values = new ContentValues();
        values.put(COD_AER, tab_aerodromo.getCod_aer());
        values.put(NOM_AER, tab_aerodromo.getNom_aer());
        values.put(SIG_AER, tab_aerodromo.getSig_aer());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.insert(TABLE_AERODROMO, null, values);
    }

    //Retorna os dados da tabela Aerodromo
    public ArrayList getAerodromo() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_AERODROMO;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        cursor.moveToFirst();

        ArrayList<Aerodromo> lista_aerodromo = new ArrayList<Aerodromo>();

        if (cursor.moveToFirst()) {
            do {
                Aerodromo tab_aerodromo = new Aerodromo();

                tab_aerodromo.setCod_aer(cursor.getLong(cursor.getColumnIndex(COD_AER)));
                tab_aerodromo.setNom_aer(cursor.getString(cursor.getColumnIndex(NOM_AER)));
                tab_aerodromo.setSig_aer(cursor.getString(cursor.getColumnIndex(SIG_AER)));

                lista_aerodromo.add(tab_aerodromo);

            } while (cursor.moveToNext());
        }

        return lista_aerodromo;
    }
}
