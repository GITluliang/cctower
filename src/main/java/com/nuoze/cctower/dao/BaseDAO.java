package com.nuoze.cctower.dao;

import org.springframework.stereotype.Repository;

/**
 * @author JiaShun
 * @date 2019-01-18 23:35
 */
@Repository
public interface BaseDAO<T> {

    /**
     * 根据主键删除
     *
     * @param id 主键
     * @return int
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 添加数据
     *
     * @param record 实体
     * @return int
     */
    int insert(T record);

    /**
     * 添加数据
     *
     * @param record 实体
     * @return int
     */
    int insertSelective(T record);

    /**
     * 通过主键查找
     *
     * @param id 主键
     * @return T
     */
    T selectByPrimaryKey(Long id);

    /**
     * 更新数据
     *
     * @param record 实体
     * @return 个数
     */
    int updateByPrimaryKeySelective(T record);

    /**
     * 更新数据
     *
     * @param record 实体
     * @return 个数
     */
    int updateByPrimaryKey(T record);

}
