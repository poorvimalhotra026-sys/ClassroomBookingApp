package com.example.classroombooking.service;

import com.example.classroombooking.model.Timetable;
import com.example.classroombooking.repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class CsvService {
    @Autowired private TimetableRepository timetableRepo;

    public void loadAllTimetables() {
        String[] files = {"TIMETABLES DATABASE - BTECH CE DIV B.csv", "TIMETABLES DATABASE - BTECH CE DIV C.csv", "TIMETABLES DATABASE - MBATECH CE DIV E.csv"};
        for (String f : files) parse(f);
    }

    private void parse(String fileName) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) return;
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String headerLine = br.readLine();
            if (headerLine == null) return;
            String[] headers = headerLine.split(",");
            String line;
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (cols.length < 2) continue;
                String time = cols[0].trim().replace(" ", "").replace("08:00", "8:00").replace("09:00", "9:00");
                for (int i = 1; i < cols.length && i < headers.length; i++) {
                    String cell = cols[i].trim().replace("\"", "");
                    if (cell.equals("—") || cell.equalsIgnoreCase("BREAK") || cell.isEmpty()) continue;
                    for (String s : cell.split("/")) {
                        saveEntry(s.trim(), headers[i].trim(), time);
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void saveEntry(String text, String day, String time) {
        Timetable t = new Timetable();
        t.setDayOfWeek(day); t.setTimeSlot(time); t.setExternalBooking(false);
        int start = text.indexOf("("); int end = text.lastIndexOf(")");
        if (start != -1 && end != -1 && end > start) {
            String room = text.substring(start + 1, end).trim();
            t.setRoomNumber(room.replace("CR-", "").replace("TR-", "").replace("CL-", ""));
            t.setSubject(text.substring(0, start).trim());
        } else {
            t.setRoomNumber("TBD"); t.setSubject(text);
        }
        timetableRepo.save(t);
    }
}