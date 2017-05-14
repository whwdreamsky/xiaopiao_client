package Servlet;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oliver on 2017/4/30.
 */
public class VoiceServlet extends BaseServlet {

    public void init() throws ServletException
    {
        // 执行必需的初始化

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String voicedata = request.getParameter("data");

        Map msg = new HashMap<String,String>();
        msg.put("errno","0");//0表示正常
        msg.put("msg","success");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
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

    public void destroy()
    {
        // 什么也不做
    }




}
