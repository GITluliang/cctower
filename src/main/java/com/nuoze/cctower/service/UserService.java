package com.nuoze.cctower.service;

import com.nuoze.cctower.common.util.Query;
import com.nuoze.cctower.pojo.entity.User;
import com.nuoze.cctower.pojo.vo.TenantTopUpVO;
import com.nuoze.cctower.pojo.vo.UserVO;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

/**
 * @author JiaShun
 * @date 2019-03-03 15:46
 */
public interface UserService {

    /**
     * 用户列表
     *
     * @param map 条件
     * @return list
     */
    List<UserVO> list(Map<String, Object> map);

    UserVO findByIdVO(Long id);
    /**
     * 总个数
     *
     * @param map 条件
     * @return int
     */
    int count(Map<String, Object> map);

    /**
     * 根据ID查询
     *
     * @param id 主键
     * @return User
     */
    User findById(Long id);

    /**
     * 保存
     *
     * @param vo user
     * @return int
     */
    int save(UserVO vo);

    /**
     * 更新
     *
     * @param vo user
     * @return int
     */
    int update(UserVO vo);

    /**
     * 删除
     *
     * @param id userId
     * @return int
     */
    int remove(Long id);

    /**
     * 批量删除
     *
     * @param userIds [id]
     * @return size
     */
    int batchRemove(Long[] userIds);

    /**
     * 更新
     *
     * @param user user
     * @return int
     */
    int updatePersonal(User user);

    /**
     * 检验是否重复user
     *
     * @param params 条件
     * @return boolean
     */
    boolean exit(Map<String, Object> params);

    /**
     * 用户自主修改密码
     *
     * @param vo user
     * @return count
     * @throws Exception exception
     */
    int resetPwd(UserVO vo) throws Exception;

    /**
     * 管理员修改密码
     *
     * @param vo user
     * @return count
     * @throws Exception exception
     */
    int adminResetPwd(UserVO vo) throws Exception;

    Pair<Integer, List<TenantTopUpVO>> tenantTopUpList(Map<String, Object> map);

    /**
     * 更新商户余额
     * @param vo
     * @return
     */
    int updateBalance(TenantTopUpVO vo);

    /**
     * 更新时长劵
     * @param vo
     * @return
     */
    int updateTimeCoupon(TenantTopUpVO vo);
}
