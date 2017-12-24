package com.zhongyun.smoke.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.zhongyun.smoke.ApplicationConfig;
import com.zhongyun.smoke.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caozhennan on 2017/12/24.
 */
@Service
public class SmsService {
    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    @Autowired
    private ApplicationConfig config;

    private static final Logger logger = LoggerFactory.getLogger("SmsService");

    public void send(List<String> recvs, String address, String time, String tel) {
        Map<String, String> args = new HashMap<>();
        args.put("address", address);
        args.put("time", time);
        args.put("tel", tel);
        sendSms(recvs, Util.object2Json(args));
    }

    private void sendSms(List<String> recvs, String params) {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", config.getSmsId(), config.getSmsSec());
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            logger.error("SMS exception", e);
            e.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);

        for (String recv : recvs) {
            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            //必填:待发送手机号
            request.setPhoneNumbers(recv);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName("安合讯");
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode("SMS_117290477");
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam(params);

            //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");

            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//          request.setOutId("yourOutId");

            //hint 此处可能会抛出异常，注意catch
            try {
                SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
                if (sendSmsResponse.getCode().equals("OK")) {
                    logger.debug("SMS succeeded to " + recv);
                } else {
                    logger.error("SMS failed to " + recv);
                }
            } catch (ClientException e) {
                logger.error("SMS exception", e);
                return;
            }
        }
    }
}
