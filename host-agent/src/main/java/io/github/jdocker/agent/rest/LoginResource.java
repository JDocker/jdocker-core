//package io.github.jdocker.agent.rest;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.ws.rs.*;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;
//import java.io.IOException;
//import java.util.Map;
//
//@Path("/")
//public class LoginResource{
//
//    @Path("/login")
//    @GET
//    @Produces({MediaType.TEXT_HTML, MediaType.TEXT_PLAIN})
//    public String login(@Context HttpServletRequest req, @Context HttpServletResponse response){
//        Map<String,String> user = Authenticator.authenticate(req, response);
//        if(user.isEmpty()){
//            return getLoginPage();
//        }
//        return getSuccessPage(user);
//    }
//
//    @Path("login")
//    @POST
//    @Produces({MediaType.TEXT_HTML, MediaType.TEXT_PLAIN})
//    public String login(@FormParam("uid") String uid, @FormParam("pwd") String pwd, @Context HttpServletResponse response){
//        if(uid==null){
//            return getLoginPage();
//        }
//        if(pwd==null){
//            return getLoginPage().replace("AUTHENTICATE", "AUTHENTICATE WITH A PASSWORD...");
//        }
//        Map<String,String> user = Authenticator.authenticate(uid, pwd, response);
//        if(user.isEmpty()){
//            return getLoginPage().replace("PLEASE AUTHENTICATE", "AUTHENTICATION FAILED; TRY AGAIN...");
//        }
//        return getSuccessPage(user);
//    }
//
//    @Path("/user")
//    @GET
//    @Produces({MediaType.TEXT_HTML, MediaType.TEXT_PLAIN})
//    public String user(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException {
//        Map<String,String> user = Authenticator.authenticate(request);
//        if(user.isEmpty()){
//            response.sendRedirect("/login");
//            return null;
//        }
//        else{
//            return getSuccessPage(user);
//        }
//    }
//
//    private String getLoginPage(){
//        return "<!DOCTYPE html>"+
//                "<html>"+
//                "<head>"+
//                "<meta charset=\"UTF-8\">"+
//                "<title>Login Page</title>"+
//                "</head>"+
//                "<body>"+
//                "<h3>PLEASE AUTHENTICATE</h3>"+
//                "<form action=\"login\" method=\"post\">"+
//                "        Username: <input type=\"text\" name=\"uid\">"+
//                "<br>"+
//                "        Password: <input type=\"password\" name=\"pwd\">"+
//                "<br>"+
//                "<input type=\"submit\" value=\"Login\">"+
//                "</form>"+
//                "</body>"+
//                "</html>";
//    }
//
//    private String getSuccessPage(Map<String,String> user){
//        return "<!DOCTYPE html>"+
//                "<html>"+
//                "<head>"+
//                "<meta charset=\"UTF-8\">"+
//                "<title>Login Successful</title>"+
//                "</head>"+
//                "<body>"+
//                "<h3>Login Successful</h3>"+
//                "<p>User Token: " + user.get("token") + "</p>"+
//                "<p>User Data : " + user + "</p>"+
//                "</body>"+
//                "</html>";
//    }
//
//}
