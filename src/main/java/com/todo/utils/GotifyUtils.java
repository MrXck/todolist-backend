package com.todo.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

public class GotifyUtils {

    public static void sendTitleAndContent(String url, String title, String content) {
        Map<String, String> data = new HashMap<>();
        data.put("title", title);
        data.put("message", content);
        HttpUtil.post(url, JSONUtil.toJsonStr(data));
    }
}
