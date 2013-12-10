package org.instorm.threadpool;

import org.junit.Test;


public class ThreadPoolTest {
	
	@Test
	public void testThreadPool(){
		System.out.println("启动线程池");
		ThreadPool threadPool = new ThreadPool(1);
		for(int i = 0; i < 20; i++){
			threadPool.executeTask(new Runnable() {
				@Override
				public void run() {
					System.out.println("Hello World!");
				}
			});
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("关闭线程池");
		threadPool.closePool();
	}
}
