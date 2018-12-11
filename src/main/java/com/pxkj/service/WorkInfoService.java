package com.pxkj.service;

import com.pxkj.dao.WorkInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WorkInfoService {

    private final Pattern p_html = Pattern.compile("<(\\S*?)[^>]*>.*?|<.*? />", Pattern.CASE_INSENSITIVE);
    private final Pattern p_blank = Pattern.compile("^\\s*|\\s*$", Pattern.CASE_INSENSITIVE);
    private final Pattern p_bracket = Pattern.compile("^[\\{\\}\\[\\]\\(\\)]*$", Pattern.CASE_INSENSITIVE);

    @Autowired
    private WorkInfoDao workInfoDao;
    @Autowired
    private IndexesService indexesService;

    public List<Map<String, Object>> html2Text() {
        List<Map<String, Object>> info = workInfoDao.getWorkInfo();
        if (info != null && info.size() > 0) {
            for (Map<String, Object> map : info) {
                String htmlStr = map.getOrDefault("html", "").toString();
                htmlStr = dealHtml(htmlStr);
                map.put("html", htmlStr);
                Integer id = Integer.parseInt(String.valueOf(map.get("Id")));
                String title = String.valueOf(map.get("title"));
                String content = htmlStr;
                indexesService.createIndex(content, id, title);
            }
        }
        return info;
    }

    public List<Map<String, String>> searchHtml() {
        List<Map<String, String>> result = new ArrayList<>();
        List<Map<String, Object>> info = workInfoDao.getWorkInfo2();
        System.out.println(info);
        if (info != null && info.size() > 0) {
            for (Map<String, Object> map : info) {
                String htmlStr = map.getOrDefault("html", "").toString();
                htmlStr = dealHtml(htmlStr);
                String content = htmlStr;
                List<Map<String, String>> search = indexesService.search(content);
                result.addAll(search);
            }
        }
        if (result != null &&result.size() > 0){
            for (Map<String, String> map : result) {
                if (!map.containsKey("html")){
                    map.put("html", "");
                }
                if (!map.containsKey("score")){
                    map.put("score", "0");
                }
            }
        }
        return result;
    }

    private String dealHtml(String text) {
        Matcher m_html = p_html.matcher(text);
        text = m_html.replaceAll(""); // 过滤html标签
        Matcher m_bracket = p_bracket.matcher(text);
        text = m_bracket.replaceAll("");
        text = text.replaceAll("&nbsp;", "");
        Matcher m_blank = p_blank.matcher(text);
        text = m_blank.replaceAll("");
        return text;
    }

}
