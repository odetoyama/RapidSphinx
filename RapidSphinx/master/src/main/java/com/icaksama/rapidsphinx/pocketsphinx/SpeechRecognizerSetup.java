//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.icaksama.rapidsphinx.pocketsphinx;

import java.io.File;
import java.io.IOException;

public class SpeechRecognizerSetup {
    private final Config config;

    public static SpeechRecognizerSetup defaultSetup() {
        return new SpeechRecognizerSetup(Decoder.defaultConfig());
    }

    public static SpeechRecognizerSetup setupFromFile(File configFile) {
        return new SpeechRecognizerSetup(Decoder.fileConfig(configFile.getPath()));
    }

    private SpeechRecognizerSetup(Config config) {
        this.config = config;
    }

    public SpeechRecognizer getRecognizer() throws IOException {
        return new SpeechRecognizer(this.config);
    }

    public SpeechRecognizerSetup setAcousticModel(File model) {
        return this.setString("-hmm", model.getPath());
    }

    public SpeechRecognizerSetup setDictionary(File dictionary) {
        return this.setString("-dict", dictionary.getPath());
    }

    public SpeechRecognizerSetup setSampleRate(int rate) {
        return this.setFloat("-samprate", (double)rate);
    }

    public SpeechRecognizerSetup setRawLogDir(File dir) {
        return this.setString("-rawlogdir", dir.getPath());
    }

    public SpeechRecognizerSetup setKeywordThreshold(float threshold) {
        return this.setFloat("-kws_threshold", (double)threshold);
    }

    public SpeechRecognizerSetup setBoolean(String key, boolean value) {
        this.config.setBoolean(key, value);
        return this;
    }

    public SpeechRecognizerSetup setInteger(String key, int value) {
        this.config.setInt(key, value);
        return this;
    }

    public SpeechRecognizerSetup setFloat(String key, double value) {
        this.config.setFloat(key, value);
        return this;
    }

    public SpeechRecognizerSetup setString(String key, String value) {
        this.config.setString(key, value);
        return this;
    }

    static {
        System.loadLibrary("pocketsphinx_jni");
    }
}
