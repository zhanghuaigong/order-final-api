package com.dang.order.finalapi.service.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by caojian on 2016/8/1.
 */
public class AppConfig {
    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private static Properties props = new Properties();

    static{
        try {
            props.load(new FileInputStream("conf/app/app.properties"));
        }catch (Exception e){
            logger.error("load app config error", e);
        }
    }

    public static String getProperty(String k){
        return props.getProperty(k);
    }

    public static int getInt(String k){
        return Integer.parseInt(getProperty(k));
    }

    public static boolean getBoolean(String k){
        return Boolean.parseBoolean(getProperty(k));
    }
}
