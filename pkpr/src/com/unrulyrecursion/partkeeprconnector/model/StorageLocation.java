package com.unrulyrecursion.partkeeprconnector.model;

public class StorageLocation {

	private int id;
	private String name;
	
	public StorageLocation (int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public StorageLocation () {}
	
	public String toString() {
		return name;// + " - " + id;
	}
	
	public Boolean equals(StorageLocation s) {
		if (s.getId() == id){
			return true;
		}
		// could be strict and include exact name
		return false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
