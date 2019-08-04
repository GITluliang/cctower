package com.nuoze.cctower.dao;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-03-10 14:26
 */
@Repository
public interface GeneratorDAO {

    @Select("select table_name tableName, ENGINE engine, table_comment tableComment, create_time createTime from information_schema.tables"
            + " where table_schema = (select database())")
    List<Map<String, Object>> list();

    @Select("select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables \r\n"
            + "	where table_schema = (select database()) and table_name = #{tableName}")
    Map<String, String> findByTableName(String tableName);


    @Select("select column_name columnName, data_type dataType, column_comment columnComment, column_key columnKey, extra from information_schema.columns\r\n"
            + " where table_name = #{tableName} and table_schema = (select database()) order by ordinal_position")
    List<Map<String, String>> listColumns(String tableName);
}
