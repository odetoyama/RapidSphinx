package com.icaksama.rapidsphinxdemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;package com.icaksama.rapidsphinx;

        import android.Manifest;
        import android.app.ProgressDialog;
        import android.content.pm.PackageManager;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;

        import java.util.List;

public class MainActivity extends AppCompatActivity implements RapidSphinxListener {

    private RapidSphinx rapidSphinx;
    private Button btnRecognizer;
    private Button btnSync;
    private EditText editText;
    private TextView txtResult;
    private TextView txtStatus;
    private TextView txtPartialResult;
    private TextView txtUnsupported;

    private ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rapidSphinx = new RapidSphinx(this);
        rapidSphinx.addListener(this);
        rapidSphinx.setSilentToDetect(2000);

        this.requestPermissions();

        editText = (EditText) findViewById(R.id.editText);
        txtResult = (TextView) findViewById(R.id.txtResult);
        txtPartialResult = (TextView) findViewById(R.id.txtPartialResult);
        txtUnsupported = (TextView) findViewById(R.id.txtUnsuported);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        btnSync = (Button) findViewById(R.id.btnSync);
        btnRecognizer = (Button) findViewById(R.id.btnRecognizer);
        txtStatus.setText("Preparing data!");

        // Disable buttons for first time
        btnSync.setEnabled(false);
        btnRecognizer.setEnabled(false);

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
//                btnSync.setEnabled(false);
                btnRecognizer.setEnabled(false);
                rapidSphinx.updateVocabulary(editText.getText().toString(), new RapidSphinxCompletionListener() {
                    @Override
                    public void rapidSphinxCompletedProcess() {
                        btnRecognizer.setEnabled(true);
                        dialog.dismiss();
                    }
                });
            }
        });

        btnRecognizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtStatus.setText("");
                btnSync.setEnabled(false);
                btnRecognizer.setEnabled(false);
//                rapidSphinx.updateVocabulary(editText.getText().toString());
                rapidSphinx.startRapidSphinx(10000);
                txtStatus.setText("Speech NOW!");
            }
        });

        rapidSphinx.prepareRapidSphinx(new RapidSphinxCompletionListener() {
            @Override
            public void rapidSphinxCompletedProcess() {
                dialog = ProgressDialog.show(MainActivity.this, "",
                        "Loading. Please wait...", true);
                btnSync.setEnabled(true);
                btnRecognizer.setEnabled(false);
                txtStatus.setText("RapidSphinx ready!");
                dialog.dismiss();
            }
        });
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    1);
        }
    }

    @Override
    public void rapidSphinxDidStop(String reason, int code) {
        btnSync.setEnabled(true);
        btnRecognizer.setEnabled(true);
        if (code == 500) { // 200 code for error
            System.out.println(reason);
        } else if (code == 522) { // 200 code for timed out
            System.out.println(reason);
        } else if (code == 200) { // 200 code for finish speech
            System.out.println(reason);
        }
    }

    @Override
    public void rapidSphinxFinalResult(String result, List<String> arrHypo, List<Double> scores) {
        txtResult.setText(result);
        if (result.equalsIgnoreCase(editText.getText().toString())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                txtResult.setTextColor(getResources().getColor(android.R.color.holo_green_light, null));
            } else {
                txtResult.setTextColor(getResources().getColor(android.R.color.holo_green_light));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                txtResult.setTextColor(getResources().getColor(android.R.color.holo_red_light, null));
            } else {
                txtResult.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }
        }
    }

    @Override
    public void rapidSphinxPartialResult(String partialResult) {
        txtPartialResult.setText(partialResult);
    }

    @Override
    public void rapidSphinxUnsupportedWords(List<String> words) {
        String unsupportedWords = "";
        for (String word: words) {
            unsupportedWords += word + ", ";
        }
        txtUnsupported.setText("Unsupported words : \n" + unsupportedWords);
    }

    @Override
    public void rapidSphinxDidSpeechDetected() {
        txtStatus.setText("Speech detected!");
    }
}
