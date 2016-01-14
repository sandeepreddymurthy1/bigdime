package io.bigdime.alert;

import io.bigdime.alert.InvalidDataTypeException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InvalidDataTypeExceptionTest {
	private static final Logger logger = LoggerFactory.getLogger(InvalidDataTypeExceptionTest.class);
	
	@Test
	public void invalidDataTypeExceptionDefaultConstructerTest(){
		InvalidDataTypeException invalidDataTypeException=new InvalidDataTypeException("test");
		Assert.assertEquals(invalidDataTypeException.getMessage(),"test");
	}
	
	@Test
	public void invalidDataTypeExceptionForOverloadedConstructerTest(){
		InvalidDataTypeException invalidDataTypeException=new InvalidDataTypeException("test", new Exception("test"));
		Assert.assertEquals(invalidDataTypeException.getMessage(),"test");
		Assert.assertEquals(invalidDataTypeException.getCause().getMessage(), "test");
	}
}
