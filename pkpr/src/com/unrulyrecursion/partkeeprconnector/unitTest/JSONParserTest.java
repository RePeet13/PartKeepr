package com.unrulyrecursion.partkeeprconnector.unitTest;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.unrulyrecursion.partkeeprconnector.model.PartCategory;
import com.unrulyrecursion.partkeeprconnector.utilities.JsonParser;

import junit.framework.TestCase;

public class JSONParserTest extends TestCase {

	@Test
	public void testLoginParse () {
		assertTrue(true); // TODO Placeholder
	}
	
	@Test
	public void testPartCategoryParse () {
		assertTrue("Testing", true);
		/* Single Leaf Part Category */
		JSONObject sLeaf;
		try {
			sLeaf = new JSONObject("{\"JSON\": \"Hello, World\"}");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			sLeaf = new JSONObject("{\"id\":19}");
//			sLeaf.put("id", 19);
//			sLeaf.put(JsonParser.TAG_PC_NAME, "Julie's Old Room");
//			sLeaf.put(JsonParser.TAG_PC_DESCRIPTION, "");
//			sLeaf.put(JsonParser.TAG_PC_LEAF, true);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		PartCategory pc = new PartCategory();
		pc.setId(19);
		pc.setName("Julie's Old Room");
		pc.setDescription("");
		pc.setLeaf(true);
//		assertNotNull(sLeaf);
//		PartCategory madePC = JsonParser.parsePartCategories(sLeaf);
//		assertTrue(pc.strictEquals(madePC));
		
	}

	@Test
	public void testPartParse () {
		assertTrue(false); // TODO Placeholder
	}
}
