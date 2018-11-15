#Ver2.0,2016.01.18
#!/bin/bash
cd `dirname $0`
if [ "$1" = "start" ]; then
	./start.sh
else
	if [ "$1" = "stop" ]; then
		./stop.sh
	else
		if [ "$1" = "debug" ]; then
			./start.sh debug
		else
			if [ "$1" = "restart" ]; then
				./restart.sh
			else
				echo "ERROR: Please input argument: [start] or [stop] or [debug] or [restart]!"
				exit 1
			fi
		fi
	fi
fi
