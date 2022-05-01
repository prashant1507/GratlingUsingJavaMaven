#!/bin/bash

#params
SIMULATION_CLASSES=

#usage
function usage (){
    echo "usage: $0 options"
    echo "This script run Gatling load tests"
    echo ""
    echo "OPTIONS:"
    echo "Run options:"
    echo "   -s             [*] Simulation classes (comma separated)"
}

#INIT PARAMS
while getopts "s:" OPTION
do
     case $OPTION in
     s) SIMULATION_CLASSES=$OPTARG;;
     ?) usage
        exit 1;;
     esac
done

#checks
if [[ -z $SIMULATION_CLASSES ]]; then
    usage
    exit 1
fi

#run scenarios
SIMULATION_CLASSES_ARRAY=($(echo $SIMULATION_CLASSES | tr "," "\n"))

for SIMULATION_CLASS in "${SIMULATION_CLASSES_ARRAY[@]}"
do
    echo "Run scenario for $SIMULATION_CLASS"
    mvn gatling:execute -Dgatling.simulationClass=$SIMULATION_CLASS
done