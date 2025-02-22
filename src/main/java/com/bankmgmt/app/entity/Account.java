package com.bankmgmt.app.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class Account {
	private Integer id;
	@NotEmpty(message = "Account holder name is required")
	@NotNull(message = "Account holder name is required")
	private String accountHolderName;
	@Pattern(regexp = "^(SAVINGS|CURRENT)$", message = "Account type must be either SAVINGS or CURRENT")
	private String accountType;
	@Positive(message = "Balance cannot be negative")
	private Double balance;
	@Email(message = "Email should be valid")
    @NotNull(message = "Email is required")
	private String email;

	// Constructors, getters, and setters

	public Account(Integer id, String accountHolderName, String accountType, Double balance, String email) {
		this.id = id;
		this.accountHolderName = accountHolderName;
		this.accountType = accountType;
		this.balance = balance;
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", accountHolderName=" + accountHolderName + ", accountType=" + accountType
				+ ", balance=" + balance + ", email=" + email + "]";
	}

	// TODO: Add getters and setters

}
