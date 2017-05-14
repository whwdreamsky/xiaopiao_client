package Servlet;

import com.google.gson.Gson;
import com.sun.deploy.net.HttpResponse;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by oliver on 2017/4/27.
 */
public class BaseServlet extends HttpServlet {

    public void responseJson(HttpServletResponse response , Object object)
    {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(new Gson().toJson(object));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(out!=null)
        {
            out.close();
        }
    }





}
