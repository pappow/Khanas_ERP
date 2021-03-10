package com.asl.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.DataList;
import com.asl.entity.Listhead;

/**
 * @author Zubayer Ahamed
 * @since Feb 16, 2021
 */
@Mapper
public interface ListMapper {

	public long saveListHead(Listhead listHead);
	public long saveDataList(DataList dataList);

	public long updateListHead(Listhead listHead);
	public long updateDataList(DataList dataList);

	public Listhead findListHeadById(Long listHeadId);
	public DataList findDataListById(Long listId);

	public Listhead findListHeadByListCode(String listCode, String zid);
	public List<DataList> findDataListByListCode(String listCode, String zid);

	public List<DataList> getList(String listCode, String zid, Map<String, String> params);

	public List<Listhead> getAllListHead(String zid);
}
