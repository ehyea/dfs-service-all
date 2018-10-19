package cn.eric.service.dfs.service.impl;

import cn.eric.service.dfs.util.CodeReturn;
import cn.eric.service.dfs.util.FDFSUtil;
import cn.eric.service.dfs.service.FdfsService;
import cn.eric.service.dfs.entity.NoteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.InputStream;

/**
 * 文件服务实现
 *
 * @author chenchao
 */
@Slf4j
@Service
public class FdfsServiceImpl implements FdfsService {

    @Override
    public NoteResult uploadFile(InputStream fileStream, String fileExtName) {
        NoteResult result = new NoteResult();
        if (fileStream == null) {
            result.setCode(CodeReturn.fail);
            result.setInfo("非法参数，文件流不能为空");
        } else {
            try {
                byte[] content = StreamUtils.copyToByteArray(fileStream);
                String remoteFileName = FDFSUtil.uploadFile(content, fileExtName == null ? "" : fileExtName);
                if (remoteFileName != null) {
                    result.setCode(CodeReturn.success);
                    result.setData(remoteFileName);
                } else {
                    result.setCode(CodeReturn.fail);
                }
            } catch (Throwable e) {
                log.error("上传文件流到DFS发生异常", e);
                result.setCode(CodeReturn.fail);
                result.setInfo("上传文件流到DFS发生异常");
            }
        }
        return result;
    }

    @Override
    public NoteResult downloadFile(String fileName) {
        NoteResult result = new NoteResult();
        if (fileName == null || fileName.trim().length() == 0) {
            result.setCode(CodeReturn.fail);
            result.setInfo("非法参数，文件名不能为空");
        } else {
            try {
                fileName = fileName.replaceFirst("http://.*?/", "");
                byte[] content = FDFSUtil.downloadFile(fileName);
                if (content != null) {
                    result.setCode(CodeReturn.success);
                    result.setData(content);
                } else {
                    result.setCode(CodeReturn.fail);
                }
            } catch (Throwable e) {
                log.error("下载DFS文件发生异常", e);
                result.setCode(CodeReturn.fail);
                result.setInfo("下载DFS文件发生异常");
            }
        }
        return result;
    }

    @Override
    public NoteResult deleteFile(String fileName) {
        NoteResult result = new NoteResult();
        if (fileName == null || fileName.trim().length() == 0) {
            result.setCode(CodeReturn.fail);
            result.setInfo("非法参数，文件名不能为空");
        } else {
            try {
                fileName = fileName.replaceFirst("http://.*?/", "");
                boolean isDelete = FDFSUtil.deleteFile(fileName);
                if (isDelete) {
                    result.setCode(CodeReturn.success);
                } else {
                    result.setCode(CodeReturn.fail);
                }
            } catch (Throwable e) {
                log.error("删除DFS文件发生异常", e);
                result.setCode(CodeReturn.fail);
                result.setInfo("删除DFS文件发生异常");
            }
        }
        return result;
    }
}
