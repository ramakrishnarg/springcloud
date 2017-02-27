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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;


@EnableDiscoveryClient
@SpringBootApplication
public class StudentsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentsServiceApplication.class, args);
	
	}
}


@RepositoryRestResource
interface StudentRepository extends JpaRepository<Student, Serializable>{
	
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

