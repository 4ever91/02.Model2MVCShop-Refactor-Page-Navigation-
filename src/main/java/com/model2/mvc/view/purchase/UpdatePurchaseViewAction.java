package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;

public class UpdatePurchaseViewAction extends Action {
	
	@Override
	public String execute(	HttpServletRequest request,
									HttpServletResponse response) throws Exception {

		HttpSession session=request.getSession();
		User user=((User)session.getAttribute("user"));
		
		PurchaseVO purchaseVO = new PurchaseVO();
		
		int tranNo = Integer.parseInt(request.getParameter("tranNo"));
		UserService userService = new UserServiceImpl();
		purchaseVO.setTranNo(tranNo);
		purchaseVO.setBuyer(userService.getUser(user.getUserId()));
		purchaseVO.setPaymentOption(request.getParameter("paymentOption"));
		
		request.setAttribute("purchaseVO", purchaseVO);
		
		return "forward:/purchase/updatePurchaseView.jsp";
	}

}
