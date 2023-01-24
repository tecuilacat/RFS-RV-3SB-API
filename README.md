# Robot API
Usable API for RV-3SB in room F113
#### YOU DO NOT HAVE TO CHANGE THE JAVA CLASSES IN PACKAGES!

## FEATURES
- Easy init of a robot
- Fast and easy way to move the robot to positions
- Full control of the robot
- Easy way to write programs and nested subprograms / -routines

## USAGE
### Init the robot
To init the Robot you must use a `RobotBuilder` which you must initialize with a host, and a port. As of 2023 the host is `192.168.1.223` and the port `10001`.  
Init the robot with `RV3SB robot = new RobotBuilder(HOST, PORT). [...] .build();`  
The [...] stands for more actions that you can / have to add to the init process like `.setSafePosition([...])` or `.setSpeed([...])`.  
Also do not forget to define a `CommandSet` which tells the robot which commands to use. If this API does not include the fitting commands, implement a new class 
and implement the `CommandSet` interface.

## Usage of programs
The interface `RunnableProgram` allows you to use the `RV-3SB` itself to run programs.  
Implement your program in a separate class and run it by passing either the controls or the robot.

### Usage of subprograms
You can implement nested instances of `RunnableProgram` used for subprograms or other ISRs (which you could handle in a thread)  
Make sure to use a fitting name for all your programs!

## Usage of `Position`
NEVER assign a position another position. Always copy a variable like the following: `PNEW = POLD.copy();`  
There are two methods that get interesting when moving the robot to a position without overwriting the position. Both will return 
the position you want, but afterwards you can use that position the way it was:  
- `alterZ(int value)`:  
    - In some programs in MelfaBasic4 you will encounter lines like `MOV PSAFE, -50`
    - The method works just like that. Take your position and alter the z-index by -50
    - API-Command: `movToPosition(PSAFE.alterZ(-50.0), false);` (the false stands for safeTravel, which you would not need in this case)
    - **_Tipp_**: _The method movToPosition does exactly that for you with the value -50. If you want another value, you have to either alter the method itself or do it manually as in the line above_
- `alterAbsoluteZ(double value)`:
    - Sometimes you want to use a certain position, but with another z-index
    - In that case you can pass in that absolut z-value with the other parameters remaining as they were
    - API-Command: `mvsToPosition(PCURR.absoluteZ(300.0));`
    
In both cases the z-values of PSAFE and PCURR stay the same after those method calls

## What will happen on startup?
The API will initialize the robot accordingly to your definitions of the Builder class and start up in a secure way.

## Running in IntelliJ in Debug-Mode
In order for you to get more information about what´s really happening behind the scenes, run debug mode in IntelliJ
and get access to more detailed logs.

**_&copy; Oliver Steck (01 | 2023)_**