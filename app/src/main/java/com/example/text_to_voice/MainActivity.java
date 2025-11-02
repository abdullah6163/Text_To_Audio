package com.example.text_to_voice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText et;
    Button bt;
    TextToSpeech textToSpeech;
    LottieAnimationView lottieAnim;
    ImageView logoImage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        et = findViewById(R.id.et);
        bt = findViewById(R.id.bt);
        lottieAnim = findViewById(R.id.lottieAnim);
        logoImage = findViewById(R.id.logoImage);

        textToSpeech = new TextToSpeech(MainActivity.this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.ENGLISH);
            }
        });

        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                runOnUiThread(() -> {
                    logoImage.setVisibility(View.GONE);
                    lottieAnim.setVisibility(View.VISIBLE);
                    lottieAnim.playAnimation();
                });
            }

            @Override
            public void onDone(String utteranceId) {
                runOnUiThread(() -> {
                    lottieAnim.pauseAnimation();
                    lottieAnim.setVisibility(View.GONE);
                    logoImage.setVisibility(View.VISIBLE);
                });
            }

            @Override
            public void onError(String utteranceId) {
                runOnUiThread(() -> {
                    lottieAnim.pauseAnimation();
                    lottieAnim.setVisibility(View.GONE);
                    logoImage.setVisibility(View.VISIBLE);
                });
            }
        });

        bt.setOnClickListener(v -> {
            String text = et.getText().toString();
            if (!text.isEmpty()) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1");
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
