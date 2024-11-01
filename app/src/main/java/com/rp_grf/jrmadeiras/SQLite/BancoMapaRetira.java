package com.rp_grf.jrmadeiras.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rp_grf.jrmadeiras.Tabelas.Item_romaneio_ent;
import com.rp_grf.jrmadeiras.Tabelas.Romaneio_ent;

import java.util.ArrayList;

public class BancoMapaRetira extends SQLiteOpenHelper {
    private static final String DB_NAME = "banco_mapa_retira";

    private static final int DB_VERSION = 1;

    private static final String TABLE_ROMANEIO_RET = "Romaneio_ret";

    private static final String ROMANEIO_NUM_ROM = "num_rom";
    private static final String ROMANEIO_NUM_SEQ = "num_seq";
    private static final String ROMANEIO_NUM_REG_NTS = "num_reg_nts";
    private static final String COD_SER = "cod_ser";
    private static final String NUM_DOC = "num_doc";
    private static final String NOM_CLI = "nom_cli";
    private static final String END_CLI = "end_cli";
    private static final String NUM_END_CLI = "num_end_cli";
    private static final String COM_BAI_CLI = "com_bai_cli";
    private static final String NOM_MUN = "nom_mun";
    private static final String COD_UNI_FED = "cod_uni_fed";
    private static final String CEP_CLI = "cep_cli";
    private static final String FON_CLI = "fon_cli";
    private static final String FON_CLI_2 = "fon_cli_2";
    private static final String NOM_VEN = "nom_ven";
    private static final String DAT_LIB_RE = "dat_lib_re";
    private static final String USU_LIB_RE = "usu_lib_re";
    private static final String HOR_LIB_RE = "hor_lib_re";

    //

    private static final String TABLE_ITEM_RET = "Item_romaneio_Ret";

    private static final String ITEM_NUM_ROM = "num_rom";
    private static final String ITEM_NUM_SEQ = "num_seq";
    private static final String COD_ITE = "cod_ite";
    private static final String ITEM_NUM_REG_NTS = "num_reg_nts";
    private static final String NOM_ITE = "nom_ite";
    private static final String QTD_ITE = "qtd_ite";
    private static final String QTD_PEN = "qtd_pen";
    private static final String COD_UNI = "cod_uni";
    private static final String COD_DEP = "cod_dep";
    private static final String NOM_DEP = "nom_dep";
    private static final String COD_IDE_LOC = "cod_ite_loc";

    public BancoMapaRetira(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String criarRomaneio =
                "CREATE TABLE " + TABLE_ROMANEIO_RET + "(" +
                        ROMANEIO_NUM_ROM + " VARCHAR," +
                        ROMANEIO_NUM_SEQ + " VARCHAR," +
                        ROMANEIO_NUM_REG_NTS + " VARCHAR," +
                        COD_SER + " VARCHAR," +
                        NUM_DOC + " VARCHAR," +
                        NOM_CLI + " VARCHAR," +
                        END_CLI + " VARCHAR," +
                        NUM_END_CLI + " VARCHAR," +
                        COM_BAI_CLI + " VARCHAR," +
                        NOM_MUN + " VARCHAR," +
                        COD_UNI_FED + " VARCHAR," +
                        CEP_CLI + " VARCHAR," +
                        FON_CLI + " VARCHAR," +
                        FON_CLI_2 + " VARCHAR," +
                        NOM_VEN + " VARCHAR," +
                        DAT_LIB_RE + " VARCHAR," +
                        USU_LIB_RE + " VARCHAR," +
                        HOR_LIB_RE + " VARCHAR" +
                        ")";

        sqLiteDatabase.execSQL(criarRomaneio);

        String criarItem =
                "CREATE TABLE " + TABLE_ITEM_RET + "(" +
                        ITEM_NUM_ROM + " VARCHAR," +
                        ITEM_NUM_SEQ + " VARCHAR," +
                        COD_ITE + " VARCHAR," +
                        ITEM_NUM_REG_NTS + " VARCHAR," +
                        NOM_ITE + " VARCHAR," +
                        QTD_ITE + " VARCHAR," +
                        QTD_PEN + " VARCHAR," +
                        COD_UNI + " VARCHAR," +
                        COD_DEP + " VARCHAR," +
                        NOM_DEP + " VARCHAR," +
                        COD_IDE_LOC + " VARCHAR" +
                        ")";

        sqLiteDatabase.execSQL(criarItem);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String dropRomaneio = "DROP TABLE IF EXISTS " + TABLE_ROMANEIO_RET;
        sqLiteDatabase.execSQL(dropRomaneio);

        String dropItem = "DROP TABLE IF EXISTS " + TABLE_ITEM_RET;
        sqLiteDatabase.execSQL(dropItem);
    }

    //Apaga a tabela e chama o onCreate novamente
    public void limparBanco() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String dropRomaneio = "DROP TABLE IF EXISTS " + TABLE_ROMANEIO_RET;
        sqLiteDatabase.execSQL(dropRomaneio);

        String dropItem = "DROP TABLE IF EXISTS " + TABLE_ITEM_RET;
        sqLiteDatabase.execSQL(dropItem);

        onCreate(sqLiteDatabase);
    }

    //Grava na tabela Romaneio_Ent
    public void setRomaneio_Ent(Romaneio_ent tab_romaneio_ent) {
        ContentValues values = new ContentValues();
        values.put(ROMANEIO_NUM_ROM, tab_romaneio_ent.getNum_rom());
        values.put(ROMANEIO_NUM_SEQ, tab_romaneio_ent.getNum_seq());
        values.put(ROMANEIO_NUM_REG_NTS, tab_romaneio_ent.getNum_reg_nts());
        values.put(COD_SER, tab_romaneio_ent.getCod_ser());
        values.put(NUM_DOC, tab_romaneio_ent.getNum_doc());
        values.put(NOM_CLI, tab_romaneio_ent.getNom_cli());
        values.put(END_CLI, tab_romaneio_ent.getEnd_cli());
        values.put(NUM_END_CLI, tab_romaneio_ent.getNum_end_cli());
        values.put(COM_BAI_CLI, tab_romaneio_ent.getCom_bai_cli());
        values.put(NOM_MUN, tab_romaneio_ent.getNom_mun());
        values.put(COD_UNI_FED, tab_romaneio_ent.getCod_uni_fed());
        values.put(CEP_CLI, tab_romaneio_ent.getCep_cli());
        values.put(FON_CLI, tab_romaneio_ent.getFon_cli());
        values.put(FON_CLI_2, tab_romaneio_ent.getFon_cli_2());
        values.put(NOM_VEN, tab_romaneio_ent.getNom_ven());
        values.put(DAT_LIB_RE, tab_romaneio_ent.getDat_lib_re());
        values.put(USU_LIB_RE, tab_romaneio_ent.getUsu_lib_re());
        values.put(HOR_LIB_RE, tab_romaneio_ent.getHor_lib_re());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.insert(TABLE_ROMANEIO_RET, null, values);
    }

    //Retorna os dados da tabela Romaneio_Ent
    public ArrayList getRomaneio_Ent() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_ROMANEIO_RET;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        cursor.moveToFirst();

        ArrayList<Romaneio_ent> lista_romaneio_ent = new ArrayList<Romaneio_ent>();

        if (cursor.moveToFirst()) {
            do {

                Romaneio_ent tab_romaneio_ent = new Romaneio_ent();

                tab_romaneio_ent.setNum_rom(cursor.getString(cursor.getColumnIndex(ROMANEIO_NUM_ROM)));
                tab_romaneio_ent.setNum_seq(cursor.getLong(cursor.getColumnIndex(ROMANEIO_NUM_SEQ)));
                tab_romaneio_ent.setNum_reg_nts(cursor.getLong(cursor.getColumnIndex(ROMANEIO_NUM_REG_NTS)));
                tab_romaneio_ent.setCod_ser(cursor.getString(cursor.getColumnIndex(COD_SER)));
                tab_romaneio_ent.setNum_doc(cursor.getString(cursor.getColumnIndex(NUM_DOC)));
                tab_romaneio_ent.setNom_cli(cursor.getString(cursor.getColumnIndex(NOM_CLI)));
                tab_romaneio_ent.setEnd_cli(cursor.getString(cursor.getColumnIndex(END_CLI)));
                tab_romaneio_ent.setNum_end_cli(cursor.getString(cursor.getColumnIndex(NUM_END_CLI)));
                tab_romaneio_ent.setCom_bai_cli(cursor.getString(cursor.getColumnIndex(COM_BAI_CLI)));
                tab_romaneio_ent.setNom_mun(cursor.getString(cursor.getColumnIndex(NOM_MUN)));
                tab_romaneio_ent.setCod_uni_fed(cursor.getString(cursor.getColumnIndex(COD_UNI_FED)));
                tab_romaneio_ent.setCep_cli(cursor.getString(cursor.getColumnIndex(CEP_CLI)));
                tab_romaneio_ent.setFon_cli(cursor.getString(cursor.getColumnIndex(FON_CLI)));
                tab_romaneio_ent.setFon_cli_2(cursor.getString(cursor.getColumnIndex(FON_CLI_2)));
                tab_romaneio_ent.setNom_ven(cursor.getString(cursor.getColumnIndex(NOM_VEN)));
                tab_romaneio_ent.setDat_lib_re(cursor.getString(cursor.getColumnIndex(DAT_LIB_RE)));
                tab_romaneio_ent.setUsu_lib_re(cursor.getString(cursor.getColumnIndex(USU_LIB_RE)));
                tab_romaneio_ent.setHor_lib_re(cursor.getString(cursor.getColumnIndex(HOR_LIB_RE)));

                lista_romaneio_ent.add(tab_romaneio_ent);

            } while (cursor.moveToNext());
        }

        return lista_romaneio_ent;
    }

    //Atualiza na tabela
    public void updateRomaneio_ent(Romaneio_ent tab_romaneio_ent) {

        ContentValues values = new ContentValues();
        values.put(ROMANEIO_NUM_ROM, tab_romaneio_ent.getNum_rom());
        values.put(ROMANEIO_NUM_SEQ, tab_romaneio_ent.getNum_seq());
        values.put(ROMANEIO_NUM_REG_NTS, tab_romaneio_ent.getNum_reg_nts());
        values.put(COD_SER, tab_romaneio_ent.getCod_ser());
        values.put(NUM_DOC, tab_romaneio_ent.getNum_doc());
        values.put(NOM_CLI, tab_romaneio_ent.getNom_cli());
        values.put(END_CLI, tab_romaneio_ent.getEnd_cli());
        values.put(NUM_END_CLI, tab_romaneio_ent.getNum_end_cli());
        values.put(COM_BAI_CLI, tab_romaneio_ent.getCom_bai_cli());
        values.put(NOM_MUN, tab_romaneio_ent.getNom_mun());
        values.put(COD_UNI_FED, tab_romaneio_ent.getCod_uni_fed());
        values.put(CEP_CLI, tab_romaneio_ent.getCep_cli());
        values.put(FON_CLI, tab_romaneio_ent.getFon_cli());
        values.put(FON_CLI_2, tab_romaneio_ent.getFon_cli_2());
        values.put(NOM_VEN, tab_romaneio_ent.getNom_ven());
        values.put(DAT_LIB_RE, tab_romaneio_ent.getDat_lib_re());
        values.put(USU_LIB_RE, tab_romaneio_ent.getUsu_lib_re());
        values.put(HOR_LIB_RE, tab_romaneio_ent.getHor_lib_re());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.update(TABLE_ROMANEIO_RET, values, "num_rom= " + tab_romaneio_ent.getNum_rom(), null);

    }

    //Grava na tabela Item_Romaneio_Ent
    public void setItem_Romaneio_Ent(Item_romaneio_ent tab_item_romaneioent) {
        ContentValues values = new ContentValues();
        values.put(ITEM_NUM_ROM, tab_item_romaneioent.getNum_rom());
        values.put(ITEM_NUM_SEQ, tab_item_romaneioent.getNum_seq());
        values.put(COD_ITE, tab_item_romaneioent.getCod_ite());
        values.put(ITEM_NUM_REG_NTS, tab_item_romaneioent.getNum_reg_nts());
        values.put(NOM_ITE, tab_item_romaneioent.getNom_ite());
        values.put(QTD_ITE, tab_item_romaneioent.getQtd_ite());
        values.put(QTD_PEN, tab_item_romaneioent.getQtd_pen());
        values.put(COD_UNI, tab_item_romaneioent.getCod_uni());
        values.put(COD_DEP, tab_item_romaneioent.getCod_dep());
        values.put(NOM_DEP, tab_item_romaneioent.getNom_dep());
        values.put(COD_IDE_LOC, tab_item_romaneioent.getCod_ide_loc());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.insert(TABLE_ITEM_RET, null, values);
    }

    //Retorna os dados da tabela Item_Romaneio_Ent
    public ArrayList getItem_Romaneio_Ent() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_ITEM_RET;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        cursor.moveToFirst();

        ArrayList<Item_romaneio_ent> lista_item_romaneioent = new ArrayList<Item_romaneio_ent>();

        if (cursor.moveToFirst()) {
            do {

                Item_romaneio_ent tab_item_romaneio_ent = new Item_romaneio_ent();

                tab_item_romaneio_ent.setNum_rom(cursor.getString(cursor.getColumnIndex(ITEM_NUM_ROM)));
                tab_item_romaneio_ent.setNum_seq(cursor.getString(cursor.getColumnIndex(ITEM_NUM_SEQ)));
                tab_item_romaneio_ent.setCod_ite(cursor.getLong(cursor.getColumnIndex(COD_ITE)));
                tab_item_romaneio_ent.setNum_reg_nts(cursor.getLong(cursor.getColumnIndex(ITEM_NUM_REG_NTS)));
                tab_item_romaneio_ent.setNom_ite(cursor.getString(cursor.getColumnIndex(NOM_ITE)));
                tab_item_romaneio_ent.setQtd_ite(cursor.getString(cursor.getColumnIndex(QTD_ITE)));
                tab_item_romaneio_ent.setQtd_pen(cursor.getString(cursor.getColumnIndex(QTD_PEN)));
                tab_item_romaneio_ent.setCod_uni(cursor.getString(cursor.getColumnIndex(COD_UNI)));
                tab_item_romaneio_ent.setCod_dep(cursor.getString(cursor.getColumnIndex(COD_DEP)));
                tab_item_romaneio_ent.setNom_dep(cursor.getString(cursor.getColumnIndex(NOM_DEP)));
                tab_item_romaneio_ent.setCod_ide_loc(cursor.getString(cursor.getColumnIndex(COD_IDE_LOC)));

                lista_item_romaneioent.add(tab_item_romaneio_ent);

            } while (cursor.moveToNext());
        }

        return lista_item_romaneioent;
    }

}
