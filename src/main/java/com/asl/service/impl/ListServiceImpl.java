package com.asl.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asl.entity.DataList;
import com.asl.entity.ListHead;
import com.asl.mapper.ListMapper;
import com.asl.service.ListService;

/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 */
@Service
public class ListServiceImpl extends AbstractGenericService implements ListService {

	@Autowired private ListMapper listMapper;

	@Override
	public String modifiedListCode(String pc) {
		if(StringUtils.isBlank(pc)) return "";
		pc = pc.toUpperCase();
		pc = pc.replace(" ", "_");
		return pc;
	}

	@Override
	public long save(ListHead listHead) {
		return save(listHead, sessionManager.getBusinessId());
	}

	@Override
	public long save(ListHead listHead, String businessId) {
		if(listHead == null) return 0;
		listHead.setListCode(modifiedListCode(listHead.getListCode()));
		listHead.setBusinessId(businessId);
		return listMapper.saveListHead(listHead);
	}

	@Override
	public long update(ListHead listHead) {
		return update(listHead, sessionManager.getBusinessId());
	}

	@Override
	public long update(ListHead listHead, String businessId) {
		if(listHead == null) return 0;
		listHead.setListCode(modifiedListCode(listHead.getListCode()));
		listHead.setBusinessId(businessId);
		return listMapper.updateListHead(listHead);
	}

	@Override
	public long save(DataList dataList) {
		return save(dataList, sessionManager.getBusinessId());
	}

	@Override
	public long save(DataList dataList, String businessId) {
		if(dataList == null) return 0;
		dataList.setBusinessId(businessId);
		return listMapper.saveDataList(dataList);
	}

	@Override
	public long update(DataList dataList) {
		return update(dataList, sessionManager.getBusinessId());
	}

	@Override
	public long update(DataList dataList, String businessId) {
		if(dataList == null) return 0;
		dataList.setBusinessId(businessId);
		return listMapper.updateDataList(dataList);
	}

	@Override
	public ListHead findListHeadById(Long listHeadId) {
		if(listHeadId == null) return null;
		return listMapper.findListHeadById(listHeadId);
	}

	@Override
	public DataList findDataListById(Long dataListId) {
		if(dataListId == null) return null;
		return listMapper.findDataListById(dataListId);
	}

	@Override
	public ListHead findListHeadByListCode(String listCode) {
		return findListHeadByListCode(listCode, sessionManager.getBusinessId());
	}

	@Override
	public ListHead findListHeadByListCode(String listCode, String businessId) {
		if(StringUtils.isBlank(listCode) || StringUtils.isBlank(businessId)) return null;
		return listMapper.findListHeadByListCode(listCode, businessId);
	}

	@Override
	public List<DataList> findDataListByListCode(String listCode) {
		return findDataListByListCode(listCode, sessionManager.getBusinessId());
	}

	@Override
	public List<DataList> findDataListByListCode(String listCode, String businessId) {
		if(StringUtils.isBlank(listCode) || StringUtils.isBlank(businessId)) return Collections.emptyList();
		List<DataList> list = listMapper.findDataListByListCode(listCode, businessId);
		if(list == null) return Collections.emptyList();
		return list;
	}

	@Override
	public List<DataList> getList(String listCode, String... values){
		if(StringUtils.isBlank(listCode)) return Collections.emptyList();

		Map<String, String> paramMap = new HashMap<>();
		if(values != null) {
			int index = 0;
			for(String value : values) {
				index++;
				if(StringUtils.isBlank(value)) continue;
				paramMap.put("listValue" + index, value);
			}
		}

		List<DataList> resultList = listMapper.getList(listCode, sessionManager.getBusinessId(), paramMap);
		return resultList != null ? resultList : Collections.emptyList();
	}

	@Override
	public List<ListHead> getAllListHead() {
		List<ListHead> list = listMapper.getAllListHead(sessionManager.getBusinessId());
		if(list == null) return Collections.emptyList();
		return list;
	}

//	@Override
//	public List<Map<String, Object>> getExportDataByChunk(long limit, long offset) {
//		StringBuilder sql = new StringBuilder("SELECT ")
//												.append("lh.listHeadId AS LH_ID, ")
//												.append("lh.listCode AS LH_CODE, ")
//												.append("lh.description AS LH_DESC, ")
//												.append("lh.prompt1 AS LH_PRMP_1, ")
//												.append("lh.prompt2 AS LH_PRMP_2, ")
//												.append("lh.prompt3 AS LH_PRMP_3, ")
//												.append("lh.prompt4 AS LH_PRMP_4, ")
//												.append("lh.prompt5 AS LH_PRMP_5, ")
//												.append("lh.prompt6 AS LH_PRMP_6, ")
//												.append("lh.prompt7 AS LH_PRMP_7, ")
//												.append("lh.prompt8 AS LH_PRMP_8, ")
//												.append("lh.prompt9 AS LH_PRMP_9, ")
//												.append("lh.prompt10 AS LH_PRMP_10, ")
//												.append("lh.prompt11 AS LH_PRMP_11, ")
//												.append("lh.prompt12 AS LH_PRMP_12, ")
//												.append("lh.prompt13 AS LH_PRMP_13, ")
//												.append("lh.prompt14 AS LH_PRMP_14, ")
//												.append("lh.prompt15 AS LH_PRMP_15, ")
//												.append("lh.prompt16 AS LH_PRMP_16, ")
//												.append("ls.listId AS LS_ID, ")
//												.append("ls.listCode AS LS_CODE, ")
//												.append("ls.listValue1 AS LS_VAL_1, ")
//												.append("ls.listValue2 AS LS_VAL_2, ")
//												.append("ls.listValue3 AS LS_VAL_3, ")
//												.append("ls.listValue4 AS LS_VAL_4, ")
//												.append("ls.listValue5 AS LS_VAL_5, ")
//												.append("ls.listValue6 AS LS_VAL_6, ")
//												.append("ls.listValue7 AS LS_VAL_7, ")
//												.append("ls.listValue8 AS LS_VAL_8, ")
//												.append("ls.listValue9 AS LS_VAL_9, ")
//												.append("ls.listValue10 AS LS_VAL_10, ")
//												.append("ls.listValue11 AS LS_VAL_11, ")
//												.append("ls.listValue12 AS LS_VAL_12, ")
//												.append("ls.listValue13 AS LS_VAL_13, ")
//												.append("ls.listValue14 AS LS_VAL_14, ")
//												.append("ls.listValue15 AS LS_VAL_15, ")
//												.append("ls.listValue16 AS LS_VAL_16 ")
//											.append("FROM ")
//												.append("lh ")
//												.append("LEFT JOIN ls ON ls.listCode = lh.listCode AND ls.status = 'L' AND ls.bsid='"+ sessionManager.getBusinessId() +"' ")
//											.append("WHERE ")
//												.append("lh.bsid = '"+ sessionManager.getBusinessId() +"' ")
//												.append("AND lh.statUS = 'L' ")
//												.append("ORDER BY lh.listCode ")
//												.append("OFFSET "+ offset +" ROWS ")
//												.append("FETCH NEXT "+ limit +" ROWS ONLY ");
//		return jdbcTemplate.queryForList(sql.toString());
//	}
//
//	@Transactional
//	@Override
//	public int archiveDataListByListCode(String listCode) {
//		return archiveDataListByListCode(listCode, sessionManager.getBusinessId());
//	}
//
//	@Transactional
//	@Override
//	public int archiveDataListByListCode(String listCode, String businessId) {
//		StringBuilder sql = new StringBuilder("UPDATE DataList ls SET ls.status=:stat WHERE ls.businessId=:bsid AND ls.status=:lstat AND UPPER(ls.listCode)=:lscd");
//		return em.createQuery(sql.toString())
//				.setParameter("stat", RecordStatus.D)
//				.setParameter("bsid", businessId)
//				.setParameter("lstat", RecordStatus.L)
//				.setParameter("lscd", listCode.toUpperCase())
//				.executeUpdate();
//	}

	

}