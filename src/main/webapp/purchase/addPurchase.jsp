<%@ page language="java" contentType="text/html; charset=EUC-KR"%>
<%@ page import="com.model2.mvc.service.purchase.vo.*" %>

<%
	PurchaseVO vo = (PurchaseVO)request.getAttribute("addPurchaseVO");
%>	

<html>
<head>
<title>Insert title here</title>
</head>

<body>

<form name="addPurchase" action="/addPurchase.do?prodNo=<%=vo.getPurchaseProd().getProdNo() %>" method="post">

������ ���� ���Ű� �Ǿ����ϴ�.

<table border=1>
	<tr>
		<td>��ǰ��ȣ</td>
		<td><%=vo.getPurchaseProd().getProdNo() %></td>
		<td></td>
	</tr>
	<tr>
		<td>�����ھ��̵�</td>
		<td><%=vo.getBuyer().getUserId() %></td>
		<td></td>
	</tr>
	<tr>
		<td>���Ź��</td>
		<td>
			<% if (vo.getPaymentOption().equals("1")) { %>
				���ݱ���
			<%} else if(vo.getPaymentOption().equals("2"))  { %>
				�ſ뱸��
			<% } %>
		</td>
		<td></td>
	</tr>
	<tr>
		<td>�������̸�</td>
		<td><%=vo.getReceiverName() %></td>
		<td></td>
	</tr>
	<tr>
		<td>�����ڿ���ó</td>
		<td><%=vo.getReceiverPhone() %></td>
		<td></td>
	</tr>
	<tr>
		<td>�������ּ�</td>
		<td><%=vo.getDlvyAddr() %></td>
		<td></td>
	</tr>
	<tr>
		<td>���ſ�û����</td>
		<td><%=vo.getDlvyRequest() %></td>
		<td></td>
	</tr>
	<tr>
		<td>����������</td>
		<td><%=vo.getDlvyDate() %></td>
		<td></td>
	</tr>
</table>
</form>

</body>
</html>