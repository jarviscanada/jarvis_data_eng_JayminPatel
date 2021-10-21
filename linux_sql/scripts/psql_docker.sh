
cmd=$1
db_username=$2
db_password=$3
sudo systemctl status docker || systemctl start docker
docker container inspect jrvsdata-psql
container_status=$?
echo "Container status=$container_status"
case $cmd in
    create)
    if [ $container_status -eq 0 ]; then
      echo 'Container already exists'
      exit 0
    fi

    if [ $# -ne 3 ]; then
      echo 'Create requires username and password'
      exit 1
    fi

    docker volume create pgdata
    docker run --name jrvsdata-psql -e POSTGRES_USER=$db_username -e POSTGRES_PASSWORD=$db_password -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres
    exit $?
    ;;

    start|stop)
    if [ $container_status -ne 0 ]; then
      echo "Container is not created. Please Create container first"
      exit 1
    fi
    docker container $cmd jrvsdata-psql
    exit $?
    ;;
  *)
    echo 'Illegal command'
    echo 'Commands: start|stop|create'
    exit 1
    ;;
esac