
BIN_FOLDER=bin/
JAR_FILE=dist/elm327-reader.jar
MANIFEST=Manifest.txt

jar: ${BIN_FOLDER}
	rm -f ${JAR_FILE}
	jar -c -v -m ${MANIFEST} -f ${JAR_FILE} -C ${BIN_FOLDER} . 