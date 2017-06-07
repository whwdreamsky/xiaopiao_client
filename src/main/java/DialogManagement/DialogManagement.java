package DialogManagement;

import NLU.NLUResult;
import UtilTools.ReadProperties;
import UtilTools.UntilXML;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by oliver on 2017/4/10.
 */
public class DialogManagement
{
    private String actionname="";
    private String actiontype="";
    private DMResultBean dmResultBean;
    private NLUResult nluResult;
    private SessionManagement sessionManagement;


    public DialogManagement()
    {
        sessionManagement = new SessionManagement();
    }
    public boolean getSession(String userid)
    {
        //TO DO
        return false;
    }
    public void dialogManageProcess(NLUResult nluResult)
    {
        this.nluResult=nluResult;
        //处理nlu的结果添加offset 参数
        addSlotOffSet();
        //查询session
        sessionManagement.connectSession();
        SessionObject sessionold = sessionManagement.getSession(nluResult.getAppname(),nluResult.getUserid());
        if(sessionold==null)
        {
            triggerStrategy();
            dmResultProduce();
            System.out.println("actiontype"+dmResultBean.getActiontype()+"actionname" +dmResultBean.getActionname());
            if(!(dmResultBean.getActiontype().equals("fail")||dmResultBean.getActionname().equals("failaction")))
            {
                sessionManagement.saveSession(this.dmResultBean);
                System.out.println("save1");
            }


        }
        else
        {
            DMResultBean dmResultold = sessionManagement.getLastDmResult(sessionold);
            //若为update意图的mergeslot方式，主动澄清
            if(nluResult.getIntent().equals("UPDATESLOT"))
            {
                nluResult.setIntent(dmResultold.getIntent());
            }
            //为被动澄清槽位
            mergeSlot(nluResult,dmResultold);
            triggerStrategy();
            dmResultProduce();
            if(!(dmResultBean.getActiontype().equals("fail")||dmResultBean.getActionname().equals("failaction")))
            {
                sessionManagement.saveSession(this.dmResultBean,sessionold);
                System.out.println("save2");
            }

            
        }
        sessionManagement.closesession();

    }

    //为槽位添加偏移量
    private void addSlotOffSet() {
        HashMap<String,JsonElement> newslots = this.nluResult.getSlotlist();
        Iterator iterator= newslots.entrySet().iterator();
        while(iterator.hasNext())
        {
            Map.Entry entry = (Map.Entry) iterator.next();
            JsonElement oldslotvalue = (JsonElement) entry.getValue();
            JsonElement newslotvalue = new JsonObject();
            newslotvalue.getAsJsonObject().addProperty("offset",0);
            newslotvalue.getAsJsonObject().add("value",oldslotvalue);
            newslots.put((String)entry.getKey(),newslotvalue);
        }
        this.nluResult.setSlotlist(newslots);
    }

    //若为update意图的mergeslot方式，主动澄清
    private void mergeSlotForUpdate(NLUResult nluResult,DMResultBean dmResultBeanold)
    {
        nluResult.setIntent(dmResultBeanold.getIntent());
        HashMap<String,JsonElement> slotlist = nluResult.getSlotlist();



    }
    //为被动澄清槽位
    private void mergeSlot(NLUResult nluResult,DMResultBean dmResultBean)
    {
        Iterator itoldslot = dmResultBean.getSlotlist().entrySet().iterator();
        HashMap<String,JsonElement> newslot = nluResult.getSlotlist();
        //读取槽位的存活周期
        Map slotmap = ReadProperties.getSlotClyclePropers();
        /*
        String notextendedstr = ReadProperties.getDMPropers("notextended");
        String[] notextendedlist = null;
        if(!notextendedstr.equals(""))
        {
            System.out.println("notextendedlist : " + notextendedstr);
            notextendedlist = notextendedstr.split(",");
        }
        */
        int offset=0;
        while(itoldslot.hasNext())
        {
            Map.Entry entry= (Map.Entry)itoldslot.next();
            String key = entry.getKey().toString();
            //根据配置去除不要被继承的槽位
            if(slotmap!=null)
            {
               //这里对超出生命周期的槽位进行过滤
                if(slotmap.containsKey(key))
                {
                    JsonObject slotcycle = (JsonObject) entry.getValue();
                    int  offset_old = slotcycle.get("offset").getAsInt()+1;
                    if(offset_old==Integer.parseInt((String)slotmap.get(key)))
                    {
                        continue;
                    }
                }
            }
            if(!newslot.containsKey(key))
            {
                JsonElement jsonElement = (JsonElement) entry.getValue();
                offset = jsonElement.getAsJsonObject().get("offset").getAsInt()+1;
                jsonElement.getAsJsonObject().addProperty("offset",offset);
                newslot.put(key,(JsonElement) entry.getValue());
            }
            //对于新一轮包含的槽位这里直接做替换，这个后期可以完善
            /*
            else
            {
                JsonElement jsonElement = ((JsonElement) entry.getValue()).getAsJsonObject().get("value");
                JsonElement newelement = newslot.get(entry.getKey()).getAsJsonObject().get("value");
                if(jsonElement instanceof JsonObject && newelement instanceof JsonObject)
                {
                    Iterator iteratornew = ((JsonObject) newelement).entrySet().iterator();
                    while (iteratornew.hasNext())
                    {
                        Map.Entry entry1 = (Map.Entry) iteratornew.next();
                        ((JsonObject) jsonElement).add((String) entry1.getKey(),(JsonElement) entry1.getValue());
                    }
                    JsonElement jsonmerge = newslot.get(entry.getKey());
                    jsonmerge.getAsJsonObject().add("value",jsonElement);
                    newslot.put((String)entry.getKey(),jsonmerge);
                }

            }
            */
        }
        nluResult.setSlotlist(newslot);
    }

    private void triggerStrategy()
    {
        UntilXML untilXML = new UntilXML();
        Element root = untilXML.parseDMXML("./conf/triggers.xml");
        String temp="";
        if (root != null)
        {
            NodeList triggers = root.getChildNodes();
            if (triggers != null)
            {
                for (int i=0;i<triggers.getLength();i++)
                {
                    Node trigger= triggers.item(i);
                    if (trigger instanceof Element)
                    {
                        Element triggerelement = (Element)trigger;
                        if (trigger!=null && trigger.getNodeType()==Node.ELEMENT_NODE)
                        {
                            if(triggerelement.getElementsByTagName("intent")!=null){
                                temp = triggerelement.getElementsByTagName("intent").item(0).getFirstChild().getNodeValue();
                            }
                            //System.out.println(nluResult.getIntent());
                            //判别intent匹配
                            if (nluResult.getIntent().equals(temp))
                            {
                                //匹配必须槽位
                                boolean require_bool = matchAciton(triggerelement,nluResult,"require");
                                //匹配没填充槽位
                                boolean notfilled_bool = matchAciton(triggerelement,nluResult,"notfilled");

                                System.out.println("req:"+require_bool+"notfild"+notfilled_bool);
                                if(require_bool&&notfilled_bool)
                                {
                                    actionname = triggerelement.getElementsByTagName("actionname").item(0).getFirstChild().getNodeValue();
                                    actiontype = triggerelement.getAttribute("type").toString();
                                }
                                if (!actionname.equals("")) break;

                            }
                            //System.out.println(temp);

                        }
                    }

                }
            }

        }
        if(actionname.equals(""))
        {
            actionname="failaction";
            actiontype="fail";
        }
    }


    public boolean matchAciton(Element triggerelement,NLUResult nluResult,String tagname)
    {
        boolean result =true;
        //通过不同tag 名称获取节点
        Node tagnode = triggerelement.getElementsByTagName(tagname).item(0);
        if(tagnode==null) return result;
        NodeList slots = ((Element)tagnode).getElementsByTagName("slot");
        if (slots != null)
        {
            for(int i=0;i<slots.getLength();i++)
            {
                Node slot = slots.item(i);
                if (slot instanceof Element)
                        {
                            String slotname = ((Element)slot).getFirstChild().getNodeValue().toString();
                            //System.out.println("22222222222"+slotname);
                            if(tagname.equals("require"))
                            {
                                System.out.println("require :"+slotname);
                                //require 表示必须要填充，因此遇到没有填充的返回错误
                                if (!nluResult.getSlotlist().containsKey(slotname))return false;
                            }
                            else if(tagname.equals("notfilled"))
                            {
                        //notfilled 表示必须不填充，因此遇到填充的返回错误
                        System.out.println("notfilled :"+slotname);
                        if (nluResult.getSlotlist().containsKey(slotname))return false;
                    }

                }

            }
        }

        return result;

    }

    private void dmResultProduce()
    {
        this.dmResultBean = new DMResultBean();
        this.dmResultBean.setActionname(this.actionname);
        this.dmResultBean.setActiontype(this.actiontype);
        this.dmResultBean.setIntent(nluResult.getIntent());
        this.dmResultBean.setSlotlist(nluResult.getSlotlist());
        this.dmResultBean.setUserid(nluResult.getUserid());
        this.dmResultBean.setAppname(nluResult.getAppname());
        this.dmResultBean.setQuery(nluResult.getQuery());
    }
    public String getActionname() {
        return actionname;
    }

    public DMResultBean getDmResultBean() {
        return dmResultBean;
    }
}
