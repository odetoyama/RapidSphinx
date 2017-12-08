package com.icaksama.rapidsphinx.pocketsphinx;

/**
 * Created by icaksama on 08/12/17.
 */

public class Feature {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected Feature(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(Feature obj) {
        return obj == null?0L:obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if(this.swigCPtr != 0L) {
            if(this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_Feature(this.swigCPtr);
            }

            this.swigCPtr = 0L;
        }

    }

    public Feature() {
        this(SphinxBaseJNI.new_feature(), true);
    }
}
