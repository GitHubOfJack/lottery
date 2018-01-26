package com.jack.lottery.task;

import com.alibaba.fastjson.JSONObject;
import com.jack.lottery.po.PrizeDetail;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryTask {

    public static void ssq() throws Exception {

        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/lottery", "root", "root");


        String insert = "insert into lottery_term(type, term, startTime, endTime, isCurrent, status, result, prize_detail) values (?, ?, ?, ?, ?, ?, ?, ?)";
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
            /*if (termNo.compareTo("2009019") > 0) {
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
                if (i == 5) {
                    sb.append("|");
                } else if (i == 6) {

                } else {
                    sb.append(",");
                }
                i++;
            }
            String result = sb.toString();
            System.out.println(result);

            List<PrizeDetail> detailList = new ArrayList<>();
            Elements trs = document1.getElementsByClass("kj_tablelist02").get(1).getElementsByTag("tr");
            for (int j=2; j<8; j++) {
                PrizeDetail prize = new PrizeDetail();
                Elements tds = trs.get(j).getElementsByTag("td");
                if (tds.get(2).text().equals("--") || tds.get(1).text().equals("--")) {
                    prize.setAmount(BigDecimal.valueOf(0));
                    prize.setNum(0);
                } else {
                    prize.setAmount(BigDecimal.valueOf(Double.parseDouble(tds.get(2).text().replace(",", ""))));
                    prize.setNum(Integer.parseInt(tds.get(1).text()));
                }
                prize.setName(String.valueOf(j-1));
                detailList.add(prize);
            }
            String prizeDetail = JSONObject.toJSONString(detailList);
            System.out.println(prizeDetail);

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
            statement.setString(8, prizeDetail);
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


        String insert = "insert into lottery_term(type, term, startTime, endTime, isCurrent, status, result, prize_detail) values (?, ?, ?, ?, ?, ?, ?, ?)";
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
            if (termNo.compareTo("2012130") >= 0) {
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
                    sb.append(",");
                }
                i++;
            }
            String result = sb.toString();
            System.out.println(result);

            List<PrizeDetail> detailList = new ArrayList<>();
            int tnum = 1;
            if (document1.getElementsByClass("kj_tablelist02").size() == 4) {
                tnum = 2;
            }
            Elements trs = document1.getElementsByClass("kj_tablelist02").get(tnum).getElementsByTag("tr");

            int j = 0;
            for (Element tr : trs) {
                if (tr.text().contains("一等奖") && !tr.text().contains("baoshi")) {
                    PrizeDetail prize1 = new PrizeDetail();
                    Elements tds1 = tr.getElementsByTag("td");
                    prize1.setName("1");
                    if (tds1.get(2).text().equals("--") || tds1.get(3).text().equals("--")) {
                        prize1.setAmount(BigDecimal.valueOf(0));
                        prize1.setNum(0);
                    } else {
                        prize1.setAmount(BigDecimal.valueOf(Double.parseDouble(tds1.get(3).text().replace(",", ""))));
                        prize1.setNum(Integer.parseInt(tds1.get(2).text()));
                    }
                    detailList.add(prize1);
                } else if (tr.text().contains("二等奖") && !tr.text().contains("baoshi")) {
                    PrizeDetail prize2 = new PrizeDetail();
                    Elements tds2 = tr.getElementsByTag("td");
                    prize2.setName("2");
                    if (tds2.get(2).text().equals("--") || tds2.get(3).text().equals("--")) {
                        prize2.setAmount(BigDecimal.valueOf(0));
                        prize2.setNum(0);
                    } else {
                        prize2.setAmount(BigDecimal.valueOf(Double.parseDouble(tds2.get(3).text().replace(",", ""))));
                        prize2.setNum(Integer.parseInt(tds2.get(2).text()));
                    }
                    detailList.add(prize2);
                } else if (tr.text().contains("三等奖") && !tr.text().contains("baoshi")) {
                    PrizeDetail prize3 = new PrizeDetail();
                    Elements tds3 = tr.getElementsByTag("td");
                    prize3.setName("3");
                    if (tds3.get(2).text().equals("--") || tds3.get(3).text().equals("--")) {
                        prize3.setAmount(BigDecimal.valueOf(0));
                        prize3.setNum(0);
                    } else {
                        prize3.setAmount(BigDecimal.valueOf(Double.parseDouble(tds3.get(3).text().replace(",", ""))));
                        prize3.setNum(Integer.parseInt(tds3.get(2).text()));
                    }
                    detailList.add(prize3);
                } else if (tr.text().contains("四等奖") && !tr.text().contains("baoshi")) {
                    PrizeDetail prize4 = new PrizeDetail();
                    Elements tds4 = tr.getElementsByTag("td");
                    prize4.setName("4");
                    if (tds4.get(2).text().equals("--") || tds4.get(3).text().equals("--")) {
                        prize4.setAmount(BigDecimal.valueOf(0));
                        prize4.setNum(0);
                    } else {
                        prize4.setAmount(BigDecimal.valueOf(Double.parseDouble(tds4.get(3).text().replace(",", ""))));
                        prize4.setNum(Integer.parseInt(tds4.get(2).text()));
                    }
                    detailList.add(prize4);
                } else if (tr.text().contains("五等奖") && !tr.text().contains("baoshi")) {
                    PrizeDetail prize5 = new PrizeDetail();
                    Elements tds5 = tr.getElementsByTag("td");
                    prize5.setName("5");
                    if (tds5.get(2).text().equals("--") || tds5.get(3).text().equals("--")) {
                        prize5.setAmount(BigDecimal.valueOf(0));
                        prize5.setNum(0);
                    } else {
                        prize5.setAmount(BigDecimal.valueOf(Double.parseDouble(tds5.get(3).text().replace(",", ""))));
                        prize5.setNum(Integer.parseInt(tds5.get(2).text()));
                    }
                    detailList.add(prize5);
                } else if (tr.text().contains("六等奖") && !tr.text().contains("baoshi")) {
                    PrizeDetail prize6 = new PrizeDetail();
                    Elements tds6 = tr.getElementsByTag("td");
                    prize6.setName("6");
                    if (tds6.get(2).text().equals("--") || tds6.get(3).text().equals("--")) {
                        prize6.setAmount(BigDecimal.valueOf(0));
                        prize6.setNum(0);
                    } else {
                        prize6.setAmount(BigDecimal.valueOf(Double.parseDouble(tds6.get(3).text().replace(",", ""))));
                        prize6.setNum(Integer.parseInt(tds6.get(2).text()));
                    }
                    detailList.add(prize6);
                } else if (tr.text().contains("追加") && j == 0) {
                    PrizeDetail prize7 = new PrizeDetail();
                    Elements tds7 = tr.getElementsByTag("td");
                    prize7.setName("7");
                    if (tds7.get(2).text().equals("--") || tds7.get(1).text().equals("--")) {
                        prize7.setAmount(BigDecimal.valueOf(0));
                        prize7.setNum(0);
                    } else {
                        prize7.setAmount(BigDecimal.valueOf(Double.parseDouble(tds7.get(2).text().replace(",", ""))));
                        prize7.setNum(Integer.parseInt(tds7.get(1).text()));
                    }
                    detailList.add(prize7);
                    j++;
                } else if (tr.text().contains("追加") && j == 1) {
                    PrizeDetail prize8 = new PrizeDetail();
                    Elements tds8 = tr.getElementsByTag("td");
                    prize8.setName("8");
                    if (tds8.get(2).text().equals("--") || tds8.get(1).text().equals("--")) {
                        prize8.setAmount(BigDecimal.valueOf(0));
                        prize8.setNum(0);
                    } else {
                        prize8.setAmount(BigDecimal.valueOf(Double.parseDouble(tds8.get(2).text().replace(",", ""))));
                        prize8.setNum(Integer.parseInt(tds8.get(1).text()));
                    }
                    detailList.add(prize8);
                    j++;
                } else if (tr.text().contains("追加") && j == 2) {
                    PrizeDetail prize9 = new PrizeDetail();
                    Elements tds9 = tr.getElementsByTag("td");
                    prize9.setName("9");
                    if (tds9.get(2).text().equals("--") || tds9.get(1).text().equals("--")) {
                        prize9.setAmount(BigDecimal.valueOf(0));
                        prize9.setNum(0);
                    } else {
                        prize9.setAmount(BigDecimal.valueOf(Double.parseDouble(tds9.get(2).text().replace(",", ""))));
                        prize9.setNum(Integer.parseInt(tds9.get(1).text()));
                    }
                    detailList.add(prize9);
                    j++;
                } else if (tr.text().contains("追加") && j == 3) {
                    PrizeDetail prize10 = new PrizeDetail();
                    Elements tds10 = tr.getElementsByTag("td");
                    prize10.setName("10");
                    if (tds10.get(2).text().equals("--") || tds10.get(1).text().equals("--")) {
                        prize10.setAmount(BigDecimal.valueOf(0));
                        prize10.setNum(0);
                    } else {
                        prize10.setAmount(BigDecimal.valueOf(Double.parseDouble(tds10.get(2).text().replace(",", ""))));
                        prize10.setNum(Integer.parseInt(tds10.get(1).text()));
                    }
                    detailList.add(prize10);
                    j++;
                } else if (tr.text().contains("追加") && j == 4) {
                    PrizeDetail prize11 = new PrizeDetail();
                    Elements tds11 = tr.getElementsByTag("td");
                    prize11.setName("11");
                    if (tds11.get(2).text().equals("--") || tds11.get(1).text().equals("--")) {
                        prize11.setAmount(BigDecimal.valueOf(0));
                        prize11.setNum(0);
                    } else {
                        prize11.setAmount(BigDecimal.valueOf(Double.parseDouble(tds11.get(2).text().replace(",", ""))));
                        prize11.setNum(Integer.parseInt(tds11.get(1).text()));
                    }
                    detailList.add(prize11);
                    j++;
                }
            }

            String prizeDetail = JSONObject.toJSONString(detailList);
            System.out.println(prizeDetail);

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
            statement.setString(8, prizeDetail);
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


        String insert = "insert into lottery_term(type, term, startTime, endTime, isCurrent, status, result, prize_detail) values (?, ?, ?, ?, ?, ?, ?, ?)";
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
            if (termNo.compareTo("2017283") >= 0) {
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
                    sb.append(",");
                }
                i++;
            }
            String result = sb.toString();
            System.out.println(result);

            List<PrizeDetail> detailList = new ArrayList<>();
            Elements trs = document1.getElementsByClass("kj_tablelist02").get(1).getElementsByTag("tr");

            if (termNo.equals("2017195")) {
                System.out.println("dao");
            }

            PrizeDetail prize1 = new PrizeDetail();
            Elements tds1 = trs.get(2).getElementsByTag("td");
            prize1.setAmount(BigDecimal.valueOf(Double.parseDouble(tds1.get(2).text().replace(",", ""))));
            prize1.setName(String.valueOf(1));
            prize1.setNum(Integer.parseInt(tds1.get(1).text()));
            detailList.add(prize1);

            Elements tds2 = trs.get(3).getElementsByTag("td");
            if (tds2.get(0).text().equals("组三") || tds2.get(0).text().equals("组六")) {
                PrizeDetail prize2 = new PrizeDetail();
                if (tds2.get(0).text().equals("组三")) {
                    prize2.setName(String.valueOf(2));
                } else {
                    prize2.setName(String.valueOf(3));
                }
                if (tds2.get(2).text().contains("--") || tds2.get(1).text().contains("--")) {
                    prize2.setAmount(BigDecimal.ZERO);
                    prize2.setNum(0);
                } else {
                    prize2.setAmount(BigDecimal.valueOf(Double.parseDouble(tds2.get(2).text().replace(",", ""))));
                    prize2.setNum(Integer.parseInt(tds2.get(1).text()));
                }
                detailList.add(prize2);
            }

            String prizeDetail = JSONObject.toJSONString(detailList);
            System.out.println(prizeDetail);

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
            statement.setString(8, prizeDetail);
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


        String insert = "insert into lottery_term(type, term, startTime, endTime, isCurrent, status, result, prize_detail) values (?, ?, ?, ?, ?, ?, ?, ?)";
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
            if (termNo.compareTo("2006121") >= 0) {
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
                    sb.append(",");
                }
                i++;
            }
            String result = sb.toString();
            System.out.println(result);

            List<PrizeDetail> detailList = new ArrayList<>();
            Elements trs = document1.getElementsByClass("kj_tablelist02").get(1).getElementsByTag("tr");

            PrizeDetail prize1 = new PrizeDetail();
            Elements tds1 = trs.get(2).getElementsByTag("td");
            prize1.setAmount(BigDecimal.valueOf(Double.parseDouble(tds1.get(2).text().replace(",", ""))));
            prize1.setName(String.valueOf(1));
            prize1.setNum(Integer.parseInt(tds1.get(1).text()));
            detailList.add(prize1);


            Elements tds2 = trs.get(3).getElementsByTag("td");
            if (tds2.get(0).text().equals("排列三组三") || tds2.get(0).text().equals("排列三组六")) {
                PrizeDetail prize2 = new PrizeDetail();
                if (tds2.get(0).text().equals("排列三组三")) {
                    prize2.setName(String.valueOf(2));
                } else {
                    prize2.setName(String.valueOf(3));
                }
                if (tds2.get(2).text().contains("--") || tds2.get(1).text().contains("--")) {
                    prize2.setAmount(BigDecimal.ZERO);
                    prize2.setNum(0);
                } else {
                    prize2.setAmount(BigDecimal.valueOf(Double.parseDouble(tds2.get(2).text().replace(",", ""))));
                    prize2.setNum(Integer.parseInt(tds2.get(1).text()));
                }
                detailList.add(prize2);
            }
            String prizeDetail = JSONObject.toJSONString(detailList);
            System.out.println(prizeDetail);

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
            statement.setString(8, prizeDetail);
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

    public static void plw() throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/lottery", "root", "root");


        String insert = "insert into lottery_term(type, term, startTime, endTime, isCurrent, status, result, prize_detail) values (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(insert);

        String url = "http://kaijiang.500.com/plw.shtml";
        Document document = Jsoup.connect(url).get();
        Elements iSelectList = document.getElementsByClass("iSelectList");
        Elements as = iSelectList.select("a");
        int m = 0;
        for (Element e : as) {
            String href = e.attr("href");
            Document document1 = null;
            try {
                document1 = Jsoup.connect(href).get();
            } catch (Exception e1) {
                continue;
            }
            Elements cfont2 = document1.getElementsByClass("cfont2");
            String termNo = "20"+cfont2.text();
            System.out.println(termNo);
            if (termNo.compareTo("2017283") >= 0) {
                continue;
            }
            Elements span_right = document1.getElementsByClass("span_right");
            String endTime = span_right.text();
            String date = endTime;
            if (endTime.contains("年")) {
                String year = endTime.split("兑奖截止日期")[0].split("年")[0].split("：")[1].trim();
                String month = endTime.split("兑奖截止日期")[0].split("年")[1].split("月")[0].trim();
                String day = endTime.split("兑奖截止日期")[0].split("年")[1].split("月")[1].split("日")[0].trim();
                date = year + "-"+ month + "-"+day;
            } else {
                date = date.split("日期：")[1].split("兑奖")[0].trim();
            }
            System.out.println(date);
            Elements iSelectList1 = document1.getElementsByClass("ball_box01");
            Elements lis = iSelectList1.select("li");
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (Element li : lis) {
                sb.append(li.text());
                if (i == 4) {

                } else {
                    sb.append(",");
                }
                i++;
            }
            String result = sb.toString();
            System.out.println(result);

            List<PrizeDetail> detailList = new ArrayList<>();
            Elements trs = document1.getElementsByClass("kj_tablelist02").get(1).getElementsByTag("tr");

            PrizeDetail prize1 = new PrizeDetail();
            Elements tds1 = trs.get(2).getElementsByTag("td");
            prize1.setName(String.valueOf(1));
            if (tds1.get(1).text().contains("--") || tds1.get(2).text().contains("--")) {
                prize1.setAmount(BigDecimal.ZERO);
                prize1.setNum(0);
            } else {
                prize1.setAmount(BigDecimal.valueOf(Double.parseDouble(tds1.get(2).text().replace(",", ""))));
                prize1.setNum(Integer.parseInt(tds1.get(1).text()));
            }
            detailList.add(prize1);

            String prizeDetail = JSONObject.toJSONString(detailList);
            System.out.println(prizeDetail);


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);
            java.sql.Date sd = new java.sql.Date(d.getTime());
            statement.setString(1, "4");
            statement.setString(2, termNo);
            statement.setDate(3, sd);
            statement.setDate(4, sd);
            statement.setByte(5, (byte)1);
            statement.setString(6, "2");
            statement.setString(7, result);
            statement.setString(8, prizeDetail);
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
        //pls();
        //plw();
        sd();
    }

}
