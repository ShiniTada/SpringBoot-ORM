package com.epam.esm.dto.list;

import com.epam.esm.dto.UsersDto;
import com.epam.esm.dto.jsonview.Views;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

public class UsersListDto {

	@JsonView(Views.AllUsersView.class)
	private List<UsersDto> users;

	public UsersListDto(List<UsersDto> users) {
		this.users = users;
	}

	public List<UsersDto> getUsers() {
		return users;
	}

	public void setUsers(List<UsersDto> users) {
		this.users = users;
	}
}
