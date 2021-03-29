package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Opcrndetail;
import com.asl.entity.Opcrnheader;

@Component
public interface OpcrnService {
	
	public long save(Opcrnheader opcrnheader);
	public long update(Opcrnheader opcrnheader);
	
	public long saveDetail(Opcrndetail opcrndetail);
	public long updateDetail(Opcrndetail opcrndetail);
	public long deleteDetail(Opcrndetail opcrndetail);
	
	public List<Opcrnheader> getAllOpcrnheader();
	public List<Opcrndetail> findOpcrnDetailByXcrnnum(String xcrnnum);
	
	public Opcrnheader findOpcrnHeaderByXcrnnum(String xcrnnum);
	public Opcrnheader findOpcrnHeaderByXdornum(String xdornum);
	public Opcrndetail findOpcrnDetailByXcrnnumAndXrow(String xcrnnum, int xrow);
	
	//Procedure Calls
	public void procConfirmCRN(String xcrnnum, String p_seq);
	public void procTransferOPtoAR(String xdocnum, String p_screen, String p_seq);

}
