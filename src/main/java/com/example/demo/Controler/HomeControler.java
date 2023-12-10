package com.example.demo.Controler;

import java.util.List;
import java.util.Map;

import org.ektorp.Page;
import org.ektorp.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Model.Book;
import com.example.demo.Repository.BookRepo;
import com.example.demo.dto.BookPageDTO;
import com.example.demo.dto.GetBooksRequestBody;
import com.example.demo.dto.Statistics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Controller
public class HomeControler {
	@Autowired
	BookRepo bookRepo;
	
	@GetMapping("")
	public String getHomePage() {
		return "index";
	}
	
	@PostMapping("/getBooks")
	@ResponseBody
	public ResponseEntity<BookPageDTO> getPage(@RequestBody GetBooksRequestBody requestBody) {
		return ResponseEntity.ok(bookRepo.getBooks(requestBody));
	}
	
	@GetMapping("/getPriceStatistics")
	@ResponseBody
	public ResponseEntity<Map<String, Statistics>> getPriceStatistics() throws JsonMappingException, JsonProcessingException {
		return ResponseEntity.ok(bookRepo.getPriceStatistics());
	}
	
	@GetMapping("/getStarsStatistics")
	@ResponseBody
	public ResponseEntity<Map<String, Statistics>> getStarsStatistics() throws JsonMappingException, JsonProcessingException {
		return ResponseEntity.ok(bookRepo.getStarsStatistics());
	}
	
	@GetMapping("/getAvailabilityStatistics")
	@ResponseBody
	public ResponseEntity<Map<String, Statistics>> getAvailabilityStatistics() throws JsonMappingException, JsonProcessingException {
		return ResponseEntity.ok(bookRepo.getAvailabilityStatistics());
	}
	
	@GetMapping("/getNumberReviewsStatistics")
	@ResponseBody
	public ResponseEntity<Map<String, Statistics>> getNumberReviewsStatistics() throws JsonMappingException, JsonProcessingException {
		return ResponseEntity.ok(bookRepo.getNumberReviewsStatistics());
	}
	
	@PostMapping("/addBook")
	@ResponseBody
	public ResponseEntity<Void> addBook(@RequestBody Book book){
		bookRepo.add(book);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@PostMapping("/deleteBooks")
	@ResponseBody
	public ResponseEntity<Void> deleteBooks(@RequestBody List<Book> books){
		bookRepo.deleteBooks(books);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
