//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.icaksama.rapidsphinx.pocketsphinx;

public class NGramModelSet implements Iterable<NGramModel> {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected NGramModelSet(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(NGramModelSet obj) {
        return obj == null?0L:obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if(this.swigCPtr != 0L) {
            if(this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_NGramModelSet(this.swigCPtr);
            }

            this.swigCPtr = 0L;
        }

    }

    public NGramModelSetIterator iterator() {
        long cPtr = SphinxBaseJNI.NGramModelSet_iterator(this.swigCPtr, this);
        return cPtr == 0L?null:new NGramModelSetIterator(cPtr, true);
    }

    public NGramModelSet(Config config, LogMath logmath, String path) {
        this(SphinxBaseJNI.new_NGramModelSet(Config.getCPtr(config), config, LogMath.getCPtr(logmath), logmath, path), true);
    }

    public int count() {
        return SphinxBaseJNI.NGramModelSet_count(this.swigCPtr, this);
    }

    public NGramModel add(NGramModel model, String name, float weight, boolean reuse_widmap) {
        long cPtr = SphinxBaseJNI.NGramModelSet_add(this.swigCPtr, this, NGramModel.getCPtr(model), model, name, weight, reuse_widmap);
        return cPtr == 0L?null:new NGramModel(cPtr, false);
    }

    public NGramModel select(String name) {
        long cPtr = SphinxBaseJNI.NGramModelSet_select(this.swigCPtr, this, name);
        return cPtr == 0L?null:new NGramModel(cPtr, false);
    }

    public NGramModel lookup(String name) {
        long cPtr = SphinxBaseJNI.NGramModelSet_lookup(this.swigCPtr, this, name);
        return cPtr == 0L?null:new NGramModel(cPtr, false);
    }

    public String current() {
        return SphinxBaseJNI.NGramModelSet_current(this.swigCPtr, this);
    }
}
