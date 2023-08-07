FROM eclipse-temurin:20.0.2_9-jdk
WORKDIR /usr/app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline

RUN mkdir -p model
COPY model* model

RUN if [ ! -f model/en-sent.bin ] ; then wget -O model/en-sent.bin http://opennlp.sourceforge.net/models-1.5/en-sent.bin ; fi
RUN if [ ! -f model/en-token.bin ] ; then wget -O model/en-token.bin http://opennlp.sourceforge.net/models-1.5/en-token.bin ; fi
RUN if [ ! -f model/en-tagger.bin ] ; then wget -O model/en-tagger.bin http://opennlp.sourceforge.net/models-1.5/en-pos-maxent.bin ; fi

RUN if [ ! -f model/en-lemmatizer.bin ] && [ ! -f model/en-lemmatizer.dict ] ; then wget -O model/en-lemmatizer.dict  \
    https://raw.githubusercontent.com/richardwilly98/elasticsearch-opennlp-auto-tagging/master/src/main/resources/models/en-lemmatizer.dict ; fi
RUN if [ ! -f model/en-lemmatizer.bin ] ; then java -jar \
     ~/.m2/repository/org/apache/opennlp/opennlp-tools/1.9.3/opennlp-tools-1.9.3.jar \
     LemmatizerTrainerME -model model/en-lemmatizer.bin -lang en -data model/en-lemmatizer.dict -encoding UTF-8 ; fi

COPY . .
RUN ./mvnw package

FROM eclipse-temurin:20.0.2_9-jre-jammy
WORKDIR /usr/app
COPY --from=0 /usr/app/model/*.bin model/
COPY --from=0 /usr/app/target/loreum-1.0-SNAPSHOT.jar .

ENTRYPOINT java -jar loreum-1.0-SNAPSHOT.jar
