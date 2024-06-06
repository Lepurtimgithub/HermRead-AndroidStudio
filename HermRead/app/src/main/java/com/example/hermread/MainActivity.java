package com.example.hermread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private LinearLayout loginLayout;
    private LinearLayout mainLayout;
    private LinearLayout hizAyarLayout;
    private EditText username;
    private EditText password;
    private Button loginButton;
    private Button baslatButton;
    private Button geriButton;
    private Button ileriButton;
    private SeekBar hizAyar;
    private TextView kelimeGoster;
    private String[] kelimeler = {"Hızlı", "Okuma", "Uygulaması", "Hoş", "Geldiniz","Hızlı", "Okuma", "Uygulaması", "Hoş", "Geldiniz","Hızlı", "Okuma", "Uygulaması", "Hoş", "Geldiniz"};
    private int kelimeIndex = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private int hiz = 500; // Varsayılan hız (ms)
    private Runnable kelimeGosterici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginLayout = findViewById(R.id.login_layout);
        mainLayout = findViewById(R.id.layout_main);
        hizAyarLayout = findViewById(R.id.hiz_ayar_layout);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        baslatButton = findViewById(R.id.baslatButton);
        geriButton = findViewById(R.id.geriButton);
        ileriButton = findViewById(R.id.ileriButton);
        hizAyar = findViewById(R.id.hizAyar);
        kelimeGoster = new TextView(this);
        mainLayout.addView(kelimeGoster);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Giriş kontrolü (örnek)
                if (username.getText().toString().equals("user") && password.getText().toString().equals("pass")) {
                    loginLayout.setVisibility(View.GONE);
                    mainLayout.setVisibility(View.VISIBLE);
                    baslatButton.setVisibility(View.VISIBLE);
                    hizAyarLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        baslatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baslatButton.setVisibility(View.GONE);
                startReading();
            }
        });

        geriButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kelimeIndex > 0) kelimeIndex--;
                updateWord();
            }
        });

        ileriButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kelimeIndex < kelimeler.length - 1) kelimeIndex++;
                updateWord();
            }
        });

        hizAyar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hiz = 500 - (progress * 50);
                if (kelimeGosterici != null) {
                    handler.removeCallbacks(kelimeGosterici);
                    startReading();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void startReading() {
        kelimeIndex = 0;
        kelimeGosterici = new Runnable() {
            @Override
            public void run() {
                updateWord();
                if (kelimeIndex < kelimeler.length - 1) {
                    kelimeIndex++;
                    handler.postDelayed(this, hiz);
                }
            }
        };
        handler.postDelayed(kelimeGosterici, hiz);
    }

    private void updateWord() {
        if (kelimeIndex < kelimeler.length) {
            String kelime = kelimeler[kelimeIndex];
            int ortalama = kelime.length() / 2;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < kelime.length(); i++) {
                if (i == ortalama) {
                    sb.append("<font color='red'>").append(kelime.charAt(i)).append("</font>");
                } else {
                    sb.append(kelime.charAt(i));
                }
            }
            kelimeGoster.setText(Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY));
        }
    }
}
