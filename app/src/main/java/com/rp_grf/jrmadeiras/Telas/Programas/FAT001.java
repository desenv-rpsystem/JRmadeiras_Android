package com.rp_grf.jrmadeiras.Telas.Programas;

import androidx.appcompat.app.AppCompatActivity;
import com.rp_grf.jrmadeiras.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FAT001 extends AppCompatActivity {

    Button w_botao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fat100);

        w_botao = (Button) findViewById(R.id.btn_mob002);

        w_botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MOB002.class);
                startActivity(intent);
            }
        });
    }
}
