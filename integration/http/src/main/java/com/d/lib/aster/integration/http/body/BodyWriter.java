package com.d.lib.aster.integration.http.body;

import com.d.lib.aster.utils.Util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * BodyWriter
 * Created by D on 2018/12/10.
 **/
public class BodyWriter {
    public static final String BOUNDARY = java.util.UUID.randomUUID().toString();
    public static final String TWO_HYPHENS = "--";
    public static final String LINE_END = "\r\n";

    public static String getParamsString(Map<String, String> paramsMap) {
        StringBuilder sb = new StringBuilder();
        for (String key : paramsMap.keySet()) {
            String value = paramsMap.get(key);
            sb.append(getParamsString(key, value));
        }
        return sb.toString();
    }

    public static String getParamsString(String key, String value) {
        StringBuilder sb = new StringBuilder();
        sb.append(TWO_HYPHENS);
        sb.append(BOUNDARY);
        sb.append(LINE_END);
        sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"");
        sb.append(LINE_END);
        sb.append("Content-Type: " + "text/plain");
        sb.append(LINE_END);
        sb.append("Content-Lenght: ").append(value.length());
        sb.append(LINE_END);
        sb.append(LINE_END);
        sb.append(value);
        sb.append(LINE_END);
        return sb.toString();
    }



    public static void writeFile(File file, String fileKey, String fileType,
                                 final DataOutputStream outputStream,
                                 final RequestBody.ForwardingSink forwardingSink) throws IOException {
        outputStream.write(getFileParamsString(file, fileKey, fileType).getBytes());
        outputStream.flush();

        FileInputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024 * 2];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
            if (forwardingSink != null) {
                forwardingSink.write(outputStream, length);
            }
        }
        outputStream.flush();
        Util.closeQuietly(inputStream);
    }

    public static void writeInputStream(InputStream inputStream,
                                        final DataOutputStream outputStream,
                                        final RequestBody.ForwardingSink forwardingSink) throws IOException {
        byte[] buffer = new byte[1024 * 2];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
            if (forwardingSink != null) {
                forwardingSink.write(outputStream, length);
            }
        }

        outputStream.flush();
        Util.closeQuietly(inputStream);
    }

    public static String getFileParamsString(File file, String fileKey, String fileType) {
        StringBuilder sb = new StringBuilder();
        sb.append(LINE_END);
        sb.append(TWO_HYPHENS);
        sb.append(BOUNDARY);
        sb.append(LINE_END);
        sb.append("Content-Disposition: form-data; name=\"").append(fileKey)
                .append("\"; filename=\"").append(file.getName()).append("\"");
        sb.append(LINE_END);
        sb.append("Content-Type: ").append(fileType);
        sb.append(LINE_END);
        sb.append("Content-Length: ").append(file.length());
        sb.append(LINE_END);
        sb.append(LINE_END);
        return sb.toString();
    }
}
