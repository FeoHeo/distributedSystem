# Running the project:

### Using Eclipse IDE (I use Linux but I think Windows is almost the same)

First open the file as an Eclipse Project

**Setup rmiregistry**

Click the drop-down button (arrow down) at the top bar of Eclipse
[External tool configurations] -> [New launch configuration](looks like a white file icon)
-> [Browse Filesystem] -> Select Rmiregistry .bin file (for my machine running Ubuntu it's at "/usr/lib/jvm/java-17-openjdk-amd64/bin/rmiregistry")
-> Input "2000" into the arguments box and click [Apply]
(The rmiregistry in my computer would not run in default port cause there are other tasks that would occupy it before)

From here, we can use Eclipse to run [AggregationServer.java] -> [ContentServer.java] -> [GETClient.java]

**Import .jar file** (it should be inside 'assignment2/lib/json-simple-1.1.jar')

[Right-click assignment2 on Eclipse filesystem (left-bar)] -> [Hover on 'Build Path'] -> [Click 'Add external archive'] -> [Select 'json-simple-1.1.jar' (the on inside '/lib/')]

### Navigate through the files

**ContentServer**: Inputting "send" (after starting ContentServer) will proceed to send the data

**Aggregation Server**: There is a 'debug' variable at the top (default is true) and it will print more info

**GETClient**: Stays up and listen for data till termination to receive data, will print any info received through the connected socket

# Testing Functionality

### Sending Text and data

[Start AggregationServer] -> [Start ContentServer] -> [Start GETClient]/[Access localhost:4567 on browser]

Input: "send" on ContentServer should send the data. Output on ContentServer terminal is sent JSON and output on AggregationServer is received and stored JSON

Note: Can also test updating JSON data, sending null JSON and send invalid JSON using "send" in ContentServer. Just change the data.txt file 


### Test expunging expired data

After sending JSON data from the ContentServer, Console for AggregationServer will print out "Expunging data" after 30 seconds (you can also change the delay by changing the number at line 111 in AggregationServer)

NOTE: Please email me a1876928@adelaide.edu.au if something is not working. The code for my previous assignment ran on my machine but did not compiled on the grader's machine (this was largely my fault cause I did not include a README) so I wanted to fix the problem ASAP if it arise.

### Connection retry

Pretty simple. If "ContentServer" or "GETClient" started without AggregationServer running previously then an attempt to retry connection will be made after every 5 seconds (maximum of 5 times)
