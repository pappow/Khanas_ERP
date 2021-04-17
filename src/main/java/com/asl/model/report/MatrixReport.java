package com.asl.model.report;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.asl.model.BranchesRequisitions;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Apr 15, 2021
 */
@Data
@XmlRootElement(name = "matrixreport")
@XmlAccessorType(XmlAccessType.FIELD)
public class MatrixReport{
	@XmlElementWrapper(name = "columns")
	@XmlElement(name = "column")
	List<TableColumn> columns = new ArrayList<>();

	@XmlElementWrapper(name = "rows")
	@XmlElement(name = "row")
	List<BranchRow> rows = new ArrayList<>();

	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	List<BranchesRequisitions> items = new ArrayList<>();
}
