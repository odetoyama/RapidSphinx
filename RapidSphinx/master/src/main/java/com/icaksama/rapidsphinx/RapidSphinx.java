package com.icaksama.rapidsphinx;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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
import edu.cmu.pocketsphinx.Config;
import edu.cmu.pocketsphinx.Decoder;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.NGramModel;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.Segment;
import edu.cmu.pocketsphinx.SegmentIterator;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

/**
 * Created by icaksama on 16/11/17.
 */

public class RapidSphinx implements RecognitionListener {

    // Basic Setting
    private String SEARCH_ID = "icaksama";
    private RapidRecognizer rapidRecognizer;
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
    private boolean fwdflat = false;
    private boolean bestpath = false;
    private float vadThreshold = (float) 3.0;
    private float subvq = (float) 0.001;
    private int maxWPF = 5;
    private int maxHMMPF = 3000;
    private int maxcdsenpf = 2000;
    private long silentToDetect = 0;
    private long timeOutAfterSpeech = 0;
    private long sampleRate = 16000;
    private String pbeam = "1e-10";

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

    private void generateDictonary(String[] words) {
        unsupportedWords.clear();
        try {
            File fileOut = new File(assetDir, "arpas/" + SEARCH_ID + ".dict");
            File fullDic = new File(assetDir, "cmudict-en-us.dict");
            rapidRecognizer.getRapidDecoder().loadDict(fullDic.getPath(), null, "dict");
            if (fileOut.exists()){
                fileOut.delete();
            }
            System.out.println("Words " + words.length);
            FileOutputStream outputStream = new FileOutputStream(fileOut);
            for (String word: words) {
                String pronoun = rapidRecognizer.getRapidDecoder().lookupWord(word);
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
            rapidRecognizer.getRapidDecoder().loadDict(fileOut.getPath(), null, "dict");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateNGramModel(String[] words) {
        File oldFile = new File(assetDir, SEARCH_ID + ".arpa");
        File newFile = new File(assetDir, "arpas/"+ SEARCH_ID +"-new.arpa");
        try {
            if (newFile.exists()) {
                newFile.delete();
            }
            util.copyFile(oldFile, newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        NGramModel nGramModel = new NGramModel(rapidRecognizer.getRapidDecoder().getConfig(),
                rapidRecognizer.getRapidDecoder().getLogmath(), newFile.getPath());
        nGramModel.prob(words);
        for (String word: words) {
            nGramModel.addWord(word, 2);
        }
        rapidRecognizer.getRapidDecoder().setLm(SEARCH_ID, nGramModel);
    }

    public void prepareRapidSphinx(final RapidPreparationListener rapidPreparationListener) {
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
                    SpeechRecognizerSetup speechRecognizerSetup = SpeechRecognizerSetup.defaultSetup();
                    speechRecognizerSetup.setAcousticModel(new File(assetDir, "en-us-ptm"));
                    speechRecognizerSetup.setDictionary(new File(assetDir, "cmudict-en-us.dict"));
                    Config config = speechRecognizerSetup.getRecognizer().getDecoder().getConfig();
                    config.setFloat("-samprate", sampleRate);
                    config.setFloat("-vad_threshold", vadThreshold);
                    config.setFloat("-subvq", subvq);
                    config.setInt("-maxwpf", maxWPF);
                    config.setInt("-maxhmmpf", maxHMMPF);
                    config.setInt("-maxcdsenpf", maxcdsenpf);
                    config.setString("-ci_pbeam", pbeam);
                    config.setBoolean("-allphone_ci", allPhoneCI);
                    config.setBoolean("-fwdflat ", fwdflat);
                    config.setBoolean("-bestpath", bestpath);
                    config.setBoolean("-pl_window", plWindow);
                    config.setBoolean("-lponlybeam", lPonlyBeam);
                    rapidPreparationListener.rapidPreExecute(config);
                    if (rawLogAvailable) {
                        config.setString("-rawlogdir", assetDir.getPath());
                    }
                    rapidRecognizer = new RapidRecognizer(assetDir, config);
                    rapidRecognizer.addRapidListener(RapidSphinx.this);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    rapidPreparationListener.rapidPostExecute(false);
                }
                return null;
            }
            @Override
            protected void onPostExecute(Exception e) {
                System.out.println("RapidSphinx is ready!");
                if (rapidPreparationListener != null) {
                    rapidPreparationListener.rapidPostExecute(true);
                }
            }
        }.execute();
    }

    public void prepareRapidSphinx(final File gramOrLM, @Nullable final File dictionary, final RapidPreparationListener rapidPreparationListener) {
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
                    SpeechRecognizerSetup speechRecognizerSetup = SpeechRecognizerSetup.defaultSetup();
                    speechRecognizerSetup.setAcousticModel(new File(assetDir, "en-us-ptm"));
                    speechRecognizerSetup.setDictionary(new File(assetDir, "cmudict-en-us.dict"));
                    Config config = speechRecognizerSetup.getRecognizer().getDecoder().getConfig();
                    config.setFloat("-samprate", sampleRate);
                    config.setFloat("-vad_threshold", vadThreshold);
                    config.setFloat("-subvq", subvq);
                    config.setInt("-maxwpf", maxWPF);
                    config.setInt("-maxhmmpf", maxHMMPF);
                    config.setInt("-maxcdsenpf", maxcdsenpf);
                    config.setString("-ci_pbeam", pbeam);
                    config.setBoolean("-allphone_ci", allPhoneCI);
                    config.setBoolean("-fwdflat ", fwdflat);
                    config.setBoolean("-bestpath", bestpath);
                    config.setBoolean("-pl_window", plWindow);
                    config.setBoolean("-lponlybeam", lPonlyBeam);
                    rapidPreparationListener.rapidPreExecute(config);
                    if (rawLogAvailable) {
                        config.setString("-rawlogdir", assetDir.getPath());
                    }
                    if (gramOrLM.isFile()) {
                        if (gramOrLM.getPath().endsWith(".gram")) {
                            speechRecognizerSetup.setString("-jsgf", gramOrLM.getPath());
                        } else if (gramOrLM.getPath().endsWith(".arpa") ||
                                gramOrLM.getPath().endsWith(".lm") ||
                                gramOrLM.getPath().endsWith(".bin")||
                                gramOrLM.getPath().endsWith(".dmp")) {
                            speechRecognizerSetup.setString("-lm", gramOrLM.getPath());
                        } else {
                            Log.e("RapidSphinx", gramOrLM.getPath() + " is not a language model file.");
                        }
                    } else {
                        Log.e("RapidSphinx", gramOrLM.getPath() + " is not a file.");
                    }
                    rapidRecognizer = new RapidRecognizer(assetDir, config);
                    rapidRecognizer.addRapidListener(RapidSphinx.this);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    rapidPreparationListener.rapidPostExecute(false);
                }
                return null;
            }
            @Override
            protected void onPostExecute(Exception e) {
                System.out.println("RapidSphinx is ready!");
                if (rapidPreparationListener != null) {
                    rapidPreparationListener.rapidPostExecute(true);
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
            rapidRecognizer.startRapidListening(SEARCH_ID, timeOut);
        }
    }

    public void updateVocabulary(final String words, @Nullable final RapidCompletionListener rapidCompletionListener) {
        new AsyncTask<Void, Void, Exception>(){
            @Override
            protected void onPreExecute() {
                System.out.println("Updating vocabulary!");
            }
            @Override
            protected Exception doInBackground(Void... params) {
                generateDictonary(new HashSet<String>(Arrays.asList(words.split(" "))).toArray(new String[0]));
                generateNGramModel(new HashSet<String>(Arrays.asList(words.split(" "))).toArray(new String[0]));
                return null;
            }
            @Override
            protected void onPostExecute(Exception e) {
                System.out.println("Vocabulary updated!");
                if (rapidCompletionListener != null) {
                    rapidCompletionListener.rapidCompletedProcess();
                }
                if (rapidSphinxListener != null) {
                    rapidSphinxListener.rapidSphinxUnsupportedWords(unsupportedWords);
                }
            }
        }.execute();
    }

    public void updateVocabulary(final String[] words, @Nullable final RapidCompletionListener rapidCompletionListener) {
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected void onPreExecute() {
                System.out.println("Updating vocabulary!");
            }
            @Override
            protected Exception doInBackground(Void... params) {
                RapidSphinx.this.words = new HashSet<String>(Arrays.asList(words)).toArray(new String[0]);
                generateDictonary(RapidSphinx.this.words);
                generateNGramModel(RapidSphinx.this.words);
                return null;
            }
            @Override
            protected void onPostExecute(Exception e) {
                System.out.println("Vocabulary updated!");
                if (rapidCompletionListener != null) {
                    rapidCompletionListener.rapidCompletedProcess();
                }
                if (rapidSphinxListener != null) {
                    rapidSphinxListener.rapidSphinxUnsupportedWords(unsupportedWords);
                }
            }
        }.execute();
    }

    public void updateGrammar(final String grammarStr, @Nullable final File dictionary, @Nullable final RapidCompletionListener rapidCompletionListener) {
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected void onPreExecute() {
                System.out.println("Updating vocabulary!");
            }
            @Override
            protected Exception doInBackground(Void... params) {
                if (dictionary != null) {
                    if (dictionary.isFile()) {
                        rapidRecognizer.getRapidDecoder().loadDict(dictionary.getPath(), null, "dict");
                    } else {
                        Log.e("RapidSphinx", dictionary.getPath() + " is not a dictionary file");
                    }
                }
                rapidRecognizer.getRapidDecoder().setJsgfString(SEARCH_ID, grammarStr);
                return null;
            }
            @Override
            protected void onPostExecute(Exception e) {
                System.out.println("Vocabulary updated!");
                if (rapidCompletionListener != null) {
                    rapidCompletionListener.rapidCompletedProcess();
                }
            }
        }.execute();
    }

    public void updateGrammar(final File jsgf, @Nullable final File dictionary, @Nullable final RapidCompletionListener rapidCompletionListener) {
        if (jsgf.isFile()) {
            new AsyncTask<Void, Void, Exception>() {
                @Override
                protected void onPreExecute() {
                    System.out.println("Updating vocabulary!");
                }
                @Override
                protected Exception doInBackground(Void... params) {
                    if (dictionary != null) {
                        if (dictionary.isFile()) {
                            rapidRecognizer.getRapidDecoder().loadDict(dictionary.getPath(), null, "dict");
                        } else {
                            Log.e("RapidSphinx", dictionary.getPath() + " is not a dictionary file");
                        }
                    }
                    rapidRecognizer.getRapidDecoder().setJsgfFile(SEARCH_ID, jsgf.getPath());
                    return null;
                }
                @Override
                protected void onPostExecute(Exception e) {
                    System.out.println("Vocabulary updated!");
                    if (rapidCompletionListener != null) {
                        rapidCompletionListener.rapidCompletedProcess();
                    }
                }
            }.execute();
        } else {
            Log.e("RapidSphinx", jsgf.getPath() + " is not a grammar file");
        }
    }

    public void updateLmFile(final File lmFile, @Nullable final File dictionary, @Nullable final RapidCompletionListener rapidCompletionListener) {
        if (lmFile.isFile()) {
            new AsyncTask<Void, Void, Exception>() {
                @Override
                protected void onPreExecute() {
                    System.out.println("Updating vocabulary!");
                }
                @Override
                protected Exception doInBackground(Void... params) {
                    if (dictionary != null) {
                        if (dictionary.isFile()) {
                            rapidRecognizer.getRapidDecoder().loadDict(dictionary.getPath(), null, "dict");
                        } else {
                            Log.e("RapidSphinx", dictionary.getPath() + " is not a dictionary file");
                        }
                    }
                    rapidRecognizer.getRapidDecoder().setLmFile(SEARCH_ID, lmFile.getPath());
                    return null;
                }
                @Override
                protected void onPostExecute(Exception e) {
                    System.out.println("Vocabulary updated!");
                    if (rapidCompletionListener != null) {
                        rapidCompletionListener.rapidCompletedProcess();
                    }
                }
            }.execute();
        } else {
            Log.e("RapidSphinx", lmFile.getPath() + " is not a language model file");
        }
    }

    public Decoder getDecoder() {
        return rapidRecognizer.getRapidDecoder();
    }

    public RapidRecorder getRapidRecorder() {
        return rapidRecognizer.getRapidRecorder();
    }

    public void addListener(RapidSphinxListener rapidSphinxListener) {
        this.rapidSphinxListener = rapidSphinxListener;
    }

    public void removeListener() {
        this.rapidSphinxListener = null;
    }

    public File getLanguageModelPath() {
        File lmFile = new File(assetDir, "arpas/"+ SEARCH_ID +"-new.arpa");
        return lmFile;
    }

    public File getDictonaryFile() {
        File dictFile = new File(assetDir, SEARCH_ID + ".dict");
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

    public void stop() {
        rapidRecognizer.stopRapid();
        rapidRecognizer.shutdownRapid();
    }

    public SegmentIterator getSegmentIterator() {
        return rapidRecognizer.getRapidDecoder().seg().iterator();
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
        rapidRecognizer.stopRapid();
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
        if (hypothesis != null) {
            SegmentIterator segmentIterator = rapidRecognizer.getRapidDecoder().seg().iterator();
            while (segmentIterator.hasNext()) {
                Segment segment = segmentIterator.next();
                double score =  rapidRecognizer.getRapidDecoder().getLogmath().exp(segment.getProb());
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
        rapidRecognizer.stopRapid();
        if (rapidSphinxListener != null) {
            rapidSphinxListener.rapidSphinxDidStop(e.getMessage(), 500);
        }
    }

    @Override
    public void onTimeout() {
        rapidRecognizer.stopRapid();
        if (rapidSphinxListener != null) {
            rapidSphinxListener.rapidSphinxDidStop("Speech timed out!", 522);
        }
    }
}
