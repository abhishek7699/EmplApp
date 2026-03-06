package com.EmplApp.EmplApp.utilty;

import com.EmplApp.EmplApp.dto.RabbitMQDTO;
import com.EmplApp.EmplApp.model.EmplRecords;

public class RabbitMqDeconverter {

    public  EmplRecords converter(RabbitMQDTO emp) {
        EmplRecords entity = new EmplRecords();
        entity.setEmail(emp.getEmail());       // assuming record getters are .email()
        entity.setName(emp.getName());
        entity.setDob(emp.getDob());
        entity.setDept(emp.getDept());
        entity.setDesignation(emp.getDesignation());
        entity.setSsn(emp.getSsn());
        entity.setEmpId(emp.getEmpId());


        return entity;

    }
}
