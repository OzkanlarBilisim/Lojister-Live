package com.lojister.core.helper;

import org.springframework.http.MediaType;

public class ContentTypeHelper {

    public static String getDataByContentType(String fileExtension){

        String contentType="";

        if("pdf".equals(fileExtension)){
            contentType = MediaType.APPLICATION_PDF_VALUE;
        }
        else if ("png".equals(fileExtension)){
            contentType = MediaType.IMAGE_PNG_VALUE;
        }
        else if("jpg".equals(fileExtension)){
            contentType = MediaType.IMAGE_JPEG_VALUE;
        }
        else if("jpeg".equals(fileExtension)){
            contentType = MediaType.IMAGE_JPEG_VALUE;
        }
        else if("gif".equals(fileExtension)){
            contentType = MediaType.IMAGE_GIF_VALUE;
        }
        else if("txt".equals(fileExtension)){
            contentType = MediaType.TEXT_PLAIN_VALUE;
        }
        else if("png".equals(fileExtension)){
            contentType = MediaType.IMAGE_PNG_VALUE;
        }
        else if("mov".equalsIgnoreCase(fileExtension)){
            contentType = "video/mp4";
        }
        else if("mp4".equalsIgnoreCase(fileExtension)){
            contentType = "video/mp4";
        }

        return contentType;
    }
}
