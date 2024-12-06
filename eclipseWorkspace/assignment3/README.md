# Paxos Project Setup and Usage Guide

I recommend checking out [Assessment Testing](#assessment-testing) for some time save. I added steps that you could take to satisfy each criteria's specifications in the assignment's descriptions. Of course, you should do [Environment Setup](#environment-setup) first


## Environment Setup

### IDE and Project Import
- This program is built using Eclipse IDE on an Ubuntu machine so all instructions are based on using Eclipse IDE (it's an IDE so I don't think OS matters)
- Import file 'assignment3' as a project. 
  1. Click on `File` (top left)
  2. `Open projects from file system`
  3. `Directory`
  4. Select `asignment3`

### Launch Configuration Import
1. Click on `File` -> `Import`
2. Search for `launch configurations`
3. Click `Next` -> `Browse`
4. Open `assignmnet3`
5. `Check the box for assignment 3`
6. `Check the box for all .launch files`

## Running and Testing

There are 3 Paxos versions that you could run:
- Full functionality version: `Paxos`
- Testing version: `PaxosTest` (uses 3 Acceptor and 1 Proposer so it could be faster to navigate if you're testing for certain features)
- Full functionality on different port(just in case): `Paxos8000`. You also have to change the `Run Configurations`

### Full Functionality Version
- Run the project: 
  1. Click on `Run` (on the same bar as `File`) 
  2. `Run configurations` 
  3. `Paxos` (It should be in 'launch group' or could just search 'Paxos')
  1. `Run` (On future run, you could use `Run history` inside `Run`)
  2. Rightclick `SimpleProposer` -> `Run As` -> `1 Java app`
  (If you use `Paxos8000` then you have to use run `SimpleProposer(8000)` instead)

#### Notes:
- You mus run `Paxos` then run `SimpleProposer` for it to work. Interact with the system through `SimpleProposer` console. Take a look at the [commands](#proposer-console-commands)
- When Proposer starts, if there is an issue. Try changing the `nodeNum` value located at 
- If port 5000 to 5008 does not work, another launch group called [Paxos8000] is included which uses port 8000 to 8008
- If for some reason, the above two doesn't work. Just contact me and I'll send you a .launch (it's not difficult to make but pretty tedious)

## Guide: Navigating the Program

- If the project is running, a `console` should be opened with some outputs
- The program only runs when connected to 1 Proposer and the specified number of nodes (you can change amount of Acceptor nodes)

### Proposer Console
- Around bottom right, click on the drop-down menu for [Console] 
- Click on "SimpleProposer"
- If you see "Connected to server via port: %port" then you are at the right place
- Input "Send" to request the default value of 5 or "Send %number" to request a different value

## Using Proposer

> Notes on syntax:
> - Things inside [] like ["Send"] will be user inputs of "Send" (without quotes) in the Proposer console
> - Quotes without [] like "Establishing on 5000" will be console outputs or parts of code (for both Proposer and Acceptors)
> - % inside quotes like "%localPort" will signify the value of localPort after %
> - Each Acceptor's output will be of format "[Acceptor-%port] %messageOutput"

### Proposer Console Commands

- `["Send"]`: Will request the default value 5
- `["Send %numberInput"]`: Will request a selected value
- `["Pause:%portNumber"]`: Will prevent the paused port from responding to Proposer's message
  - This does not prevent that same port to update data based on the output of other Acceptor nodes 
  - Only responses to Proposer is blocked
- `["Unpause:%portNumber"]`: Unpause the specified port
- `["NoDelay"]`: Eliminate delay in all Acceptor nodes
- `["Delay"]`: Restore delay in all Acceptor nodes
- `["ctest"]`: Will send "Confirmed:5:2" where 5 is the value and 2 is roundId
  - This simulates a message that Acceptor will send to each other when they receive an "Accept" message from Proposer
  - Mostly used for testing

## Notes on Possible Errors

Right now, the only expected error is when nodeNum is not correctly configured.

Before making any changes, be sure to check the .launch file you're using:
- [Paxos]: will launch 9 nodes
- [PaxosTest]: will launch 3 nodes

To modify:
- Change nodeNum value in SimpleAcceptor (line 12)
- Change nodeNum value in SimpleProposer (line 10)
- Update configurations accordingly

## Assessment Testing

### Multiple Proposers
- Currently unavailable

### Immediate Responses for node M1-M9
1. `Run` -> `Run config` -> `Paxos` -> `launch Proposer`
2. Input `["NoDelay"]` in Proposer console
3. Input `["Send %value"]`

### Delayed Responses for node M1-M9 (with M3 disconnected)
1. Keep delay (or run `["Delay"]` if previous console was on `["NoDelay"]`)
1. Manually disconnect M2 and M3 via Proposer console:
   - `["Pause:%M2_port"]`
   - `["Pause:%M3_port"]`
2. Reconnect them:
   - `["Unpause:%M2_port"]`
   - `["Unpause:%M3_port"]`

#### Adjusting Delay for specific nodes
- `Run` -> `Run config` -> `Paxos`
- Select desired node -> `Edit` -> `Arguments`
- Third argument is delay in milliseconds (blank = 10ms default)

Also if you have any ifficulties running the program, just lemme know via mail: a1876928@adelaide.edu.au 