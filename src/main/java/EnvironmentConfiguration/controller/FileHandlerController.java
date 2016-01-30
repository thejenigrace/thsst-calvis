package EnvironmentConfiguration.controller;

import EnvironmentConfiguration.model.file_handling.FilePathList;
import EnvironmentConfiguration.model.file_handling.ForSomething;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    public static void WriteLocationFile(String filename, ArrayList<FilePathList> filePathListArrayList, ArrayList<String> selectedIndexes){
        Path filePath = Paths.get(filename);
        ArrayList<String> writables = new ArrayList<String>();
        for(int x = 0; x < filePathListArrayList.size(); x++){
            writables.add(filePathListArrayList.get(x).getCategory());
            writables.add("/*StartOfFile*/");
            for(int y = 0; y < filePathListArrayList.get(x).size(); y++){
                writables.add(filePathListArrayList.get(x).get(y).getLocation());
                writables.add("/*EndOfFile*/");
            }

        }
        writables.add("/*StartOfIndex*/");
        for(int x = 0; x < selectedIndexes.size(); x++){
            writables.add(selectedIndexes.get(x));
        }
        writables.add("/*EndOfIndex*/");

        try {
            Files.write(filePath, writables, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ForSomething loadFilenames(String filename){
        ArrayList<ArrayList<String>> completeFileNames = new ArrayList<ArrayList<String>>();
        ArrayList<String> filepathList = null;
        ArrayList<Integer> selectedIndexes = new ArrayList<Integer>();
        //ForSomething fs = new ForSomething(completeFileNames, selectedIndexes);

        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
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
        return new ForSomething(completeFileNames, selectedIndexes);
    }
}
