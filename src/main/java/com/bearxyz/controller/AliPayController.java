package com.bearxyz.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by bearxyz on 2017/9/10.
 */
@Controller
@RequestMapping(value = "/alipay")
public class AliPayController {
    
    private static final String APP_ID = "2017090708605502";

    private static final String CHARSET = "utf-8";

    private static final String APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCae85gO/ymlOZOFNQtQqxDGAgs4IVMuN6n8lK9t1dyE5utMQX6LVENC/HT+qt/ApToobtJY7EVDxSi4Ot8tg7Kbjzq7jOfs1QQbA+6JjY2v5crSbh2ZkNSISOMvlA/6QqcAx7VNqpINJ7i3ngZiqm8zDuUUV92+VLXa352esoGWtSnMu4vfatgcSXmWQvtTgHTPFWqOOmxgPmlHKEnvrB2vPGfqHgVjYhUlwBr/T5ukst1UFJeI1x7DH+e9INCCXekKK1jsnKiCHVtnR+VM9yajq8yVJNtLxYZX9eUVCS2cV7dWxQU7gwlL+RUnKHZ5z2qi7JsYaKZ5CrKxYs0yii3AgMBAAECggEAWZqoKL+CmyjQsMd6DkqW2k/NJiKQjuDIHQf8IBiA+yUObhV6TxML6RJdkUPbR087CfgNRtMxOnLF2He3f12mFJp+cRH7FY2Rm/jh4moZlgXo6+3Vta85KyEXmqIkr/0+7yEMOEV93WXQJu09IiuAqoaXpD7SrZKOD29FKfmsxqPtZvvyi7xD/ieKlKNJCxjIneqbnBfNaoF+dY6ORA0IdL9dD0pJUa4kgy57lT++9w33YoGuSacP4z5oCerRUNRd5vm4T4tO6rptm6jQB5Qn725o+ikXZGU44Q3dXoziWkmJgLq0u8U79O9oKIpSg40JeiB04+FcdPTt1vcV61Q9mQKBgQDmt+BMWAhKJyLrnvQ1o7NSWrKyaw2Ppr3RKYuUnLw5nqwrPyXH5zZUwAPYJnuei+Ez7cI5NTZ9NclZKr+OFiKtaKju9U9AwT3PifVp4C/EisMcMQcZklqD3ELm5zo4WtChShydmFALYHU4oZKsQL4UpoeK770D5TfUUMQvTTMQvQKBgQCraWQCrgCA8orMxHETa8vxmyUZ2A+PxKLdwzxGnGBAsyXjlI18fmCkWtHIircI5EwIONrVPxz8nhvtJccDya4wH6k03O5F/nECUEDh2W9kVUekUo3r+1O82KYgZqt1EWFetDVJi0PZfdNU+qTd6eykumGYiY0svCqH7Bo0GBB4gwKBgQDL3GWAj7ijbPlaINXAH6lvd1y2cglFiRvoGUGQSv22HBIdFGsZu58yO4gqEbbhXM/cxpT7X52J/WCwPSmNLtntR7Edk/w3R4iKPWhv6PvMOqAz5M5VNhBsus8Sn/W9neEC4y1twwvXQZ/SLlIfigVq0cqqZTMqofs2yzbOZPReMQKBgQCQ3fVkPmK2mEWuYaefXk9y1kg4213rh6iN+98JXkgK5l1zRa1+NeC/hvIdpv6iSS/pEwP/jeOVrJq1hmP4U48fpOKAhufpx+0Co4jyV63JAjoWKp8/fZ170S413A+0VQgR3gPbMBQb0De3bk3AL1+MqOiq3jWuRSk8ztImjN/+uQKBgQDDbrIlxkP3slXwnuCw607Z3Yox9pWz12t7MYYK9dM+pAvx/Qw9DjN8tiv72cyaLWFwjG5BFJW0HXo9H12esAwHi/NuVbNZ1kmFbAuM8RpKExsIdL1T4AF9fXqUnTvGKk1x8KtY/B3MXpYS3Ncg9Kh9I769oEApdL8OwZ3rqa1Y0Q==";

    private static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmnvOYDv8ppTmThTULUKsQxgILOCFTLjep/JSvbdXchObrTEF+i1RDQvx0/qrfwKU6KG7SWOxFQ8UouDrfLYOym486u4zn7NUEGwPuiY2Nr+XK0m4dmZDUiEjjL5QP+kKnAMe1TaqSDSe4t54GYqpvMw7lFFfdvlS12t+dnrKBlrUpzLuL32rYHEl5lkL7U4B0zxVqjjpsYD5pRyhJ76wdrzxn6h4FY2IVJcAa/0+bpLLdVBSXiNcewx/nvSDQgl3pCitY7Jyogh1bZ0flTPcmo6vMlSTbS8WGV/XlFQktnFe3VsUFO4MJS/kVJyh2ec9qouybGGimeQqysWLNMootwIDAQAB";

    @RequestMapping("/pay")
    public void pay(HttpServletRequest httpRequest,
                    HttpServletResponse httpResponse) throws IOException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID,APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("http://erp.xjd.com/alipay/return");
        alipayRequest.setNotifyUrl("http://erp.xjd.com/alipay/notify");//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"20150320010101001\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":88.88," +
                "    \"subject\":\"Iphone6 16G\"," +
                "    \"body\":\"Iphone6 16G\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }"+
                "  }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

}
