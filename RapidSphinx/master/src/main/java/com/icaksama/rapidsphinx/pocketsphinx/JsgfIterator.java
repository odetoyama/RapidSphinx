//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.icaksama.rapidsphinx.pocketsphinx;

import java.util.Iterator;

public class JsgfIterator implements Iterator<JsgfRule> {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected JsgfIterator(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(JsgfIterator obj) {
        return obj == null?0L:obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if(this.swigCPtr != 0L) {
            if(this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_JsgfIterator(this.swigCPtr);
            }

            this.swigCPtr = 0L;
        }

    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public JsgfIterator(SWIGTYPE_p_void ptr) {
        this(SphinxBaseJNI.new_JsgfIterator(SWIGTYPE_p_void.getCPtr(ptr)), true);
    }

    public JsgfRule next() {
        long cPtr = SphinxBaseJNI.JsgfIterator_next(this.swigCPtr, this);
        return cPtr == 0L?null:new JsgfRule(cPtr, true);
    }

    public boolean hasNext() {
        return SphinxBaseJNI.JsgfIterator_hasNext(this.swigCPtr, this);
    }
}
