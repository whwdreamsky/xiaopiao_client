package Servlet;

import UtilTools.UtilTools;
import com.google.gson.Gson;
import com.google.gson.annotations.Until;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import webservice.ASRVoice;
import webservice.ResultVoice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by oliver on 2017/4/30.
 */
public class VoiceServlet extends BaseServlet {

    private int maxFileSize = 500 * 1024;

    public void init() throws ServletException
    {
        // 执行必需的初始化


    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String result = "";
        Map msg = new HashMap<String,String>();
        String userid = request.getParameter("userid");
        //获取servlet 根路径
        String filepath  = getServletContext().getRealPath("/")+"static\\upload\\";
        String filename = "upload_"+userid+".wav";
        System.out.println(filepath);
        msg.put("errno","0");//0表示正常
        msg.put("msg","success");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //设置最大存储文件大小
        factory.setSizeThreshold(maxFileSize);
        factory.setRepository(new File(filepath));
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(maxFileSize);

        try {
            List fileitem = upload.parseRequest(request);
            if(fileitem!=null&&fileitem.size()!=0) {
                FileItem item = (FileItem) fileitem.get(0);
                System.out.println("filesize : "+ fileitem.size());
                String contentType = item.getContentType();
                result = filename + contentType;
                System.out.println(result);
                try {
                    item.write(new File(filepath + filename));
                    ASRVoice asrVoice = new ASRVoice();
                    asrVoice.transferToText(filepath+filename);
                    ResultVoice resultVoice = asrVoice.getResultVoice();
                    if(resultVoice.getErr_no()==0)
                    {
                        result = resultVoice.getResultstr();
                        System.out.println("utf8_result:"+ result);
                        msg.put("result",result);
                    }
                    msg.put("errno",resultVoice.getErr_no());//0表示正常
                    msg.put("msg",resultVoice.getErr_msg());

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        responseJson(response,msg);

    }

    public void destroy()
    {
        // 什么也不做
    }




}
