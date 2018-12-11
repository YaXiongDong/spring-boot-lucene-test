package com.pxkj.controller;

import com.pxkj.service.WorkInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class TestController {

    @Autowired
    private WorkInfoService workInfoService;

    @RequestMapping(value = "/test")
    @ResponseBody
    public List<Map<String, Object>> test(){
        List<Map<String, Object>> list = workInfoService.html2Text();
        return list;
    }

    @RequestMapping(value = "/search")
    public String search(Model model){
        List<Map<String, String>> list = workInfoService.searchHtml();
        model.addAttribute("list", list);
        return "index";
    }

}
