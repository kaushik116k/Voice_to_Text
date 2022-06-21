package com.example.voice_text;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextToSpeech t1;
    TextView e1;
    TextView e2;
    ImageButton b1, b2;
    static  final int REQUEST_CODE_SPEECH_INPUT = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1 = findViewById(R.id.edit_text1);
        b1 = findViewById(R.id.button1);

        e2 = findViewById(R.id.edit_text2);
        b2 = findViewById(R.id.button2);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    t1.setLanguage(Locale.getDefault());
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toSpeak = e1.getText().toString();
                Toast.makeText(MainActivity.this, toSpeak, Toast.LENGTH_SHORT).show();
                t1.speak(toSpeak, TextToSpeech.QUEUE_ADD, null);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voice();
            }
        });
    }

    private void voice() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Something");

        try{
            startActivityForResult(i,REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:{
                if(resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    e2.setText(result.get(0));
                    String val = e2.getText().toString();
                    if(val.equals("4")){
                        Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
                        t1.speak("correct", TextToSpeech.QUEUE_ADD, null);
                    } else{
                        Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show();
                        t1.speak("incorrect", TextToSpeech.QUEUE_ADD, null);
                    }
                }
                break;
            }
        }
    }
}