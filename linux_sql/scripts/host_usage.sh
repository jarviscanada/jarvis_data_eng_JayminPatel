psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

if [ $# -ne 5 ]; then
      echo 'Invalid Arguments'
      exit 1
fi

vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)
memory_free=$(echo "$vmstat_mb" | awk '{print $4}'| tail -n1 | xargs)
cpu_idle=$(echo "$vmstat_mb" | awk '{print $15}' | tail -n1 | xargs)
cpu_kernel=$(echo "$vmstat_mb" | awk '{print $14}' | tail -n1 | xargs)
disk_io=$(echo "$vmstat_mb" | awk '{print $10}' | sed 's/[^0-9]*//g'  | xargs)
disk_available=$(df -BM / | awk '{print $4}' | tail -1 | sed 's/[^0-9]*//g' | xargs)
timestamp=$(date -u "+%Y-%m-%d %H:%M:%S")

host_id="(SELECT id FROM host_info
          WHERE hostname='$hostname')";

insert_stmt="INSERT INTO host_usage(
                                    host_id,
                                    memory_free,
                                    cpu_idle,
                                    cpu_kernel,
                                    disk_io,
                                    disk_available,
                                    timestamp
                                  )
                        VALUES    (
                                     $host_id,
                                     $memory_free,
                                    '$cpu_idle',
                                    '$cpu_kernel',
                                     $disk_io,
                                     $disk_available,
                                    '$timestamp'
                                  )"

export PGPASSWORD=$psql_password
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
#psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "(SELECT * FROM host_usage)"

exit $?