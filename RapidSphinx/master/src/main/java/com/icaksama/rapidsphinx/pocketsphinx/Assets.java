//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.icaksama.rapidsphinx.pocketsphinx;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;

@SuppressWarnings("LossyEncoding") public class Assets {
    protected static final String TAG = Assets.class.getSimpleName();
    public static final String ASSET_LIST_NAME = "assets.lst";
    public static final String SYNC_DIR = "sync";
    public static final String HASH_EXT = ".md5";
    private final AssetManager assetManager;
    private final File externalDir;

    public Assets(Context context) throws IOException {
        File appDir = context.getExternalFilesDir((String)null);
        if(null == appDir) {
            throw new IOException("cannot get external files dir, external storage state is " + Environment.getExternalStorageState());
        } else {
            this.externalDir = new File(appDir, "sync");
            this.assetManager = context.getAssets();
        }
    }

    public Assets(Context context, String dest) {
        this.externalDir = new File(dest);
        this.assetManager = context.getAssets();
    }

    public File getExternalDir() {
        return this.externalDir;
    }

    public Map<String, String> getItems() throws IOException {
        Map<String, String> items = new HashMap();
        Iterator var2 = this.readLines(this.openAsset("assets.lst")).iterator();

        while(var2.hasNext()) {
            String path = (String)var2.next();
            Reader reader = new InputStreamReader(this.openAsset(path + ".md5"));
            items.put(path, (new BufferedReader(reader)).readLine());
        }

        return items;
    }

    public Map<String, String> getExternalItems() {
        try {
            Map<String, String> items = new HashMap();
            File assetFile = new File(this.externalDir, "assets.lst");
            Iterator var3 = this.readLines(new FileInputStream(assetFile)).iterator();

            while(var3.hasNext()) {
                String line = (String)var3.next();
                String[] fields = line.split(" ");
                items.put(fields[0], fields[1]);
            }

            return items;
        } catch (IOException var6) {
            return Collections.emptyMap();
        }
    }

    public Collection<String> getItemsToCopy(String path) throws IOException {
        Collection<String> items = new ArrayList();
        Queue<String> queue = new ArrayDeque();
        queue.offer(path);

        while(!queue.isEmpty()) {
            path = (String)queue.poll();
            String[] list = this.assetManager.list(path);
            String[] var5 = list;
            int var6 = list.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String nested = var5[var7];
                queue.offer(nested);
            }

            if(list.length == 0) {
                items.add(path);
            }
        }

        return items;
    }

    private List<String> readLines(InputStream source) throws IOException {
        List<String> lines = new ArrayList();
        BufferedReader br = new BufferedReader(new InputStreamReader(source));

        String line;
        while(null != (line = br.readLine())) {
            lines.add(line);
        }

        return lines;
    }

    private InputStream openAsset(String asset) throws IOException {
        return this.assetManager.open((new File("sync", asset)).getPath());
    }

    public void updateItemList(Map<String, String> items) throws IOException {
        File assetListFile = new File(this.externalDir, "assets.lst");
        PrintWriter pw = new PrintWriter(new FileOutputStream(assetListFile));
        Iterator var4 = items.entrySet().iterator();

        while(var4.hasNext()) {
            Entry<String, String> entry = (Entry)var4.next();
            pw.format("%s %s\n", new Object[]{entry.getKey(), entry.getValue()});
        }

        pw.close();
    }

    public File copy(String asset) throws IOException {
        InputStream source = this.openAsset(asset);
        File destinationFile = new File(this.externalDir, asset);
        destinationFile.getParentFile().mkdirs();
        OutputStream destination = new FileOutputStream(destinationFile);
        byte[] buffer = new byte[1024];

        int nread;
        while((nread = source.read(buffer)) != -1) {
            if(nread == 0) {
                nread = source.read();
                if(nread < 0) {
                    break;
                }

                destination.write(nread);
            } else {
                destination.write(buffer, 0, nread);
            }
        }

        destination.close();
        return destinationFile;
    }

    public File syncAssets() throws IOException {
        Collection<String> newItems = new ArrayList();
        Collection<String> unusedItems = new ArrayList();
        Map<String, String> items = this.getItems();
        Map<String, String> externalItems = this.getExternalItems();
        Iterator var5 = items.keySet().iterator();

        while(true) {
            String path;
            while(var5.hasNext()) {
                path = (String)var5.next();
                if(((String)items.get(path)).equals(externalItems.get(path)) && (new File(this.externalDir, path)).exists()) {
                    Log.i(TAG, String.format("Skipping asset %s: checksums are equal", new Object[]{path}));
                } else {
                    newItems.add(path);
                }
            }

            unusedItems.addAll(externalItems.keySet());
            unusedItems.removeAll(items.keySet());
            var5 = newItems.iterator();

            File file;
            while(var5.hasNext()) {
                path = (String)var5.next();
                file = this.copy(path);
                Log.i(TAG, String.format("Copying asset %s to %s", new Object[]{path, file}));
            }

            var5 = unusedItems.iterator();

            while(var5.hasNext()) {
                path = (String)var5.next();
                file = new File(this.externalDir, path);
                file.delete();
                Log.i(TAG, String.format("Removing asset %s", new Object[]{file}));
            }

            this.updateItemList(items);
            return this.externalDir;
        }
    }
}
