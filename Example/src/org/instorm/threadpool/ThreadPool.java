package org.instorm.threadpool;

import java.util.LinkedList;

public class ThreadPool{
	
	private LinkedList<Runnable> tasks;
	private int poolSize;
	private boolean isClosed = false;
	
	public int getPoolSize() {
		return poolSize;
	}

	public ThreadPool(int poolSize) {
		this.tasks = new LinkedList<Runnable>();
		if(poolSize < 0 || poolSize > 100){
			throw new IllegalStateException();
		}
		this.poolSize = poolSize;
		for(int i = 1; i <= poolSize; i++){
			WorkerThread workerThread = new WorkerThread(i);
			workerThread.start();
		}
	}
	
	public synchronized void executeTask(Runnable task){
		if(isClosed){
			throw new IllegalStateException();
		}
		if(task != null){
			tasks.add(task);
			notify();
		}
	}
	
	private synchronized Runnable getTask(){
		if(tasks.isEmpty()){
			try {
				wait();
				System.out.println("工作线程被唤醒");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(isClosed){
			return null;
		}
		return tasks.removeFirst();
	}
	
	public synchronized void closePool(){
		if(!isClosed){
			isClosed = true;
			tasks.clear();
			notifyAll();
		}
	}
	
	private class WorkerThread extends Thread{
		
		private int id;
		
		public WorkerThread(int id){
			this.id = id;
		}
		
		@Override
		public void run() {
			if(!isInterrupted()){
				Runnable task = null;
				System.out.println("工作线程 " + id + "：启动");
				while(true){
					if(isClosed){
						System.out.println("工作线程 " + id + "：停止");
						return;
					}
					task = getTask();
					if(task != null){
						System.out.println("工作线程 " + id + "：执行任务");
						task.run();
					}
				}
			}
		}
	}
}
