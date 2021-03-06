package com.microproxy.zuul;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@IntegrationComponentScan
@EnableBinding (StudentChannels.class)
@EnableCircuitBreaker
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@EnableFeignClients
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class MicroProxyForServicesApplication {

	public static void main(String[] args) {

		SpringApplication.run(MicroProxyForServicesApplication.class, args);
	}
	

}

	@FeignClient("student-service")
	interface StudentReader {
		@RequestMapping(method = RequestMethod.GET, value = "students")
		Resources<Student> read();
	}
	
	@MessagingGateway
	interface StudentWriter{
		
		@Gateway(requestChannel="outputStudent")
		void write(String name);
	}
	
	interface StudentChannels{
		
		@Output
		MessageChannel outputStudent();
		
	}

	@RestController
	@RequestMapping("/students")
	class StudentApiGateWay {

		private final StudentReader studentReader;
		private final StudentWriter studentWriter;


		@Autowired
		public StudentApiGateWay(StudentReader studentReader,StudentWriter studentWriter) {
			this.studentReader = studentReader;
			this.studentWriter = studentWriter;
		}

		public List<String> fallback() {
			List<String> names=new ArrayList<>();
			names.add("Student Service is down");
			
			return names;
		}

		@HystrixCommand(	)
		@GetMapping(value = "/names")
		public List<String> getStudentNames() {

			Collection<Student> students = studentReader.read().getContent();
			return students.stream().map(Student::getName).collect(Collectors.toList());

		}
		
		@PostMapping(value="/student")
		public void saveStudentNames(@RequestBody Student student){
			
			this.studentWriter.write(student.getName());
			
			
			
		}

	}

