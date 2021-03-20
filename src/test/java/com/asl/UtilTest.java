package com.asl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;

/**
 * @author Zubayer Ahamed
 * @since Mar 20, 2021
 */
public class UtilTest {

	@Test
	void someTest() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		
		System.out.println(sdf.format(new Date()));
		
		
	}
}
