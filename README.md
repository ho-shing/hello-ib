#Hello IB

This is a very simple sample program to show how to use [Java IB API](https://github.com/wilsonths/ib-api).

Currently, the program is a console program (no UI) and only does one thing (I'll add more features later):

* Provide an interactive console for querying stock price

To understand more about what the API can do, you can explore the `com.ib.controller.ApiController` class and try out different methods provided by the `ApiController` class.

#Usage
In Eclipse, Import > Gradle > Gradle Project

Build project

```
./gradlew build
```

Run program

```
cd build/libs
java -jar HelloIB-1.0.0.jar
```

In the interactive console, enter the ticker (e.g. AAPL)

```
AAPL
tickPrice - NewTickType:CLOSE price:117.81 canAutoExecute:0
tickSnapshotEnd
```

Quit the program, type

```
quit
```