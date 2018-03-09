package ca.test;

public class ObjectLock {
	/*
	 * 下面的两个方法，如果一个不加synchronized互不影响，如果都加的话，就会同步，因为这个synchronized是获得的对象锁
	 */
	public synchronized void method1(){
		try {
			System.out.println(Thread.currentThread().getName());
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void method2(){
		System.out.println(Thread.currentThread().getName());
	}
	
    public static void main(String[] args) {
    	final ObjectLock lock = new ObjectLock();
    	
		Thread t1 = new Thread(new Runnable() {
			
			public void run() {
				lock.method1();
			}
		});
		
		Thread t2 = new Thread(new Runnable() {
			
			public void run() {
				lock.method2();
			}
		});
		
		t1.start();
		t2.start();
	}
}
