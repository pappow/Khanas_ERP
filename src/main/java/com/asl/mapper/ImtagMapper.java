package com.asl.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.asl.entity.Imtag;

@Mapper
public interface ImtagMapper {
	public long save(Imtag imtag);
	public long update(Imtag imtag);
	
	public Imtag findImTagByXtagnum(String xtagnum, String zid);
	public List<Imtag> getAllImTag(String zid);
}
