test_results = open("results.txt", "r")
line = test_results.readline()
if("START" in line):
    line = test_results.readline()
else:
    exit()

if("P1" in line):
    player = 1
else:
    player = 2

line = test_results.readline()

loseAgainst = []

overall_wins = 0
total_games = 0

while not "END" in line :
    line_count = 1
    game_count = 0
    agent_win_count = 0
    p2_cumulative_avg_time = 0

    agent_name = line
    line = test_results.readline().strip()

    while line[0] == "0" or line[0] == "1":
        # Assuming player 2
        if (player == 1 and line_count % 2 == 1) or (player == 2 and line_count % 2 == 0):
            if line[0] == "1":
                agent_win_count += 1
            else:
                loseAgainst.append(agent_name)
            p2_cumulative_avg_time += int(line[2:])
            game_count += 1
        line = test_results.readline().strip()
        line_count += 1

    overall_wins += agent_win_count
    total_games += game_count  

    print("Agent: " + agent_name.strip())
    print("Percent Games Won: " + str((agent_win_count / game_count) * 100) + "%")
    print("Mean Turn Time: " + str((p2_cumulative_avg_time / game_count)) + "ms")

print("\n\n## Overall Win Percent: " + str((overall_wins / total_games) * 100) + "% ##\n\n")

print("Agents we lost to: \n")
for agent in loseAgainst:
    print(agent + "\n")
