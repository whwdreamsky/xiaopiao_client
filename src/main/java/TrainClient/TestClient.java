package TrainClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oliver on 2017/4/17.
 */
public class TestClient
{
    public static String INPUT_PROMPT="> ";
    public static void main(String args[])
    {
        TrainClient trainClient = new TrainClient();
        String line="";
        //GlobalData.LOADTRAINCODE();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print(INPUT_PROMPT);
            while (null != (line = reader.readLine())) {
               trainClient.run("oliver58",line);
                System.out.print(INPUT_PROMPT);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("See ya!");

        //map.put("starttime","123");

        //NLGenerateFactory nlGenerateFactory = new NLGenerateFactory(map);
        //nlGenerateFactory.analysePatternXml();
    }

}
