package com.jack.lottery.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryTask {

    public static void ssq() throws Exception {

        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/lottery", "root", "root");


        String insert = "insert into lottery_term(type, term, startTime, endTime, isCurrent, status, result) values (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(insert);

        String url = "http://kaijiang.500.com/shtml/ssq/18005.shtml";
        Document document = Jsoup.connect(url).get();
        Elements iSelectList = document.getElementsByClass("iSelectList");
        Elements as = iSelectList.select("a");
        int m = 0;
        for (Element e : as) {
            String href = e.attr("href");
            Document document1 = Jsoup.connect(href).get();
            Elements cfont2 = document1.getElementsByClass("cfont2");
            String termNo = "20" + cfont2.text();
            System.out.println(termNo);
            Elements span_right = document1.getElementsByClass("span_right");
            String endTime = span_right.text();
            String year = endTime.split("兑奖截止日期")[0].split("年")[0].split("：")[1].trim();
            String month = endTime.split("兑奖截止日期")[0].split("年")[1].split("月")[0].trim();
            String day = endTime.split("兑奖截止日期")[0].split("年")[1].split("月")[1].split("日")[0].trim();
            String date = year + "-"+ month + "-"+day;
            System.out.println(date);
            Elements iSelectList1 = document1.getElementsByClass("ball_box01");
            Elements lis = iSelectList1.select("li");
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (Element li : lis) {
                sb.append(li.text());
                if (i == 5) {
                    sb.append("|");
                } else if (i == 6) {

                } else {
                    sb.append("^");
                }
                i++;
            }
            String result = sb.toString();
            System.out.println(result);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);
            java.sql.Date sd = new java.sql.Date(d.getTime());
            statement.setString(1, "1");
            statement.setString(2, termNo);
            statement.setDate(3, sd);
            statement.setDate(4, sd);
            statement.setByte(5, (byte)1);
            statement.setString(6, "2");
            statement.setString(7, result);
            statement.addBatch();
            if (m >0 && m%100 == 0) {
                statement.executeBatch();
                statement.clearBatch();
            }
            m++;
        }
        statement.executeBatch();
        statement.clearBatch();
        statement.close();
        connection.close();
    }

    public static void dlt() throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/lottery", "root", "root");


        String insert = "insert into lottery_term(type, term, startTime, endTime, isCurrent, status, result) values (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(insert);

        String url = "http://kaijiang.500.com/shtml/dlt/18008.shtml";
        Document document = Jsoup.connect(url).get();
        Elements iSelectList = document.getElementsByClass("iSelectList");
        Elements as = iSelectList.select("a");
        int m = 0;
        for (Element e : as) {
            String href = e.attr("href");
            Document document1 = Jsoup.connect(href).get();
            Elements cfont2 = document1.getElementsByClass("cfont2");
            String termNo = "20" + cfont2.text();
            System.out.println(termNo);
            if (termNo.compareTo("2016015") >= 0) {
                continue;
            }
            Elements span_right = document1.getElementsByClass("span_right");
            String endTime = span_right.text();
            String year = endTime.split("兑奖截止日期")[0].split("年")[0].split("：")[1].trim();
            String month = endTime.split("兑奖截止日期")[0].split("年")[1].split("月")[0].trim();
            String day = endTime.split("兑奖截止日期")[0].split("年")[1].split("月")[1].split("日")[0].trim();
            String date = year + "-"+ month + "-"+day;
            System.out.println(date);
            Elements iSelectList1 = document1.getElementsByClass("ball_box01");
            Elements lis = iSelectList1.select("li");
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (Element li : lis) {
                sb.append(li.text());
                if (i == 4) {
                    sb.append("|");
                } else if (i == 6) {

                } else {
                    sb.append("^");
                }
                i++;
            }
            String result = sb.toString();
            System.out.println(result);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);
            java.sql.Date sd = new java.sql.Date(d.getTime());
            statement.setString(1, "2");
            statement.setString(2, termNo);
            statement.setDate(3, sd);
            statement.setDate(4, sd);
            statement.setByte(5, (byte)1);
            statement.setString(6, "2");
            statement.setString(7, result);
            statement.addBatch();
            if (m >0 && m%100 == 0) {
                statement.executeBatch();
                statement.clearBatch();
            }
            m++;
        }
        statement.executeBatch();
        statement.clearBatch();
        statement.close();
        connection.close();
    }

    public static void sd() throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/lottery", "root", "root");


        String insert = "insert into lottery_term(type, term, startTime, endTime, isCurrent, status, result) values (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(insert);

        String url = "http://kaijiang.500.com/sd.shtml";
        Document document = Jsoup.connect(url).get();
        Elements iSelectList = document.getElementsByClass("iSelectList");
        Elements as = iSelectList.select("a");
        int m = 0;
        for (Element e : as) {
            String href = e.attr("href");
            Document document1 = Jsoup.connect(href).get();
            Elements cfont2 = document1.getElementsByClass("cfont2");
            String termNo = cfont2.text();
            System.out.println(termNo);
            /*if (termNo.compareTo("2016015") >= 0) {
                continue;
            }*/
            Elements span_right = document1.getElementsByClass("span_right");
            String endTime = span_right.text();
            String year = endTime.split("兑奖截止日期")[0].split("年")[0].split("：")[1].trim();
            String month = endTime.split("兑奖截止日期")[0].split("年")[1].split("月")[0].trim();
            String day = endTime.split("兑奖截止日期")[0].split("年")[1].split("月")[1].split("日")[0].trim();
            String date = year + "-"+ month + "-"+day;
            System.out.println(date);
            Elements iSelectList1 = document1.getElementsByClass("ball_box01");
            Elements lis = iSelectList1.select("li");
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (Element li : lis) {
                sb.append(li.text());
                if (i == 2) {

                } else {
                    sb.append("^");
                }
                i++;
            }
            String result = sb.toString();
            System.out.println(result);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);
            java.sql.Date sd = new java.sql.Date(d.getTime());
            statement.setString(1, "5");
            statement.setString(2, termNo);
            statement.setDate(3, sd);
            statement.setDate(4, sd);
            statement.setByte(5, (byte)1);
            statement.setString(6, "2");
            statement.setString(7, result);
            statement.addBatch();
            if (m >0 && m%100 == 0) {
                statement.executeBatch();
                statement.clearBatch();
            }
            m++;
        }
        statement.executeBatch();
        statement.clearBatch();
        statement.close();
        connection.close();
    }

    public static void pls() throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/lottery", "root", "root");


        String insert = "insert into lottery_term(type, term, startTime, endTime, isCurrent, status, result) values (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(insert);

        String url = "http://kaijiang.500.com/pls.shtml";
        Document document = Jsoup.connect(url).get();
        Elements iSelectList = document.getElementsByClass("iSelectList");
        Elements as = iSelectList.select("a");
        int m = 0;
        for (Element e : as) {
            String href = e.attr("href");
            Document document1 = Jsoup.connect(href).get();
            Elements cfont2 = document1.getElementsByClass("cfont2");
            String termNo = "20"+cfont2.text();
            System.out.println(termNo);
            if (termNo.compareTo("2016234") >= 0) {
                continue;
            }
            Elements span_right = document1.getElementsByClass("span_right");
            String endTime = span_right.text();
            String year = endTime.split("兑奖截止日期")[0].split("年")[0].split("：")[1].trim();
            String month = endTime.split("兑奖截止日期")[0].split("年")[1].split("月")[0].trim();
            String day = endTime.split("兑奖截止日期")[0].split("年")[1].split("月")[1].split("日")[0].trim();
            String date = year + "-"+ month + "-"+day;
            System.out.println(date);
            Elements iSelectList1 = document1.getElementsByClass("ball_box01");
            Elements lis = iSelectList1.select("li");
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (Element li : lis) {
                sb.append(li.text());
                if (i == 2) {

                } else {
                    sb.append("^");
                }
                i++;
            }
            String result = sb.toString();
            System.out.println(result);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);
            java.sql.Date sd = new java.sql.Date(d.getTime());
            statement.setString(1, "3");
            statement.setString(2, termNo);
            statement.setDate(3, sd);
            statement.setDate(4, sd);
            statement.setByte(5, (byte)1);
            statement.setString(6, "2");
            statement.setString(7, result);
            statement.addBatch();
            if (m >0 && m%100 == 0) {
                statement.executeBatch();
                statement.clearBatch();
            }
            m++;
        }
        statement.executeBatch();
        statement.clearBatch();
        statement.close();
        connection.close();
    }

    public static void main(String[] args) throws Exception {
        //ssq();
        //dlt();
        //sd();
        pls();
    }

}
