package model23.bridge.copy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileDisplay extends DisplayImpl {
    private String filename;
    private BufferedReader reader;
    private final int MAX_READHEAD_LIMIT = 4096;
    public FileDisplay(String filename) {
        this.filename = filename;
    }
    @Override
    public void rawOpen() {
        try {
            reader = new BufferedReader(new FileReader(filename));
            reader.mark(MAX_READHEAD_LIMIT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("==========="+filename+"============");

    }

    @Override
    public void rawPrint() {
        try {
            String line;
            reader.reset();
            while ((line = reader.readLine()) != null){
                System.out.println(">"+line);

            }
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    @Override
    public void rawClose() {
        // TODO Auto-generated method stub
        System.out.println("==============");
        try {
            reader.close();
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }
}
