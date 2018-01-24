package com.jack.lottery.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class URLConnectionUtil {
	private static final Logger logger = LoggerFactory.getLogger(URLConnectionUtil.class);

	public static String doPost(String urlString, JSONObject obj){
		logger.info("连接{}请求开始,请求参数{}", urlString, obj.toJSONString());
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		 try {
	            //创建连接
	            URL url = new URL(urlString);
	            connection = (HttpURLConnection) url
	                    .openConnection();
	            connection.setDoOutput(true);
	            connection.setDoInput(true);
	            connection.setRequestMethod("POST");
	            connection.setUseCaches(false);
	            connection.setInstanceFollowRedirects(true);
	            connection.setRequestProperty("Content-Type",
	                    "application/json;charset=utf-8");
	            connection.setConnectTimeout(3000);
			    connection.setReadTimeout(3000);

	            connection.connect();

	            //POST请求
	            DataOutputStream out = new DataOutputStream(
	                    connection.getOutputStream());

	            out.write(obj.toString().getBytes("utf-8"));
	            out.flush();
	            out.close();

	            //读取响应
	            reader = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream()));
	            String lines;
	            StringBuffer sb = new StringBuffer("");
	            while ((lines = reader.readLine()) != null) {
	                lines = new String(lines.getBytes(), "utf-8");
	                sb.append(lines);
	            }
	            logger.info("请求参数:"+obj+"------>>>返回结果:"+sb);
	            return sb.toString();
	        } catch (Exception e) {
	            logger.error("连接{}请求报错:{}", urlString+"--->"+obj.toJSONString(),e);
	            return null;
	        } finally {
	        	if (null != reader) {
					try {
						reader.close();
					} catch (IOException e) {
						logger.error("关闭connection连接失败", e);
					}
				}
	        	// 断开连接
	        	if (null != connection) {
					connection.disconnect();
				}
	        }
	}

	/**
	 * 向指定URL发送GET方法的请求
	 *
	 * @param url
	 *            发送请求的URL
	 * @param params
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 */
	public static String doGet(String url, String params) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlName = url + "?" + params;
			System.out.println(urlName);
			URL realUrl = new URL(urlName);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			//conn.setRequestProperty("Content-type", "application/json;charset=UTF-8");
			conn.setReadTimeout(3000);
			conn.setConnectTimeout(3000);
			// 建立实际的连接
			conn.connect();
			// 获取所有响应头字段

			Map<String, List<String>> map = conn.getHeaderFields();
			// 遍历所有的响应头字段
			/*for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}

			System.out.println(conn.getResponseCode());*/
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// 定义BufferedReader输入流来读取URL的响应
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					line = new String(line.getBytes(), "utf-8");
					result += line;
				}
			}
			conn.disconnect();
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}
