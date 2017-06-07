package TrainClient;
import UtilTools.UntilXML;
import com.google.gson.Gson;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by oliver on 2017/4/19.
 */
public class NLGenerateFactory
{
    private Map<String,String> nlgneed;
    private SchemaConifg schemaConifg;
    private List<String> patternlist;
    private List<String> nlgresult;

    public NLGenerateFactory(Map<String,String> nlgneed,SchemaConifg schemaConifg)
    {
        this.nlgneed = nlgneed;
        this.schemaConifg = schemaConifg;
        this.nlgresult = new ArrayList<>();
        this.patternlist = new ArrayList<>();
        setNeed();
    }

    public void nlgProcess()
    {


        System.out.println("nlgneed: " + new Gson().toJson(nlgneed));
        analysePatternXml();
        if(patternlist.size()==0)
        {
            nlgresult.add("小票不知道您在说什么~");
            System.err.println("没有模板");
        }
        else
        {
            for (int i=0;i<patternlist.size();i++)
            {
                analysePattern(patternlist.get(i));
            }
            if(nlgresult.size()==0)
            {
                nlgresult.add("对不起小票不知道您在说什么~");
            }

        }

    }

    public void setNeed()
    {
        //之前有设置意图为clarify 为防止这个被清晰掉
        if(!nlgneed.containsKey("intent"))
        {
            nlgneed.put("intent",this.schemaConifg.getIntent());
        }
        if(!schemaConifg.getArrivedate().getValue().equals(""))nlgneed.put("arrivedate",schemaConifg.getArrivedate().getValue());
        nlgneed.put("traintype",GlobalData.GetTrainType(schemaConifg.getTraintype().getValue()));
        nlgneed.put("startpoint",schemaConifg.getStartpoint().getValue());
        nlgneed.put("arrivepoint",schemaConifg.getArrivepoint().getValue());
        nlgneed.put("startdate",schemaConifg.getStartdate().getValue());
        nlgneed.put("userid",schemaConifg.getUserid());

    }


    public void analysePattern(String patternstr)
    {
        String[] strlist = patternstr.split(" ");
        String key="";
        String result ="";
        for(int i=0;i<strlist.length;i++)
        {
            if(strlist[i].indexOf("{",0)!=-1||strlist[i].indexOf("}",0)!=-1)
            {
                key = strlist[i].substring(1,strlist[i].length()-1);
                if(!nlgneed.containsKey(key))
                {
                    System.out.println("缺乏必要参数："+key);
                    return ;
                }
                strlist[i] = nlgneed.get(key);
            }
        }
        for(int i=0;i<strlist.length;i++)
        {
            result+=strlist[i];
        }
        nlgresult.add(result);

    }

    public void analysePatternXml()
    {
        UntilXML untilXML = new UntilXML();
        Element root = untilXML.parseDMXML("./conf/nlgpattern.xml");
        int parampass,k;
        parampass=0;
        k=0;
        if(root!=null)
        {
            System.out.println("1");
            NodeList intentpatterns = root.getChildNodes();
            if(intentpatterns!=null)
            {
                System.out.println("2");
                for(int i=0;i<intentpatterns.getLength();i++)
                {
                    Node interntnode =  intentpatterns.item(i);
                    if(!(interntnode instanceof Element)) continue;
                    Element intentpattern = (Element)interntnode;
                    if(intentpattern.getAttribute("intent").equals(nlgneed.get("intent")))
                    {
                        System.out.println("3");
                        if(intentpattern.hasChildNodes())
                        {
                            NodeList patternslist = intentpattern.getChildNodes();
                            if(patternslist!=null)
                            {
                                for(int j=0;j<patternslist.getLength();j++)
                                {
                                    Node patternnode = patternslist.item(j);
                                    if(!(patternnode instanceof Element)) continue;
                                    Element patterns = (Element)patternnode ;
                                    if(patterns.getAttribute("type").equals(nlgneed.get("type")))
                                    {
                                        System.out.println("5");
                                        parampass=1;
                                        if(patterns.getElementsByTagName("require")!=null)
                                        {
                                            if(patterns.getElementsByTagName("require").item(0) instanceof Element)
                                            {
                                                Element requireslot = (Element) patterns.getElementsByTagName("require").item(0);
                                                if(requireslot.hasChildNodes())
                                                {
                                                    NodeList slotlist = requireslot.getElementsByTagName("param");
                                                    for(k=0;k<slotlist.getLength();k++)
                                                    {
                                                        if(!(slotlist.item(k) instanceof Element)) continue;
                                                        String paramname = ((Element) slotlist.item(k)).getFirstChild().getNodeValue().toString();
                                                        if(!(nlgneed.containsKey(paramname))) break;
                                                    }
                                                    if(k!=slotlist.getLength())parampass=0;
                                                    System.out.println("parampass"+parampass);
                                                }
                                            }

                                        }
                                        if(parampass==0) continue;
                                        NodeList patternlist =patterns.getElementsByTagName("pattern");
                                        if(patternlist!=null)
                                        {
                                            System.out.println("6");
                                            //System.out.println(((Element)patternlist.item(0)).getFirstChild().getNodeValue());

                                            for(int q=0;q<patternlist.getLength();q++)
                                            {
                                                if(patternlist.item(q) instanceof Element)
                                                {
                                                    String patterntemp =((Element)patternlist.item(q)).getFirstChild().getNodeValue();
                                                    this.patternlist.add(patterntemp);
                                                    System.out.println(patterntemp);

                                                }
                                            }
                                            break;
                                        }




                                    }
                                }
                            }
                        }
                    }
                }

            }
        }


    }


    public List<String> getNlgresult() {
        return nlgresult;
    }

    public List<String> getPatternlist() {
        return patternlist;
    }
}
