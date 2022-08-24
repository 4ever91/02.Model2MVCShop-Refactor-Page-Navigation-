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

public class UpdatePurchaseAction extends Action {
	
	@Override
	public String execute(	HttpServletRequest request,
									HttpServletResponse response) throws Exception {

		int tranNo = Integer.parseInt(request.getParameter("tranNo"));
		
		PurchaseVO purchaseVO = new PurchaseVO();
		HttpSession session=request.getSession();
		User user=((User)session.getAttribute("user"));
			
		
		
		PurchaseService getPurchaseService = new PurchaseServiceImpl();
		PurchaseVO getPurchaseVO = new PurchaseVO();
		getPurchaseVO = getPurchaseService.getPurchase(tranNo);
			
		UserService userService = new UserServiceImpl();
		purchaseVO.setBuyer(userService.getUser(user.getUserId()));
				
		purchaseVO.setTranNo(tranNo);
		purchaseVO.setPurchaseProd(getPurchaseVO.getPurchaseProd());
		purchaseVO.setPaymentOption(request.getParameter("paymentOption"));
		purchaseVO.setReceiverName(request.getParameter("receiverName"));
		purchaseVO.setReceiverPhone(request.getParameter("receiverPhone"));
		purchaseVO.setDlvyAddr(request.getParameter("receiverAddr"));
		purchaseVO.setDlvyRequest(request.getParameter("receiverRequest"));
		purchaseVO.setDlvyDate(request.getParameter("divyDate"));
		purchaseVO.setTranCode("SOL");
		System.out.println(purchaseVO);
		
		PurchaseService purchaseService = new PurchaseServiceImpl();
		purchaseService.updatePurchase(purchaseVO);
		
		request.setAttribute("purchaseVO", purchaseVO);
		return "forward:/purchase/updatePurchase.jsp";
		
	}
}
