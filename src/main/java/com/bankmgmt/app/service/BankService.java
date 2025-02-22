package com.bankmgmt.app.service;

import com.bankmgmt.app.entity.Account;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.*;

@Service
public class BankService {

    private List<Account> accounts = new ArrayList<>();
    private static Integer currentId = 1;

    public Account createNewAccount(String accountHolderName, String accountType, String email) {
    	
    	 if (isEmailTaken(email)) {
             throw new IllegalArgumentException("Email already exists");
         }
    	accounts.add(new Account(currentId++, accountHolderName, accountType, 0.0, email));
    	return accounts.get(accounts.size()-1);
    }
    
    public boolean isEmailTaken(String email) {
        for (Account account : accounts) {
            if (account.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    // TODO: Method to Get All Accounts
    public List<Account> getAllAcconts(){
    	return accounts;
    }
    

    // TODO: Method to Get Account by ID
    public Account getAcountById(Integer id) {
    	for(Account i : accounts) {
    		if(i.getId().equals(id)) {
    			return i;
    		}
    	}
    	return null;
    }


	public List<Account> transferMoney(Integer formId, Integer toId, Double amount) {
		Account remiiter = getAcountById(formId);
		Account benificary = getAcountById(toId);
		
		if(remiiter !=null && benificary != null && remiiter.getBalance() >= amount) {
			benificary.setBalance(benificary.getBalance()+amount);
			remiiter.setBalance(remiiter.getBalance()-amount);
		}
		return Arrays.asList(remiiter,benificary);
	}


	public Account depositeMoney(Integer id, Double deposite) {
		Account ac = getAcountById(id);
		if(ac!=null) {
			ac.setBalance(ac.getBalance()+deposite);
		}
		return ac;
	}


	public Account withdrawMoney(Integer id, Double withdraw) {
		Account ac = getAcountById(id);
		if(ac!=null && withdraw <= ac.getBalance()) {
			ac.setBalance(ac.getBalance()-withdraw);
		}
		return ac;
	}


	public boolean deleteAccount(Integer id) {
		int idx = 0;
			for(Account acs:accounts) {
				if(id == acs.getId()) {
					accounts.remove(idx);
					return true;
				}
				idx++;
			}
//		}
		return false;
	}

}
