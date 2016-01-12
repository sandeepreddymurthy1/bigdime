/**
 * Copyright (C) 2015 Stubhub.
 */
package io.bigdime.validation;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.bigdime.core.ActionEvent;
import io.bigdime.core.constants.ActionEventHeaderConstants;
import io.bigdime.core.validation.DataValidationException;
import io.bigdime.core.validation.ValidationResponse.ValidationResult;
import io.bigdime.libs.hdfs.WebHdfs;
import static org.mockito.Mockito.*;

public class RawChecksumValidatorTest {

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void validateNullHostTest() throws DataValidationException {
		RawChecksumValidator rawChecksumValidator = new RawChecksumValidator();
		ActionEvent mockActionEvent = Mockito.mock(ActionEvent.class);
		@SuppressWarnings("unchecked")
		Map<String, String> mockMap = Mockito.mock(Map.class);
		when(mockActionEvent.getHeaders()).thenReturn(mockMap);
		when(mockMap.get(anyString())).thenReturn("");
		rawChecksumValidator.validate(mockActionEvent);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void validateNullPortNumberTest() throws DataValidationException {
		RawChecksumValidator rawChecksumValidator = new RawChecksumValidator();
		ActionEvent mockActionEvent = Mockito.mock(ActionEvent.class);
		@SuppressWarnings("unchecked")
		Map<String, String> mockMap = Mockito.mock(Map.class);
		when(mockActionEvent.getHeaders()).thenReturn(mockMap);
		when(mockMap.get(anyString())).thenReturn("host").thenReturn(null);
		rawChecksumValidator.validate(mockActionEvent);
	}
	
	@Test
	public void notReadyToValidateTest() throws DataValidationException{
		RawChecksumValidator rawChecksumValidator = new RawChecksumValidator();
		ActionEvent mockActionEvent = Mockito.mock(ActionEvent.class);
		Map<String, String> headers = new HashMap<>();
		headers.put(ActionEventHeaderConstants.HOST_NAMES, "host");
		headers.put(ActionEventHeaderConstants.PORT, "123");
		headers.put(ActionEventHeaderConstants.READ_COMPLETE, "false");
		when(mockActionEvent.getHeaders()).thenReturn(headers);
		Assert.assertEquals(rawChecksumValidator.validate(mockActionEvent).getValidationResult(),
				ValidationResult.NOT_READY);
	}
	
	@Test(expectedExceptions = NumberFormatException.class)
	public void validatePortNumberFormatTest() throws DataValidationException {
		RawChecksumValidator rawChecksumValidator = new RawChecksumValidator();
		ActionEvent mockActionEvent = Mockito.mock(ActionEvent.class);
		Map<String, String> headers = new HashMap<>();
		headers.put(ActionEventHeaderConstants.HOST_NAMES, "host");
		headers.put(ActionEventHeaderConstants.PORT, "hello");
		headers.put(ActionEventHeaderConstants.READ_COMPLETE, "true");
		when(mockActionEvent.getHeaders()).thenReturn(headers);
		rawChecksumValidator.validate(mockActionEvent);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void validateNullHdfsPathTest() throws DataValidationException {
		RawChecksumValidator rawChecksumValidator = new RawChecksumValidator();
		ActionEvent mockActionEvent = Mockito.mock(ActionEvent.class);
		Map<String, String> mockMap = new HashMap<>();

		mockMap.put(ActionEventHeaderConstants.HOST_NAMES, "host");
		mockMap.put(ActionEventHeaderConstants.PORT, "123");
		mockMap.put(ActionEventHeaderConstants.HDFS_PATH, null);
		mockMap.put(ActionEventHeaderConstants.HDFS_FILE_NAME, "fileName");
		mockMap.put(ActionEventHeaderConstants.HIVE_PARTITION_VALUES, "part");
		mockMap.put(ActionEventHeaderConstants.SOURCE_FILE_PATH, "filePath");
		String totalSize = String.valueOf(new SecureRandom().nextInt());
		mockMap.put(ActionEventHeaderConstants.SOURCE_FILE_TOTAL_SIZE, totalSize);
		mockMap.put(ActionEventHeaderConstants.SOURCE_FILE_TOTAL_READ, totalSize);
		mockMap.put(ActionEventHeaderConstants.READ_COMPLETE, "true");

		when(mockActionEvent.getHeaders()).thenReturn(mockMap);
		rawChecksumValidator.validate(mockActionEvent);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void validateNullHdfsFileNameTest() throws DataValidationException {
		RawChecksumValidator rawChecksumValidator = new RawChecksumValidator();
		ActionEvent mockActionEvent = Mockito.mock(ActionEvent.class);
		Map<String, String> headers = new HashMap<>();
		headers.put(ActionEventHeaderConstants.HOST_NAMES, "host");
		headers.put(ActionEventHeaderConstants.PORT, "123");
		headers.put(ActionEventHeaderConstants.HDFS_PATH, "path");
		headers.put(ActionEventHeaderConstants.HDFS_FILE_NAME, "");

		headers.put(ActionEventHeaderConstants.READ_COMPLETE, "true");
		when(mockActionEvent.getHeaders()).thenReturn(headers);

		rawChecksumValidator.validate(mockActionEvent);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void validateNullSourceFilePathTest() throws DataValidationException {
		RawChecksumValidator rawChecksumValidator = new RawChecksumValidator();
		ActionEvent mockActionEvent = Mockito.mock(ActionEvent.class);

		Map<String, String> headers = new HashMap<>();
		headers.put(ActionEventHeaderConstants.HOST_NAMES, "host");
		headers.put(ActionEventHeaderConstants.PORT, "123");
		headers.put(ActionEventHeaderConstants.HDFS_PATH, "path");
		headers.put(ActionEventHeaderConstants.HDFS_FILE_NAME, "hdfsFile");

		headers.put(ActionEventHeaderConstants.READ_COMPLETE, "true");
		when(mockActionEvent.getHeaders()).thenReturn(headers);

		rawChecksumValidator.validate(mockActionEvent);
	}

	@Test
	public void validateSourceFileNotFoundTest() throws DataValidationException {
		RawChecksumValidator rawChecksumValidator = new RawChecksumValidator();
		ActionEvent mockActionEvent = Mockito.mock(ActionEvent.class);

		Map<String, String> headers = new HashMap<>();
		headers.put(ActionEventHeaderConstants.HOST_NAMES, "host");
		headers.put(ActionEventHeaderConstants.PORT, "123");
		headers.put(ActionEventHeaderConstants.HDFS_PATH, "path");
		headers.put(ActionEventHeaderConstants.HDFS_FILE_NAME, "hdfsFile");
		headers.put(ActionEventHeaderConstants.HIVE_PARTITION_VALUES, "");
		headers.put(ActionEventHeaderConstants.SOURCE_FILE_PATH, "testLocation");

		headers.put(ActionEventHeaderConstants.READ_COMPLETE, "true");
		when(mockActionEvent.getHeaders()).thenReturn(headers);

		File mockFile = Mockito.mock(File.class);
		when(mockFile.exists()).thenReturn(false);
		Assert.assertEquals(rawChecksumValidator.validate(mockActionEvent).getValidationResult(),
				ValidationResult.FAILED);
	}

	@Test
	public void validateFailedToGetHdfsChecksumTest() throws DataValidationException, IOException {
		RawChecksumValidator rawChecksumValidator = new RawChecksumValidator();
		ActionEvent mockActionEvent = Mockito.mock(ActionEvent.class);

		Map<String, String> headers = new HashMap<>();
		headers.put(ActionEventHeaderConstants.HOST_NAMES, "host");
		headers.put(ActionEventHeaderConstants.PORT, "123");
		headers.put(ActionEventHeaderConstants.HDFS_PATH, "path");
		headers.put(ActionEventHeaderConstants.HDFS_FILE_NAME, "hdfsFile");
		headers.put(ActionEventHeaderConstants.HIVE_PARTITION_VALUES, "");
		headers.put(ActionEventHeaderConstants.SOURCE_FILE_PATH, "sourceFileName");

		headers.put(ActionEventHeaderConstants.READ_COMPLETE, "true");
		when(mockActionEvent.getHeaders()).thenReturn(headers);

		File mockFile = Mockito.mock(File.class);
		when(mockFile.exists()).thenReturn(true);
		InputStream sourceInputStream = new ByteArrayInputStream("line 1 \nline 2".getBytes(Charset.forName("UTF-8")));
		FileUtils.copyInputStreamToFile(sourceInputStream, new File("sourceFileName"));
		sourceInputStream.close();
		WebHdfs mockWebHdfs = Mockito.mock(WebHdfs.class);
		ReflectionTestUtils.setField(rawChecksumValidator, "webHdfs", mockWebHdfs);
		HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
		when(mockWebHdfs.checksum(anyString())).thenReturn(mockResponse);
		HttpEntity mockHttpEntity = Mockito.mock(HttpEntity.class);
		when(mockResponse.getEntity()).thenReturn(mockHttpEntity);
		InputStream hdfsInputStream = new ByteArrayInputStream("".getBytes(Charset.forName("UTF-8")));
		when(mockHttpEntity.getContent()).thenReturn(hdfsInputStream);
		Assert.assertEquals(rawChecksumValidator.validate(mockActionEvent).getValidationResult(),
				ValidationResult.FAILED);
	}

	@Test
	public void validateChecksumWithoutPartitionTest()
			throws DataValidationException, ClientProtocolException, IOException, URISyntaxException {
		RawChecksumValidator rawChecksumValidator = new RawChecksumValidator();
		ActionEvent mockActionEvent = Mockito.mock(ActionEvent.class);

		Map<String, String> headers = new HashMap<>();
		headers.put(ActionEventHeaderConstants.HOST_NAMES, "host");
		headers.put(ActionEventHeaderConstants.PORT, "123");
		headers.put(ActionEventHeaderConstants.HDFS_PATH, "path");
		headers.put(ActionEventHeaderConstants.HDFS_FILE_NAME, "hdfsFile");
		headers.put(ActionEventHeaderConstants.HIVE_PARTITION_VALUES, "");
		headers.put(ActionEventHeaderConstants.SOURCE_FILE_PATH, "sourceFileName");

		headers.put(ActionEventHeaderConstants.READ_COMPLETE, "true");
		when(mockActionEvent.getHeaders()).thenReturn(headers);

		File mockFile = Mockito.mock(File.class);
		when(mockFile.exists()).thenReturn(true);
		InputStream sourceInputStream = new ByteArrayInputStream("line 1 \nline 2".getBytes(Charset.forName("UTF-8")));
		FileUtils.copyInputStreamToFile(sourceInputStream, new File("sourceFileName"));
		sourceInputStream.close();
		WebHdfs mockWebHdfs = Mockito.mock(WebHdfs.class);
		ReflectionTestUtils.setField(rawChecksumValidator, "webHdfs", mockWebHdfs);
		HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
		when(mockWebHdfs.checksum(anyString())).thenReturn(mockResponse);
		HttpEntity mockHttpEntity = Mockito.mock(HttpEntity.class);
		when(mockResponse.getEntity()).thenReturn(mockHttpEntity);
		String contents = "{\"FileChecksum\":{\"algorithm\":\"test_algorithm\",\"bytes\":\"000002000000000000000000b7c6a39568e93707653e46be8170cd9600000000\",\"length\":28}}";
		InputStream hdfsInputStream = new ByteArrayInputStream(contents.getBytes(Charset.forName("UTF-8")));
		when(mockHttpEntity.getContent()).thenReturn(hdfsInputStream);
		Assert.assertEquals(rawChecksumValidator.validate(mockActionEvent).getValidationResult(),
				ValidationResult.PASSED);
	}

	@Test
	public void validateChecksumFailedWithPartitionTest()
			throws DataValidationException, ClientProtocolException, IOException, URISyntaxException {

		RawChecksumValidator rawChecksumValidator = new RawChecksumValidator();
		ActionEvent mockActionEvent = Mockito.mock(ActionEvent.class);
		Map<String, String> headers = new HashMap<>();
		headers.put(ActionEventHeaderConstants.HOST_NAMES, "host");
		headers.put(ActionEventHeaderConstants.PORT, "123");
		headers.put(ActionEventHeaderConstants.HDFS_PATH, "/webhdfs/v1/path/");
		headers.put(ActionEventHeaderConstants.HDFS_FILE_NAME, "hdfsFile");
		headers.put(ActionEventHeaderConstants.HIVE_PARTITION_VALUES, "dt, hr");
		headers.put(ActionEventHeaderConstants.SOURCE_FILE_PATH, "sourceFileName");
		headers.put(ActionEventHeaderConstants.READ_COMPLETE, "true");
		when(mockActionEvent.getHeaders()).thenReturn(headers);
		
		File mockFile = Mockito.mock(File.class);
		when(mockFile.exists()).thenReturn(true);
		InputStream sourceInputStream = new ByteArrayInputStream("line 1 \nline 2".getBytes(Charset.forName("UTF-8")));
		FileUtils.copyInputStreamToFile(sourceInputStream, new File("sourceFileName"));
		sourceInputStream.close();
		WebHdfs mockWebHdfs = Mockito.mock(WebHdfs.class);
		ReflectionTestUtils.setField(rawChecksumValidator, "webHdfs", mockWebHdfs);
		HttpResponse mockResponse1 = Mockito.mock(HttpResponse.class);
		when(mockWebHdfs.checksum(anyString())).thenReturn(mockResponse1);
		HttpEntity mockHttpEntity = Mockito.mock(HttpEntity.class);
		when(mockResponse1.getEntity()).thenReturn(mockHttpEntity);
		String contents = "{\"FileChecksum\":{\"algorithm\":\"test_algorithm\",\"bytes\":\"0000020000000000000000005b8436f9d2cdeser265ae02abdbeac3000000000\",\"length\":28}}";
		InputStream hdfsInputStream = new ByteArrayInputStream(contents.getBytes(Charset.forName("UTF-8")));
		when(mockHttpEntity.getContent()).thenReturn(hdfsInputStream);
		doNothing().when(mockWebHdfs).releaseConnection();

		HttpResponse mockResponse2 = Mockito.mock(HttpResponse.class);
		when(mockWebHdfs.fileStatus(anyString())).thenReturn(mockResponse2);
		StatusLine mockSL1 = Mockito.mock(StatusLine.class);
		when(mockResponse2.getStatusLine()).thenReturn(mockSL1);
		when(mockSL1.getStatusCode()).thenReturn(404);
		doNothing().when(mockWebHdfs).releaseConnection();

		HttpResponse mockResponse3 = Mockito.mock(HttpResponse.class);
		when(mockWebHdfs.mkdir(anyString())).thenReturn(mockResponse3);
		StatusLine mockSL2 = Mockito.mock(StatusLine.class);
		when(mockResponse3.getStatusLine()).thenReturn(mockSL2);
		when(mockSL2.getStatusCode()).thenReturn(200);
		doNothing().when(mockWebHdfs).releaseConnection();
		Assert.assertEquals(rawChecksumValidator.validate(mockActionEvent).getValidationResult(),
				ValidationResult.FAILED);
	}

	@Test
	public void validateChecksumFailedWithErrorChecksumDirExistsTest()
			throws DataValidationException, ClientProtocolException, IOException, URISyntaxException {

		RawChecksumValidator rawChecksumValidator = new RawChecksumValidator();
		ActionEvent mockActionEvent = Mockito.mock(ActionEvent.class);

		Map<String, String> headers = new HashMap<>();
		headers.put(ActionEventHeaderConstants.HOST_NAMES, "host");
		headers.put(ActionEventHeaderConstants.PORT, "123");
		headers.put(ActionEventHeaderConstants.HDFS_PATH, "/webhdfs/v1/path/");
		headers.put(ActionEventHeaderConstants.HDFS_FILE_NAME, "hdfsFile");
		headers.put(ActionEventHeaderConstants.HIVE_PARTITION_VALUES, "");
		headers.put(ActionEventHeaderConstants.SOURCE_FILE_PATH, "sourceFileName");

		headers.put(ActionEventHeaderConstants.READ_COMPLETE, "true");
		when(mockActionEvent.getHeaders()).thenReturn(headers);

		File mockFile = Mockito.mock(File.class);
		when(mockFile.exists()).thenReturn(true);
		InputStream sourceInputStream = new ByteArrayInputStream("line 1 \nline 2".getBytes(Charset.forName("UTF-8")));
		FileUtils.copyInputStreamToFile(sourceInputStream, new File("sourceFileName"));
		sourceInputStream.close();
		WebHdfs mockWebHdfs = Mockito.mock(WebHdfs.class);
		ReflectionTestUtils.setField(rawChecksumValidator, "webHdfs", mockWebHdfs);
		HttpResponse mockResponse1 = Mockito.mock(HttpResponse.class);
		when(mockWebHdfs.checksum(anyString())).thenReturn(mockResponse1);
		HttpEntity mockHttpEntity = Mockito.mock(HttpEntity.class);
		when(mockResponse1.getEntity()).thenReturn(mockHttpEntity);
		String contents = "{\"FileChecksum\":{\"algorithm\":\"test_algorithm\",\"bytes\":\"0000020000000000000000005b8436f9d2cdeser265ae02abdbeac3000000000\",\"length\":28}}";
		InputStream hdfsInputStream = new ByteArrayInputStream(contents.getBytes(Charset.forName("UTF-8")));
		when(mockHttpEntity.getContent()).thenReturn(hdfsInputStream);
		doNothing().when(mockWebHdfs).releaseConnection();

		HttpResponse mockResponse2 = Mockito.mock(HttpResponse.class);
		when(mockWebHdfs.fileStatus(anyString())).thenReturn(mockResponse2);
		StatusLine mockSL1 = Mockito.mock(StatusLine.class);
		when(mockResponse2.getStatusLine()).thenReturn(mockSL1);
		when(mockSL1.getStatusCode()).thenReturn(200);
		doNothing().when(mockWebHdfs).releaseConnection();
		Assert.assertEquals(rawChecksumValidator.validate(mockActionEvent).getValidationResult(),
				ValidationResult.FAILED);
	}

	@Test
	public void testGettersAndSetters() throws DataValidationException {
		RawChecksumValidator rawChecksumValidator = new RawChecksumValidator();
		rawChecksumValidator.setName("unit-name");
		Assert.assertEquals(rawChecksumValidator.getName(), "unit-name");

	}

}
