package com.albez.web.Controllers;

import com.albez.web.Entity.Filter;
import com.albez.web.Repository.AdministratorRepository;
import com.albez.web.Repository.FilterRepository;
import com.albez.web.Utilities.Parser;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@Controller
@SessionAttributes("test")
public class FilterPageController {

    @Autowired
    FilterRepository filterRepository;
    @Autowired
    AdministratorRepository administratorRepository;

    @GetMapping("/filter")
    public String filter(@RequestParam(value = "id", required = false) Integer id, HttpSession session){
        String pyService = "http://localhost:8081/";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(pyService, String.class);
        HashMap<Integer, String> map = Parser.parserGroups(result);
        session.setAttribute("selected", id);
        session.setAttribute("groups", map);
        session.setAttribute("filterText", filterRepository.getDictionary(id.toString()));
        session.setAttribute("idFilter", filterRepository.getIdFilter(id.toString()));
        session.setAttribute("theme", administratorRepository.getTheme("123456"));
        return "filter";
    }

    @PostMapping("/filter")
    @Transactional
    public String test(@RequestParam(value = "filter", required = false) String filterText, HttpSession session){
        filterRepository.setDictionary(filterText, (Long) session.getAttribute("idFilter"));
        session.setAttribute("filterText", filterText);
        return "filter";
    }
}
