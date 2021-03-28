package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Xusers;

/**
 * @author Zubayer Ahamed
 * @since Dec 30, 2020
 */
@Component
public interface XusersService {

	public long save(Xusers user);

	public long update(Xusers user);

	public List<Xusers> findByZemail(String zemail);

	public List<Xusers> findByZemailAndXpassword(String zemail, String xpassword);

	public Xusers findByZemailAndZid(String zemail, String zid);

	public List<Xusers> getAllXusers();

	public String modifyZemail(String zemail);
}
