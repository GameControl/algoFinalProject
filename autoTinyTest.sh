RUN=1
while true;
do
  java -cp ./bin/ MWQT ./IO/r3.txt ./IO/output-r3/$RUN-10-g.txt 10 -g
  java -cp ./bin/ MWQT ./IO/r5.txt ./IO/output-r5/$RUN-10-g.txt 10 -g
  java -cp ./bin/ MWQT ./IO/r20.txt ./IO/output-r20/$RUN-10-g.txt 10 -g
  java -cp ./bin/ MWQT ./IO/r3.txt ./IO/output-r3/$RUN-10-p.txt 10 -p
  java -cp ./bin/ MWQT ./IO/r5.txt ./IO/output-r5/$RUN-10-p.txt 10 -p
  java -cp ./bin/ MWQT ./IO/r20.txt ./IO/output-r20/$RUN-10-p.txt 10 -p
  RUN=$[RUN+1]
done