package read;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

/**
 * HttpClient工具类
 */
public class HttpUtil {

    private static Logger logger = Logger.getLogger(HttpUtil.class);


    /**
     * post请求（用于请求json格式的参数）
     *
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, String params) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(params, charSet);
        httpPost.setEntity(entity);

        HttpResponse response = httpclient.execute(httpPost);
        String readContent = null;
        System.out.println("1.Get Response Status: " + response.getStatusLine());
        if (entity != null) {
            System.out.println("  Get ResponseContentEncoding():" + entity.getContentEncoding());
            System.out.println("  Content Length():" + entity.getContentLength());
            //getResponse
            InputStream in = entity.getContent();
            int count = 0;
            while (count == 0) {
                count = Integer.parseInt("" + entity.getContentLength());//in.available();
            }
            byte[] bytes = new byte[count];
            int readCount = 0; // 已经成功读取的字节的个数
            while (readCount <= count) {
                if (readCount == count) break;
                readCount += in.read(bytes, readCount, count - readCount);
            }

            //转换成字符串
            readContent = new String(bytes, 0, readCount, "UTF-8"); // convert to string using bytes

            System.out.println("2.Get Response Content():\n" + readContent);
        }
        return readContent;
    }
}





