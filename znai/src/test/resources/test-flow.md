# nginx->access_log

NGINX stores every access to access.log. It also kerberizes the request.

# access_log->log_stash_log

Log Stash runs on the same machine and consumes access.log lines. 
As long as lines contain required information it gets send to Kafka