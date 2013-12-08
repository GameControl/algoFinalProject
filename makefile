MWQT: Quartet Tree
	javac -Xlint -cp ./bin/ ./src/MWQT.java -d ./bin/

Tree: Edge
	javac -Xlint -cp ./bin/ ./src/Tree.java -d ./bin/

Quartet:
	javac -Xlint -cp ./bin/ ./src/Quartet.java -d ./bin/

Edge: 
	javac -Xlint -cp ./bin/ ./src/Edge.java -d ./bin/

cTest: MWQT
	java -cp ./bin/ MWQT ./IO/c1.txt ./IO/output-c1-100-g.txt 100 -g

test: MWQT
	java -cp ./bin/ MWQT ./IO/r3.txt ./IO/output-r3-1-g.txt 1 -g
	java -cp ./bin/ MWQT ./IO/r5.txt ./IO/output-r5-1-g.txt 1 -g
	java -cp ./bin/ MWQT ./IO/r20.txt ./IO/output-r20-1-g.txt 1 -g

massTest: MWQT
	java -cp ./bin/ MWQT ./IO/r3.txt ./IO/output-r3-50-g.txt 50 -g
	java -cp ./bin/ MWQT ./IO/r5.txt ./IO/output-r5-50-g.txt 50 -g
	java -cp ./bin/ MWQT ./IO/r20.txt ./IO/output-r20-50-g.txt 50 -g
	java -cp ./bin/ MWQT ./IO/r3.txt ./IO/output-r3-50-p.txt 50 -p
	java -cp ./bin/ MWQT ./IO/r5.txt ./IO/output-r5-50-p.txt 50 -p
	java -cp ./bin/ MWQT ./IO/r20.txt ./IO/output-r20-50-p.txt 50 -p

giantTest: MWQT
	java -cp ./bin/ MWQT ./IO/r3.txt ./IO/output-r3-100-g.txt 100 -g
	java -cp ./bin/ MWQT ./IO/r5.txt ./IO/output-r5-100-g.txt 100 -g
	java -cp ./bin/ MWQT ./IO/r20.txt ./IO/output-r20-100-g.txt 100 -g
	java -cp ./bin/ MWQT ./IO/r3.txt ./IO/output-r3-100-p.txt 100 -p
	java -cp ./bin/ MWQT ./IO/r5.txt ./IO/output-r5-100-p.txt 100 -p
	java -cp ./bin/ MWQT ./IO/r20.txt ./IO/output-r20-100-p.txt 100 -p
