package com.students;

import java.io.Serializable;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@IntegrationComponentScan
@EnableBinding(StudentChannels.class)
@EnableDiscoveryClient
@SpringBootApplication
public class StudentsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentsServiceApplication.class, args);
	
	}
}

interface StudentChannels{
	
	@Input
	SubscribableChannel input();
	
}


@RepositoryRestResource
interface StudentRepository extends JpaRepository<Student, Serializable>{
	
}


@MessageEndpoint
class StudentProcessor{
	
	private StudentRepository studentRepository;
	
	public StudentProcessor(StudentRepository studentRepository){
		this.studentRepository=studentRepository;
	}
	
	@ServiceActivator(inputChannel="input")
	public void onNewStudent(String name){
		studentRepository.save(new Student(name));
	}
	
}




@Component
class SampleCLR implements CommandLineRunner{
	
	private final StudentRepository studentRepository;
	
	@Autowired
	public SampleCLR(StudentRepository studentRepository) {
		this.studentRepository=studentRepository;
	}

	@Override
	public void run(String... arg0) throws Exception {
		Stream.of("Ram","Anu","Siri","Krish","Reddi").forEach(name->studentRepository.save(new Student(name)));
		studentRepository.findAll().forEach(System.out::println);
	}
	
}



@Entity
class Student implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4509966800694943269L;

	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	public Student(){
	}
	
	public Student(String name){
		this.name=name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + "]";
	}
	
	
	
}

