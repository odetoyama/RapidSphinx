package com.icaksama.rapidsphinx.pocketsphinx;

/**
 * Created by icaksama on 08/12/17.
 */

public class FrontEnd {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected FrontEnd(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(FrontEnd obj) {
        return obj == null?0L:obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if(this.swigCPtr != 0L) {
            if(this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_FrontEnd(this.swigCPtr);
            }

            this.swigCPtr = 0L;
        }

    }

    public FrontEnd() {
        this(SphinxBaseJNI.new_FrontEnd(), true);
    }

    public int outputSize() {
        return SphinxBaseJNI.FrontEnd_outputSize(this.swigCPtr, this);
    }

    public int processUtt(String spch, long nsamps, SWIGTYPE_p_p_p_mfcc_t cep_block) {
        return SphinxBaseJNI.FrontEnd_processUtt(this.swigCPtr, this, spch, nsamps, SWIGTYPE_p_p_p_mfcc_t.getCPtr(cep_block));
    }
}
