package com.wo.module.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wo.module.common.constant.Constants;

@SuppressWarnings("serial")
public class DisplayImage extends HttpServlet {  
  
    public void doGet(HttpServletRequest request,HttpServletResponse response)  
             throws IOException  
    {  
    
    String absolutePath = request.getParameter("filePath");
    absolutePath = Constants.decryptString(absolutePath);
    response.setContentType("image/jpeg");  
    ServletOutputStream out;  
    out = response.getOutputStream();  
    FileInputStream fin = new FileInputStream(absolutePath);  
      
    BufferedInputStream bin = new BufferedInputStream(fin);  
    BufferedOutputStream bout = new BufferedOutputStream(out);  
    int ch =0; ;  
    while((ch=bin.read())!=-1)  
    {  
    bout.write(ch);  
    }  
      
    bin.close();  
    fin.close();  
    bout.close();  
    out.close();  
    }  
}  
