package com.icaksama.rapidsphinx.pocketsphinx;

/**
 * Created by icaksama on 08/12/17.
 */

public class Decoder {
    private transient long swigCPtr;
    protected transient boolean swigCMemOwn;

    protected Decoder(long cPtr, boolean cMemoryOwn) {
        this.swigCMemOwn = cMemoryOwn;
        this.swigCPtr = cPtr;
    }

    protected static long getCPtr(Decoder obj) {
        return obj == null?0L:obj.swigCPtr;
    }

    protected void finalize() {
        this.delete();
    }

    public synchronized void delete() {
        if(this.swigCPtr != 0L) {
            if(this.swigCMemOwn) {
                this.swigCMemOwn = false;
                PocketSphinxJNI.delete_Decoder(this.swigCPtr);
            }

            this.swigCPtr = 0L;
        }

    }

    public Decoder() {
        this(PocketSphinxJNI.new_Decoder__SWIG_0(), true);
    }

    public Decoder(Config config) {
        this(PocketSphinxJNI.new_Decoder__SWIG_1(Config.getCPtr(config), config), true);
    }

    public void reinit(Config config) {
        PocketSphinxJNI.Decoder_reinit(this.swigCPtr, this, Config.getCPtr(config), config);
    }

    public void loadDict(String fdict, String ffilter, String format) {
        PocketSphinxJNI.Decoder_loadDict(this.swigCPtr, this, fdict, ffilter, format);
    }

    public void saveDict(String dictfile, String format) {
        PocketSphinxJNI.Decoder_saveDict(this.swigCPtr, this, dictfile, format);
    }

    public void addWord(String word, String phones, int update) {
        PocketSphinxJNI.Decoder_addWord(this.swigCPtr, this, word, phones, update);
    }

    public String lookupWord(String word) {
        return PocketSphinxJNI.Decoder_lookupWord(this.swigCPtr, this, word);
    }

    public Lattice getLattice() {
        long cPtr = PocketSphinxJNI.Decoder_getLattice(this.swigCPtr, this);
        return cPtr == 0L?null:new Lattice(cPtr, false);
    }

    public Config getConfig() {
        long cPtr = PocketSphinxJNI.Decoder_getConfig(this.swigCPtr, this);
        return cPtr == 0L?null:new Config(cPtr, true);
    }

    public static Config defaultConfig() {
        long cPtr = PocketSphinxJNI.Decoder_defaultConfig();
        return cPtr == 0L?null:new Config(cPtr, true);
    }

    public static Config fileConfig(String path) {
        long cPtr = PocketSphinxJNI.Decoder_fileConfig(path);
        return cPtr == 0L?null:new Config(cPtr, true);
    }

    public void startStream() {
        PocketSphinxJNI.Decoder_startStream(this.swigCPtr, this);
    }

    public void startUtt() {
        PocketSphinxJNI.Decoder_startUtt(this.swigCPtr, this);
    }

    public void endUtt() {
        PocketSphinxJNI.Decoder_endUtt(this.swigCPtr, this);
    }

    public int processRaw(short[] SDATA, long NSAMP, boolean no_search, boolean full_utt) {
        return PocketSphinxJNI.Decoder_processRaw(this.swigCPtr, this, SDATA, NSAMP, no_search, full_utt);
    }

    public void setRawdataSize(long size) {
        PocketSphinxJNI.Decoder_setRawdataSize(this.swigCPtr, this, size);
    }

    public short[] getRawdata() {
        return PocketSphinxJNI.Decoder_getRawdata(this.swigCPtr, this);
    }

    public Hypothesis hyp() {
        long cPtr = PocketSphinxJNI.Decoder_hyp(this.swigCPtr, this);
        return cPtr == 0L?null:new Hypothesis(cPtr, true);
    }

    public FrontEnd getFe() {
        long cPtr = PocketSphinxJNI.Decoder_getFe(this.swigCPtr, this);
        return cPtr == 0L?null:new FrontEnd(cPtr, false);
    }

    public Feature getFeat() {
        long cPtr = PocketSphinxJNI.Decoder_getFeat(this.swigCPtr, this);
        return cPtr == 0L?null:new Feature(cPtr, false);
    }

    public boolean getInSpeech() {
        return PocketSphinxJNI.Decoder_getInSpeech(this.swigCPtr, this);
    }

    public FsgModel getFsg(String name) {
        long cPtr = PocketSphinxJNI.Decoder_getFsg(this.swigCPtr, this, name);
        return cPtr == 0L?null:new FsgModel(cPtr, false);
    }

    public void setFsg(String name, FsgModel fsg) {
        PocketSphinxJNI.Decoder_setFsg(this.swigCPtr, this, name, FsgModel.getCPtr(fsg), fsg);
    }

    public void setJsgfFile(String name, String path) {
        PocketSphinxJNI.Decoder_setJsgfFile(this.swigCPtr, this, name, path);
    }

    public void setJsgfString(String name, String jsgf_string) {
        PocketSphinxJNI.Decoder_setJsgfString(this.swigCPtr, this, name, jsgf_string);
    }

    public String getKws(String name) {
        return PocketSphinxJNI.Decoder_getKws(this.swigCPtr, this, name);
    }

    public void setKws(String name, String keyfile) {
        PocketSphinxJNI.Decoder_setKws(this.swigCPtr, this, name, keyfile);
    }

    public void setKeyphrase(String name, String keyphrase) {
        PocketSphinxJNI.Decoder_setKeyphrase(this.swigCPtr, this, name, keyphrase);
    }

    public void setAllphoneFile(String name, String lmfile) {
        PocketSphinxJNI.Decoder_setAllphoneFile(this.swigCPtr, this, name, lmfile);
    }

    public NGramModel getLm(String name) {
        long cPtr = PocketSphinxJNI.Decoder_getLm(this.swigCPtr, this, name);
        return cPtr == 0L?null:new NGramModel(cPtr, true);
    }

    public void setLm(String name, NGramModel lm) {
        PocketSphinxJNI.Decoder_setLm(this.swigCPtr, this, name, NGramModel.getCPtr(lm), lm);
    }

    public void setLmFile(String name, String path) {
        PocketSphinxJNI.Decoder_setLmFile(this.swigCPtr, this, name, path);
    }

    public LogMath getLogmath() {
        long cPtr = PocketSphinxJNI.Decoder_getLogmath(this.swigCPtr, this);
        return cPtr == 0L?null:new LogMath(cPtr, true);
    }

    public void setSearch(String search_name) {
        PocketSphinxJNI.Decoder_setSearch(this.swigCPtr, this, search_name);
    }

    public void unsetSearch(String search_name) {
        PocketSphinxJNI.Decoder_unsetSearch(this.swigCPtr, this, search_name);
    }

    public String getSearch() {
        return PocketSphinxJNI.Decoder_getSearch(this.swigCPtr, this);
    }

    public int nFrames() {
        return PocketSphinxJNI.Decoder_nFrames(this.swigCPtr, this);
    }

    public SegmentList seg() {
        long cPtr = PocketSphinxJNI.Decoder_seg(this.swigCPtr, this);
        return cPtr == 0L?null:new SegmentList(cPtr, false);
    }

    public NBestList nbest() {
        long cPtr = PocketSphinxJNI.Decoder_nbest(this.swigCPtr, this);
        return cPtr == 0L?null:new NBestList(cPtr, false);
    }
}
