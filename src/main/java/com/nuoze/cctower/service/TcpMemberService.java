package com.nuoze.cctower.service;

import com.nuoze.cctower.pojo.vo.MemberVO;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 * ToC用户表
 *
 * @author JiaShun
 * @email jiashun@outlook.com
 * @date 2019-08-21 22:05:15
 */
public interface TcpMemberService {

    Pair<Integer, List<MemberVO>> list(Map<String, Object> map);

}
