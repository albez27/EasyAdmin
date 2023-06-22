package com.albez.web.Repository;

import com.albez.web.Entity.Administrator;
import com.albez.web.Entity.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
    List<Administrator> findByUserName(String userName);

    List<Administrator> findByIdVk(String idVk);

    @Query("select s.displayed_comments from Settings s where s.administrator.idVk = ?1")
    int getDisplayedComments(String id);

    @Query("select s.theme from Settings s where s.administrator.idVk = ?1")
    int getTheme(String id);

    Administrator getAdministratorByIdVk(String idVk);



}
