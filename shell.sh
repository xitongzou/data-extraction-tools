#!/bin/sh
array=`ls *.txt | cut -d "." -f 1`
for file in $array
do
count=0
	while read line
	do
	if [ $count -eq 2 ] 
	then
		place="$line"
		mkdir -p $place
		echo "$place"
	fi
	let count+=1
	done < $file.txt
	mv $file.txt $place
done