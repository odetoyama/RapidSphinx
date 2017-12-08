//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.icaksama.rapidsphinx.pocketsphinx;

import java.util.Iterator;

public class NBestIterator implements Iterator<NBest> {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected NBestIterator(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(NBestIterator obj) {
        return obj == null?0L:obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if(this.swigCPtr != 0L) {
            if(this.swigCMemOwn) {
                this.swigCMemOwn = false;
                PocketSphinxJNI.delete_NBestIterator(this.swigCPtr);
            }

            this.swigCPtr = 0L;
        }

    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public NBestIterator(SWIGTYPE_p_void ptr) {
        this(PocketSphinxJNI.new_NBestIterator(SWIGTYPE_p_void.getCPtr(ptr)), true);
    }

    public NBest next() {
        long cPtr = PocketSphinxJNI.NBestIterator_next(this.swigCPtr, this);
        return cPtr == 0L?null:new NBest(cPtr, true);
    }

    public boolean hasNext() {
        return PocketSphinxJNI.NBestIterator_hasNext(this.swigCPtr, this);
    }
}
