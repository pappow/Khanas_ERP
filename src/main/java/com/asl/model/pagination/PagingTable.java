package com.asl.model.pagination;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Feb 22, 2021
 */
@Data
public class PagingTable {

	private String defaultShow;
	private List<Integer> showRecords; 
	private String hint;
	private List<HeaderColumn> headers = new ArrayList<>();
	private List<List<BodyRecord>> records = new ArrayList<>();
	private String totalText;
	private String selectedPage;
}
