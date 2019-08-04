package com.nuoze.cctower.service;

import java.util.List;
import java.util.Map;

/**
 * @author JiaShun
 * @date 2019-03-10 13:58
 */
public interface GeneratorService {
    List<Map<String, Object>> list();

    byte[] generatorCode(String[] tableNames);
}
