package com.albez.web.Controllers;

import com.albez.web.Entity.*;
import com.albez.web.Repository.AdministratorRepository;
import com.albez.web.Repository.CommentsRepository;
import com.albez.web.Repository.GroupsVKRepository;
import com.albez.web.Repository.PostsRepository;
import com.albez.web.Utilities.CreateAdministrator;
import com.albez.web.Utilities.Parser;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class HomePageController {


    @Autowired
    AdministratorRepository administratorRepository;

    @Autowired
    GroupsVKRepository groupsVKRepository;

    @Autowired
    PostsRepository postsRepository;

    @Autowired
    CommentsRepository commentsRepository;

    @GetMapping("/")
    @Transactional
    public String index(@RequestParam(value = "id", required = false) Integer id, Model model, HttpSession session){
        String pyService = "http://localhost:8081/";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(pyService, String.class);
        HashMap<Integer, String> map = Parser.parserGroups(result);
        if(id == null) {
            Optional<Integer> firstKey = map.keySet().stream().findFirst();
            id = firstKey.get();
        }
        String idAdmin = "123456";
        List<Administrator> administratorList = administratorRepository.findByIdVk(idAdmin);
        System.out.println(administratorList.size());
        if(administratorList.size() != 0) {
            System.out.println(administratorRepository.findByIdVk(idAdmin).get(0));
        }
        else {
            CreateAdministrator create = new CreateAdministrator();
            create.createAdmin("albez", idAdmin, map, administratorRepository, groupsVKRepository);
        }
        System.out.println(administratorRepository.findByIdVk(idAdmin).get(0).getGroups());
        GroupsVK groupsVK = groupsVKRepository.getIdByIdGroup(id.toString());

        requestPosts(groupsVK);
        List<Posts> posts = postsRepository.getPostsByGroup(groupsVK);
        requestCommentsFromPost(posts, groupsVK.getIdGroupVk());


//        HashMap<Posts, List<Comments>> postAndComments = new HashMap<>();
//        for(Posts p : firstPost){
//            postAndComments.put(p, commentsRepository.getAllByPosts(p));
//        }
//        System.out.println(firstPost);
        session.setAttribute("theme", administratorRepository.getTheme(idAdmin));
        model.addAttribute("groups", map);
        model.addAttribute("selected", id);
        session.setAttribute("groups", map);
        session.setAttribute("name", map.get(id));
        session.setAttribute("selected", id);
        session.setAttribute("photo", groupsVK.getLinkToPhoto());
        model.addAttribute("name", map.get(id));
        session.setAttribute("posts", posts);
        session.setAttribute("commentsRepo", commentsRepository);
        return "index";
    }

    @PostMapping("/")
    @ModelAttribute("auth")
    public String analyze(@RequestParam("text") String text, Model model) {
        String pyService = "http://localhost:8081/" + text;
        boolean authorized = true;
        model.addAttribute("auth", authorized);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(pyService, String.class);
        return "fragments";
    }


    public void requestPosts(GroupsVK groupsVK){
        String owner_id = "-" + groupsVK.getIdGroupVk();
        String pyService = "http://localhost:8081/posts?owner_id=" + owner_id + "&count=" + 100 + "&offset=0";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(pyService, String.class);
        String screenName = groupsVKRepository.getScreenName(groupsVK.getIdGroupVk());
        List<Posts> posts = addPosts(response, screenName, owner_id, groupsVK);
        if(posts.size() > 0) {
            postsRepository.saveAll(posts);
        }
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
            id = items.getJSONObject(i).getLong("id");
            commentCount = items.getJSONObject(i).getLong("commentCount");
            date = items.getJSONObject(i).getLong("date");
            formattedDate = unixToDate(date);
            text = items.getJSONObject(i).getString("text");
            likes = items.getJSONObject(i).getLong("likes");
            link = createLink(screenName, ownerId, id);
            Posts existPost = postsRepository.getPostsByGroupAndIdPostVk(group, id);
            if(existPost == null){
                posts.add(new Posts(id, link, commentCount, formattedDate, text, likes, group));
            }
        }
        return posts;
    }
    public String createLink(String screenName, String ownerId, long idPost){
        return "https://vk.com/" + screenName + "?w=wall" + ownerId + "_" + idPost;
    }
    public void requestCommentsFromPost(List<Posts> posts, String idVkGroup){
        String owner_id = "-" + idVkGroup;
        String pyService = "http://localhost:8081/getComments?owner_id=" + owner_id + "&post_id=";
        String request = "";
        RestTemplate restTemplate = new RestTemplate();
        for(Posts p : posts){
            request = pyService + p.getIdPostVk();
            String response = restTemplate.getForObject(request, String.class);
            List<Comments> comments = getCommentsFromRequest(response, p);
            commentsRepository.saveAll(comments);
        }
    }
    public List<Comments> getCommentsFromRequest(String response, Posts post){
        JSONObject obj = new JSONObject(response);
        JSONArray items = obj.getJSONArray("items");
        JSONArray profiles = obj.getJSONArray("profiles");
        int score;
        Long idCommentator, vkIdComment;
        String text, date, userName, photo;
        List<Comments> comments = new ArrayList<>();
        for(int i = 0; i < items.length(); i++){
            idCommentator = items.getJSONObject(i).getLong("from_id");
            text = items.getJSONObject(i).getString("text");
            date = unixToDate(items.getJSONObject(i).getLong("date"));
            score = items.getJSONObject(i).getInt("score");
            userName = requestUserNames(idCommentator, profiles);
            vkIdComment = items.getJSONObject(i).getLong("id");
            photo = requestPhoto(idCommentator, profiles);
            Comments existComment = commentsRepository.getCommentByVkIdCommentAndPosts(vkIdComment, post);
            if(existComment == null){
                comments.add(new Comments(idCommentator, userName, text, score,
                        false, date, false, vkIdComment,
                        photo, false, post));
            }
        }
        return comments;
    }
    public String requestUserNames(Long id, JSONArray profiles){
        String userName = "";
        for(int i = 0; i < profiles.length(); i++){
            JSONObject temp = profiles.getJSONObject(i);
            if(temp.getLong("id") == id){
                userName = temp.getString("userName");
            }
        }
        return userName;
    }
    public String requestPhoto(Long id, JSONArray profiles){
        String photo = "";
        for(int i = 0; i < profiles.length(); i++){
            JSONObject temp = profiles.getJSONObject(i);
            if(temp.getLong("id") == id){
                photo = temp.getString("photo");
            }
        }
        return photo;
    }
    public String unixToDate(Long date){
        Date normalDate = new java.util.Date(date*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyy");
        return sdf.format(normalDate);
    }
}
