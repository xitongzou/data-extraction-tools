#!/bin/sh
array=`ls *.txt`
i=0
for file in $array
do
	while [ $i -le 341 ]
	do
		echo -e "DEBUG_ON=false\nSOURCE_FILE=$file\nPARENT_ID=$i\nTABLE_NAME=national_websites" > InsertRentalNames.properties
		java InsertRentalNames
		i=`expr $i + 1`
	done
i=0
done