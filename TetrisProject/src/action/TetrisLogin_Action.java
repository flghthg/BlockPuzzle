package action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;



public class TetrisLogin_Action {

   static TetrisLogin_Action login = new TetrisLogin_Action();
   public static TetrisLogin_Action getWrite() {
      return login;
   }

   String returns = "";
   Connection conn = null;
   PreparedStatement pstmt = null;
   //여기에는 하나씩 찾아봐야하기 때문에 resultSet이 필요함
   ResultSet rs = null;


   public String login( String id) {

      try {         
         //DB연결 
         Context init = new InitialContext();
         DataSource ds = (DataSource)init.lookup("java:comp/env/jdbc/oracle_test");

         conn = ds.getConnection();

            if(!id.trim().equals("")) {
         String sql = "select * from Tetris_USER where id=?";
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, id);

         rs = pstmt.executeQuery();

         //이게  while문 돌아가면서 확인함
         int cnt = 0;

         while(rs.next()) {
            rs.getString("id");
            cnt = 1 ;//로그인성공시 0->1

         }

         if(cnt == 1) {
            //로그인에 성공했다면
            returns = String.format("{res:[{'result':'%s','id':'%s'}]}","success",id);    

         }else{ //로그인 실패시

            returns =String.format("{res:[{'result':'%s','id':'%s'}]}","fail",id);

         }
         
            }else {
            	//여백일때
               
               returns =String.format("{res:[{'result':'%s','id':'%s'}]}","fail2",id);
            }

      } catch (Exception e) {
         // TODO: handle exception
         e.printStackTrace();
         //정보등록 실패시
      }finally {
         try {

            rs.close();
            pstmt.close();
            conn.close();

         } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
         }
      }//finally

      return returns;

   }//write()


}