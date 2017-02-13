#!/bin/bash

#To run, you will need to have pygments installed
#Get it at http://pygments.org/download/

style=manni

cssFile=syntax.css
{
	echo "<style>";
	pygmentize -f html -S $style;
	echo "</style>"
} > $cssFile

sort files-to-print.txt -o files-to-print.txt

while read javaFile; do
	pathToFile=$(find .. -name $javaFile.java)
	if [ -z "${pathToFile// }" ]; then
		echo "Couldn't locate" $javaFile.java
		exit
	fi
	{
		echo '<link rel="stylesheet" href="'$cssFile'" />';
		grep -v import $pathToFile |
		cat -s | #no more than 1 blank line at a time
		pygmentize -l java -O style=$style,tabsize=2 -f html
	} > $javaFile.html
	xdg-open $javaFile.html 2> /dev/null || open $javaFile.html
done < files-to-print.txt