package com.todo.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class BarkUtils {

    public static void sendTitleAndContent(String url, String title, String content) throws UnsupportedEncodingException {
        Map<String, String> data = new HashMap<>();
        data.put("title", title);
        data.put("body", content);
        HttpUtil.post(url, JSONUtil.toJsonStr(data));
    }
}
