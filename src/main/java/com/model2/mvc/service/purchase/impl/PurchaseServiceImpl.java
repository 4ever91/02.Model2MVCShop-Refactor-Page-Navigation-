package com.model2.mvc.service.purchase.impl;

import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.dao.PurchaseDAO;
import com.model2.mvc.service.purchase.vo.PurchaseVO;

public class PurchaseServiceImpl implements PurchaseService {

	private PurchaseDAO purchaseDAO;
	
	public PurchaseServiceImpl() {
		purchaseDAO = new PurchaseDAO();
		// TODO Auto-generated constructor stub
	}

	@Override
	public PurchaseVO getPurchase(int transNo) throws Exception {
		return purchaseDAO.findPurchase(transNo);
	}

	@Override
	public Map<String, Object> getPurchaseList(Search search, String buyerId) throws Exception {
		// TODO Auto-generated method stub
		return purchaseDAO.getPurchaseList(search, buyerId);
	}

	@Override
	public Map<String, Object> getSaleList(Search search) throws Exception {
		// TODO Auto-generated method stub
		return purchaseDAO.getSaleList(search);
	}

	@Override
	public void addPurchase(PurchaseVO purchaseVO) throws Exception {
		// TODO Auto-generated method stub
		purchaseDAO.insertPurchase(purchaseVO);
	}
	
	@Override
	public void updatePurchase(PurchaseVO purchaseVO) throws Exception {
		// TODO Auto-generated method stub
		purchaseDAO.updatePurchase(purchaseVO);
	}
	
	@Override
	public void updateTranCode(String transCode, int tranNo) throws Exception {
		// TODO Auto-generated method stub
		purchaseDAO.updateTranCode(transCode, tranNo);
	}
	
	@Override
	public void updateTranCodeByProd(String transCode, int prodNo) throws Exception {
		purchaseDAO.updateTranCodeByProd(transCode, prodNo);
	}
	
}
