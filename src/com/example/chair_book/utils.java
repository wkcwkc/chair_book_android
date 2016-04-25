package com.example.chair_book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

public class utils {
	public static Header[] headers_get_home = {new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko"), 
			new BasicHeader("Accept-Language", "zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3"), 
			new BasicHeader("Accept", "text/html, application/xhtml+xml, */*"),
			new BasicHeader("Host", "ischoolgu.xmu.edu.cn"),
			new BasicHeader("DNT", "1"),
			new BasicHeader("Connection", "Keep-Alive"),
			new BasicHeader("Accept-Encoding", "gzip, deflate")
			};
	
	
	public static Header[] headers_post_general = {new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko"), 
			new BasicHeader("Accept-Language", "zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3"), 
			new BasicHeader("Accept", "text/html, application/xhtml+xml, */*"),
			new BasicHeader("Referer", "http://ischoolgu.xmu.edu.cn/Default.aspx"),
			new BasicHeader("Host", "ischoolgu.xmu.edu.cn"),
			new BasicHeader("DNT", "1"),
			new BasicHeader("Pragma", "no-cache"),
			new BasicHeader("Connection", "Keep-Alive"),
			new BasicHeader("Content-Type", "application/x-www-form-urlencoded"),
			new BasicHeader("Accept-Encoding", "gzip, deflate")			
			};
	public static Header[] headers_get_general = {new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko"), 
			new BasicHeader("Accept-Language", "zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3"), 
			new BasicHeader("Accept", "text/html, application/xhtml+xml, */*"),
			new BasicHeader("Referer", "http://ischoolgu.xmu.edu.cn/admin_left.aspx"),
			new BasicHeader("Host", "ischoolgu.xmu.edu.cn"),
			new BasicHeader("DNT", "1"),
			new BasicHeader("Connection", "Keep-Alive"),
			new BasicHeader("Accept-Encoding", "gzip, deflate")
			};
	
	public static Map<String,String> getviewstate(String resp)
	{
		String VIEWSTATE,VIEWSTATEGENERATOR,EVENTVALIDATION;
		int a=resp.indexOf("id=\"__VIEWSTATE\" value=\"")+24;
		int b=resp.indexOf("id=\"__VIEWSTATEGENERATOR\" value=\"")+33;
		int c=resp.indexOf("id=\"__EVENTVALIDATION\" value=\"")+30;
		
		
		int a1=resp.indexOf("\"", a);
		int b1=resp.indexOf("\"", b);
		int c1=resp.indexOf("\"", c);
		
		VIEWSTATE=resp.substring(a, a1);
		VIEWSTATEGENERATOR=resp.substring(b,b1);
		EVENTVALIDATION=resp.substring(c,c1);
		Map<String,String> viewstate=new HashMap<String,String>();
		viewstate.put("VIEWSTATE", VIEWSTATE);
		viewstate.put("VIEWSTATEGENERATOR", VIEWSTATEGENERATOR);
		viewstate.put("EVENTVALIDATION",EVENTVALIDATION);
		return viewstate;
	}
	public static int getbookstate(String resp,String bt)
	{
		int button=Integer.valueOf(bt);
		int a=resp.indexOf("value=\"预约该讲座",button);		
		
		if(a>(button+100))
			return -1;
		else
			return a;
	}
	
	public static int check_book(String resp,String bt)
	{
		int button=Integer.valueOf(bt);
		int a=resp.indexOf("value=\"取消预约",button);		
		
		if(a>(button+100))
			return -1;
		else
			return a;
	}
	public static int check_login(String resp)
	{
		int a=resp.indexOf("'>alert('用户名或密码错误!");		
	
		return a;
	}
	
	public static String getbuttonname(String resp,String bt)
	{
		int a_name1=resp.indexOf("name=",Integer.valueOf(bt))+6;
		int a_name2=resp.indexOf("\"", a_name1);		
		return resp.substring(a_name1,a_name2);
	}
	
	public static List<Map<String,String>> getchairmsg(String resp)
	{
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		int a_date=resp.indexOf("讲座日期");
		while(a_date!=-1)
		{
			Map<String,String> per=new HashMap<String,String>();
			
			
			int a_name1=resp.indexOf("name=", a_date)+6;
			int a_name2=resp.indexOf("\"", a_name1);		
			per.put("id_name",resp.substring(a_name1,a_name2));
			
			int a_value1=resp.indexOf("value=", a_date)+7;
			int a_value2=resp.indexOf("\"", a_value1);		
			per.put("id_value",resp.substring(a_value1,a_value2));
			
			int a_chair1=resp.indexOf("讲座名称",a_date)+28;
			int a_chair2=resp.indexOf("<",a_chair1);
			per.put("chair_name",resp.substring(a_chair1,a_chair2));
			
			int a_time1=resp.indexOf("预约起始时间",a_date)+30;
			int a_time2=resp.indexOf("<",a_time1);
			per.put("chair_booktime",resp.substring(a_time1,a_time2));
			
			int a_listen_time1=resp.indexOf("讲座时间",a_date)+28;
			int a_listen_time2=resp.indexOf("<",a_listen_time1);
			per.put("chair_listen_time",resp.substring(a_listen_time1,a_listen_time2));
			
			int a_button=resp.indexOf("colspan",a_date);
			String str=resp.substring(a_button+12,a_button+16);
			int sss=1;
			if(str.compareTo("预约时间")==0)
				sss=1;
			if(str.compareTo("预约总人")==0)
				sss=2;
			String str2=resp.substring(a_button+53,a_button+55);
			if(str2.compareTo("取消")==0)
				sss=3;
			per.put("state", Integer.toString(sss));
			
			per.put("button", Integer.toString(a_button));
			
			list.add(per);
			a_date=resp.indexOf("讲座日期",a_button);
		}
		return list;
	}

}
