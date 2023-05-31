package com.lojister.core.util;

import lombok.Getter;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUploadUtil {

    @Getter
    public static class FileResult{
        private InputStream fileStream;
        private String filename;
    }

    @Getter
    public static class FileListResult {
        private List<FileResult> resultList;
    }

    public static FileListResult getFileList(HttpServletRequest request){
        FileListResult fileResultList = new FileListResult();
        fileResultList.resultList = getFiles(request);
        return fileResultList;
    }
    
    public static List<FileResult> getFiles(HttpServletRequest request)  {
        ServletFileUpload upload = new ServletFileUpload();

        FileItemIterator iter;
        List<FileResult> resultList = new ArrayList<>();
        try {
            iter = upload.getItemIterator(request);

            // loop through each item
            while (iter.hasNext()) {
                FileItemStream item = iter.next();

                // check if the item is a file
                if (!item.isFormField()) {
                    FileResult fileResult = new FileResult();
                    fileResult.filename = item.getName();
                    fileResult.fileStream = item.openStream();
                    resultList.add(fileResult);
                    break;
                }
            }
        } catch (FileUploadException | IOException e) {
            // log / handle the error here as necessary
             e.printStackTrace();
             throw new com.lojister.core.exception.FileUploadException("dosya yüklenemedi");
        }
        return resultList;
    }
}
