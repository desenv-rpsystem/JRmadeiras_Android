package com.rp_grf.jrmadeiras.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rp_grf.jrmadeiras.Adapter.RegistroVooAdapters.AlunoAdapter;
import com.rp_grf.jrmadeiras.Interfaces.RetornaAluno;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoRegistroVoo;
import com.rp_grf.jrmadeiras.Tabelas.Cliente;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Busca Aluno - Registro de voo
 * Autor: André Castro
 */

public class BuscaAlunoFragment extends DialogFragment {
    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB;

    private static RetornaAluno retornaAluno_interface;

    private boolean flag_online;

    private Long codigo_aluno;
    private String nome_aluno;
    private String nome_fantasia_aluno;

    private FragmentAListener listener;

    private EditText w_pesquisa_aluno;
    private CardView w_botao_pesquisa;

    RecyclerView w_aluno;

    private ArrayList<Cliente> lista_aluno;
    private AlunoAdapter alunoAdapter;

    ProgressDialog dialogPesquisa;


    public interface FragmentAListener {
        void onInputASent(CharSequence input);
    }

    public BuscaAlunoFragment() {

    }

    public static BuscaAlunoFragment newInstance(String title, RetornaAluno retornaAluno) {
        BuscaAlunoFragment frag = new BuscaAlunoFragment();
        Bundle args = new Bundle();
        args.putString("", title);
        frag.setArguments(args);
        retornaAluno_interface = retornaAluno;
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busca_aluno, container, false);

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
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseDB = FirebaseDatabase.getInstance();
        referenceDB = firebaseDB.getReference("Cliente");

        //Chama os dados temporarios do usuário
        SharedPreferences settings = this.getActivity().getSharedPreferences("usuario-temp", 0);
        String flag_login = settings.getString("flag_online", "");

        isConnected(flag_login);

        iniciarCampos(view);

        cliqueBotaoPesquisar();

        if (flag_online) {
            consultarAlunos_Online("");
        } else {
            consultarAlunos_Offline("");
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
        w_pesquisa_aluno = view.findViewById(R.id.edit_text_pesquisar_aluno);
        w_pesquisa_aluno.setHint("Pesquisar Aluno");

        w_botao_pesquisa = view.findViewById(R.id.card_view_pesquisar_aluno);

        w_aluno = view.findViewById(R.id.recycler_view_aluno);

        lista_aluno = new ArrayList<Cliente>();

        dialogPesquisa = new ProgressDialog(getActivity(), R.style.DialogTheme);
    }

    private void cliqueBotaoPesquisar() {
        w_botao_pesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Fecha o teclado ao clicar no botão
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                String texto_pesquisa = w_pesquisa_aluno.getText().toString();

                if (flag_online) {
                    consultarAlunos_Online(texto_pesquisa);
                } else {
                    consultarAlunos_Offline(texto_pesquisa);
                }
            }
        });
    }

    //Faz a consulta no banco Firebase para filtrar a lista de alunos
    private void consultarAlunos_Online(final String texto_pesquisa) {
        dialogPesquisa.setMessage("Processando,\npor favor aguarde...");
        dialogPesquisa.show();
        referenceDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_aluno.clear();

                for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                    Cliente tab_aluno = retorno.getValue(Cliente.class);
                    String nom_cli = tab_aluno.getNom_cli();
                    String nom_fan_cli = tab_aluno.getNom_fan_cli();
                    String cod_cli = tab_aluno.getCod_cli().toString().trim();

                    if ((validaPesquisa(texto_pesquisa, nom_cli) == true) ||
                            (validaPesquisa(texto_pesquisa, nom_fan_cli) == true) ||
                            (validaPesquisa(texto_pesquisa, cod_cli) == true)) {

                        lista_aluno.add(tab_aluno);

                    }

                }

                Collections.sort(lista_aluno);

                alunoAdapter = new AlunoAdapter(getActivity(), getDialog(),
                        lista_aluno, retornaAluno_interface);
                w_aluno.setAdapter(alunoAdapter);
                alunoAdapter.notifyDataSetChanged();

                dialogPesquisa.dismiss();

                if (lista_aluno.isEmpty()) {
                    alertSmall("Nenhum Registro Encontrado !!!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertSmall("Não foi possível acessar a base de dados !!!\n" +
                        "Favor reiniciar o aplicativo.");
            }
        });
    }

    //Faz a consulta no banco SQLite para filtrar a lista de alunos
    private void consultarAlunos_Offline(final String texto_pesquisa) {
        BancoRegistroVoo bancoRegistroVoo = new BancoRegistroVoo(getActivity());

        ArrayList<Cliente> retornoAluno = bancoRegistroVoo.getCliente();

        lista_aluno.clear();

        for (int i = 0; i < retornoAluno.size(); i++) {
            Cliente tab_aluno = new Cliente();

            String cod_cli = retornoAluno.get(i).getCod_cli().toString();
            String nom_cli = retornoAluno.get(i).getNom_cli();
            String nom_fan_cli = retornoAluno.get(i).getNom_fan_cli();

            tab_aluno.setCod_cli(retornoAluno.get(i).getCod_cli());
            tab_aluno.setNom_cli(retornoAluno.get(i).getNom_cli());
            tab_aluno.setNom_fan_cli(retornoAluno.get(i).getNom_fan_cli());

            if ((validaPesquisa(texto_pesquisa, nom_cli) == true) ||
                    (validaPesquisa(texto_pesquisa, nom_fan_cli) == true) ||
                    (validaPesquisa(texto_pesquisa, cod_cli) == true)) {

                lista_aluno.add(tab_aluno);

            }
        }

        Collections.sort(lista_aluno);

        alunoAdapter = new AlunoAdapter(getActivity(), getDialog(), lista_aluno, retornaAluno_interface);
        w_aluno.setAdapter(alunoAdapter);
        alunoAdapter.notifyDataSetChanged();

        if (lista_aluno.isEmpty()) {
            alertSmall("Nenhum Registro Encontrado !!!");
        }
    }

    private boolean validaPesquisa(String texto_pesquisa, String texto_banco) {
        Boolean retorno = false;

        String[] array_separador = texto_pesquisa.toLowerCase().split("[ *]+");

        try {
            for (int i = 0; i < array_separador.length; i++) {
                if (texto_banco.toLowerCase().contains(array_separador[i])) {
                    retorno = true;
                } else {
                    retorno = false;
                    break;
                }
            }
        } catch (Exception ex) {
            alertSmall(ex.toString());
        }

        return retorno;
    }

    private void alertSmall(String s) {
        //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
        System.out.println(s);
    }
}