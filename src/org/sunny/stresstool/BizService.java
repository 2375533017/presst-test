package org.sunny.stresstool;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import com.busap.stresstool.util.HttpClient;


public class BizService {
	
	private static boolean isRun = false;
	private static AtomicInteger overCount = new AtomicInteger(0);
	private static AtomicInteger failCount = new AtomicInteger(0);

	public void dealBiz(final String url, String userCount,
			String totalCount, String intervalTime)  {
		isRun = true;
		initNum();
		final int userThreadNumber = Integer.parseInt(userCount);
		final int intervalTimeNumber = Integer.parseInt(intervalTime);
		final int totalCountNumber = Integer.parseInt(totalCount);
		
		startCompleteListerner(userThreadNumber * totalCountNumber);
		
		for (int i = 0; i < userThreadNumber; i++) {  
			Worker worker = new Worker("work"+i, url, intervalTimeNumber, totalCountNumber);
			worker.start();
		}
	
	}
	
	class Worker extends Thread{
		String workerName;   
        int intervalTime;  
        int totalCount;
        String url;
        
		public Worker(String workerName ,String url, int intervalTime , int totalCount) {
			this.workerName=workerName;  
            this.intervalTime=intervalTime;  
            this.totalCount = totalCount;
            this.url = url;
		}
		
		@Override
		public void run() {
			for(int j=0;j<totalCount&&isRun();j++){
				try {
					Thread.sleep(intervalTime);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	            try {  
	                HttpClient client = new HttpClient();  
	                client.openConnection(url);  
					// http://www.baidu.com/s?wd=java  
	                client.request("uid=" + UUID.randomUUID().toString());  
	                client.response();  
	                client.closeConnection();  
	                increaseOverNum();
	            } catch (Throwable e) { 
	            	failDeal();
	                e.printStackTrace();  
	            }  
			}
		}
	}
	/**
	 * 
	 * listener whether the task is over
	 * @param taskNum total task number
	 */
	private void startCompleteListerner(final int taskNum) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int complete = 0;
				while(taskNum>complete){
					complete=getFailNum()+getOverNum();
				}
				completeJob();
			}
		}).start();
	}
	
	/**
	 * tell all thread program is over
	 */
	private void completeJob() {
		isRun = false;
	}
	
	public void start(){
		isRun = true;
	}

	private void initNum(){
		overCount.set(0);
		failCount.set(0);
	}
	/**
	 * stop thread
	 */
	public void stop() {
		isRun = false;
	}
	/**
	 * return whether the program is running
	 * @return
	 */
	public boolean isRun(){
		return isRun;
	}
	/**
	 * return complete number
	 * @return
	 */
	public int getOverNum(){
		return overCount.get();
	}
	/**
	 * return failure number
	 * @return
	 */
	public int getFailNum(){
		return failCount.get();
	}
	/**
	 * increase complete number
	 */
	private void increaseOverNum(){
		overCount.addAndGet(1);
	}
	/**
	 * increase failure number
	 */
	private void failDeal(){
		failCount.addAndGet(1);
	}
	
	private void postData(){
	
	}
	
	

}
