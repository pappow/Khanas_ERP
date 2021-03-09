package com.asl.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.asl.entity.ImTag;

@Mapper
public interface ImTagMapper {
	public long save(ImTag imtag);
	public long update(ImTag imtag);
	
	public ImTag findImTagByXtagnum(String xtagnum, String zid);
	public List<ImTag> getAllImTag(String zid);
}
