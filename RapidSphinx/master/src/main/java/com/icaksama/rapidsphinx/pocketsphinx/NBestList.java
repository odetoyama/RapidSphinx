//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.icaksama.rapidsphinx.pocketsphinx;

public class NBestList implements Iterable<NBest> {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected NBestList(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(NBestList obj) {
        return obj == null?0L:obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if(this.swigCPtr != 0L) {
            if(this.swigCMemOwn) {
                this.swigCMemOwn = false;
                PocketSphinxJNI.delete_NBestList(this.swigCPtr);
            }

            this.swigCPtr = 0L;
        }

    }

    public NBestIterator iterator() {
        long cPtr = PocketSphinxJNI.NBestList_iterator(this.swigCPtr, this);
        return cPtr == 0L?null:new NBestIterator(cPtr, true);
    }
}
