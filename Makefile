
BIN_FOLDER=bin/
JAR_FOLDER=dist/
JAR_NAME=elm327-reader.jar
JAR_FILE=${JAR_FOLDER}${JAR_NAME}
MANIFEST=Manifest.txt

jar: ${BIN_FOLDER}
	mkdir -p dist/
	jar cm ${MANIFEST} -C ./${BIN_FOLDER} . > ${JAR_FILE}

${BIN_FOLDER}:
	mkdir -p ${BIN_FOLDER}
	javac -d ${BIN_FOLDER} src/elm327/reader/*.java

clean:
	rm -rf ${BIN_FOLDER} ${JAR_FILE}

