FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/instainsta.jar /instainsta/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/instainsta/app.jar"]
