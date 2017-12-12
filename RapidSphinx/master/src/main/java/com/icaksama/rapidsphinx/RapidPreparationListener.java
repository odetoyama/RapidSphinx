package com.icaksama.rapidsphinx;

import edu.cmu.pocketsphinx.Config;

/**
 * Created by icaksama on 12/12/17.
 */

public interface RapidPreparationListener {

    void rapidPreExecute(Config config);
    void rapidPostExecute(boolean isSuccess);
}
