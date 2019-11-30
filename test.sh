#!/bin/bash

RESULTS=results.txt

all()
{
    echo "MKREF" >> $RESULTS
    java -jar ManKalah.jar "java -jar MKRefAgent.jar" "java -jar Agent.jar" >> $RESULTS

    for file in ./Agents/error404.jar; do 
        echo "${file##*/}"  >> $RESULTS
        java -jar ManKalah.jar "java -jar $file" "java -jar Agent.jar" >> $RESULTS
    done
}

some()
{
    echo "MKREF" >> $RESULTS
    java -jar ManKalah.jar "java -jar MKRefAgent.jar" "java -jar Agent.jar" >> $RESULTS
    echo "JIMMY" >> $RESULTS
    java -jar ManKalah.jar "java -jar Agents/JimmyPlayer.jar" "java -jar Agent.jar" >> $RESULTS
    echo "ERROR404" >> $RESULTS
    java -jar ManKalah.jar "java -jar Agents/error404.jar" "java -jar Agent.jar" >> $RESULTS
    echo "GROUP2" >> $RESULTS
    java -jar ManKalah.jar "java -jar Agents/Group2Agent.jar" "java -jar Agent.jar" >> $RESULTS
    echo "GROUP1" >> $RESULTS
    java -jar ManKalah.jar "java -jar Agents/Group1.jar" "java -jar Agent.jar" >> $RESULTS
}


echo "START" > $RESULTS

case "$1" in
    -a|--all)
        all
        ;;
    *)
        some
        ;;
esac

echo "END" >> $RESULTS

python3 analyse_test_results.py
