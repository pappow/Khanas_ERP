package com.asl.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.asl.entity.DataList;
import com.asl.entity.ListHead;

/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 */
@Component
public interface ListService {

//	public ListHead save(ListHead listHead);
//	public ListHead save(ListHead listHead, String businessId);
//
//	public DataList save(DataList dataList);
//	public DataList save(DataList dataList, String businessId);
//
//	public ListHead findListHeadById(Long listHeadId);
//
//	public DataList findDataListById(Long dataListId);
//
//	public ListHead findListHeadByListCode(String listCode);
//	public ListHead findListHeadByListCode(String listCode, String businessId);
//
//	public List<DataList> findDataListByListCode(String listCode);
//	public List<DataList> findDataListByListCode(String listCode, String businessId);
//
//	public List<DataList> getList(String listCode, String... values);

	public List<ListHead> findAllListHead();

//	public List<Map<String, Object>> getExportDataByChunk(long limit, long offset);
//	public int archiveDataListByListCode(String listCode);
//	public int archiveDataListByListCode(String listCode, String businessId);
}
