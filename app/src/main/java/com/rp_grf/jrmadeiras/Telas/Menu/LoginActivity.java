package com.rp_grf.jrmadeiras.Telas.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoAgendaTemp;
import com.rp_grf.jrmadeiras.SQLite.BancoFotos;
import com.rp_grf.jrmadeiras.SQLite.BancoModulos;
import com.rp_grf.jrmadeiras.SQLite.BancoProgramas;
import com.rp_grf.jrmadeiras.SQLite.BancoAgenda;
import com.rp_grf.jrmadeiras.SQLite.BancoRegistroVoo;
import com.rp_grf.jrmadeiras.SQLite.BancoUsuario;
import com.rp_grf.jrmadeiras.Tabelas.Aerodromo;
import com.rp_grf.jrmadeiras.Tabelas.Aeronave;
import com.rp_grf.jrmadeiras.Tabelas.Agenda;
import com.rp_grf.jrmadeiras.Tabelas.Cliente;
import com.rp_grf.jrmadeiras.Tabelas.Fotos;
import com.rp_grf.jrmadeiras.Tabelas.Modulos;
import com.rp_grf.jrmadeiras.Tabelas.Pre_voo;
import com.rp_grf.jrmadeiras.Tabelas.Programas;
import com.rp_grf.jrmadeiras.Tabelas.Tipo_voo;
import com.rp_grf.jrmadeiras.Tabelas.Usuario;
import com.rp_grf.jrmadeiras.Tabelas.Versao;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

/**
 * Autor: André
 * Data da última atualização: 15/10/2020
 */

public class LoginActivity extends AppCompatActivity {
    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB;

    //SQLite - Banco
    private BancoUsuario bancoUsuario;
    private BancoModulos bancoModulos;
    private BancoProgramas bancoProgramas;
    private BancoAgenda bancoAgenda;
    private BancoRegistroVoo bancoRegistroVoo;

    boolean flag_primeiro = false;

    Boolean flag_online;

    LinearLayout layout;
    ImageView img_view_logo;
    EditText edit_cod_usu;
    EditText edit_sen_usu;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        bancoUsuario = new BancoUsuario(this);
        bancoModulos = new BancoModulos(this);
        bancoProgramas = new BancoProgramas(this);

        bancoRegistroVoo = new BancoRegistroVoo(this);

        firebaseDB = FirebaseDatabase.getInstance();
        referenceDB = firebaseDB.getReference("Usuario"); //Define a referencia como sendo da tabela Usuario

        iniciarCampos(); //associa os parametros com os campos do XML do Layout

        modoPaisagem(); //Muda o layout quando em modo paisagem

        cliqueBotao(); //define as ações dos botões

        verificarVersao(); //Verifica se a versão do aplicativo está desatualizada

        limparBancoTemporario(); //limpa as tabelas temporarias

    }

    private void iniciarCampos() {
        layout = (LinearLayout) findViewById(R.id.linear_layout);

        edit_cod_usu = (EditText) findViewById(R.id.txt_usuario);
        edit_sen_usu = (EditText) findViewById(R.id.txt_senha);

        img_view_logo = (ImageView) findViewById(R.id.img_logo);

        button = (Button) findViewById(R.id.btn_entrar);
    }

    private void modoPaisagem() {
        if (LoginActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layout.setOrientation(LinearLayout.VERTICAL);
        } else {
            layout.setOrientation(LinearLayout.HORIZONTAL);
        }
    }

    private void cliqueBotao() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String w_cod_usu = edit_cod_usu.getText().toString().trim();
                String w_sen_usu = edit_sen_usu.getText().toString().trim();

                fechaTeclado();

                //Se ocorrer algum erro no login, limpa o banco do celular

                try {
                    verificarConexao(w_cod_usu, w_sen_usu);
                } catch (Exception ex) {
                    apagarDados();
                }
            }
        });
    }

    //Compara a versão do aplicativo com a última versão na nuvem
    private void verificarVersao() {
        firebaseDB.getReference("Versao")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Versao tab_versao = dataSnapshot.child("atual").getValue(Versao.class);

                        String[] versao_aplicativo = getResources().getString(R.string.app_version).split("\\.");
                        String[] versao_atual = tab_versao.getCod_ver().split("\\.");

                        //Indice que evita o erro IndexOutOfBounds
                        int maxIndex = Math.min(versao_aplicativo.length, versao_atual.length);

                        Boolean flag_versao = false;

                        for (int i = 0; i < maxIndex; i++) {

                            int inteiro_versao_aplicativo = Integer.valueOf(versao_aplicativo[i]);
                            int inteiro_versao_atual = Integer.valueOf(versao_atual[i]);

                            if (inteiro_versao_aplicativo < inteiro_versao_atual) {
                                flag_versao = false;
                            } else if (inteiro_versao_aplicativo >= inteiro_versao_atual) {
                                flag_versao = true;
                            }

                        }

                        final String url = String.valueOf(tab_versao.getUrl_dow());

                        if (flag_versao == false) {
                            alertHyperLink("APLICATIVO DESATUALIZADO!", "CLIQUE AQUI para atualizar", url);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        alertSmall("Não foi possível retornar a versão!");
                    }
                });
    }

    //Método que fecha o teclado
    private void fechaTeclado() {
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception ex) {
            System.out.println("fechaTeclado: " + ex.toString());
        }
    }

    //Método que limpa os dados do aplicativo
    private void apagarDados() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Aviso!");
        builder.setMessage("Foi encontrada uma incompatibilidade com os dados gravados offline.\n" +
                "Para acessar, seus dados atuais precisam ser apagados, deseja continuar?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String packageName = getApplicationContext().getPackageName();
                    Runtime runtime = Runtime.getRuntime();
                    runtime.exec("pm clear " + packageName);
                    alertMessage("Dados Apagados com sucesso!\nEfetue o login novamente.");
                } catch (Exception ex) {
                    System.out.println("apagarDados: " + ex.toString());
                    alertMessage("Ocorreu algo inesperado.\n" +
                            "Efetue a limpeza manual dos dados do aplicativo nas configurações do aparelho.");
                }
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }

        return deletedAll;
    }

    //Verifica se o celular consegue se conectar ao Firebase
    private void verificarConexao(final String w_cod_usu, final String w_sen_usu) {
        firebaseDB.getReference(".info/connected")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean connected = dataSnapshot.getValue(Boolean.class);
                        if (connected) {
                            flag_online = true;
                            eventoLogin_Online(w_cod_usu, w_sen_usu);
                        } else {
                            flag_online = false;
                            eventoLogin_Offline(w_cod_usu, w_sen_usu);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        alertSmall("Conexão Cancelada!");
                    }
                });
    }

    //Faz o login usando o banco Firebase
    private void eventoLogin_Online(final String w_cod_usu, final String w_sen_usu) {
        try {
            referenceDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(w_cod_usu).exists()) {
                        if (!w_cod_usu.isEmpty()) {
                            Usuario tab_usuario = dataSnapshot.child(w_cod_usu).getValue(Usuario.class);
                            if (tab_usuario.getSen_usu().equals(w_sen_usu)) {

                                enviarRegistroVoo();

                                atualizarBancoOffline();

                                defineUsuarioTemp(tab_usuario, "Sim");

                                //Inicia a tela de Menu
                                Intent intent = new Intent(getBaseContext(), ProgramasActivity.class);
                                intent.putExtra("cod_mod", "cm");
                                intent.putExtra("nom_mod", "Menu");
                                startActivity(intent);
                            } else {
                                alertMessage("Senha incorreta !!!");
                            }
                        } else {
                            alertSmall("Login não Preenchido !!!");
                        }
                    } else {
                        alertMessage("Usuário " + w_cod_usu + " não registrado !!!");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    alertSmall("Falha na validação !!!");
                }
            });
        } catch (Exception ex) {
            alertMessage("Login Online: " + ex.toString());
        }
    }

    //Faz o login usando o banco SQLite
    private void eventoLogin_Offline(final String w_cod_usu, final String w_sen_usu) {
        try {
            bancoUsuario = new BancoUsuario(this);

            ArrayList<Usuario> retornoUsuario = new ArrayList<Usuario>();

            retornoUsuario.clear();

            retornoUsuario = bancoUsuario.getUsuarios();


            if (retornoUsuario.size() != 0) {

                Boolean flag = false;

                for (int i = 0; i < retornoUsuario.size(); i++) {

                    if (retornoUsuario.get(i).getCod_usu() == null) {

                    } else if (retornoUsuario.get(i).getCod_usu().equals(w_cod_usu)) {

                        String senha = retornoUsuario.get(i).getSen_usu();

                        if (senha.equals(w_sen_usu)) {
                            flag = true;

                            Usuario tab_usuario = new Usuario();

                            tab_usuario.setCod_usu(retornoUsuario.get(i).getCod_usu());
                            tab_usuario.setSen_usu(retornoUsuario.get(i).getSen_usu());
                            tab_usuario.setNom_usu(retornoUsuario.get(i).getNom_usu());

                            defineUsuarioTemp(tab_usuario, "Nao");

                            //Inicia a tela de Menu
                            Intent intent = new Intent(getBaseContext(), ProgramasActivity.class);
                            intent.putExtra("cod_mod", "cm");
                            intent.putExtra("nom_mod", "Menu");
                            startActivity(intent);
                        }
                    }

                }

                if (flag != true) {
                    alertMessage("Login e/ou senha incorretos!");
                }

            } else {
                alertMessage("Usuário não cadastrado!");
            }


        } catch (Exception ex) {
            alertSmall("Login Offline: " + ex.toString());
        }
    }

    //Atualiza os dados do banco SQLite
    private void atualizarBancoOffline() {

        bancoUsuario.limparBanco(); //Limpa a tabela de usuarios e cria uma nova
        bancoUsuario = new BancoUsuario(this);

        atualizarUsuario();

        bancoModulos.limparBanco(); //Limpa a tabela de modulos e cria uma nova
        bancoModulos = new BancoModulos(this);

        atualizarModulos();

        bancoProgramas.limparBanco(); //Limpa a tabela de programas e cria uma nova
        bancoProgramas = new BancoProgramas(this);

        atualizarProgramas();

        bancoRegistroVoo.limparBanco(); //Limpa as tabelas de registro de voo e cria novas

        atualizarTabelasRegistroVoo();

    }

    private void atualizarUsuario() {
        firebaseDB.getReference("Usuario").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                    Usuario tab_usuario = retorno.getValue(Usuario.class);

                    bancoUsuario.setUsuarios(tab_usuario); //grava o retorno do Firebase no SQLite
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertSmall("Falha ao gravar banco temporário !!!");
            }
        });
    }

    private void atualizarModulos() {
        firebaseDB.getReference("Modulos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                    Modulos tab_modulos = retorno.getValue(Modulos.class);

                    bancoModulos.setModulos(tab_modulos); //grava o retorno do Firebase no SQLite
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertSmall("Falha ao gravar banco temporário !!!");
            }
        });
    }

    private void atualizarProgramas() {
        firebaseDB.getReference("Programas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                    Programas tab_programas = retorno.getValue(Programas.class);

                    bancoProgramas.setProgramas(tab_programas); //grava o retorno do Firebase no SQLite
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertSmall("Falha ao gravar banco temporário !!!");
            }
        });
    }

    private void atualizarTabelasRegistroVoo() {

        //Atualiza a tabela Pre_voo no banco SQLite
        firebaseDB.getReference("Pre_voo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot retorno : dataSnapshot.getChildren()) {

                    Pre_voo tab_pre_voo = retorno.getValue(Pre_voo.class);
                    bancoRegistroVoo.setPre_voo(tab_pre_voo); //grava o retorno do Firebase no SQLite

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertSmall("Falha ao gravar banco temporário !!!");
            }
        });

        //Atualiza a tabela Cliente no banco SQLite
        firebaseDB.getReference("Cliente").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot retorno : dataSnapshot.getChildren()) {

                    Cliente tab_cliente = retorno.getValue(Cliente.class);
                    bancoRegistroVoo.setCliente(tab_cliente); //grava o retorno do Firebase no SQLite

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertSmall("Falha ao gravar banco temporário !!!");
            }
        });

        //Atualiza a tabela Aeronave no banco SQLite
        firebaseDB.getReference("Aeronave").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot retorno : dataSnapshot.getChildren()) {

                    Aeronave tab_aeronave = retorno.getValue(Aeronave.class);
                    bancoRegistroVoo.setAeronave(tab_aeronave); //grava o retorno do Firebase no SQLite

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertSmall("Falha ao gravar banco temporário !!!");
            }
        });

        //Atualiza a tabela Tipo_voo no banco SQLite
        firebaseDB.getReference("Tipo_voo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot retorno : dataSnapshot.getChildren()) {

                    Tipo_voo tab_tipo_voo = retorno.getValue(Tipo_voo.class);
                    bancoRegistroVoo.setTipo_voo(tab_tipo_voo); //grava o retorno do Firebase no SQLite

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertSmall("Falha ao gravar banco temporário !!!");
            }
        });

        //Atualiza a tabela Aerodromo no banco SQLite
        firebaseDB.getReference("Aerodromo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot retorno : dataSnapshot.getChildren()) {

                    Aerodromo tab_aerodromo = retorno.getValue(Aerodromo.class);
                    bancoRegistroVoo.setAerodromo(tab_aerodromo); //grava o retorno do Firebase no SQLite

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertSmall("Falha ao gravar banco temporário !!!");
            }
        });

    }

    //Envia os regitros de voo salvos no banco SQLite para o Firebase
    private void enviarRegistroVoo() {
        bancoAgenda = new BancoAgenda(this);

        final ArrayList<Agenda> dadosAgenda = bancoAgenda.getAgenda();

        firebaseDB.getReference("Agenda").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int contador = 0;

                try {
                    for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                        Agenda tab_agenda = retorno.getValue(Agenda.class);

                        contador = Integer.parseInt(tab_agenda.getCod_age().toString());

                        System.out.println("ENTROU TRY");
                    }
                } catch (Exception ex) {
                    //contador = 0;
                    System.out.println("ENTROU CATCH");
                }

                contador++;

                System.out.println("contador " + contador);

                for (int i = 0; i < dadosAgenda.size(); i++) {

                    Agenda tab_agenda = dadosAgenda.get(i);

                    tab_agenda.setCod_age(Long.parseLong(String.valueOf(contador)));

                    String sequencia = String.valueOf(contador);

                    firebaseDB.getReference("Agenda").child(sequencia).setValue(tab_agenda);

                    enviarPreVoo(tab_agenda);

                    System.out.println("contador2 " + contador);

                    contador++;

                }

                bancoAgenda.limparBanco();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertSmall("Falha ao enviar registros de voo !!!");
            }
        });

    }

    //Envia as fotos salvos no banco SQLite para o Firebase
    private void enviarPreVoo(final Agenda tab_agenda) {

        final BancoRegistroVoo bancoRegistroVoo = new BancoRegistroVoo(this);

        final ArrayList<Pre_voo> dadosPre_voo = bancoRegistroVoo.getPre_voo();

        final DatabaseReference referencePre_voo = firebaseDB.getReference("Pre_voo");

        referencePre_voo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot retorno : dataSnapshot.getChildren()) {

                    Pre_voo tab_pre_voo = retorno.getValue(Pre_voo.class);

                    if ((tab_pre_voo.getCod_pre_voo() == tab_agenda.getCod_pre_voo())
                            && (tab_pre_voo.getCod_age() == Long.parseLong("0"))) {

                        tab_pre_voo.setCod_age(tab_agenda.getCod_age());

                        Long codigo_agenda = tab_agenda.getCod_age();

                        HashMap map = new HashMap();
                        map.put("cod_age", codigo_agenda);

                        referencePre_voo.child(tab_pre_voo.getCod_pre_voo().toString()).updateChildren(map);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertSmall("Falha ao enviar pre voos !!!");
            }
        });

        bancoRegistroVoo.close();

    }

    //Envia as fotos salvos no banco SQLite para o Firebase
    private void enviarFotos() {
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        BancoFotos bancoFotos = new BancoFotos(this);

        final ArrayList<Fotos> dadosFotos = bancoFotos.getFotos();

        for (int i = 0; i < dadosFotos.size(); i++) {

            String nome_foto = dadosFotos.get(i).getNome_arquivo();
            Uri caminho_foto = Uri.parse(dadosFotos.get(i).getCaminho_foto());

            final StorageReference banco = storageRef.child("fotos/" + nome_foto);

            banco.putFile(caminho_foto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    banco.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //alertMessage("Ocorreu um erro ao enviar fotos salvas, verifique sua conexão!");
                }
            });

        }
    }

    // Salva as informações temporarias do usuário logado
    private void defineUsuarioTemp(Usuario tab_usuario, String flag) {
        SharedPreferences settings = getSharedPreferences("usuario-temp", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("cod_usu_tmp", tab_usuario.getCod_usu());
        editor.putString("nom_usu_tmp", tab_usuario.getNom_usu());
        editor.putString("flag_online", flag);
        editor.commit();
    }

    private void limparBancoTemporario() {
        BancoAgendaTemp bancoAgendaTemp = new BancoAgendaTemp(this);

        bancoAgendaTemp.limparBanco();
    }

    private void alertSmall(String s) {
        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    private void alertMessage(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
        alert.setTitle("Erro");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }

    private void alertHyperLink(String titulo, String mensagem, String url) {

        TextView textView = new TextView(this);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(getResources().getDimension(R.dimen.h6));
        textView.setText(Html.fromHtml(
                "<br />" +
                        "<h4>" + titulo + "</h4>" +
                        "<br />" +
                        "<a href=\"" + url + "\">" + "\n\n" + mensagem + "</a>" +
                        "<br />" +
                        "<br />"
        ));

        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this).setView(textView);
        //alert.setTitle("APLICATIVO DESATUALIZADO");
        alert.setCancelable(false);
        alert.show();

    }
}
