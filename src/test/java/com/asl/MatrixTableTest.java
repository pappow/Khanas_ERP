package com.asl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import lombok.Data;

public class MatrixTableTest {


	@Test
	void testZbusinessSave() {
		
		
		List<Branch> branches = getData();
		branches.stream().forEach(b -> {
			System.out.println(b.getName());
			b.getItems().stream().forEach(i -> {
				System.out.print(i.toString());
			});
			System.out.println();
			b.getTotalMap().entrySet().stream().forEach(i -> {
				System.out.println(i.getKey() + " = " + i.getValue());
			});
			System.out.println();
		});
		
	}

	private List<Branch> getData(){
		List<Branch> branches = new ArrayList<Branch>();
		
		for(int i = 1; i <= 5; i++) {
			Branch b = new Branch();
			b.setName("Branch-" + i);

			List<Item> items = new ArrayList<Item>();
			for(int j = 1; j < 5; j++) {
				Item item = new Item();
				item.setName("item-" + j);
				item.setQty(j);

				if(b.getTotalMap().get(item.getName()) != null) {
					int total = b.getTotalMap().get(item.getName()).intValue() + item.getQty();
					b.getTotalMap().put(item.getName(), total);
				} else {
					b.getTotalMap().put(item.getName(), item.getQty());
				}
				items.add(item);
			}
			b.setItems(items);
			branches.add(b);
		}

		return branches;
	}
}

@Data
class Branch{
	private String name;
	private List<Item> items;
	Map<String, Integer> totalMap = new HashMap<String, Integer>();
}

@Data
class Item{
	private String name;
	private int qty;
}