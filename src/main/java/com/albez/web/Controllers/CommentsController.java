package com.albez.web.Controllers;

import com.albez.web.Entity.Comments;
import com.albez.web.Entity.GroupsVK;
import com.albez.web.Entity.Posts;
import com.albez.web.Repository.AdministratorRepository;
import com.albez.web.Repository.CommentsRepository;
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
public class CommentsController {

    @Autowired
    CommentsRepository commentsRepository;

    @Autowired
    GroupsVKRepository groupsVKRepository;
    @Autowired
    PostsRepository postsRepository;

    @Autowired
    AdministratorRepository administratorRepository;

    @GetMapping("/comments")
    public String comment(@RequestParam(value = "id", required = false) Integer id,
                          HttpSession session){
        session.setAttribute("id", id);
        GroupsVK groupsVK = groupsVKRepository.getIdByIdGroup(id.toString());
        List<Posts> posts = postsRepository.getPostsByGroup(groupsVK);
        List<Comments> allComments = new ArrayList<>();
        for(Posts p : posts){
            allComments.addAll(commentsRepository.getAllByPosts(p));
        }
        session.setAttribute("theme", administratorRepository.getTheme("123456"));
        session.setAttribute("allComments", allComments);
        return "comments";
    }

    @GetMapping("/trash")
    public String trash(@RequestParam(value = "id", required = false) Integer id,
                        HttpSession session){
        session.setAttribute("id", id);
        GroupsVK groupsVK = groupsVKRepository.getIdByIdGroup(id.toString());
        List<Posts> posts = postsRepository.getPostsByGroup(groupsVK);
        List<Comments> allComments = new ArrayList<>();
        for(Posts p : posts){
            allComments.addAll(commentsRepository.getAllByToDeleteAndDeletedAndPosts(true, false, p));
        }
        System.out.println(allComments);
        session.setAttribute("allComments", allComments);
        return "trash";
    }
}
