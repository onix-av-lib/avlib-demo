package com.onix.streamer.demo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

public class IoUtil {

    public static String getSyncHttp(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(2000);
            conn.setConnectTimeout(2000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream iStream = (InputStream) conn.getContent();
            return getString(iStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getString(InputStream is) throws IOException {
        if (is != null) {
            Writer writer = new StringWriter();
            char[] buffer = new char[16384];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 65535);
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }
}
