package com.icaksama.rapidsphinx;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.NGramModel;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.Segment;
import edu.cmu.pocketsphinx.SegmentIterator;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

/**
 * Created by icaksama on 16/11/17.
 */

public class RapidSphinx implements RecognitionListener {

    // Basic Setting
    private String ARPA_SEARCH_ID = "icaksama";
    private SpeechRecognizer speechRecognizer;
    private Context context;
    private String[] words = null;
    private Utilities util = new Utilities();
    private RapidSphinxListener rapidSphinxListener = null;
    private File assetDir = null;

    // Additional Setting
    private boolean rawLogAvailable = false;
    private boolean allPhoneCI = false;
    private boolean plWindow = false;
    private boolean lPonlyBeam = false;
    private float vadThreshold = (float) 3.0;
    private int maxWPF = 5;
    private int maxHMMPF = 3000;
    private long silentToDetect = 0;
    private long timeOutAfterSpeech = 0;
    private List<String> unsupportedWords = new ArrayList<String>();
    private List<Double> scores = new ArrayList<Double>();

    public RapidSphinx(Context context) {
        this.context = context;
        try {
            Assets assetsContext = new Assets(context);
            assetDir = assetsContext.syncAssets();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void prepareRapidSphinx(final RapidSphinxCompletionListener rapidSphinxCompletionListener) {
        if (ContextCompat.checkSelfPermission(this.context,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Perrmission record not granted!");
            return;
        }
        new AsyncTask<Void, Void, Exception>(){
            @Override
            protected void onPreExecute() {
                System.out.println("Preparing RapidSphinx!");
            }
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assetsDir = new Assets(context);
                    File assetDir = assetsDir.syncAssets();
                    speechRecognizer = SpeechRecognizerSetup.defaultSetup()
                            .setAcousticModel(new File(assetDir, "en-us-ptm"))
                            .setDictionary(new File(assetDir, "cmudict-en-us.dict"))
//                            .setBoolean("-allphone_ci", allPhoneCI)
//                            .setFloat("-vad_threshold", vadThreshold)
//                            .setInteger("-maxwpf", maxWPF)
//                            .setInteger("-maxhmmpf", maxHMMPF)
                            .setFloat("-samprate", 16000)
//                            .setBoolean("-pl_window", plWindow)
//                            .setBoolean("-lponlybeam", lPonlyBeam)
                            .getRecognizer();
                    if (rawLogAvailable) {
                        speechRecognizer.getDecoder().getConfig().setString("-rawlogdir", assetDir.getPath());
                    }
                    speechRecognizer.addListener(RapidSphinx.this);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Exception e) {
                System.out.println("RapidSphinx is ready!");
                if (rapidSphinxCompletionListener != null) {
                    rapidSphinxCompletionListener.rapidSphinxCompletedProcess();
                }
            }
        }.execute();
    }

    public void startRapidSphinx(int timeOut) {
        if (ContextCompat.checkSelfPermission(this.context,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Perrmission record not granted!");
        } else {
            scores.clear();
            speechRecognizer.startListening(ARPA_SEARCH_ID, timeOut);
        }
    }

    public void updateVocabulary(final String words, final RapidSphinxCompletionListener rapidSphinxCompletionListener) {
        new AsyncTask<Void, Void, Exception>(){
            @Override
            protected void onPreExecute() {
                System.out.println("Updating vocabulary!");
            }
            @Override
            protected Exception doInBackground(Void... params) {
//                RapidSphinx.this.words = ;
                generateDictonary(new HashSet<String>(Arrays.asList(words.split(" "))).toArray(new String[0]));
                // Use this for instance
                speechRecognizer.getDecoder().setLm(ARPA_SEARCH_ID,
                        generateNGramModel(new HashSet<String>(Arrays.asList(words.split(" "))).toArray(new String[0])));
                // Use this with file
//              speechRecognizer.addNgramSearch("icaksama", newFile);
                return null;
            }
            @Override
            protected void onPostExecute(Exception e) {
                System.out.println("Vocabulary updated!");
                if (rapidSphinxCompletionListener != null) {
                    rapidSphinxCompletionListener.rapidSphinxCompletedProcess();
                }
                if (rapidSphinxListener != null) {
                    rapidSphinxListener.rapidSphinxUnsupportedWords(unsupportedWords);
                }
            }
        }.execute();
    }


    public void updateVocabulary(final String[] words, final RapidSphinxCompletionListener rapidSphinxCompletionListener) {
        new AsyncTask<Void, Void, Exception>(){
            @Override
            protected void onPreExecute() {
                System.out.println("Updating vocabulary!");
            }
            @Override
            protected Exception doInBackground(Void... params) {
                RapidSphinx.this.words = new HashSet<String>(Arrays.asList(words)).toArray(new String[0]);
                generateDictonary(RapidSphinx.this.words);
                // Use this for instance
                speechRecognizer.getDecoder().setLm(ARPA_SEARCH_ID, generateNGramModel(RapidSphinx.this.words));
                // Use this with file
//              speechRecognizer.addNgramSearch("icaksama", newFile);
                return null;
            }
            @Override
            protected void onPostExecute(Exception e) {
                System.out.println("Vocabulary updated!");
                rapidSphinxCompletionListener.rapidSphinxCompletedProcess();
            }
        }.execute();
    }

    private void generateDictonary(String[] words) {
        unsupportedWords.clear();
        try {
            File fileOut = new File(assetDir, "arpas/" + ARPA_SEARCH_ID + ".dict");
            File fullDic = new File(assetDir, "cmudict-en-us.dict");
            speechRecognizer.getDecoder().loadDict(fullDic.getPath(), null, "dict");
            if (fileOut.exists()){
                fileOut.delete();
            }
            System.out.println("Words " + words.length);
            FileOutputStream outputStream = new FileOutputStream(fileOut);
            for (String word: words) {
                String pronoun = speechRecognizer.getDecoder().lookupWord(word);
                if (pronoun != null) {
                    String wordN = word + " ";
                    outputStream.write(wordN.toLowerCase(Locale.ENGLISH).getBytes(Charset.forName("UTF-8")));
                    outputStream.write(pronoun.getBytes(Charset.forName("UTF-8")));
                    outputStream.write(System.getProperty("line.separator").getBytes());
                } else {
                    unsupportedWords.add(word);
                }
            }
            outputStream.close();
            speechRecognizer.getDecoder().loadDict(fileOut.getPath(), null, "dict");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private NGramModel generateNGramModel(String[] words) {
        File oldFile = new File(assetDir, ARPA_SEARCH_ID + ".arpa");
        File newFile = new File(assetDir, "arpas/"+ ARPA_SEARCH_ID +"-new.arpa");
        try {
            if (newFile.exists()) {
                newFile.delete();
            }
            util.copyFile(oldFile, newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        NGramModel nGramModel = new NGramModel(speechRecognizer.getDecoder().getConfig(),
                speechRecognizer.getDecoder().getLogmath(), newFile.getPath());
        nGramModel.prob(words);
        for (String word: words) {
            nGramModel.addWord(word, 2);
        }
        return nGramModel;
    }

    public void addListener(RapidSphinxListener rapidSphinxListener) {
        this.rapidSphinxListener = rapidSphinxListener;
    }

    public void removeListener() {
        this.rapidSphinxListener = null;
    }

    public File getLanguageModelPath() {
        File lmFile = new File(assetDir, "arpas/"+ ARPA_SEARCH_ID +"-new.arpa");
        return lmFile;
    }

    public File getDictonaryFile() {
        File dictFile = new File(assetDir, ARPA_SEARCH_ID + ".dict");
        return dictFile;
    }

    public boolean isRawLogAvailable() {
        return rawLogAvailable;
    }

    public String getRawLogDirectory() {
        return assetDir.getPath();
    }

    public void setRawLogAvailable(boolean rawLogAvailable) {
        this.rawLogAvailable = rawLogAvailable;
    }

    public double getVadThreshold() {
        return vadThreshold;
    }

    public void setVadThreshold(float vadThreshold) {
        this.vadThreshold = vadThreshold;
    }

    public double getSilentToDetect() {
        return silentToDetect;
    }

    public void setSilentToDetect(long silentToDetect) {
        this.silentToDetect = silentToDetect;
    }

    public int getMaxWPF() {
        return maxWPF;
    }

    public void setMaxWPF(int maxWPF) {
        this.maxWPF = maxWPF;
    }

    public int getMaxHMMPF() {
        return maxHMMPF;
    }

    public void setMaxHMMPF(int maxHMMPF) {
        this.maxHMMPF = maxHMMPF;
    }

    public boolean isPlWindow() {
        return plWindow;
    }

    public void setPlWindow(boolean plWindow) {
        this.plWindow = plWindow;
    }

    public boolean islPonlyBeam() {
        return lPonlyBeam;
    }

    public void setlPonlyBeam(boolean lPonlyBeam) {
        this.lPonlyBeam = lPonlyBeam;
    }

    public double getTimeOutAfterSpeech() {
        return timeOutAfterSpeech;
    }

    public void setTimeOutAfterSpeech(long timeOutAfterSpeech) {
        this.timeOutAfterSpeech = timeOutAfterSpeech;
    }

    @Override
    public void onBeginningOfSpeech() {
        if (rapidSphinxListener != null) {
            rapidSphinxListener.rapidSphinxDidSpeechDetected();
        }
    }

    @Override
    public void onEndOfSpeech() {
        if (silentToDetect > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(silentToDetect);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        speechRecognizer.stop();
        if (rapidSphinxListener != null) {
            rapidSphinxListener.rapidSphinxDidStop("End of Speech!", 200);
        }
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            if (rapidSphinxListener != null) {
                rapidSphinxListener.rapidSphinxPartialResult(hypothesis.getHypstr());
            }
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (rapidSphinxListener != null) {
            SegmentIterator segmentIterator = speechRecognizer.getDecoder().seg().iterator();
            while (segmentIterator.hasNext()) {
                Segment segment = segmentIterator.next();
                double score =  speechRecognizer.getDecoder().getLogmath().exp(segment.getProb());
                if (!segment.getWord().contains("<") && !segment.getWord().contains(">")) {
                    scores.add(score);
                }
            }
            if (rapidSphinxListener != null) {
                rapidSphinxListener.rapidSphinxFinalResult(hypothesis.getHypstr(), scores);
            }
        }
    }

    @Override
    public void onError(Exception e) {
        speechRecognizer.stop();
        if (rapidSphinxListener != null) {
            rapidSphinxListener.rapidSphinxDidStop(e.getMessage(), 500);
        }
    }

    @Override
    public void onTimeout() {
        speechRecognizer.stop();
        if (rapidSphinxListener != null) {
            rapidSphinxListener.rapidSphinxDidStop("Speech timed out!", 522);
        }
    }
}
