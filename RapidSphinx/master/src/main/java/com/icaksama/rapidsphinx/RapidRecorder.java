package com.icaksama.rapidsphinx;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by icaksama on 11/12/17.
 */

public class RapidRecorder {
    private static final String TAG = "RapidRecorder";
    private String AUDIO_NAME = "icaksama_rapidsphinx";
    private int SAMPLE_RATE = 16000;

    private boolean isPlaying = false;
    private File audioRecordDirectory = null;
    private String audioRecordPath = null;
    private AudioTrack audioTrack;

    protected RapidRecorder(File assetDir, int rate) {
        this.SAMPLE_RATE = rate;
        this.audioRecordDirectory = assetDir;
        this.audioRecordPath = audioRecordDirectory.getPath() + File.separator + AUDIO_NAME + ".wav";
        if (!audioRecordDirectory.exists()) {
            if (!audioRecordDirectory.mkdirs()) {
                Log.e(TAG, "Failed to create directory");
                return;
            }
        }
    }

    public String getAudioPath() {
        return audioRecordPath;
    }

    public void play(@Nullable RapidCompletionListener rapidCompletionListener) throws IOException {
        File file = new File(audioRecordPath);
        if (!file.exists())
            return;

        byte[] byteData = new byte[(int) file.length()];
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(byteData);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int intSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, intSize, AudioTrack.MODE_STREAM);
        if (audioTrack != null) {
            isPlaying = true;
            audioTrack.play();
            audioTrack.write(byteData, 0, byteData.length);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                audioTrack.pause();
                audioTrack.flush();
            } else {
                audioTrack.stop();
            }
            audioTrack.release();
            isPlaying = false;
            if (rapidCompletionListener != null) {
                rapidCompletionListener.rapidCompletedProcess();
            }
        } else
            Log.d(TAG, "audio track is not initialised ");
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void stop() {
        if (audioTrack != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                audioTrack.pause();
                audioTrack.flush();
            } else {
                audioTrack.stop();
            }
            audioTrack.release();
            isPlaying = false;
        }
    }

}
