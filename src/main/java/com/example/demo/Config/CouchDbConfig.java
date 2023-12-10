package com.example.demo.Config;

import java.net.MalformedURLException;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CouchDbConfig {
	@Bean
	public CouchDbInstance couchdbInstance() throws MalformedURLException {
		HttpClient httpClient = new StdHttpClient.Builder()
		        .url("http://localhost:5984")
		        .username("admin")
		        .password("admin")
		        .build();

		return new StdCouchDbInstance(httpClient);
	}
	
    @Bean
    public CouchDbConnector couchDbConnector(CouchDbInstance couchdbInstance) {
    	CouchDbConnector db = new StdCouchDbConnector("books1", couchdbInstance);
    	db.createDatabaseIfNotExists();
    	return db;
    }
}