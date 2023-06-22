package com.albez.web.Utilities;

import com.albez.web.Entity.Administrator;
import com.albez.web.Entity.Filter;
import com.albez.web.Entity.GroupsVK;
import com.albez.web.Entity.Settings;
import com.albez.web.Repository.AdministratorRepository;
import com.albez.web.Repository.GroupsVKRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


public class CreateAdministrator {

    @Transactional
    public int createAdmin(String userName, String idVk, Map<Integer, String> groups,
                           AdministratorRepository administratorRepository, GroupsVKRepository groupsVKRepository){
        Administrator administrator = new Administrator(userName, idVk);
        administrator.setSettings(defaultSetting());
        List<GroupsVK> groupsVk = new ArrayList<>();
        for(var entry : groups.entrySet()) {
            GroupsVK newGroup = new GroupsVK(entry.getKey().toString(), entry.getValue(), new Filter());
            groupsVk.add(newGroup);
        }
        administrator.setGroups(groupsVk);
        administratorRepository.save(administrator);
        ArrayList<String> ids = new ArrayList<>();
        for(GroupsVK item : groupsVk){
            ids.add(item.getIdGroupVk());
        }
        StringBuilder strIds = new StringBuilder();
        for(int i = 0; i < ids.size(); i++){
            strIds.append(ids.get(i));
            if(i != ids.size() - 1){
               strIds.append(",");
            }
        }
        String pyService = "http://localhost:8081/screen_name?groups_ids="+strIds;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(pyService, String.class);
        HashMap<Integer, String> idAndScreenNameMap = Parser.parserScreenNameGroups(result);
        HashMap<Integer, String> idAndLinkToPost = Parser.parserLinkToPostGroups(result);
        for(var entry : idAndScreenNameMap.entrySet()){
            groupsVKRepository.setScreenName(entry.getValue(), entry.getKey().toString());
        }
        for(var entry : idAndLinkToPost.entrySet()){
            groupsVKRepository.setLinkToPost(entry.getValue(), entry.getKey().toString());
        }
        groupsVKRepository.saveAll(groupsVk);
        return 1;
    }

    public Settings defaultSetting(){
        return new Settings(1, "Диаграмма", 10);
    }
}
