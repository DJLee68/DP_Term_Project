package com.holub.database;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.holub.database.Database.PrintVisitor;
import com.holub.database.Database.StringValue;
import com.holub.database.Database.Value;

class VisitorTest {

	@Test
	void test() {
		StringValue stringvalue = new StringValue("DJ");
		assertEquals(stringvalue.value, "DJ");
	}

}
