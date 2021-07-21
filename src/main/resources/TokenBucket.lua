--传入错误的参数，返回-1
if KEYS[1] == nil then
    return tonumber(-1)
end

--第一次进行请求，在redis中初始化限流信息
if(redis.call("exists",KEYS[1])==0) then
    redis.call("hmset",KEYS[1],
                "volume",ARGV[1],
                "rate",ARGV[2],
                "current",ARGV[1],
                "last_visit",ARGV[4]
                )
    redis.call("expire",KEYS[1],ARGV[3])
end
--获取上次访问请求的时间，计算新增令牌并放入桶中
local rateLimitInfo = redis.call("hmget",KEYS[1],"volume","rate","current","last_visit")
local volume = tonumber(rateLimitInfo[1])
local rate = tonumber(rateLimitInfo[2])
local current = tonumber(rateLimitInfo[3])
local last_visit = tonumber(rateLimitInfo[4])
local now = current + rate * math.floor((tonumber(ARGV[4])-last_visit)/1000)
if(now > volume) then
    now = volume
end

redis.call("hmset",KEYS[1],"current",now)

if(now < 0) then
    return tonumber(0)
else
    redis.call("hincrby",KEYS[1],"current",-1)
    redis.call("hmset",KEYS[1],"last_visit",ARGV[4])
    return tonumber(now)
end