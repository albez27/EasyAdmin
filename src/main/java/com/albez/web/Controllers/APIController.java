package com.albez.web.Controllers;

import com.albez.web.Entity.Posts;
import com.albez.web.Entity.Settings;
import com.albez.web.Repository.AdministratorRepository;
import com.albez.web.Repository.CommentsRepository;
import com.albez.web.Repository.SettingsRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
public class APIController {

    @Autowired
    CommentsRepository commentsRepository;

    @Autowired
    AdministratorRepository administratorRepository;

    @Autowired
    SettingsRepository settingsRepository;

    @GetMapping("/deleteAPI")
    @Transactional
    public void deleteAPI(@RequestParam(value = "idsDel") String idsDel){
        List<String> items = Arrays.asList(idsDel.split("\\s*,\\s*"));
        List<Integer> commentList = convertLists(items);
        for(int a : commentList){
            commentsRepository.changeToDeleteStatus(true, (long)a);
        }
    }
    @GetMapping("/eraseComment_api")
    @Transactional
    public void eraseComment_api(@RequestParam(value = "idsDel") String idsDel){
        Long idToDelete = Long.parseLong(idsDel);
        commentsRepository.changeToDeleteStatus(true, idToDelete);
    }
    public List<Integer> convertLists(List<String> stringList){
        List<Integer> integerList = new ArrayList<>();
        for(String s : stringList){
            integerList.add(Integer.parseInt(s));
        }
        return integerList;
    }

    @GetMapping("returnAPI")
    @Transactional
    public void returnAPI(@RequestParam(value = "idsRet") String idsRet){
        List<String> items = Arrays.asList(idsRet.split("\\s*,\\s*"));
        List<Integer> commentList = convertLists(items);
        for(int a : commentList){
            commentsRepository.changeToDeleteStatus(false, (long)a);
        }
    }
    @GetMapping("/deleteComAPI")
    @Transactional
    public void deleteComAPI(@RequestParam(value = "idsDel") String idsDel){
        List<String> items = Arrays.asList(idsDel.split("\\s*,\\s*"));
        List<Integer> commentList = convertLists(items);
        for(int a : commentList){
            commentsRepository.deleteComments((long)a);
        }
    }

    @GetMapping("/recoverComment_api")
    @Transactional
    public void returnCommentAPI(@RequestParam(value = "id") String id){
        Long idToDelete = Long.parseLong(id);
        commentsRepository.changeToDeleteStatus(false, idToDelete);
    }
    @GetMapping("/changeScore_api")
    @Transactional
    public void changeScoreAPI(@RequestParam(value = "id") String id){
        Long idL = Long.parseLong(id);
        int score = commentsRepository.getScoreById(idL);
        System.out.println(score + " СЧЕТ");
        if(score == 0) {
            score = 1;
        }
        else score = 0;
        commentsRepository.changeScore(score, idL);
    }

    @GetMapping("/changeTheme_api")
    @Transactional
    public void changeThemeAPI(HttpSession session){
        String themeS = session.getAttribute("theme").toString();

        int theme = Integer.parseInt(themeS);
        if(theme == 1) {
            theme = 0;
        }
        else theme = 1;
//        System.out.println(themeS);
        settingsRepository.changeSettingTheme(theme, settingsRepository.getSettingsByAdministrator_IdVk("123456").getId());
        session.setAttribute("theme", theme);
//        System.out.println(settingsRepository.getSettingsByAdministrator_IdVk("123456").getDataView() + " ASdasd");
    }

    public String formatter(String input) throws ParseException {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dt.parse(input);
        SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
        return dt1.format(date);
    }
    public boolean compareDateRange(String minDate, String maxDate, String date) throws ParseException {
        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
        Date inputDate = dt.parse(date);
        Date min = dt.parse(minDate);
        Date max = dt.parse(maxDate);
        return !inputDate.before(min) && !inputDate.after(max);
    }
    @GetMapping("/datePosts_api")
    public void datePosts_api(HttpSession session, @RequestParam(value = "min") String min,
                              @RequestParam(value = "max") String max) throws ParseException {
        //return !d.before(min) && !d.after(max);
        List<Posts> list = (List<Posts>) session.getAttribute("posts");
        String formattedMin = "", formattedMax = "";
        if(min != null){
            formattedMin = formatter(min);
            session.setAttribute("selectedMin", min);
        }
        if(max != null){
            formattedMax = formatter(max);
            session.setAttribute("selectedMax", max);
        }

        if(list.size() > 0){
            List<Posts> selectedPosts = new ArrayList<>();
            for(Posts p : list){
                if(compareDateRange(formattedMin, formattedMax, p.getDatePost())){
                    selectedPosts.add(p);
                }
            }
            session.setAttribute("postsSelected", selectedPosts);
        }
    }
}
