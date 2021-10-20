PGSQL_DATA_PATH='/var/lib/postgresql/data'
CONTAINER="jrvsdata-psql"
option="$1"
db_username="$2"
db_password="$3"

function getStatus(){
    CONTAINER_ID=$(docker ps -a | grep -v Exit | grep $CONTAINER | awk '{print $1}')
    if [[ -z $CONTAINER_ID ]] ; then
        echo 'Not running.'
        return 1
    else
        echo "Running in container: $CONTAINER_ID"
        return 0
    fi
}

#Open Docker, only if is not running
if (! docker stats --no-stream ); then
  # the terminal command to launch Docker
  systemctl status docker || systemctl start docker
 #Wait until Docker daemon is running and has completed initialisation
while (! docker stats --no-stream ); do
  # Docker takes a few seconds to initialize
  echo "Waiting for Docker to launch..."
  sleep 1
done
fi

CONTAINER_ID=$(docker ps -a | grep -v Exit | grep $CONTAINER | awk '{print $1}')

## create a psql docker container with the given username and password.
## print error message if username or password is not given
## print error message if the container is already created
if [ "$option" == "create" ];then
	if [ -z "$db_username" ] || [ -z "$db_password" ];then
		echo "unable to create"
		exit 1
	elif [ "$CONTAINER_ID" ];then
		echo "already exist"
		exit 1
	else
		$(docker run --name $CONTAINER -e POSTGRES_PASSWORD=$db_password -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 $db_username)
		exit 0
	fi

##check the status of the container,print message
getStatus

## start the stoped psql docker container
## print error message if the container is not created
elif [ "$option" == "start" ];then
	docker start $CONTAINER
	getStatus
	exit 0

## stop the running psql docker container
## print error message if the container is not created
elif [ "$option" == "stop" ];then
	if [[ -z $CONTAINER_ID ]]; then
		echo "already stopped"
		exit 1
	else
		docker stop $CONTAINER
		getStatus
		exit 0
	fi
fi

exit 0

