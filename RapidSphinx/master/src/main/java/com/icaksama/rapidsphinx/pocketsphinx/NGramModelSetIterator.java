//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.icaksama.rapidsphinx.pocketsphinx;

import java.util.Iterator;

public class NGramModelSetIterator implements Iterator<NGramModel> {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected NGramModelSetIterator(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(NGramModelSetIterator obj) {
        return obj == null?0L:obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if(this.swigCPtr != 0L) {
            if(this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_NGramModelSetIterator(this.swigCPtr);
            }

            this.swigCPtr = 0L;
        }

    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public NGramModelSetIterator(SWIGTYPE_p_void ptr) {
        this(SphinxBaseJNI.new_NGramModelSetIterator(SWIGTYPE_p_void.getCPtr(ptr)), true);
    }

    public NGramModel next() {
        long cPtr = SphinxBaseJNI.NGramModelSetIterator_next(this.swigCPtr, this);
        return cPtr == 0L?null:new NGramModel(cPtr, true);
    }

    public boolean hasNext() {
        return SphinxBaseJNI.NGramModelSetIterator_hasNext(this.swigCPtr, this);
    }
}
