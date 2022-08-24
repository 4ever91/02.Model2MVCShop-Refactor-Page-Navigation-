package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;

public class PurchaseDAO {

	public PurchaseDAO() {
		// TODO Auto-generated constructor stub
	}
	
	public PurchaseVO findPurchase(int tranNo ) throws Exception {
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT * FROM transaction WHERE tran_no=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1,tranNo);

		ResultSet rs = stmt.executeQuery();

		PurchaseVO purchaseVO =new PurchaseVO();
		while (rs.next()) {
			
			purchaseVO.setTranNo(tranNo);
			ProductService productService = new ProductServiceImpl();
			purchaseVO.setPurchaseProd(productService.findProduct(rs.getInt("PROD_NO")));
			UserService userService = new UserServiceImpl();
			purchaseVO.setBuyer(userService.getUser(rs.getString("BUYER_ID")));
			purchaseVO.setPaymentOption(rs.getString("PAYMENT_OPTION"));
			purchaseVO.setReceiverName(rs.getString("RECEIVER_NAME"));
			purchaseVO.setDlvyAddr(rs.getString("DLVY_ADDR"));
			purchaseVO.setDlvyRequest(rs.getString("RECEIVER_PHONE"));
			purchaseVO.setReceiverPhone(rs.getString("DLVY_REQUEST"));
			purchaseVO.setTranCode(rs.getString("TRAN_STATUS_CODE"));
			purchaseVO.setOrderDate(rs.getDate("ORDER_DATE"));
			purchaseVO.setDlvyDate(rs.getString("DLVY_DATE"));
		}
		
		rs.close();
		stmt.close();
		con.close();
		
		return purchaseVO;
	}
	
	public Map<String, Object> getPurchaseList(Search search, String buyerId) throws Exception {
		// TODO Auto-generated method stub
		Map<String , Object>  map = new HashMap<String, Object>();
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT t.tran_no, p.prod_no, t.receiver_phone, t.tran_status_code "
				+ " FROM transaction t, product p, users u "
				+ " WHERE t.prod_no = p.prod_no"
				+ " AND t.buyer_id = u.user_id"
				+ " AND u.user_id = '"+buyerId+"'";
				
		sql += " ORDER BY t.tran_no";

		System.out.println("PurchaseDAO::Original SQL :: " + sql);
		
		int totalCount = this.getTotalCount(sql);
		System.out.println("PurchaseDAO :: totalCount  :: " + totalCount);
		
		sql = makeCurrentPageSql(sql, search);
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();

		System.out.println(search);
		
		List<PurchaseVO> list = new ArrayList<PurchaseVO>();
		
		while (rs.next()) {
				PurchaseVO vo = new PurchaseVO();
				vo.setTranNo(rs.getInt("TRAN_NO"));
				ProductService productService = new ProductServiceImpl();
				vo.setPurchaseProd(productService.findProduct(rs.getInt("PROD_NO")));
				//vo.getBuyer().setUserId(rs.getString("BUYER_ID"));
				//vo.setPaymentOption(rs.getString("PAYMENT_OPTION"));
				//vo.setReceiverName(rs.getString("RECEIVER_NAME"));
				//vo.setDivyAddr(rs.getString("DLVY_ADDR"));
				vo.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
				//vo.setDivyRequest(rs.getString("DLVY_REQUEST"));
				//vo.setOrderDate(rs.getDate("ORDER_DATA"));
				vo.setTranCode(rs.getString("TRAN_STATUS_CODE"));
				//vo.setDivyDate(rs.getDate("DLVY_DATE"));
				list.add(vo);
			}

		map.put("purchaseTotalCount", new Integer(totalCount));
		map.put("purchaseList", list);
	
		rs.close();
		stmt.close();
		con.close();
		
		return map;
	}

	public Map<String, Object> getSaleList(Search search) throws Exception {
		// TODO Auto-generated method stub
		Map<String , Object>  map = new HashMap<String, Object>();
		Connection con = DBUtil.getConnection();
		
		String sql = "SELELCT * FROM transaction ";
		if (search.getSearchCondition() != null) {
			if (search.getSearchCondition().equals("0") &&  !search.getSearchKeyword().equals("") ) {
				sql += " WHERE prod_no = '" + search.getSearchKeyword()+"'";
			} else if (search.getSearchCondition().equals("1") &&  !search.getSearchKeyword().equals("") ) {
				sql += " WHERE prod_name = '" + search.getSearchKeyword()+"'";
			} else if (search.getSearchCondition().equals("2") &&  !search.getSearchKeyword().equals("") ) {
				sql += " WHERE price='" + search.getSearchKeyword()+"'";
			}
		}
		sql += " ORDER BY prod_no";

		System.out.println("PurchaseDAO::Original SQL :: " + sql);
		
		int totalCount = this.getTotalCount(sql);
		System.out.println("PurchaseDAO :: totalCount  :: " + totalCount);
		
		sql = makeCurrentPageSql(sql, search);
		
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();

		System.out.println(search);
		
		List<PurchaseVO> list = new ArrayList<PurchaseVO>();
				
		while (rs.next()) {
				PurchaseVO vo = new PurchaseVO();
				vo.setTranNo(rs.getInt("TRAN_NO"));
				vo.getPurchaseProd().setProdNo(rs.getInt("PROD_NO"));
				vo.getBuyer().setUserId(rs.getString("BUYER_ID"));
				vo.setPaymentOption(rs.getString("PAYMENT_OPTION"));
				vo.setReceiverName(rs.getString("RECEIVER_NAME"));
				vo.setDlvyAddr(rs.getString("DLVY_ADDR"));
				vo.setDlvyRequest(rs.getString("RECEIVER_PHONE"));
				vo.setReceiverPhone(rs.getString("DLVY_REQUEST"));
				vo.setTranCode(rs.getString("TRAN_STATUS_CODE"));
				vo.setOrderDate(rs.getDate("ORDER_DATA"));
				vo.setDlvyDate(rs.getString("DLVY_DATE"));
				
				list.add(vo);
		}
		
		map.put("totalCount", new Integer(totalCount));
		map.put("list", list);
	
		rs.close();
		stmt.close();
		con.close();
		
		return map;
	}

	public void insertPurchase(PurchaseVO purchaseVO) throws Exception {
		Connection con = DBUtil.getConnection();

		String sql = "INSERT INTO transaction VALUES (seq_transaction_tran_no.NEXTVAL,?,?,?,?,?,?,?,?,SYSDATE,TO_DATE(?))";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, purchaseVO.getPurchaseProd().getProdNo());
		stmt.setString(2, purchaseVO.getBuyer().getUserId());
		stmt.setString(3, purchaseVO.getPaymentOption());
		stmt.setString(4, purchaseVO.getReceiverName());
		stmt.setString(5, purchaseVO.getReceiverPhone());
		stmt.setString(6, purchaseVO.getDlvyAddr());
		stmt.setString(7, purchaseVO.getDlvyRequest());
		stmt.setString(8, purchaseVO.getTranCode());
		stmt.setString(9, purchaseVO.getDlvyDate());
		
		stmt.executeUpdate();
		
		stmt.close();
		con.close();
	}
	
	public void updatePurchase(PurchaseVO purchaseVO) throws Exception {
		// TODO Auto-generated method stub
		Connection con = DBUtil.getConnection();

		String sql = "UPDATE transaction SET PROD_NO=?,BUYER_ID=?,PAYMENT_OPTION=?,RECEIVER_NAME=?, DLVY_ADDR=?,RECEIVER_PHONE=?,"
				+ "DLVY_REQUEST=?,TRAN_STATUS_CODE=?,ORDER_DATE=?,DLVY_DATE=? WHERE TRAN_NO=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, purchaseVO.getPurchaseProd().getProdNo());
		stmt.setString(2, purchaseVO.getBuyer().getUserId());
		stmt.setString(3, purchaseVO.getPaymentOption());
		stmt.setString(4, purchaseVO.getReceiverName());
		stmt.setString(5, purchaseVO.getDlvyAddr());
		stmt.setString(6, purchaseVO.getDlvyRequest());
		stmt.setString(7, purchaseVO.getReceiverPhone());
		stmt.setString(8, purchaseVO.getTranCode());
		stmt.setDate(9, purchaseVO.getOrderDate());
		stmt.setString(10, purchaseVO.getDlvyDate());
		stmt.setInt(11, purchaseVO.getTranNo());
		
		stmt.executeUpdate();
		
		stmt.close();
		con.close();
	}

	public void updateTranCode(String transCode, int tranNo) throws Exception {
		// TODO Auto-generated method stub
		Connection con = DBUtil.getConnection();

		String sql = "UPDATE transaction SET tran_status_code=? WHERE tran_no=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, transCode);
		stmt.setInt(2, tranNo);
		
		stmt.executeUpdate();
		
		stmt.close();
		con.close();
	}
	
	public void updateTranCodeByProd(String transCode, int prodNo) throws Exception {

		Connection con = DBUtil.getConnection();

		String sql = "UPDATE transaction SET tran_status_code=? WHERE prod_no=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, transCode);
		stmt.setInt(2, prodNo);
		
		stmt.executeUpdate();
		
		stmt.close();
		con.close();
	}

private int getTotalCount(String sql) throws Exception {
	
	sql = "SELECT COUNT(*) "+
	          "FROM ( " +sql+ ") countTable";
	
	Connection con = DBUtil.getConnection();
	PreparedStatement pStmt = con.prepareStatement(sql);
	ResultSet rs = pStmt.executeQuery();
	
	int totalCount = 0;
	if( rs.next() ){
		totalCount = rs.getInt(1);
	}
	
	rs.close();
	pStmt.close();
	con.close();
	
	return totalCount;
}

// 게시판 currentPage Row 만  return 
private String makeCurrentPageSql(String sql , Search search){
	sql = 	"SELECT * "+ 
				"FROM (		SELECT inner_table. * ,  ROWNUM AS row_seq " +
								" 	FROM (	"+sql+" ) inner_table "+
								"	WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
				"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
	
	System.out.println("PurchaseDAO :: make SQL :: "+ sql);	
	
	return sql;
	}
}


