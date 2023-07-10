package com.example.netdisk.service;



import com.example.netdisk.bean.FileIndex;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface IFileSystem {
    /**
     * ls 列出子目录和文件
     * @return
     */
    List<FileIndex> ls(String dir) throws Exception;

    /**
     * 创建目录
     * @return
     */
    void mkdir(String dir)throws Exception;

    /**
     * 删除文件或目录
     * 返回父亲目录
     */
    String rm(String path) throws Exception;

    /**
     * 上传
     */
    void upload(InputStream is, String dstHDFSFile) throws Exception;

    /**
     * 下载
     */
    void download(String file, OutputStream os) throws Exception;

    /**
     * 移动到
     */
    void mv();

    /**
     * 重命名
     */
    String[] rename(String path,String dirName) throws Exception;

    /**
     * 获取文件命
     * @param path
     * @return
     */
    String getFileName(String path);

    //查询出当前fileIndexId可转移的目录集合，当前用户下该目录以及该目录的子目录不可选
    List getOptionTranPath(String path);

    List<FileIndex> searchFileByPage(String keyWord, int pageSize, int pageNum) throws Exception;

    List getStaticNums();
}
