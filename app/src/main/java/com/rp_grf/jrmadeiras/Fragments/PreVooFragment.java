package com.rp_grf.jrmadeiras.Fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rp_grf.jrmadeiras.Adapter.RegistroVooAdapters.PreVooAdapter;
import com.rp_grf.jrmadeiras.Interfaces.RetornaAerodromo;
import com.rp_grf.jrmadeiras.Interfaces.RetornaAeronave;
import com.rp_grf.jrmadeiras.Interfaces.RetornaAluno;
import com.rp_grf.jrmadeiras.Interfaces.RetornaPreVoo;
import com.rp_grf.jrmadeiras.Interfaces.RetornaTipo_voo;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoRegistroVoo;
import com.rp_grf.jrmadeiras.Tabelas.Aerodromo;
import com.rp_grf.jrmadeiras.Tabelas.Aeronave;
import com.rp_grf.jrmadeiras.Tabelas.Cliente;
import com.rp_grf.jrmadeiras.Tabelas.Pre_voo;
import com.rp_grf.jrmadeiras.Tabelas.Pre_voo_Aux;
import com.rp_grf.jrmadeiras.Tabelas.Tipo_voo;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Busca Pré-voo - Registro de voo
 * Autor: André Castro
 */

public class PreVooFragment extends DialogFragment {
    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB;

    private static RetornaAluno retornaAluno_bundle;
    private static RetornaAeronave retornaAeronave_bundle;
    private static RetornaTipo_voo retornaTipo_voo_bundle;
    private static RetornaAerodromo retornaAerodromo_Origem_bundle;
    private static RetornaAerodromo retornaAerodromo_Destino_bundle;
    private static RetornaPreVoo retornaPreVoo_bundle;

    Long codigo_pre_voo;

    String codigo_instrutor;

    Long codigo_aluno;
    String nome_aluno;
    String nome_fantasia_aluno;
    String codigo_alternativo;
    String data_exame_medico;

    Long codigo_aeronave;
    String prefixo_aeronave;
    String nome_aeronave;
    String tipo_aeronave;

    Long codigo_tipo_voo;
    String nome_tipo_voo;

    Long codigo_origem;
    String nome_origem;
    String sigla_origem;

    Long codigo_destino;
    String nome_destino;
    String sigla_destino;

    private boolean flag_online;

    RecyclerView w_prevoo;

    private ArrayList<Pre_voo_Aux> lista_prevoo;
    private PreVooAdapter preVooAdapter;

    ProgressDialog dialogPesquisa;

    TextView w_sem_registro;

    public interface FragmentAListener {
        void onInputASent(CharSequence input);
    }

    public PreVooFragment() {

    }

    public static PreVooFragment newInstance(String title,
                                             RetornaAluno retornaAluno,
                                             RetornaAeronave retornaAeronave,
                                             RetornaTipo_voo retornaTipo_voo,
                                             RetornaAerodromo retornaOrigem,
                                             RetornaAerodromo retornaDestino,
                                             RetornaPreVoo retornaPreVoo) {
        PreVooFragment frag = new PreVooFragment();
        Bundle args = new Bundle();
        args.putString("", title);
        frag.setArguments(args);
        retornaAluno_bundle = retornaAluno;
        retornaAeronave_bundle = retornaAeronave;
        retornaTipo_voo_bundle = retornaTipo_voo;
        retornaAerodromo_Origem_bundle = retornaOrigem;
        retornaAerodromo_Destino_bundle = retornaDestino;
        retornaPreVoo_bundle = retornaPreVoo;
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prevoo, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = getResources().getDisplayMetrics().widthPixels;
        params.height = getResources().getDisplayMetrics().heightPixels;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseDB = FirebaseDatabase.getInstance();
        referenceDB = firebaseDB.getReference("Pre_voo");

        //Chama os dados temporarios do usuário
        SharedPreferences settings = this.getActivity().getSharedPreferences("usuario-temp", 0);
        String flag_login = settings.getString("flag_online", "");
        codigo_instrutor = settings.getString("cod_usu_tmp", "");

        isConnected(flag_login);

        iniciarCampos(view);

        if (flag_online) {
            consultarPreVoos_Online();
        } else {
            consultarPreVoos_Offline();
        }

        String title = getArguments().getString("title", "");
        getDialog().setTitle(title);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    //Verifica se o celular conseguiu se conectar ao Firebase
    private void isConnected(String flag_login) {
        if (flag_login.equals("Sim")) {
            flag_online = true;
        } else if (flag_login.equals("Nao")) {
            flag_online = false;
        } else {
            alertSmall("Erro ao verificar se há conexão!");
        }
    }

    private void iniciarCampos(View view) {
        w_prevoo = view.findViewById(R.id.recycler_view_prevoo);

        lista_prevoo = new ArrayList<Pre_voo_Aux>();

        dialogPesquisa = new ProgressDialog(getActivity(), R.style.DialogTheme);

        w_sem_registro = view.findViewById(R.id.text_view_prevoo);
    }

    private void preencheCampos(Long consulta_cod_cli, Long consulta_cod_bem, Long consulta_cod_tip_voo,
                                Long consulta_cod_aer_ori, Long consulta_cod_aer_des, int operacao) {

        if (flag_online) {
            consultarAluno_Online("Cliente", consulta_cod_cli, operacao);
            consultarAeronave_Online("Aeronave", consulta_cod_bem, operacao);
            consultarTipo_voo_Online("Tipo_voo", consulta_cod_tip_voo, operacao);
            consultarAerodromo_Origem_Online("Aerodromo", consulta_cod_aer_ori, operacao);
            consultarAerodromo_Destino_Online("Aerodromo", consulta_cod_aer_des, operacao);
        } else {
            consultarAluno_Offline(consulta_cod_cli, operacao);
            consultarAeronave_Offline(consulta_cod_bem, operacao);
            consultarTipo_voo_Offline(consulta_cod_tip_voo, operacao);
            consultarAerodromo_Origem_Offline(consulta_cod_aer_ori, operacao);
            consultarAerodromo_Destino_Offline(consulta_cod_aer_des, operacao);
        }

    }

    //Consulta pelo código no banco Firebase e retorna para a Activity usando a interface
    private void consultarAluno_Online(final String tabela, final Long codigo, final int operacao) {
        DatabaseReference reference = firebaseDB.getReference(tabela);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                    Cliente tab_aluno = retorno.getValue(Cliente.class);

                    String cod_cli = String.valueOf(tab_aluno.getCod_cli()).trim();
                    String nom_cli = tab_aluno.getNom_cli();
                    String nom_fan_cli = tab_aluno.getNom_fan_cli();
                    String cod_alt = tab_aluno.getCod_alt();
                    String dat_exa_med = tab_aluno.getDat_exa_med();

                    if (cod_cli.equals(String.valueOf(codigo))) {

                        //Operacao 1 preenche o item do recyclerview usando a interface
                        if (operacao == 1) {

                            retornaAluno_local.recebeAluno(cod_cli, nom_cli, nom_fan_cli, cod_alt, dat_exa_med);

                            //Operacao 2 retorna os dados para a activity usando a interface
                        } else if (operacao == 2) {

                            retornaAluno_bundle.recebeAluno(cod_cli, nom_cli, nom_fan_cli, cod_alt, dat_exa_med);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertSmall("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });

    }

    //Consulta pelo código no banco SQLite e retorna para a Activity usando a interface
    private void consultarAluno_Offline(final Long codigo, final int operacao) {
        BancoRegistroVoo bancoRegistroVoo = new BancoRegistroVoo(getActivity());

        ArrayList<Cliente> retornoAluno = bancoRegistroVoo.getCliente();

        for (int i = 0; i < retornoAluno.size(); i++) {

            String cod_cli = retornoAluno.get(i).getCod_cli().toString();
            String nom_cli = retornoAluno.get(i).getNom_cli();
            String nom_fan_cli = retornoAluno.get(i).getNom_fan_cli();
            String cod_alt = retornoAluno.get(i).getCod_alt();
            String dat_exa_med = retornoAluno.get(i).getDat_exa_med();

            if (cod_cli.equals(String.valueOf(codigo))) {

                //Operacao 1 preenche o item do recyclerview usando a interface
                if (operacao == 1) {

                    retornaAluno_local.recebeAluno(cod_cli, nom_cli, nom_fan_cli, cod_alt, dat_exa_med);

                    //Operacao 2 retorna os dados para a activity usando a interface
                } else if (operacao == 2) {

                    retornaAluno_bundle.recebeAluno(cod_cli, nom_cli, nom_fan_cli, cod_alt, dat_exa_med);

                }
            }
        }

    }

    //Consulta pelo código no banco Firebase e retorna para a Activity usando a interface
    private void consultarAeronave_Online(final String tabela, final Long codigo, final int operacao) {
        DatabaseReference reference = firebaseDB.getReference(tabela);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                    Aeronave tab_aeronave = retorno.getValue(Aeronave.class);

                    String cod_bem = tab_aeronave.getCod_bem().toString().trim();
                    String pre_bem = tab_aeronave.getPre_bem();
                    String nom_bem = tab_aeronave.getNom_bem();
                    String tip_bem = tab_aeronave.getTip_bem();

                    if (cod_bem.equals(String.valueOf(codigo))) {

                        //Operacao 1 preenche o item do recyclerview
                        if (operacao == 1) {

                            retornaAeronave_local.recebeAeronave(cod_bem, pre_bem, nom_bem, tip_bem);

                            //Operacao 2 retorna os dados para a activity
                        } else if (operacao == 2) {

                            retornaAeronave_bundle.recebeAeronave(cod_bem, pre_bem, nom_bem, tip_bem);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertSmall("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });

    }

    //Consulta pelo código no banco SQLite e retorna para a Activity usando a interface
    private void consultarAeronave_Offline(final Long codigo, final int operacao) {
        BancoRegistroVoo bancoRegistroVoo = new BancoRegistroVoo(getActivity());

        ArrayList<Aeronave> retornoAeronave = bancoRegistroVoo.getAeronave();

        for (int i = 0; i < retornoAeronave.size(); i++) {

            String cod_bem = retornoAeronave.get(i).getCod_bem().toString();
            String pre_bem = retornoAeronave.get(i).getPre_bem();
            String nom_bem = retornoAeronave.get(i).getNom_bem();
            String tip_bem = retornoAeronave.get(i).getTip_bem();

            if (cod_bem.equals(String.valueOf(codigo))) {

                //Operacao 1 preenche o item do recyclerview usando a interface
                if (operacao == 1) {

                    retornaAeronave_local.recebeAeronave(cod_bem, pre_bem, nom_bem, tip_bem);

                    //Operacao 2 retorna os dados para a activity usando a interface
                } else if (operacao == 2) {

                    retornaAeronave_bundle.recebeAeronave(cod_bem, pre_bem, nom_bem, tip_bem);

                }
            }
        }

    }

    //Consulta pelo código no banco Firebase e retorna para a Activity usando a interface
    private void consultarTipo_voo_Online(final String tabela, final Long codigo, final int operacao) {
        DatabaseReference reference = firebaseDB.getReference(tabela);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                    Tipo_voo tab_tipo_voo = retorno.getValue(Tipo_voo.class);

                    String cod_tip_voo = String.valueOf(tab_tipo_voo.getCod_tip_voo()).trim();
                    String nom_tip_voo = tab_tipo_voo.getNom_tip_voo();

                    if (cod_tip_voo.equals(String.valueOf(codigo))) {

                        //Operacao 1 preenche o item do recyclerview
                        if (operacao == 1) {

                            retornaTipo_voo_local.recebeTipo_voo(cod_tip_voo, nom_tip_voo);

                            //Operacao 2 retorna os dados para a activity
                        } else if (operacao == 2) {

                            retornaTipo_voo_bundle.recebeTipo_voo(cod_tip_voo, nom_tip_voo);

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertSmall("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });

    }

    //Consulta pelo código no banco SQLite e retorna para a Activity usando a interface
    private void consultarTipo_voo_Offline(final Long codigo, final int operacao) {
        BancoRegistroVoo bancoRegistroVoo = new BancoRegistroVoo(getActivity());

        ArrayList<Tipo_voo> retorno_tipo_voo = bancoRegistroVoo.getTipo_voo();

        for (int i = 0; i < retorno_tipo_voo.size(); i++) {

            String cod_tip_voo = retorno_tipo_voo.get(i).getCod_tip_voo().toString();
            String nom_tip_voo = retorno_tipo_voo.get(i).getNom_tip_voo();

            if (cod_tip_voo.equals(String.valueOf(codigo))) {

                //Operacao 1 preenche o item do recyclerview usando a interface
                if (operacao == 1) {

                    retornaTipo_voo_local.recebeTipo_voo(cod_tip_voo, nom_tip_voo);

                    //Operacao 2 retorna os dados para a activity usando a interface
                } else if (operacao == 2) {

                    retornaTipo_voo_bundle.recebeTipo_voo(cod_tip_voo, nom_tip_voo);

                }
            }
        }

    }

    //Consulta pelo código no banco Firebase e retorna para a Activity usando a interface
    private void consultarAerodromo_Origem_Online(final String tabela, final Long codigo, final int operacao) {
        DatabaseReference reference = firebaseDB.getReference(tabela);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                    Aerodromo tab_aerodromo = retorno.getValue(Aerodromo.class);

                    String cod_aer = String.valueOf(tab_aerodromo.getCod_aer()).trim();
                    String nom_aer = tab_aerodromo.getNom_aer();
                    String sig_aer = tab_aerodromo.getSig_aer();

                    if (cod_aer.equals(String.valueOf(codigo))) {

                        //Operacao 1 preenche o item do recyclerview
                        if (operacao == 1) {

                            retornaOrigem_local.recebeAerodromo(cod_aer, nom_aer, sig_aer);

                            //Operacao 2 retorna os dados para a activity
                        } else if (operacao == 2) {


                            retornaAerodromo_Origem_bundle.recebeAerodromo(cod_aer, nom_aer, sig_aer);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertSmall("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });

    }

    //Consulta pelo código no banco SQLite e retorna para a Activity usando a interface
    private void consultarAerodromo_Origem_Offline(final Long codigo, final int operacao) {
        BancoRegistroVoo bancoRegistroVoo = new BancoRegistroVoo(getActivity());

        ArrayList<Aerodromo> retornoAerodromo = bancoRegistroVoo.getAerodromo();

        for (int i = 0; i < retornoAerodromo.size(); i++) {

            String cod_aer = retornoAerodromo.get(i).getCod_aer().toString();
            String nom_aer = retornoAerodromo.get(i).getNom_aer();
            String sig_aer = retornoAerodromo.get(i).getSig_aer();

            if (cod_aer.equals(String.valueOf(codigo))) {

                //Operacao 1 preenche o item do recyclerview usando a interface
                if (operacao == 1) {

                    retornaAerodromo_Origem_bundle.recebeAerodromo(cod_aer, nom_aer, sig_aer);

                    //Operacao 2 retorna os dados para a activity usando a interface
                } else if (operacao == 2) {

                    retornaAerodromo_Origem_bundle.recebeAerodromo(cod_aer, nom_aer, sig_aer);

                }
            }
        }
    }

    //Consulta pelo código no banco Firebase e retorna para a Activity usando a interface
    private void consultarAerodromo_Destino_Online(final String tabela, final Long codigo, final int operacao) {
        DatabaseReference reference = firebaseDB.getReference(tabela);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                    Aerodromo tab_aerodromo = retorno.getValue(Aerodromo.class);

                    String cod_aer = String.valueOf(tab_aerodromo.getCod_aer()).trim();
                    String nom_aer = tab_aerodromo.getNom_aer();
                    String sig_aer = tab_aerodromo.getSig_aer();

                    if (cod_aer.equals(String.valueOf(codigo))) {

                        //Operacao 1 preenche o item do recyclerview
                        if (operacao == 1) {

                            retornaDestino_local.recebeAerodromo(cod_aer, nom_aer, sig_aer);

                            //Operacao 2 retorna os dados para a activity
                        } else if (operacao == 2) {

                            retornaAerodromo_Destino_bundle.recebeAerodromo(cod_aer, nom_aer, sig_aer);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertSmall("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });

    }

    //Consulta pelo código no banco SQLite e retorna para a Activity usando a interface
    private void consultarAerodromo_Destino_Offline(final Long codigo, final int operacao) {
        BancoRegistroVoo bancoRegistroVoo = new BancoRegistroVoo(getActivity());

        ArrayList<Aerodromo> retornoAerodromo = bancoRegistroVoo.getAerodromo();

        for (int i = 0; i < retornoAerodromo.size(); i++) {

            String cod_aer = retornoAerodromo.get(i).getCod_aer().toString();
            String nom_aer = retornoAerodromo.get(i).getNom_aer();
            String sig_aer = retornoAerodromo.get(i).getSig_aer();

            if (cod_aer.equals(String.valueOf(codigo))) {

                //Operacao 1 preenche o item do recyclerview usando a interface
                if (operacao == 1) {

                    retornaAerodromo_Destino_bundle.recebeAerodromo(cod_aer, nom_aer, sig_aer);

                    //Operacao 2 retorna os dados para a activity usando a interface
                } else if (operacao == 2) {

                    retornaAerodromo_Destino_bundle.recebeAerodromo(cod_aer, nom_aer, sig_aer);

                }
            }
        }
    }

    //Faz a consulta no banco Firebase para filtrar a lista de prevoos por instrutor
    private void consultarPreVoos_Online() {
        dialogPesquisa.setMessage("Processando,\npor favor aguarde...");
        dialogPesquisa.show();
        referenceDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_prevoo.clear();

                for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                    Pre_voo tab_pre_voo = retorno.getValue(Pre_voo.class);

                    if (tab_pre_voo.getCod_cli_ins().equals(codigo_instrutor)) {

                        if ((tab_pre_voo.getCod_age() == Long.parseLong("0"))) {

                            Long consulta_cod_pre_voo = tab_pre_voo.getCod_pre_voo();
                            Long consulta_cod_cli = tab_pre_voo.getCod_cli();
                            Long consulta_cod_bem = tab_pre_voo.getCod_bem();
                            Long consulta_cod_tip_voo = tab_pre_voo.getCod_tip_voo();
                            Long consulta_cod_aer_ori = tab_pre_voo.getCod_aer_ori();
                            Long consulta_cod_aer_des = tab_pre_voo.getCod_aer_des();

                            preencheCampos(
                                    consulta_cod_cli,
                                    consulta_cod_bem,
                                    consulta_cod_tip_voo,
                                    consulta_cod_aer_ori,
                                    consulta_cod_aer_des,
                                    1);

                            Pre_voo_Aux tab_aux = new Pre_voo_Aux();

                            tab_aux.setCod_pre_voo(consulta_cod_pre_voo);
                            tab_aux.setNom_cli(tab_pre_voo.getNom_cli());
                            tab_aux.setPre_bem(tab_pre_voo.getPre_bem());
                            tab_aux.setNom_tip_voo(tab_pre_voo.getNom_tip_voo());
                            tab_aux.setDat_mov_agv(tab_pre_voo.getDat_mov_agv());
                            tab_aux.setPer_ini_agv(tab_pre_voo.getPer_ini_agv());

                            lista_prevoo.add(tab_aux);

                        }
                    }
                }

                Collections.sort(lista_prevoo);

                preVooAdapter = new PreVooAdapter(getActivity(), getDialog(),
                        lista_prevoo, retornaPreVoo);
                w_prevoo.setAdapter(preVooAdapter);
                preVooAdapter.notifyDataSetChanged();

                dialogPesquisa.dismiss();

                if (lista_prevoo.isEmpty()) {
                    mostrarMensagemSemRegistro(true);
                } else {
                    mostrarMensagemSemRegistro(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertSmall("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });
    }

    //Consulta o banco Firebase usando o código do pre voo e retorna os dados para a activity, usando a interface
    private void retornaAgendaSelecionada_Online(final Long codigo) {
        referenceDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                    Pre_voo tab_pre_voo = retorno.getValue(Pre_voo.class);

                    System.out.println("AGENDA SELECIONADA ONLINE FRAGMENT");
                    System.out.println(tab_pre_voo.getCod_pre_voo());
                    System.out.println(codigo);

                    String cod_pre_voo = String.valueOf(tab_pre_voo.getCod_pre_voo());

                    if (cod_pre_voo.equals(String.valueOf(codigo))) {
                        Long consulta_cod_cli = tab_pre_voo.getCod_cli();
                        Long consulta_cod_bem = tab_pre_voo.getCod_bem();
                        Long consulta_cod_tip_voo = tab_pre_voo.getCod_tip_voo();
                        Long consulta_cod_aer_ori = tab_pre_voo.getCod_aer_ori();
                        Long consulta_cod_aer_des = tab_pre_voo.getCod_aer_des();

                        System.out.println("ENTROU IF AGENDA SELECIONADA");

                        preencheCampos(
                                consulta_cod_cli,
                                consulta_cod_bem,
                                consulta_cod_tip_voo,
                                consulta_cod_aer_ori,
                                consulta_cod_aer_des,
                                2);

                        retornaPreVoo_bundle.recebePreVoo(String.valueOf(codigo));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertSmall("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });
    }

    //Faz a consulta no banco SQLite para filtrar a lista de Pre_voo
    private void consultarPreVoos_Offline() {
        BancoRegistroVoo bancoRegistroVoo = new BancoRegistroVoo(getActivity());

        ArrayList<Pre_voo> retornoPre_voo = bancoRegistroVoo.getPre_voo();

        lista_prevoo.clear();

        for (int i = 0; i < retornoPre_voo.size(); i++) {
            Pre_voo tab_pre_voo = new Pre_voo();

            Long cod_pre_voo = retornoPre_voo.get(i).getCod_pre_voo();
            Long cod_age = retornoPre_voo.get(i).getCod_age();
            Long cod_cli = retornoPre_voo.get(i).getCod_cli();
            String nom_cli = retornoPre_voo.get(i).getNom_cli();
            Long cod_bem = retornoPre_voo.get(i).getCod_bem();
            String pre_bem = retornoPre_voo.get(i).getPre_bem();
            Long cod_tip_voo = retornoPre_voo.get(i).getCod_tip_voo();
            String nom_tip_voo = retornoPre_voo.get(i).getNom_tip_voo();
            Long cod_aer_ori = retornoPre_voo.get(i).getCod_aer_ori();
            String sig_aer_ori = retornoPre_voo.get(i).getSig_aer_ori();
            Long cod_aer_des = retornoPre_voo.get(i).getCod_aer_des();
            String sig_aer_des = retornoPre_voo.get(i).getSig_aer_ori();
            String cod_cli_ins = retornoPre_voo.get(i).getCod_cli_ins();
            String dat_mov_agv = retornoPre_voo.get(i).getDat_mov_agv();
            String per_ini_agv = retornoPre_voo.get(i).getPer_ini_agv();

            tab_pre_voo.setCod_pre_voo(cod_pre_voo);
            tab_pre_voo.setCod_age(cod_age);
            tab_pre_voo.setCod_cli(cod_cli);
            tab_pre_voo.setNom_cli(nom_cli);
            tab_pre_voo.setCod_bem(cod_bem);
            tab_pre_voo.setPre_bem(pre_bem);
            tab_pre_voo.setCod_tip_voo(cod_tip_voo);
            tab_pre_voo.setNom_tip_voo(nom_tip_voo);
            tab_pre_voo.setCod_aer_ori(cod_aer_ori);
            tab_pre_voo.setSig_aer_ori(sig_aer_ori);
            tab_pre_voo.setCod_aer_des(cod_aer_des);
            tab_pre_voo.setSig_aer_des(sig_aer_des);
            tab_pre_voo.setCod_cli_ins(cod_cli_ins);
            tab_pre_voo.setDat_mov_agv(dat_mov_agv);
            tab_pre_voo.setPer_ini_agv(per_ini_agv);

            //

            Pre_voo_Aux tab_aux = new Pre_voo_Aux();

            tab_aux.setCod_pre_voo(tab_pre_voo.getCod_pre_voo());
            tab_aux.setNom_cli(tab_pre_voo.getNom_cli());
            tab_aux.setPre_bem(tab_pre_voo.getPre_bem());
            tab_aux.setNom_tip_voo(tab_pre_voo.getNom_tip_voo());
            tab_aux.setDat_mov_agv(tab_pre_voo.getDat_mov_agv());
            tab_aux.setPer_ini_agv(tab_pre_voo.getPer_ini_agv());

            if (tab_pre_voo.getCod_cli_ins().equals(codigo_instrutor)) {

                System.out.println(tab_pre_voo.getCod_pre_voo());
                System.out.println(tab_pre_voo.getCod_age());

                if ((tab_pre_voo.getCod_age() == Long.parseLong("0"))) {

                    lista_prevoo.add(tab_aux);

                }
            }
        }

        Collections.sort(lista_prevoo);

        preVooAdapter = new PreVooAdapter(getActivity(), getDialog(), lista_prevoo, retornaPreVoo);
        w_prevoo.setAdapter(preVooAdapter);
        preVooAdapter.notifyDataSetChanged();

        if (lista_prevoo.isEmpty()) {
            mostrarMensagemSemRegistro(true);
        } else {
            mostrarMensagemSemRegistro(false);
        }

    }

    private void mostrarMensagemSemRegistro(Boolean flag) {

        if (flag == true) {
            w_prevoo.setVisibility(View.GONE);

            w_sem_registro.setVisibility(View.VISIBLE);

            w_sem_registro.setText("Nenhuma Agenda Encontrada !!!\nClique para Voltar");
            w_sem_registro.setClickable(true);
            w_sem_registro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        } else {
            w_prevoo.setVisibility(View.VISIBLE);

            w_sem_registro.setVisibility(View.GONE);
        }

    }

    //Consulta o banco SQLite usando o código do pre voo e retorna os dados para a activity, usando a interface
    private void retornaAgendaSelecionada_Offline(final Long codigo) {

        BancoRegistroVoo bancoRegistroVoo = new BancoRegistroVoo(getActivity());

        ArrayList<Pre_voo> retornoPre_voo = bancoRegistroVoo.getPre_voo();

        for (int i = 0; i < retornoPre_voo.size(); i++) {

            if (retornoPre_voo.get(i).getCod_pre_voo() == codigo) {
                Long consulta_cod_cli = retornoPre_voo.get(i).getCod_cli();
                Long consulta_cod_bem = retornoPre_voo.get(i).getCod_bem();
                Long consulta_cod_tip_voo = retornoPre_voo.get(i).getCod_tip_voo();
                Long consulta_cod_aer_ori = retornoPre_voo.get(i).getCod_aer_ori();
                Long consulta_cod_aer_des = retornoPre_voo.get(i).getCod_aer_des();

                preencheCampos(
                        consulta_cod_cli,
                        consulta_cod_bem,
                        consulta_cod_tip_voo,
                        consulta_cod_aer_ori,
                        consulta_cod_aer_des,
                        2);

                retornaPreVoo_bundle.recebePreVoo(String.valueOf(codigo));
            }

        }

    }

    private void alertSmall(String s) {
        //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
        System.out.println(s);
    }

    //Interface

    RetornaPreVoo retornaPreVoo = new RetornaPreVoo() {
        @Override
        public void recebePreVoo(String cod_pre_voo) {
            codigo_pre_voo = Long.parseLong(cod_pre_voo);

            if (flag_online) {
                retornaAgendaSelecionada_Online(codigo_pre_voo);
            } else {
                retornaAgendaSelecionada_Offline(codigo_pre_voo);
            }

        }
    };

    RetornaAluno retornaAluno_local = new RetornaAluno() {
        @Override
        public void recebeAluno(String cod_cli, String nom_cli, String nom_fan_cli,
                                String cod_alt, String dat_exa_med) {
            codigo_aluno = Long.parseLong(cod_cli);
            nome_aluno = nom_cli;
            nome_fantasia_aluno = nom_fan_cli;
            codigo_alternativo = cod_alt;
            data_exame_medico = dat_exa_med;
        }
    };

    RetornaAeronave retornaAeronave_local = new RetornaAeronave() {
        @Override
        public void recebeAeronave(String cod_bem, String pre_bem, String nom_bem, String tip_bem) {
            codigo_aeronave = Long.parseLong(cod_bem);
            prefixo_aeronave = pre_bem;
            nome_aeronave = nom_bem;
            tipo_aeronave = tip_bem;
        }
    };

    RetornaTipo_voo retornaTipo_voo_local = new RetornaTipo_voo() {
        @Override
        public void recebeTipo_voo(String cod_tip_voo, String nom_tip_voo) {
            codigo_tipo_voo = Long.parseLong(cod_tip_voo);
            nome_tipo_voo = nom_tip_voo;
        }
    };

    RetornaAerodromo retornaOrigem_local = new RetornaAerodromo() {
        @Override
        public void recebeAerodromo(String cod_aer, String nom_aer, String sig_aer) {
            codigo_origem = Long.parseLong(cod_aer);
            nome_origem = nom_aer;
            sigla_origem = sig_aer;
        }
    };

    RetornaAerodromo retornaDestino_local = new RetornaAerodromo() {
        @Override
        public void recebeAerodromo(String cod_aer, String nom_aer, String sig_aer) {
            codigo_destino = Long.parseLong(cod_aer);
            nome_destino = nom_aer;
            sigla_destino = sig_aer;
        }
    };
}