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

/**
 * 任务执行Executor接口
 * @author Chenlong
 * */
public interface IActorExecutor extends Runnable,Comparable<IActorExecutor>{
	/**
	 * 提交任务给Executor
	 * @param task 提交的任务
	 * */
	void submit(Runnable task);
	/**
	 * @return  当前等待完成的任务数
	 * */
	int getUndoneTaskSize();
	/**
     * @return 获取Executor的工作线程
     * */
	Thread workThread();

	/**
	 * actor数量加1
	 */
	void incrActorCount();
	/**
	 * actor数量减1
	 */
	void decrActorCount();
	/**
	 * 获取actor数量
	 */
	int getActorCount();

}
