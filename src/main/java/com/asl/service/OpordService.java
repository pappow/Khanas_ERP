package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Oporddetail;
import com.asl.entity.Opordheader;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Component
public interface OpordService {

	public long saveOpordHeader(Opordheader opordheader);
	public long updateOpordHeader(Opordheader opordheader);

	public long saveOpordDetail(Oporddetail oporddetail);
	public long updateOpordDetail(Oporddetail oporddetail);

	public Opordheader findOpordHeaderByXordernum(String xordernum);
	public Oporddetail findOporddetailByXordernumAndXrow(String xordernum, int xrow);

	public List<Oporddetail> findOporddetailByXordernum(String xordernum);
	public List<Opordheader> getAllOpordheader();
}
