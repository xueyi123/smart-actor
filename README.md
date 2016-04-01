#SmartActor是一个任务模型
###它能保证同一个Actor执行的任务都在同一个执行线程里，并且保证同一个Actor任务都是顺序执行的
###经常用于多并发任务系统，他的功能其实类似于线程池，只不过线程池的执行任务是随机分配线程的,而SmartActor可以指定线程执行
<br>//创建Actor管理器
<br>ActorManager manager = new ActorManager();
<br>//创建一个Actor
<br>IActor actor= manager.createActor();
<br>//执行普通任务
<br>actor.execute();
<br>//执行定时任务
<br>actor.scheduleAtFixedRateTask();
<br>//切换执行线程,用于切换工作场景时切换执行线程，比如用户进入某个房间，则要设置为房间的执行线程，这样就消除多线程的问题)
<br>actor.switchExecutor();