package com.example.netdisk.service.impl;

import com.example.netdisk.bean.FileIndex;
import com.example.netdisk.service.IFileSystem;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class FileSystemImpl implements IFileSystem {
    Logger logger = LoggerFactory.getLogger(FileSystemImpl.class);

    @Value("${hadoop.namenode.rpc.url}")
    private String namenodeRpcUrl;

//    private static String NAMENODE_RPC="hdfs://192.168.72.128:8020";

    // 获取文件系统
    public FileSystem getFS() throws URISyntaxException, IOException, InterruptedException {
        Configuration conf=new Configuration();
        //配置NameNode地址
        URI uri=new URI(namenodeRpcUrl);
        //指定用户名,获取FileSystem对象
        FileSystem fs=FileSystem.get(uri,conf,"sh");

        return fs;
    }


    @Override
    public List<FileIndex> ls(String dir) throws Exception{
        Configuration conf=new Configuration();
        //配置NameNode地址
        URI uri=new URI(namenodeRpcUrl);
        //指定用户名,获取FileSystem对象
        FileSystem fs=FileSystem.get(uri,conf,"shi");

        Path path = new Path(dir);
        //如果不存在，返回
        if(! fs.exists(path)){
            logger.error("dir:"+dir+" not exists!");
            throw new RuntimeException("dir:"+dir+" not exists!");
        }

        List<FileIndex> list = new ArrayList<FileIndex>();
        FileStatus[] filesStatus = fs.listStatus(path);
        for(FileStatus f:filesStatus){
            FileIndex fileIndex = new FileIndex();
            fileIndex.setIsFile(f.isDirectory()?"0":"1");
            fileIndex.setName(f.getPath().getName());
            fileIndex.setPath(f.getPath().toUri().getPath());
            fileIndex.setCreateTime(new Date());

            list.add(fileIndex);
        }

        //不需要再操作FileSystem了，关闭
        fs.close();

        return list;
    }

    @Override
    public void mkdir(String dir) throws Exception{
        Configuration conf=new Configuration();
        //配置NameNode地址
        URI uri=new URI(namenodeRpcUrl);
        //指定用户名,获取FileSystem对象
        FileSystem fs=FileSystem.get(uri,conf,"shi");

        fs.mkdirs(new Path(dir));

        //不需要再操作FileSystem了，关闭client
        fs.close();

        System.out.println( "mkdir "+dir+" Successfully!" );

    }

    @Override
    /**
     * 删除文件或目录
     */
    public String rm(String path) throws Exception {
        Configuration conf=new Configuration();
        //配置NameNode地址
        URI uri=new URI(namenodeRpcUrl);
        //指定用户名,获取FileSystem对象
        FileSystem fs=FileSystem.get(uri,conf,"shi");

        Path filePath = new Path(path);
        fs.delete(filePath,true);

        //不需要再操作FileSystem了，关闭client
        fs.close();

        System.out.println( "Delete "+path+" Successfully!" );

        return filePath.getParent().toUri().getPath();
    }

    @Override
    public void upload(InputStream is, String dstHDFSFile) throws Exception {
        //TODO
		//请实现

        System.out.println( "Upload Successfully!" );
    }

    @Override
    public void download(String file, OutputStream os) throws Exception {
            //TODO
		//请实现

        System.out.println( "Download Successfully!" );
    }

    @Override
    public void mv() {

    }

    @Override
    public String[] rename(String path, String dirName) throws Exception {
        Configuration conf=new Configuration();
        //配置NameNode地址
        URI uri=new URI(namenodeRpcUrl);
        //指定用户名,获取FileSystem对象
        FileSystem fs=FileSystem.get(uri,conf,"shi");

        Path oldPath = new Path(path);
        if(oldPath.getParent() ==null){
            String[] arr = new String[2];
            arr[0]="/";
            arr[1]="/";
            return arr;
        }
        String parentPath = oldPath.getParent().toUri().getPath();
        String newPathStr = parentPath.endsWith("/")?(parentPath+dirName):(parentPath + "/" +dirName);
        Path newPath = new Path(newPathStr );
        fs.rename(oldPath,newPath);

        //不需要再操作FileSystem了，关闭client
        fs.close();

        String[] arr = new String[2];
        arr[0]=parentPath;
        arr[1]=newPathStr;

        System.out.println( "rename Successfully!" );

        return arr;
    }



    @Override
    public String getFileName(String path) {
        Path filePath = new Path(path);
        return filePath.getName();
    }

    @Override
    public List getOptionTranPath(String path) {
        return new ArrayList();
    }

    @Override
    public List<FileIndex> searchFileByPage(String keyWord, int pageSize, int pageNum) throws Exception{
        Configuration conf=new Configuration();
        //配置NameNode地址
        URI uri=new URI(namenodeRpcUrl);
        //指定用户名,获取FileSystem对象
        FileSystem fs=FileSystem.get(uri,conf,"shi");
        ArrayList<FileStatus> results = null;
        PathFilter filter = new PathFilter() {
            @Override
            public boolean accept(Path path) {
                if(keyWord == null || keyWord.trim().isEmpty()){
                    return false;
                }
                if(path.getName().contains(keyWord)){
                    return  true;
                }
                return false;
            }
        };

        List<FileIndex> list = new ArrayList<FileIndex>();
        FileStatus[] fileStatusArr = fs.listStatus(new Path("/"),filter);
        for(FileStatus status :fileStatusArr){
            FileIndex fileIndex = new FileIndex();
            fileIndex.setPath(status.getPath().toUri().getPath());
            fileIndex.setName(status.getPath().getName());
            fileIndex.setIsFile(status.isFile()?"1":"0");
            list.add(fileIndex);
        }

        //不需要再操作FileSystem了，关闭client
        fs.close();

        System.out.println( "Search Successfully!" );

        return list;
    }

    @Override
    public List getStaticNums() {
        return null;
    }
}
