package com.EmplApp.EmplApp.utilty;

import com.EmplApp.EmplApp.model.EmplRecords;
import com.EmplApp.EmplApp.dto.EmployeeCreateDTO;

//@UtilityClass
public class ObjectMapping {
    public EmplRecords convertToEntity(EmployeeCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        EmplRecords entity = new EmplRecords();
        entity.setEmail(dto.email());       // assuming record getters are .email()
        entity.setName(dto.name());
        entity.setDob(dto.dob());
        entity.setDept(dto.dept());
        entity.setDesignation(dto.designation());
        entity.setSsn(dto.ssn());
        // empId will be set later in service
        // ssn — decide if you want to include it or not (usually not from DTO)
        return entity;
    }
}
