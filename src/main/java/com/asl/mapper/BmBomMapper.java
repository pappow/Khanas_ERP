package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.BmBomHeader;
import com.asl.entity.BmBomDetail;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Mapper
public interface BmBomMapper {
	
	public long saveBmBomHeader(BmBomHeader bmbomheader);
	public long updateBmBomHeader(BmBomHeader bmbomheader);

	public long saveBmBomDetail(BmBomDetail bmbomdetail);
	public long updateBmBomDetail(BmBomDetail bmbomdetail);

	public BmBomHeader findBmBomHeaderByXbomkey(String xbomkey, String zid);
	public BmBomDetail findBmBomdetailByXbomkeyAndXbomrow(String xbomrow, int xbomkey, String zid);

	public List<BmBomDetail> findBmBomdetailByXbomkey(String xbomkey, String zid);
	public List<BmBomHeader> getAllBmBomheader(String zid);

}
