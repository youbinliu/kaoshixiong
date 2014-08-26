package com.kaoshixiong.datamining;

import org.neo4j.graphdb.Node;

public class PersonNode {
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	private String id;
	
	private String name;
	
	private String job;
	
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String JOB = "job";
	
	public static Node Create(String id,String name,String job){
		NeoDatabaseFactory factory = NeoDatabaseFactory.getInstance();
		Node one = factory.service().createNode();
		
		one.setProperty(ID, id);
		one.setProperty(NAME,name);
		one.setProperty(JOB, job);
		
		factory.indexs().add(one, ID, id);
		
		return one;
	}
	
	public static PersonNode From(Node node){
		PersonNode person = new PersonNode();
		person.setId((String)node.getProperty(ID));
		person.setJob((String)node.getProperty(JOB));
		person.setName((String)node.getProperty(NAME));
		return person;
	}
	
	public String toString(){
		return String.format("[id:%s] [name:%s] [job:%s]", getId(),getName(),getJob());
	}
	
}
