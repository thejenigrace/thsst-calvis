package EnvironmentConfiguration.controller;

import EnvironmentConfiguration.model.error_logging.RegisterFileErrorInvalidMessage;
import EnvironmentConfiguration.model.error_logging.RegisterFileErrorMissingMessage;
import EnvironmentConfiguration.model.error_logging.RegisterInvalid;
import EnvironmentConfiguration.model.error_logging.RegisterMissing;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Ivan on 1/28/2016.
 */
public class HandleConfigFunctions {
    public static ArrayList<String> generateArrayListString(String... stringList){
        ArrayList<String> stringArrayList = new ArrayList<String>();
        for(int i = 0; i < stringList.length; i++){
            stringArrayList.add(stringList[i]);
        }

        return stringArrayList;
    }

//    public static ArrayList<Enum> generateArrayListEnum(Enum... enumList){
//        ArrayList<Enum> enumLists = new ArrayList<Enum>();
//        for(int i = 0; i < enumList.length; i++){
//            enumLists.add(enumList[i]);
//        }
//
//        return enumLists;
//    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    public static String[] adjustAnArray(String[] reg, int offSet){
        String[] adjustArray = new String[reg.length + offSet];
        System.arraycopy(reg, 0, adjustArray, 0, reg.length);
        reg = adjustArray;
        return reg;
    }

    public static ArrayList<String> checkifMissing(String[] strArr){
        ArrayList<String> missingArr = new ArrayList<String>();
        for(int x = 0; x < strArr.length; x++){
            if(strArr[x].isEmpty()) {
                switch (x) {
                    case 0:
                        missingArr.add(new RegisterFileErrorMissingMessage(RegisterMissing.missingSourceRegister).generateMessage());
                        break;
                    case 1:
                        missingArr.add(new RegisterFileErrorMissingMessage(RegisterMissing.missingNewRegister).generateMessage());
                        break;
                    case 2:
                        missingArr.add(new RegisterFileErrorMissingMessage(RegisterMissing.missingRegisterSize).generateMessage());
                        break;
                    case 3:
                        missingArr.add(new RegisterFileErrorMissingMessage(RegisterMissing.missingRegisterType).generateMessage());
                        break;
                    case 4:
                        missingArr.add(new RegisterFileErrorMissingMessage(RegisterMissing.missingRegisterStartIndex).generateMessage());
                        break;
                    case 5:
                        missingArr.add(new RegisterFileErrorMissingMessage(RegisterMissing.missingRegisterEndIndex).generateMessage());
                        break;
                }
            }
        }
        return missingArr;
    }


    public static ArrayList<String> checkForInvalidInput(String[] strArr){
        ArrayList<String> invalidArr = new ArrayList<String>();
        for(int x = 0; x < strArr.length; x++){
            if(!strArr[x].matches("[0-9]+")) {
                switch (x) {
//                    case 0:
//                        invalidArr.add(new RegisterFileErrorInvalidMessage(RegisterInvalid.invalidSourceRegister).generateMessage());
//                        break;
//                    case 1:
//                        invalidArr.add(new RegisterFileErrorInvalidMessage(RegisterInvalid.invalidNewRegister).generateMessage());
//                        break;
                    case 2:
                        invalidArr.add(new RegisterFileErrorInvalidMessage(RegisterInvalid.invalidRegisterSize).generateMessage());
                        break;
                    case 3:
                        invalidArr.add(new RegisterFileErrorInvalidMessage(RegisterInvalid.invalidRegisterType).generateMessage());
                        break;
                    case 4:
                        invalidArr.add(new RegisterFileErrorInvalidMessage(RegisterInvalid.invalidRegisterStartIndex).generateMessage());
                        break;
                    case 5:
                        invalidArr.add(new RegisterFileErrorInvalidMessage(RegisterInvalid.invalidRegisterEndIndex).generateMessage());
                        break;
                }
            }
        }
        return invalidArr;
    }

    public static String[] split(String line, char characterToSplit) {
        char[] charArr = line.toCharArray();
        int numberOfCommas = 0;
        for ( char x : charArr ){
            if ( x == characterToSplit ){
                numberOfCommas++;
            }
        }
        String charSplit = "";
        charSplit += characterToSplit;
        String[] array = new String[numberOfCommas+1];
        String[] lineArray = line.split(charSplit);
        for (int i = 0; i < array.length; i++) {
            if ( i <= lineArray.length - 1){
                array[i] = lineArray[i];
            }
            else {
                array[i] = "";
            }
        }
        return array;
    }

    public static boolean StringSearchInstruction (String[] a, String key) {
        for(int x = 0; x < a.length; x++){
            if(key.equals(a[x])){
                return true;
            }
        }
        return false;
    }

    public static boolean StringSearchContains (String[] a, String key) {
        String tempo = key.substring(1);
        for(int x = 0; x < a.length; x++) {
            if ((a[x].charAt(0) == key.charAt(0))) {
                if (tempo.length() == 0)
                    return true;
                else if (!Pattern.matches("[a-zA-Z]+", tempo))
                    return true;
            }
        }
        return false;
    }

    public static String[] removeAllSpecificElements(String[] array, String key){
        for(int x = 0; x < array.length; x++){
            if(array[x].equals(key))
                array = ArrayUtils.remove(array, x);
        }
        return array;
    }

}
