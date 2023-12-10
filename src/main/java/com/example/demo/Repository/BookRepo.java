package com.example.demo.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ektorp.BulkDeleteDocument;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.ektorp.support.Views;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.example.demo.Model.Book;
import com.example.demo.dto.BookPageDTO;
import com.example.demo.dto.GetBooksRequestBody;
import com.example.demo.dto.Statistics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class BookRepo extends CouchDbRepositorySupport<Book> {
	protected BookRepo(@Qualifier("couchDbConnector") CouchDbConnector couchDbConnector) {
		super(Book.class, couchDbConnector);
		initStandardDesignDocument();
	}

	@Views({ @View(name = "group-by-price", map = "function(doc) { if (doc.product_type == \'books\') { emit(doc.price,doc) } }"),
			@View(name = "group-by-stars", map = "function(doc) { if (doc.product_type == \'books\') { emit(doc.stars,doc) } }"),
			@View(name = "group-by-availability", map = "function(doc) { if (doc.product_type == \'books\') { emit(doc.availability,doc) } }"),
			@View(name = "group-by-num_reviews", map = "function(doc) { if (doc.product_type == \'books\') { emit(doc.num_reviews,doc) } }") })
	public BookPageDTO getBooks(GetBooksRequestBody requestBody) {
		int skip = requestBody.getSize() * requestBody.getPage();
		ViewQuery q = createQuery("group-by-" + requestBody.getSortProperty()).descending(requestBody.isDesc()).skip(skip).limit(requestBody.getSize()).includeDocs(true);
		BookPageDTO result = new BookPageDTO();
		ViewResult vr = db.queryView(q);
		result.setBooks(vr.getRows().stream().map(i -> {
			try {
				return new ObjectMapper().readValue(i.getValue(), Book.class);
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return null;
		}).toList());
		result.setNumber(requestBody.getPage());
		result.setTotalPages((int)(vr.getTotalRows()/requestBody.getSize()) + 1);
		result.setTotalElements(vr.getTotalRows());
		
		return result;
	}
	
	@View(
			name = "group-category-price-reduce", 
			map = "function(doc) { if (doc.product_type == \'books\') { emit(doc.category,doc.price) } }", 
			reduce = "_stats"
			)
	public Map<String, Statistics> getPriceStatistics() throws JsonMappingException, JsonProcessingException{
		ViewQuery q = createQuery("group-category-price-reduce").reduce(true).group(true).includeDocs(false);
		Map<String, Statistics> map = new LinkedHashMap<>();
		ViewResult vr = db.queryView(q);
		for (ViewResult.Row row : vr.getRows()) {
			map.put(row.getKey(), new ObjectMapper().readValue(row.getValue(), Statistics.class));
		}
		return map;
	}
	
	@View(
			name = "group-category-stars-reduce", 
			map = "function(doc) { if (doc.product_type == \'books\') { emit(doc.category, doc.stars) } }", 
			reduce = "_stats"
			)
	public Map<String, Statistics> getStarsStatistics() throws JsonMappingException, JsonProcessingException{
		ViewQuery q = createQuery("group-category-stars-reduce").reduce(true).group(true).includeDocs(false);
		Map<String, Statistics> map = new LinkedHashMap<>();
		ViewResult vr = db.queryView(q);
		for (ViewResult.Row row : vr.getRows()) {
			map.put(row.getKey(), new ObjectMapper().readValue(row.getValue(), Statistics.class));
		}
		return map;
	}
	
	@View(
			name = "group-category-availability-reduce", 
			map = "function(doc) { if (doc.product_type == \'books\') { emit(doc.category, doc.availability) } }", 
			reduce = "_stats"
			)
	public Map<String, Statistics> getAvailabilityStatistics() throws JsonMappingException, JsonProcessingException{
		ViewQuery q = createQuery("group-category-availability-reduce").reduce(true).group(true).includeDocs(false);
		Map<String, Statistics> map = new LinkedHashMap<>();
		ViewResult vr = db.queryView(q);
		for (ViewResult.Row row : vr.getRows()) {
			map.put(row.getKey(), new ObjectMapper().readValue(row.getValue(), Statistics.class));
		}
		return map;
	}
	
	@View(
			name = "group-category-num_reviews-reduce", 
			map = "function(doc) { if (doc.product_type == \'books\') { emit(doc.category,doc.num_reviews) } }", 
			reduce = "_stats"
			)
	public Map<String, Statistics> getNumberReviewsStatistics() throws JsonMappingException, JsonProcessingException{
		ViewQuery q = createQuery("group-category-num_reviews-reduce").reduce(true).group(true).includeDocs(false);
		Map<String, Statistics> map = new LinkedHashMap<>();
		ViewResult vr = db.queryView(q);
		for (ViewResult.Row row : vr.getRows()) {
			map.put(row.getKey(), new ObjectMapper().readValue(row.getValue(), Statistics.class));
		}
		return map;
	}
	
	public void deleteBooks(List<Book> books) {
		List<BulkDeleteDocument> bulkDocs = books.stream().map(i -> BulkDeleteDocument.of(i)).toList();
		db.executeBulk(bulkDocs);
	}
}
