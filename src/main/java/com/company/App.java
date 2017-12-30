package com.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static final String FILENAME = "C:\\java\\maven-project\\users.txt";
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main( String[] args ) throws IOException, IllegalAccessException {

        writeUser();

    }

    private static void writeUser() throws IOException, IllegalAccessException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        User user;
        List<User> listUsers = new ArrayList();
        File file = new File(FILENAME);
        Field[] fields;
        String str = "";
        boolean flag = true;

        if(file.exists()) {
            listUsers = getUsersFromJson(getFileContent());
        }

            while (flag){
            user = new User();
            System.out.println("If you want to quit enter exit.");
            fields = User.class.getDeclaredFields();

            for (Field f:fields) {
                f.setAccessible(true);
                System.out.println(Validator.getFieldName(f));
                str = reader.readLine();
                Validator.validate(str, f);
                if(str.equalsIgnoreCase("exit")) {
                    return;

                }
                f.set(user, str);
            }

                ArrayList<User> listTempo = new ArrayList<User>(listUsers) ;
                boolean isExist=false;
                for (User u : listTempo) {
                    if (user.getUserName().equals(u.getUserName())) {
                        System.out.println("This username is already exists. Try again.");
                        isExist=true;
                    }
                }
                if (!isExist){
                    listUsers.add(user);
                    writeToFile(getJson(listUsers));
                }
            }

        try {
            if(reader!=null) {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getJson(List<User> listUsers) throws JsonProcessingException {
        return objectMapper.writeValueAsString(listUsers);
    }

    private static void writeToFile(String json) throws IOException {
        FileUtils.write(new File(FILENAME), json);
    }

    private static String getFileContent() throws IOException {
        return FileUtils.readFileToString(new File(FILENAME));
    }

    private static List<User> getUsersFromJson(String json)throws IOException{
        return objectMapper.readValue(json, new TypeReference<List<User>>(){});
    }

}