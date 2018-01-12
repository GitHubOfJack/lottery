package com.jack.lottery.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class HistoryTask {

    public static void getConn() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/lottery");
        Statement statement = connection.createStatement();
    }

    public static void ssq() throws IOException {
        String url = "http://kaijiang.500.com/shtml/ssq/18005.shtml";
        Document document = Jsoup.connect(url).get();
        Elements iSelectList = document.getElementsByClass("iSelectList");
        Elements as = iSelectList.select("a");
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
            System.out.println(sb.toString());
        }
    }

    public static void main(String[] args) throws IOException {
        ssq();
    }

}
