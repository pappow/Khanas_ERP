package com.asl.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.DataList;
import com.asl.entity.ListHead;

/**
 * @author Zubayer Ahamed
 * @since Feb 16, 2021
 */
@Mapper
public interface ListMapper {

	public long saveListHead(ListHead listHead);
	public long saveDataList(DataList dataList);

	public long updateListHead(ListHead listHead);
	public long updateDataList(DataList dataList);

	public ListHead findListHeadById(Long listHeadId);
	public DataList findDataListById(Long listId);

	public ListHead findListHeadByListCode(String listCode, String zid);
	public List<DataList> findDataListByListCode(String listCode, String zid);

	public List<DataList> getList(String listCode, String zid, Map<String, String> params);

	public List<ListHead> getAllListHead(String zid);
}
