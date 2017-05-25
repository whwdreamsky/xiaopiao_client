package DAO;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DialogManagement.SessionObject;
import org.apache.log4j.Logger;

/**
 * Created by oliver on 2017/4/12.
 */
public class SessionDAO extends MyBatisAbstractDAO{

    public SessionDAO()
    {
        super();
        sqlSession =getSqlSession();
    }


    public SessionObject selectSession(String appname, String userid)
    {
        Map<String,String> selectword = new HashMap<String,String>();
        selectword.put("appname",appname);
        selectword.put("userid",userid);
        List<SessionObject> sessionObjectlist =sqlSession.selectList("sessionforbot",selectword);
        if(sessionObjectlist.size()==0) return null;
        else{
        if(sessionObjectlist.size()>1)
        {
            System.err.println("用户存在冗余session");
            Logger.getLogger(SessionObject.class).error("用户存在冗余session");

        }
        return sessionObjectlist.get(0);
        }
    }

    public int insertSession(SessionObject sessionObject)
    {
        int result = 0;
        if(sqlSession!=null)
        {
            result =sqlSession.insert("insertSession",sessionObject);
            sqlSession.commit();
            System.out.println("插入成功");
        }
        return result;

    }

    public int updateSession(SessionObject sessionObject)
    {
        int result = 0;
        if(sqlSession!=null)
        {
        result =sqlSession.insert("updateSession",sessionObject);
        sqlSession.commit();
        System.out.println("更新成功");
    }
        return result;
    }

    public int deleteSession(String appname,String userid)
    {
        int result = 1;
        SessionObject sessionObject;
        sessionObject = selectSession(appname,userid);
        if (sessionObject!=null)
        {
            if(sqlSession!=null)
            {
                result =sqlSession.delete("deleteSession",sessionObject);
                sqlSession.commit();
                System.out.println("删除成功");
            }

        }
        else
        {
            result=1;
            System.out.println("该用户无session");

        }


        return result;
    }
    public void closeSession()
    {
        closeSqlSession(sqlSession);
    }






}
