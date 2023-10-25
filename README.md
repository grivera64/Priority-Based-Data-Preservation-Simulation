# Priority-Based-Data-Preservation-Simulation

---
A simulation for testing data preservation of base station-less networks (BSNs) while trying to maximize profit.

## Table of Contents

---
- [About](#about)
- [Setup](#setup)
- [Example](#example)
  - [Terminal Output](#terminal-output)
- [Authors](#authors)

## About

---
This data preservation simulation uses the project [grivera64/Max-Profit-Data-Preservation-Simulation](https://github.com/grivera64/Max-Profit-Data-Preservation-Simulation)
to generate a suite of base station-less networks (BSNs) to apply ILP-based solutions and compare with previous solutions like Goldberg's CS2 program.

This simulation outputs the energy consumption of using the above algorithms.

## Setup

---

### Dependencies

- JDK 17 or newer ([Latest JDK from Oracle](https://www.oracle.com/java/technologies/downloads/))
- CS2 executable ([Installation and Setup Instructions](./CS2_SETUP.md))
  - Use `make` to build the binaries from the repository above and save the executable somewhere and remeber its path.
  > Note: We will refer to the path to the CS2 executable as `%PATH_TO_CS2%`.
- Guava ([Latest Guava Instructions](https://developers.google.com/optimization/install/java))
  - Dependencies:
    - JNA ([GitHub Link](https://github.com/java-native-access/jna)) 
    - Google Protocol Buffers ([GitHub Link](https://github.com/protocolbuffers/protobuf))

### 1. Clone the Repository

Open a command line or terminal instance and enter the following command:
```sh
git clone https://github.com/grivera64/Max-Profit-Data-Preservation-Simulation.git
```

You can also download the repository as a zip file directly
from GitHub [here](https://github.com/grivera64/Data-Preservation-Simulation/archive/refs/heads/main.zip) and unzip it.

### 2. Change directories into the source folder.

```sh
cd Priority-Based-Data-Preservation-Simulation
cd src
```

### 3. Compile using javac

Windows:
```batch
javac -p . -cp ".;%PATH_TO_GUAVA%/*;%PATH_TO_JNA%/*;%PATH_TO_PROTOBUF%/*" *.java -d ../bin
```

Mac/Linux:
```sh
javac -p "." -cp ".:$PATH_TO_GUAVA/*:$PATH_TO_JNA/*:$PATH_TO_PROTOBUF/*" **.java -d ../bin
```

### 4. Change directories into the binaries folder

```sh
cd ..
cd bin
```

### 5. Run the program

Windows:
```bat
java -p . -cp ".;%PATH_TO_GUAVA%/*;%PATH_TO_JNA%/*;%PATH_TO_PROTOBUF%/*" RunModelTests
```

Mac/Linux:
```sh
java -p "." -cp ".:$PATH_TO_GUAVA/*:$PATH_TO_JNA/*:$PATH_TO_PROTOBUF/*" RunModelTests
```

## Example

---
### Terminal output

```txt
Welcome to the Max Profit Data Preservation Simulator!
===========================================

Please enter an option: (G)enerate/(F)ile/(Q)uit:
> F
Please enter the file name:
F > figure_3_sensor_network.sn

Where is your installation of cs2.exe located?
(".") > $PATH_TO_CS2

Running models...
=================
Greedy:
Cost: 6412 µJ
Profit: 9452 µJ

CS2 (Optimal):
Saved flow network in file "cs2_tmp_20230518164514.inp"!
Cost: 6406 µJ
Profit: 9458 µJ

```

## Related Projects

---
- Sensor Generator with Max Profit ([grivera64/Sensor-Generator-with-Max-Profit](https://github.com/grivera64/Sensor-Generator-with-Max-Profit))
  - Sensor Network generator visualizer for CS2 Min-Cost Flow input for maximizing profit in data preservation.
  - By Giovanni Rivera ([@grivera64](https://github.com/grivera64))

- Sensor Generator with MCF ([grivera64/Sensor-Generator-with-MCF](https://github.com/grivera64/Sensor-Generator-with-MCF))
  - Original Sensor Network generator and visualizer for CS2 Min-Cost Flow input that we forked for [grivera64/Sensor-Generator-with-Max-Profit](https://github.com/grivera64/Sensor-Generator-with-Max-Profit).
  - By Giovanni Rivera ([@grivera64](https://github.com/grivera64))

- Data Preservation Simulation ([grivera64/Data-Preservation-Simulation](https://github.com/grivera64/Data-Preservation-Simulation))
  - A simulation for testing data preservation of base station-less networks (BSNs).
  - By Giovanni Rivera ([@grivera64](https://github.com/grivera64))

## Authors

---
- Giovanni Rivera ([@grivera64](https://github.com/grivera64))
- Christopher Gonzalez ([@chrisagonza97](https://github.com/chrisagonza97))
