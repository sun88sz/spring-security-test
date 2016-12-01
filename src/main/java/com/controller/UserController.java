package com.controller;

import com.bean.User;
import com.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "user-api", description = "有关于用户的CURD操作")
@RestController
@RequestMapping("/user")

public class UserController {

	@Autowired
	private UserService service;

//	@Autowired
//	RestTemplate restTemplate;

	@ApiOperation(value = "创建用户", notes = "根据User对象创建用户", response = User.class)
	@RequestMapping(value = "", method = RequestMethod.POST)
	public User create(@RequestBody User user) {
		return service.save(user);
	}

	@ApiOperation(value = "查询用户", notes = "查询所有用户")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<User> get(@ModelAttribute User user) {
//		Page<User> userpages = service.findBySpecAndPaginate(user.getPage());
//		List<User> users = userpages.getContent();
//		int totalPages = userpages.getTotalPages();
//		int size = userpages.getSize();
//
//		System.out.println(users);
//		System.out.println(totalPages);
//		System.out.println(size);

//		return users;
		return null;
	}

	@ApiOperation(value = "获取用户详细信息", notes = "根据url的id获取用户详细信息")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public User getUser(@PathVariable Long id) {
		return service.getById(id);
	}

	@ApiOperation(value = "更新用户详细信息", notes = "根据url的id来指定更新对象，并根据传过来的user信息来更新用户详细信息")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public User putUser(@PathVariable Long id, @RequestBody User user) {
		return service.update(id, user);
	}


}