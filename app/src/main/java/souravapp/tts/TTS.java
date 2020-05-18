package souravapp.tts;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by SOURAV on 1/27/2017.
 */
public class TTS extends Activity {
    TextToSpeech tts;
    EditText text;
    Button spk,del,cpy,mic;
    Spinner Language_Select;
    int Language;
    SeekBar Pitch,Volume,Speed;
    AudioManager audioManager;
    private final int SPEECH_RECOGNITION_CODE = 1;
    InterstitialAd interstitialAd;
    AdView mAdview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tts);
        mAdview =(AdView)findViewById(R.id.ad);
        AdRequest adRequest= new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_fullscreen));

        adRequest = new AdRequest.Builder().build();

        interstitialAd.loadAd(adRequest);

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if(interstitialAd.isLoaded()){
                    interstitialAd.show();
                }
            }
        });


        text=(EditText)findViewById(R.id.editText);

        spk=(Button)findViewById(R.id.spk);
        del=(Button)findViewById(R.id.del);
        cpy=(Button)findViewById(R.id.copy);
        mic=(Button)findViewById(R.id.speech_to_text);


        Language_Select=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.languages,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Language_Select.setAdapter(adapter);

        Pitch=(SeekBar)findViewById(R.id.pitchBar);
        Pitch.setProgress(50);

        Speed=(SeekBar)findViewById(R.id.speedBar);
        Speed.setProgress(50);

        Volume=(SeekBar)findViewById(R.id.volumeBar);


        audioManager =(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        Volume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        Volume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        Pitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double pitch =Pitch.getProgress()+1;
                pitch= pitch/25;
                tts.setPitch((float)pitch);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double speed =Speed.getProgress()+1;
                speed= speed/30;
                tts.setSpeechRate((float)speed);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,(progress),0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
            }
        });

        tts =new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=tts.ERROR){
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(text.getText().toString().length()==0) {
                    Toast.makeText(getApplicationContext(), "Nothing to Delete!!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    text.setText("");
                    Toast.makeText(getApplicationContext(), "Text Deleted!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(text.getText().toString().length()==0) {
                    Toast.makeText(getApplicationContext(), "Nothing to Speak!!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    String tospeak = text.getText().toString();

                    switch (Language_Select.getSelectedItemPosition()){
                        case 0:
                            tts.setLanguage(Locale.ENGLISH);
                            break;
                        case 1:
                            tts.setLanguage(Locale.UK);
                            break;
                        case 2:
                            tts.setLanguage(new Locale("es","ES"));
                            break;
                        case 3:
                            tts.setLanguage(Locale.FRENCH);
                            break;
                        case 4:
                            tts.setLanguage(Locale.GERMAN);
                            break;
                        case 5:
                            tts.setLanguage(Locale.ITALIAN);
                            break;
                        case 6:
                            tts.setLanguage(new Locale("ru","RU"));
                            break;
                        case 7:
                            tts.setLanguage(new Locale("hi","IN"));
                            break;

                        default:
                            tts.setLanguage(Locale.ENGLISH);
                            break;
                    }
                    tts.speak(tospeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        cpy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.getText().toString().length()==0) {
                    Toast.makeText(getApplicationContext(), "Nothing to Copy!!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    ClipboardManager clipbrd = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip =ClipData.newPlainText("TTS text",text.getText().toString());
                    clipbrd.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Text Copied to Clipboard!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(tts !=null){
            tts.stop();
        }
    }

    @Override
    protected void onDestroy() {

        if(tts !=null){
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    /**
     * Start speech to text intent. This opens up Google Speech Recognition API dialog box to listen the speech input.
     * */
    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak something...");
        try {
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Callback for speech recognition activity
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    text.setText(text.getText().toString()+" "+result.get(0));
                }
                break;
            }
        }
    }
}