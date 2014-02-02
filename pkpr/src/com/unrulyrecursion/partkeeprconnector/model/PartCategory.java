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
	
	// Empty constructor to aid in JSON parsing
	public PartCategory() { leaf = false; } // initialize to "not leaf mode"

	public void addChild(PartCategory kid) {
		if (kid != null) {
			children.add(kid); // Could have a validity check in this class to help here
		}
	}
	
	public ArrayList<String> getAllNames() {
		ArrayList<String> out = new ArrayList<String>();
		out.add(name);
		if (children != null) {
			for (PartCategory pc : children) {
				out.addAll(pc.getAllNames());
			}
		}
		return out;
	}
	
	@Override
	public String toString() {
		return name;
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
