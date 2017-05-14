package UtilTools;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
/**
 * Created by oliver on 2017/4/10.
 */
public class UntilXML {
    private  DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private Element root;
    public UntilXML()  {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            documentBuilder=documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    public Element parseDMXML(String filepath)
    {

        try
        {
            InputStream in = UntilXML.class.getClassLoader().getResourceAsStream(filepath);
            if (in==null)
            {
                System.err.println("XML can't be found");
                return null;
            }

            document=documentBuilder.parse(in);
            root=document.getDocumentElement();
            return root;

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
