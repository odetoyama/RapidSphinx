//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.icaksama.rapidsphinx.pocketsphinx;

public class LogMath {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected LogMath(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(LogMath obj) {
        return obj == null?0L:obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if(this.swigCPtr != 0L) {
            if(this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_LogMath(this.swigCPtr);
            }

            this.swigCPtr = 0L;
        }

    }

    public LogMath() {
        this(SphinxBaseJNI.new_LogMath(), true);
    }

    public double exp(int prob) {
        return SphinxBaseJNI.LogMath_exp(this.swigCPtr, this, prob);
    }
}
