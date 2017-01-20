# nginx->access_log

NGINX stores every access to access.log. It kerberizes the request.

# access_log->log_stash_log

Log Stash runs on the same machine and consumes access.log lines. 
As long as lines contain required information it gets processed.

# log_stash_log->kafka

Log stash won't write data directly to Elastic. Instead we will use Kafka as a buffer in front. 

# kafka->log_stash_elastic

Another instance of Log Stash consumes records from Kafka queue and writes them to Elastic.
Need follow-up with ELK team to see if buffer is still required.

# log_stash_elastic->elastic

Finally data gets written to Elastic by LogStash. Access.log timestamp will be the primary timestamp of a record.

# client->web_dashboard

We have built a web app to display news consumption analytics. And URL is be accessible by everyone.

# web_dashboard->elastic

Most of the data we take from Elastic. Data grows very fast and we need to come up with a scalable approach.  

# web_dashboard->cmdb

CMDB departments, active users and potential other information we take from CMDB. We can't replicate this queries by simply using
out of the box Kibana interface

