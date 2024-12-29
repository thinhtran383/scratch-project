package org.example.lib.utils;

import org.example.lib.models.Account;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileUtil<T> {


    public interface MapFunction<T> {
        T map(String[] fields);
    }

    public interface MapToStringFunction<T> {
        String mapToString(T item);
    }

    public List<T> readFile(String filePath, MapFunction<T> mapFunction) {
        List<T> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line; // try catch resource
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                result.add(mapFunction.map(data));
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return result;
    }

    public void writeFile(String filePath, List<T> data, MapToStringFunction<T> mapToStringFunction) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            for (T item : data) {
                String line = mapToStringFunction.mapToString(item); // cac truong cach nhau boi dau phay
                writer.write(line);
                writer.newLine();
            }




        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
