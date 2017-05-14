package UtilTools;

import java.io.*;
import java.util.Properties;

/**
 * Created by oliver on 2017/4/15.
 */
public class ReadProperties {
    public static String getDMPropers(String key)
    {
        Properties properties = new Properties();
        String value="";
        System.out.println();
        try {
            InputStream inputStream = ReadProperties.class.getClassLoader().getResourceAsStream(("./conf/dialogmanagement.properties"));
            properties.load(inputStream);
            value = properties.getProperty(key);
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("找不到DM配置文件");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //注意使用完后要pro.close
        return value;

    }
}
