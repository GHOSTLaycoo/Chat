package chat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	private Socket socket;
	
	public Client()throws Exception{
		System.out.println("正在连接服务端...");
		socket = new Socket("localhost",8088);//服务端
		System.out.println("已与服务端建立连接！");
	}
	
	public void start() {
		try {
			Scanner scan = new Scanner(System.in);
			String nickName = null;
			while(true) {
			System.out.println("请输入用户名");
			nickName = scan.nextLine();
			if(nickName.length()>0) {
				break;
			}
			System.out.println("输入有误!");
			}
			System.out.println("欢迎你"+nickName+"开始聊天！");
			//输出流
			OutputStream out = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(out,"UTF-8");
			PrintWriter pw = new PrintWriter(osw,true);
			
			pw.println(nickName);
			
			//启动线程
			ServerHandler handler = new ServerHandler();
			Thread t =new Thread(handler);
			t.start();
			
			while(true) {
				pw.println(scan.nextLine());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			Client client = new Client();
			client.start();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("客户端启动失败!");
		}
	}
	
	class ServerHandler implements Runnable{
		public void run() {
			try {
				//输入流
				InputStream in = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(in,"UTF-8");
				BufferedReader br = new BufferedReader(isr);
				
				String message = null;
				while((message = br.readLine())!=null) {
					System.out.println(message);
				}
			}catch(Exception e) {
				
			}
		}
	}

}
