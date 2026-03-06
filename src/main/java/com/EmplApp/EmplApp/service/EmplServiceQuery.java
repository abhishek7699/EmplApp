package com.EmplApp.EmplApp.service;


import com.EmplApp.EmplApp.config.RabbitMQConfig;
import com.EmplApp.EmplApp.dto.RabbitMQDTO;
import com.EmplApp.EmplApp.exception.EmployeeExistsException;
import com.EmplApp.EmplApp.exception.EmployeeNotFoundException;
import com.EmplApp.EmplApp.model.EmplRecords;
import com.EmplApp.EmplApp.repo.command.EmplRepoCommand;
import com.EmplApp.EmplApp.repo.query.EmplRepoQuery;
import com.EmplApp.EmplApp.utilty.RabbitMqConverter;
import com.EmplApp.EmplApp.utilty.RabbitMqDeconverter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.UUID;
@Service
public class EmplServiceQuery implements EmplInterfaceQuery {
    @Autowired
    private EmplRepoQuery repo;

    @Autowired
    private ObjectMapper objectMapper;



    RabbitMqDeconverter drq= new RabbitMqDeconverter();

    //@Override
    public  EmplRecords viewEmployee(String email) {
        return  repo.FindByID(email);
        //.orElseThrow(()->new EmployeeNotFoundException(error));

    }


    public Page<EmplRecords> allEmployee(int page, int size) {


        Pageable pageObj=   PageRequest.of(page,size);

        Page<EmplRecords> list = repo.FindAll(pageObj);
        if(list.isEmpty()){
            throw new EmployeeNotFoundException("list is Empty");
        }
        //list.sort(Comparator.comparing(EmplRecords::getName));
        return list;
    }


    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receiveMessage(String message){

        RabbitMQDTO entity = objectMapper.readValue(message, RabbitMQDTO.class);
        String action= entity.getAction();
        System.out.println(action);
        EmplRecords emp= drq.converter(entity);
        System.out.println(emp.getEmail());

        try {
            switch (action) {

                case "add":
                    repo.AddRecord(emp.getEmail(), emp.getEmpId(), emp.getName(), emp.getDob(), emp.getSsn(), emp.getDept(), emp.getDesignation());
                    break;
                case "delete":
                    repo.DeleteRecord(emp.getEmail());
                    break;
                case "edit":
                    repo.EditRecord(emp.getName(), emp.getDob(), emp.getSsn(), emp.getDept(), emp.getDesignation(), emp.getEmail(), emp.getEmpId());
                    break;
            }
        }catch(Exception e){
            System.out.println("Error processing message: " + e.getMessage());
        }

        }






}
