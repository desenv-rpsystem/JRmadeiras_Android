package com.rp_grf.jrmadeiras.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rp_grf.jrmadeiras.Tabelas.Agenda;
import com.rp_grf.jrmadeiras.Tabelas.Fotos;

import java.util.ArrayList;

/**
 * Banco OFFline que salva a tabela que será enviada para a nuvem quando o usuário fizer login com internet
 * Autor: André Castro
 */

public class BancoAgenda extends SQLiteOpenHelper {

    public static final String DB_NAME = "banco_agenda";

    public static final String TABLE = "Agenda";

    public static final int DB_VERSION = 1;

    private static final String TABLE_FOTOS = "Fotos";

    private static final String codigo_agenda = "codigo_agenda";
    private static final String nome_arquivo = "nome_arquivo";
    private static final String caminho_foto = "caminho_foto";

    public BancoAgenda(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createSQL =
                "CREATE TABLE " + TABLE + "(" +
                        "cod_age INTEGER," +
                        "cod_pre_voo INTEGER," +
                        "cod_cli VARCHAR," +
                        "cod_cli_ins VARCHAR," +
                        "cod_bem VARCHAR," +
                        "cod_tip_voo VARCHAR," +
                        "cod_aer_ori VARCHAR," +
                        "cod_aer_des VARCHAR," +

                        "dat_reg_voo VARCHAR," +
                        "hor_par VARCHAR," +
                        "min_par VARCHAR," +
                        "hor_dec VARCHAR," +
                        "min_dec VARCHAR," +
                        "hor_pou VARCHAR," +
                        "min_pou VARCHAR," +
                        "hor_cor VARCHAR," +
                        "min_cor VARCHAR," +
                        "hor_voo VARCHAR," +
                        "min_voo VARCHAR," +

                        "hor_ini_fli VARCHAR," +
                        "hor_fin_fli VARCHAR," +
                        "tmp_voo_fli VARCHAR," +
                        "num_pou VARCHAR," +

                        "lit_com VARCHAR," +
                        "gal_com VARCHAR," +

                        "hor_ini_hob VARCHAR," +
                        "hor_fin_hob VARCHAR," +
                        "tmp_voo_hob VARCHAR," +

                        "hor_diu VARCHAR," +
                        "min_diu VARCHAR," +
                        "hor_not VARCHAR," +
                        "min_not VARCHAR," +

                        "hor_ifrr VARCHAR," +
                        "min_ifrr VARCHAR," +
                        "hor_ifrc VARCHAR," +
                        "min_ifrc VARCHAR," +
                        "hor_vfr VARCHAR," +
                        "min_vfr VARCHAR," +

                        "inf_dia_bor VARCHAR," +
                        "sob_voo_mn VARCHAR," +
                        "flg_nav VARCHAR," +

                        "mod_dry_wet VARCHAR," +

                        "obs_reg_voo VARCHAR," +

                        "dat_lib_age VARCHAR," +
                        "hor_lib_age VARCHAR," +
                        "cod_esp_doc VARCHAR," +

                        "tmp_nao_voo VARCHAR" +
                        ")";

        sqLiteDatabase.execSQL(createSQL);

        String criarFotos =
                "CREATE TABLE " + TABLE_FOTOS + "(" +
                        codigo_agenda + " VARCHAR," +
                        nome_arquivo + " VARCHAR," +
                        caminho_foto + " VARCHAR" +
                        ")";

        sqLiteDatabase.execSQL(criarFotos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE;
        sqLiteDatabase.execSQL(sql);

        String sql_fotos = "DROP TABLE IF EXISTS " + TABLE_FOTOS;
        sqLiteDatabase.execSQL(sql_fotos);

        onCreate(sqLiteDatabase);
    }

    //Apaga a tabela e chama o onCreate novamente
    public void limparBanco() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String dropSQL = "DROP TABLE IF EXISTS " + TABLE;
        sqLiteDatabase.execSQL(dropSQL);

        String dropSQL_Fotos = "DROP TABLE IF EXISTS " + TABLE_FOTOS;
        sqLiteDatabase.execSQL(dropSQL_Fotos);

        onCreate(sqLiteDatabase);
    }

    //Grava na tabela
    public void setAgenda(Agenda tab_agenda) {
        ContentValues values = new ContentValues();
        values.put("cod_age", tab_agenda.getCod_age());
        values.put("cod_pre_voo", tab_agenda.getCod_pre_voo());
        values.put("cod_cli", tab_agenda.getCod_cli());
        values.put("cod_cli_ins", tab_agenda.getCod_cli_ins());
        values.put("cod_bem", tab_agenda.getCod_bem());
        values.put("cod_tip_voo", tab_agenda.getCod_tip_voo());
        values.put("cod_aer_ori", tab_agenda.getCod_aer_ori());
        values.put("cod_aer_des", tab_agenda.getCod_aer_des());

        values.put("dat_reg_voo", tab_agenda.getDat_reg_voo());
        values.put("hor_par", tab_agenda.getHor_par());
        values.put("min_par", tab_agenda.getMin_par());
        values.put("hor_dec", tab_agenda.getHor_dec());
        values.put("min_dec", tab_agenda.getMin_dec());
        values.put("hor_pou", tab_agenda.getHor_pou());
        values.put("min_pou", tab_agenda.getMin_pou());
        values.put("hor_cor", tab_agenda.getHor_cor());
        values.put("min_cor", tab_agenda.getMin_cor());
        values.put("hor_voo", tab_agenda.getHor_voo());
        values.put("min_voo", tab_agenda.getMin_voo());

        values.put("hor_ini_fli", tab_agenda.getHor_ini_fli());
        values.put("hor_fin_fli", tab_agenda.getHor_fin_fli());
        values.put("tmp_voo_fli", tab_agenda.getTmp_voo_fli());
        values.put("num_pou", tab_agenda.getNum_pou());

        values.put("lit_com", tab_agenda.getLit_com());
        values.put("gal_com", tab_agenda.getGal_com());

        values.put("hor_ini_hob", tab_agenda.getHor_ini_hob());
        values.put("hor_fin_hob", tab_agenda.getHor_fin_hob());
        values.put("tmp_voo_hob", tab_agenda.getTmp_voo_hob());

        values.put("hor_diu", tab_agenda.getHor_diu());
        values.put("min_diu", tab_agenda.getMin_diu());
        values.put("hor_not", tab_agenda.getHor_not());
        values.put("min_not", tab_agenda.getMin_not());

        values.put("hor_ifrr", tab_agenda.getHor_ifrr());
        values.put("min_ifrr", tab_agenda.getMin_ifrr());
        values.put("hor_ifrc", tab_agenda.getHor_ifrc());
        values.put("min_ifrc", tab_agenda.getMin_ifrc());
        values.put("hor_vfr", tab_agenda.getHor_vfr());
        values.put("min_vfr", tab_agenda.getMin_vfr());

        values.put("inf_dia_bor", tab_agenda.getInf_dia_bor());
        values.put("sob_voo_mn", tab_agenda.getSob_voo_mn());
        values.put("flg_nav", tab_agenda.getFlg_nav());

        values.put("mod_dry_wet", tab_agenda.getMod_dry_wet());

        values.put("obs_reg_voo", tab_agenda.getObs_reg_voo());

        values.put("dat_lib_age", tab_agenda.getDat_lib_age());
        values.put("hor_lib_age", tab_agenda.getHor_lib_age());
        values.put("cod_esp_doc", tab_agenda.getCod_esp_doc());

        values.put("tmp_nao_voo", tab_agenda.getTmp_nao_voo());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.insert(TABLE, null, values);
    }

    //Grava a agenda e o caminho das fotos
    public void setFotos(String agenda, String nome, String caminho){
        ContentValues values = new ContentValues();
        values.put("codigo_agenda", agenda);
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

                tab_fotos.setCodigo_registro(cursor.getString(cursor.getColumnIndex("codigo_agenda")));
                tab_fotos.setNome_arquivo(cursor.getString(cursor.getColumnIndex("nome_arquivo")));
                tab_fotos.setCaminho_foto(cursor.getString(cursor.getColumnIndex("caminho_foto")));

                lista_fotos.add(tab_fotos);

            } while (cursor.moveToNext());
        }

        return lista_fotos;
    }

    //Retorna os dados da tabela
    public ArrayList getAgenda() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        cursor.moveToFirst();

        ArrayList<Agenda> lista_agenda = new ArrayList<Agenda>();

        if (cursor.moveToFirst()) {
            do {
                Agenda tab_agenda = new Agenda();

                tab_agenda.setCod_age(cursor.getLong(cursor.getColumnIndex("cod_age")));
                tab_agenda.setCod_pre_voo(cursor.getLong(cursor.getColumnIndex("cod_pre_voo")));
                tab_agenda.setCod_cli(cursor.getLong(cursor.getColumnIndex("cod_cli")));
                tab_agenda.setCod_cli_ins(cursor.getString(cursor.getColumnIndex("cod_cli_ins")));
                tab_agenda.setCod_bem(cursor.getLong(cursor.getColumnIndex("cod_bem")));
                tab_agenda.setCod_tip_voo(cursor.getLong(cursor.getColumnIndex("cod_tip_voo")));
                tab_agenda.setCod_aer_ori(cursor.getLong(cursor.getColumnIndex("cod_aer_ori")));
                tab_agenda.setCod_aer_des(cursor.getLong(cursor.getColumnIndex("cod_aer_des")));

                tab_agenda.setDat_reg_voo(cursor.getString(cursor.getColumnIndex("dat_reg_voo")));
                tab_agenda.setHor_par(cursor.getString(cursor.getColumnIndex("hor_par")));
                tab_agenda.setMin_par(cursor.getString(cursor.getColumnIndex("min_par")));
                tab_agenda.setHor_dec(cursor.getString(cursor.getColumnIndex("hor_dec")));
                tab_agenda.setMin_dec(cursor.getString(cursor.getColumnIndex("min_dec")));
                tab_agenda.setHor_pou(cursor.getString(cursor.getColumnIndex("hor_pou")));
                tab_agenda.setMin_pou(cursor.getString(cursor.getColumnIndex("min_pou")));
                tab_agenda.setHor_cor(cursor.getString(cursor.getColumnIndex("hor_cor")));
                tab_agenda.setMin_cor(cursor.getString(cursor.getColumnIndex("min_cor")));
                tab_agenda.setHor_voo(cursor.getString(cursor.getColumnIndex("hor_voo")));
                tab_agenda.setMin_voo(cursor.getString(cursor.getColumnIndex("min_voo")));

                tab_agenda.setHor_ini_fli(cursor.getString(cursor.getColumnIndex("hor_ini_fli")));
                tab_agenda.setHor_fin_fli(cursor.getString(cursor.getColumnIndex("hor_fin_fli")));
                tab_agenda.setTmp_voo_fli(cursor.getString(cursor.getColumnIndex("tmp_voo_fli")));
                tab_agenda.setNum_pou(cursor.getString(cursor.getColumnIndex("num_pou")));

                tab_agenda.setLit_com(cursor.getString(cursor.getColumnIndex("lit_com")));
                tab_agenda.setGal_com(cursor.getString(cursor.getColumnIndex("gal_com")));

                tab_agenda.setHor_ini_hob(cursor.getString(cursor.getColumnIndex("hor_ini_hob")));
                tab_agenda.setHor_fin_hob(cursor.getString(cursor.getColumnIndex("hor_fin_hob")));
                tab_agenda.setTmp_voo_hob(cursor.getString(cursor.getColumnIndex("tmp_voo_hob")));

                tab_agenda.setHor_diu(cursor.getString(cursor.getColumnIndex("hor_diu")));
                tab_agenda.setMin_diu(cursor.getString(cursor.getColumnIndex("min_diu")));
                tab_agenda.setHor_not(cursor.getString(cursor.getColumnIndex("hor_not")));
                tab_agenda.setMin_not(cursor.getString(cursor.getColumnIndex("min_not")));

                tab_agenda.setHor_ifrr(cursor.getString(cursor.getColumnIndex("hor_ifrr")));
                tab_agenda.setMin_ifrr(cursor.getString(cursor.getColumnIndex("min_ifrr")));
                tab_agenda.setHor_ifrc(cursor.getString(cursor.getColumnIndex("hor_ifrc")));
                tab_agenda.setMin_ifrc(cursor.getString(cursor.getColumnIndex("min_ifrc")));
                tab_agenda.setHor_vfr(cursor.getString(cursor.getColumnIndex("hor_vfr")));
                tab_agenda.setMin_vfr(cursor.getString(cursor.getColumnIndex("min_vfr")));

                tab_agenda.setInf_dia_bor(cursor.getString(cursor.getColumnIndex("inf_dia_bor")));
                tab_agenda.setSob_voo_mn(cursor.getString(cursor.getColumnIndex("sob_voo_mn")));
                tab_agenda.setFlg_nav(cursor.getString(cursor.getColumnIndex("flg_nav")));

                tab_agenda.setMod_dry_wet(cursor.getString(cursor.getColumnIndex("mod_dry_wet")));

                tab_agenda.setObs_reg_voo(cursor.getString(cursor.getColumnIndex("obs_reg_voo")));

                tab_agenda.setDat_lib_age(cursor.getString(cursor.getColumnIndex("dat_lib_age")));
                tab_agenda.setHor_lib_age(cursor.getString(cursor.getColumnIndex("hor_lib_age")));
                tab_agenda.setCod_esp_doc(cursor.getString(cursor.getColumnIndex("cod_esp_doc")));

                tab_agenda.setTmp_nao_voo(cursor.getString(cursor.getColumnIndex("tmp_nao_voo")));

                lista_agenda.add(tab_agenda);

            } while (cursor.moveToNext());
        }

        return lista_agenda;
    }
}
