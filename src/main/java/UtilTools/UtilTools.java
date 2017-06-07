package UtilTools;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by oliver on 2017/5/16.
 */
public class UtilTools {


    public static String tranStrToUtf8(String old)
    {
        String newstr= null;
        try {
            newstr = new String(old.getBytes(),"utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  newstr;
    }

    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }

    public static String GetCurrentTimeStr()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datastring = simpleDateFormat.format(new Date());
        return datastring;
    }

    public static Date TransferTimestrToDate(String timestr)
    {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = simpleDateFormat.parse(timestr);
            } catch (ParseException e) {
                e.printStackTrace();
            return null;
        }
        return date;
    }
    public static int GetRandomNum(int min,int max)
    {

        Random random = new Random();
        int resultindex = random.nextInt(max)%(max-min+1)+min;
        return resultindex;
    }












}

