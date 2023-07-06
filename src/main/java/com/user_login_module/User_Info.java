package com.user_login_module;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service("user_info")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class User_Info implements UserDetails{

	private String user_name ;//represents the primary key in the db

	private String password  = new String();

	private List<GrantedAuthority> authorities ;

	private String nick_name;

	private int  account_active;

	public int getAccount_active() {
		return account_active;
	}

	public void setAccount_active(int account_active) {
		this.account_active = account_active;
	}

	public User_Info() {

	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities( String role ) {

		this.authorities = Arrays.asList(  new SimpleGrantedAuthority( role )) ;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return authorities;

	}

	@Override
	public String getPassword() {
		return password;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	@Override
	public String getUsername() {
		return user_name;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@Override
	public boolean isEnabled() {

		if ( this.account_active == 1 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
