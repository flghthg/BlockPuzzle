package action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;



public class TetrisJoin_Write {

   static TetrisJoin_Write join = new TetrisJoin_Write();
   public static TetrisJoin_Write getWrite() {
      return join;
   }

   String returns = "";
   Connection conn = null;
   PreparedStatement pstmt = null;


   public String write(String id) {

   

         try {         
            //DB연결 
            Context init = new InitialContext();
            DataSource ds = (DataSource)init.lookup("java:comp/env/jdbc/oracle_test");

            conn = ds.getConnection();
            
            if(!id.trim().equals("")) {
            

            String sql = "insert into Tetris_USER values(seq_login_idx.nextVal,?)"; 
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.executeUpdate();

            //정보등록에 성공했다면
            returns = String.format("{res:[{'result':'%s'}]}","success");
            
            }else {
               
            returns = String.format("{res:[{'result':'%s'}]}","fail2");
               
            }


         } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            //정보등록 실패시

            returns = String.format("{res:[{'result':'%s'}]}","fail");

         }finally {
            try {

               pstmt.close();
               conn.close();

            } catch (Exception e2) {
               // TODO: handle exception
            }
         }//finally

         return returns;


   }//write()
}