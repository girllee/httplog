package org.girllee.httplog;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by boot on 12/6/16.
 *
 * @author Asin Liu
 */
public class PropertiesReader {
    private static final String CONFIG_PROPERTIES_FILE="httplog.properties";

    public static void main(String[] args) {
        InputStream is = ClassLoader.getSystemResourceAsStream(CONFIG_PROPERTIES_FILE);
        InputStream stream = PropertiesReader.class.getClassLoader().getResourceAsStream(CONFIG_PROPERTIES_FILE);

        System.out.println(ClassLoader.getSystemClassLoader().equals(PropertiesReader.class.getClassLoader()));
        try {
            Properties props = loadPropertiesFileStream(is);
            System.out.println(props.getProperty("httplog.linePerRequest"));
            is.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static final Properties loadPropertiesFileStream(InputStream inputStream) throws Exception{
        Properties props = null;
        try {
            props = new Properties();
            props.load(inputStream);
            inputStream.close();
        }finally {
            if(inputStream != null){
                inputStream.close();
            }
        }
        return props;
    }
}

