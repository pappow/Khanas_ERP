package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Bmbomheader;
import com.asl.entity.Bmbomdetail;

/**
 * @author Zubayer Ahamed
 * @since Mar 8, 2021
 */
@Mapper
public interface BmbomMapper {
	
	public long saveBmBomHeader(Bmbomheader bmbomheader);
	public long updateBmBomHeader(Bmbomheader bmbomheader);

	public long saveBmBomDetail(Bmbomdetail bmbomdetail);
	public long updateBmBomDetail(Bmbomdetail bmbomdetail);

	public Bmbomheader findBmBomHeaderByXbomkey(String xbomkey, String zid);
	public Bmbomdetail findBmBomdetailByXbomkeyAndXbomrow(String xbomrow, int xbomkey, String zid);

	public List<Bmbomdetail> findBmBomdetailByXbomkey(String xbomkey, String zid);
	public List<Bmbomheader> getAllBmBomheader(String zid);

}
