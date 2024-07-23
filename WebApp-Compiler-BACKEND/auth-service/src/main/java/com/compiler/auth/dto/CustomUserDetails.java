package com.compiler.auth.dto;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class CustomUserDetails implements UserDetails
{   private String username;

	public void setUsername(String username) {
	this.username = username;
}

	private String email;
    private String password;
    private String phone;
    private String role;
	private Set<? extends GrantedAuthority> authorities;
	
    
    
    public void setAuthorities(Set<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public CustomUserDetails(String username,String email, String password, Set<? extends GrantedAuthority> authorities, String phone,String role) {
        this.username=username;
		this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.phone = phone;
        this.role=role;
    }


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

}

