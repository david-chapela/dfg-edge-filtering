#!/bin/bash

search_dir="/home/davidchapela/Documents/datasets/real-life-event-logs"

for entry in "$search_dir"/*.xes.gz
do
  filename=$(basename $entry)
  echo "Data loading run with $entry"
  java -Xmx5g -cp bpmtk.jar:lib/* au.edu.unimelb.services.ServiceProvider DFG "$entry" > ./output/"$filename".output
  echo -e "\n\n\n" >> ./output/"$filename".output

  echo "First run"
  java -Xmx5g -cp bpmtk.jar:lib/* au.edu.unimelb.services.ServiceProvider DFG "$entry" >> ./output/"$filename".output
  echo -e "\n\n\n" >> ./output/"$filename".output

  echo "Second run"
  java -Xmx5g -cp bpmtk.jar:lib/* au.edu.unimelb.services.ServiceProvider DFG "$entry" >> ./output/"$filename".output
  echo -e "\n\n\n" >> ./output/"$filename".output

  echo "Third run"
  java -Xmx5g -cp bpmtk.jar:lib/* au.edu.unimelb.services.ServiceProvider DFG "$entry" >> ./output/"$filename".output
  echo -e "\n\n\n" >> ./output/"$filename".output

  echo "Fourth run"
  java -Xmx5g -cp bpmtk.jar:lib/* au.edu.unimelb.services.ServiceProvider DFG "$entry" >> ./output/"$filename".output
  echo -e "\n\n\n" >> ./output/"$filename".output

  echo "Fiveth run"
  java -Xmx5g -cp bpmtk.jar:lib/* au.edu.unimelb.services.ServiceProvider DFG "$entry" >> ./output/"$filename".output
  echo -e "\n\n\n" >> ./output/"$filename".output

  echo "Sixth run"
  java -Xmx5g -cp bpmtk.jar:lib/* au.edu.unimelb.services.ServiceProvider DFG "$entry" >> ./output/"$filename".output
  echo -e "\n\n\n" >> ./output/"$filename".output

  echo "Seventh run"
  java -Xmx5g -cp bpmtk.jar:lib/* au.edu.unimelb.services.ServiceProvider DFG "$entry" >> ./output/"$filename".output
  echo -e "\n\n\n" >> ./output/"$filename".output

  echo "Eighth run"
  java -Xmx5g -cp bpmtk.jar:lib/* au.edu.unimelb.services.ServiceProvider DFG "$entry" >> ./output/"$filename".output
  echo -e "\n\n\n" >> ./output/"$filename".output

  echo "Nineth run"
  java -Xmx5g -cp bpmtk.jar:lib/* au.edu.unimelb.services.ServiceProvider DFG "$entry" >> ./output/"$filename".output
  echo -e "\n\n\n" >> ./output/"$filename".output

  echo "Tenth run"
  java -Xmx5g -cp bpmtk.jar:lib/* au.edu.unimelb.services.ServiceProvider DFG "$entry" >> ./output/"$filename".output
done
