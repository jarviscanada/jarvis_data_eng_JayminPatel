CREATE TABLE PUBLIC.host_info
(
    id			        SERIAL NOT NULL,
    hostname		    VARCHAR NOT NULL,
    cpu_number		    SMALLINT NOT NULL,
    cpu_architecture	VARCHAR NOT NULL,
    cpu_model		    VARCHAR NOT NULL,
    cpu_mhz			    REAL NOT NULL,
    L2_cache		    INTEGER NOT NULL,
    total_mem		    REAL NOT NULL,
    timestamp		    TIMESTAMP default now(),
    CONSTRAINT host_info_pk PRIMARY KEY (id),
    CONSTRAINT host_info_un UNIQUE (hostname)
);

CREATE TABLE PUBLIC.host_usage
(
    timestamp	TIMESTAMP default now(),git
    host_id		INTEGER NOT NULL,
    memory_free	INTEGER NOT NULL,g
    cpu_idle	INTEGER NOT NULL,
    cpu_kernel	INTEGER NOT NULL,
    disk_io 	INTEGER NOT NULL,
    disk_available	INTEGER NOT NULL,
    CONSTRAINT host_uasge_host_info_fk FOREIGN KEY (host_id) REFERENCES host_info(id)
);
