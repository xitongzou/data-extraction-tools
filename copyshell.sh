#!/bin/sh
array=`ls *.txt` 
lds=`ls -d */ | xargs -l basename`
for dir in $lds
do
	for file in $array
	do
		cp $file $dir
	done
done