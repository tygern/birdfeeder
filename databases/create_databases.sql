drop database if exists feed;
drop database if exists feed_test;

create user if not exists birdfeeder;

create database feed;
create database feed_test;

grant all privileges on feed.* to birdfeeder;
grant all privileges on feed_test.* to birdfeeder;
