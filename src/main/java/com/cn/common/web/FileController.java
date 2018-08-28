package com.cn.common.web;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cn.common.service.AttachmentService;
import com.cn.common.util.DateUtil;
import com.cn.common.util.JsonObjectUtil;
import com.cn.common.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 文件下载帮助接口
 * @author chen.kai
 * date:2016-12-16
 */

@RequestMapping(value="/file")
@Controller
public class FileController {
	
    Logger log=LoggerFactory.getLogger(FileController.class);
    
    @Resource
    private AttachmentService service;
    
    /**
     * 批量上传接口
     * @param request
     * @return
     * @throws IOException
     */
	@RequestMapping(value="/uploadfiles",method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject uploadFiles(MultipartHttpServletRequest request)throws IOException{
		String  systemid=request.getParameter("systemid");
		//取得request中的所有文件名  
		JSONObject json=JsonObjectUtil.initErrorJson();
		//如果附件超过5m,就拒绝发送
		int index=0;
		if(request.getContentLength()<5300000){
			Iterator<String> iter = request.getFileNames();
			if(iter.hasNext()){
				JSONArray array=new JSONArray();
				while(iter.hasNext()){        	
	                //取得上传文件  
	                MultipartFile file = request.getFile(iter.next());
	                //取得当前上传文件的文件名称  
	                String myFileName = file.getOriginalFilename();  
	                if(myFileName.length()>257){
	                	json.put("retMsg", "“"+myFileName+"”，附件名长度超过限制，不能超过256个字符！");
	                	index=-1;
	                	break;
	                }
	                //如果名称不为“”,说明该文件存在，否则说明该文件不存在  
	                String attid=StringUtil.getUuId();
	                //重命名上传后的文件名  
	                String fileName = attid.concat(myFileName.substring(myFileName.lastIndexOf("."), myFileName.length()));  
	                //获取当前服务器的绝对路径 
	                String filepath= request.getSession().getServletContext().getRealPath("/");
	                //创建自定义路径,/files/plms/20161219/
	                String propath="/files/".concat(systemid).concat("/").concat(DateUtil.formatDate(null, DateUtil.yyyyMMdd)).concat("/");
	                //构建文件路径
	                String path = filepath.concat(propath);                    
	                File localFile=new File(path);
	                //如果地址不存在就创建
	                if(!localFile.exists())localFile.mkdirs();
	                File newFile = new File(path.concat(fileName)); 
	                //上传当前文件到指定文件夹下
	                file.transferTo(newFile);  
	                //当前服务器的ip地址
	                //String ip=Inet4Address.getLocalHost().getHostAddress();
	                //将当前的文件资料插入到数据库
	                JSONObject object=new JSONObject();
	                String location="http://".concat(request.getLocalAddr()).concat(":").concat(String.valueOf(request.getLocalPort())).concat("/msnc").concat(propath).concat(fileName);
	                object.put("attid",attid);
	                object.put("attname", myFileName);
	                object.put("attnew", fileName);
	                object.put("systemid", systemid);
	                object.put("location", location);
	                service.insert(object);
	                object=null;
	                //如果插入成功就将messageid，文件名称，生成的url地址返回给业务系
                    object=new JSONObject();
                	object.put("messageid", attid);
                	object.put("filename", myFileName);
                	object.put("location", location);
                	array.add(object);
		          } 
				if(index!=-1){
					json.put("files", array); 
				    return JsonObjectUtil.initSucceedJson(json);	
				}
			}
			else json.put("retMsg", "附件为空或不存在！");
		}
		else json.put("retMsg", "附件超过5m，暂不支持上传!");
		return json;
	}
	
	/**
     * 附件单个上传接口
     * @param request
     * @return
     * @throws IOException
     */
	@RequestMapping(value="/uploadfile",method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject uploadFile(MultipartHttpServletRequest request)throws IOException{
		String  systemid=request.getParameter("systemid"); 
		//取得request中的所有文件名  
		JSONObject json=JsonObjectUtil.initErrorJson();
		//超过5m，则不提供发送
		if(request.getContentLength()<5300000){
			Map<String,MultipartFile> fileMap=request.getFileMap();
			//判断文件是否存在
			if(fileMap.size()>0){
				Iterator it=fileMap.entrySet().iterator();
				Map.Entry entry=(Map.Entry) it.next();
				MultipartFile file=(MultipartFile) entry.getValue();
			    //取得当前上传文件的文件名称  
	            String myFileName = file.getOriginalFilename();  
	            if(myFileName.length()>257){
                	json.put("retMsg", "“"+myFileName+"”，附件名长度超过限制，不能超过256个字符！");
                	return json; 
                }
	            //如果名称不为“”,说明该文件存在，否则说明该文件不存在  
                String attid=StringUtil.getUuId();
                //重命名上传后的文件名  
                String fileName = attid.concat(myFileName.substring(myFileName.lastIndexOf("."), myFileName.length()));  
                //获取当前服务器的绝对路径 
                String filepath= request.getSession().getServletContext().getRealPath("/");
                //创建自定义路径,/files/plms/20161219/
                String propath="/files/".concat(systemid).concat("/").concat(DateUtil.formatDate(null, DateUtil.yyyyMMdd)).concat("/");
                //构建文件路径
                String path = filepath.concat(propath);                    
                File localFile=new File(path);
                //如果地址不存在就创建
                if(!localFile.exists())localFile.mkdirs();
                File newFile = new File(path.concat(fileName)); 
                //上传当前文件到指定文件夹下
                file.transferTo(newFile);  
                //当前服务器的ip地址
                //String ip=Inet4Address.getLocalHost().getHostAddress();
                //将当前的文件资料插入到数据库
                Map<String,String> map=new HashMap<String,String>();
                String location="http://".concat(request.getLocalAddr()).concat(":").concat(String.valueOf(request.getLocalPort())).concat("/msnc").concat(propath).concat(fileName);
                map.put("attid",attid);
                map.put("attname", myFileName);
                map.put("attnew", fileName);
                map.put("systemid", systemid);
                map.put("location", location);
                //将数据插入到数据库
                service.insert(map);
                //如果插入成功就将messageid，文件名称，生成的url地址返回给业务系统
                json.put("messageid", attid);
                json.put("filename", myFileName);
                json.put("location", location);
                return JsonObjectUtil.initSucceedJson(json);
			}
			else
				json.put("retMsg", "附件为空或不存在！"); 
		}else json.put("retMsg", "附件超过5M,暂不提供上传.");
		return json;

	}
	
	/**
	 * 下载
	 * @param fileName
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value="download")
	 public ResponseEntity<byte[]> download(String fileName,File file) throws IOException{
		    //说明下载的文件不存在
		    if(!file.exists())return null;
		    HttpHeaders headers=new HttpHeaders();
	        //设置响应头的名字和响应头的值
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        headers.setContentDispositionFormData("attachment", fileName);        
	        HttpStatus statusCode=HttpStatus.OK;	        
	        ResponseEntity<byte[]> responseEntity=new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, statusCode);
	        return responseEntity;
	    }
}  
