//package com.EmplApp.EmplApp.repo;
//
//
//import com.EmplApp.EmplApp.model.EmplRecords;
//import com.EmplApp.EmplApp.model.EmplRecords;
//import lombok.Data;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.UUID;
//
//@Repository
//@Data
//public class EmplRepo2 {
//
//    @Autowired
//    private EmplRecords emp;
//
//    private HashMap<String, EmplRecords> employeeMap = new HashMap<>();
//
//
//    private List<EmplRecords> list = new ArrayList<>();
//
//
//
//
//
//
//
//
//public void populate(){
//    emp.setEmpId(UUID.randomUUID().toString());
//    emp.setName("Jane Doe");
//    emp.setEmail("jane@company.com");
//    emp.setDob(LocalDate.of(1985, 10, 20));
//    emp.setSsn(987654321L);
//    emp.setDept("HR");
//    emp.setDesignation("Manager");
//
//    list.add(emp);
//    employeeMap.put(emp.getEmail(),emp);
//
//}
//
//
//
//
//    public void addEmployess(String email,  EmplRecords e) {
//
//
//
//
//        if (employeeMap.get(email) == null) {
//
//            employeeMap.put(email,e);
//
//
//
//
//
//        }
//
//
//
//
//    }
//
//    public EmplRecords findEmployee(String email) {
//
//
//
//        if (employeeMap.get(email) == null) {
//
//
//            return null ;
//
//        } else {
//            return employeeMap.get(email);
//
//        }
//
//
//    }
//
//    public List<EmplRecords> sort_name(List<EmplRecords> list, EmplRecords emp, String name) {
//
//        if(list.contains(emp)){
//            list.remove(emp);
//
//        }
//
//        if (!list.isEmpty()) {
//
//
//            int index = 0;
//
//            int shift = 1;
//
//            while (shift > 0 && index<list.size()) {
//
//                EmplRecords e = list.get(index);
//
//                shift = name.compareToIgnoreCase(e.getName());
//
//                index++;
//
//            }
//            index = (index != 0) ? (index - 1) : index;
//
//            if (shift < 0) {
//
//                for (int l = list.size(); l > index; l--) {
//                    EmplRecords e = list.get(l - 1);
//
//                    if (l == list.size()) {
//                        list.add(e);
//                    } else {
//
//                        list.set(l, list.get(l - 1));
//                    }
//
//
//                }
//                list.set(index,emp);
//
//            }
//            else {
//                list.add(emp);
//            }
//        }
//
//
//
//        else {
//
//            list.add(emp);
//
//
//        }
//        return list;
//
//
//    }
//
//}
