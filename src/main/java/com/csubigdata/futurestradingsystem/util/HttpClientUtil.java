package com.csubigdata.futurestradingsystem.util;

import com.csubigdata.futurestradingsystem.common.Constants;
import com.csubigdata.futurestradingsystem.vo.RemoteVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Slf4j
public class HttpClientUtil {

    @Value("${remote.url}")
    private static String FLASK_BASE_URL;

    public static int doPostParams(RemoteVO remoteVO, String url) {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 参数是对象
        HttpPost httpPost = getObjectHttpPost(remoteVO, url);
        // 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        CloseableHttpResponse response = null;
        try {

            response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            log.info("【发送POST请求】成功，相应状态为：{}", response.getStatusLine());
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode() && null != httpEntity) {

                String result = EntityUtils.toString(httpEntity);
                log.info("【发送POST请求】成功，响应内容为：{}", result);
                return Constants.SUCCESS;
            }
            if (300 == response.getStatusLine().getStatusCode() && null != httpEntity) {

                String result = EntityUtils.toString(httpEntity);
                log.info("【发送POST请求】成功，响应内容为：{}", result);
                return Constants.HOLDING;
            }
        } catch (IOException e) {

            log.error("【发送POST请求】失败，执行发送请求时，出现IO异常，异常信息为：{}", e);
            return Constants.ERROR;
        } finally {
            try {
                httpClient.close();
//                close(httpClient, response);
            } catch (IOException e) {

                log.error("【发送POST请求】失败，关闭流时，出现IO异常，异常信息为：{}", e);
            }
        }
        return Constants.ERROR;
    }


    public static HttpPost getObjectHttpPost(RemoteVO tradingVO, String url) {

        HttpPost httpPost = new HttpPost(Constants.FLASK_BASE_URL + url);
        // 将JAVA对象转换为Json字符串
        String jsonString = JsonUtil.objToJson(tradingVO);
        StringEntity stringEntity = new StringEntity(jsonString, "UTF-8");
        // post请求是将参数放在请求体里面传过去的
        httpPost.setEntity(stringEntity);
        return httpPost;
    }

}
