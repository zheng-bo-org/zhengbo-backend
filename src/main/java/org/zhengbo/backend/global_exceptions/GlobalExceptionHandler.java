package org.zhengbo.backend.global_exceptions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.zhengbo.backend.utils.JSON;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GlobalExceptionHandler implements HandlerExceptionResolver {
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final HashMap<String, HashMap<String, String>> messages = new HashMap<>(16);

    public GlobalExceptionHandler() throws Exception {
        var messages = MessageGenerator.getMessages();
        this.messages.putAll(messages);
    }

    private void noSuchMessageFoundError(ModelAndView view, Exception ex) {
        view.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        view.addObject("errCode","UNKNOWN_ERROR");
        view.addObject("errMsg", "No messages found for the Exception: " + ex.getClass().getSimpleName());
        log.error("No messages found for the Exception: {}", ex.getClass().getName());
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ex.printStackTrace();
        response.setContentType("application/json");

        ModelAndView model = new ModelAndView();
        model.setView(new MappingJackson2JsonView());

        if (ex instanceof HttpMessageNotReadableException) {
            model.addObject("errCode", "INVALID_DATA_FORMAT");
            model.addObject("errMsg", ex.getMessage());
            model.setStatus(HttpStatus.BAD_REQUEST);
            return model;
        }

        if (ex instanceof MethodArgumentNotValidException) {
            var bindRs = ((MethodArgumentNotValidException) ex).getBindingResult();

            model.addObject("errCode", "BAD_REQUEST");
            model.addObject("errMsg", Objects.requireNonNull(bindRs.getFieldError()).getDefaultMessage());
            model.setStatus(HttpStatus.BAD_REQUEST);
            return model;
        }


        String key = ex.getClass().getSimpleName();
        HashMap<String, String> msg = messages.get(key);
        if (msg == null) {
            noSuchMessageFoundError(model, ex);
            return model;
        }

        GlobalException globalException = (GlobalException) ex;
        response.setStatus(globalException.getStatusCode().value());
        String code = globalException.getCode();
        String realMsg = msg.get(code);
        model.addObject("errCode", code);
        model.addObject("errMsg", realMsg);
        return model;
    }
}
