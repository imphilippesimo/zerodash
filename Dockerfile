# Use an official java runtime as a parent image
FROM openjdk:8-jre-alpine


# Define environment variable
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS\
    SLEEP=10\
    JAVA_OPTS="-Xmx1024m -Xms256m"\
    DB_HOST=52.71.73.60\
    DB_PORT=3307\
    DB_NAME=zerodash\
    DB_USER=zerodash\
    DB_PASSWORD=zerodash\
    SPRING_ACTIVE_PROFILES=prod

# Add a zerodash to run our application so that it doesn't need to run as root
#RUN adduser -D -s /bin/sh zerodash


# Set the current working directory to /home/zerodash
#WORKDIR /home/zerodash

#copy the app to be deployed in the container
ADD target/zerodash.jar zerodash.jar

#create a file entrypoint-dos.sh and put the project entrypoint.sh content in it
ADD entrypoint.sh entrypoint-dos.sh

#Get rid of windows characters and put the result in a new entrypoint.sh in the container
RUN sed -e 's/\r$//' entrypoint-dos.sh > entrypoint.sh

#set the file as an executable and set zerodash as the owner
RUN chmod 755 entrypoint.sh
#&& chown zerodash:zerodash entrypoint.sh

#set the user to use when running the image to zerodash
#USER zerodash

# Make port 9010 available to the world outside this container
EXPOSE 9010


ENTRYPOINT ["./entrypoint.sh"]

