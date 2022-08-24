package com.model2.mvc.service.purchase;

import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.purchase.vo.PurchaseVO;


public interface PurchaseService {
	
	public PurchaseVO getPurchase(int transCode) throws Exception;
	
	public Map<String, Object> getPurchaseList(Search search, String buyerId) throws Exception;
	
	public Map<String, Object> getSaleList(Search search) throws Exception;
	
	public void addPurchase(PurchaseVO purchaseVO ) throws Exception;
	
	public void updatePurchase(PurchaseVO purchaseVO) throws Exception;
	
	public void updateTranCode(String transCode, int tranNo) throws Exception;
	
	public void updateTranCodeByProd(String transCode, int prodNo) throws Exception;

}
