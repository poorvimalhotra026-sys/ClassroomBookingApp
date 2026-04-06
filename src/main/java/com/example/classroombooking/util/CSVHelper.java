package com.example.classroombooking.util;

import com.example.classroombooking.model.BookingRequest;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {

    public static List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<>();
        String line = "";
        String splitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip the header row
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] row = line.split(splitBy);
                data.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}