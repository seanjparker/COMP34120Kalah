#!/bin/bash

RESULTS=results.txt

echo "START" > $RESULTS

echo "MKREF" >> $RESULTS
java -jar ManKalah.jar "java -jar MKRefAgent.jar" "java -jar Agent.jar" >> $RESULTS
echo "JIMMY" >> $RESULTS
java -jar ManKalah.jar "java -jar Agents/JimmyPlayer.jar" "java -jar Agent.jar" >> $RESULTS
echo "ERROR404" >> $RESULTS
java -jar ManKalah.jar "java -jar Agents/error404.jar" "java -jar Agent.jar" >> $RESULTS
echo "GROUP2" >> $RESULTS
java -jar ManKalah.jar "java -jar Agents/Group2Agent.jar" "java -jar Agent.jar" >> $RESULTS
# echo "GROUP1" >> $RESULTS
# java -jar ManKalah.jar "java -jar Agents/Group1.jar" "java -jar Agent.jar" >> $RESULTS

echo "END" >> $RESULTS

python3 analyse_test_results.py
