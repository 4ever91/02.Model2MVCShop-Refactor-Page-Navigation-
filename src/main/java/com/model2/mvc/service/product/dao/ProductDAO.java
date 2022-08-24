package com.model2.mvc.service.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.vo.ProductVO;

public class ProductDAO {

	public ProductDAO() {
		// TODO Auto-generated constructor stub
	}
	
	public void insertProduct(ProductVO productVO) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "INSERT INTO product VALUES (seq_product_prod_no.NEXTVAL,?,?,?,?,?,sysdate)";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, productVO.getProdName());
		stmt.setString(2, productVO.getProdDetail());
		stmt.setString(3, productVO.getManuDate());
		stmt.setInt(4, productVO.getPrice());
		stmt.setString(5, productVO.getFileName());
		
		stmt.executeUpdate();
		
		stmt.close();
		con.close();
	}


	public ProductVO findProduct(int prodNo) throws Exception {
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT * FROM product WHERE prod_no=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1,prodNo);

		ResultSet rs = stmt.executeQuery();

		ProductVO productVO =new ProductVO();
		while (rs.next()) {
			
			productVO.setProdNo(rs.getInt("PROD_NO"));
			productVO.setProdName(rs.getString("PROD_NAME"));
			productVO.setProdDetail(rs.getString("PROD_DETAIL"));
			productVO.setManuDate(rs.getString("MANUFACTURE_DAY"));
			productVO.setPrice(rs.getInt("PRICE"));
			productVO.setFileName(rs.getString("IMAGE_FILE"));
			productVO.setRegDate(rs.getDate("REG_DATE"));
		}
		
		rs.close();
		stmt.close();
		con.close();
		
		return productVO;
	}
	
	public Map<String,Object> getProductList(Search search) throws Exception {
		
		Map<String , Object>  map = new HashMap<String, Object>();
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT prod_no, prod_name, price, reg_date FROM product ";
		
		if (search.getSearchCondition() != null) {
			if (search.getSearchCondition().equals("0") &&  !search.getSearchKeyword().equals("") ) {
				sql += " WHERE prod_no LIKE '%" + search.getSearchKeyword()+"%'";
			} else if (search.getSearchCondition().equals("1") &&  !search.getSearchKeyword().equals("") ) {
				sql += " WHERE prod_name LIKE '%" + search.getSearchKeyword()+"%'";
			} else if (search.getSearchCondition().equals("2") &&  !search.getSearchKeyword().equals("") ) {
				sql += " WHERE price LIKE '%" + search.getSearchKeyword()+"%'";
			}
		}
		sql += " ORDER BY prod_no";

		System.out.println("ProductDAO::Original SQL :: " + sql);
		
		int totalCount = this.getTotalCount(sql);
		System.out.println("ProductDAO :: totalCount  :: " + totalCount);
		
		sql = makeCurrentPageSql(sql, search);
		
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();

		System.out.println(search);
		
		List<ProductVO> list = new ArrayList<ProductVO>();
		
		while (rs.next()) {
			ProductVO vo = new ProductVO();
			vo.setProdNo(rs.getInt("prod_no"));
			vo.setProdName(rs.getString("prod_name"));
			//vo.setProdDetail(rs.getString("PROD_DETAIL"));
			//vo.setManuDate(rs.getString("MANUFACTURE_DAY"));
			vo.setPrice(rs.getInt("price"));
			//vo.setFileName(rs.getString("IMAGE_FILE"));
			vo.setRegDate(rs.getDate("reg_date"));
			list.add(vo);
			
		}
		
		map.put("productTotalCount", new Integer(totalCount));
		map.put("productList", list);
		
		rs.close();
		stmt.close();
		con.close();
			
		return map;
	}
	
public void updateProduct(ProductVO productVO) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "Update product SET prod_name=?,prod_detail=?,manufacture_day=?,price=? WHERE prod_no=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, productVO.getProdName());
		stmt.setString(2, productVO.getProdDetail());
		stmt.setString(3, productVO.getManuDate());
		stmt.setInt(4, productVO.getPrice());
		stmt.setInt(5, productVO.getProdNo());
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
	
	System.out.println("ProductDAO :: make SQL :: "+ sql);	
	
	return sql;
	}
}

