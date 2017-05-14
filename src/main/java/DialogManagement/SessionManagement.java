package DialogManagement;
import DAO.SessionDAO;
import UtilTools.ReadProperties;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by oliver on 2017/4/11.
 */
public class SessionManagement {
    //之前没有历史信息
    private SessionObject sessionObject;
    private SessionDAO sessionDAO;

    public SessionManagement()
    {
        sessionObject = new SessionObject();
    }

    public void connectSession()
    {
        sessionDAO = new SessionDAO();
    }

    public SessionObject getSession(String appname,String userid)
    {
        SessionObject sessionold = sessionDAO.selectSession(appname,userid);
        return sessionold;
    }

    public DMResultBean getLastDmResult(SessionObject sessionold)
    {
        List<DMResultBean> dmResultBeanList = new Gson().fromJson(sessionold.getDmresultbeanlist(),new TypeToken<ArrayList<DMResultBean>>(){}.getType());
        int i;
        for(i=0;i<dmResultBeanList.size();i++)
        {
            if(dmResultBeanList.get(i).getOffset()==0)
            {
                break;
            }
        }
        return dmResultBeanList.get(i);

    }
    //之前没有session 的保存形式
    public void saveSession(DMResultBean dmResultBean)
    {
        produceSession(dmResultBean);
        insertSession();
    }



    //之前有session 的保存形式
    public void saveSession(DMResultBean dmResultBean,SessionObject oldsession)
    {
        produceSession(dmResultBean,oldsession);
        updateSession();
    }
    private int insertSession()
    {
        return  sessionDAO.insertSession(this.sessionObject);
    }
    private int updateSession()
    {
        return sessionDAO.updateSession(this.sessionObject);
    }

    public void closesession()
    {
        sessionDAO.closeSession();
    }

    private void produceSession(DMResultBean dmResultBean)
    {
        this.sessionObject.setAppname(dmResultBean.getAppname());
        this.sessionObject.setUserid(dmResultBean.getUserid());
        this.sessionObject.setUpdatetime(updateSessionTime());
        dmResultBean.setOffset(0);
        List<DMResultBean> dmResultBeanList = new ArrayList<DMResultBean>();
        dmResultBeanList.add(dmResultBean);
        this.sessionObject.setDmresultbeanlist(new Gson().toJson(dmResultBeanList).toString());
        this.sessionObject.setRound(1);

    }
    //有旧的session
    private void produceSession( DMResultBean dmResultBean, SessionObject oldsession)
    {
        dmResultBean.setOffset(0);

        oldsession.setUpdatetime(updateSessionTime());
        Gson gson = new Gson();
        List<DMResultBean> dmResultBeanList = gson.fromJson(oldsession.getDmresultbeanlist(),new TypeToken<ArrayList<DMResultBean>>(){}.getType());
        for(int i=0;i<dmResultBeanList.size();i++)
        {
            dmResultBeanList.get(i).setOffset(dmResultBeanList.get(i).getOffset()+1);
        }
        dmResultBeanList.add(0,dmResultBean);
        int maxsessionround = Integer.parseInt(ReadProperties.getDMPropers("maxround"));
        if(dmResultBeanList.size()>=maxsessionround)
        {
            for(int i=maxsessionround-1;i<dmResultBeanList.size();i++)
            {
                dmResultBeanList.remove(dmResultBeanList.size()-1);
            }
        }

        oldsession.setRound(dmResultBeanList.size());
        oldsession.setDmresultbeanlist(gson.toJson(dmResultBeanList));
        this.sessionObject = oldsession;

    }
    public String updateSessionTime()
    {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datastring = simpleDateFormat.format(new Date());
        return datastring;
    }

    public void printSession()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println(this.sessionObject.getAppname());
        System.out.println(this.sessionObject.getRound());
        System.out.println(this.sessionObject.getUpdatetime());

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.println(new Gson().toJson(this.sessionObject.getDmresultbeanlist()));
        /*
        for (int i=0;i<this.sessionObject.getDmResultBeanList().size();i++)
        {
            System.out.println("DMResultBean:"+i);
            System.out.println("actionname: "+this.sessionObject.getDmResultBeanList().get(i).getActionname());
            System.out.println("actiontype: "+this.sessionObject.getDmResultBeanList().get(i).getActiontype());
            System.out.println("intent: "+this.sessionObject.getDmResultBeanList().get(i).getIntent());
            System.out.println("offset: "+this.sessionObject.getDmResultBeanList().get(i).getOffset());
            System.out.println("userid: "+this.sessionObject.getDmResultBeanList().get(i).getUserid());
            System.out.println("slotlist: "+this.sessionObject.getDmResultBeanList().get(i).getSlotlist().toString());

        }*/

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}
