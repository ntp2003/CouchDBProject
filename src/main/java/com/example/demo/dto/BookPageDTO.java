package com.example.demo.dto;

import java.io.Serializable;
import java.util.List;
import java.util.OptionalInt;

import com.example.demo.Model.Book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookPageDTO implements Serializable{
	private List<Book> books;
	private int totalPages;
	private int number;
	private int totalElements;
}
