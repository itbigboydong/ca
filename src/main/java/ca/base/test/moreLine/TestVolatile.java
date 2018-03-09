package ca.base.test.moreLine;

import java.util.ArrayList;
import java.util.List;

public class TestVolatile {
	/*
	 * 这里的volatile修饰符的作用是实现了li可以在线程t1,t2之间可见，
	 * 如果这里去掉，那么线程t2不会中断，因为它看不见集合li的不断增加
	 */
	private volatile static List li = new ArrayList(); 
	
	public void add(){
		li.add("abc");
		System.out.println("li的集合中添加了一个元素");
	}
	
	public int getSize(){
		return li.size();
	}

   public static void main(String[] args) {
	   final TestVolatile v = new TestVolatile();
	   
		Thread t1 = new Thread(new Runnable() {
			public void run() {
			 try {
				for(int i=0;i<10;i++){
				  v.add();
				  System.out.println(Thread.currentThread().getName()+"添加了一个元素");
				  Thread.sleep(500);
				  }
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		},"t1");
		
		Thread t2 = new Thread(new Runnable() {
			
			public void run() {
	             while(true){
	            	 if(v.getSize()==5){
	            		 System.out.println(Thread.currentThread().getName()+"v.getSize==5了，要中断");
	            		throw new RuntimeException();
	            	 }
	             }			
			}
		},"t2");
		
		t1.start();
		t2.start();
   }
}
