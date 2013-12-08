RUN=1
while true;
do
  java -cp ./bin/ MWQT ./IO/r3.txt ./IO/output-r3/$RUN-50-g.txt 50 -g
  java -cp ./bin/ MWQT ./IO/r5.txt ./IO/output-r5/$RUN-50-g.txt 50 -g
  java -cp ./bin/ MWQT ./IO/r20.txt ./IO/output-r20/$RUN-50-g.txt 50 -g
  java -cp ./bin/ MWQT ./IO/r3.txt ./IO/output-r3/$RUN-50-p.txt 50 -p
  java -cp ./bin/ MWQT ./IO/r5.txt ./IO/output-r5/$RUN-50-p.txt 50 -p
  java -cp ./bin/ MWQT ./IO/r20.txt ./IO/output-r20/$RUN-50-p.txt 50 -p
  RUN=$[RUN+1]
done
