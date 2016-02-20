package EnvironmentConfiguration.controller;

import EnvironmentConfiguration.model.error_logging.InstructionFileErrorInvalidMessage;
import EnvironmentConfiguration.model.error_logging.InstructionInvalid;
import EnvironmentConfiguration.model.file_handling.FilePathHandler;
import EnvironmentConfiguration.model.file_handling.FilePathList;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Ivan on 1/27/2016.
 */
public class FileHandlerController {

    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int extensionPos = filename.lastIndexOf('.');
        int lastUnixPos = filename.lastIndexOf('/');
        int lastWindowsPos = filename.lastIndexOf('\\');
        int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);

        int index = lastSeparator > extensionPos ? -1 : extensionPos;
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }

    public String checkIfFileExists(String filepath){
        File f = new File(filepath);
        ArrayList<String> pathString = new ArrayList<String>();
        pathString.add(filepath);
        if(!f.exists() && !filepath.isEmpty()) {
           return new InstructionFileErrorInvalidMessage(InstructionInvalid.invalidFilePath).generateMessage(pathString);
        }
        return "";
    }

//    public static void writeLocationFile(String filename, ArrayList<FilePathList> filePathListArrayList, ArrayList<String> selectedIndexes){
//        Path filePath = Paths.get(filename);
//        ArrayList<String> writable = new ArrayList<>();
//        for(int x = 0; x < filePathListArrayList.size(); x++){
//            writable.add(filePathListArrayList.get(x).getCategory());
//            writable.add("/*StartOfFile*/");
//            for(int y = 0; y < filePathListArrayList.get(x).size(); y++){
//                writable.add(filePathListArrayList.get(x).get(y).getLocation());
//            }
//            writable.add("/*EndOfFile*/");
//
//        }
//        writable.add("/*StartOfIndex*/");
//        for(int x = 0; x < selectedIndexes.size(); x++){
//            writable.add(selectedIndexes.get(x));
//        }
//        writable.add("/*EndOfIndex*/");
//
//        try {
//            Files.write(filePath, writable, Charset.forName("UTF-8"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static FilePathHandler loadFilenames(String filename){
        ArrayList<ArrayList<String>> completeFileNames = new ArrayList<ArrayList<String>>();
        ArrayList<String> filepathList = null;
        ArrayList<Integer> selectedIndexes = new ArrayList<Integer>();
        //FilePathHandler fs = new FilePathHandler(completeFileNames, selectedIndexes);

        try(BufferedReader br = new BufferedReader(new InputStreamReader(FileHandlerController.class.getResourceAsStream(filename)))) {
            String line = br.readLine();

            while (line != null) {
                boolean IsSkept = false;
                if(line.equals("/*StartOfFile*/")){
                    filepathList = new ArrayList<String>();
                }
                else if(line.equals("/*EndOfFile*/")){
                    completeFileNames.add(filepathList);
                }
                else if(line.equals("Memory File") || line.equals("Register File") || line.equals("Instruction File") || line.equals("/*StartOfIndex*/") || line.equals("/*EndOfIndex*/")){
                    line = br.readLine();
                    IsSkept = true;
                }
                else if(line.length() == 1){
                    selectedIndexes.add(Integer.parseInt(line));
                }
                else{
                    filepathList.add(line);
                    System.out.println(line);
                }
                if(!IsSkept) {
                    line = br.readLine();
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new FilePathHandler(completeFileNames, selectedIndexes);
    }
}
