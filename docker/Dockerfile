FROM ubuntu:16.10

# Install Java 8
RUN apt-get update
RUN apt-get install software-properties-common -y
RUN add-apt-repository ppa:webupd8team/java -y
RUN apt-get update
RUN echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
RUN apt-get install oracle-java8-installer -y --force-yes
RUN apt-get install oracle-java8-set-default

#Install Sci tool
RUN wget http://builds.scitools.com/all_builds/b844/Understand/Understand-4.0.844-Linux-64bit.tgz && tar -zxvf Understand-4.0.844-Linux-64bit.tgz
RUN echo "Server: scitools-license.devfactory.com 00000000 9000" > scitools/conf/license/locallicense.dat
ENV PATH="/scitools/bin/linux64:${PATH}"

#configure libraries
RUN apt-get install libxrender1 -y
RUN apt-get install libxi6 libgconf-2-4 -y
RUN apt-get install libxtst6 -y

#Configure env
RUN mkdir app
COPY DeadCodeDetector-0.0.1-SNAPSHOT.jar /app/server.jar
#COPY python /app/python
#RUN ln /usr/bin/python3.5 /usr/bin/python
#COPY temp /app/temp

EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java -jar /app/server.jar" ]
