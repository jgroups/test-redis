**1、SDR说明**

  Spring Data Redis(SDR)，是SpringFramework提供的一套简化访问Redis的API，是对Jedis的又一层封装。
SDR集成了Jedis，JRedis，SRP，Lettuce这四种开源的Redis Connector，这些Connector都是针对于Redis的开源Java库。其中，JRedis和SRP从spring-data-redis1.7开始，就不支持了。

  **2、RedisTemplate说明**

  RedisTemplate是SDR的一个核心Helper类，该类是一个高级的抽象（对Jedis的又一层封装），它封装了对Redis中的数据的CRUD操作，包含“高级封装”。

  _(1)高级封装（推荐使用）_

  高级封装的操作包含：OpsForValue()，OpsForList()，OpsForSet()，OpsForZset()，OpsForHash()等等。
SDR官方文档中对Redistemplate的介绍：the template is in fact the central class of the Redis module due to its rich feature set. The template offers a high-level abstraction for Redis interactions.
　　通过Redistemplate可以调用ValueOperations和ListOperations等等方法，分别是对Redis命令的高级封装。
　　但是ValueOperations等等这些命令最终是要转化成为RedisCallback来执行的。也就是说通过使用RedisCallback可以实现更强的功能，SDR文档对RedisCallback的介绍：RedisTemplate and StringRedisTemplate allow the developer to talk directly to Redis through the RedisCallback interface. This gives complete control to the developer as it talks directly to the RedisConnection。

  _(2)从高级封装获得低级封装的过渡：_
  
  `RedisOperations<String, Object> operations = opsForValue.getOperations();`

  **3、RedisConnection提供了“低级封装”。**

  _(1)低级封装_
低级封装的操作是通过连接到Redis的Connection对象，直接对Redis数据进行操作。
低级封装的核心是：`redisTemplate.execute(new RedisCallback(){})`

  **4、注意事项**

  搭建Spring与Reids的环境时，重点在于在Spring容器中配置RedisTemplate和ConnectionFactory，以及封装RedisTemplate组件。
  
  Spring Data Redis 1.x binaries requires JDK level 6.0 and above, and Spring Framework 4.2.6.RELEASE and above.
  
  Redis默认有16个数据库，在配置文件中，设置Redis需要使用的哪个Redis数据库，应该使用属性“database”，而不是“dbIndex（自定义）”这个属性。
