//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.icaksama.rapidsphinx.pocketsphinx;

public class Lattice {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected Lattice(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(Lattice obj) {
        return obj == null?0L:obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if(this.swigCPtr != 0L) {
            if(this.swigCMemOwn) {
                this.swigCMemOwn = false;
                PocketSphinxJNI.delete_Lattice(this.swigCPtr);
            }

            this.swigCPtr = 0L;
        }

    }

    public Lattice(String path) {
        this(PocketSphinxJNI.new_Lattice__SWIG_0(path), true);
    }

    public Lattice(Decoder decoder, String path) {
        this(PocketSphinxJNI.new_Lattice__SWIG_1(Decoder.getCPtr(decoder), decoder, path), true);
    }

    public void write(String path) {
        PocketSphinxJNI.Lattice_write(this.swigCPtr, this, path);
    }

    public void writeHtk(String path) {
        PocketSphinxJNI.Lattice_writeHtk(this.swigCPtr, this, path);
    }
}
