/*
 * $Id: HttpUtils.java, 2017年9月12日 上午9:53:01 yangtao Exp $
 * 
 * Copyright (c) 2017 doublecom
 * All rights reserved.
 * 
 * This software is copyrighted and owned by ciotc or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.vocust.qywx.demo.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HttpUtils {

    private final static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * http get
     * 
     * @return
     * @throws Exception 
     */
    public static String get(String baseUrl, Map<String, String> param) throws Exception {
        String realUrl = baseUrl;
        StringBuffer sb = new StringBuffer();
        if (!MapUtils.isEmpty(param)) {
            int index = 0;
            for (Entry<String, String> entry : param.entrySet()) {
                if (index != 0) {
                    sb.append("&");
                }
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
                index++;
            }
        }
        if (sb.length() > 0) {
            realUrl = realUrl + "?" + sb.toString();
        }
        try {
            HttpGet httpget = new HttpGet(realUrl);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity);
                }
            } finally {
                httpclient.close();
                response.close();
            }
        } catch (IOException e) {
            throw new Exception(e);
        }
        return null;
    }

    /**
     * http post
     * 
     * @return
     */
    public static String post(String baseUrl, Map<String, String> param) {

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        for (Entry<String, String> entry : param.entrySet()) {
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        HttpPost httppost = new HttpPost(baseUrl);

        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);

            httpclient = HttpClients.createDefault();
            response = httpclient.execute(httppost);
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            } else {
                throw new Exception("http status=" + response.getStatusLine());
            }
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
            return null;
        } finally {
            if (httpclient != null)
                try {
                    httpclient.close();
                } catch (IOException e) {

                }
            if (response != null)
                try {
                    response.close();
                } catch (IOException e) {

                }
        }

    }

    public static String post(String baseUrl, String body) {

        HttpPost httppost = new HttpPost(baseUrl);
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-Type", "application/json;charset=utf-8");
        BasicHttpEntity httpEntity = new BasicHttpEntity();
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            httpEntity.setContent(new java.io.ByteArrayInputStream(body.getBytes("UTF-8")));
            httpEntity.setContentLength(body.getBytes("UTF-8").length);
            httppost.setEntity(httpEntity);
            // http post请求 连接 传输请求时间 3秒
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000)
                    .build();
            httppost.setConfig(requestConfig);

            httpclient = HttpClients.createDefault();
            response = httpclient.execute(httppost);
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            } else {
                throw new Exception("http status=" + response.getStatusLine());
            }
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
            return null;
        } finally {
            if (httpclient != null)
                try {
                    httpclient.close();
                } catch (IOException e) {

                }
            if (response != null)
                try {
                    response.close();
                } catch (IOException e) {

                }
        }

    }

    /**
     * http post and return object
     * 
     * @param baseUrl
     * @param param
     * @param resultClass
     * @return
     */
    public static <T> T post(String baseUrl, Map<String, String> param, Class<T> resultClass) {
        String result = post(baseUrl, param);
        return new Gson().fromJson(result, resultClass);
    }

    public static <T> T post(String baseUrl, String body, Class<T> resultClass) {
        String result = post(baseUrl, body);
        return new Gson().fromJson(result, resultClass);
    }
    
    public static <T> List<T> post(String baseUrl, Map<String, String> param, TypeToken<List<T>> typeToken) {
        String result = post(baseUrl, param);
        return new Gson().fromJson(result, typeToken.getType());
    }


    /**
     * http get and return object
     * 
     * @param baseUrl
     * @param param
     * @param resultClass
     * @return
     * @throws Exception 
     */
    public static <T> List<T> get(String baseUrl, Map<String, String> param, TypeToken<List<T>> typeToken) throws Exception {
        String result = get(baseUrl, param);
        return new Gson().fromJson(result, typeToken.getType());
    }

    public static <T> T get(String baseUrl, Map<String, String> param, Class<T> resultClass) throws Exception {
        String result = get(baseUrl, param);
        return new Gson().fromJson(result, resultClass);
    }



}
