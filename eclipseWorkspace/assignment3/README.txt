[Setup environment]
- This program is built using Eclipse IDE on an Ubuntu machine so all instructions are based on using Eclipse IDE (it's an IDE so I don't think OS matters)
- Import file 'assignment3' as a project. Click on [File](top left) 
	-> [Open projects from file system] -> [Directory] -> #select 'asignment3'#
	
- Import launch settings.Click on [File] -> [Import] 
	-> #Search for 'launch configurations'# -> [Next] -> [Browse] 
	-> #open 'assignmnet3'# -> [Check the box for assignment 3] 
	-> [Check the box for all .launch files]
	
{I recommend checking out [Testing notes](bottom) for some time save. I added steps that you could take to satisfy each criteria's specifications in the assignment's descriptions}

[Running and testing]: There are 2 versions that you could run, one full functionality and one for testing 
(testing version uses 3 Acceptor and 1 Proposer so it could be faster to navigate if you're testing for certain features)

- Run the project(full functionality):click on ->[Run](on the same bar as [File]) 
	-> [Run configurations] 
	-> [Paxos] (It should be in 'launch group' or could just search 'Paxos')
	-> [Run] (On future run, you could use 'Run history' inside [Run])

	+ From here, 9 Acceptor nodes and 1 Proposer node will be started. Refer to [Guide] at the bottom on how to navigate
	+ If you wished to use a different port then I afraid that you will have to go to [Run] -> [Run config] -> [Paxos] -> [Edit]
		Then using [Edit]->[Arguments] to change the port. You'll have to change all 9 of them in increasing order.
		So according to default setup, Node_1 use port 5000, Node_2 use port 5001, ... , node_9 use port 5008.
		Because of this, you'll need to change all 9 ports to an increasing order and then change [arguments] for Proposer into the desired port also.
		More on program arguments at [Program arguments]
	+ Because this is too complicated, I have included in another .launch called [Paxos8000] which is the same but uses port 8000
	
	
[Guide]: Explainations on how to navigate the program
- If the project is running then on the bottom there should be a [console] opened with some outputs
	the program only runs when it's connected to 1 Proposer and the specified number of nodes (you can change amount of Acceptor nodes)

- Around bottom right, click on the drop-down menu for [Console] and 
	click on "SimpleProposer". If you see "Connected to server via port: %port" then you are at the right place
	Input "Send" to request the default value of 5 or "Send %number" to request a differnet value
	More details on user input at Proposer down below
	
[Using Proposer]

# Things inside [] like ["Send"] will be user inputs of "Send" (without quotes) in the Proposer console
# Quotes without [] like "Establishing on 5000" will be console outputs or parts of code (for both Proposer and Acceptors)
# % inside quotes like "%localPort" will signify the value of localPort after %
# Each Acceptor's output will be of format "[Acceptor-%port] %messageOutput"


["Send"]: Will request the default value 5

["Send %numberInput"]: will request a selected value

["Pause:%portNumber"]:	will prevent the paused port to respond 
						to Proposer's message. This does not prevent
						that same port to update data based on the output
						of other Acceptor nodes 
						(only responses to Proposer is blocked)
						
["Unpause:%portNumber"]: Unpause the specified port

["NoDelay"]: Eliminate delay in all Acceptor nodes

["Delay"]: Restore delay in all Acceptor nodes
						
["ctest"]:	Will send "Confirmed:5:2" where 5 is the value and 2 is roundId. 
			This simulates a message that Acceptor will send to each other when
			they receive a "Acccept" message from Proposer (this is mostly for testing)

[Notes on possible errors]:
Right now, the only error I expected is when nodeNum is not correctly configured.

Before you make any changes, be sure to check the .launch file you're using cause

[Paxos]: will launch 9 nodes
[PaxosTest]: will launch 3 nodes

-> Change nodeNum value in SimpleAcceptor(line 12) -> Change nodeNum value in SimpleProposer(line 10)
-> Update the 



[Assessment testing]

2 Proposer can send message at the same time: Currently unavailable

(I would suggest look at [Using Proposer] on using Proposer console)
M1-M9 have immediate responses: Click on [Run] -> [Run config] -> [Paxos] -> [launch Proposer] -> Input ["NoDelay"] in Proposer console -> Input ["Send %value"]
	(it's a launch group, if it's not there then you should import the launch config first)
	
M1-M9 work when there is delay and when M2 and M3 is down: Like the previous one, but this time not running ["NoDelay"] (or running ["Delay"] if you haven't ended the previous console)
	You'll have to manually disconnect M2 and M3 via Proposer console input ["Pause:%M2_port"] and ["Pause:%M3_port"]
	Then reconnect them via ["Unpause:%M2_port"] and ["Unpause:%M3_port"]
	
	If you want to further check the delay then you can update the [Paxos] launch config by
	-> [Run] -> [Run config] -> [Paxos] -> #Click on the desired node you want to change -> [Edit] -> [Arguments] -> #The third argument is the delay (delay in millisec, if blank then delay is 10ms)
	
Printouts confirmation: I have made it so that all elements of Paxos will printout the data they received.



