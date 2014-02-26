package com.hit.changyou;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class POIBeanTest {

	private POIBean poiBean = new POIBean("1", "yxy", "haha", 1, 127.923234, 45.245265);
	public POIBeanTest() {
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetId() {
		assertEquals(poiBean.getId(), "1");
	}

	@Test
	public void testSetId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetName() {
		assertEquals(poiBean.getName(), "yxy");
//		fail("Not yet implemented");
	}

	@Test
	public void testSetName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDescription() {
		assertEquals(poiBean.getDescription(), "haha");
	}

	@Test
	public void testSetDescription() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetType() {
		assertEquals(poiBean.getType(), "1");
	}

	@Test
	public void testSetType() {
		fail("Not yet implemented");
	}

}
