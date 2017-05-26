# Twitter Analyzing #

Java example of analyzing twitter data with Apache Hadoop MapReduce.

 - `TwitterFollowerCounter` - evaluate number of followers per user
 - `TwitterAvgFollowers` - evaluate average number of followers per all users
 - `TwitterTopFollowers` - evaluate to 50 users by number of followers
 - `TwitterFollowerCounterGroupByRanges` - evaluate number of followers in ranges [1 .. 10], [11 .. 100], [101 .. 1000], ...



# How-to run #

    * Download the folder *
    cd Twitter Analysis
    
    mvn compile
    mvn package
    
    hadoop fs -rm -r /user/s0073/count || echo "No"
    hadoop fs -rm -r /user/s0073/avg || echo "No"
    hadoop fs -rm -r /user/s0073/top || echo "No"
    hadoop fs -rm -r /user/s0073/range || echo "No"
    
    hadoop jar target/hhd-1.0-SNAPSHOT.jar TwitterFollowerCounter /data/twitter/twitter_rv.net /user/s0073/count
    hadoop jar target/hhd-1.0-SNAPSHOT.jar TwitterAvgFollowers /user/s0073/count /user/s0073/avg
    hadoop jar target/hhd-1.0-SNAPSHOT.jar TwitterTopFollowers /user/s0073/count /user/s0073/top
    hadoop jar target/hhd-1.0-SNAPSHOT.jar TwitterFollowerCounterGroupByRanges /user/s0073/count /user/s0073/range

    
# Source Data format #

 - `/data/twitter/twitter_rv.net` contains strings like `user_id \t follower_id` (without spaces around `\t`)


# Generated Data formats #

 - `/user/s0073/count` - contains strings like `user_id \t number_of_followers` (without spaces around `\t`)
 - `/user/s0073/avg` - contains one string `0 \t average_number_of_followers` (without spaces around `\t`, `0` is fictive key)
 - `/user/s0073/top` - contains 50 strings like `0 \t user_id \t number_of_followers` (without spaces around `\t`, `0` is fictive key)
 - `/user/s0073/range` - contains strings like `range_number \t number_of_users` (without spaces around `\t`, `range_number` example: 10 - [1 .. 10], 11 - [11 .. 100], ...)

 
# How-to view results #

    # hadoop fs -cat /user/s0073/count/*
    hadoop fs -cat /user/s0073/avg/*
    hadoop fs -cat /user/s0073/top/*
    hadoop fs -cat /user/s0073/range/*


# Time #

Hadoop work on 8x nodes: 2x Dual-core AMD Opteron 285 2.6 GHz, 8 GB RAM, 150 GB HDD.

* Files *  
 - `/data/twitter/twitter_rv.net` - 24.4 G
 - `/user/s0073/count` - 35.6 M * 13 (parts 00000 - 00012)

* Time *  
 - `TwitterFollowerCounter` - 22:32:52 - 23:02:35 (0:29:43)
 - `TwitterAvgFollowers` - 23:02:43 - 23:04:24 (0:01:41)
 - `TwitterTopFollowers` - 23:04:31 - 23:06:50 (0:02:19)
 - `TwitterFollowerCounterGroupByRanges` - 23:06:57 - 23:08:06 (0:01:09)


# Other #

Sometime you need up hadoop job priority. Use this command:

    mapred job -set-priority <job_id> VERY_HIGH
