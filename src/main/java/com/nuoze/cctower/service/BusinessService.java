package com.nuoze.cctower.service;

import com.nuoze.cctower.pojo.entity.User;
import com.nuoze.cctower.pojo.vo.UserVO;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-03-13 23:19
 */
public interface BusinessService {
    List<UserVO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(User vo);
}
