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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rp_grf.jrmadeiras.Adapter.RegistroVooAdapters.AeronaveAdapter;
import com.rp_grf.jrmadeiras.Interfaces.RetornaAeronave;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoRegistroVoo;
import com.rp_grf.jrmadeiras.Tabelas.Aeronave;

import java.util.ArrayList;
import java.util.Collections;

public class BuscaAeronaveFragment extends DialogFragment {
    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB;

    private static RetornaAeronave retornaAeronave_interface;

    private boolean flag_online;

    private Long codigo_aeronave;
    private String prefixo_aeronave;
    private String nome_aeronave;
    private String tipo_aeronave;

    private BuscaAeronaveFragment.FragmentAListener listener;

    private EditText w_pesquisa_aeronave;
    private CardView w_botao_pesquisa;

    RecyclerView w_aeronave;

    private ArrayList<Aeronave> lista_aeronave;
    private AeronaveAdapter aeronaveAdapter;

    ProgressDialog dialogPesquisa;


    public interface FragmentAListener {
        void onInputASent(CharSequence input);
    }

    public BuscaAeronaveFragment() {

    }

    public static BuscaAeronaveFragment newInstance(String title, RetornaAeronave retornaAeronave) {
        BuscaAeronaveFragment frag = new BuscaAeronaveFragment();
        Bundle args = new Bundle();
        args.putString("", title);
        frag.setArguments(args);
        retornaAeronave_interface = retornaAeronave;
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busca_aeronave, container, false);

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
        referenceDB = firebaseDB.getReference("Aeronave");

        //Chama os dados temporarios do usuário
        SharedPreferences settings = this.getActivity().getSharedPreferences("usuario-temp", 0);
        String flag_login = settings.getString("flag_online", "");

        isConnected(flag_login);

        iniciarCampos(view);

        if (flag_online) {
            consultarAeronave_Online("");
        } else {
            consultarAeronave_Offline("");
        }

        cliqueBotaoPesquisar();

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
        w_pesquisa_aeronave = view.findViewById(R.id.edit_text_pesquisar_aeronave);
        w_pesquisa_aeronave.setHint("Pesquisar Aeronave");

        w_botao_pesquisa = view.findViewById(R.id.card_view_pesquisar_aeronave);

        w_aeronave = view.findViewById(R.id.recycler_view_aeronave);

        lista_aeronave = new ArrayList<Aeronave>();

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

                String texto_pesquisa = w_pesquisa_aeronave.getText().toString();

                if (flag_online) {
                    consultarAeronave_Online(texto_pesquisa);
                } else {
                    consultarAeronave_Offline(texto_pesquisa);
                }
            }
        });
    }

    //Faz a consulta no banco Firebase para filtrar a lista de aeronaves
    private void consultarAeronave_Online(final String texto_pesquisa) {
        dialogPesquisa.setMessage("Processando,\npor favor aguarde...");
        dialogPesquisa.show();

        referenceDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_aeronave.clear();

                for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                    Aeronave tab_aeronave = retorno.getValue(Aeronave.class);
                    String cod_bem = tab_aeronave.getCod_bem().toString().trim();
                    String pre_bem = tab_aeronave.getPre_bem();
                    String nom_bem = tab_aeronave.getNom_bem();
                    String tip_bem = tab_aeronave.getTip_bem();

                    if ((validaPesquisa(texto_pesquisa, cod_bem) == true) ||
                            (validaPesquisa(texto_pesquisa, pre_bem) == true) ||
                            (validaPesquisa(texto_pesquisa, nom_bem) == true) ||
                            (validaPesquisa(texto_pesquisa, tip_bem) == true)) {

                        lista_aeronave.add(tab_aeronave);

                    }

                }

                Collections.sort(lista_aeronave);

                aeronaveAdapter = new AeronaveAdapter(getActivity(), getDialog(),
                        lista_aeronave, retornaAeronave_interface);
                w_aeronave.setAdapter(aeronaveAdapter);
                aeronaveAdapter.notifyDataSetChanged();

                dialogPesquisa.dismiss();

                if (lista_aeronave.isEmpty()) {
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

    //Faz a consulta no banco SQLite para filtrar a lista de aeronaves
    private void consultarAeronave_Offline(final String texto_pesquisa) {
        BancoRegistroVoo bancoRegistroVoo = new BancoRegistroVoo(getActivity());

        ArrayList<Aeronave> retornoAeronave = bancoRegistroVoo.getAeronave();

        lista_aeronave.clear();

        for (int i = 0; i < retornoAeronave.size(); i++) {
            Aeronave tab_aeronave = new Aeronave();

            String cod_bem = retornoAeronave.get(i).getCod_bem().toString();
            String pre_bem = retornoAeronave.get(i).getPre_bem();
            String nom_bem = retornoAeronave.get(i).getNom_bem();
            String tip_bem = retornoAeronave.get(i).getTip_bem();

            tab_aeronave.setCod_bem(retornoAeronave.get(i).getCod_bem());
            tab_aeronave.setPre_bem(retornoAeronave.get(i).getPre_bem());
            tab_aeronave.setNom_bem(retornoAeronave.get(i).getNom_bem());
            tab_aeronave.setTip_bem(retornoAeronave.get(i).getTip_bem());

            if ((validaPesquisa(texto_pesquisa, cod_bem) == true) ||
                    (validaPesquisa(texto_pesquisa, pre_bem) == true) ||
                    (validaPesquisa(texto_pesquisa, nom_bem) == true) ||
                    (validaPesquisa(texto_pesquisa, tip_bem))) {

                lista_aeronave.add(tab_aeronave);

            }
        }

        Collections.sort(lista_aeronave);

        aeronaveAdapter = new AeronaveAdapter(getActivity(), getDialog(),
                lista_aeronave, retornaAeronave_interface);
        w_aeronave.setAdapter(aeronaveAdapter);
        aeronaveAdapter.notifyDataSetChanged();

        if (lista_aeronave.isEmpty()) {
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
