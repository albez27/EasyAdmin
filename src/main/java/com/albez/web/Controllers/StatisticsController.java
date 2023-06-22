package com.albez.web.Controllers;

import com.albez.web.Entity.Comments;
import com.albez.web.Entity.GroupsVK;
import com.albez.web.Entity.Posts;
import com.albez.web.Repository.AdministratorRepository;
import com.albez.web.Repository.GroupsVKRepository;
import com.albez.web.Repository.PostsRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StatisticsController {

    @Autowired
    GroupsVKRepository groupsVKRepository;

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    AdministratorRepository administratorRepository;

    @GetMapping("/statistics")
    public String statistics(@RequestParam(value = "id") Integer id,
            HttpSession session){
        session.setAttribute("id", id.toString());
        GroupsVK groupsVK = groupsVKRepository.getIdByIdGroup(id.toString());
        List<Posts> allPost = postsRepository.getPostsByGroup(groupsVK);
        int groupCountComment = 0;
        int posCountComment = 0;
        long maxCount = 0;
        for(Posts p : allPost){
            if(p.getCountComments() > maxCount) {
                maxCount = p.getCountComments();
                session.setAttribute("postMax", p);
            }
            groupCountComment += p.getCountComments();
            List<Comments> comments = p.getComments();
            for(Comments c : comments){
                if(c.getScore() == 1){
                    posCountComment++;
                }
            }
        }
        session.setAttribute("theme", administratorRepository.getTheme("123456"));
        session.setAttribute("countComment", groupCountComment);
        session.setAttribute("countPos", posCountComment);
        session.setAttribute("countNeg", groupCountComment - posCountComment);
        System.out.println(groupCountComment);
        List<Posts> top10NegPost = new ArrayList<>();
        List<Posts> top10PosPost = new ArrayList<>();


        return "statistics";
    }
}
