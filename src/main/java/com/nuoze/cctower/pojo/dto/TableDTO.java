package com.nuoze.cctower.pojo.dto;

import java.util.List;

/**
 * @author JiaShun
 * @date 2019-03-10 13:47
 */
public class TableDTO {
    /**
     * 表的名称
     */
    private String tableName;
    /**
     * 表的名称
     */
    private String comments;
    /**
     * 表的名称
     */
    private ColumnDTO pk;
    /**
     * 表的列名(不包含主键)
     */
    private List<ColumnDTO> columns;
    /**
     * 类名(第一个字母大写)，如：sys_user => SysUser
     */
    private String className;
    /**
     * 类名(第一个字母小写)，如：sys_user => sysUser
     */
    private String classname;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ColumnDTO getPk() {
        return pk;
    }

    public void setPk(ColumnDTO pk) {
        this.pk = pk;
    }

    public List<ColumnDTO> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDTO> columns) {
        this.columns = columns;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    @Override
    public String toString() {
        return "TableDTO{" +
                "tableName='" + tableName + '\'' +
                ", comments='" + comments + '\'' +
                ", pk=" + pk +
                ", columns=" + columns +
                ", className='" + className + '\'' +
                ", classname='" + classname + '\'' +
                '}';
    }
}
