package com.albez.web.Controllers;

import com.albez.web.Utilities.Parser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class AnalyzePageController {

    @GetMapping("/analyze")
    public String analyze(@RequestParam(value = "id", required = false) Integer id,
                          @RequestParam(value = "link", required = false) String link,
                          Model model)
    {
        boolean authorized = true;
        model.addAttribute("auth", authorized);
        String pyService = "http://localhost:8081/";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(pyService, String.class);
        HashMap<Integer, String> map = Parser.parserGroups(result);
        System.out.println(result);

        model.addAttribute("groups", map);
        model.addAttribute("selected", id);
        if(link != null){
            String group_id = link.substring(link.indexOf('-'), link.indexOf('_'));
            String post_id = link.substring(link.indexOf("_")+1);
            String pyServ = "http://localhost:8081/link?group_id=" + group_id + "&post_id=" + post_id;
            String res = restTemplate.getForObject(pyServ, String.class);
            ArrayList<String> arrayList = Parser.parseComment(res);
            model.addAttribute("comments", arrayList);
            for(String ar : arrayList){
                System.out.println(ar);
            }
            model.addAttribute("name", map.get(id));
            model.addAttribute("link", link);
        }
        return "analyze";
    }
}
