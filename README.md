## Compiling
`kalah.sh` accepts either `-r` to run `Agent.jar` with against some specific opponent or `-c` to compile the Agent defined in `MKAgent` folder

## Running Kalah Agent
The following command will **compile** the agent in `MKAgent` and **run** it againt the `MKRefAgent`
```bash
$ ./kalah.sh
```

You can use optionally pass two commands for the agents to runn against each other e.g:
```
$ ./kalah.sh "nc localhost 12345" "java -jar MKRefAgent.jar"
```
To play against your agent use `Agent.jar`
```
$ ./kalah.sh "nc localhost 12345" "java -jar Agent.jar"
```


