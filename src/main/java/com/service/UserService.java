package com.service;

import com.bean.User;
import com.dao.IUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Sun on 16/11/7.
 */
@Service
@Transactional
public class UserService {

	@Autowired
	private IUserDAO userDAO;

	public List<User> findAll() {
		List<User> all = userDAO.findAll();
		return all;
	}

	public List<User> findByUsername(String username) {
		List<User> list = userDAO.findByUsername(username);
		return list;
	}

	public User save(User u) {
		User save = userDAO.save(u);
		return save;
	}

	public User getById(Long id) {
		User one = userDAO.findOne(id);
		return one;
	}

	public User update(Long id, User u) {
		User user = getById(id);
		return userDAO.saveAndFlush(u);
	}


}
