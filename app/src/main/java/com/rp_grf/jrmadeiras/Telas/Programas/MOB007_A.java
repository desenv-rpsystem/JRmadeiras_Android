package com.rp_grf.jrmadeiras.Telas.Programas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rp_grf.jrmadeiras.Utils.GpsTracker;
import com.rp_grf.jrmadeiras.Adapter.RomaneioAdapters.ItemEntregaAdapter;
import com.rp_grf.jrmadeiras.Fragments.AssinaturaFragment;
import com.rp_grf.jrmadeiras.Fragments.TirarFotoFragment;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoAssinatura;
import com.rp_grf.jrmadeiras.SQLite.BancoFotos;
import com.rp_grf.jrmadeiras.SQLite.BancoMapaEntrega;
import com.rp_grf.jrmadeiras.Tabelas.Fotos;
import com.rp_grf.jrmadeiras.Tabelas.Item_romaneio_ent;
import com.rp_grf.jrmadeiras.Tabelas.Romaneio_ent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class MOB007_A extends AppCompatActivity {

    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB_romaneio;
    private static DatabaseReference referenceDB_item_romaneio;

    boolean flag_online;

    String latitude_chegada;
    String longitude_chegada;

    String data_chegada;
    String horario_chegada;

    RecyclerView w_registro_mapa;

    ArrayList<Item_romaneio_ent> lista_item_romaneioent;

    ItemEntregaAdapter itemEntregaAdapter;

    TextView w_serie;
    TextView w_documento;
    TextView w_sequencia;

    Button w_btn_confirmar;

    String descricao_programa;

    String numero_registro;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_mob007_a);

        descricao_programa = getIntent().getStringExtra("des_prg");

        numero_registro = getIntent().getStringExtra("num_reg_nts");

        getSupportActionBar().setTitle("Detalhes");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseDB = FirebaseDatabase.getInstance();
        referenceDB_romaneio = firebaseDB.getReference("Romaneio_ent");
        referenceDB_item_romaneio = firebaseDB.getReference("Item_romaneio_ent");

        //Flag que verifica se estava online na hora do login
        SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
        String flag_login = settings.getString("flag_online", "");

        isConnected(flag_login);

        iniciarCampos();

        cliqueBotao();

        verificarStatusConexao();

        iniciarDocumento();

        limparAssinaturas();
    }

    //Verifica se o celular conseguiu se conectar ao Firebase
    private void isConnected(String flag_login) {
        if (flag_login.equals("Sim")) {
            flag_online = true;
        } else if (flag_login.equals("Nao")) {
            flag_online = false;
        } else {
            alertMessage("Erro ao verificar se há conexão!");
        }
    }

    private void limparAssinaturas() {
        BancoFotos bancoFotos = new BancoFotos(this);

        bancoFotos.limparBanco();

        BancoAssinatura bancoAssinatura = new BancoAssinatura(this);

        bancoAssinatura.limparBanco();
    }

    private void iniciarCampos() {

        w_registro_mapa = (RecyclerView) findViewById(R.id.recycler_view_confirmar_mapa_entrega);

        w_btn_confirmar = (Button) findViewById(R.id.button_confirmar_entrega);

        lista_item_romaneioent = new ArrayList<Item_romaneio_ent>();

        dialog = new ProgressDialog(this, R.style.DialogTheme);

    }

    private void cliqueBotao() {
        w_btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validarAssinatura() == true) {

                    //Verifica se foi gravada as coordenadas de chegada
                    if (latitude_chegada != null && longitude_chegada != null) {

                        if (flag_online == true) {
                            enviarFotos();
                            enviarAssinaturas();
                            gravarRomaneio_Online();
                        } else {
                            gravarRomaneio_Offline();
                        }

                        Toasty.success(MOB007_A.this,
                                "Entrega Confirmada!",
                                Toast.LENGTH_SHORT, true).show();
                        finish();

                    } else {
                        alertMessage("Não há confirmação de chegada!");
                    }
                } else {
                    alertMessage("É obrigatória a assinatura do cliente para confirmar a entrega!");
                }

            }
        });
    }

    private void iniciarDocumento() {
        w_serie = (TextView) findViewById(R.id.cod_ser_item_mapa_entrega);
        w_serie.setText(getIntent().getStringExtra("cod_ser"));

        w_documento = (TextView) findViewById(R.id.num_doc_item_mapa_entrega);
        w_documento.setText("Documento " + getIntent().getStringExtra("num_doc"));

        w_sequencia = (TextView) findViewById(R.id.num_seq_item_mapa_entrega);
        w_sequencia.setText("Seq. " + getIntent().getStringExtra("num_seq"));

    }

    //Recebe a data atual no celular e retorna como String
    private String retornarData() {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String data_atual = sdf.format(date);

        //String data_atual = DateFormat.getDateInstance().format(calendar.getTime());
        //data_atual.replace(".", "/");

        return data_atual;
    }

    //Recebe a hora atual no celular e retorna como String
    private String retornarHora() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String hora_atual = sdf.format(date);

        return hora_atual;
    }

    private void verificarStatusConexao() {

        consultarItemMapaEntrega_Online();

        /*
        if (flag_online == true) {
            consultarItemMapaEntrega_Online();
        } else if (flag_online == false) {
            consultarItemMapaEntrega_Offline();
        }
        */

    }

    private void consultarItemMapaEntrega_Online() {
        dialog.setMessage("Processando,\npor favor aguarde...");
        dialog.show();
        referenceDB_item_romaneio.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_item_romaneioent.clear();
                for (DataSnapshot retornoBanco : dataSnapshot.getChildren()) {
                    Item_romaneio_ent tab_item_romaneio = retornoBanco.getValue(Item_romaneio_ent.class);

                    String num_reg = String.valueOf(tab_item_romaneio.getNum_reg_nts());


                    if (num_reg.equals(numero_registro))
                        lista_item_romaneioent.add(tab_item_romaneio);
                }

                if (lista_item_romaneioent.isEmpty()) {
                    alertSmall("Não há itens no romaneio de entrega!");
                }

                Collections.sort(lista_item_romaneioent);


                itemEntregaAdapter = new ItemEntregaAdapter(MOB007_A.this, lista_item_romaneioent);
                w_registro_mapa.setAdapter(itemEntregaAdapter);
                itemEntregaAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });
    }

    private void consultarItemMapaEntrega_Offline() {

        BancoMapaEntrega bancoMapaEntrega = new BancoMapaEntrega(this);

        ArrayList<Item_romaneio_ent> retorno_item_romaneioent = bancoMapaEntrega.getItem_Romaneio_Ent();

        lista_item_romaneioent.clear();

        try {

            for (int i = 0; i < retorno_item_romaneioent.size(); i++) {

                Item_romaneio_ent tab_item_romaneio = new Item_romaneio_ent();

                Long cod_ite = retorno_item_romaneioent.get(i).getCod_ite();
                Long num_reg_nts = retorno_item_romaneioent.get(i).getNum_reg_nts();
                String nom_ite = retorno_item_romaneioent.get(i).getNom_ite();
                String qtd_ite = retorno_item_romaneioent.get(i).getQtd_ite();
                String cod_uni = retorno_item_romaneioent.get(i).getCod_uni();

                tab_item_romaneio.setCod_ite(cod_ite);
                tab_item_romaneio.setNum_reg_nts(num_reg_nts);
                tab_item_romaneio.setNom_ite(nom_ite);
                tab_item_romaneio.setQtd_ite(qtd_ite);
                tab_item_romaneio.setCod_uni(cod_uni);

                String num_reg = String.valueOf(tab_item_romaneio.getNum_reg_nts());

                if (num_reg.equals(numero_registro))
                    lista_item_romaneioent.add(tab_item_romaneio);
            }

            if (lista_item_romaneioent.isEmpty()) {
                alertSmall("Não há itens no romaneio de entrega!");
            }

            Collections.sort(lista_item_romaneioent);


            itemEntregaAdapter = new ItemEntregaAdapter(MOB007_A.this, lista_item_romaneioent);
            w_registro_mapa.setAdapter(itemEntregaAdapter);
            itemEntregaAdapter.notifyDataSetChanged();

        } catch (Exception ex) {
            System.out.println("consultarItemMapaEntrega_Offline: " + ex.toString());
        }

        bancoMapaEntrega.close();

    }

    private void gravarRomaneio_Online() {
        referenceDB_romaneio.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot retornoBanco : dataSnapshot.getChildren()) {
                    Romaneio_ent tab_romaneio = retornoBanco.getValue(Romaneio_ent.class);

                    if (tab_romaneio.getNum_reg_nts().toString().equals(numero_registro)) {
                        tab_romaneio.setDat_lib_re(retornarData());
                        tab_romaneio.setHor_lib_re(retornarHora());

                        //Chama os dados temporarios do usuário
                        SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
                        String usuario_tmp = settings.getString("cod_usu_tmp", "");

                        tab_romaneio.setUsu_lib_re(usuario_tmp);

                        tab_romaneio.setMap_lat(latitude_chegada);
                        tab_romaneio.setMap_lon(longitude_chegada);

                        tab_romaneio.setDat_che_cli(data_chegada);
                        tab_romaneio.setHor_che_cli(horario_chegada);

                        referenceDB_romaneio.child(numero_registro).setValue(tab_romaneio);

                        /*
                        String recipientList = "suporte.rpsystem@terra.com.br";
                        String[] recipients = recipientList.split(",");
                        String subject = "Teste Email Android";
                        String message = Html.fromHtml(new StringBuilder()
                                .append("<p><b>Teste Titulo</b></p>")
                                .append("<small><p>Teste Texto 123456</p></small>")
                                .toString()).toString();

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                        intent.putExtra(Intent.EXTRA_TEXT, message);
                        intent.setType("message/rfc822");
                        startActivity(Intent.createChooser(intent, "Selecione o aplicativo de email padrão"));

                         */

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertMessage("Não foi possível acessar a base de dados !!!\n" +
                        "Contate o suporte para mais detalhes.");
            }
        });
    }

    private void gravarRomaneio_Offline() {
        BancoMapaEntrega bancoMapaEntrega = new BancoMapaEntrega(this);

        ArrayList<Romaneio_ent> retorno_romaneio_ent = bancoMapaEntrega.getRomaneio_Ent();

        for (int i = 0; i < retorno_romaneio_ent.size(); i++) {

            Romaneio_ent tab_romaneio = new Romaneio_ent();

            Long num_seq = retorno_romaneio_ent.get(i).getNum_seq();
            Long num_reg_nts = retorno_romaneio_ent.get(i).getNum_reg_nts();
            String cod_ser = retorno_romaneio_ent.get(i).getCod_ser();
            String num_doc = retorno_romaneio_ent.get(i).getNum_doc();
            String nom_cli = retorno_romaneio_ent.get(i).getNom_cli();
            String end_cli = retorno_romaneio_ent.get(i).getEnd_cli();
            String num_end_cli = retorno_romaneio_ent.get(i).getNum_end_cli();
            String com_bai_cli = retorno_romaneio_ent.get(i).getCom_bai_cli();
            String nom_mun = retorno_romaneio_ent.get(i).getNom_mun();
            String cod_uni_fed = retorno_romaneio_ent.get(i).getCod_uni_fed();
            String cep_cli = retorno_romaneio_ent.get(i).getCep_cli();
            String fon_cli = retorno_romaneio_ent.get(i).getFon_cli();
            String fon_cli_2 = retorno_romaneio_ent.get(i).getFon_cli_2();
            String nom_ven = retorno_romaneio_ent.get(i).getNom_ven();
            String dat_lib_re = retorno_romaneio_ent.get(i).getDat_lib_re();
            String usu_lib_re = retorno_romaneio_ent.get(i).getUsu_lib_re();
            String hor_lib_re = retorno_romaneio_ent.get(i).getHor_lib_re();

            tab_romaneio.setNum_seq(num_seq);
            tab_romaneio.setNum_reg_nts(num_reg_nts);
            tab_romaneio.setCod_ser(cod_ser);
            tab_romaneio.setNum_doc(num_doc);
            tab_romaneio.setNom_cli(nom_cli);
            tab_romaneio.setEnd_cli(end_cli);
            tab_romaneio.setNum_end_cli(num_end_cli);
            tab_romaneio.setCom_bai_cli(com_bai_cli);
            tab_romaneio.setNom_mun(nom_mun);
            tab_romaneio.setCod_uni_fed(cod_uni_fed);
            tab_romaneio.setCep_cli(cep_cli);
            tab_romaneio.setFon_cli(fon_cli);
            tab_romaneio.setFon_cli_2(fon_cli_2);
            tab_romaneio.setNom_ven(nom_ven);
            tab_romaneio.setDat_lib_re(dat_lib_re);
            tab_romaneio.setUsu_lib_re(usu_lib_re);
            tab_romaneio.setHor_lib_re(hor_lib_re);

            if (tab_romaneio.getNum_reg_nts().toString().equals(numero_registro)) {

                tab_romaneio.setDat_lib_re(retornarData());
                tab_romaneio.setHor_lib_re(retornarHora());

                //Chama os dados temporarios do usuário
                SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
                String usuario_tmp = settings.getString("cod_usu_tmp", "");

                tab_romaneio.setUsu_lib_re(usuario_tmp);

                bancoMapaEntrega.updateRomaneio_ent(tab_romaneio);

                final Intent shareIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Email Teste Android");
                shareIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        Html.fromHtml(new StringBuilder()
                                .append("<p><b>Teste Titulo</b></p>")
                                .append("<small><p>Teste Texto 123456</p></small>")
                                .toString())
                );

            }

        }

        bancoMapaEntrega.close();

    }

    protected void sendEmail() {
        String[] TO = {""};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MOB007_A.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarAssinatura() {

        boolean validado = false;

        BancoAssinatura bancoAssinatura = new BancoAssinatura(this);

        final ArrayList<Fotos> dadosAssinatura = bancoAssinatura.getAssinatura();

        for (int i = 0; i < dadosAssinatura.size(); i++) {

            if (dadosAssinatura.get(i).getCodigo_registro().equals(numero_registro)) {
                validado = true;
            }
        }

        return validado;
    }

    //Envia as fotos para a nuvem
    private void enviarFotos() {
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        BancoFotos bancoFotos = new BancoFotos(this);

        final ArrayList<Fotos> dadosFotos = bancoFotos.getFotos();

        //Percorre os arraylist com o caminho das fotos salvos na tabela temporaria
        for (int i = 0; i < dadosFotos.size(); i++) {

            if (dadosFotos.get(i).getCodigo_registro().equals(numero_registro)) {
                String nome_foto = dadosFotos.get(i).getNome_arquivo();
                Uri caminho_foto = Uri.parse(dadosFotos.get(i).getCaminho_foto());

                final StorageReference banco = storageRef.child("fotos/" + nome_foto);

                banco.putFile(caminho_foto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        banco.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //mensagem indicando sucesso
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        alertMessage("Ocorreu um erro ao enviar a foto, verifique sua conexão!");
                    }
                });

            }
        }
    }

    //Envia as assinaturas para a nuvem
    private void enviarAssinaturas() {
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        BancoAssinatura bancoAssinatura = new BancoAssinatura(this);

        final ArrayList<Fotos> dadosAssinatura = bancoAssinatura.getAssinatura();

        //Percorre os arraylist com o caminho das fotos salvos na tabela temporaria
        for (int i = 0; i < dadosAssinatura.size(); i++) {

            if (dadosAssinatura.get(i).getCodigo_registro().equals(numero_registro)) {
                String nome_imagem = dadosAssinatura.get(i).getNome_arquivo();
                Uri caminho_imagem = Uri.parse(dadosAssinatura.get(i).getCaminho_foto());

                final StorageReference banco = storageRef.child("assinaturas/" + nome_imagem);

                banco.putFile(caminho_imagem).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        banco.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //mensagem indicando sucesso
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        alertMessage("Ocorreu um erro ao enviar a assinatura, verifique sua conexão!");
                    }
                });

            }
        }
    }

    //Método que abre o fragment de tirar foto
    private void iniciarCamera() {
        FragmentManager fragment = getSupportFragmentManager();
        TirarFotoFragment alertDialog = TirarFotoFragment.newInstance("Foto");

        Bundle bundle = new Bundle();
        bundle.putString("codigo", numero_registro);
        //Nome da foto no banco Storage
        bundle.putString("nome", "MAPA_RETIRA_" + numero_registro + "_");

        alertDialog.setArguments(bundle);
        alertDialog.show(fragment, "fragment_alert");
    }

    //Método que abre o fragment de assinatura
    private void iniciarAssinatura() {
        FragmentManager fragment = getSupportFragmentManager();
        AssinaturaFragment alertDialog = AssinaturaFragment.newInstance("Assinatura");

        Bundle bundle = new Bundle();
        bundle.putString("codigo", numero_registro);
        //Mensagem que vai aparecer na tela de assinatura
        bundle.putString("confirmacao", "Confirmo recebimento de mercadoria");
        //Nome da assinatura no banco Storage
        bundle.putString("nome", "MAPA_RETIRA_" + numero_registro + "_");

        alertDialog.setArguments(bundle);
        alertDialog.show(fragment, "fragment_alert");
    }

    private void receberCoordenadas() {

        //Solicitar permissão para usar o GPS
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        GpsTracker gpsTracker = new GpsTracker(this);
        if (gpsTracker.canGetLocation()) {

            //Recebe a data e hora
            final String data = retornarData();
            final String horario = retornarHora();

            //Recebe as coordenadas
            final Double latitude = gpsTracker.getLatitude();
            final Double longitude = gpsTracker.getLongitude();

            //Janela de confirmação de chegada
            AlertDialog.Builder alertGps = new AlertDialog.Builder(MOB007_A.this);
            alertGps.setTitle("Chegada");
            alertGps.setMessage(data + " - " + horario + "\n\n" +
                    "Latitude: " + latitude.toString() + "\nLongitude: " + longitude.toString());


            alertGps.setPositiveButton("Gravar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    latitude_chegada = latitude.toString();
                    longitude_chegada = longitude.toString();

                    data_chegada = data;
                    horario_chegada = horario;

                    Toasty.success(MOB007_A.this,
                            "Salvo",
                            Toast.LENGTH_SHORT, true).show();
                }
            });
            alertGps.show();

        } else {
            gpsTracker.showSettingsAlert();
        }

    }

    // Cria o botão na Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.botao_camera, menu);
        getMenuInflater().inflate(R.menu.botao_bandeira, menu);
        getMenuInflater().inflate(R.menu.botao_assinar, menu);
        getMenuInflater().inflate(R.menu.botao_offline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Define a ação dos botões da Action Bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Botão voltar
            case android.R.id.home:
                super.onBackPressed();
                this.finish();
                return true;
            //Botão camera
            case R.id.botao_camera_action_bar:
                iniciarCamera();
                return true;
            //Botão chegada
            case R.id.botao_bandeira_action_bar:
                receberCoordenadas();
                return true;
            //Botão assinatura
            case R.id.botao_assinar_action_bar:
                iniciarAssinatura();
                return true;
            //Botão offline
            case R.id.botao_offline_action_bar:
                Toasty.info(MOB007_A.this,
                        "Atenção!\nSem conexão com a nuvem.",
                        Toast.LENGTH_SHORT, true).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Oculta o botão se retornar que foi possível conectar com a nuvem
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (flag_online == true) {
            menu.findItem(R.id.botao_offline_action_bar)
                    .setVisible(false).setEnabled(false);
        } else {
            menu.findItem(R.id.botao_offline_action_bar)
                    .setVisible(true).setEnabled(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void alertSmall(String s) {
        Toast.makeText(MOB007_A.this, s, Toast.LENGTH_SHORT).show();
    }

    private void alertMessage(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MOB007_A.this);
        //alert.setTitle("Erro");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }

}