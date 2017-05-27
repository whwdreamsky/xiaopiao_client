package webservice;

import java.util.List;

/**
 * Created by oliver on 2017/4/29.
 */
public class TestVoice {

    public static void main(String args[])
    {
        ASRVoice asrVoice = new ASRVoice();


        if(asrVoice.transferToText("src/test.pcm"))
        {

            ResultVoice resultVoice = asrVoice.getResultVoice();
            if(resultVoice.getErr_no() == 0)
            {
                System.out.println(resultVoice.getResultstr());
            }
            System.out.println("errno:"+resultVoice.getErr_no());
            System.out.println("errmsg:"+resultVoice.getErr_msg());
        }
        else
        {
            System.out.println("请求失败");
        }
    }
}
