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

/**
 * Hello world!
 *
 */
public class App {

    private static final String FILENAME = "C:\\java\\maven-project\\users.txt";
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main( String[] args ) throws IOException, IllegalAccessException {
        /*ObjectMapper mapper = new ObjectMapper();
        User user = new User("admin", "password1234");
        String json = mapper.writeValueAsString(user);
        System.out.println(json);

        User deserializedUser = mapper.readValue(json, User.class);
        System.out.println( deserializedUser.getUserName());*/

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
            String strings = getFileContent();
            listUsers = getUsersFromJson(strings);
        }

            while (flag){
            user = new User();
            System.out.println("If you want to quit enter exit.");
            fields = User.class.getDeclaredFields();
            for (Field f:fields) {
                f.setAccessible(true);
                System.out.println(f.getName());
                str = reader.readLine();
                if(str.equalsIgnoreCase("exit")) {
                    flag=false;
                    break;
                }
                f.set(user, str);
            }
            if (flag){
                //checkUserName(user);
                ArrayList<User> listTempo = new ArrayList<User>(listUsers) ;
                boolean isExist=false;
                for (User u : listTempo) {
                    if (user.getUserName().equals(u.getUserName()))
                    {
                        System.out.println("This username is already exists. Try again.");
                        isExist=true;
                    }
                }
                if (!isExist){
                    listUsers.add(user);
                }
            }
        }

        try {
            if(reader!=null) {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*for (User u:listUsers) {
            System.out.println(u);
        }*/
        String json = getJson(listUsers);
        //System.out.println(json);
        writeToFile(json);
        /*System.out.println("----------");
        String strings = getFileContent();
        System.out.println(strings);
        List<User> usersFromFile = getUsersFromJson(strings);
        for (User u:usersFromFile) {
            System.out.println(u);
        }*/
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