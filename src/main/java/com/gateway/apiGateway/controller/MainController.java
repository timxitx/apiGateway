package com.gateway.apiGateway.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.HttpClients;


@RestController
public class MainController {
	
	private static final String URL_PREFIX = "https://CdkSt-LB8A1-1B59AY410FIYY-2047389031.us-east-2.elb.amazonaws.com";
	private static final String MS1_TEST_URI1 = URL_PREFIX+":7070";
	private static final String MS1_TEST_URI2 = URL_PREFIX+":8080/test";
	private static final String MS1_TEST_URI3 = URL_PREFIX+":9090/test";
	private static final String MS1_TEST_URI4 = URL_PREFIX+":7070/upload";
	private static final String PASSWORD = "password";
	private static final String CERT_PATH = "/clientCert.jks";
	RestTemplate restTemplate;
	RequestEntity<Object> requestEntity = null;

	public MainController() {
		try {
			this.restTemplate = getRestTemplate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@RequestMapping("/test")
	public String test() throws Exception {
		return "This is a test";
	}

	@RequestMapping("/ms1Test")
	public String ms1Test() throws Exception {
		ResponseEntity<String> resp = this.restTemplate.exchange(MS1_TEST_URI1, HttpMethod.GET, this.requestEntity, String.class);
		return resp.getBody().trim();
	}

	@RequestMapping("/ms2Test")
	public String ms2Test() throws Exception {
		ResponseEntity<String> resp = this.restTemplate.exchange(MS1_TEST_URI2, HttpMethod.GET, this.requestEntity, String.class);
		return resp.getBody().trim();
	}

	@RequestMapping("/ms3Test")
	public String ms3Test() throws Exception {
		ResponseEntity<String> resp = this.restTemplate.exchange(MS1_TEST_URI3, HttpMethod.GET, this.requestEntity, String.class);
		return resp.getBody().trim();
	}

	@PostMapping("/upload")
	public String upload(@RequestParam(value = "file") MultipartFile file) throws Exception {
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", new ByteArrayResource(file.getBytes()));
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<MultiValueMap<String, Object>> re =
            new HttpEntity<>(body, headers);
		ResponseEntity<String> resp = this.restTemplate.exchange(MS1_TEST_URI4, HttpMethod.POST, re, String.class);
		return resp.getBody().trim();
	}
	
	@SuppressWarnings("deprecation")
	public RestTemplate getRestTemplate() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		File file = new File(CERT_PATH);
		InputStream is = new FileInputStream(file);
		KeyStore keystore = KeyStore.getInstance("jks");
		keystore.load(is, PASSWORD.toCharArray());
		SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(keystore).loadKeyMaterial(keystore, PASSWORD.toCharArray()).build();
		HttpClient client = HttpClients.custom().setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).setSSLContext(sslcontext).build();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
		return restTemplate;
	}
}
