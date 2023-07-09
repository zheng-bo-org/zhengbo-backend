package org.zhengbo.backend.global_exceptions;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.zhengbo.backend.utils.JSON;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.HashMap;

public class MessageGenerator {
    record Messages(String key, HashMap<String, String> messages) {

    }

    private static Messages toMessages(Class<?> clz) {
        String key = clz.getSimpleName();
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackage("org.zhengbo.backend"));
        var classes = reflections.getTypesAnnotatedWith(GlobalException.MessagesInEnum.class);
        HashMap<String, String> messages = new HashMap<>(16);
        for (Class<?> messagesInEnumClz : classes) {
            for (Field field : messagesInEnumClz.getFields()) {
                var descSet = field.getDeclaredAnnotationsByType(GlobalException.EnumMsgDesc.class);
                String desc = "";
                for (GlobalException.EnumMsgDesc descAnnotation : descSet) {
                    desc = descAnnotation.desc();
                }
                var name = field.getName();

                messages.put(name, desc);
            }

        }
        return new Messages(key, messages);
    }

    public static HashMap<String, HashMap<String, String>> getMessages() throws Exception{
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackage("org.zhengbo.backend.global_exceptions"));
        var classes = reflections.getTypesAnnotatedWith(GlobalException.MessageAbleException.class);
        var messages = classes.stream().map(MessageGenerator::toMessages).toList();
        HashMap<String, HashMap<String, String>> finalMessages = new HashMap<>(16);
        for (Messages msg : messages) {
            finalMessages.put(msg.key, msg.messages());
        }
        return finalMessages;
    }
}
