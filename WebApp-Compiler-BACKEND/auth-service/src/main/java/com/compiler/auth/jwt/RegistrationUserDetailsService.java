package com.compiler.auth.jwt;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.compiler.auth.entity.RegistrationEntity;
import com.compiler.auth.repository.RegistrationRepository;
import com.compiler.auth.serviceImp.RegistrationServiceImp;



public class RegistrationUserDetailsService implements UserDetailsService
{
	
	@Autowired
	RegistrationRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		
        if (usernameExistsInYourSystem(username)) 
        {

            return buildUserDetails(username);
        } 
        else 
        {
            
            throw new UsernameNotFoundException("Username not found: " + username);
        }
	}
	
    private boolean usernameExistsInYourSystem(String username) 
    {
        Optional<RegistrationEntity> opt = repo.findIdByEmail(username);
        if(opt!=null)
        {
        	RegistrationEntity details=opt.get();
        	return true;
        }
        else
        {
        	return false;
        }
        
    }
    
    private UserDetails buildUserDetails(String username) 
    {
    	Optional<RegistrationEntity> opt = repo.findIdByEmail(username);
    	RegistrationEntity details=opt.get();
    	return (UserDetails) details;
    }

}

