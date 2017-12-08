//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.icaksama.rapidsphinx.pocketsphinx;

public class JsgfRule {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected JsgfRule(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(JsgfRule obj) {
        return obj == null?0L:obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if(this.swigCPtr != 0L) {
            if(this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_JsgfRule(this.swigCPtr);
            }

            this.swigCPtr = 0L;
        }

    }

    public JsgfRule() {
        this(SphinxBaseJNI.new_JsgfRule(), true);
    }

    public static JsgfRule fromIter(SWIGTYPE_p_void itor) {
        long cPtr = SphinxBaseJNI.JsgfRule_fromIter(SWIGTYPE_p_void.getCPtr(itor));
        return cPtr == 0L?null:new JsgfRule(cPtr, false);
    }

    public String getName() {
        return SphinxBaseJNI.JsgfRule_getName(this.swigCPtr, this);
    }

    public boolean isPublic() {
        return SphinxBaseJNI.JsgfRule_isPublic(this.swigCPtr, this);
    }
}
