package com.joe.servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.joe.image.ImageCut;

public class ImgUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String IMGROOT = "/uploads/";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		String userWebAppPath = getWebAppPath();
		System.out.println("In ImgUploadServlet.doPost(), userWebAppPath:"+userWebAppPath);
		
		/**检查是否有图片上传文件夹,没有就建立一个*/
		checkImageDir(userWebAppPath);	
		
		/**图片上传的相对路径*/
		String imgUploadPath = null;
		String imgWebAppPath = null;
		
		/**图片后缀*/
		String imgFileExt = null;
		
		/**图片名称:以当前日期*/
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
		String imgFileId = formatter.format(new Date());
		
		//图片初始化高度与宽度
		String width = null;
		String height = null;
		
		int imgWidth = 0;
		int imgHeight = 0;

		try {
			
			com.jspsmart.upload.SmartUpload smartUpload = new com.jspsmart.upload.SmartUpload();
			smartUpload.initialize(getServletConfig(), request, response);
			smartUpload.upload();
			com.jspsmart.upload.Request rqest = smartUpload.getRequest();
			
			//指定图片宽度和高度
			width = rqest.getParameter("width");
			if(null == width){
				width = "700";
			}
			height= rqest.getParameter("height");	
			if(null == height){
				height = "600";
			}
			
			imgWidth = Integer.parseInt(width);
			imgHeight = Integer.parseInt(height);
			
			
			//文件个数
			int fileCounts =  smartUpload.getFiles().getCount();	
		
			for (int i = 0; i <fileCounts; i++) {
				com.jspsmart.upload.File myFile = smartUpload.getFiles().getFile(i);
				
				if (!myFile.isMissing()) {
					
					imgFileExt = myFile.getFileExt();
					//组装图片真实名称
					imgFileId += i + System.currentTimeMillis() + "." + imgFileExt;
					
					//图片生成路径
					imgWebAppPath = userWebAppPath + imgFileId;
					
					//生成图片文件
					myFile.saveAs(imgWebAppPath);
					//图片的相对路径
					imgUploadPath = IMGROOT + imgFileId;
					
					//检查图片大小
					BufferedImage src = ImageIO.read(new File(imgWebAppPath)); // 读入文件						 
					int imgSrcWidth = src.getWidth(); // 得到源图宽							 
					//重新指定大小
					imgWidth = imgSrcWidth > imgWidth ? imgWidth : imgSrcWidth;
					
					int imgSrcHeight = src.getHeight(); // 得到源图长
					imgHeight = imgSrcHeight > imgHeight ? imgHeight : imgSrcHeight;
				
					//按照图片的设置大小生成
					ImageCut.scale(imgWebAppPath, imgWebAppPath,imgWidth,imgHeight);
					 File f = new File(imgWebAppPath);								
					 if(f.exists()){						
						 System.out.println("创建"+imgWidth+"*"+imgHeight+"图片成功");
					 }					
				}
				
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		String path = "/imgcrop.jsp?tag=0&oldImgPath="+imgUploadPath+"&imgFileExt="+imgFileExt+"&imgRoot="+IMGROOT+"&width="+imgWidth+"&height="+imgHeight;
		System.out.println("path: "+path);
		request.getRequestDispatcher(path).forward(request,response);
		
	}
	
	private String getWebAppPath(){
		String webAppPath = this.getServletContext().getRealPath("/");		
		String userWebAppPath = webAppPath+IMGROOT;
		return userWebAppPath;
	}

	private void checkImageDir(String userWebAppPath) {		
		 File file = new File(userWebAppPath);
		 if(!file.exists()){
			 file.mkdir();
		 }
	}

}