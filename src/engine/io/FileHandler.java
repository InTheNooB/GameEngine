package engine.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class FileHandler {

    public static ArrayList<String> readTextFile(String filepath) {
        ArrayList<String> lines = null;
        BufferedReader br = null;

        try {
            br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(filepath), "UTF8"));
            lines = new ArrayList<String>();

            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

            br.close();
            br = null;
        } catch (Exception e) {
            lines = null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                }
            }
        }
        return lines;

    }

    public static boolean writeTextFile(String filepath, List<String> linesToWrite) {
        boolean result = false;
        if (linesToWrite != null) {
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(filepath, false), "UTF8"));

                for (String line : linesToWrite) {
                    if (line != null) {
                        bw.write(line);
                        bw.newLine();
                    }
                }
                bw.flush();
                bw.close();
                bw = null;
                result = true;
            } catch (Exception e) {
            } finally {
                if (bw != null) {
                    try {
                        bw.close();
                        bw = null;
                        if (!result) {
                            new File(filepath).delete();
                        }
                    } catch (IOException e) {
                    }
                }
            }
        }
        return result;
    }

    public static boolean appendToTextFile(String filepath, String newLineContent) {
        boolean result = false;
        if (newLineContent != null) {
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(filepath, true), "UTF8"));

                bw.write(newLineContent);
                bw.newLine();
                bw.flush();
                bw.close();
                result = true;

            } catch (IOException e) {
            } finally {
                if (bw != null) {
                    try {
                        bw.close();
                        bw = null;
                    } catch (IOException e) {

                    }
                }
            }
        }
        return result;
    }

}
