package com.etl.base.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

/**
 * <b>author</b>: forvoyager@outlook.com
 * <b>time</b>: 2020-01-13 11:48:00 <br>
 * <b>description</b>: Http访问工具类<br>
 */
public class HttpUtils {

  protected static final String DEFAULT_CHARSET = "UTF-8";

  protected static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

  protected static final String HTTP_REQUEST_LOG_PATTERN = "{} {}, with parameter {}, and cost is {}ms, http status is {}, result is {}";
  protected static final String HTTP_METHOD_POST = "POST";
  protected static final String HTTP_METHOD_GET = "GET";

  public static String post(String url) throws Exception{
    return post(url, null);
  }

  public static String post(String url, Map<String, Object> params) throws Exception{

    long startTime = System.currentTimeMillis();
    HttpURLConnection conn = getConnection(url, HTTP_METHOD_POST);

    if (params != null && params.size() > 0) {
      StringBuffer reqParams = new StringBuffer();
      for (String key : params.keySet()) {
        reqParams.append(key).append("=").append(URLEncoder.encode(params.get(key).toString(), DEFAULT_CHARSET)).append("&");
      }

      OutputStream outwritestream = conn.getOutputStream();
      outwritestream.write(reqParams.toString().getBytes(DEFAULT_CHARSET));
      outwritestream.flush();
      outwritestream.close();
    }

    String result = parseResponse(conn);
    conn.disconnect();

    logger.info(HTTP_REQUEST_LOG_PATTERN, "POST", url, JsonUtils.parseJson(params), (System.currentTimeMillis() - startTime), conn.getResponseCode(), result);

    return result;
  }

  public static String get(String url) throws Exception{
    return get(url, null);
  }

  public static String get(String url, Map<String, Object> params) throws Exception{

    StringBuffer finalUrl = new StringBuffer();
    if (params != null && params.size() > 0) {
      for (String key : params.keySet()) {
        finalUrl.append("&").append(key).append("=").append(URLEncoder.encode(params.get(key).toString(), DEFAULT_CHARSET));
      }
      finalUrl.replace(0, 1, "?");
    }
    finalUrl.insert(0, url);

    long startTime = System.currentTimeMillis();
    HttpURLConnection conn = getConnection(finalUrl.toString(), HTTP_METHOD_GET);

    String result = parseResponse(conn);
    conn.disconnect();

    logger.info(HTTP_REQUEST_LOG_PATTERN, "GET", finalUrl.toString(), JsonUtils.parseJson(params), (System.currentTimeMillis() - startTime), conn.getResponseCode(), result);

    return result;
  }

  public static byte[] getBytes(String url, Map<String, Object> params) throws Exception{

    StringBuffer finalUrl = new StringBuffer();
    if (params != null && params.size() > 0) {
      for (String key : params.keySet()) {
        finalUrl.append("&").append(key).append("=").append(URLEncoder.encode(params.get(key).toString(), DEFAULT_CHARSET));
      }
      finalUrl.replace(0, 1, "?");
    }
    finalUrl.insert(0, url);

    long startTime = System.currentTimeMillis();
    HttpURLConnection conn = getConnection(finalUrl.toString(), HTTP_METHOD_GET);
    InputStream is = conn.getInputStream();

    if(is != null){
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      try {
        byte buff[] = new byte[8*1024];
        int len = 0;
        while ( (len = is.read(buff) ) > 0 ) {
          baos.write(buff, 0, len);
        }
        baos.flush();
        return baos.toByteArray();
      }finally {
        logger.info(HTTP_REQUEST_LOG_PATTERN, "GET", finalUrl.toString(), JsonUtils.parseJson(params), (System.currentTimeMillis() - startTime), conn.getResponseCode(), "");
        if(is != null){ is.close(); }
        if(baos != null){ baos.close(); }
        conn.disconnect();
      }
    }

    return new byte[]{};
  }

  private static HttpURLConnection getConnection(String url, String type) throws Exception {
    type = StringUtils.isEmpty(type) ? "GET": type.toUpperCase();
    HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
    conn.setRequestProperty("Charset", DEFAULT_CHARSET);
    conn.setRequestProperty("accept", "*/*");
    conn.setRequestProperty("connection", "Keep-Alive");
    conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0");
    if(HTTP_METHOD_POST.equals(type)){
      conn.setDoOutput(true);
    }
    conn.setDoInput(true);
    conn.setUseCaches(false);
    conn.setRequestMethod(type);
    conn.setInstanceFollowRedirects(true);
    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    conn.setRequestProperty("accept","*/*"); // */* application/json
    conn.connect();
    return conn;
  }

  private static String parseResponse(HttpURLConnection conn) throws Exception{

    InputStream responseStream = null;
    if(conn.getResponseCode() == 200) {
      responseStream = conn.getInputStream();
    }else {
      responseStream = conn.getErrorStream();
    }

    StringBuilder result = new StringBuilder();

    BufferedReader in = new BufferedReader(new InputStreamReader(responseStream));
    try {
      String line = null;
      while ((line = in.readLine()) != null) {
        result.append(URLDecoder.decode(line, DEFAULT_CHARSET));
      }
    } catch (IOException e) {
      logger.error("parse response occur exception.", e);
    } finally {
      if(in != null){
        try {
          in.close();
        } catch (Exception e) { }
      }
    }

    return result.toString();
  }
}
