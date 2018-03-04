package ca.jbk;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class IONServer {
	
	private Selector selector;  //通道管理器
	
	/*
	 * 获取一个serverSocker通道，并做一些初始化工作
	 * 
	 * @param   port  端口号
	 */
	public void initServer(int port) throws Exception{
		//获取一个ServerSocket通道
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		
		serverChannel.configureBlocking(false);//设置为非阻塞
		
		//将通道对应的serverSocket绑定到port端口上
		serverChannel.socket().bind(new InetSocketAddress(port));
		
		//获取一个通道管理器
		this.selector = Selector.open();
		
		//将通道管理器和通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件，注册该事件后，
		//当该事件到达，selector.select()会返回，如果事件没有到达，selector.select()会一直阻塞
		serverChannel.register(selector ,SelectionKey.OP_ACCEPT);//这一步就是在通道上注册通道管理器
		
		
		
	}
	
	public void listen()throws Exception{
		
		System.out.println("服务端启动成功");
		//轮询访问selector
		while(true){
			//当注册事件到达时返回true,否则阻塞
			selector.select();
			//获取选中项的迭代器，选中项为注册事件
			Iterator<?> ite = this.selector.selectedKeys().iterator();
			  while(ite.hasNext()){
				  SelectionKey key = (SelectionKey) ite.next();
				  //删除已选中的key,防止重复
				  ite.remove();
				  handle(key);
			  }
		}
	}
  /*
   * 处理请求
   */
	private void handle(SelectionKey key) throws Exception {
    		//判断这个key是新来的客户端请求连接事件  还是已经进来的客人要点餐
		if(key.isAcceptable()){
			 handleAccept(key);
			 //isReadable（）就是有客人要点餐了，会执行handleRead()
		}else if(key.isReadable()){
			handleRead(key);
		}
	}

/*
 * 处理连接请求
 */
	private void handleAccept(SelectionKey key) throws Exception{
		 ServerSocketChannel server = (ServerSocketChannel) key.channel();
		 //获取和客户端连接的通道
		 SocketChannel channel = server.accept();
		 //设置成非阻塞
		 channel.configureBlocking(false);
		 //在这里可以给客户端发消息
		 System.out.println("新的客户端连接");
		 //在和客户端连接成功后，为了可以接受客户端的消息，需要给通道设置读取权限
		 channel.register(this.selector, SelectionKey.OP_READ); //这里就像客人进来之后，看看菜单准备点餐，然后服务员
		                                                        //就可招呼别的客人了，要点餐的时候在通知服务员
	}

	private void handleRead(SelectionKey key) throws Exception{
		//服务器可读取消息，得到事件发生的Socket通道
		SocketChannel channel = (SocketChannel) key.channel();
		//创建读取缓存区
		ByteBuffer buffer =ByteBuffer.allocate(1024);
		int read =channel.read(buffer);
		if(read>0){
			byte[] data = buffer.array();
			String msg = new String(data).trim();
			System.out.println("服务端收到消息 "+msg);
			
			ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
			channel.write(outBuffer);  //将消息返回给客户端
		}else{
			System.out.println("客户端关闭");
			key.cancel();
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		IONServer server = new IONServer();
		server.initServer(8087);
		server.listen();
	}
}
