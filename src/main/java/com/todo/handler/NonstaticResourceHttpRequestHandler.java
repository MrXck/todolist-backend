package com.todo.handler;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xck
 */
@Component
public class NonstaticResourceHttpRequestHandler extends ResourceHttpRequestHandler {

    public final static String ATTR_FILE = "NON-STATIC-FILE";

    @Override
    protected Resource getResource(HttpServletRequest request) {
        String filePath =  (String) request.getAttribute(ATTR_FILE);
        return new FileSystemResource(filePath);
    }
}