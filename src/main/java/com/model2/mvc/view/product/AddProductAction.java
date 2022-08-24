package com.model2.mvc.view.product;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.product.vo.ProductVO;

public class AddProductAction  extends Action {

	@Override
	public String execute(	HttpServletRequest request,
									HttpServletResponse response) throws Exception  {
		
		if(FileUpload.isMultipartContent(request) ) {
			String tempDir="C:\\workspace\\02.Model2MVCShop(Refactor & Page Navigation)\\src\\main\\webapp\\images\\uploadFiles\\";
			//Strinf tempDir2="/uploadFiles/";
			
			DiskFileUpload fileUpload = new DiskFileUpload();
			fileUpload.setRepositoryPath(tempDir);
			fileUpload.setSizeMax(1024*1024*100);
			fileUpload.setSizeThreshold(1024*100);
			
			if(request.getContentLength() < fileUpload.getSizeMax()) {
				ProductVO productVO = new ProductVO();
				
				StringTokenizer token = null;
				ProductServiceImpl service = new ProductServiceImpl();
				
				List fileItemList = fileUpload.parseRequest(request);
				int Size = fileItemList.size();
				
				for (int i = 0; i < Size; i++) {
					FileItem fileItem = (FileItem)fileItemList.get(i);
					
					if(fileItem.isFormField()) {
						if(fileItem.getFieldName().equals("munuDate")) {
							token = new StringTokenizer(fileItem.getString("euc-kr"), "-");
							String munuDate = token.nextToken() +token.nextToken() + token.nextToken();
							productVO.setManuDate(munuDate);
						}
						else if(fileItem.getFieldName().equals("prodName"))
							productVO.setProdName(fileItem.getString("euc-kr"));
						else if(fileItem.getFieldName().equals("prodDetail"))
							productVO.setProdDetail(fileItem.getString("euc-kr"));
						else if(fileItem.getFieldName().equals("price"))
							productVO.setPrice(Integer.parseInt(fileItem.getString("euc-kr")));
					}else {
						if(fileItem.getSize() > 0) {
							int idx = fileItem.getName().lastIndexOf(".");
							if(idx == -1) {
								idx = fileItem.getName().lastIndexOf("/");
							}
							String fileName = fileItem.getName().substring(idx+1);
							productVO.setFileName(fileName);
							try {
								File uploadedFile = new File(tempDir, fileName);
								fileItem.write(uploadedFile);
							} catch (IOException e) {
								// TODO: handle exception
								System.out.println(e);
							}
						} else {
							productVO.setFileName("../../images/empty.GIF");
						}
					}
				}
				ProductService pservice = new ProductServiceImpl();
				pservice.insertProduct(productVO);
				
				request.setAttribute("pvo", productVO);
			}else {
				int overSize = (request.getContentLength()/1000000);
				System.out.println("<script> alert('파일의 크기는 1MB까지 입니다.  올리신 파일 용량은"
								+ overSize+ "MB입니다');");
				System.out.println("history.back();</script>");
			}
		}else {
			System.out.println("인코딩 타입이 multipart/form-data가 아닙니다.");
			ProductVO productVO = new ProductVO();
			
			productVO.setProdName(request.getParameter("prodName"));
			productVO.setProdDetail(request.getParameter("prodDetail"));
			productVO.setManuDate(request.getParameter("manuDate").replaceAll("-", ""));
			productVO.setPrice(Integer.parseInt(request.getParameter("price")));
			productVO.setFileName("../../images/empty.GIF");
			
			System.out.println(productVO);
			
			ProductService service = new ProductServiceImpl();
			service.insertProduct(productVO);
			
			request.setAttribute("pvo", productVO);
		}
		return "forward:/product/addProduct.jsp";
	}
		/*
		ProductVO productVO = new ProductVO();
		
		productVO.setProdName(request.getParameter("prodName"));
		productVO.setProdDetail(request.getParameter("prodDetail"));
		productVO.setManuDate(request.getParameter("manuDate"));
		productVO.setPrice(Integer.parseInt(request.getParameter("price")));
		productVO.setFileName(request.getParameter("fileName"));
		
		System.out.println(productVO);
		
		ProductService service = new ProductServiceImpl();
		service.insertProduct(productVO);
		
		request.setAttribute("pvo", productVO);
		
		return "forward:/product/addProduct.jsp";
		
		*/
}

