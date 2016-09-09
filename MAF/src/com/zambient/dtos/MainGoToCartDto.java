package com.zambient.dtos;

import java.util.ArrayList;

public class MainGoToCartDto {

	private String totalCost;
	
	private String noOfItems;
	
	private ArrayList<CartProductsDto> cartProducts;

	public String getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}

	public String getNoOfItems() {
		return noOfItems;
	}

	public void setNoOfItems(String noOfItems) {
		this.noOfItems = noOfItems;
	}

	public ArrayList<CartProductsDto> getCartProducts() {
		return cartProducts;
	}

	public void setCartProducts(ArrayList<CartProductsDto> cartProducts) {
		this.cartProducts = cartProducts;
	}
	
	
}
