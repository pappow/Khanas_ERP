package com.asl.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.AccountGroup;
import com.asl.mapper.AccountGroupMapper;
import com.asl.service.AccountGroupService;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2021
 */
@Service
public class AccountGroupServiceImpl extends AbstractGenericService implements AccountGroupService {

	@Autowired AccountGroupMapper accountGroupMapper;

	@Transactional
	@Override
	public long save(AccountGroup accountGroup) {
		if(accountGroup == null) return 0;
		accountGroup.setZid(sessionManager.getBusinessId());
		accountGroup.setZauserid(getAuditUser());
		return accountGroupMapper.save(accountGroup);
	}

	@Transactional
	@Override
	public long update(AccountGroup accountGroup) {
		if(accountGroup == null) return 0;
		accountGroup.setZid(sessionManager.getBusinessId());
		accountGroup.setZuuserid(getAuditUser());
		return accountGroupMapper.update(accountGroup);
	}

	@Override
	public List<AccountGroup> getAllByLevelAndType(int xaglevel, String xagtype) {
		if(StringUtils.isBlank(xagtype)) return Collections.emptyList();
		return accountGroupMapper.getAllByLevelAndType(xaglevel, xagtype, sessionManager.getBusinessId());
	}

	@Override
	public AccountGroup findByCode(String xagcode) {
		if(StringUtils.isBlank(xagcode)) return null;
		return accountGroupMapper.findByCode(xagcode, sessionManager.getBusinessId());
	}

}
