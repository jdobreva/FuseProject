/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.estafet.fuse.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.estafet.fuse.dao.AccountServiceApi;
import com.estafet.fuse.model.Account;

public class AccountServiceImpl implements AccountServiceApi {

	/**
	 * The EntityManager wired to this service via the modifier.
	 */
	@PersistenceContext(unitName="bankx")
	private EntityManager entityManager;

	@Override
	public Account getAccountByIban(String iban) {
		return entityManager.find(Account.class, iban);
	}

	@Override
	public boolean saveAccount(Account account) {
		EntityTransaction t = entityManager.getTransaction();
		try {
			t.begin();
			entityManager.persist(account);
			t.commit();	
		}catch (Exception e){
			t.rollback();
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean updateAccount(Account account) {
		EntityTransaction t = entityManager.getTransaction();
		try {
			t.begin();
			entityManager.merge(account);
			t.commit();	
		}catch (Exception e){
			t.rollback();
			return false;
		}
		
		return true;
	}

	@Override
	public List<String> getAccountsWithRasedFlag() {
		Query query = entityManager.createNamedQuery("flaggedAccounts");
		return query.getResultList();
	}

}