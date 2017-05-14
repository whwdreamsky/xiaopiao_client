package TrainClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oliver on 2017/4/20.
 */
public class GlobalData {

    public  Map<String,String> traincode=null;
    public static  String GetTrainType(String traintype)
    {
        Map<String,String> traintypemap = new HashMap();
        traintypemap.put("H","火车");
        traintypemap.put("G","高铁");
        traintypemap.put("Y","旅游列车");
        traintypemap.put("D","动车");
        traintypemap.put("T","特快列车");
        traintypemap.put("K","快速火车");
        traintypemap.put("P","普通快速");

        if(traintypemap.containsKey(traintype)) return traintypemap.get(traintype);
        return traintype;

    }

    public static String  GetSeatType(String seattype)
    {
        Map<String,String> seatmap = new HashMap();
        seatmap.put("高级软卧","gjrw_num");
        seatmap.put("其他","qtxb_num");
        seatmap.put("软卧","rw_num");
        seatmap.put("软座","rz_num");
        seatmap.put("特等座","tdz_num");
        seatmap.put("无座","wz_num");
        seatmap.put("硬卧","yw_num");
        seatmap.put("硬座","yz_num");
        seatmap.put("二等座","edz_num");
        seatmap.put("一等座","ydz_num");
        seatmap.put("商务座","swz_num");

        if(seatmap.containsKey(seattype))
        {
            return seatmap.get(seattype);
        }
        return  seattype;

    }
    public  void LOADTRAINCODE()
    {
        if(traincode!=null)return;
        InputStream in = GlobalData.class.getClassLoader().getResourceAsStream("./conf/traincode_name.txt");
        traincode = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String str=null;
        try {
            str = bufferedReader.readLine();
            while (str!=null)
            {
                String[] strlist = str.split("\t");
                if(strlist.length==2)
                {
                    traincode.put(strlist[1],strlist[0]);
                }
                str = bufferedReader.readLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if(in!=null)
            {
                in.close();
            }
            if(bufferedReader!=null)
            {
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /*
    public static void main(String[] args)
    {
        GlobalData.LOADTRAINCODE();
        if(GlobalData.traincode!=null)
        {
            System.out.println(GlobalData.traincode.size());
        }
        else
        {
            System.out.println("为空！！");
        }
    }
    */



    public static int MAXOFFSET=1000;
}
