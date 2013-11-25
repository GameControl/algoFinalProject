test: MWQT
	java -cp ./bin/ MWQT ./IO/r3.txt ./IO/output1.txt
#	java -cp ./bin/ MWQT ./IO/r5.txt ./IO/output1.txt
#	java -cp ./bin/ MWQT ./IO/r20.txt ./IO/output1.txt


MWQT:
	javac -Xlint -cp ./bin/ ./src/MWQT.java -d ./bin/