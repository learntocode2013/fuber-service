mvn clean install -DskipTests=true
java -jar target/fuberservice-microbundle.jar
#docker build -t learntocode2013/fuberservice .
#docker run -p 8080:8080 learntocode2013/fuberservice:latest
#docker push learntocode2013/fuberservice:latest