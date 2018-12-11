package com.pxkj.dao;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface WorkInfoDao {

    List<Map<String, Object>> getWorkInfo(Map<String, Object> params);

    List<Map<String, Object>> getWorkInfo2(Map<String, Object> params);

}
