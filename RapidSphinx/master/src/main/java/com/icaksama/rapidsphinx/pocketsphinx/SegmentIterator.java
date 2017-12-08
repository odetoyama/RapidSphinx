//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.icaksama.rapidsphinx.pocketsphinx;

import java.util.Iterator;

public class SegmentIterator implements Iterator<Segment> {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SegmentIterator(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SegmentIterator obj) {
        return obj == null?0L:obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if(this.swigCPtr != 0L) {
            if(this.swigCMemOwn) {
                this.swigCMemOwn = false;
                PocketSphinxJNI.delete_SegmentIterator(this.swigCPtr);
            }

            this.swigCPtr = 0L;
        }

    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public SegmentIterator(SWIGTYPE_p_void ptr) {
        this(PocketSphinxJNI.new_SegmentIterator(SWIGTYPE_p_void.getCPtr(ptr)), true);
    }

    public Segment next() {
        long cPtr = PocketSphinxJNI.SegmentIterator_next(this.swigCPtr, this);
        return cPtr == 0L?null:new Segment(cPtr, true);
    }

    public boolean hasNext() {
        return PocketSphinxJNI.SegmentIterator_hasNext(this.swigCPtr, this);
    }
}
