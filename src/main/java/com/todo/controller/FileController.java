package com.todo.controller;

import com.todo.exception.APIException;
import com.todo.utils.Constant;
import com.todo.utils.NoAuthorization;
import com.todo.utils.NotControllerResponseAdvice;
import com.todo.utils.UserThreadLocal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    @PostMapping(value = "/upload")
    @NotControllerResponseAdvice
    public Map<String, Object> upload(@RequestParam("editormd-image-file") MultipartFile multipartFile) throws Exception {
        Map<String, Object> map = new HashMap<>();

        File file = new File(Constant.PATH);
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            String[] split = multipartFile.getOriginalFilename().split("\\.");

            if (split.length < 2) {
                throw new APIException("上传的文件有误");
            }

            if (!Constant.SUFFIX_WHITE_LIST.contains(split[1])) {
                throw new APIException("上传的文件格式不支持");
            }

            String path = UserThreadLocal.get().toString() + "_" + UUID.randomUUID() + "." + split[1];
            multipartFile.transferTo(new File(Constant.PATH + path));

            map.put("success", 1);
            map.put("url", "/file/download/" + path);
        } catch (Exception e) {
            map.put("success", 0);
            map.put("message", "文件上传错误");
            e.printStackTrace();
        }

        return map;
    }

    @GetMapping("/download/{filename}")
    @NoAuthorization
    public void download(@PathVariable("filename") String filename, HttpServletResponse response) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(Constant.PATH + filename));
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/octet-stream");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
        }
    }
}
