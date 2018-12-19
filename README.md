
# PlayPhone Internship Inteview Project
Se Jin Lee  August 14, 2018

Description 
====
```
A single multithreaded producer and consumer program.
Producer produces a Set of strings of random length with random strings taken from the dictionary
There're multiple Consumers. Each Consumer is running in its own thread.
```
```
 All Consumers are waiting for data on a single BlockingQueue and when Consumer gets data from the queue it processes it and prints out the result and then starts waiting for data again.
 Incoming data for Consumer is a Set<String> produced by Producer. Consumer task is to concatenate all Strings in a given set and output the resulting String.
```
How to Execute
=====
```
compile App.java
run java "Path to App.java" <number of threads>
```

