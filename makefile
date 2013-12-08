test: MWQT
	java -cp ./bin/ MWQT ./IO/c1.txt ./IO/output-c1.txt 1 -g
	java -cp ./bin/ MWQT ./IO/r3.txt ./IO/output-r3.txt 1 -g
#	diff ./IO/expected-r3.txt ./IO/output-r3.txt
	java -cp ./bin/ MWQT ./IO/r5.txt ./IO/output-r5.txt 1 -g
#	diff ./IO/expected-r5.txt ./IO/output-r5.txt
	java -cp ./bin/ MWQT ./IO/r20.txt ./IO/output-r20.txt 1 -g
#	diff ./IO/expected-r20.txt ./IO/output-r20.txt

precision: MWQT
	java -cp ./bin/ MWQT ./IO/c1.txt ./IO/output-c1-p.txt 100 -p
	java -cp ./bin/ MWQT ./IO/c1.txt ./IO/output-c1-g.txt 100 -g

MWQT: Quartet Tree
	javac -Xlint -cp ./bin/ ./src/MWQT.java -d ./bin/

Tree: Edge
	javac -Xlint -cp ./bin/ ./src/Tree.java -d ./bin/

Quartet:
	javac -Xlint -cp ./bin/ ./src/Quartet.java -d ./bin/

Edge: 
	javac -Xlint -cp ./bin/ ./src/Edge.java -d ./bin/
