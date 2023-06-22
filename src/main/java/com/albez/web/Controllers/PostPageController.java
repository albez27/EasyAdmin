package com.albez.web.Controllers;

import com.albez.web.Entity.Comments;
import com.albez.web.Entity.GroupsVK;
import com.albez.web.Entity.Posts;
import com.albez.web.Repository.AdministratorRepository;
import com.albez.web.Repository.CommentsRepository;
import com.albez.web.Repository.GroupsVKRepository;
import com.albez.web.Repository.PostsRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.json.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class PostPageController {

    @Autowired
    GroupsVKRepository groupsVKRepository;

    @Autowired
    AdministratorRepository administratorRepository;

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    CommentsRepository commentsRepository;

    @GetMapping("/posts/all")
    @Transactional
    public String postsPage(@RequestParam(value = "id", required = false) Integer id, HttpSession session, Model model){
        session.setAttribute("selected", id);
        String currentGroupVk = id.toString();
        String owner_id = "-" + currentGroupVk;
        String pyService = "http://localhost:8081/posts?owner_id=" + owner_id + "&count=" + 100 + "&offset=0";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(pyService, String.class);
        String screenName = groupsVKRepository.getScreenName(id.toString());
        GroupsVK group = groupsVKRepository.getIdByIdGroup(currentGroupVk);
        List<Posts> posts = postsRepository.getPostsByGroup(group);
        if(posts.size() == 0){
            posts = addPosts(result, screenName, owner_id, group);
            postsRepository.saveAll(posts);
        }
        System.out.println(posts);
        model.addAttribute("posts", posts);

        //REWRITE
        session.setAttribute("postsH", posts);
        return "posts";
    }

    @GetMapping("/posts/all/analyze")
    public String postAllPage(@RequestParam(value = "id", required = false) Integer id,
                              @RequestParam(value = "posts", required = false) String posts,
                              HttpSession session, Model model){
        List<String> items = Arrays.asList(posts.split("\\s*,\\s*"));
        String currentGroupVk = id.toString();
        String owner_id = "-" + currentGroupVk;
        String pyService = "http://localhost:8081/getComments?owner_id=" + owner_id + "&post_id=" + postsRepository.getIdPostVkById(Long.parseLong(items.get(0)));
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(pyService, String.class);
//
//        List<Comments> comments = parseComments(result, groupsVKRepository.getIdByIdGroup(id.toString()));
//
//        GroupsVK groupsVK = groupsVKRepository.getIdByIdGroup(id.toString());
//        if(comments.size() != 0){
//            commentsRepository.saveAll(comments);
//        }
        Posts post = postsRepository.getPostsById(Long.parseLong(items.get(0)));
        model.addAttribute("comments", commentsRepository.getAllByPosts(post));
//        System.out.println(commentsRepository.getAllByPosts(post));
        return "postAnalyze";
    }

    @GetMapping("/posts/date")
    public String postDate(@RequestParam(value = "id", required = false) Integer id,
                           HttpSession session, Model model){
        System.out.println(model.getAttribute("posts"));;
        return "postDate";
    }

    public int countComment(String response){
        JSONObject obj = new JSONObject(response);
        return obj.getInt("countComment");
    }
    @GetMapping("/selectPosts")
    public String selectPosts(@RequestParam(value = "id", required = false) Integer id,
                              HttpSession session, Model model){
        List<Posts> list = (List<Posts>) session.getAttribute("posts");
        session.setAttribute("maxDate",  parseToHtml(list.get(0).getDatePost()));
        if(list.size() > 1) {
            session.setAttribute("minDate", parseToHtml(list.get(list.size()-1).getDatePost()));
        }
        return "selectPostToDate";
    }

    public String parseToHtml(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
        LocalDate dateLocale = LocalDate.parse(date, formatter);
        System.out.println(dateLocale);
        return dateLocale.toString();
    }

//    public List<Comments> parseComments(String response, GroupsVK groupsVK){
//        JSONObject obj = new JSONObject(response);
//        JSONArray items = obj.getJSONArray("items");
//        JSONArray profiles = obj.getJSONArray("profiles");
//        int score;
//        Long idCommentator, idPostVk, vkIdComment;
//        String text, date, userName;
//        List<Comments> comments = new ArrayList<>();
//        for(int i = 0; i < items.length(); i++){
//            idCommentator = items.getJSONObject(i).getLong("from_id");
//            idPostVk = items.getJSONObject(i).getLong("post_id");
//            Posts post = postsRepository.getPostsByGroupAndIdPostVk(groupsVK, idPostVk);
//            text = items.getJSONObject(i).getString("text");
//            date = unixToDate(items.getJSONObject(i).getLong("date"));
//            score = items.getJSONObject(i).getInt("score");
//            userName = requestUserNames(idCommentator, profiles);
//            vkIdComment = items.getJSONObject(i).getLong("id");
//            Comments existComment = commentsRepository.getCommentByVkIdCommentAndPosts(vkIdComment, post);
//            if(existComment == null){
//                comments.add(new Comments(idCommentator, userName, text, score, false, date, false, vkIdComment, post));
//            }
//        }
//        return comments;
//    }

    public String  requestUserNames(Long id, JSONArray profiles){
        String userName = "";
        for(int i = 0; i < profiles.length(); i++){
            JSONObject temp = profiles.getJSONObject(i);
            if(temp.getLong("id") == id){
                userName = temp.getString("userName");
            }
        }
        return userName;
    }

//    public String parseProfiles(JSONArray profiles)

    public void updatePosts(){

    }

    public List<Posts> addPosts(String response, String screenName, String ownerId, GroupsVK group) {
        JSONObject obj = new JSONObject(response);
        JSONArray items = obj.getJSONArray("items");
        long id, commentCount, date, likes;
        String formattedDate;
        String text;
        List<Posts> posts = new ArrayList<>();
        String link;
        for(int i = 0; i < items.length(); i++){
            System.out.println(items.get(i));
            id = items.getJSONObject(i).getLong("id");
            commentCount = items.getJSONObject(i).getLong("commentCount");
            date = items.getJSONObject(i).getLong("date");
            formattedDate = unixToDate(date);
            text = items.getJSONObject(i).getString("text");
            likes = items.getJSONObject(i).getLong("likes");
            link = createLink(screenName, ownerId, id);
            posts.add(new Posts(id, link, commentCount, formattedDate, text, likes, group));
        }
        return posts;
    }
    public String unixToDate(Long date){
        Date normalDate = new java.util.Date(date*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyy");
        return sdf.format(normalDate);
    }
    //https://vk.com/trissreducis?w=wall-151651256_11
    public String createLink(String screenName, String ownerId, long idPost){
        return "https://vk.com/" + screenName + "?w=wall" + ownerId + "_" + idPost;
    }

}
