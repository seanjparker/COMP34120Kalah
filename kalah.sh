#!/bin/bash

# Run this command to launch it without compiling the jar
# java -cp ./bin MKAgent.Main 
compile()
{
  if [ -f Agent.jar ]; then
    rm Agent.jar
  fi
  if [ ! -d bin ]; then
    mkdir bin
  fi
  javac MKAgent/Main.java -d bin
  jar cfm Agent.jar META-INF/MANIFEST.MF -C bin .
}

run()
{
  agent1=$1
  agent2=$2
  if [ $# -eq 1 ]; then
    agent2="java -jar MKRefAgent.jar"
  elif [ $# -eq 0 ]; then
    agent1="java -jar Agent.jar"
    agent2="java -jar MKRefAgent.jar"
  fi
  echo "Agent 1 -> ${agent1}"
  echo "Agent 2 -> ${agent2}"
  java -jar ManKalah.jar "${agent1}" "${agent2}"
}

case "$1" in
    -c|--compile)
      compile
      ;;
    -r|--run)
      run "${@:2}"
      ;;
    -h | --help)
      echo $"Usage: $0 {--compile or -c | --run or -r}"
      exit 1
      ;;
    *)
      compile
      run "${@:2}"
      ;;
esac