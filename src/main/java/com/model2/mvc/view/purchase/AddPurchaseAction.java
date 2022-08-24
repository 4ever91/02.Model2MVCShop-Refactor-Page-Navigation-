package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;

import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;

public class AddPurchaseAction extends Action {
	
	public String execute(	HttpServletRequest request,
									HttpServletResponse response) throws Exception {
		
		PurchaseVO purchaseVO = new PurchaseVO();
			
		HttpSession session=request.getSession();
		User user=((User)session.getAttribute("user"));
			
		UserService userService = new UserServiceImpl();
		purchaseVO.setBuyer(userService.getUser(user.getUserId()));
		
		ProductService productService = new ProductServiceImpl();
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));

		purchaseVO.setPurchaseProd(productService.findProduct(prodNo));
		
		purchaseVO.setPaymentOption(request.getParameter("paymentOption"));
		purchaseVO.setReceiverName(request.getParameter("receiverName"));
		purchaseVO.setReceiverPhone(request.getParameter("receiverPhone"));
		purchaseVO.setDlvyAddr(request.getParameter("receiverAddr"));
		purchaseVO.setDlvyRequest(request.getParameter("receiverRequest"));
		purchaseVO.setDlvyDate(request.getParameter("receiverDate"));
		System.out.println(purchaseVO);
		
		PurchaseService service = new PurchaseServiceImpl();
		service.addPurchase(purchaseVO);
		service.updateTranCodeByProd("SOL", prodNo);
		
		request.setAttribute("addPurchaseVO", purchaseVO);
		
		return "forward:/purchase/addPurchase.jsp";
	}
}
