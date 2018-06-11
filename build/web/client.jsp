
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.ResultSetMetaData"%>
<%@page import="paquete.prueba"%>
<%@page import="paquete.Conexion"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html lang="es">
<head>
 

  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>Login</title>
    <%

//allow access only if session exists
String user = null;
if(session.getAttribute("user") == null){
  response.sendRedirect("index.html");
%>
<script>window.location.href='index.html';</script>
<%
  System.out.println("Por que?");

}else {user = (String) session.getAttribute("user");}
System.out.println("user: "+session.getAttribute("user"));
String id = String.valueOf(session.getAttribute("id"));
String userName = null;
String sessionID = null;
Cookie[] cookies = request.getCookies();
if(cookies !=null){
for(Cookie cookie : cookies){
  if(cookie.getName().equals("user")) userName = cookie.getValue();
  if(cookie.getName().equals("JSESSIONID")) sessionID = cookie.getValue();
}
}else{
  sessionID = session.getId();
}
%>
<!DOCTYPE html>


  <!-- CSS  -->
  <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
  <link rel="stylesheet" type="text/css" href="js/validetta101/validetta.min.css">
  <link href="css/materialize.css" type="text/css" rel="stylesheet" media="screen,projection"/>
  <link href="css/style2.css" type="text/css" rel="stylesheet" media="screen,projection"/>
  <link rel="stylesheet" type="text/css" href="js/confirm/jquery-confirm.min.css">
  
  <script src="js/jquery-3.1.1.min.js"></script>
  <script src="js/validetta101/validetta.min.js"></script>
  <script src="js/validetta101/validetta.js"></script>
  <script src="js/index.js"></script>
  <script src="js/signin.js"></script>
  <script src="js/materialize.js"></script>

<script src="js/confirm/jquery-confirm.min.js"></script>


  <script src="js/init.js"></script>
  <script>

      $(document).ready(function() {
        

        $("#Download_Image").on("click",function(){
          $('#dl').val('0');
        });
        $("#Download_Key").on("click",function(){
          $('#dl').val('1');
        });

      });
  </script>
</head>
<body> 


    <!--Modifica los datos del alumno con la instruccion update en el crud.js y update_AX-->
    <div id="modalSignIn" class="modal">
      <form id="signIn" >
        <div class="modal-content">
          <h4 class="center-align blue white-text"></h4>
            
            <div class="row">
              <div class="col s12 l12 input-field">
                <label for="user">User:</label>
                <input type="text" id="user2" name="user2" data-validetta="required">
              </div>
            </div>
            <div class="row">
              <div class="col s12 l12 input-field">
                <label for="pass">Password:</label>
                <input type="text" id="pass2" name="pass2" data-validetta="required,minLength[8],regExp[passwords]">
              </div>
            </div>

                <br>

           <div class="row">
                <div class="col s12 l12">
                   <button type="submit" class="btn green">Submit</button>
               </div>
           </div>
        </div>
        <div class="modal-footer">
           
                   <button class="modal-close waves-effect waves-red btn-flat">Close</button>
          
        </div>
      </form>
    </div> 
  <div id="modalAX" class="modal">
      <div class="modal-content">
          <h4 class="center-align blue white-text "><div class="fuente">Artcripted</div></h4>
        </div>
      </div>
  </div>
  <div id="index-banner" class="parallax-container">

      <div class="container">
      	<div class="row">
            <div class="col s6 l6 input-field">
                <form action="<%=response.encodeURL("Logout") %>" method="post">
					       <input type="submit" class="btn red" value="Logout" >
				      </form>  
            </div>
        </div>
        <br><br>
        <h2 class="header center teal-text text-lighten-2"><div style="color:#FFF; font-family: 'Times New Roman', Times, serif;">Hi <%=userName %>, Login successful. Your Session ID=<%=sessionID %></div></h2>
        <div class="row center">
        </div>
        <div class="container">

            <div class="row center">
              <div class="col s12 l6 input-field">
                <!--<label for="user" id="luser"><strong>User:</strong></label>
                  <input type="text" id="user" name="user" data-validetta="required" style=" font-size: 2em;">
              	-->
              </div>
              <div class="col s12 l6 input-field">
                <!--<label for="pass" id="lpass"><strong>Password:</strong></label>
                  <input type="password" id="pass" name="pass" data-validetta="required" style=" font-size: 2em;">
                 -->
              </div>
            </div>

         
            <br>
          <div class="row center">
            <div class="col s12 l12 input-field" id="openMod">
                <div class="col s12 l6 input-field">
                	<form method="post">
						<input type="submit" class="btn black" value="Generate new keys" >
					</form>
              	</div>
				<div class="col s12 l6 input-field">
                	<form action="<%=response.encodeURL("DownloadFileServlet") %>" method="get">
						<input type="submit" class="btn black" value="Download digital certificate" >
					</form>
              	</div>
            </div>
          </div>
        </div>
        <br><br>

      </div>

    <div class="parallax"><img src="img/wu1.jpg" alt="Unsplashed background img 1"></div>
  </div>


  <div class="container">
    <div class="section">
      <!--   Icon Section   -->
        <div class="icon-block">
          <div class="row center">
            <div class="col s12 m12 l12">
              <h3 class="blue-text">Image upload</h3>
              <br>
            </div>
          </div>
            
          <form enctype="multipart/form-data" id="uploadImage" action="UploadImage" method="post">
            <div class="row">
              <div class="file-field input-field">
                <div class="btn">
                  <span>Select the image</span>
                  <input type="file" name="image" accept="image/*" data-validetta="required">
                </div>
              <input type="hidden" name="user_id" id="user_id" value="<%=id%>">
              <input type="hidden" name="receiver" id="receiver" value="artist">
              <div class="file-path-wrapper">
                  <input class="file-path validate" name="img_name" type="text">
              </div>
                  </div>
              </div>
              <div class="row center">
                  <input type="submit" class="btn purple" value="Submit" >
              </div>
          </form>
        </div>
        <div class="row center">
          <p style="color: #888;">- - - - - - - - - - - - - - - - - - - - - - - - - -</p>
        </div>
        <div class="row center">
            <div class="col s12 m12 l12">
              <h3 class="blue-text">Received images</h3>
              <br>
            </div>
          </div>
<%

        String selectSQL = "SELECT * FROM image WHERE receiver=?";
        ResultSet rs = null;
        String text = "0";
 
        try (Connection conn=Conexion.getConexion();
                PreparedStatement pstmt = conn.prepareStatement(selectSQL);) {
                        // set parameter;
                pstmt.setString(1, id);
                System.out.println(id);
                rs = pstmt.executeQuery();
          while (rs.next()) {



%>
  <form action='<%=response.encodeURL("DownloadFileServlet") %>' method='post'>
    <div class="row center">
      <div class="col s12 m12 l12">
        <img class="responsive-img" src="img/file.png" style="max-width: 15%">
      </div>
    </div>
    <div class="row center">
      <div class="col s12 m12 l12">
        <input type="hidden" name="id" value="<%=rs.getString("id")%>">
        <input type="hidden" name="name" value="<%=rs.getString("img_name")%>">
        <input type="hidden" name="dl" id="dl" value="0">
        <h4><%=rs.getString("img_name")%></h4>
      </div>
    </div>
    <div class="row center">
      <div class="col s12 m12 l6">
        <input type="submit" class="btn blue" id="Download_Image" value="Download image" style="width:75%;">
      </div>
      <div class="col s12 m12 l6">
        <input type="submit" class="btn green" id="Download_Key" value="Download key" style="width:75%;" >
      </div>
    </div>
  </form>
  <div class="row center">
    <p style="color: #888;">- - - - - - - - - - - - - - - - - - - - - - - - - -</p>
  </div>
<%
                }
            } catch (SQLException e) {
                text="2";

            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {

                    rs.close();
                }
            } catch (SQLException e) {
                text="3";
                System.out.println(e.getMessage());
            }
        }

%>

    </div>
  </div>




  <div class="parallax-container valign-wrapper" >
    <div class="container">
        <div class="row center">
            <div class="col s12 m12 l12">
                <h2 class="black-text">Decrypt an image</h2>
                <br>
            </div>
        </div>
        <div class="row center">
            <div class="col s12 m12 l12">
                <h3 class="purple-text">Steps:</h3>
            </div>
        </div>

  
        <div class="row center">
            <div class="col s12 m12 l12">
                <h4 class="white-text">1) Upload the files</h4>
            </div>
        </div>
        <div class="row">
            <div class=" col s12 m12 l12 file-field input-field">
                <div class="btn">
                    <span>Select the encrypted image</span>
                    <input type="file" name="enc_img" accept="image/*" data-validetta="required">
                    
                </div>
                <input type="hidden" name="user" id="user" value="admin">
                <div class="file-path-wrapper">
                    <input class="file-path validate white-text" type="text">
                </div>
            </div>
        </div>
        <div class="row">                  
          <div class=" col s12 m12 l12 file-field input-field">
            <div class="btn">
                <span>Select the encrypted key</span>
                <input type="file" name="enc_key" >         
            </div>
            <div class="file-path-wrapper">
                <input class="file-path validate white-text" type="text" data-validetta="required">
            </div>
          </div>
        </div>
        <div class="row center">
          <div class="col s12 m12 l12">
              <h4 class="white-text">2) Download the decrypted img</h4>
          </div>
        </div>
        <div class="row center">
          <div class="col s12 m6 l12">
            <form action="<%=response.encodeURL("DownloadEncImg") %>" method="post">
	          <input type="submit" class="btn black" value="Download Decrypted Image" >
          </div>
        </div>                    
    </div>
    <div class="parallax"><img src="img/wu2.jpg" alt="Unsplashed background img 2"></div>
  </div>



  <div class="container">
    <div class="section">
            <div class="row center">
                <div class="col s12 m12 l12">
                    <h3 class="green-text"> Confirm the price to pay with digital certificate</h3>
                </div>
            </div>
            <form>
              <div class="row center">
                <div class="col s12 m12 l12 input-fields">
                  <i class="material-icons prefix">account_circle</i>
  
                  <input name="message" id="icon_prefix" type="text" class="validate" data-validetta="required,maxLength[255]">
                  <label for="icon_prefix">Enter the message:</label>
                </div>
              </div>
              <div class="row center">
                <div class="col s12 m12 l12">
                  <input type="submit" class="btn blue" value="Submit">
                </div>
              </div>
            </form>
    </div>
  </div>

  <div class="parallax-container valign-wrapper" id="banner">
    <div class="section no-pad-bot">
      <div class="container">
        <div class="row center">
          <div class="azul-p col s12 l12" ><h2><b>Cryptography - 3CV7</b></h2></div>
          <BR>
        </div>
        <div class="row center">
          <div class="azul-p col s12 l12" ><h5>ESCOM - IPN</h5></div>
        </div>
        <div class="row">
            <div class="col s6 l6 input-field">
                <form action="<%=response.encodeURL("Logout") %>" method="post">
                 <input type="submit" class="btn red" value="Logout" >
              </form>  
            </div>
        </div>
      </div>
    </div>
    <div class="parallax"><img src="img/w2.jpg" alt="Unsplashed background img 2"></div>
  </div>

  </body>


</html>