package DAO;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by oliver on 2017/4/12.
 */
public class MyBatisAbstractDAO {
    private static SqlSessionFactory factory;
    protected SqlSession sqlSession;
    public MyBatisAbstractDAO()
    {
        try {
            //获取配置文件资源
            InputStream inputStream= Resources.getResourceAsStream("./conf/mybatis-config.xml");
            //获取SqlSessionFactory对象
            factory=new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取SqlSession对象
     */
    public static SqlSession getSqlSession(){
        return factory.openSession();
    }

    /**
     * 关闭SqlSession对象
     */
    public static void closeSqlSession(SqlSession session){
        if(null!=session){
            //关闭Sqlsession对象
            session.close();
        }
        session=null;
    }


}
