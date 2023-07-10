package com.example.netdisk.controller;


import com.example.netdisk.bean.FileIndex;
import com.example.netdisk.service.IFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    private IFileSystem fileSystem = null;

    @RequestMapping("/netdisk")
    public String index(String path,Model model) {
        FileIndex fileIndex = new FileIndex();

        path = (path==null || path.trim().isEmpty()) ? "/": path.trim();
        fileIndex.setPath(path);
        String fileName = fileSystem.getFileName(path);
        fileIndex.setName(fileName);
        model.addAttribute("rootDir",fileIndex);

        List<FileIndex> list = new ArrayList<FileIndex>();
        try {
            list = fileSystem.ls(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("rootFiles",list);

        return "jsp/index";
    }

    @GetMapping("/download")
    public String download(HttpServletResponse response, @RequestParam String file) throws Exception {
//        String filename="xxx.txt";

        File hFile = new File(file);
        String filename = hFile.getName();

        response.setHeader("Content-Disposition", "attachment;fileName=" + filename);
        fileSystem.download(file,response.getOutputStream());
        return null;
    }

    /**
     *
     * @param path  //一个文件或目录
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    public String delete(@RequestParam String path) throws Exception {
        String parentPath = fileSystem.rm(path);

        //跳转到父目录
        return "redirect:./?path=" + parentPath;
    }

    @ResponseBody
    @RequestMapping(value="/checkMD5",method=RequestMethod.POST)
    public String checkMD5(String md5code){
        //校验云盘中是否已存在MD5等于md5code的文件
        return "no";
    }

    @PostMapping("/uploadFile")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   String parentPath,RedirectAttributes redirectAttributes) throws IOException {

        String fileName = getOriginalFilename(file.getOriginalFilename());
        InputStream is = file.getInputStream();
        Long size = file.getSize();

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {
            String dstPath = parentPath.endsWith("/")? (parentPath+fileName) : (parentPath +"/"+fileName);
            fileSystem.upload(is,dstPath);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:./?path="+parentPath;
    }

    @PostMapping("/mkdir")
    public String mkdir(String directName,String parentPath) throws Exception {
        String newDir = parentPath.endsWith("/")?(parentPath+directName):(parentPath+"/"+directName);
        fileSystem.mkdir(newDir);
        return "redirect:./?path="+parentPath;
    }


    @RequestMapping("renameForm")
    public String renameForm(String directName,String isRoot,String path) throws Exception {
        //修改fileindex的name和path
        //修改子目录下的path
        String[] arr= fileSystem.rename(path,directName);
        //判断是修改本目录名称还是修改子目录名称，本目录名称修改完依然跳转到本目录页面，修改子目录的名称的话还是跳转到本目录
        if(isRoot!=null && isRoot.equals("yes")){
            return "redirect:./?path=" + arr[1];  //mypath
        }else{
            return "redirect:./?path=" + arr[0];//parent
        }
    }

    @RequestMapping("/getOptionalPath")
    @ResponseBody
    public List getOptionalPath(String path){
        //查询出当前fileIndexId可转移的目录集合，当前用户下该目录以及该目录的子目录不可选
        List result =  fileSystem.getOptionTranPath(path);
        return result;
    }

    @RequestMapping("/searchFiles")
    public String searchFiles(String keyWord,@RequestParam(value="pageNum",defaultValue="1")int pageNum,Model model) throws Exception {
        int pageSize = 3;
        List<FileIndex> result = fileSystem.searchFileByPage(keyWord,pageSize,pageNum);

        com.github.pagehelper.Page<FileIndex> page =new com.github.pagehelper.Page<FileIndex>();
        page.addAll(result);
        model.addAttribute("result", page);
        model.addAttribute("keyWord",keyWord);
        return "searchResult";
    }
    @RequestMapping("/stasticFiles")
    public String stasticFiles(Model model){
//        List staticResult = fileSystem.getStaticNums();


        List<Map> staticResult = new ArrayList<Map>();
        Map map = new HashMap();
        map.put("doc_number",100);
        map.put("video_number",87);
        map.put("pic_number",66);
        map.put("code_number",44);
        map.put("other_number",23);
        staticResult.add(map);

        model.addAttribute("staticResult", staticResult);

        return "stasticfiles";
    }

    private String getOriginalFilename(String originalFilename){
        if(originalFilename == null)
        {
            return "";
        }

        if(originalFilename.contains("/") || originalFilename.contains("\\")){
            File file = new File(originalFilename);
            return file.getName();
        }
        return  originalFilename;
    }
}
       