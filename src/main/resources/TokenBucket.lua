--key值，用来辨识要限流的请求
local key = "rate.limit:"..KEYS[1]
--设置限流值
local limit = tonumber(ARGV[1])
--获取当前请求数，如果这个请求的限流信息没有被初始化，就设为0
--当redis里没有该请求的限流信息的时候，并不需要添加，因为后面的INCREBY命令会在查询不到key的时候自行添加一个value为0的行，再执行INCREBY
local current = tonumber(redis.call("get",key) or "0")
--请求超过上限，返回0表示请求失败
if current+1 > limit then
    return tostring(0)
else
    --请求能够被接受，先增加处理的请求数，再更新限流信息的时限
    redis.call("INCRBY",key,"1")
    redis.call("expire",key,"2")
    return tostring(current+1)
end