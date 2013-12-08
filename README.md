algoFinalProject
================
Develop and test heuristics for Maximum Weight Quartet Compatibility

Use
---
java MWQT <input file> <output file> <number of iterations> <-p/g> precise/greedy flag
<input file>                    File to read quartets from
<output file>                   File to write output to
<number of iterations>          Number of times to run the algorithm Larger values mean more accuracy at the cost of running time
<-p/-g precise/greedy flags>    -p triggers "precise" mode. It disables half of our greedy choices(Edge returns both neighbors reguardless)

Build
-----
use makefile in project folder
EG:
$make
  or
$make MWQT

Problem
-------
We are designing a Quartet amalgamation method (a method that constructs a supertree when all source trees are four-leaf trees)