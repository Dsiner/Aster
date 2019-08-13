package com.d.lib.aster.integration.http.body;

import com.d.lib.aster.utils.Util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * BodyWriter
 * Created by D on 2018/12/10.
 **/
public class BodyWriter {
    public static final String BOUNDARY = java.util.UUID.randomUUID().toString();
    public static final String TWO_HYPHENS = "--";
    public static final String LINE_END = "\r\n";

    @Deprecated
    private static String getParamsString(Map<String, String> paramsMap) {
        StringBuilder sb = new StringBuilder();
        for (String key : paramsMap.keySet()) {
            String value = paramsMap.get(key);
            sb.append(getParamsString(key, value));
        }
        return sb.toString();
    }

    @Deprecated
    private static String getParamsString(String key, String value) {
        StringBuilder sb = new StringBuilder();
        sb.append(TWO_HYPHENS);
        sb.append(BOUNDARY);
        sb.append(LINE_END);
        sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"");
        sb.append(LINE_END);
        sb.append("Content-Type: " + "text/plain");
        sb.append(LINE_END);
        sb.append("Content-Length: ").append(value.length());
        sb.append(LINE_END);
        sb.append(LINE_END);
        sb.append(value);
        sb.append(LINE_END);
        return sb.toString();
    }

    @Deprecated
    public static void writeFile(final DataOutputStream outputStream,
                                 final File file, final String name, final String mimeType,
                                 final RequestBody.ForwardingSink forwardingSink) throws IOException {
        outputStream.write(getFileParamsString(file, name, mimeType).getBytes());
        outputStream.flush();

        FileInputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024 * 4];
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
        byte[] buffer = new byte[1024 * 4];
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

    @Deprecated
    private static String getFileParamsString(File file, String name, String mimeType) {
        StringBuilder sb = new StringBuilder();
        sb.append(LINE_END);
        sb.append(TWO_HYPHENS);
        sb.append(BOUNDARY);
        sb.append(LINE_END);
        sb.append("Content-Disposition: form-data; name=\"").append(name)
                .append("\"; filename=\"").append(file.getName()).append("\"");
        sb.append(LINE_END);
        sb.append("Content-Type: ").append(mimeType);
        sb.append(LINE_END);
        sb.append("Content-Length: ").append(file.length());
        sb.append(LINE_END);
        sb.append(LINE_END);
        return sb.toString();
    }

    @Deprecated
    private static void post(OutputStream out,
                             Map<String, String> params,
                             List<FileParams> fileParams,
                             String charset) throws IOException {
        final String boundary = System.currentTimeMillis() + "";
        final String contentType = "multipart/form-data;charset=" + charset + ";boundary=" + boundary;
        try {
            final byte[] entryBoundaryBytes = ("\r\n--" + boundary + "\r\n").getBytes(charset);
            final Set<Map.Entry<String, String>> paramSet = params.entrySet();
            for (Map.Entry<String, String> param : paramSet) {
                byte[] bytes = getFormEntry(param.getKey(), param.getValue()).getBytes(charset);
                out.write(entryBoundaryBytes);
                out.write(bytes);
            }
            for (FileParams file : fileParams) {
                byte[] fileBytes = getFileEntry(file.name, file.fileName, file.mimeType)
                        .getBytes(charset);
                out.write(entryBoundaryBytes);
                out.write(fileBytes);
                out.write(file.content);
            }
            final byte[] endBoundaryBytes = ("\r\n--" + boundary + "--\r\n")
                    .getBytes(charset);
            out.write(endBoundaryBytes);
        } catch (IOException e) {
            throw e;
        }
    }

    @Deprecated
    private static String getFormEntry(String fieldName, String fieldValue) throws IOException {
        StringBuilder entry = new StringBuilder();
        entry.append("Content-Disposition:form-data;name=\"");
        entry.append(fieldName);
        entry.append("\"");
        entry.append(LINE_END);
        entry.append("Content-Type:text/plain");
        entry.append(LINE_END);
        entry.append(LINE_END);
        entry.append(fieldValue);
        return entry.toString();
    }

    @Deprecated
    private static String getFileEntry(String name, String fileName, String mimeType) throws IOException {
        StringBuilder entry = new StringBuilder();
        entry.append("Content-Disposition:form-data;name=\"");
        entry.append(name);
        entry.append("\";filename=\"");
        entry.append(fileName);
        entry.append("\"");
        entry.append(LINE_END);
        entry.append("Content-Type:");
        entry.append(mimeType);
        entry.append(LINE_END);
        entry.append(LINE_END);
        return entry.toString();
    }

    public static class FileParams {
        public String name;
        public String fileName;
        public String mimeType;
        public byte[] content;
    }
}
