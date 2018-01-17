package com.jack.lottery.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PropertyUtil {

    private static Properties props;

    static{
        loadProps();
    }

    synchronized static private void loadProps(){
        props = new Properties();
        InputStream in = null;
        try {
            in = PropertyUtil.class.getClassLoader().getResourceAsStream("filter.properties");
            props.load(in);
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {

            }
        }

    }

    public static String getProperty(String key){
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static boolean contains(String uri) {
        List<String> ignorUrls = Arrays.asList(getProperty("ignor.login.urls").split(";"));
        if (ignorUrls.contains(uri)) {
            return true;
        } else {
            return false;
        }
    }
}
