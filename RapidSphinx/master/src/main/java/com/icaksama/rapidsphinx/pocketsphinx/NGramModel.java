//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.icaksama.rapidsphinx.pocketsphinx;

public class NGramModel {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected NGramModel(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(NGramModel obj) {
        return obj == null?0L:obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if(this.swigCPtr != 0L) {
            if(this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_NGramModel(this.swigCPtr);
            }

            this.swigCPtr = 0L;
        }

    }

    public static NGramModel fromIter(SWIGTYPE_p_void itor) {
        long cPtr = SphinxBaseJNI.NGramModel_fromIter(SWIGTYPE_p_void.getCPtr(itor));
        return cPtr == 0L?null:new NGramModel(cPtr, false);
    }

    public NGramModel(String path) {
        this(SphinxBaseJNI.new_NGramModel__SWIG_0(path), true);
    }

    public NGramModel(Config config, LogMath logmath, String path) {
        this(SphinxBaseJNI.new_NGramModel__SWIG_1(Config.getCPtr(config), config, LogMath.getCPtr(logmath), logmath, path), true);
    }

    public void write(String path, int ftype) {
        SphinxBaseJNI.NGramModel_write(this.swigCPtr, this, path, ftype);
    }

    public int strToType(String str) {
        return SphinxBaseJNI.NGramModel_strToType(this.swigCPtr, this, str);
    }

    public String typeToStr(int type) {
        return SphinxBaseJNI.NGramModel_typeToStr(this.swigCPtr, this, type);
    }

    public void casefold(int kase) {
        SphinxBaseJNI.NGramModel_casefold(this.swigCPtr, this, kase);
    }

    public int size() {
        return SphinxBaseJNI.NGramModel_size(this.swigCPtr, this);
    }

    public int addWord(String word, float weight) {
        return SphinxBaseJNI.NGramModel_addWord(this.swigCPtr, this, word, weight);
    }

    public int prob(String[] n) {
        return SphinxBaseJNI.NGramModel_prob(this.swigCPtr, this, n);
    }
}
