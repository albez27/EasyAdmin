package com.albez.web;

import com.albez.web.Entity.*;
import com.albez.web.Repository.AdministratorRepository;
import com.albez.web.Repository.FilterRepository;
import com.albez.web.Repository.GroupsVKRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.context.WebApplicationContext;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


@SpringBootApplication
public class Application implements CommandLineRunner{


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }



    @Override
    public void run(String... args) throws Exception{

//        Administrator admin = new Administrator("admin3");
//        Settings settings = new Settings(1, "График", 1020);
//        admin.setSettings(settings);
//        Filter filter = new Filter("asda;asdasd;12312");
//        Filter filter1 = new Filter("32123;zxzxxx;12312");
//        GroupsVK groupsVK = new GroupsVK("-100213", filter);
//        GroupsVK groupsVK1 = new GroupsVK("-12312312", filter1);
//        Posts posts = new Posts("1239912", "https://sosala.ru");
//        Posts posts2 = new Posts("000000", "https://orgenizare.ru");
//        groupsVK.setPosts(Arrays.asList(posts, posts2));
//        admin.setGroups(Arrays.asList(groupsVK, groupsVK1));
//        List<Administrator> administrators = Arrays.asList(admin);
//        List<GroupsVK> groupsVKList = Arrays.asList(groupsVK);
//        administratorRepository.saveAll(administrators);
//        groupsVKRepository.saveAll(groupsVKList);
//        System.out.println("INITIALIZED");
    }
}