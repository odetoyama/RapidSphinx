package com.icaksama.rapidsphinx.pocketsphinx;

/**
 * Created by icaksama on 08/12/17.
 */

public class FsgModel {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected FsgModel(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(FsgModel obj) {
        return obj == null?0L:obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if(this.swigCPtr != 0L) {
            if(this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_FsgModel(this.swigCPtr);
            }

            this.swigCPtr = 0L;
        }

    }

    public FsgModel(String name, LogMath logmath, float lw, int n) {
        this(SphinxBaseJNI.new_FsgModel__SWIG_0(name, LogMath.getCPtr(logmath), logmath, lw, n), true);
    }

    public FsgModel(String path, LogMath logmath, float lw) {
        this(SphinxBaseJNI.new_FsgModel__SWIG_1(path, LogMath.getCPtr(logmath), logmath, lw), true);
    }

    public int wordId(String word) {
        return SphinxBaseJNI.FsgModel_wordId(this.swigCPtr, this, word);
    }

    public int wordAdd(String word) {
        return SphinxBaseJNI.FsgModel_wordAdd(this.swigCPtr, this, word);
    }

    public void transAdd(int src, int dst, int logp, int wid) {
        SphinxBaseJNI.FsgModel_transAdd(this.swigCPtr, this, src, dst, logp, wid);
    }

    public int nullTransAdd(int src, int dst, int logp) {
        return SphinxBaseJNI.FsgModel_nullTransAdd(this.swigCPtr, this, src, dst, logp);
    }

    public int tagTransAdd(int src, int dst, int logp, int wid) {
        return SphinxBaseJNI.FsgModel_tagTransAdd(this.swigCPtr, this, src, dst, logp, wid);
    }

    public int addSilence(String silword, int state, float silprob) {
        return SphinxBaseJNI.FsgModel_addSilence(this.swigCPtr, this, silword, state, silprob);
    }

    public int addAlt(String baseword, String altword) {
        return SphinxBaseJNI.FsgModel_addAlt(this.swigCPtr, this, baseword, altword);
    }

    public void writefile(String path) {
        SphinxBaseJNI.FsgModel_writefile(this.swigCPtr, this, path);
    }
}
