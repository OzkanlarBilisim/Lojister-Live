package com.lojister.core.util;

import lombok.Getter;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TempFileUtil {

    @Getter
    public static class TempFileResult implements AutoCloseable{
        private File file;
        private FileInputStream fileInputStream;
        private long length;

        @Override
        public void close() throws Exception {
            file.delete();
        }
    }

    public static TempFileResult cache(String filename, InputStream fileStream) throws IOException {
        String dt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmssSSS_"));
        File file = File.createTempFile(dt + filename,null);

        FileOutputStream outputStream = new FileOutputStream(file);
        IOUtils.copy(fileStream,outputStream);
        outputStream.flush();
        outputStream.getFD().sync();
        outputStream.close();

        TempFileResult tempFileResult = new TempFileResult();
        tempFileResult.file = file;
        tempFileResult.fileInputStream = new FileInputStream(file);
        tempFileResult.length = file.length();

        file.deleteOnExit();

        return tempFileResult;
    }
}
