package com.unrulyrecursion.partkeeprconnector.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Part implements Serializable {

	private String name, description, status, categoryPath, createDate, comment, pcName, storageLocName; // TODO better format for date?
	private int id, categoryId, storageLocId, attachmentCount;
	private Set<Integer> attachIds;
	private boolean needsReview;
	// private List<PartAttachments> attachments; // TODO may need/want this
	
/*
	private static final String TAG_P_NAME = "name";
	private static final String TAG_P_DESCRIPTION = "description";
	private static final String TAG_P_AVERAGE_PRICE = "averagePrice";
	private static final String TAG_P_STATUS = "status";
	private static final String TAG_P_NEEDS_REVIEW = "needsReview";
	private static final String TAG_P_CREATE_DATE = "createDate";
	private static final String TAG_P_ID = "id";
	private static final String TAG_P_STOCK_LEVEL = "stockLevel";
	private static final String TAG_P_MIN_STOCK_LEVEL = "minStockLevel";
	private static final String TAG_P_COMMENT = "comment";
	private static final String TAG_P_STORAGE_LOCATION_ID = "storageLocation_id";
	private static final String TAG_P_FOOTPRINT_ID = "footprint_id";
	private static final String TAG_P_FOOTPRINT_NAME = "footprintName";
	private static final String TAG_P_PART_CATEGORY = "category";
	private static final String TAG_P_PART_CATEGORY_NAME = "categoryName";
	private static final String TAG_P_PART_UNIT = "partUnit";
	private static final String TAG_P_PART_UNIT_NAME = "partUnitName";
	private static final String TAG_P_PART_UNIT_SHORT_NAME = "partUnitShortName";
	private static final String TAG_P_PART_UNIT_DEFAULT = "partUnitDefault";
	private static final String TAG_P_PART_CONDITION = "partCondition";
	private static final String TAG_P_ATTACHMENT_COUNT = "attachmentCount";
	private static final String TAG_P_ATTACHMENTS = "attachments";
*/
	
	public Part (String name, String desc, String status, String createDate, String comment, 
			String pcName, String storageLocName, String categoryPath, int id, int categoryId, 
			int storageLocId, int attachmentCount, Boolean needsReview, Set<Integer> attachIds) {
		this.name = name;
		this.description = desc;
		this.status = status;
		this.createDate = createDate;
		this.pcName = pcName;
		this.id = id;
		this.categoryId = categoryId;
		this.comment = comment;
		this.storageLocId = storageLocId;
		this.storageLocName = storageLocName;
		this.setCategoryPath(categoryPath);
		this.attachmentCount = attachmentCount;
		this.needsReview = needsReview;
		this.attachIds = attachIds;
	}

	// Constructor to aid in JSON parsing
	public Part () {
		this.name = "";
		this.description = "";
		this.status = "";
		this.createDate = "";
		this.pcName = "";
		this.id = 0;
		this.categoryId = 0;
		this.comment = "";
		this.storageLocId = 0;
		this.storageLocName = "";
		this.setCategoryPath("");
		this.attachmentCount = 0;
		this.needsReview = false;
		this.attachIds = new TreeSet<Integer>();
	}
	
	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getPcName() {
		return pcName;
	}

	public void setPcName(String pcName) {
		this.pcName = pcName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getStorageLocId() {
		return storageLocId;
	}

	public void setStorageLocId(int storageLocId) {
		this.storageLocId = storageLocId;
	}

	public int getAttachmentCount() {
		return attachmentCount;
	}

	public void setAttachmentCount(int attachmentCount) {
		this.attachmentCount = attachmentCount;
	}

	public boolean isNeedsReview() {
		return needsReview;
	}

	public void setNeedsReview(boolean needsReview) {
		this.needsReview = needsReview;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getStorageLocName() {
		return storageLocName;
	}

	public void setStorageLocName(String storageLocName) {
		this.storageLocName = storageLocName;
	}

	public String getCategoryPath() {
		return categoryPath;
	}

	public void setCategoryPath(String categoryPath) {
		this.categoryPath = categoryPath;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set<Integer> getAttachIds() {
		return attachIds;
	}

	public void setAttachIds(Set<Integer> attachIds) {
		this.attachIds = attachIds;
	}
	
	public void addAttachId(int i) {
		attachIds.add(i);
	}
}
