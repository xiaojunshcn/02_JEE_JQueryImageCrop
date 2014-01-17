package com.joe.servlet;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.joe.image.ImageCut;

public class ImgCropServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("x: " + request.getParameter("x") + "," + request.getParameter("y") + "," + request.getParameter("w") + "," + request.getParameter("h"));

		// 用户经过剪辑后的图片的大小
		Integer x = Integer.parseInt(request.getParameter("x"));
		Integer y = Integer.parseInt(request.getParameter("y"));
		Integer w = Integer.parseInt(request.getParameter("w"));
		Integer h = Integer.parseInt(request.getParameter("h"));
		
		//获取原显示图片路径
		String oldImgPath = request.getParameter("oldImgPath");
		//图片后缀
		String imgFileExt = request.getParameter("imgFileExt");
		String imgRoot =  request.getParameter("imgRoot");
		
		Integer width = Integer.parseInt(request.getParameter("width"));
		Integer height = Integer.parseInt(request.getParameter("height"));
		
		//WEB应用程序根路径
		String webAppPath = getServletContext().getRealPath("/");
		
		/**图片名称:以当前日期*/
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
		String imgFileId = formatter.format(new Date());
		String imgName =  imgRoot + imgFileId + System.currentTimeMillis() + "." + imgFileExt;			
		//组装图片真实名称
		String createImgPath = webAppPath + imgName;
		
		//之前上传的图片路径
		webAppPath += oldImgPath;
		
		System.out.println("原图片路径: " + webAppPath + ",新图片路径: " + createImgPath);
		
		//进行剪切图片操作
		ImageCut.abscut(webAppPath, createImgPath, x,y,w, h);
		
		 File f = new File(createImgPath);								
		 if(f.exists()){						
			 System.out.println("剪切图片大小: "+w+"*"+h+"图片成功!");
		 }	
		 
		String path = "/imgcrop.jsp?tag=1&oldImgPath="+oldImgPath+"&imgFileExt="+imgFileExt+"&imgRoot="+imgRoot + "&imgName="+imgName+"&height=" + height + "&width=" + width;
		System.out.println("imgCrop: " + path);
		request.getRequestDispatcher(path).forward(request,response);
	}

}
