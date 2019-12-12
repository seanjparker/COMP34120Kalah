#!/bin/bash

RESULTS=results.txt

# Plays as north player.
north()
{
    echo "MKREF" >> $RESULTS
    echo "MKREF"
    java -jar ManKalah.jar "java -jar MKRefAgent.jar" "java -jar Agent.jar" >> $RESULTS

    for file in ./Agents/error404.jar; do 
        echo "${file##*/}"  >> $RESULTS
        echo "${file##*/}"
        java -jar ManKalah.jar "java -jar $file" "java -jar Agent.jar" >> $RESULTS
    done
}

# Plays all as south player
south()
{
    echo "MKREF" >> $RESULTS
    echo "MKREF"
    java -jar ManKalah.jar "java -jar Agent.jar" "java -jar MKRefAgent.jar" >> $RESULTS

    for file in ./Agents/*.jar; do 
        echo "${file##*/}"  >> $RESULTS
        echo "${file##*/}"
        java -jar ManKalah.jar "java -jar Agent.jar" "java -jar $file" >> $RESULTS
    done
}

some()
{
    echo "JIMMY" >> $RESULTS
    echo "JIMMY"
    java -jar ManKalah.jar "java -jar Agents/JimmyPlayer.jar" "java -jar Agent.jar" >> $RESULTS
 
    # echo "MKREF" >> $RESULTS
    # echo "MKREF"
    # java -jar ManKalah.jar "java -jar MKRefAgent.jar" "java -jar Agent.jar" >> $RESULTS
       # echo "ERROR404" >> $RESULTS
    # echo "ERROR404"
    # java -jar ManKalah.jar "java -jar Agents/error404.jar" "java -jar Agent.jar" >> $RESULTS
    # echo "GROUP2" >> $RESULTS
    # echo "GROUP2"
    # java -jar ManKalah.jar "java -jar Agents/Group2Agent.jar" "java -jar Agent.jar" >> $RESULTS
    # echo "GROUP1" >> $RESULTS
    # echo "GROUP1"
    # java -jar ManKalah.jar "java -jar Agents/Group1.jar" "java -jar Agent.jar" >> $RESULTS
}


echo "START" > $RESULTS

case "$1" in
    -a|--all)
        echo "P2"
        north
        ;;
    -s|--south)
        echo "P1"
        south
        ;;
    *)
        echo "P2"
        some
        ;;
esac

echo "END" >> $RESULTS

python3 analyse_test_results.py
