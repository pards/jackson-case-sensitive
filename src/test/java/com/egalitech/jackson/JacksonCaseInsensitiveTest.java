package com.egalitech.jackson;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JacksonCaseInsensitiveTest {

	private ObjectMapper mapper;

	@Before
	public void setUp() {
		mapper = new ObjectMapper();
		mapper.registerModule( new JavaTimeModule());
	}
	
	@Test
	public void testParseDateMixedCase() throws Exception {
		MyObject result = mapper.readValue(createJson("22-Jul-1974"), MyObject.class);
		assertEquals( Month.JULY, result.getBirthday().getMonth());
	}

	@Test
	public void testParseDateUpperCase() throws Exception {
		MyObject result = mapper.readValue(createJson("22-JUL-1974"), MyObject.class);
		assertEquals( Month.JULY, result.getBirthday().getMonth());
	}
	
	@Test
	public void testConfigurableParser() {
	    DateTimeFormatter formatter= new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd-MMM-yyyy").toFormatter();
	    LocalDate date = LocalDate.parse("22-JUL-1974", formatter);
	    assertEquals( Month.JULY, date.getMonth());
	}
	
	private String createJson(String dt) {
		return String.format("{\"birthday\":\"%s\"}", dt);
	}
}

class MyObject {
	@JsonFormat(shape=Shape.STRING, pattern="dd-MMM-yyyy")
	private LocalDate birthday;

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
}
