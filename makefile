test: MWQT
	java -cp ./bin/ MWQT ./IO/r3.txt ./IO/output1.txt
#	java -cp ./bin/ MWQT ./IO/r5.txt ./IO/output1.txt
#	java -cp ./bin/ MWQT ./IO/r20.txt ./IO/output1.txt


MWQT: Quartet Tree
	javac -Xlint -cp ./bin/ ./src/MWQT.java -d ./bin/

Tree: Edge
	javac -Xlint -cp ./bin/ ./src/Tree.java -d ./bin/

Quartet:
	javac -Xlint -cp ./bin/ ./src/Quartet.java -d ./bin/

Edge:
	javac -Xlint -cp ./bin/ ./src/Edge.java -d ./bin/
