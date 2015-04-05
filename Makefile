
BIN_FOLDER=bin/
JAR_FILE=dist/elm327-reader.jar
MANIFEST=Manifest.txt

jar: ${BIN_FOLDER}
	rm -f ${JAR_FILE}
	jar cm ${MANIFEST} -C ./${BIN_FOLDER} . > ${JAR_FILE}
