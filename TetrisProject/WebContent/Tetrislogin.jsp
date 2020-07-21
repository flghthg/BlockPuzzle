<%@page import="action.TetrisLogin_Action"%>
<%@page import="action.TetrisJoin_Write"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
       request.setCharacterEncoding("UTF-8");      

           //안드로이드에서 보내준 파라미터를 받긔.
           String returns= "";
           String type = request.getParameter("type");
           String id = request.getParameter("id");   
           
           System.out.println("type:" + type);
           System.out.println("id:" + id);
           
           if(type==null){
        	   return;
           }
           
           if(type.equals("type_join")){
              System.out.println(id);
              TetrisJoin_Write join_board = TetrisJoin_Write.getWrite();
               returns = join_board.write(id);
               //System.out.println(id);
              //안드로이드에 Json형식의 코드를 보내줌
              out.println(returns);
              
           }
           
           if(type.equals("type_login")){
              System.out.println(id);
              
              TetrisLogin_Action login_action = TetrisLogin_Action.getWrite();
              returns = login_action.login(id); 
              //안드로이드에 Json형식의 코드를 보내줌
              out.println(returns);
           
           }
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
</body>
</html>