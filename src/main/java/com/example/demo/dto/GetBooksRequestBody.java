package com.example.demo.dto;

import lombok.Data;

@Data
public class GetBooksRequestBody {
	private int page; 
	private int size; 
	private String sortProperty; 
	private boolean desc;
}
