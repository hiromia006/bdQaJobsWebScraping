package bdQaJobs.util;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class FileWriteAndReader {
    public static void WriteFile(String url) {
        String file = System.getProperty("user.dir") + "/src/main/resources/NewJobURL.txt";
        File fileName = new File(file);
        if (!fileName.exists()) {
            try {
                fileName.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Write text on txt file.
        String writeStr = url + "\n";
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(writeStr);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Set<String> getSaveJobs() {
        String file = System.getProperty("user.dir") + "/src/main/resources/OldJobs.txt";
        File fileName = new File(file);
        if (!fileName.exists()) {
            try {
                fileName.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Set<String> jobUrls = new HashSet<>();
        FileReader fr = null;
        try {
            fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String st;
            while ((st = br.readLine()) != null) {
                if (st.length() > 0 && st.contains("http")) {
                    jobUrls.add(st);
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jobUrls;
    }

    public static void copyWithoutOverWriting(String sourcePath, String destinationPath) {
        File sourceFileName = new File(sourcePath);

        File destinationFileName = new File(destinationPath);
        if (!destinationFileName.exists()) {
            try {
                destinationFileName.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileReader fr = null;
        try {
            fr = new FileReader(sourceFileName);
            BufferedReader br = new BufferedReader(fr);
            String st;
            while ((st = br.readLine()) != null) {
                if (st.length() > 0) {
                    // Write text on txt file.
                    String writeStr = st + "\n";
                    FileWriter fileWriter;
                    try {
                        fileWriter = new FileWriter(destinationFileName, true);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write(writeStr);
                        bufferedWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
