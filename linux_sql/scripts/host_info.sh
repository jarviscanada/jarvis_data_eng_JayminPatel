total_mem=$(grep "^MemTotal" /proc/meminfo | awk '{ print $2 }')
timestamp=$(date -u "+%Y-%m-%d %H:%M:%S")

insert_stmt="INSERT INTO host_info(
                                    hostname,
                                    cpu_number,
                                    cpu_architecture,
                                    cpu_model,
                                    cpu_mhz,
                                    L2_cache,
                                    total_mem,
                                    timestamp
                                  )
                        VALUES    (
                                    '$hostname',
                                     $cpu_number,
                                    '$cpu_architecture',
                                    '$cpu_model',
                                     $cpu_mhz,
                                     $L2_cache,
                                     $total_mem,
                                    '$timestamp'
                                  )"
export PGPASSWORD=$psql_password
echo "$insert_stmt"
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?