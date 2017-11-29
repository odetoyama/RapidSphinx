package com.icaksama.rapidsphinx;

import java.util.List;

/**
 * Created by icaksama on 23/11/17.
 */

public interface RapidSphinxListener {
    void rapidSphinxDidStop(String reason, int code);
    void rapidSphinxFinalResult(String result, List<String> arrHypot, List<Double> scores);
    void rapidSphinxPartialResult(String partialResult);
    void rapidSphinxUnsupportedWords(List<String> words);
    void rapidSphinxDidSpeechDetected();
}
