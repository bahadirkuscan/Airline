# Airline
Manually implemented data structures: Graph (enhanced with internal Priority Queue), Priority Queue (enhanced with internal Hash Map)

Given a list of airports with their coordinates and airfields, a list of available flight directions, a list of weather conditions of the airfields in different times and a list of missions (each mission consists of a starting airport, a destination airport, a start time and a deadline), this program completes 2 tasks. The first task is to find the path with the minimum flight cost and print it out with its cost. In this task, the time and therefore the weather conditions don't change during the whole process and they are assumed to be the initial conditions. However in the second task, time flows and therefore the weather conditions change. Also in addition, there is a parking operation that allows waiting for the weather conditions to get better. This operation also has a cost which varies over the parking airport (each airport has its own given parking cost). So this task is to find the path with the minimum total cost that does not exceed the deadline, and print it out with its cost as in task 1. The mission might be impossible in the given time period, the program outputs that no solution is found in that case.

Note: There are different subsidiaries for different test cases, they use different planes which have different flight durations for the same distances.
