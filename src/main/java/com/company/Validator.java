package com.company;

import com.company.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static void validate(String str,Field field) throws IllegalAccessException {
        field.setAccessible(true);
            Annotation[] annotations = field.getAnnotations();
            for (Annotation a:annotations) {
                validateAnnotation(a,  field, str);
            }
    }

    private static void validateAnnotation(Annotation annotation, Field field,String user) throws IllegalAccessException {
      if(annotation.annotationType().equals(Email.class)){
          validateEmail(user);
      }
      if(annotation.annotationType().equals(NotNull.class)){
          validateNotNull(user);
      }
      if(annotation.annotationType().equals(LengthString.class)){
          LengthString lengthString = field.getAnnotation(LengthString.class);
          validateStringLength(user, lengthString.minValue(), lengthString.maxValue());
      }
      if(annotation.annotationType().equals(LengthInteger.class)){
          LengthInteger lengthInteger = field.getAnnotation(LengthInteger.class);
          validationIntegerLength(user, lengthInteger.minValue(), lengthInteger.maxValue());
      }

    }

    public static void validateNotNull(Object o){
        if(o.equals(null) || o.equals("")){
            throw new RuntimeException("Field is null");
        }
        String str = (String) o;
        if(str.replaceAll("\\s+","").equals("")){
            throw new RuntimeException("Field is null");
        }
    }

    public static void validateEmail(String str){

        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{1,3})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(str);
        if(!matcher.find()){
            throw new RuntimeException("Invalid email.");
        }
    }

    public static void validateStringLength(String str, int min, int max){
        if(str.equals(null)){
            throw new RuntimeException("Value is null");
        }
        if(str.length()<=max && str.length()>=min){
            return;
        }
        throw new RuntimeException("Length is not valid. It might be between "+ min +" and " +max);
    }

    public static void validationIntegerLength(String str, int min, int max){
        try {
            Integer i = Integer.parseInt(str);
            if(i!=0){
                if((str.length()>=min && str.length()<=max)) {
                    return;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Field must be a number");
        }
        throw new RuntimeException("Field must be number and between " + min + " and " + max);
    }

    public static String getFieldName(Field field){
        if(field.getAnnotation(PrintAnnotation.class)!=null){
            PrintAnnotation annotation = field.getAnnotation(PrintAnnotation.class);
            return annotation.printValue();
        }
        return field.getName();
    }

}
