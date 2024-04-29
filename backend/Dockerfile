#
# Build stage
#
FROM maven:3.9.6 as builder
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Run stage
#
FROM eclipse-temurin:21-jre as runtime
ARG uid=1001

RUN apt-get update \
    && apt-get install -y python3-pip
RUN pip install cornac Flask gunicorn
#RUN pip install --default-timeout=1000 torch
#RUN apk add --update py3-pip
#RUN apk add pipx
#RUN pipx install cornac Flask gunicorn

RUN groupadd --gid ${uid} -r app \
 && useradd --uid ${uid} -r -g app -m app \
 && mkdir /app \
 && chown -R app:app /app

COPY --from=builder /home/app/target/*.jar /app/app.jar
COPY cornac /app

USER ${uid}
WORKDIR /app

ENTRYPOINT ["java","-jar","app.jar"]