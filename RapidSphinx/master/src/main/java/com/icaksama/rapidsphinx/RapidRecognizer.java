package com.icaksama.rapidsphinx;

import android.media.AudioRecord;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import edu.cmu.pocketsphinx.Config;
import edu.cmu.pocketsphinx.Decoder;
import edu.cmu.pocketsphinx.FsgModel;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static java.lang.String.format;

/**
 * Created by icaksama on 4/25/16.
 */

public class RapidRecognizer extends SpeechRecognizer {

    private static final String TAG = "RapidSphinx";
    private int bufferSize;
    private final Decoder decoder;
    private final int sampleRate;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Collection<RecognitionListener> listeners = new HashSet<RecognitionListener>();
    private AudioRecord recorder;
    private Thread recognizerThread;
    private FileOutputStream audioOutputStream = null;
    private RapidRecorder rapidRecorder = null;

    /**
     * Creates speech recognizer. Recognizer holds the AudioRecord object, so you
     * need to call {@linkrelease} in order to properly finalize it.
     *
     * @param config The configuration object
     * @throws IOException thrown if audio recorder can not be created for some reason.
     */
    protected RapidRecognizer(File assetDir, Config config) throws IOException {
        super(config);
        this.decoder = new Decoder(config);
        this.sampleRate = (int) decoder.getConfig().getFloat("-samprate");
        this.bufferSize = Math.round((float) this.sampleRate * 0.4F);
        this.rapidRecorder = new RapidRecorder(assetDir, sampleRate);
    }

    /**
     * Adds listener.
     */
    protected void addRapidListener(RecognitionListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Removes listener.
     */
    protected void removeRapidListener(RecognitionListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    /**
     * Starts recognition. Does nothing if recognition is active.
     *
     * @return true if recognition was actually started
     */
    protected boolean startRapidListening(String searchName) {
        if (null != recognizerThread)
            return false;

        Log.i(TAG, format("Start recognition \"%s\"", searchName));
        this.setFileOutputStream();
        decoder.setSearch(searchName);
        recognizerThread = new RecognizerThread();
        recognizerThread.start();
        return true;
    }

    /**
     * Starts recognition. After specified timeout listening stops and the
     * endOfSpeech signals about that. Does nothing if recognition is active.
     *
     * @return true if recognition was actually started
     * @timeout - timeout in milliseconds to listen.
     */
    protected boolean startRapidListening(String searchName, int timeout) {
        if (null != recognizerThread)
            return false;

        Log.i(TAG, format("Start recognition \"%s\"", searchName));
        this.setFileOutputStream();
        decoder.setSearch(searchName);
        recognizerThread = new RecognizerThread(timeout);
        recognizerThread.start();
        return true;
    }

    protected boolean stopRecognizerThread() {
        if (null == recognizerThread)
            return false;

        try {
            recognizerThread.interrupt();
            recognizerThread.join();
        } catch (InterruptedException e) {
            // Restore the interrupted status.
            Thread.currentThread().interrupt();
        }
        recognizerThread = null;
        return true;
    }

    /**
     * Stops recognition. All listeners should receive final result if there is
     * any. Does nothing if recognition is not active.
     *
     * @return true if recognition was actually stopped
     */
    protected boolean stopRapid() {
        boolean result = stopRecognizerThread();
        if (result) {
            Log.i(TAG, "Stop recognition");
            final Hypothesis hypothesis = decoder.hyp();
            mainHandler.post(new ResultEvent(hypothesis, true));
        }
        return result;
    }

    /**
     * Cancels recognition. Listeners do not receive final result. Does nothing
     * if recognition is not active.
     *
     * @return true if recognition was actually canceled
     */
    protected boolean cancelRapid() {
        boolean result = stopRecognizerThread();
        if (result) {
            Log.i(TAG, "Cancel recognition");
        }
        return result;
    }

    /**
     * Returns the decoder object for advanced operation (dictionary extension, utterance
     * data collection, adaptation and so on).
     *
     * @return Decoder
     */
    protected Decoder getRapidDecoder() {
        return decoder;
    }

    /**
     * Shutdown the recognizer and release the recorder
     */
    protected void shutdownRapid() {
        recorder.release();
    }

    /**
     * getstate the recorder
     */
    protected int getState() {
        return recorder.getState();
    }

    /**
     * Gets name of the currently active search.
     *
     * @return active search name or null if no search was started
     */
    protected String getRapidSearchName() {
        return decoder.getSearch();
    }

    protected void addRapidFsgSearch(String searchName, FsgModel fsgModel) {
        decoder.setFsg(searchName, fsgModel);
    }

    /**
     * Adds searches based on JSpeech grammar.
     *
     * @param name search name
     * @param file JSGF file
     */
    protected void addRapidGrammarSearch(String name, File file) {
        Log.i(TAG, format("Load JSGF %s", file));
        decoder.setJsgfFile(name, file.getPath());
    }

    /**
     * Adds search based on N-gram language model.
     *
     * @param name search name
     * @param file N-gram model file
     */
    protected void addRapidNgramSearch(String name, File file) {
        Log.i(TAG, format("Load N-gram model %s", file));
        decoder.setLmFile(name, file.getPath());
    }

    /**
     * Adds search based on a single phrase.
     *
     * @param name   search name
     * @param phrase search phrase
     */
    protected void addRapidKeyphraseSearch(String name, String phrase) {
        decoder.setKeyphrase(name, phrase);
    }

    /**
     * Adds search based on a keyphrase file.
     *
     * @param name search name
     * @param file a file with search phrases, one phrase per line with optional weight in the end, for example
     *             <br/>
     *             <code>
     *             oh mighty computer /1e-20/
     *             how do you do /1e-10/
     *             </code>
     */
    protected void addRapidKeywordSearch(String name, File file) {
        decoder.setKws(name, file.getPath());
    }

    /**
     * Adds a search to look for the phonemes
     *
     * @param name search name
     * @param file bigram model
     */
    protected void addRapidAllphoneSearch(String name, File file) {
        decoder.setAllphoneFile(name, file.getPath());
    }

    private final class RecognizerThread extends Thread {

        private int remainingSamples;
        private int timeoutSamples;
        private final static int NO_TIMEOUT = -1;

        public RecognizerThread(int timeout) {
            if (timeout != NO_TIMEOUT) {
                this.timeoutSamples = timeout * sampleRate / 1000;
            } else {
                this.timeoutSamples = NO_TIMEOUT;
            }
            this.remainingSamples = this.timeoutSamples;
            recorder = new AudioRecord(6, sampleRate, 16, 2, bufferSize * 2);
            if (recorder.getState() == AudioRecord.STATE_UNINITIALIZED) {
                recorder.release();
                try {
                    throw new IOException(
                            "Failed to initialize recorder. Microphone might be already in use.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public RecognizerThread() {
            this(NO_TIMEOUT);
        }

        @Override
        public void run() {
            recorder.startRecording();
            if (recorder.getRecordingState() == AudioRecord.RECORDSTATE_STOPPED) {
                recorder.stop();
                IOException ioe = new IOException(
                        "Failed to start recording. Microphone might be already in use.");
                mainHandler.post(new OnErrorEvent(ioe));
                return;
            }
            decoder.startUtt();
            short[] buffer = new short[bufferSize];
            boolean inSpeech = decoder.getInSpeech();

            // Skip the first buffer, usually zeroes
            recorder.read(buffer, 0, buffer.length);
            while (!interrupted()
                    && ((timeoutSamples == NO_TIMEOUT) || (remainingSamples > 0))) {
                int nread = recorder.read(buffer, 0, buffer.length);
                if (-1 == nread) {
                    throw new RuntimeException("error reading audio buffer");
                } else if (nread > 0) {
                    decoder.processRaw(buffer, nread, false, false);
                    int max = 0;
                    for (int i = 0; i < nread; i++) {
                        max = Math.max(max, Math.abs(buffer[i]));
                    }
                    if (decoder.getInSpeech() != inSpeech) {
                        inSpeech = decoder.getInSpeech();
                        mainHandler.post(new InSpeechChangeEvent(inSpeech));
                    }
                    if (inSpeech)
                        remainingSamples = timeoutSamples;
                    final Hypothesis hypothesis = decoder.hyp();
                    mainHandler.post(new ResultEvent(hypothesis, false));
                    byte bufferData[] = short2byte(buffer);
                    try {
                        if (inSpeech) {
                            audioOutputStream.write(bufferData, 0, bufferData.length);
                        }
                        Log.d(TAG, "Audio data actually saved");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (timeoutSamples != NO_TIMEOUT) {
                    remainingSamples = remainingSamples - nread;
                }
            }
            recorder.stop();
//            int nread = recorder.read(buffer, 0, buffer.length);
//            decoder.processRaw(buffer, nread, false, false);
            decoder.endUtt();
            try {
                audioOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Remove all pending notifications.
            mainHandler.removeCallbacksAndMessages(null);

            // If we met timeout signal that speech ended
            if (timeoutSamples != NO_TIMEOUT && remainingSamples <= 0) {
                mainHandler.post(new TimeoutEvent());
            }
        }
    }

    private abstract class RecognitionEvent implements Runnable {
        public void run() {
            RecognitionListener[] emptyArray = new RecognitionListener[0];
            for (RecognitionListener listener : listeners.toArray(emptyArray))
                execute(listener);
        }
        protected abstract void execute(RecognitionListener listener);
    }

    private class InSpeechChangeEvent extends RecognitionEvent {
        private final boolean state;

        InSpeechChangeEvent(boolean state) {
            this.state = state;
        }

        @Override
        protected void execute(RecognitionListener listener) {
            if (state)
                listener.onBeginningOfSpeech();
            else
                listener.onEndOfSpeech();
        }
    }

    private class ResultEvent extends RecognitionEvent {
        protected final Hypothesis hypothesis;
        private final boolean finalResult;

        ResultEvent(Hypothesis hypothesis, boolean finalResult) {
            this.hypothesis = hypothesis;
            this.finalResult = finalResult;
        }

        @Override
        protected void execute(RecognitionListener listener) {
            if (finalResult)
                listener.onResult(hypothesis);
            else
                listener.onPartialResult(hypothesis);
        }
    }

    private class OnErrorEvent extends RecognitionEvent {
        private final Exception exception;

        OnErrorEvent(Exception exception) {
            this.exception = exception;
        }

        @Override
        protected void execute(RecognitionListener listener) {
            listener.onError(exception);
        }
    }

    private class TimeoutEvent extends RecognitionEvent {
        @Override
        protected void execute(RecognitionListener listener) {
            listener.onTimeout();
        }
    }

    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;
    }

    protected void setFileOutputStream() {
        if (null != audioOutputStream) {
            try {
                audioOutputStream.close();
            } catch (IOException e) {
                Log.d(TAG, "Could not close audioOutputStream");
            }
        }
        try {
            File audioFile = new File(rapidRecorder.getAudioPath());
            if (audioFile.exists()) {
                audioFile.delete();
            }
            audioOutputStream = new FileOutputStream(rapidRecorder.getAudioPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected RapidRecorder getRapidRecorder() {
        return rapidRecorder;
    }
}
