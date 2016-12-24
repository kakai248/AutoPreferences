package com.kakai.android.autopreferences.backup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

class FileUtils {

    protected static void writeToFile(File f, String s) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(f));
        outputStreamWriter.write(s);
        outputStreamWriter.close();
    }

    protected static String readFromFile(File f) throws IOException {
        String ret;

        InputStream inputStream = new FileInputStream(f);

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String receiveString;
        StringBuilder stringBuilder = new StringBuilder();

        while ( (receiveString = bufferedReader.readLine()) != null ) {
            stringBuilder.append(receiveString);
        }

        inputStream.close();
        ret = stringBuilder.toString();

        return ret;
    }
}
