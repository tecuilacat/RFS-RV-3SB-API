# RobotAPI
Usable API for RV-3SB in room F113
### YOU DO NOT HAVE TO CHANGE THE JAVA CLASSES IN PACKAGES!

## USAGE
### Init the robot
To init the Robot you must use a `RobotBuilder` which you must initialize with a host, and a port. As of 2023 the host is `192.168.1.223` and the port `10001`.  
Init the robot with `RV3SB robot = new RobotBuilder(HOST, PORT). [...] .build();`  
The [...] stands for more actions that you can / have to add to the init process like `.setSafePosition([...])` or `.setSpeed([...])`.  

### Write your program(s)
