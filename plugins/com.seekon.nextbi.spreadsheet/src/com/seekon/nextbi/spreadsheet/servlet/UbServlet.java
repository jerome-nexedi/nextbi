package com.seekon.nextbi.spreadsheet.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;

public class UbServlet extends JsonResponseServlet{

	private static final long serialVersionUID = 8427700133133008903L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
//		Enumeration enum0 = req.getParameterNames();
//		if(enum0.hasMoreElements()){
//			String name = enum0.nextElement().toString();
//			System.out.println(name + ":" + req.getParameter(name));
//		}
		
		String body = "";
		ServletInputStream sis = req.getInputStream();
		if(sis != null){
			byte[] temp = new byte[1024];
			int length = 0;
			while((length = sis.read(temp)) > 0){
				if(length < temp.length){
					body += new String(temp, 0, length);
				}else{
					body += new String(temp);
				}
			}
		}
		
		if(body != null && body.trim().length() > 0){
//			try {
//				JSONArray jsonBody = new JSONArray(body);
//				
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
			
			String result = null;
			if(body.startsWith("[[\"olst")){
				result = "[[true,{\"b74c9c04-9714-11e1-8ff-35e69c1cf085\":\"Sheet1\"}],[true,{}],[true,\"b74c9c04-9714-11e1-8ff-35e69c1cf085\"],[true,[]]]";
			}else if(body.startsWith("[[\"gdcr")){
				result = "[[true,[64,20]],[true,[]],[true,[]],[true,[1,1]],[true,[]]]";
			}else if(body.startsWith("[[\"wget")){
				result = "[[true,[]],[true,[]],[true,[]],[true,[]]]";
			}else if(body.startsWith("[[\"grar")){
				result = "[[true]]";
			}else if(body.startsWith("[[\"lock")){
				result = "[[true,\"\"]]";
			}else if(body.startsWith("[[\"ctrn")){
				result = "[[true,[1]]]";
			}
			System.out.println("####body:" + body);
			
			flushJsonResult(resp, result);
		}
	}
	
	
}
