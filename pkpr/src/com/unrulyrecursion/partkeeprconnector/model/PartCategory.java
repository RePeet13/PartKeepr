package com.unrulyrecursion.partkeeprconnector.model;

import java.util.ArrayList;

public class PartCategory {

	private ArrayList<PartCategory> children;
	private int id, parentId;
	private String name, description;
	private Boolean leaf, expanded;
	
	public PartCategory(int id, String name, String description, Boolean leaf, Boolean expanded) {
		this.setId(id);
		this.setName(name);
		this.setDescription(description);
		this.setLeaf(leaf);
		this.setExpanded(expanded);
		children = new ArrayList<PartCategory>();
	}
	
	// Empty constructor to aid in JSON parsing
	public PartCategory() { leaf = false; children = new ArrayList<PartCategory>(); } // initialize to "not leaf mode"

	public void addChild(PartCategory kid) {
		if (kid != null) {
			kid.setParentId(id);
			children.add(kid); // Could have a validity check in this class to help here
		}
	}
	
	public void clearChildren() {
		children.clear();
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
	
	public ArrayList<PartCategory> flatten() {
		ArrayList<PartCategory> out = new ArrayList<PartCategory>();
		out.add(this);
		if (!leaf) {
			for (PartCategory kid : children) {
				out.addAll(kid.flatten());
			}
		}
		return out;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public Boolean equals(PartCategory in) {
		if (this.id != in.getId()) { // Biggest factor
			return false;
		}
		if (this.parentId != in.getParentId()) { // Big factor
			return false;
		}
		
		return true;
	}
	
	public Boolean strictEquals(PartCategory in) {
		if (!equals(in)) {
			return false;
		}
		if (!name.equalsIgnoreCase(in.getName())) {
			return false;
		}
		if (!description.equalsIgnoreCase(in.getDescription())) {
			return false;
		}
		if (leaf != in.getLeaf()) {
			return false;
		}
		if (children.size() != in.getChildren().size()) {
			return false;
		}
		return true;
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
	
	public ArrayList<PartCategory> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<PartCategory> children) {
		this.children = children;
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

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
}
