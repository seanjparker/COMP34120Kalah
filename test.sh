#!/bin/bash

RESULTS=results.txt

echo "START" > $RESULTS
for i in {1..5}
do
    java -jar ManKalah.jar "java -jar MKRefAgent.jar" "java -jar Agents/error404.jar" >> $RESULTS
done

echo "END" >> $RESULTS
python3 analyse_test_results.py
