package com.EmplApp.EmplApp.repo.command;

import com.EmplApp.EmplApp.model.EmplRecords;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface EmplRepoCommand extends JpaRepository<EmplRecords, String> {

    @Query(value="SELECT * FROM emplrecords ",nativeQuery = true)
    Page<EmplRecords> FindAll(Pageable pageObj);



    @Query(value="SELECT * FROM emplrecords WHERE email=:EMAIL", nativeQuery=true)
    EmplRecords FindByID(@Param("EMAIL")  String EMAIL);




    @Modifying
    @Transactional
    @Query(value="INSERT INTO emplrecords (email,empid,name,dob,ssn,dept,designation) VALUES(:email ,:empId,:name ,:dob ,:ssn ,:dept ,:designation)",nativeQuery=true)
    void AddRecord(@Param("email") String email,@Param("empId") String empId,@Param("name") String name,@Param("dob") LocalDate dob,@Param("ssn") Long ssn,@Param("dept") String dept,@Param("designation") String designation);


    @Modifying
    @Transactional  // roll back
    @Query(value=" DELETE FROM emplrecords where email=:EMAIL",nativeQuery = true)
    void DeleteRecord(@Param("EMAIL") String EMAIL);


    @Modifying
    @Transactional
    @Query(value=" UPDATE emplrecords SET name=:name,dob=:dob,ssn=:ssn,dept=:dept,designation=:designation,empid=:empId where email=:email",nativeQuery = true)
    void EditRecord(@Param("name") String name,@Param("dob") LocalDate dob,@Param("ssn") Long ssn,@Param("dept")String dept,@Param("designation") String designation,@Param("email") String email,@Param("empId") String empId);




}

