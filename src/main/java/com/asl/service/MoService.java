package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Modetail;
import com.asl.entity.Moheader;

/**
 * @author Zubayer Ahamed
 * @since Mar 18, 2021
 */
@Component
public interface MoService {

	public long saveBatchMoHeader(List<Moheader> moheaders);

	public long saveMoHeader(Moheader moheader);

	public long updateMoHeader(Moheader moheader);

	public long saveMoDetail(Modetail modetail);

	public long updateMoDetail(Modetail modetail);

	public Moheader findMoHeaderByXbatch(String xbatch);

	public Modetail findModetailByXrowAndXbatch(int xrow, String xbatch);

	public List<Modetail> findModetailByXbatch(String xbatch);

	public List<Moheader> getAllMoheader();

	public Moheader findMoheaderByXchalanAndXitem(String xchalan, String xitem);

	public Modetail findModetailByXbatchAndXitem(String xbatch, String xitem);

	public void processProduction(String batch, String action, String errseq);

	public Modetail findDefaultModetailByXbatch(String xbatch);
}
