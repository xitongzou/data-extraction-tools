#!/bin/sh
array=`ls *.txt`
for file in $array
do
	for num in $@
	do
		echo -e "DEBUG_ON=false\nSOURCE_FILE=$file\nPARENT_ID=$num\nTABLE_NAME=national_websites" > InsertRentalNames.properties
		java InsertRentalNames
	done
done