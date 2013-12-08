cTest: MWQT
	java -cp ./bin/ MWQT ./IO/c1.txt ./IO/output-c1-g.txt 1 -g

test: MWQT
	java -cp ./bin/ MWQT ./IO/r3.txt ./IO/output-r3-g.txt 1 -g
	java -cp ./bin/ MWQT ./IO/r5.txt ./IO/output-r5-g.txt 1 -g
	java -cp ./bin/ MWQT ./IO/r20.txt ./IO/output-r20-g.txt 1 -g

massTest: MWQT
	java -cp ./bin/ MWQT ./IO/r3.txt ./IO/output-r3-g.txt 50 -g
	java -cp ./bin/ MWQT ./IO/r5.txt ./IO/output-r5-g.txt 50 -g
	java -cp ./bin/ MWQT ./IO/r20.txt ./IO/output-r20-g.txt 50 -g
	java -cp ./bin/ MWQT ./IO/r3.txt ./IO/output-r3-p.txt 50 -p
	java -cp ./bin/ MWQT ./IO/r5.txt ./IO/output-r5-p.txt 50 -p
	java -cp ./bin/ MWQT ./IO/r20.txt ./IO/output-r20-p.txt 50 -p


MWQT: Quartet Tree
	javac -Xlint -cp ./bin/ ./src/MWQT.java -d ./bin/

Tree: Edge
	javac -Xlint -cp ./bin/ ./src/Tree.java -d ./bin/

Quartet:
	javac -Xlint -cp ./bin/ ./src/Quartet.java -d ./bin/

Edge: 
	javac -Xlint -cp ./bin/ ./src/Edge.java -d ./bin/
