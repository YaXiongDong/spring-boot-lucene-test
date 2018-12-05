package com.pxkj.controller;

import com.pxkj.service.IndexesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class TestController {

    @Autowired
    private IndexesService indexesService;

    @RequestMapping(value = "/test")
    public String test(Model model){
        //indexesService.createIndex();
        Map<String, String> map = indexesService.search();
        model.addAttribute("map", map);
        return "index";
    }

}
