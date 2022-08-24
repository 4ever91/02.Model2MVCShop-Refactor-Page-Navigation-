package com.model2.mvc.service.product;

import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.product.vo.ProductVO;

public interface ProductService {
	
	public ProductVO findProduct(int prodNo) throws Exception;
	
	public Map<String, Object> getProductList(Search search) throws Exception; 
	
	public void insertProduct(ProductVO productVO) throws Exception;
	
	public void updateProduct (ProductVO productVO) throws Exception;

}
