package com.unrulyrecursion.partkeeprconnector.model;

import java.util.ArrayList;

public class PartCategory {

	private ArrayList<PartCategory> children;
	private int id;
	private String name, description;
	private Boolean leaf, expanded;
	
	public PartCategory(int id, String name, String description, Boolean leaf, Boolean expanded) {
		this.setId(id);
		this.setName(name);
		this.setDescription(description);
		this.setLeaf(leaf);
		this.setExpanded(expanded);
	}
	
	public PartCategory() {
		
	}

	public void addChild(PartCategory kid) {
		if (kid != null) {
			children.add(kid); // Could have a validity check in this class to help here
		}
	}
	
	public ArrayList<String> getAllNames() {
		ArrayList<String> out = new ArrayList<String>();
		out.add(name);
		for (PartCategory pc : children) {
			out.addAll(pc.getAllNames());
		}
		return out;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	public Boolean getExpanded() {
		return expanded;
	}

	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}
}
