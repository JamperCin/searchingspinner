package com.jamper.searchspinner;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jamper on 12/11/2017.
 */

public class Utils {

    Context mContext;
    ArrayList<String> list;

    public Utils(Context context) {
        this.mContext = context;
        list = new ArrayList<>();
    }

    private static final String FILENAME = "com.jamper.searchspinner.addEntry";


    public static void LOGS(String message) {
        Log.d("Http >> ", message);
    }


    private void cacheStringData(final String data) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    FileOutputStream fos = mContext.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                    fos.write(data.getBytes());
                    fos.close();
                    LOGS("Writing >>" + data);
                } catch (IOException e) {
                    LOGS("Error Caching Data >> " + e.getMessage());
                }
            }
        });

    }

    private  StringBuffer getCachedString() {
        StringBuffer fileContent = null;
        try {
            FileInputStream fis = mContext.openFileInput(FILENAME);
            if (fis != null) {
                 fileContent = new StringBuffer("");

                byte[] buffer = new byte[1024];
                int n;
                while ((n = fis.read(buffer)) != -1) {
                    fileContent.append(new String(buffer, 0, n));
                    LOGS("Reading" + fileContent);
                }
            }

            fis.close();
        } catch (IOException e) {
          e.printStackTrace();
        }

        return fileContent;
    }


    /**Return an arrayList of the**/
    public ArrayList<String> getCachedStringList() {
        final ArrayList<String> value = new ArrayList<String>();
        StringBuffer fileContent = getCachedString();

        String newFileContent = String.valueOf(fileContent).trim();
        String[] dateArray = newFileContent.split("#");
        for (int i = 0; i < dateArray.length; i++) {
            value.add(dateArray[i].trim());
            LOGS("Reading" + dateArray[i]);
        }
        return value;
    }

    /**Cache the User added Entry to a file**/
    public void addEntryToList(String entry) {
        try {
            if (getCachedString() != null) {
                StringBuffer stringBuffer = getCachedString();
                stringBuffer.append(entry).append("#");
                cacheStringData(String.valueOf(stringBuffer));
            } else {
                cacheStringData(entry);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
