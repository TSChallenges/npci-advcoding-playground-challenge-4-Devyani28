package com.bankmgmt.app.rest;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bankmgmt.app.entity.Account;
import com.bankmgmt.app.service.BankService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class BankController {

    @Autowired
    private BankService bankService;
    
    @GetMapping
	public List<Account> getAllAccounts(){
		return bankService.getAllAcconts();
	}

    // TODO: API to Get an account by ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getAccountById(@PathVariable("id") Integer id) {
    	Account ac = bankService.getAcountById(id);
    	if(ac!=null) {
    		return new ResponseEntity<>(ac,HttpStatus.ACCEPTED);
    	}
    	return new ResponseEntity<>("404 NOT FOUND",HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Object> createNewAccount(@Valid @RequestBody Account account, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Collect all error messages
            StringBuilder errorMessages = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessages.append(error.getField()).append(": ").append(error.getDefaultMessage()).append(" ");
            }
            return new ResponseEntity<>(errorMessages.toString(), HttpStatus.BAD_REQUEST);
        }
        
        // Check for duplicate email
        if (bankService.isEmailTaken(account.getEmail())) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(bankService.createNewAccount(account.getAccountHolderName(), account.getAccountType(), account.getEmail()), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Object> depositMoney(@PathVariable("id") Integer id, @RequestBody Map<String, Object> json) {
        
        Double deposit = (Double) json.get("deposit");
        if (deposit <= 0) {
            return new ResponseEntity<>("Deposit amount must be positive", HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(bankService.depositeMoney(id, deposit), HttpStatus.ACCEPTED);
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Object> withdrawMoney(@PathVariable("id") Integer id,@RequestBody Map<String, Object> json) {
       
        Double withdraw = (Double) json.get("withdraw");
        if (withdraw <= 0) {
            return new ResponseEntity<>("Withdraw amount must be positive", HttpStatus.BAD_REQUEST);
        }

        Account account = bankService.getAcountById(id);
        if (account == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }

        if (account.getBalance() < withdraw) {
            return new ResponseEntity<>("Insufficient funds", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(bankService.withdrawMoney(id, withdraw), HttpStatus.ACCEPTED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Object> transferAmount(@RequestParam("fromId") Integer fromId, @RequestParam("toId") Integer toId, @RequestParam("amount") Double amount) {
      
        if (amount <= 0) {
            return new ResponseEntity<>("Transfer amount must be positive", HttpStatus.BAD_REQUEST);
        }

        Account fromAccount = bankService.getAcountById(fromId);
        Account toAccount = bankService.getAcountById(toId);
        if (fromAccount == null || toAccount == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }

        if (fromAccount.getBalance() < amount) {
            return new ResponseEntity<>("Insufficient funds", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(bankService.transferMoney(fromId, toId, amount), HttpStatus.ACCEPTED);
    }
}
