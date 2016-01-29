package EnvironmentConfiguration.controller;

import EnvironmentConfiguration.model.RegisterFileErrorInvalidMessage;
import EnvironmentConfiguration.model.RegisterFileErrorMissingMessage;
import EnvironmentConfiguration.model.RegisterInvalid;
import EnvironmentConfiguration.model.RegisterMissing;

import java.util.ArrayList;

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

    public static ArrayList<String> generateFromSplittingStrings(String line){
        String tempo = "";
        boolean isReading = false;
        ArrayList<String> stringProcessed = new ArrayList<String>();
        for(int x = 0; x < line.length(); x++){
            if(line.charAt(x) != ','){
                tempo += line.charAt(x);
                isReading = true;
            }
            else if(line.charAt(x) == ','){
                if(isReading) {
                    stringProcessed.add(tempo);
                    tempo = "";
                    isReading = false;
                }
                if(x < line.length() - 2)
                    if(line.charAt(x) == line.charAt(x + 1) && line.charAt(x) == ',' && x + 1 < line.length() - 1){
                        stringProcessed.add("");
                    }

            }
            if(x == line.length() - 1){
                stringProcessed.add(tempo);
                tempo = "";
            }

        }
        return stringProcessed;
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
}
