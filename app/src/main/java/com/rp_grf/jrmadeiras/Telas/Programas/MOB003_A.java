package com.rp_grf.jrmadeiras.Telas.Programas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rp_grf.jrmadeiras.Adapter.LiberacaoAdapters.DetalheLiberacaoAdapter;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.Tabelas.Det_lib_reg_nts;
import com.rp_grf.jrmadeiras.Tabelas.Lib_reg_nts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class MOB003_A extends AppCompatActivity {
    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB_registro;
    private static DatabaseReference referenceDB_detalhe;

    RecyclerView w_detalhe;

    ArrayList<Det_lib_reg_nts> lista_detalhe;

    DetalheLiberacaoAdapter detalheLiberacaoAdapter;

    TextView w_serie;
    TextView w_documento;

    TextView w_custo_total;
    TextView w_venda_total;
    TextView w_frete;
    TextView w_despesas;
    TextView w_margem;

    Button w_btn_liberar;

    Long numero_registro;

    Lib_reg_nts tab_registro;

    ProgressDialog dialog;

    Boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_mob003_a);

        numero_registro = Long.parseLong(getIntent().getStringExtra("num_reg_nts"));

        getSupportActionBar().setTitle("Registro " + numero_registro);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseDB = FirebaseDatabase.getInstance();
        referenceDB_registro = firebaseDB.getReference("Lib-reg-nts");
        referenceDB_detalhe = firebaseDB.getReference("Det_lib_reg_nts");

        iniciarCampos();

        iniciarDocumento();

        iniciarDetalhes();

        consultarItensDocto();

        cliqueBotao();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void iniciarCampos() {
        w_detalhe = (RecyclerView) findViewById(R.id.recycler_view_detalhes_liberar_documento);

        lista_detalhe = new ArrayList<Det_lib_reg_nts>();

        w_btn_liberar = (Button) findViewById(R.id.button_liberar_documento);

        dialog = new ProgressDialog(this, R.style.DialogTheme);
    }

    private void iniciarDocumento() {
        w_serie = (TextView) findViewById(R.id.detalhe_cod_ser);
        w_serie.setText(getIntent().getStringExtra("cod_ser"));

        w_documento = (TextView) findViewById(R.id.detalhe_num_doc);
        w_documento.setText(getIntent().getStringExtra("num_doc"));

    }

    private void iniciarDetalhes() {
        w_custo_total = (TextView) findViewById(R.id.detalhe_val_cus_tot);
        w_custo_total.setText(getIntent().getStringExtra("val_cus_tot"));

        w_venda_total = (TextView) findViewById(R.id.detalhe_val_ven_tot);
        w_venda_total.setText(getIntent().getStringExtra("val_ven_tot"));

        w_frete = (TextView) findViewById(R.id.detalhe_val_fre);
        w_frete.setText(getIntent().getStringExtra("val_fre"));

        w_despesas = (TextView) findViewById(R.id.detalhe_val_des_fin);
        w_despesas.setText(getIntent().getStringExtra("val_des_fin"));

        w_margem = (TextView) findViewById(R.id.detalhe_por_mar);
        w_margem.setText(getIntent().getStringExtra("por_mar"));
    }

    private void consultarItensDocto() {
        dialog.setMessage("Processando,\npor favor aguarde...");
        dialog.show();
        referenceDB_detalhe.child(numero_registro.toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_detalhe.clear();
                for (final DataSnapshot retornoBanco : dataSnapshot.getChildren()) {
                    Det_lib_reg_nts tab_detalhe = retornoBanco.getValue(Det_lib_reg_nts.class);
                    if (tab_detalhe.getNum_reg_nts().toString().equals(numero_registro.toString())) {
                        lista_detalhe.add(tab_detalhe);
                    }
                }
                Collections.sort(lista_detalhe);

                detalheLiberacaoAdapter = new DetalheLiberacaoAdapter(MOB003_A.this, lista_detalhe);
                w_detalhe.setAdapter(detalheLiberacaoAdapter);
                detalheLiberacaoAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });
    }

    private void cliqueBotao() {
        w_btn_liberar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String marca_equipamento = Build.MANUFACTURER;
                String modelo_equipamento = Build.MODEL + " " + Build.VERSION.RELEASE;
                String android_equipamento = Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();

                Calendar calendar = Calendar.getInstance();

                String data_atual = DateFormat.getDateInstance().format(calendar.getTime());
                data_atual.replace(".", "/");

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String hora_atual = sdf.format(date);

                //Chama os dados temporarios do usuário
                SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
                String usuario_tmp = settings.getString("cod_usu_tmp", "");

                liberarDocumento(marca_equipamento, modelo_equipamento, android_equipamento, usuario_tmp,
                        data_atual, hora_atual);


            }
        });
    }

    private void liberarDocumento(String marca_equipamento, String modelo_equipamento, String android_equipamento,
                                  String usuario_tmp, String data_atual, String hora_atual) {

        tab_registro = new Lib_reg_nts(
                Long.parseLong(getIntent().getStringExtra("num_reg_nts")),
                Long.parseLong(getIntent().getStringExtra("cod_est")),
                getIntent().getStringExtra("cod_ser"),
                getIntent().getStringExtra("num_doc"),
                getIntent().getStringExtra("cod_cli"),
                getIntent().getStringExtra("cod_ven"),
                getIntent().getStringExtra("val_cus_tot"),
                getIntent().getStringExtra("val_ven_tot"),
                getIntent().getStringExtra("val_fre"),
                getIntent().getStringExtra("val_des_fin"),
                getIntent().getStringExtra("por_mar"),
                data_atual,
                hora_atual,
                usuario_tmp,
                marca_equipamento,
                modelo_equipamento,
                android_equipamento
        );

        confirmarLiberacao(tab_registro);

    }

    private void confirmarLiberacao(final Lib_reg_nts tab_registro) {
        DialogInterface.OnClickListener diaglogClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        gravarDocumento(tab_registro);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder alert = new AlertDialog.Builder(MOB003_A.this);
        alert.setMessage("Confirma liberação do documento " + tab_registro.getNum_doc() + " ?");
        alert.setPositiveButton("SIM", diaglogClick);
        alert.setNegativeButton("NAO", diaglogClick);
        alert.setCancelable(false);
        alert.show();
    }

    private void gravarDocumento(final Lib_reg_nts tab_registro) {
        dialog.setMessage("Processando,\npor favor aguarde...");
        dialog.show();
        referenceDB_registro.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(tab_registro.getNum_reg_nts().toString()).exists()) {
                    referenceDB_registro.child(tab_registro.getNum_reg_nts().toString()).child("0")
                            .setValue(tab_registro);
                    w_btn_liberar.setText("Documento Liberado");
                    w_btn_liberar.setEnabled(false);
                } else {
                    alertMessage("Documento não encontrado !!!");
                }

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });
    }

    private void alertMessage(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MOB003_A.this);
        alert.setTitle("Erro");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}
