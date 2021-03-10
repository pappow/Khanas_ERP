package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.DataList;
import com.asl.entity.Listhead;

/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 */
@Component
public interface ListService {

	public String modifiedListCode(String listCode);

	public long save(Listhead listHead);
	public long save(Listhead listHead, String businessId);

	public long update(Listhead listHead);
	public long update(Listhead listHead, String businessId);

	public long save(DataList dataList);
	public long save(DataList dataList, String businessId);

	public long update(DataList dataList);
	public long update(DataList dataList, String businessId);

	public Listhead findListHeadById(Long listHeadId);
	public DataList findDataListById(Long dataListId);

	public Listhead findListHeadByListCode(String listCode);
	public Listhead findListHeadByListCode(String listCode, String businessId);

	public List<DataList> findDataListByListCode(String listCode);
	public List<DataList> findDataListByListCode(String listCode, String businessId);

	public List<DataList> getList(String listCode, String... values);

	public List<Listhead> getAllListHead();

//	public List<Map<String, Object>> getExportDataByChunk(long limit, long offset);
//	public int archiveDataListByListCode(String listCode);
//	public int archiveDataListByListCode(String listCode, String businessId);
}
