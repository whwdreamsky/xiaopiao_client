package Servlet;

import Action.HistoryRecordAction;
import DAO.SessionDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oliver on 2017/5/4.
 */
public class ClearServlet extends BaseServlet {
    // private TrainClient trainClient;

    public void init() throws ServletException
    {
        // 执行必需的初始化


    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException
    {
        String userid = request.getParameter("userid");
        SessionDAO sessionDAO  = new SessionDAO();
        int result = sessionDAO.deleteSession("xiaopiao",userid);

        Map msg = new HashMap<String,String>();
        msg.put("errno","0");//0表示正常
        msg.put("msg","success");
        if(result==0)
        {
            msg.put("errno","-1");//不正常删除，可能是没有session
            msg.put("msg","该用户无session");
        }
        responseJson(response,msg);
        //修改历史日志状态为不可继续写入
        HistoryRecordAction historyRecordAction = new HistoryRecordAction(userid,"xiaopiao");
        historyRecordAction.modifyState();

    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void destroy()
    {
        // 什么也不做
    }
}
