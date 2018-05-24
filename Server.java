package chat;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	private ServerSocket server;
	private List<PrintWriter> allOut;
	
	public Server()throws Exception {
		server = new ServerSocket(8088);
		
		allOut = new ArrayList<PrintWriter>();
	}
	private synchronized void addOut(PrintWriter out) {
		allOut.add(out);
	}
	
	private synchronized void removeOut(PrintWriter out) {
		allOut.remove(out);
	}
	
	private synchronized void sendMessage(String message) {
		for(PrintWriter out : allOut) {
			out.println(message);
		}
	}
	
	
	public void start() {
		try {
			while(true) {
			System.out.println("�ȴ��ͻ�������...");
			Socket socket = server.accept();
			System.out.println("һ���ͻ������ӳɹ���");
			
			ClientHandler handler = new ClientHandler(socket);
			Thread t = new Thread(handler);
			t.start();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			Server server = new Server();
			server.start();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("����������ʧ�ܣ�");
		}
	}
	
	class ClientHandler implements Runnable{
		private Socket socket;
		//private String host;//�ͻ�����Ϣ
		private String nickName;
		
		public ClientHandler(Socket socket) {
			this.socket = socket;
			InetAddress address = socket.getInetAddress();
			
			//host = address.getHostAddress();
		}
		
		public void run() {
			PrintWriter pw = null;
            try {
            	
            	//������
            	InputStream in = socket.getInputStream();
       			InputStreamReader isr = new InputStreamReader(in,"UTF-8");
       			BufferedReader br = new BufferedReader(isr);
       			
       			nickName = br.readLine();
       			System.out.println(nickName+"�����ˣ�");
       			//�����
       			OutputStream out = socket.getOutputStream();
       			OutputStreamWriter osw = new OutputStreamWriter(out,"UTF-8");
       			pw = new PrintWriter(osw,true);
       			
       			addOut(pw);
       			
       			
       			String message = null;
       			while((message = br.readLine())!=null) {	
       			  //System.out.println(host+"˵:"+message);
       		      //pw.println(host+"˵��"+message);
       				sendMessage(nickName+"˵��"+message);
       			}
            }catch(Exception e) {
            	
            }finally {
            	removeOut(pw);
            	
            	System.out.println(nickName+"�����ˣ�");
            	try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
		}
	}

}
