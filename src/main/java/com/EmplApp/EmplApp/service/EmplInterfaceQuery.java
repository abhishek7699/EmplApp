package com.EmplApp.EmplApp.service;

import com.EmplApp.EmplApp.model.EmplRecords;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public interface EmplInterfaceQuery {
    public EmplRecords viewEmployee(String s);
    public Page<EmplRecords> allEmployee(int page, int size);
}
