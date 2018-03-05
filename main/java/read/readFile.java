package read;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 递归读取某个目录下的所有文件
 */
public class readFile {

    private static List test(String fileDir) {
        File file = new File(fileDir);
        File[] files = file.listFiles();// 获取目录下的所有文件或文件夹
        if (files == null) {
            return null;
        }

        List<String> finaList=new ArrayList<String>();
        for (File f1 : files) {
            if (f1.isFile()) {
                String str = getFileContent(f1);
                finaList.addAll(getJsonString(str));
            }else{
                continue;
            }

        }
        return finaList;
    }

    //读取文件内容
    public static String getFileContent(File file) {
        FileInputStream in = null;
        StringBuffer sb = new StringBuffer();
        try {
            in = new FileInputStream(file);
            byte[] b = new byte[(int)file.length()];
            int read = 0;
            while ((read = in.read(b)) != -1) {
                sb.append(new String(b));
            }
            String str = sb.toString();
            return str;
            // return new String(str.getBytes("GBK"), "UTF-8");;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //获取getJson后面的内容
    public static List<String> getJsonString(String string){
        List<String> list=new ArrayList<String>();
        String [] strs=null;
        if(string.contains("getInputJson:")){
            strs =string.split("getInputJson:");
        }else{
            return null;
        }
        for(String str:strs){
            if(str.contains("{\"applicants\":")){
                int index=str.indexOf("{\"applicants\":");
                String sb=str.substring(index,str.length());
                int indexx=sb.indexOf("审批过程中系统自动调用\"}}");
                sb=sb.substring(0,indexx+14);
                list.add(sb);
                System.out.println(sb);
            }
        }
        return list;
    }


    //读取配置文件
    public static void main(String[] args)throws Exception {
        Properties properties = new Properties();
        properties.load(readFile.class.getClassLoader().getResourceAsStream("config.properties"));
        String templateFileName = (String) properties.get("connectionsFilesPath");
        List<String> list=test(templateFileName);
        String url="http://127.0.0.1:8080/HxbDataFluxApi/HXBApiEntrance";
        for(String str:list){
            try{
                HttpUtil.doPost(url,str);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
