FROM eclipse-temurin:22-jammy AS builder
WORKDIR /opt/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

RUN mkdir -p /root/.m2 && mkdir /root/.m2/repository
COPY settings.xml /root/.m2

RUN ./mvnw dependency:go-offline
COPY ./src ./src
ENV SPRING_PROFILES_ACTIVE=deploy
RUN ./mvnw clean install #-DskipTests


FROM eclipse-temurin:22-jammy

RUN addgroup evs2ops; adduser  --ingroup evs2ops --disabled-password evs2ops1
USER evs2ops1

WORKDIR /opt/app
EXPOSE 8111
COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=deploy", "-jar", "/opt/app/*.jar" ]


#Firt time env setup:
#Authenticate the local Docker client to our AWS ECR
#-> aws ecr get-login-password --region ap-southeast-1 | docker login --username AWS --password-stdin 904734893309.dkr.ecr.ap-southeast-1.amazonaws.com
#if error: Docker: Got permission denied while trying to connect to the Docker daemon socket at unix:///var/run/docker.sock
#need to add user to docker group
#-> sudo usermod -a -G docker your_user_name
#-> sudo reboot
#if error: aws can't find credentials
#need to configure aws profile
#-> aws configure --profile evs2deploy
#add --profile evs2deploy to the command
#End of Firt time env setup
#build docker image
#A-> docker build -t oresvc .
#or B-> docker build -t 904734893309.dkr.ecr.ap-southeast-1.amazonaws.com/oresvc:test .
#if A, then need to tag the image so we can push the image to this repository:
#-> docker tag oresvc 904734893309.dkr.ecr.ap-southeast-1.amazonaws.com/oresvc:test
#login to AWS ECR
#-> aws ecr get-login-password --region ap-southeast-1 --profile evs2deploy | docker login --username AWS --password-stdin 904734893309.dkr.ecr.ap-southeast-1.amazonaws.com
#push the image to the AWS ECR
#-> docker push 904734893309.dkr.ecr.ap-southeast-1.amazonaws.com/oresvc:test
#if get timeout error like this:
#The push refers to repository [904734893309.dkr.ecr.ap-southeast-1.amazonaws.com/oresvc]
#e4a44d44b8a6: Retrying in 1 second
#2029cd6ea185: Retrying in 1 second
#5cb8b6907b72: Retrying in 1 second
#570d93062364: Retrying in 1 second
#6b1a55bf1a8b: Retrying in 1 second
#a75d61c24821: Waiting
#202fe64c3ce3: Waiting
#EOF
#Reason: The repository with name 'oresvc' does not exist in the registry with id '904734893309'
#check to confirm:
#-> aws ecr describe-repositories --repository-names oresvc
#if not exist, create it:
#-> aws ecr create-repository --repository-name oresvc

#After pushing the updated image when the service is running,
#forces the task definition to be re-evaluated and the new container image to be pulled.
#-> aws ecs update-service --cluster evs2cluster --service oresvc --force-new-deployment
