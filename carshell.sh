#!/bin/sh
array=`ls *.txt | cut -d "." -f 1`
for file in $array
do
	echo -e "DEBUG_ON=false\nSOURCE_FILE=$file.txt\nPARENT_ID=$1\nTABLE_NAME=car_mod" > InsertCarNames.properties
	java InsertCarNames
done