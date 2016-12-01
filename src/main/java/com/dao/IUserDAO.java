package com.dao;

import com.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Sun on 16/11/10.
 */
@Repository
public interface IUserDAO extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	List<User> findByUsername(String username);


}
