package com.example.chair_book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.example.chair_book.R;


public class MainActivity extends Activity {
	private EditText nameText;
	private EditText passwordText;
	private EditText chairIdText;
	private Button loginButton;
	private Button bookButton;
	private Button holdButton;
	private TextView messageText;
	
	private myhttp client;
	private String name;
	private String password;
	private String chairId;
	
	private String home = "http://ischoolgu.xmu.edu.cn/";
	private String login_url = "http://ischoolgu.xmu.edu.cn/Default.aspx";
	private String load_get2_url = "http://ischoolgu.xmu.edu.cn/admin_bookChair.aspx";
	private String book_url = "http://ischoolgu.xmu.edu.cn/admin_bookChair.aspx";
	
	private String html = new String();
	private Map<String, String> login_param = new HashMap<String, String>();
	private Map<String, String> book_param = new HashMap<String, String>();
	private List<Map<String, String>> chairMsg;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameText=(EditText) findViewById(R.id.nameText);
        passwordText=(EditText) findViewById(R.id.passwordText);
        chairIdText=(EditText) findViewById(R.id.chairIdText);
        messageText=(TextView) findViewById(R.id.messageText);
        loginButton=(Button) findViewById(R.id.loginButton);
        bookButton=(Button) findViewById(R.id.bookButton);
        holdButton=(Button) findViewById(R.id.holdButton);
        
        loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name = nameText.getText().toString();
				password = passwordText.getText().toString();
				new Thread(loginEvent).start();			
			}
		});
        bookButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				chairId = chairIdText.getText().toString();
				if(chairId.isEmpty())
				{
					messageText.append("请先输入讲座ID\r\n");				
				}
				else
					new Thread(bookEvent).start();	
			}
		});
        
        holdButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//chairId = chairIdText.getText().toString();
				new Thread(holdEvent).start();	
			}
		});
       
        
    }
    
    Runnable holdEvent=new Runnable() {
		
		@Override
		public void run() {
			Message msg9 = new Message();
			Bundle data9 = new Bundle();
			data9.putString("value", "可捡漏的讲座有：\r\n");
            msg9.setData(data9);
            handler.sendMessage(msg9);
            
			for (int i = 0; i < chairMsg.size(); i++) {
				Map<String, String> per = chairMsg.get(i);
				if (per.get("state").compareTo("2") == 0) {
					Message msg10 = new Message();
					Bundle data10 = new Bundle();
					data10.putString("value", "讲座:"+per.get("chair_name") + "\r\n");
		            msg10.setData(data10);
		            handler.sendMessage(msg10);
		            
		            Message msg_time = new Message();
					Bundle data_time = new Bundle();
		            data_time.putString("value", "讲座时间:"+per.get("chair_listen_time") + "\r\n");
		            msg_time.setData(data_time);
		            handler.sendMessage(msg_time);
		          		            
		            Message msg0 = new Message();
					Bundle data0 = new Bundle();
		            data0.putString("value", "讲座ID:" + i + "\r\n\r\n");
		            msg0.setData(data0);
		            handler.sendMessage(msg0);
		            
				}
			}			
			
			
			
			
		}
	};
    
	
	
	
	
	
    
    Runnable loginEvent=new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			if(name.isEmpty() || password.isEmpty())
			{   
				Message msg1 = new Message();
				Bundle data1 = new Bundle();
                data1.putString("value", "请输入账号和密码\r\n");
                msg1.setData(data1);
                handler.sendMessage(msg1);
				while(true);						
			}
			client = new myhttp(name, password);
			/**************** load home *************************/
			
			Message msg2 = new Message();
			Bundle data2 = new Bundle();
			data2.putString("value", "正在载入首页...\r\n");
            msg2.setData(data2);
            handler.sendMessage(msg2);

			try {
				html = client.doget(home, utils.headers_get_home, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Message msg3 = new Message();
				Bundle data3 = new Bundle();
				data3.putString("value", "载入首页失败，请检查网络\r\n");
	            msg3.setData(data3);
	            handler.sendMessage(msg3);
				while (true);
					
			}
			
			/******************** login **************************/
			Map<String, String> viewstate = utils.getviewstate(html);
			login_param.put("__EVENTTARGET", "");
			login_param.put("__EVENTARGUMENT", "");
			login_param.put("__VIEWSTATE", viewstate.get("VIEWSTATE"));
			login_param.put("__VIEWSTATEGENERATOR", viewstate.get("VIEWSTATEGENERATOR"));
			login_param.put("__EVENTVALIDATION", viewstate.get("EVENTVALIDATION"));
			login_param.put("userName", client.getUsername());
			login_param.put("passWord", client.getPassword());
			login_param.put("userType", "1");
			login_param.put("sumbit", "登　陆");

			Message msg4 = new Message();
			Bundle data4 = new Bundle();
			data4.putString("value", "正在登陆账户...\r\n");
            msg4.setData(data4);
            handler.sendMessage(msg4);
   
			try {
				html = client.dopost(login_url, utils.headers_post_general, login_param);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Message msg5 = new Message();
				Bundle data5 = new Bundle();
				data5.putString("value", "登陆错误，请检查网络\r\n");
	            msg5.setData(data5);
	            handler.sendMessage(msg5);
				while (true);
					
			}
			if(utils.check_login(html)!=-1)
				{
				Message msg6 = new Message();
				Bundle data6 = new Bundle();
				data6.putString("value", "登陆失败，请检查用户名和密码\r\n");
	            msg6.setData(data6);
	            handler.sendMessage(msg6);
				while (true);
				}
			
			
			Message msg7 = new Message();
			Bundle data7 = new Bundle();
			data7.putString("value", "登陆成功！\r\n\r\n");
            msg7.setData(data7);
            handler.sendMessage(msg7);
			/*************************************
			 * load chair page
			 *****************/
			try {
				html = client.doget(load_get2_url, utils.headers_get_general, null);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Message msg8 = new Message();
				Bundle data8 = new Bundle();
				data8.putString("value", "获取讲座信息失败,请检查网络\r\n");
	            msg8.setData(data8);
	            handler.sendMessage(msg8);
				while (true);
			}
	
			chairMsg = utils.getchairmsg(html);
			
			Message msg9 = new Message();
			Bundle data9 = new Bundle();
			data9.putString("value", "可预约的讲座有：\r\n");
            msg9.setData(data9);
            handler.sendMessage(msg9);
            
			for (int i = 0; i < chairMsg.size(); i++) {
				Map<String, String> per = chairMsg.get(i);
				if (per.get("state").compareTo("1") == 0) {
					Message msg10 = new Message();
					Bundle data10 = new Bundle();
					data10.putString("value", "讲座:"+per.get("chair_name") + "\r\n");
		            msg10.setData(data10);
		            handler.sendMessage(msg10);
		           
		            Message msg11 = new Message();
					Bundle data11 = new Bundle();
		            data11.putString("value", "预约时间:"+per.get("chair_booktime") + "\r\n");
		            msg11.setData(data11);
		            handler.sendMessage(msg11);
		            
		            Message msg_time = new Message();
					Bundle data_time = new Bundle();
		            data_time.putString("value", "讲座时间:"+per.get("chair_listen_time") + "\r\n");
		            msg_time.setData(data_time);
		            handler.sendMessage(msg_time);
		            
		            Message msg0 = new Message();
					Bundle data0 = new Bundle();
		            data0.putString("value", "讲座ID:" + i + "\r\n\r\n");
		            msg0.setData(data0);
		            handler.sendMessage(msg0);
		            
				}
			}					
			
		}
	};
		
	
	Runnable bookEvent=new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			/*************************************
			 * load chair page
			 *****************/
			Message msg12 = new Message();
			Bundle data12 = new Bundle();
            data12.putString("value", "刷新中,请等待\r\n");
            msg12.setData(data12);
            handler.sendMessage(msg12);
			while (utils.getbookstate(html, chairMsg.get(Integer.valueOf(chairId)).get("button")) == -1) {
				try {
					html = client.doget(load_get2_url, utils.headers_get_general, null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					
				}
				
			}

			/*********************
			 * book chair
			 ***************************/
			Map<String, String> viewstate2 = utils.getviewstate(html);
			book_param.put("__VIEWSTATE", viewstate2.get("VIEWSTATE"));
			book_param.put("__VIEWSTATEGENERATOR", viewstate2.get("VIEWSTATEGENERATOR"));
			book_param.put("__EVENTVALIDATION", viewstate2.get("EVENTVALIDATION"));

			String btName = utils.getbuttonname(html, chairMsg.get(Integer.valueOf(chairId)).get("button"));
			String Id_Name=chairMsg.get(Integer.valueOf(chairId)).get("id_name");
			String IdReal = chairMsg.get(Integer.valueOf(chairId)).get("id_value");
			book_param.put(Id_Name, IdReal);
			book_param.put(btName, "预约该讲座");

			try {
				html = client.dopost(book_url, utils.headers_post_general, book_param);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Message msg8 = new Message();
				Bundle data8 = new Bundle();
				data8.putString("value", "请求失败,请检查网络\r\n");
	            msg8.setData(data8);
	            handler.sendMessage(msg8);
				while (true);
			}
			Message msg14 = new Message();
			Bundle data14 = new Bundle();
            data14.putString("value", "已执行，请等待结果\r\n");
            msg14.setData(data14);
            handler.sendMessage(msg14);
			/*************************check***************/
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Message msg8 = new Message();
				Bundle data8 = new Bundle();
				data8.putString("value", "休眠失败\r\n");
	            msg8.setData(data8);
	            handler.sendMessage(msg8);
				while (true);
			}
			
			try {
				html = client.doget(load_get2_url, utils.headers_get_general, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Message msg8 = new Message();
				Bundle data8 = new Bundle();
				data8.putString("value", "获取结果,请检查网络\r\n");
	            msg8.setData(data8);
	            handler.sendMessage(msg8);
				while (true);
				
			}
			int check_int=utils.check_book(html, chairMsg.get(Integer.valueOf(chairId)).get("button"));
			if(check_int!=-1)
			{
				Message msg13 = new Message();
				Bundle data13 = new Bundle();
	            data13.putString("value", "预约成功\r\n");
	            msg13.setData(data13);
	            handler.sendMessage(msg13);
				
			}
			else
			{
				Message msg13 = new Message();
				Bundle data13 = new Bundle();
	            data13.putString("value", "预约失败\r\n");
	            msg13.setData(data13);
	            handler.sendMessage(msg13);
				
			}
			
		}
	};
    
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            
            messageText.append(val);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
