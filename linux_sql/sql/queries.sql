SELECT cpu_number,
       id,
       total_mem,
       row_number()  OVER
    (
        PARTITION BY cpu_number
        ORDER BY total_mem DESC
    )
FROM host_info;

-- round timestamp = 5 min
CREATE FUNCTION round5(ts timestamp) RETURNS timestamp AS
    $$
BEGIN
RETURN date_trunc('hour', ts) + date_part('minute', ts):: int / 5 * interval '5 min';
END;
$$
LANGUAGE PLPGSQL;

SELECT host_id,
       round5( host_usage.timestamp ),
       trunc( avg( host_info.total_mem-memory_free ) / avg( host_info.total_mem ) * 100,1)
           as avg_used_memory_percentage
FROM host_usage
         LEFT JOIN host_info
                   ON host_usage.host_id=host_info.id
group by round5, host_id
order by host_id;

SELECT host_id,
       round5(host_usage.timestamp),
       COUNT(host_id) AS num_data_points
FROM host_usage
GROUP BY host_id, round5
HAVING count(host_id)<=2
ORDER BY host_id, round5;