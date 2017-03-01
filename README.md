# springcloud
This repository is intended to get hands on with spring cloud.

As we are in micro services era, it is important to think of each and every thing in the way of a micro service like configurations, deployments along with code.

1) configuration-service: 

This is basically a spring cloud config server. The main purpose of this is to act as a centralized repository for different application properties. This service listens to the GIT repo where we maintain all application propertes. Whenever we change a property, we no need to restart the server since it automatically gets from git and updates its cache.


Cloud dependencies used: 

	<dependencies>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-config-server</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
  
  2) greeting-service
  This is an example service where we could test cloud config server, eurekaserver and histrix. 
  This service listens to configuration-service, and whenever a property gets changed and committed into git, configuration-   
  service updates its cache, and then greeting-service uses @RefreshScope.
  
  
  Cloud dependencies used:
  
  <dependencies>
    <!-- spring-boot-starter-actuator provides us out of the box endpoints like /heath, /env, /info, and /metrics.->
    
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
    
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-config</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

  
  

