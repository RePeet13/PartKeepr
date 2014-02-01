package com.unrulyrecursion.partkeeprconnector.model;

public class Part {

	private String name, description, status, createDate, pcName, storageLocName; // TODO better format for date?
	private int id, categoryId, storageLocId, attachmentCount;
	private boolean needsReview;
	
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
	
	public Part (String name, String desc, String status, String createDate, 
			String pcName, String storageLocName, int id, int categoryId, 
			int storageLocId, int attachmentCount, Boolean needsReview) {
		this.name = name;
		this.setDescription(desc);
		this.status = status;
		this.createDate = createDate;
		this.pcName = pcName;
		this.id = id;
		this.categoryId = categoryId;
		this.storageLocId = storageLocId;
		this.setStorageLocName(storageLocName);
		this.attachmentCount = attachmentCount;
		this.needsReview = needsReview;
	}

	// Empty constructor to aid in JSON parsing
	public Part () { }
	
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
}
