algoFinalProject
================
Develop and test heuristics for Maximum Weight Quartet Compatibility

FROM TANDY:
The basic problem is that you have a set of quartet trees (at most one tree per quartet) with weights. You want to find a tree on the full set of leaves that agrees with a maximum weight subset of these quartet trees.


I have asked the T.A. to create some inputs.

The basic idea is:
Each line will be a separate quartet tree, in Newick format, with a weight that is non-negative.

For example, the following could be an input:

(a,(b,(c,d))) 4

(a,(c,(d,e))) 0.5

(b,(c,(d,e))) 2.7

((a,e),(b,c)) 0.4



Use
---
how to use the code

Build
-----
makefile in project folder

Problem
-------
We are designing a Quartet amalgamation method (a method that constructs a supertree when all source trees are four-leaf trees)

input
-----
1 example input

output
------
1 example output

Process
-------
What we did and why

Performance
-----------
How they preform

Submission
----------
A PDF for your final project is due on November 27, in class, and a short presentation of the project in class on December 2 or December 4.
