//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.icaksama.rapidsphinx.pocketsphinx;

public class NBest {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected NBest(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(NBest obj) {
        return obj == null?0L:obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if(this.swigCPtr != 0L) {
            if(this.swigCMemOwn) {
                this.swigCMemOwn = false;
                PocketSphinxJNI.delete_NBest(this.swigCPtr);
            }

            this.swigCPtr = 0L;
        }

    }

    public void setHypstr(String value) {
        PocketSphinxJNI.NBest_hypstr_set(this.swigCPtr, this, value);
    }

    public String getHypstr() {
        return PocketSphinxJNI.NBest_hypstr_get(this.swigCPtr, this);
    }

    public void setScore(int value) {
        PocketSphinxJNI.NBest_score_set(this.swigCPtr, this, value);
    }

    public int getScore() {
        return PocketSphinxJNI.NBest_score_get(this.swigCPtr, this);
    }

    public static NBest fromIter(SWIGTYPE_p_void itor) {
        long cPtr = PocketSphinxJNI.NBest_fromIter(SWIGTYPE_p_void.getCPtr(itor));
        return cPtr == 0L?null:new NBest(cPtr, false);
    }

    public Hypothesis hyp() {
        long cPtr = PocketSphinxJNI.NBest_hyp(this.swigCPtr, this);
        return cPtr == 0L?null:new Hypothesis(cPtr, true);
    }

    public NBest() {
        this(PocketSphinxJNI.new_nBest(), true);
    }
}
