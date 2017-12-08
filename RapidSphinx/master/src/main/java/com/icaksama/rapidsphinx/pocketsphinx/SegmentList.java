//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.icaksama.rapidsphinx.pocketsphinx;

public class SegmentList implements Iterable<Segment> {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected SegmentList(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(SegmentList obj) {
        return obj == null?0L:obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if(this.swigCPtr != 0L) {
            if(this.swigCMemOwn) {
                this.swigCMemOwn = false;
                PocketSphinxJNI.delete_SegmentList(this.swigCPtr);
            }

            this.swigCPtr = 0L;
        }

    }

    public SegmentIterator iterator() {
        long cPtr = PocketSphinxJNI.SegmentList_iterator(this.swigCPtr, this);
        return cPtr == 0L?null:new SegmentIterator(cPtr, true);
    }
}
