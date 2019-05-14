# PLC-Emulator
The PLC Emulator Project was a final year project designed by Vusumuzi Tshabangu for his final year (5th year) project at the National University of Science and Technology. 

# PLC EMULATOR PROJECT                       

The PLC Emulator Project was a final year project designed by Vusumuzi Tshabangu for his final year (5th year) project at the National University of Science and Technology.

The Objectves of the project were as follows:

1. To design a software compiler that will allow the user to program a PIC microcontroller using ladder logic. 
 This software will allow the user to create a ladder diagram by means of a drag and drop interface. 
This will also involve developing a data structure for the data types to be used for the PIC microcontroller to be chosen for the PLC Emulator. 
It will also allow the user to verify the syntax of the ladder diagram where now a hexadecimal file will be generated for every compilation of the program.The software will also allow the user to download the program to the PLC emulator. 
2. To design a microcontroller operating system that will allow the PLC emulator to enter run or program mode, as well as communicate with the compiler on a PC. This will involve developing a PIC microcontroller program which will indicate the status of the emulator on an LCD as well as handle communications with the software compiler. 
3. To assemble the PLC emulator illustrated in Figure 1.2 consisting of: 
 A power supply that will supply 5V DC and 24V DC to the PLC emulator components 
An interface to inputs and outputs with LED indicators indicating the status of each input or output. The inputs will also be isolated from the PIC microcontrollers via opto-isolators. Four digital inputs, four digital outputs, one analogue input and one analogue output will currently be proposed and these will be embedded on the PLC and not on any expansion cards. No expansion input or output card feature will be proposed now. 
Two PIC microcontrollers communicating via serial bus sharing information as required by the user program and compiler on PC. 
The emulator will first be assembled and simulated on Proteus then built on a breadboard. 

Hence this project comprised of a Software and a Hardware Designs.


# LICENCE                                  


This project still remains the property of the National University of Science and Technology and is distributed for demonstration purposes only. 

# PROJECT OVERVIEW                           

The PLC Emulator was designed using the PIC18F2550 Microcontroller which has the following Key Features:

USB V2.0 Compliant
Two External Clock modes, up to 48 MHz
32 KBytes Flash memory
2 KBytes RAM

This project partially fulfilled the following aspects of the IEC 61131 Standard PLC specification [20] as the project will only have less than a year to reach conclusion:
The power supply function. 
The communication function. 
The Programming, debugging, testing and documentation function. 
A limited Interface function to sensors and actuators
