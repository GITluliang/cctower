package com.nuoze.cctower.rest;

import com.alibaba.fastjson.JSON;
import com.nuoze.cctower.common.constant.Constant;
import com.nuoze.cctower.common.util.GenUtils;
import com.nuoze.cctower.common.util.R;
import com.nuoze.cctower.service.GeneratorService;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-03-10 13:31
 */
@RequestMapping("/common/generator")
@Controller
public class GeneratorController {

    private String prefix = "common/generator/";

    @Autowired
    private GeneratorService generatorService;

    @GetMapping()
    String generator() {
        return prefix + "list";
    }

    @ResponseBody
    @GetMapping("list")
    List<Map<String, Object>> list() {
        return generatorService.list();
    }

    @GetMapping("code/{tableName}")
    public void code(HttpServletResponse response, @PathVariable String tableName) throws IOException {
        String[] tableNames = new String[]{tableName};
        generatorCode(response, tableNames);
    }

    @GetMapping("batchCode")
    public void batchCode(HttpServletResponse response, String tables) throws IOException {
        String[] tableNames = (String[]) JSON.parseArray(tables).toArray();
        generatorCode(response, tableNames);
    }

    @GetMapping("edit")
    public String edit(Model model) {
        Configuration conf = GenUtils.getConfig();
        Map<String, Object> property = new HashMap<>(Constant.MAP_INIT_CAPACITY);
        property.put("author", conf.getProperty("author"));
        property.put("email", conf.getProperty("email"));
        property.put("package", conf.getProperty("package"));
        property.put("autoRemovePre", conf.getProperty("autoRemovePre"));
        property.put("tablePrefix", conf.getProperty("tablePrefix"));
        model.addAttribute("property", property);
        return prefix + "edit";
    }

    @ResponseBody
    @PostMapping("update")
    R update(@RequestParam Map<String, Object> map) {
        try {
            PropertiesConfiguration conf = new PropertiesConfiguration("generator.properties");
            conf.setProperty("author", map.get("author"));
            conf.setProperty("email", map.get("email"));
            conf.setProperty("package", map.get("package"));
            conf.setProperty("autoRemovePre", map.get("autoRemovePre"));
            conf.setProperty("tablePrefix", map.get("tablePrefix"));
            conf.save();
        } catch (ConfigurationException e) {
            return R.error("保存配置文件出错");
        }
        return R.ok();
    }

    private void generatorCode(HttpServletResponse response, String[] tableNames) throws IOException {
        byte[] data = generatorService.generatorCode(tableNames);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"cctower.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }

}
