//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.icaksama.rapidsphinx.pocketsphinx;

public interface RecognitionListener {
    void onBeginningOfSpeech();

    void onEndOfSpeech();

    void onPartialResult(Hypothesis var1);

    void onResult(Hypothesis var1);

    void onError(Exception var1);

    void onTimeout();
}
