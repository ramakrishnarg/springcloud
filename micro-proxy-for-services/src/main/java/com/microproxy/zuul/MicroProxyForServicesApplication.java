package com.microproxy.zuul;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@EnableFeignClients
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class MicroProxyForServicesApplication {
	
	
	

	public static void main(String[] args) {
		
		SpringApplication.run(MicroProxyForServicesApplication.class, args);
	}
	
	@FeignClient("student-service")
	interface StudentReader{
		@RequestMapping(method=RequestMethod.GET, value="students")
		Resources<Student> read();
	}


	@RestController
	@RequestMapping("/students")
	class StudentApiGateWay{
		
		private final StudentReader studentReader;
		
		@Autowired
		public StudentApiGateWay(StudentReader studentReader){
			this.studentReader=studentReader;
		}
		
		@GetMapping(value="/names")
		public List<String> getStudentNames(){
			
			Collection<Student> students=studentReader.read().getContent();
			return students.stream().map(Student::getName).collect(Collectors.toList());
			
		}
		
		
		
		
	}
	
	
	
	
	
		
}
