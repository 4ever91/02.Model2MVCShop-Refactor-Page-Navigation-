package com.model2.mvc.view.product;

import java.net.URLDecoder;
import java.net.URLEncoder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;


public class GetProductAction extends Action { 

	@Override
	public String execute(	HttpServletRequest request,
									HttpServletResponse response) throws Exception  {
	
		int prodNo=Integer.parseInt(request.getParameter("prodNo"));
		
		Cookie[] cookies = request.getCookies();
		if(cookies!=null && cookies.length>0) {
		  for(int i=0;i<cookies.length;i++) {	
			  Cookie cookie = cookies[i];
			if(cookie.getName().equals("history")) {
				
				String historyCookie = URLDecoder.decode(cookie.getValue(), "euc-kr") ;
				//System.out.println("history Cookie value"+cookie.getValue());
				//historyCookie = cookie.getValue()+","+prodNo);
				cookie.setValue (URLEncoder.encode((historyCookie+","+prodNo), "euc-kr"));
				cookie.setMaxAge(60*60);
				response.addCookie(cookie);
			}else{
			cookie = new Cookie("history",request.getParameter("prodNo"));
			cookie.setMaxAge(60*60);
			response.addCookie(cookie);
			}
		  }
		}
						
		ProductService service=new ProductServiceImpl();
		ProductVO productVO=service.findProduct(prodNo);
		
		request.setAttribute("productVO", productVO);
		
		return "forward:/product/getProduct.jsp";
	}
}
