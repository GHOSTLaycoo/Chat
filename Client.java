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
		System.out.println("�������ӷ����...");
		socket = new Socket("localhost",8088);//�����
		System.out.println("�������˽������ӣ�");
	}
	
	public void start() {
		try {
			Scanner scan = new Scanner(System.in);
			String nickName = null;
			while(true) {
			System.out.println("�������û���");
			nickName = scan.nextLine();
			if(nickName.length()>0) {
				break;
			}
			System.out.println("��������!");
			}
			System.out.println("��ӭ��"+nickName+"��ʼ���죡");
			//�����
			OutputStream out = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(out,"UTF-8");
			PrintWriter pw = new PrintWriter(osw,true);
			
			pw.println(nickName);
			
			//�����߳�
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
			System.out.println("�ͻ�������ʧ��!");
		}
	}
	
	class ServerHandler implements Runnable{
		public void run() {
			try {
				//������
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
