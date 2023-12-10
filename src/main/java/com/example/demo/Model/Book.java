package com.example.demo.Model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
	@JsonProperty("_id")
	@JsonInclude(Include.NON_NULL)
    private String id;
    @JsonProperty("_rev")
    @JsonInclude(Include.NON_NULL)
    private String revision; 
    private String url;
    private String title;
    private String upc;   
    @JsonProperty("product_type")
    private String productType;
    @JsonProperty("price_excl_tax")
    private BigDecimal priceExcludingTax;
    @JsonProperty("price_incl_tax")
    private BigDecimal priceIncludingTax;
    private BigDecimal tax;
    private BigDecimal price;
    private int availability;
    @JsonProperty("num_reviews")
    private int numberReviews;
    private short stars;
    private String category;
    private String description;
}
