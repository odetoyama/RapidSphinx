package com.icaksama.rapidsphinx.pocketsphinx;

/**
 * Created by icaksama on 08/12/17.
 */

public class Hypothesis {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected Hypothesis(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(Hypothesis obj) {
        return obj == null?0L:obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if(this.swigCPtr != 0L) {
            if(this.swigCMemOwn) {
                this.swigCMemOwn = false;
                PocketSphinxJNI.delete_Hypothesis(this.swigCPtr);
            }

            this.swigCPtr = 0L;
        }

    }

    public void setHypstr(String value) {
        PocketSphinxJNI.Hypothesis_hypstr_set(this.swigCPtr, this, value);
    }

    public String getHypstr() {
        return PocketSphinxJNI.Hypothesis_hypstr_get(this.swigCPtr, this);
    }

    public void setBestScore(int value) {
        PocketSphinxJNI.Hypothesis_bestScore_set(this.swigCPtr, this, value);
    }

    public int getBestScore() {
        return PocketSphinxJNI.Hypothesis_bestScore_get(this.swigCPtr, this);
    }

    public void setProb(int value) {
        PocketSphinxJNI.Hypothesis_prob_set(this.swigCPtr, this, value);
    }

    public int getProb() {
        return PocketSphinxJNI.Hypothesis_prob_get(this.swigCPtr, this);
    }

    public Hypothesis(String hypstr, int best_score, int prob) {
        this(PocketSphinxJNI.new_Hypothesis(hypstr, best_score, prob), true);
    }
}
