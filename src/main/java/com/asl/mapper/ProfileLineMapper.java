package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.ProfileLine;
import com.asl.enums.ProfileType;

/**
 * @author Zubayer Ahamed
 * @since Feb 17, 2021
 */
@Mapper
public interface ProfileLineMapper {

	long save(ProfileLine profileLine);

	long update(ProfileLine profileLine);

	ProfileLine findByProfilelineid(Long profilelineid);

	List<ProfileLine> getAllByProfilecodeAndProfiletype(String profilecode, ProfileType profiletype, String zid);
}
