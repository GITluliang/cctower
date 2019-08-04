package com.nuoze.cctower.service.impl;

import com.nuoze.cctower.common.util.DateUtils;
import com.nuoze.cctower.common.util.GenUtils;
import com.nuoze.cctower.dao.GeneratorDAO;
import com.nuoze.cctower.service.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @author JiaShun
 * @date 2019-03-10 13:58
 */
@Service
public class GeneratorServiceImpl implements GeneratorService {

    @Autowired
    GeneratorDAO generatorDAO;

    @Override
    public List<Map<String, Object>> list() {
        List<Map<String, Object>> list = generatorDAO.list();
        for (Map<String, Object> map : list) {
            Date date = (Date)map.get("createTime");
            String createTime = DateUtils.formatDateTime(date);
            map.put("createTime", createTime);
        }
        return list;
    }

    @Override
    public byte[] generatorCode(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames) {
            Map<String, String> table = generatorDAO.findByTableName(tableName);
            List<Map<String, String>> columns = generatorDAO.listColumns(tableName);
            GenUtils.generatorCode(table, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
}
