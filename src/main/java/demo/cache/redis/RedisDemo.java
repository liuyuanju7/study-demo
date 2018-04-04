package demo.cache.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuyuanju1 on 2017/12/21.
 */
public class RedisDemo {
    public static void main(String[] args) {
        //链接本地Redis 服务
        Jedis jedis = new Jedis("localhost");
        System.out.println("jedis 链接 ping ：" + jedis.ping());

        // String key value
        jedis.set("demo","redisDemo");
        System.out.println("demo :" + jedis.get("demo"));

        // list
        jedis.lpush("testList","test1");
        jedis.lpush("testList","test2");
        jedis.lpush("testList","test3");
        List<String> testList = jedis.lrange("testList",0,jedis.llen("testList"));
        System.out.println("testList :" + testList.toString());

        List<String> rlist = jedis.lrange("rlist",0,3);
        System.out.print("rlist : ");
        for(String str : rlist){
            System.out.print(str + " ");
        }

        //map
        Map<String,String> user = new HashMap<String, String>();
        user.put("name","lyj");
        user.put("age","21");
        jedis.hmset("rMap",user);

        System.out.println("\n user.name :" +jedis.hget("rMap","name"));
        System.out.println("user : " + jedis.hgetAll("rMap"));

        //key  输出所有key
        System.out.println("所有的key 为：" + jedis.keys("*"));
        //设置超时时间
        System.out.println("设置rmap的过期时间为60s : " + jedis.expire("rMap",60));
        // 查看某个key的剩余生存时间,单位【秒】.永久生存 -1  不存在的返回-2
        System.out.println("查看rmap的剩余生存时间："+jedis.ttl("rMap"));
        //清空当前选择的数据库所有key
        System.out.println("清空所有数据 " + jedis.flushDB());

        System.out.println("清空后的key 为：" + jedis.keys("*"));

        //事务
        Transaction transaction = jedis.multi();
        transaction.set("key1","value1");
        transaction.set("key2","value2");
        transaction.exec();
        System.out.println(jedis.get("key1") + " " + jedis.get("key2"));
    }
}
