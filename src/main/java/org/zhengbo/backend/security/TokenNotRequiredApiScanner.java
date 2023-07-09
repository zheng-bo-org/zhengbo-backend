package org.zhengbo.backend.security;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TokenNotRequiredApiScanner {
    private final static Logger log = LoggerFactory.getLogger(TokenNotRequiredApiScanner.class);

    private static String findPathFromTheMethod(Method method) {
        if (method.getAnnotation(GetMapping.class) != null) {
            return method.getAnnotation(GetMapping.class).value()[0];
        }

        if (method.getAnnotation(PostMapping.class) != null) {
            return method.getAnnotation(PostMapping.class).value()[0];
        }

        if (method.getAnnotation(DeleteMapping.class) != null) {
            return method.getAnnotation(DeleteMapping.class).value()[0];
        }

        if (method.getAnnotation(PutMapping.class) != null) {
            return method.getAnnotation(PutMapping.class).value()[0];
        }

        if (method.getAnnotation(PatchMapping.class) != null) {
            return method.getAnnotation(PatchMapping.class).value()[0];
        }

        throw new RuntimeException("No path could be found in the method: " + method.getName());
    }

    public static List<String> scan() {
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackage("org.zhengbo.backend.controller"));
        var classes = reflections.getTypesAnnotatedWith(RequestMapping.class);
        List<String> paths = new ArrayList<>(16);
        for (Class<?> clz : classes) {
            var rootPath = clz.getAnnotation(RequestMapping.class).value()[0];
            for (Method method : clz.getMethods()) {
                var tokenNotRequired = method.getAnnotation(TokenNotRequired.class);
                if (tokenNotRequired == null) {
                    continue;
                }
                var subPath = findPathFromTheMethod(method);
                paths.add(rootPath + subPath);
            }
        }

        return paths;
    }
}
