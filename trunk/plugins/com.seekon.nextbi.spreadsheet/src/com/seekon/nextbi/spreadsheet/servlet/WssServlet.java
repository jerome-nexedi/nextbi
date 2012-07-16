package com.seekon.nextbi.spreadsheet.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WssServlet extends JsonResponseServlet {

	private static final long serialVersionUID = -72824693074427791L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		String methodName = req.getParameter("m");
		System.out.println("####methodName:" + methodName);
		
		if(methodName != null && methodName.trim().length() > 0){
			try{
				Method method = this.getClass().getDeclaredMethod(methodName, null);
				String result = (String)method.invoke(this, null);
				
				this.flushJsonResult(response, result);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	protected String getLoadedBooks() {
		JSONArray result = new JSONArray();
		try {
			JSONArray element = new JSONArray();
			element.put("8f20e3e5-9528-11e1-8ff-35e69c1cf085");
			element.put("New Spreadsheet");
			
			String data = "{\"group\": \"fgrp1\", \"hierarchy\": \"h1\", \"node\": \"n3\", \"relpath\": \"/New Folder/New Spreadsheet\", \"ghpath\": \"/Default/Public Files\", \"name\": \"New Spreadsheet\", \"perm\": \"7\", \"hidden\": false}";
			element.put(data.toString());
			
			result.put(element);
		} catch (Exception e) {

		}
		return result.toString();
	}
	
	protected String getCurrWbId(){
		return "\"8f20e3e5-9528-11e1-8ff-35e69c1cf085\"";
	}
	
	protected String selectBook(){
		JSONArray result = new JSONArray();
		result.put("true");
		result.put("false");
		result.put("8f20e3e5-9528-11e1-8ff-35e69c1cf085");
		return result.toString();
	}
		
	public String addSheet() throws JSONException{
		JSONObject result = new JSONObject();
		result.put("errcode", 0);
		result.put("wsid", "3ac30cc6-bd96-11e1-befe-f3e2f5de3dee");
		result.put("name", "sheet2");
		return result.toString();
	}
	
	public String selectSheet(){
		JSONArray result = new JSONArray();
		result.put(true);
		result.put(false);
		result.put("8f20e3e5-9528-11e1-8ff-35e69c1cf085");
		return result.toString();
	}
	
	public String load_workbook() throws JSONException{
		JSONArray result = new JSONArray();
		result.put(true);
		JSONObject data = new JSONObject();
		data.put("wbid", "8f20e3e5-9528-11e1-8ff-35e69c1cf085");
		data.put("name", "Workbook1");
		data.put("imp", false);
		result.put(data);
		return result.toString();
	}
}
