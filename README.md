##分布式服务调用框架
###实现
1. 基于Netty5 Zookeeper实现， Guice作为DI框架；
2. 代码参考了dubbo,netty权威指南，分布式服务框架（李林锋），LTS；
3. 此项目最初是为了实现一个分布式的任务分配系统，以rpc的方式进行数据通信，后此任务被中止，于是就把之间的rpc部分提取了出来，打算做为一个公共框架使用；
4. 除zookeeper外，不需要任何配置
5. 此项目还没在线上环境中使用过，目前仅适合用于学习与研究

###使用demo
1. 服务端启动类

	public class ProviderTest {

    public static void main(String[] args) {
        try {
            final ProviderMain provider = new ProviderMain();
            provider.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	}
   
   
2. 消费端启动类
	
	public class ConsumeTest {
     public static void main(String[] args) {
        try {
            final BootStrapTask task = new BootStrapTask();
            task.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	}

	
  具体参数说明可先看ProverderMain  和  ConsumerMain  类内的注释
	
###TODO
1. 目前所有的代码都在一个工程中，后期会分为公共部分、服务提供端、服务消费端、数据库操作层；
2. 以后会加入监控与统计功能
