package com.bearxyz.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.bearxyz.domain.po.business.*;
import com.bearxyz.repository.OrderRepository;
import com.bearxyz.repository.SaleRepository;
import com.bearxyz.repository.StockRepository;
import com.bearxyz.utility.OrderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by bearxyz on 2017/9/10.
 */
@Controller
@RequestMapping(value = "/alipay")
public class AliPayController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private StockRepository stockRepository;
    
    private static final String APP_ID = "2017090708605502";

    private static final String CHARSET = "utf-8";

    private static final String APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCae85gO/ymlOZOFNQtQqxDGAgs4IVMuN6n8lK9t1dyE5utMQX6LVENC/HT+qt/ApToobtJY7EVDxSi4Ot8tg7Kbjzq7jOfs1QQbA+6JjY2v5crSbh2ZkNSISOMvlA/6QqcAx7VNqpINJ7i3ngZiqm8zDuUUV92+VLXa352esoGWtSnMu4vfatgcSXmWQvtTgHTPFWqOOmxgPmlHKEnvrB2vPGfqHgVjYhUlwBr/T5ukst1UFJeI1x7DH+e9INCCXekKK1jsnKiCHVtnR+VM9yajq8yVJNtLxYZX9eUVCS2cV7dWxQU7gwlL+RUnKHZ5z2qi7JsYaKZ5CrKxYs0yii3AgMBAAECggEAWZqoKL+CmyjQsMd6DkqW2k/NJiKQjuDIHQf8IBiA+yUObhV6TxML6RJdkUPbR087CfgNRtMxOnLF2He3f12mFJp+cRH7FY2Rm/jh4moZlgXo6+3Vta85KyEXmqIkr/0+7yEMOEV93WXQJu09IiuAqoaXpD7SrZKOD29FKfmsxqPtZvvyi7xD/ieKlKNJCxjIneqbnBfNaoF+dY6ORA0IdL9dD0pJUa4kgy57lT++9w33YoGuSacP4z5oCerRUNRd5vm4T4tO6rptm6jQB5Qn725o+ikXZGU44Q3dXoziWkmJgLq0u8U79O9oKIpSg40JeiB04+FcdPTt1vcV61Q9mQKBgQDmt+BMWAhKJyLrnvQ1o7NSWrKyaw2Ppr3RKYuUnLw5nqwrPyXH5zZUwAPYJnuei+Ez7cI5NTZ9NclZKr+OFiKtaKju9U9AwT3PifVp4C/EisMcMQcZklqD3ELm5zo4WtChShydmFALYHU4oZKsQL4UpoeK770D5TfUUMQvTTMQvQKBgQCraWQCrgCA8orMxHETa8vxmyUZ2A+PxKLdwzxGnGBAsyXjlI18fmCkWtHIircI5EwIONrVPxz8nhvtJccDya4wH6k03O5F/nECUEDh2W9kVUekUo3r+1O82KYgZqt1EWFetDVJi0PZfdNU+qTd6eykumGYiY0svCqH7Bo0GBB4gwKBgQDL3GWAj7ijbPlaINXAH6lvd1y2cglFiRvoGUGQSv22HBIdFGsZu58yO4gqEbbhXM/cxpT7X52J/WCwPSmNLtntR7Edk/w3R4iKPWhv6PvMOqAz5M5VNhBsus8Sn/W9neEC4y1twwvXQZ/SLlIfigVq0cqqZTMqofs2yzbOZPReMQKBgQCQ3fVkPmK2mEWuYaefXk9y1kg4213rh6iN+98JXkgK5l1zRa1+NeC/hvIdpv6iSS/pEwP/jeOVrJq1hmP4U48fpOKAhufpx+0Co4jyV63JAjoWKp8/fZ170S413A+0VQgR3gPbMBQb0De3bk3AL1+MqOiq3jWuRSk8ztImjN/+uQKBgQDDbrIlxkP3slXwnuCw607Z3Yox9pWz12t7MYYK9dM+pAvx/Qw9DjN8tiv72cyaLWFwjG5BFJW0HXo9H12esAwHi/NuVbNZ1kmFbAuM8RpKExsIdL1T4AF9fXqUnTvGKk1x8KtY/B3MXpYS3Ncg9Kh9I769oEApdL8OwZ3rqa1Y0Q==";

    private static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmnvOYDv8ppTmThTULUKsQxgILOCFTLjep/JSvbdXchObrTEF+i1RDQvx0/qrfwKU6KG7SWOxFQ8UouDrfLYOym486u4zn7NUEGwPuiY2Nr+XK0m4dmZDUiEjjL5QP+kKnAMe1TaqSDSe4t54GYqpvMw7lFFfdvlS12t+dnrKBlrUpzLuL32rYHEl5lkL7U4B0zxVqjjpsYD5pRyhJ76wdrzxn6h4FY2IVJcAa/0+bpLLdVBSXiNcewx/nvSDQgl3pCitY7Jyogh1bZ0flTPcmo6vMlSTbS8WGV/XlFQktnFe3VsUFO4MJS/kVJyh2ec9qouybGGimeQqysWLNMootwIDAQAB";

    @RequestMapping("/pay/{id}")
    public void pay(@PathVariable("id")String id, HttpServletRequest httpRequest,
                    HttpServletResponse httpResponse) throws IOException {
        Order order = orderRepository.findOrderByCode(id);
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID,APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("http://erp.xjdpx.com/#/my/order");
        alipayRequest.setNotifyUrl("http://erp.xjdpx.com/alipay/notify");//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\""+order.getCode()+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":"+order.getPrice()+"," +
                "    \"subject\":\"订单号:"+order.getCode()+"\"" +
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

    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public String nofify(HttpServletRequest httpRequest) throws UnsupportedEncodingException, AlipayApiException {
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = httpRequest.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, "RSA2"); //调用SDK验证签名
        if(signVerified) {//验证成功
            //商户订单号
            String out_trade_no = new String(httpRequest.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //支付宝交易号
            String trade_no = new String(httpRequest.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //交易状态
            String trade_status = new String(httpRequest.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

            if(trade_status.equals("TRADE_FINISHED")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            }else if (trade_status.equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知
                Order order = orderRepository.findOrderByCode(out_trade_no);
                order.setTradeNo(trade_no);
                order.setStatus(2);
                Sale sale = null;
                if (order.getSaleId() != null) sale = saleRepository.findOne(order.getSaleId());
                if (sale != null && !sale.getCategory().equals("GOODS_NORMAL")) {
                    sale.setBuyer(sale.getBuyer()+1);
                    saleRepository.save(sale);
                    saleRepository.insertCompanyUser(order.getCompanyId(), sale.getId(), sale.getCategory());
                } else {
                    Stock stock = new Stock();
                    stock.setType("STOCK-OUT");
                    stock.setMask("STOCK_OUT_ORDER");
                    stock.setCode(OrderUtils.genSerialnumber("CK"));
                    stock.setPurpose("销售出库");
                    stock.setDeliverAddress(order.getProvince() + order.getCity() + order.getDistrict() + order.getAddress());
                    for (OrderItem item : order.getItems()) {
                        Sale sale1 = saleRepository.findOne(item.getSaleId());
                        StockItem stockItem = new StockItem();
                        Sale s = saleRepository.findOne(item.getSaleId());
                        s.setBuyer(s.getBuyer()+item.getCount());
                        saleRepository.save(s);
                        stockItem.setGoodsId(sale1.getGoodsId());
                        stockItem.setUnit(sale1.getUnit());
                        stockItem.setCount(item.getCount());
                        stockItem.setPrice(item.getPrice());
                        stock.getItems().add(stockItem);
                    }
                    stock.setOrderId(order.getId());
                    stockRepository.save(stock);
                }
                order.setStatus(2);
                orderRepository.save(order);
            }

            return "success";

        }else {//验证失败
            return "fail";
        }
    }

}
