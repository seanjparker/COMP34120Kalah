test_results = open("results.txt", "r")
line = test_results.readline()
if("START" in line):
    line = test_results.readline()
else:
    exit()

i = 1
gameCount = 0
agent_win_count = 0
p2_cumulative_avg_time = 0



while not "END" in line :
    if i % 2 == 0:
        if line[0] == "1":
            agent_win_count += 1
        p2_cumulative_avg_time += int(line[2:])
        gameCount += 1    
    line = test_results.readline()
    i += 1


print("Percent Games Won: " + str((agent_win_count / gameCount) * 100) + "%")
print("Mean Turn Time: " + str((p2_cumulative_avg_time / gameCount)) + "ms")