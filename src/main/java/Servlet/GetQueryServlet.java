package Servlet;

import Action.HistoryRecordAction;
import TrainClient.TrainClient;
import TrainClient.FinalResult;
import UtilTools.UtilTools;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by oliver on 2017/4/27.
 */


public class GetQueryServlet extends HttpServlet{

   // private TrainClient trainClient;

    public void init() throws ServletException
    {
        // 执行必需的初始化
        //trainClient = new TrainClient();

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException
    {
        String userid = request.getParameter("userid");
        String query = request.getParameter("query");
        String reusltapiai = request.getParameter("reusltapiai");
        Map msg = new HashMap<String,JsonElement>();
        msg.put("errno","0");//0表示正常
        msg.put("msg","success");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        System.out.println("resultapi:"+reusltapiai);
        if(userid==null||query==null)
        {
            msg.put("errno","1");//1表示输人参数为空
            msg.put("msg","输人参数缺失");
        }
        else if(userid.equals("")||query.equals(""))
        {
            msg.put("errno","2");
            msg.put("msg","输入参数为空");
        }
        else
        {

            //随机输出一个回复
            TrainClient trainClient = new TrainClient();
            trainClient.run(userid,query,reusltapiai);
            FinalResult finalResult = new FinalResult();
            makeFinalResult(finalResult,trainClient);
            JsonElement reulstelement = new Gson().toJsonTree(finalResult);
            msg.put("result",reulstelement);

            //将解析结果存入历史信息
            HistoryRecordAction historyRecordAction = new HistoryRecordAction(userid,"xiaopiao");
            historyRecordAction.insertSession(finalResult);


        }
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(new Gson().toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(out!=null)
        {
            out.close();
        }


    }

    public void makeFinalResult(FinalResult finalResult,TrainClient trainClient)
    {
        List<String> result = trainClient.getResult();
        finalResult.setDmResultBean(trainClient.getDmResultBean());
        finalResult.setResultlist(result);
        finalResult.setPatternlist(trainClient.getPatternlist());
        int max = result.size();
        int min =1;
        finalResult.setResponse(result.get(UtilTools.GetRandomNum(1,result.size())-1));

    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void destroy()
    {
        // 什么也不做
    }

}
