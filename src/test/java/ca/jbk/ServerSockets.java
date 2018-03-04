package ca.jbk;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerSockets {
	private static  int step =0;
	public static void main(String[] args) throws Exception {
		//创建线程池，每当进来一个连接，就分配一个线程
		ExecutorService s = Executors.newCachedThreadPool();
		ServerSocket server = new ServerSocket(1001);
		System.out.println("服务端启动了");
		while(true){
			final Socket socket = server.accept();  //获取一个套接字
			System.out.println(++step+" 个客户的连接了");
			//业务处理
			s.execute(new Runnable() {
				
				public void run() {
					try {
						handle(socket);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
		}
		
	}
	private static void handle(Socket socket) throws IOException {
     byte[] bytes = new   byte[1024];	
     InputStream in = socket.getInputStream();
     while(true){
    	 //读取数据
    	 int read  = in.read(bytes);
    	 if(read !=-1){
    		 System.out.print(new String(bytes,0,read));
    	 }else{
    		 break;
    	 }
     }
     socket.close();
	}
}
