package com.backend;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

@Component
public class StudentDataService {
    
    private static StudentDataService instance;
    
    private List<StudentData> studentData = MySQLMethods.getStudentData();
    
    public static StudentDataService getInstance() {
        if (instance == null) {
            instance = new StudentDataService();
        }
        instance.resetData();
        return instance;
    }
    
    private void resetData() {
        studentData = MySQLMethods.getStudentData();
    }
    
    public synchronized List<StudentData> findAll() {
        return findAll(null);
    }
    
    public synchronized List<StudentData> findAll(String stringFilter) {
        resetData();
        
        if (stringFilter == null || stringFilter.isEmpty()) {
            return studentData;
        }
        
        ArrayList<StudentData> studentDataList = new ArrayList<>();
        for (StudentData student : studentData) {
            String          toQuery      = student.toFullString();
            StringTokenizer st           = new StringTokenizer(stringFilter);
            boolean         passesFilter = true;
            while (st.hasMoreTokens() && passesFilter) {
                passesFilter = toQuery.toLowerCase().contains(st.nextToken().toLowerCase());
            }
            
            if (passesFilter) {
                studentDataList.add(student.clone());
            }
        }
        
        studentDataList.sort(Comparator.comparing(StudentData::getLastName));
        
        return studentDataList;
    }
    
    public synchronized long count() {
        resetData();
        return studentData.size();
    }
    
    public synchronized void delete(Student value) {
        value.delete();
    }
}
