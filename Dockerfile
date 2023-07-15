FROM openjdk:11
EXPOSE 8888
ADD futuresTradingSystem-0.0.1-SNAPSHOT.jar /root/java/app.jar
ENTRYPOINT ["nohup","java","-jar","/root/java/app.jar","--spring.profiles.active=pro","&"]