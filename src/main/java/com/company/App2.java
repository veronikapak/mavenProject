package com.company;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.List;


public class App2 {
    private static final String FILENAME = "C:\\java\\maven-project\\users.txt";
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main( String[] args ) throws IOException, IllegalAccessException {
        User user = writeUsername();
        String strings = getFileContent();
        List<User> listUser = getUsersFromJson(strings);
        checkUser(user, listUser);
    }

    private static void checkUser(User user, List<User> listUser) {
        File file = new File(FILENAME);
        if (!file.exists()){
            System.out.println("This user is not exists.");
        }

        boolean check = false;
        for (User u : listUser){
            if (u.getUserName().equalsIgnoreCase(user.getUserName())
                    && u.getPassword().equalsIgnoreCase(user.getPassword()))
            {
                System.out.println(u);
                check = true;
                break;
            }
        }

        if(!check){
            System.out.println("This user is not exists.");
        }
    }



    private static List<User> getUsersFromJson(String json) throws IOException {
        return objectMapper.readValue(json, new TypeReference<List<User>>(){});
    }

    private static String getFileContent() throws IOException {
        return FileUtils.readFileToString(new File(FILENAME));
    }

    private static User writeUsername() throws IOException, IllegalAccessException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String str = "";
        User user = new User();
        Field[] fields = User.class.getDeclaredFields();

            for (Field f:fields) {
                f.setAccessible(true);
                System.out.println(f.getName());
                str = bufferedReader.readLine();
                f.set(user, str);
            }

        try {
            if(bufferedReader!=null) {
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }
}
