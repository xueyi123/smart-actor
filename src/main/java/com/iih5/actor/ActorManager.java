/*
 * Copyright 2016 xueyi (1581249005@qq.com)
 *
 * The Smart-Actor Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.iih5.actor;

import com.iih5.actor.util.ListSortUtil;
import com.iih5.actor.util.ThreadFactoryUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 线程并发包管理类，负责为提交者分配actor
 */
public class ActorManager {
	protected int threadSize;
	protected ExecutorService threadPool;
	private List<IActorExecutor> actorThreads;
	private ScheduledExecutorService scheduledExecutorService=Executors.newSingleThreadScheduledExecutor();
    private Map<String,IActor> actorMap=new ConcurrentHashMap<String, IActor>();
	public ActorManager(int threadSize, ThreadFactory factory) {
		this.threadSize=threadSize;
		this.threadPool=initThreadPool(factory);
		actorThreads =new ArrayList<IActorExecutor>(threadSize);
	}
	public ActorManager() {
		ThreadFactory threadFactory =ThreadFactoryUtil.createThreadFactory("Default-Pool-"+System.currentTimeMillis());
		this.threadSize=8;
		actorThreads =new ArrayList<IActorExecutor>(threadSize);
		this.threadPool=initThreadPool(threadFactory);
	}
	private ExecutorService initThreadPool(ThreadFactory factory){
		if(threadPool==null){
			this.threadPool=Executors.newFixedThreadPool(this.threadSize,factory);
		}
		return threadPool;
	}

	/**
	 * 得到一个MessageTaskExecutor
	 * */
	public IActorExecutor assignActorExecutor(){
		//判断集合中Executor是否已达到配置上限
		if(actorThreads.size()<threadSize){
			//直接取出空闲的Executor
			for(IActorExecutor executor: actorThreads){
				if(executor.getUndoneTaskSize()==0&&executor.getActorCount()==0){
					executor.incrActorCount();
					return executor;
				}
			}
			//如果没有空闲的Executor则创建新的到集合中，并提交到线程池
			IActorExecutor executor=new ActorExecutor();
			executor.incrActorCount();
			actorThreads.add(executor);
			threadPool.execute(executor);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				//
			}
			return executor;
		}else{//如果集合中元素已满，则取出一个任务最少的
			ListSortUtil.sort(actorThreads);
			IActorExecutor er= actorThreads.get(0);
			er.incrActorCount();
			return er;
		}
		
	}
    /**
     * 用来提交延时任务到队列中的线程池
     * @see Executors#newSingleThreadScheduledExecutor()
     * */
	public ScheduledExecutorService getScheduledExecutorService() {
		return scheduledExecutorService;
	}
    /**
     * 创建一个已分配好执行任务Executor的Submiter
     * */
	public IActor createActor() {
		IActor actor=new Actor(this);
		IActorExecutor actorExecutor = assignActorExecutor();
		actor.setExecutor(actorExecutor);
		return actor;
	}
    /**
     * @return  获取所有用来处理任务的RunnableExecutor
     * */
	public List<IActorExecutor> getActorExecutors() {
		return actorThreads;
		
	}

	/**
	 * 创建一个已分配好执行任务Executor的Submiter
	 * @param actorName
     * @return
     */
    public IActor createActor(String actorName) {
        IActor actor=createActor();
        actorMap.put(actorName,actor);
        return actor;
    }

	/**
	 * 根据名字获取Actor
	 * @param actorName
     * @return
     */
    public IActor getActor(String actorName) {
        return actorMap.get(actorName);
    }

	/**
	 * 关闭ActorManager线程池
	 */
	public void shutdown(){
		threadPool.shutdown();
	}


}
