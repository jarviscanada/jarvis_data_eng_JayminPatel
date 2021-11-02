SELECT cpu_number, id AS host_id, total_mem

FROM host_info

ORDER BY cpu_number ASC, total_mem DESC;

CREATE FUNCTION round5(ts timestamp) RETURNS timestamp AS $$ BEGIN RETURN
    date_trunc('hour', ts) + date_part('minute', ts):: int / 5 * interval '5 min';
END;
$$

SELECT usg.host_id,
    hostname AS host_name,
    round5(usg.timestamp) as timestamp,
    AVG(((total_mem::float - memory_free::float)/total_mem::float) * 100)::int AS avg_used_mem_percentage

FROM host_info AS inf, host_usage AS usg

WHERE inf.id = usg.host_id

GROUP BY usg.host_id,
    hostname, round5(usg.timestamp)

ORDER BY timestamp ASC;

SELECT host_id,
    round5(timestamp), count(*) AS num_data_points

FROM host_usage

GROUP BY
    host_id, round5(timestamp)

HAVING count(*) < 3

ORDER BY host_id, round5(timestamp);