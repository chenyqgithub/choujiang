package com.choujiang.choujiang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.choujiang.choujiang.resouce.RandomNum;
import com.choujiang.choujiang.utils.HttpUtils;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.HashMap;


@SpringBootApplication
public class ChoujiangApplication extends WebMvcConfigurerAdapter implements CommandLineRunner {
    public static final Log logger = LogFactory.getLog(ChoujiangApplication.class);
    public static void main(String[] args) {

        SpringApplication.run(ChoujiangApplication.class, args);
        //初始化Resouce值
        String s = HttpUtils.sendPost("http://47.104.252.44:8089/api/getabcd", new HashMap<>());
       if(!StringUtils.isEmpty(s)){
           JSONObject jsonObject = JSON.parseObject(s);
           Integer one = Integer.parseInt(""+jsonObject.get("a"));
           Integer two = Integer.parseInt(""+jsonObject.get("b"));
           Integer three = Integer.parseInt(""+jsonObject.get("c"));
           Integer four = Integer.parseInt(""+jsonObject.get("d"));
           RandomNum.a=one;
           RandomNum.b=two;
           RandomNum.c=three;
           RandomNum.d=four;

           //赋值list区间值
           for(int i=0;i<(1650-four);i++){
               RandomNum.list.add(i);
           }
           for(int i=0;i<(240-three);i++){
               RandomNum.list.add((1650+i));
           }
           for(int i=0;i<(100-two);i++){
               RandomNum.list.add((1890+i));
           }
           for(int i=0;i<(10-one);i++){
               RandomNum.list.add((1990+i));
           }
           //数据加载完毕
           logger.info("数据加载完毕");
       }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/.well-known/**").addResourceLocations("classpath:/.well-known/");
        super.addResourceHandlers(registry);
    }

//    @Bean
//    public TomcatServletWebServerFactory servletContainer() {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint securityConstraint = new SecurityConstraint();
//                securityConstraint.setUserConstraint("CONFIDENTIAL");//confidential
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                securityConstraint.addCollection(collection);
//                context.addConstraint(securityConstraint);
//            }
//        };
//        tomcat.addAdditionalTomcatConnectors(httpConnector());
//        return tomcat;
//
//    }

    @Bean
    public Connector httpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");

        //Set the scheme that will be assigned to requests received through this connector
        //@param scheme The new scheme
        connector.setScheme("http");

        //Set the port number on which we listen for requests.
        // @param port The new port number
        connector.setPort(80);

        //Set the secure connection flag that will be assigned to requests received through this connector.
        //@param secure The new secure connection flag
        //if connector.setSecure(true),the http use the http and https use the https;else if connector.setSecure(false),the http redirect to https;
        connector.setSecure(false);

        //redirectPort The redirect port number (non-SSL to SSL)
//        connector.setRedirectPort(443);
        return connector;
    }

//    @Bean
//    public Connector connector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        connector.setPort(8080);
//        connector.setSecure(false);
//        connector.setRedirectPort(8075);
//        return connector;
//    }
//
//    @Bean
//    public TomcatServletWebServerFactory servletContainer() {
//        TomcatServletWebServerFactory  tomcat = new TomcatServletWebServerFactory () {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint constraint = new SecurityConstraint();
//                constraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/");
//                constraint.addCollection(collection);
//                context.addConstraint(constraint);
//            }
//        };
//        tomcat.addAdditionalTomcatConnectors(connector());
//        return tomcat;
//    }


    public void run(String... strings) throws Exception {

    }
}
