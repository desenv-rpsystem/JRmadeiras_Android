package com.rp_grf.jrmadeiras.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rp_grf.jrmadeiras.R;
import com.rp_grf.jrmadeiras.SQLite.BancoAgendaTemp;
import com.rp_grf.jrmadeiras.SQLite.BancoFotos;
import com.rp_grf.jrmadeiras.Telas.Programas.MOB004;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.dmoral.toasty.Toasty;

/**
 * Autor: André Castro
 */
public class TirarFotoFragment extends DialogFragment {
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;

    public static final int READ_REQUEST_CODE = 107;
    public static final int WRITE_REQUEST_CODE = 108;

    //Firebase - Storage (Banco de imagens)
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    String codigo_registro;

    String caminhoAtual;

    String nomeArquivo;
    Uri uriConteudo;

    ImageView w_imageview_foto;
    ImageView w_imageview_salvar;

    CardView w_btn_fechar;
    CardView w_btn_tirar_foto;
    CardView w_btn_galeria;
    CardView w_btn_salvar;

    public TirarFotoFragment() {
        // Required empty public constructor
    }

    public static TirarFotoFragment newInstance(String title) {
        TirarFotoFragment fragment = new TirarFotoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tirar_foto, container, false);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        Bundle bundle = getArguments();
        codigo_registro = bundle.getString("codigo", "");
        nomeArquivo = bundle.getString("nome", "");

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

        w_imageview_foto = (ImageView) view.findViewById(R.id.image_view_fragment_foto);
        w_imageview_salvar = (ImageView) view.findViewById(R.id.image_view_foto_salvar);

        w_btn_fechar = (CardView) view.findViewById(R.id.card_view_botao_cancelar_foto);
        w_btn_tirar_foto = (CardView) view.findViewById(R.id.card_view_botao_tirar_foto);
        w_btn_galeria = (CardView) view.findViewById(R.id.card_view_botao_galeria_foto);
        w_btn_salvar = (CardView) view.findViewById(R.id.card_view_botao_salvar_foto);

        w_btn_salvar.setEnabled(false);

        cliqueBotao();

        String title = getArguments().getString("title", "");
        getDialog().setTitle(title);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    private void cliqueBotao() {
        w_btn_fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss(); //fecha o fragment
            }
        });

        w_btn_tirar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pedirPermissaoCamera();
            }
        });

        w_btn_galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

        w_btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //enviarFotoTirada(nomeArquivo, uriConteudo);
                salvarCaminhoFoto(nomeArquivo, uriConteudo);
            }
        });
    }

    private void abrirCamera() {

        pedirPermissaoCamera();


        //Executa a camera do dispositivo - Rafael
        /*
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
         */
    }

    private void pedirPermissaoCamera() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_REQUEST_CODE);
        } else if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_REQUEST_CODE);
        } else {
            tirarFoto();
        }
    }

    //Método que verifica as permissões da Câmera
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Boolean flag_permissao_camera = false;
        Boolean flag_permissao_read = false;
        Boolean flag_permissao_write = false;

        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                flag_permissao_camera = true;
            }
        } else if (requestCode == READ_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                flag_permissao_read = true;
            }
        } else if (requestCode == WRITE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                flag_permissao_write = true;
            }
        }

        if (flag_permissao_camera == true && flag_permissao_read == true && flag_permissao_write == true) {
            tirarFoto();
        } else {
            alertMessage("Para usar a câmera, é necessário conceder permissão!");
        }
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(contentUri));
    }

    private void tirarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = criarArquivoFoto();
        } catch (Exception ex) {
            System.out.println("Tirar Foto: " + ex.toString());
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(getContext(),
                    "com.rp_grf.jrmadeiras.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
        }
    }

    private File criarArquivoFoto() throws Exception {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String imageFileName = nomeArquivo + "RPSYSTEM" + "_" + timeStamp;
//      File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir     /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        caminhoAtual = image.getAbsolutePath();
        return image;
    }

    private void salvarCaminhoFoto(String imageFileName, Uri contentUri) {
        BancoFotos bancoFotos = new BancoFotos(getContext());

        bancoFotos.setFotos(codigo_registro, imageFileName, String.valueOf(contentUri));

        System.out.println("salvarCaminhoFoto " + codigo_registro);

        Toasty.success(getActivity(),
                "Foto salva!",
                Toast.LENGTH_SHORT, true).show();
        getDialog().dismiss(); //fecha o fragment
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //chama se for da camera
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                File arquivo = new File(caminhoAtual);
                w_imageview_foto.setImageURI(Uri.fromFile(arquivo));
                w_imageview_foto.setPadding(0, 0, 0, 0);

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(arquivo);
                mediaScanIntent.setData(contentUri);
                getActivity().sendBroadcast(mediaScanIntent);

                nomeArquivo = arquivo.getName();
                uriConteudo = contentUri;

                habilitarBotao();
            }
        }

        //chama se for da galeria
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "RPSYSTEM_" + timeStamp + "." + getFileExt(contentUri);
                w_imageview_foto.setImageURI(contentUri);

                nomeArquivo = nomeArquivo + imageFileName;
                uriConteudo = contentUri;

                habilitarBotao();
            }

        }

    }

    private void habilitarBotao() {
        w_btn_salvar.setEnabled(true);

        w_btn_salvar.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        w_imageview_salvar.setImageResource(R.drawable.icone_salvar_branco);
    }

    private void alertMessage(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Atenção");
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }

    //////////////////////

    private void enviarFotoTirada(String imageFileName, Uri contentUri) {
        final StorageReference banco = storageRef.child("fotos/" + imageFileName);
        banco.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                banco.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toasty.success(getActivity(),
                                "Foto enviada para a nuvem!",
                                Toast.LENGTH_SHORT, true).show();
                        getDialog().dismiss(); //fecha o fragment
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