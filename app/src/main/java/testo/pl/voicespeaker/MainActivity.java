package testo.pl.voicespeaker;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;


public class MainActivity extends ActionBarActivity implements TextToSpeech.OnInitListener {
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    private static int currentLanguage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupClickListeners();
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

    }

    public void setupClickListeners() {
        setupSpeakClickListener();
        setupChangeLanguageListener();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                myTTS = new TextToSpeech(this, this);
            } else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    private void setupChangeLanguageListener() {
        Button button = (Button) findViewById(R.id.switcher);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLanguage = (currentLanguage + 1) % 10;
                setCurrentLanguage(myTTS, currentLanguage);
            }
        });
    }

    private void setCurrentLanguage(TextToSpeech myTTS, int lang) {
        String chosenLanguage;
        switch (lang) {
            case 1: {
                myTTS.setLanguage(Locale.UK);
                chosenLanguage = "UK";
                break;
            }
            case 2: {
                myTTS.setLanguage(Locale.US);
                chosenLanguage = "US";
                break;
            }
            case 3: {
                myTTS.setLanguage(Locale.CHINESE);
                chosenLanguage = "Chinese";
                break;
            }
            case 4: {
                myTTS.setLanguage(Locale.FRENCH);
                chosenLanguage = "French";
                break;
            }
            case 5: {
                myTTS.setLanguage(Locale.GERMAN);
                chosenLanguage = "German";
                break;
            }
            case 6: {
                myTTS.setLanguage(Locale.JAPANESE);
                chosenLanguage = "Japanese";
                break;
            }
            default: {
                myTTS.setLanguage(Locale.getDefault());
                chosenLanguage = "Default language";
                break;
            }

        }
        Toast.makeText(this, "Chosen language is " + chosenLanguage, Toast.LENGTH_SHORT).show();
    }


    private void setupSpeakClickListener() {
        Button button = (Button) findViewById(R.id.button);
        final EditText edittext = (EditText) findViewById(R.id.edittext);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToRead = edittext.getText().toString();
                speakWords(textToRead);
            }
        });
    }

    private void speakWords(String textToRead) {
        myTTS.speak(textToRead, TextToSpeech.QUEUE_FLUSH, null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            myTTS.setLanguage(Locale.US);
        } else if (status == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }
}
