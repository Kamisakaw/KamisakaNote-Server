create table if not exists article(
 `aid` bigint auto_increment,
 `title` varchar(60),
 `author` varchar(30),
 `createDate` timestamp,
 `updateDate` timestamp,
 `cid` varchar(30),
 `description` varchar(200),
 `content` text,
 `view` int,
 primary key (`aid`)
);