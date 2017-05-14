package Servlet;

import TrainClient.TrainClient;
import com.google.gson.Gson;

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
        Map msg = new HashMap<String,String>();
        msg.put("errno","0");//0表示正常
        msg.put("msg","success");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
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
            trainClient.run(userid,query);
            List<String> result = trainClient.getResult();
            int max = result.size();
            int min =1;
            Random random = new Random();
            int resultindex = random.nextInt(max)%(max-min+1)+min;
            msg.put("result",trainClient.getResult().get(resultindex-1));
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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void destroy()
    {
        // 什么也不做
    }

}
