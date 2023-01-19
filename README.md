# RobotAPI
Usable API for RV-3SB in room F113
#### YOU DO NOT HAVE TO CHANGE THE JAVA CLASSES IN PACKAGES!

## USAGE
### Init the robot
To init the Robot you must use a `RobotBuilder` which you must initialize with a host, and a port. As of 2023 the host is `192.168.1.223` and the port `10001`.  
Init the robot with `RV3SB robot = new RobotBuilder(HOST, PORT). [...] .build();`  
The [...] stands for more actions that you can / have to add to the init process like `.setSafePosition([...])` or `.setSpeed([...])`.  

## Usage of programs
The interface `RunnableProgram` allows you to use the `RV-3SB` itself to run programs.  
Implement your program in a separate class and run it by passing either the controls or the robot.

### Usage of subprograms
You can implement nested instances of `RunnableProgram` used for subprograms or other ISRs (which you would handle in a thread)  
Make sure to use a fitting name for all your programs!

## What will happen on startup?
The API will initialize the robot accordingly to your definitions of the Builder class and start up in a secure way.

## Run in IntelliJ in Debug-Mode
In order for you to get more information about what´s really happening behind the scenes, run debug mode in IntelliJ
and get access to all logs