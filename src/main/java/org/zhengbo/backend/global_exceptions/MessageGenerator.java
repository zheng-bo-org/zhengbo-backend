package org.zhengbo.backend.global_exceptions;

import org.reflections.Reflections;
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
        var classes = new Reflections(clz).getTypesAnnotatedWith(GlobalException.MessagesInEnum.class);
        HashMap<String, String> messages = new HashMap<>(16);
        for (Class<?> messagesInEnumClz : classes) {
            for (Field field : messagesInEnumClz.getFields()) {
                var descSet = field.getDeclaredAnnotationsByType(GlobalException.EnumMsgDesc.class);
                String desc = "";
                for (GlobalException.EnumMsgDesc descAnnotation : descSet) {
                    desc = descAnnotation.desc();
                }
                var name = field.getName().toLowerCase();

                messages.put(name, desc);
            }

        }
        return new Messages(key, messages);
    }

    private static void writeToFile(String json, File file) throws Exception{
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
        }
    }

    public static String getJson() throws Exception{
        var classes = new Reflections("org.zhengbo.backend.global_exceptions").getTypesAnnotatedWith(GlobalException.MessageAbleException.class);
        var messages = classes.stream().map(MessageGenerator::toMessages).toList();
        HashMap<String, HashMap<String, String>> finalMessages = new HashMap<>(16);
        for (Messages msg : messages) {
            finalMessages.put(msg.key, msg.messages());
        }
        return JSON.toJson(finalMessages);
    }

    public static void generateMsg(File file) throws Exception {
        System.out.println("generateMsg!!!!");
        String json = getJson();
        writeToFile(json, file);
    }
}
