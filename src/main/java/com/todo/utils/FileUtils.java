package com.todo.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;


/**
 * @author xck
 */
public class FileUtils {

    public static void file(HttpServletResponse response, String filename) throws IOException {
        BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(filename));
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("application/octet-stream");
        response.setHeader("content-type", "application/octet-stream");
        int len;
        byte[] bytes = new byte[1024];
        while ((len = fileInputStream.read(bytes)) != -1){
            outputStream.write(bytes, 0, len);
            outputStream.flush();
        }
        outputStream.close();
        fileInputStream.close();
    }

    public static String uploadFile(MultipartFile file) throws IOException {
        File file1 = new File(Constant.PATH);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + suffix;
        file.transferTo(new File(Constant.PATH + filename));
        return filename;
    }
}
