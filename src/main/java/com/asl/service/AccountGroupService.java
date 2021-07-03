package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.AccountGroup;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2021
 */
@Component
public interface AccountGroupService {

	public long save(AccountGroup accountGroup);

	public long update(AccountGroup accountGroup);

	public List<AccountGroup> getAllByLevelAndType(int xaglevel, String xagtype);

	public AccountGroup findByCode(String xagcode);
}