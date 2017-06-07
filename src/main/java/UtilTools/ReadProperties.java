package UtilTools;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by oliver on 2017/4/15.
 */
public class ReadProperties {

    public static Map getSlotClyclePropers()
    {
        Map map = new HashMap<>();
        try {
            InputStream inputStream = ReadProperties.class.getClassLoader().getResourceAsStream(("./conf/slotslifecycle.properties"));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();
            while(line!=null)
            {
                String[] strlist = line.split("=");
                if(strlist.length==2)
                {
                    map.put(strlist[0],strlist[1]);
                }
                line = bufferedReader.readLine();
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("找不到DM配置文件");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
    public static String getDMPropers(String key)
    {
        Properties properties = new Properties();
        String value="";
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
