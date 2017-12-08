//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.icaksama.rapidsphinx.pocketsphinx;

public class SphinxBaseJNI {
    public SphinxBaseJNI() {
    }

    public static final native void delete_Config(long var0);

    public static final native void Config_setBoolean(long var0, Config var2, String var3, boolean var4);

    public static final native void Config_setInt(long var0, Config var2, String var3, int var4);

    public static final native void Config_setFloat(long var0, Config var2, String var3, double var4);

    public static final native void Config_setString(long var0, Config var2, String var3, String var4);

    public static final native void Config_setStringExtra(long var0, Config var2, String var3, String var4);

    public static final native boolean Config_exists(long var0, Config var2, String var3);

    public static final native boolean Config_getBoolean(long var0, Config var2, String var3);

    public static final native int Config_getInt(long var0, Config var2, String var3);

    public static final native double Config_getFloat(long var0, Config var2, String var3);

    public static final native String Config_getString(long var0, Config var2, String var3);

    public static final native long new_FrontEnd();

    public static final native void delete_FrontEnd(long var0);

    public static final native int FrontEnd_outputSize(long var0, FrontEnd var2);

    public static final native int FrontEnd_processUtt(long var0, FrontEnd var2, String var3, long var4, long var6);

    public static final native void delete_Feature(long var0);

    public static final native long new_feature();

    public static final native long new_FsgModel__SWIG_0(String var0, long var1, LogMath var3, float var4, int var5);

    public static final native long new_FsgModel__SWIG_1(String var0, long var1, LogMath var3, float var4);

    public static final native void delete_FsgModel(long var0);

    public static final native int FsgModel_wordId(long var0, FsgModel var2, String var3);

    public static final native int FsgModel_wordAdd(long var0, FsgModel var2, String var3);

    public static final native void FsgModel_transAdd(long var0, FsgModel var2, int var3, int var4, int var5, int var6);

    public static final native int FsgModel_nullTransAdd(long var0, FsgModel var2, int var3, int var4, int var5);

    public static final native int FsgModel_tagTransAdd(long var0, FsgModel var2, int var3, int var4, int var5, int var6);

    public static final native int FsgModel_addSilence(long var0, FsgModel var2, String var3, int var4, float var5);

    public static final native int FsgModel_addAlt(long var0, FsgModel var2, String var3, String var4);

    public static final native void FsgModel_writefile(long var0, FsgModel var2, String var3);

    public static final native long new_JsgfRule();

    public static final native long JsgfRule_fromIter(long var0);

    public static final native String JsgfRule_getName(long var0, JsgfRule var2);

    public static final native boolean JsgfRule_isPublic(long var0, JsgfRule var2);

    public static final native void delete_JsgfRule(long var0);

    public static final native long NGramModel_fromIter(long var0);

    public static final native long new_NGramModel__SWIG_0(String var0);

    public static final native long new_NGramModel__SWIG_1(long var0, Config var2, long var3, LogMath var5, String var6);

    public static final native void delete_NGramModel(long var0);

    public static final native void NGramModel_write(long var0, NGramModel var2, String var3, int var4);

    public static final native int NGramModel_strToType(long var0, NGramModel var2, String var3);

    public static final native String NGramModel_typeToStr(long var0, NGramModel var2, int var3);

    public static final native void NGramModel_casefold(long var0, NGramModel var2, int var3);

    public static final native int NGramModel_size(long var0, NGramModel var2);

    public static final native int NGramModel_addWord(long var0, NGramModel var2, String var3, float var4);

    public static final native int NGramModel_prob(long var0, NGramModel var2, String[] var3);

    public static final native long new_LogMath();

    public static final native void delete_LogMath(long var0);

    public static final native double LogMath_exp(long var0, LogMath var2, int var3);

    public static final native long new_NGramModelSetIterator(long var0);

    public static final native void delete_NGramModelSetIterator(long var0);

    public static final native long NGramModelSetIterator_next(long var0, NGramModelSetIterator var2);

    public static final native boolean NGramModelSetIterator_hasNext(long var0, NGramModelSetIterator var2);

    public static final native long new_JsgfIterator(long var0);

    public static final native void delete_JsgfIterator(long var0);

    public static final native long JsgfIterator_next(long var0, JsgfIterator var2);

    public static final native boolean JsgfIterator_hasNext(long var0, JsgfIterator var2);

    public static final native long NGramModelSet_iterator(long var0, NGramModelSet var2);

    public static final native long new_NGramModelSet(long var0, Config var2, long var3, LogMath var5, String var6);

    public static final native void delete_NGramModelSet(long var0);

    public static final native int NGramModelSet_count(long var0, NGramModelSet var2);

    public static final native long NGramModelSet_add(long var0, NGramModelSet var2, long var3, NGramModel var5, String var6, float var7, boolean var8);

    public static final native long NGramModelSet_select(long var0, NGramModelSet var2, String var3);

    public static final native long NGramModelSet_lookup(long var0, NGramModelSet var2, String var3);

    public static final native String NGramModelSet_current(long var0, NGramModelSet var2);

    public static final native long Jsgf_iterator(long var0, Jsgf var2);

    public static final native long new_Jsgf(String var0);

    public static final native void delete_Jsgf(long var0);

    public static final native String Jsgf_getName(long var0, Jsgf var2);

    public static final native long Jsgf_getRule(long var0, Jsgf var2, String var3);

    public static final native long Jsgf_buildFsg(long var0, Jsgf var2, long var3, JsgfRule var5, long var6, LogMath var8, float var9);
}
