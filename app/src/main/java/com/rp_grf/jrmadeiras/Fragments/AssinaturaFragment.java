package com.rp_grf.jrmadeiras.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoAssinatura;
import com.rp_grf.jrmadeiras.Telas.Programas.MOB006_A;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.dmoral.toasty.Toasty;

/**
 * Autor: André Castro
 */
public class AssinaturaFragment extends DialogFragment {

    public AssinaturaFragment() {
        // Required empty public constructor
    }

    public static final int READ_REQUEST_CODE = 107;
    public static final int WRITE_REQUEST_CODE = 108;

    Boolean flag_cancelado = false;

    File imagemAssinatura;

    String caminhoAtual;

    String nomeArquivo;
    Uri uriConteudo;

    SignaturePad w_signaturePad;

    CardView w_btn_salvar;
    CardView w_btn_limpar;

    TextView w_txt_salvar;
    TextView w_txt_limpar;
    TextView w_txt_confirmar;

    String codigo_registro;
    String texto_confirmacao;

    String cancelamento;

    public static AssinaturaFragment newInstance(String title) {
        AssinaturaFragment fragment = new AssinaturaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        pedirPermissao();

        Bundle bundle = getArguments();
        codigo_registro = bundle.getString("codigo", "");
        texto_confirmacao = bundle.getString("confirmacao", "");
        nomeArquivo = bundle.getString("nome", "");
        cancelamento = bundle.getString("cancelamento", "");

        this.flag_cancelado = Boolean.parseBoolean(cancelamento);

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_assinatura, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iniciarCampos(view);

        cliqueBotao();

        habilitarBotao(false);
        if (flag_cancelado){
            botaoCancelar(false);
        }

        w_signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {
                habilitarBotao(true);
                if (flag_cancelado){
                    botaoCancelar(true);
                }
            }

            @Override
            public void onClear() {
                habilitarBotao(false);
                if (flag_cancelado){
                    botaoCancelar(false);
                }
            }
        });

    }

    private void iniciarCampos(View view) {
        w_signaturePad = (SignaturePad) view.findViewById(R.id.signature_pad_assinatura);

        w_btn_salvar = (CardView) view.findViewById(R.id.card_view_botao_salvar_assinatura);
        w_btn_limpar = (CardView) view.findViewById(R.id.card_view_botao_limpar_assinatura);

        w_txt_salvar = (TextView) view.findViewById(R.id.text_view_salvar_assinatura);
        w_txt_limpar = (TextView) view.findViewById(R.id.text_view_limpar_assinatura);

        w_txt_confirmar = (TextView) view.findViewById(R.id.text_view_confirmar_assinatura);
        w_txt_confirmar.setText(texto_confirmacao);

        if (this.flag_cancelado) {
            w_txt_confirmar.setTextColor(getResources().getColor(R.color.dialog_error));
        }

    }

    private void cliqueBotao() {
        w_btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getDialog().dismiss();

                Bitmap bitmap = w_signaturePad.getSignatureBitmap();

                try {
                    criarArquivoAssinatura();
                    gravarAssinatura(bitmap);

                    BancoAssinatura bancoAssinatura = new BancoAssinatura(getContext());
                    bancoAssinatura.setAssinatura(
                            codigo_registro,
                            nomeArquivo,
                            String.valueOf(uriConteudo)
                    );

                    if (flag_cancelado) {
                        ((MOB006_A) getActivity()).cancelarRomaneioNuvem();
                    } else {
                        ((MOB006_A) getActivity()).enviarAssinaturas();
                    }

                    Toasty.success(getActivity(),
                            "Assinatura salva!",
                            Toast.LENGTH_SHORT, true).show();

                    getDialog().dismiss(); //fecha o fragment

                } catch (Exception ex) {
                    pedirPermissao();
                    ex.printStackTrace();
                    Toasty.error(getActivity(),
                            "Erro ao salvar assinatura!\nVerifique as permissões do aplicativo.",
                            Toast.LENGTH_SHORT, true).show();
                }

            }
        });

        w_btn_limpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                w_signaturePad.clear();
            }
        });
    }

    private void criarArquivoAssinatura() throws Exception {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String imageFileName = nomeArquivo + "RPSYSTEM" + "_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".bmp",  /* suffix */
                storageDir     /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        caminhoAtual = image.getAbsolutePath();

        imagemAssinatura = image;
    }

    private void gravarAssinatura(Bitmap imagem) throws Exception {

        try {

            File signatureFile = null;

            signatureFile = imagemAssinatura;

            if (signatureFile != null) {

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

                Uri signatureUri = FileProvider.getUriForFile(getContext(),
                        "com.rp_grf.jrmadeiras.fileprovider",
                        signatureFile);

                mediaScanIntent.setData(signatureUri);
                getActivity().sendBroadcast(mediaScanIntent);

                nomeArquivo = signatureFile.getName();
                uriConteudo = signatureUri;

                FileOutputStream fileOutputStream = new FileOutputStream(signatureFile);

                imagem.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                fileOutputStream.close();

            }

        } catch (Exception ex) {
            pedirPermissao();
            ex.printStackTrace();
            System.out.println("Gravar Assinatura: " + ex.toString());
        }
    }

    private void pedirPermissao() {

        System.out.println("pedir permissao\n");
        System.out.println(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE));
        System.out.println(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE));
        System.out.println(PackageManager.PERMISSION_GRANTED);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_REQUEST_CODE);
        } else if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_REQUEST_CODE);
        }
    }

    private void habilitarBotao(boolean flag) {

        w_btn_salvar.setEnabled(flag);
        w_btn_limpar.setEnabled(flag);

        if (flag == true) {
            w_btn_salvar.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            w_btn_limpar.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));

            w_txt_salvar.setTextColor(getResources().getColor(R.color.white));
            w_txt_limpar.setTextColor(getResources().getColor(R.color.white));
        } else {
            w_btn_salvar.setCardBackgroundColor(getResources().getColor(R.color.cinza_claro));
            w_btn_limpar.setCardBackgroundColor(getResources().getColor(R.color.cinza_claro));

            w_txt_salvar.setTextColor(getResources().getColor(R.color.black));
            w_txt_limpar.setTextColor(getResources().getColor(R.color.black));
        }

    }

    private void botaoCancelar(boolean habilitado){

        w_txt_salvar.setText("Cancelar");

        if (habilitado == true){
            w_btn_salvar.setCardBackgroundColor(getResources().getColor(R.color.dialog_error));
        } else {
            w_btn_salvar.setCardBackgroundColor(getResources().getColor(R.color.cinza_claro));
        }


    }

}