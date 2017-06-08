package webservice;

/**
 * Created by oliver on 2017/4/8.
 */
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainTicketInquire {

    /**
     *12306火车票查询调用示例代码 － 聚合数据
     *在线接口文档：http://www.juhe.cn/docs/22
     **/

        public static final String DEF_CHATSET = "UTF-8";
        public static final int DEF_CONN_TIMEOUT = 30000;
        public static final int DEF_READ_TIMEOUT = 30000;
        public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
        //配置您申请的KEY
        //public  String APPKEY ="0f3a0df8365e55833efb598ec31cc00c";
        public  String APPKEY ="062320eb5aa1760e2c6fb663b69a2783";
        private String result="";
        private String errocode="0";
        private String erromeassage="";

        //1.站到站查询（含票价）

        public List<TrainToFromData> getTrainList(String startpoint, String arrivepoint){
            String url ="http://apis.juhe.cn/train/s2swithprice";//请求接口地址
            Map params = new HashMap();//请求参数
            params.put("start",startpoint);//出发站
            params.put("end",arrivepoint);//终点站
            params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
            params.put("dtype","json");//返回数据的格式,xml或json，默认json
            JsonObject jsonObject = connnectWebSerive(url,params);
            List<TrainToFromData> trainToFromDatalist =null;
            if(errocode.equals("0"))
            {
                trainToFromDatalist = new Gson().fromJson(jsonObject.get("result").getAsJsonObject().get("list").toString(),new TypeToken<ArrayList<TrainToFromData>>(){}.getType());
            }
            else
            {
                System.out.println("火车票数据错误："+errocode+"    "+erromeassage);
            }
            return trainToFromDatalist;

        }

        //2.12306订票②：车次票价查询
        public  void getTrainPrice(){
            String url ="http://apis.juhe.cn/train/ticket.price.php";//请求接口地址
            Map params = new HashMap();//请求参数
            params.put("train_no","");//列次编号，对应12306订票①：查询车次中返回的train_no
            params.put("from_station_no","");//出发站序号，对应12306订票①：查询车次中返回的from_station_no
            params.put("to_station_no","");//出发站序号，对应12306订票①：查询车次中返回的to_station_no
            params.put("date","");//默认当天，格式：2014-12-25
            params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
            JsonObject object = connnectWebSerive(url,params);

        }

        //3.车次查询
        public  TrainInfoData  getTrainInfo(String name){
            String result =null;
            String url ="http://apis.juhe.cn/train/s";//请求接口地址
            Map params = new HashMap();//请求参数
            params.put("name",name);//车次名称，如：G4
            params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
            params.put("dtype","json");//返回数据的格式,xml或json，默认json

            JsonObject jsonObject = connnectWebSerive(url,params);
            TrainInfoData trainInfoData = null;

            if(errocode.equals("0"))
            {
                trainInfoData = new Gson().fromJson(jsonObject.get("result").getAsJsonObject().toString(),new TypeToken<TrainInfoData>(){}.getType());
            }
            return trainInfoData;
        }

        //4.站到站查询
        public void getRequest4(){
            String result =null;
            String url ="http://apis.juhe.cn/train/s2s";//请求接口地址
            Map params = new HashMap();//请求参数
            params.put("start","");//出发站
            params.put("end","");//终点站
            params.put("traintype","");//列车类型，G-高速动车 K-快速 T-空调特快 D-动车组 Z-直达特快 Q-其他
            params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
            params.put("dtype","json");//返回数据的格式,xml或json，默认json

            connnectWebSerive(url,params);
        }

        //5.12306实时余票查询
        public List<TrainTicketRealTimeData> getTrainTicketRealTime(String startpoint,String arrivepoint,String date,String traintype){
            //APPKEY = "937c2443343d85d87fbc95a6eb5c6c2d";
            APPKEY = "9c0678fe099e16ab189213737c80e020";
            //String url ="http://apis.juhe.cn/train/yp";//请求接口地址
            String url ="http://op.juhe.cn/trainTickets/ticketsAvailable";//请求接口地址
            /*
            Map params = new HashMap();//请求参数
            params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
            params.put("dtype","json");//返回数据的格式,xml或json，默认json
            params.put("from",startpoint);//出发站,如：上海虹桥
            params.put("to",arrivepoint);// 到达站,如   ：温州南
            params.put("date",date);//出发日期，默认今日
            params.put("tt",traintype);//车次类型，默认全部，如：G(高铁)、D(动车)、T(特快)、Z(直达)、K(快速)、Q(其他)
            */

            Map params = new HashMap();//请求参数
            params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
            params.put("dtype","json");//返回数据的格式,xml或json，默认json
            params.put("from_station",startpoint);//出发站,如：上海虹桥
            params.put("to_station",arrivepoint);// 到达站,如   ：温州南
            params.put("train_date",date);//出发日期，默认今日
            //params.put("tt",traintype);//车次类型，默认全部，如：G(高铁)、D(动车)、T(特快)、Z(直达)、K(快速)、Q(其他)

            JsonObject jsonObject= connnectWebSerive(url,params);
            List<TrainTicketRealTimeData> realTimeData = null;
            if(errocode.equals("0"))
            {
                realTimeData = new Gson().fromJson(jsonObject.get("result").getAsJsonObject().get("list").toString(),new TypeToken<List<TrainTicketRealTimeData>>(){}.getType());
                System.out.println(jsonObject.get("result").toString());
            }
            else
            {
                System.err.println("errno:"+errocode);
                System.err.println("erromessage:"+erromeassage);
            }

            return realTimeData;

        }

        //6.12306订票①：查询车次
        public  void getTrainTicket(){
            String result =null;
            String url ="http://apis.juhe.cn/train/ticket.cc.php";//请求接口地址
            Map params = new HashMap();//请求参数
            params.put("from","");//出发站名称：如上海虹桥
            params.put("to","");//到达站名称：如温州南
            params.put("date","");//默认当天，格式：2014-07-11
            params.put("tt","");//车次类型，默认全部，如：G(高铁)、D(动车)、T(特快)、Z(直达)、K(快速)、Q(其他)
            params.put("key",APPKEY);//应用APPKEY(应用详细页查询)

            connnectWebSerive(url,params);
        }

        //7.火车票代售点查询
        public void getRequest7(){
            String result =null;
            String url ="http://apis.juhe.cn/train/dsd";//请求接口地址
            Map params = new HashMap();//请求参数
            params.put("province","");//省份,如：浙江
            params.put("city","");//城市，如：温州
            params.put("county","");//区/县，如：鹿城区
            params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
            params.put("dtype","");//返回数据的格式,xml或json，默认json

            connnectWebSerive(url,params);
        }

        //8.列车站点列表
        public  void getRequest8(){
            String result =null;
            String url ="http://apis.juhe.cn/train/station.list.php";//请求接口地址
            Map params = new HashMap();//请求参数
            params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
            params.put("dtype","");//返回数据的格式,xml或json，默认json
            connnectWebSerive(url,params);
        }


        private JsonObject connnectWebSerive(String url,Map params)
        {

            JsonObject object=null;
            try {
                result =net(url, params, "GET");
                object = new JsonParser().parse(result).getAsJsonObject();
                if((object.get("error_code").getAsInt()==0))
                {
                    //System.out.println(object.get("result").toString());
                }else{
                    //System.out.println(object.get("error_code")+":"+object.get("reason").toString());
                    errocode=object.get("error_code").getAsString();
                    erromeassage=object.get("reason").getAsString();
                }

            } catch (Exception e) {
                e.printStackTrace();
                errocode="-1";
                erromeassage="网络异常,train data 访问失败";
            }
            return object;
        }



        /**
         *
         * @param strUrl 请求地址
         * @param params 请求参数
         * @param method 请求方法
         * @return  网络请求字符串
         * @throws Exception
         */
        public  String net(String strUrl, Map params,String method) throws Exception {
            HttpURLConnection conn = null;
            BufferedReader reader = null;
            String rs = null;
            try {
                StringBuffer sb = new StringBuffer();
                if(method==null || method.equals("GET")){
                    strUrl = strUrl+"?"+urlencode(params);
                }
                URL url = new URL(strUrl);
                conn = (HttpURLConnection) url.openConnection();
                if(method==null || method.equals("GET")){
                    conn.setRequestMethod("GET");
                }else{
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                }
                conn.setRequestProperty("User-agent", userAgent);
                conn.setUseCaches(false);
                conn.setConnectTimeout(DEF_CONN_TIMEOUT);
                conn.setReadTimeout(DEF_READ_TIMEOUT);
                conn.setInstanceFollowRedirects(false);
                conn.connect();
                if (params!= null && method.equals("POST")) {
                    try {
                        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                        out.writeBytes(urlencode(params));
                    } catch (Exception e) {
                        // TODO: handle exception
                        System.err.println("访问Train Data 失败！！");
                    }
                }
                InputStream is = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
                String strRead = null;
                while ((strRead = reader.readLine()) != null) {
                    sb.append(strRead);
                }
                rs = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    reader.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return rs;
        }

        //将map型转为请求参数型
        private  String urlencode(Map<String,String> data) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry i : data.entrySet()) {
                try {
                    sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrocode() {
        return errocode;
    }

    public void setErrocode(String errocode) {
        this.errocode = errocode;
    }

    public String getErromeassage() {
        return erromeassage;
    }

    public void setErromeassage(String erromeassage) {
        this.erromeassage = erromeassage;
    }
}

