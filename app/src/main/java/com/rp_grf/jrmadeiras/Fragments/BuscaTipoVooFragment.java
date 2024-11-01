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
import com.rp_grf.jrmadeiras.Adapter.RegistroVooAdapters.TipoVooAdapter;
import com.rp_grf.jrmadeiras.Interfaces.RetornaTipo_voo;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoRegistroVoo;
import com.rp_grf.jrmadeiras.Tabelas.Tipo_voo;

import java.util.ArrayList;
import java.util.Collections;

public class BuscaTipoVooFragment extends DialogFragment {
    //Firebase - Banco
    private static FirebaseDatabase firebaseDB;
    private static DatabaseReference referenceDB;

    private static RetornaTipo_voo retornaTipo_voo_interface;

    private boolean flag_online;

    private Long codigo_tipo_voo;
    private String nome_tipo_voo;

    private BuscaTipoVooFragment.FragmentAListener listener;

    private EditText w_pesquisa_tipo_voo;
    private CardView w_botao_pesquisa;

    RecyclerView w_tipo_voo;

    private ArrayList<Tipo_voo> lista_tipo_voo;
    private TipoVooAdapter tipo_vooAdapter;

    ProgressDialog dialogPesquisa;

    public interface FragmentAListener {
        void onInputASent(CharSequence input);
    }

    public BuscaTipoVooFragment() {

    }

    public static BuscaTipoVooFragment newInstance(String title, RetornaTipo_voo retornaTipo_voo) {
        BuscaTipoVooFragment frag = new BuscaTipoVooFragment();
        Bundle args = new Bundle();
        args.putString("", title);
        frag.setArguments(args);
        retornaTipo_voo_interface = retornaTipo_voo;
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busca_tipo_voo, container, false);

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
        referenceDB = firebaseDB.getReference("Tipo_voo");

        //Chama os dados temporarios do usuário
        SharedPreferences settings = this.getActivity().getSharedPreferences("usuario-temp", 0);
        String flag_login = settings.getString("flag_online", "");

        isConnected(flag_login);

        iniciarCampos(view);

        if (flag_online) {
            consultarTipoVoo_Online("");
        } else {
            consultarTipoVoo_Offline("");
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
        w_pesquisa_tipo_voo = view.findViewById(R.id.edit_text_pesquisar_tipo_voo);
        w_pesquisa_tipo_voo.setHint("Pesquisar Tipo de Voo");

        w_botao_pesquisa = view.findViewById(R.id.card_view_pesquisar_tipo_voo);

        w_tipo_voo = view.findViewById(R.id.recycler_view_tipo_voo);

        lista_tipo_voo = new ArrayList<Tipo_voo>();

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

                String texto_pesquisa = w_pesquisa_tipo_voo.getText().toString();

                if (flag_online) {
                    consultarTipoVoo_Online(texto_pesquisa);
                } else {
                    consultarTipoVoo_Offline(texto_pesquisa);
                }
            }
        });
    }

    //Faz a consulta no banco Firebase para filtrar a lista de tipos de voo
    private void consultarTipoVoo_Online(final String texto_pesquisa) {
        dialogPesquisa.setMessage("Processando,\npor favor aguarde...");
        dialogPesquisa.show();

        referenceDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_tipo_voo.clear();

                for (DataSnapshot retorno : dataSnapshot.getChildren()) {
                    Tipo_voo tab_tipo_voo = retorno.getValue(Tipo_voo.class);
                    String cod_tip_voo = tab_tipo_voo.getCod_tip_voo().toString().trim();
                    String nom_tip_voo = tab_tipo_voo.getNom_tip_voo();

                    if ((validaPesquisa(texto_pesquisa, cod_tip_voo) == true) ||
                            (validaPesquisa(texto_pesquisa, nom_tip_voo) == true)){

                        lista_tipo_voo.add(tab_tipo_voo);

                    }

                }

                Collections.sort(lista_tipo_voo);

                tipo_vooAdapter = new TipoVooAdapter(getActivity(), getDialog(),
                        lista_tipo_voo, retornaTipo_voo_interface);
                w_tipo_voo.setAdapter(tipo_vooAdapter);
                tipo_vooAdapter.notifyDataSetChanged();

                dialogPesquisa.dismiss();

                if (lista_tipo_voo.isEmpty()) {
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
    private void consultarTipoVoo_Offline(final String texto_pesquisa) {

        BancoRegistroVoo bancoRegistroVoo = new BancoRegistroVoo(getActivity());

        ArrayList<Tipo_voo> retorno_tipo_voo = bancoRegistroVoo.getTipo_voo();

        lista_tipo_voo.clear();

        for (int i = 0; i < retorno_tipo_voo.size(); i++) {
            Tipo_voo tab_tipo_voo = new Tipo_voo();

            String cod_tip_voo = retorno_tipo_voo.get(i).getCod_tip_voo().toString();
            String nom_tip_voo = retorno_tipo_voo.get(i).getNom_tip_voo();

            tab_tipo_voo.setCod_tip_voo(retorno_tipo_voo.get(i).getCod_tip_voo());
            tab_tipo_voo.setNom_tip_voo(retorno_tipo_voo.get(i).getNom_tip_voo());

            if ((validaPesquisa(texto_pesquisa, cod_tip_voo) == true) ||
                    (validaPesquisa(texto_pesquisa, nom_tip_voo) == true)) {

                lista_tipo_voo.add(tab_tipo_voo);

            }
        }

        Collections.sort(lista_tipo_voo);

        tipo_vooAdapter = new TipoVooAdapter(getActivity(), getDialog(),
                lista_tipo_voo, retornaTipo_voo_interface);
        w_tipo_voo.setAdapter(tipo_vooAdapter);
        tipo_vooAdapter.notifyDataSetChanged();

        if (lista_tipo_voo.isEmpty()) {
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