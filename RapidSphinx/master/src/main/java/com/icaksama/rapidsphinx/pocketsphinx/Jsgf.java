//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.icaksama.rapidsphinx.pocketsphinx;

public class Jsgf implements Iterable<JsgfRule> {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected Jsgf(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(Jsgf obj) {
        return obj == null?0L:obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if(this.swigCPtr != 0L) {
            if(this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_Jsgf(this.swigCPtr);
            }

            this.swigCPtr = 0L;
        }

    }

    public JsgfIterator iterator() {
        long cPtr = SphinxBaseJNI.Jsgf_iterator(this.swigCPtr, this);
        return cPtr == 0L?null:new JsgfIterator(cPtr, true);
    }

    public Jsgf(String path) {
        this(SphinxBaseJNI.new_Jsgf(path), true);
    }

    public String getName() {
        return SphinxBaseJNI.Jsgf_getName(this.swigCPtr, this);
    }

    public JsgfRule getRule(String name) {
        long cPtr = SphinxBaseJNI.Jsgf_getRule(this.swigCPtr, this, name);
        return cPtr == 0L?null:new JsgfRule(cPtr, false);
    }

    public FsgModel buildFsg(JsgfRule rule, LogMath logmath, float lw) {
        long cPtr = SphinxBaseJNI.Jsgf_buildFsg(this.swigCPtr, this, JsgfRule.getCPtr(rule), rule, LogMath.getCPtr(logmath), logmath, lw);
        return cPtr == 0L?null:new FsgModel(cPtr, false);
    }
}
