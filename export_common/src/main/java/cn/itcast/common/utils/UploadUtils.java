package cn.itcast.common.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class UploadUtils {

    private static String bucket = "heima125";
    private static String accessKey = "COuoDRVa7JLsuurzIvQSI_pEDceHDw3yGfJEmvwv";
    private static String secretKey = "3RWpTjB5Jxg3QosUFr4mxbHXJ5JR2m6AHQqYsSlr";
    private static String url = "http://q7j6ulp3a.bkt.clouddn.com/";


    /**
     * 工具类：传递图片的byte数组，自动的上传到七牛云上
     * 返回值：上传到七牛云之后，访问图片（拼接一个图片的访问URL）
     */
    public  String upload(byte[] uploadBytes) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        String address = "";
        try {
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(uploadBytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                address = url + putRet.key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return address;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("G:\\java\\ssm\\ssm项目\\Saas\\day10\\day10-资料\\1-货物操作流程图.png");
        InputStream input = new FileInputStream(file);
        byte[] byt = new byte[input.available()];
        input.read(byt);
        String upload = new UploadUtils().upload(byt);
        System.out.println(upload);
    }
}