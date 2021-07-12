package com.asl.service.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asl.entity.Acdef;
import com.asl.entity.Acdetail;
import com.asl.entity.Acheader;
import com.asl.mapper.AcMapper;
import com.asl.service.AcService;

/**
 * @author Zubayer Ahamed
 * @since Jul 10, 2021
 */
@Service
public class AcServiceImpl extends AbstractGenericService implements AcService {

	@Autowired private AcMapper acMapper;

	@Transactional
	@Override
	public long saveAcheader(Acheader acheader) {
		if(acheader == null) return 0;
		acheader.setZid(sessionManager.getBusinessId());
		acheader.setZauserid(getAuditUser());
		return acMapper.saveAcheader(acheader);
	}

	@Transactional
	@Override
	public long updateAcheader(Acheader acheader) {
		if(acheader == null) return 0;
		acheader.setZid(sessionManager.getBusinessId());
		acheader.setZuuserid(getAuditUser());
		return acMapper.updateAcheader(acheader);
	}

	@Override
	public List<Acheader> getAllAcheader() {
		return acMapper.getAllAcheader(sessionManager.getBusinessId());
	}

	@Override
	public Acheader findAcheaderByXvoucher(String xvoucher) {
		if(StringUtils.isBlank(xvoucher)) return null;
		return acMapper.findAcheaderByXvoucher(xvoucher, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long saveAcdetail(Acdetail acdetail) {
		if(acdetail == null) return 0;
		acdetail.setZid(sessionManager.getBusinessId());
		acdetail.setZauserid(getAuditUser());
		long count = acMapper.saveAcdetail(acdetail);
		if(count != 0) {
			count = updateAcheaderXstatusjv(acdetail.getXvoucher());
		}
		return count;
	}

	@Transactional
	@Override
	public long updateAcdetail(Acdetail acdetail) {
		if(acdetail == null) return 0;
		acdetail.setZid(sessionManager.getBusinessId());
		acdetail.setZuuserid(getAuditUser());
		long count = acMapper.updateAcdetail(acdetail);
		if(count != 0) {
			count = updateAcheaderXstatusjv(acdetail.getXvoucher());
		}
		return count;
	}

	@Transactional
	@Override
	public long updateAcheaderXstatusjv(String xvoucher) {
		if(StringUtils.isBlank(xvoucher)) return 0;
		return acMapper.updateAcheaderXstatusjv(xvoucher, sessionManager.getBusinessId());
	}

	@Override
	public List<Acdetail> getAllAcdetail() {
		return acMapper.getAllAcdetail(sessionManager.getBusinessId());
	}

	@Override
	public Acdetail findAcdetailByXrowAndXvoucher(int xrow, String xvoucher) {
		if(xrow == 0 || StringUtils.isBlank(xvoucher)) return null;
		return acMapper.findAcdetailByXrowAndXvoucher(xrow, xvoucher, sessionManager.getBusinessId());
	}

	@Override
	public List<Acdetail> findAcdetailsByXvoucher(String xvoucher) {
		if(StringUtils.isBlank(xvoucher)) return Collections.emptyList();
		return acMapper.findAcdetailsByXvoucher(xvoucher, sessionManager.getBusinessId());
	}

	@Override
	public void procAcVoucherPost(Integer xyear, Integer xper, String xfvoucher, String xtvoucher) {
		acMapper.procAcVoucherPost(sessionManager.getBusinessId(), getAuditUser(), xyear, xper, xfvoucher, xtvoucher);
	}

	@Override
	public void procAcVoucherUnPost(Integer xyear, Integer xper, String xfvoucher, String xtvoucher) {
		acMapper.procAcVoucherUnPost(sessionManager.getBusinessId(), getAuditUser(), xyear, xper, xfvoucher, xtvoucher);
	}

	@Transactional
	@Override
	public long deleteAcheader(String xvoucher) {
		if(StringUtils.isBlank(xvoucher)) return 0;
		return acMapper.deleteAcheader(xvoucher, sessionManager.getBusinessId());
	}

	@Transactional
	@Override
	public long deleteAcdetail(int xrow, String xvoucher) {
		if(xrow == 0 || StringUtils.isBlank(xvoucher)) return 0;
		long count = acMapper.deleteAcdetail(xrow, xvoucher, sessionManager.getBusinessId());
		if(count != 0) {
			count = updateAcheaderXstatusjv(xvoucher);
		}
		return count;
	}

	@Override
	public Acheader setXyearAndXper(Acheader acheader, Acdef acdef) {
		if(acheader == null) return acheader;
		if(acdef == null) return acheader;

		Calendar cal = Calendar.getInstance();
		cal.setTime(acheader.getXdate());

		int year = cal.get(Calendar.YEAR);
		int per = 12 + (cal.get(Calendar.MONTH) + 1) - acdef.getXoffset();
		if(per <= 12) {
			year = year - 1;
		} else {
			per = per - 12;
		}

		acheader.setXyear(year);
		acheader.setXper(per);
		return acheader;
	}

	
}
