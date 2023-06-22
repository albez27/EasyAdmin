package com.albez.web.Controllers;

import com.albez.web.Entity.Comments;
import com.albez.web.Repository.CommentsRepository;
import com.albez.web.Repository.GroupsVKRepository;
import com.albez.web.Repository.PostsRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OnDeletePageController {

    @Autowired
    CommentsRepository commentsRepository;

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    GroupsVKRepository groupsVKRepository;

    @GetMapping("/ondelete")
    public String onDeletePage(@RequestParam(value = "id", required = false) Integer id,
                               HttpSession session, Model model){
        session.setAttribute("selected", id);
        List<Comments> commentsToDelete = commentsRepository.getToDeleteComments(true, groupsVKRepository.getIdByIdGroup(id.toString()));
        model.addAttribute("commentsToDelete", commentsToDelete);
        return "deletePage";
     }

}
