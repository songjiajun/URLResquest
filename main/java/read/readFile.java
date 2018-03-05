package read;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 递归读取某个目录下的所有文件
 */
public class readFile {

    private static Map<String,List> test(String fileDir) {
        File file = new File(fileDir);
        File[] files = file.listFiles();// 获取目录下的所有文件或文件夹
        if (files == null) {
            return null;
        }
        List<String> AF1001List=new ArrayList<String>();
        List<String> AF1002List=new ArrayList<String>();
        for (File f1 : files) {
            if (f1.isFile()) {
               Map<String,List> map= getFileContent(f1);
               AF1001List.addAll(map.get("AF1001"));
               AF1002List.addAll(map.get("AF1002"));
                /*finaList.addAll(getJsonString(str));*/
            }else{
                continue;
            }
        }
        Map<String,List> map=new HashMap<String, List>();
        map.put("AF1001",AF1001List);
        map.put("AF1002",AF1002List);
        return map;
    }


    public static Map<String,List> getFileContent(File file){
        //读取文件的内容，转为字符串
        FileInputStream in = null;
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            List<String> listAF1001 = new ArrayList<String>();
            List<String> listAF1002 = new ArrayList<String>();
            while (reader.ready()) {
                String readerLine = reader.readLine();
                String regEx = ":\\{\"applicants\":.*";
                Pattern p = Pattern.compile(regEx);
                Matcher matcher = p.matcher(readerLine);
                while (matcher.find()) {
                    if (matcher.group().contains("AF1001")) {
                        listAF1001.add(matcher.group());
                    } else {
                        listAF1002.add(matcher.group());
                    }
                }
            }
            Map<String,List> map=new HashMap<String, List>();
            if(listAF1001!=null){
                map.put("AF1001",listAF1001);
            }
            if(listAF1002!=null){
                map.put("AF1002",listAF1002);
            }
            return map;

        } catch (IOException e) {
            e.printStackTrace();
        }catch(Exception e){
        e.printStackTrace();
        }
        return null;
    }




    //读取配置文件
    public static void main(String[] args)throws Exception {
        Properties properties = new Properties();
        properties.load(readFile.class.getClassLoader().getResourceAsStream("config.properties"));
        String templateFileName = (String) properties.get("connectionsFilesPath");
        Map<String,List> map=test(templateFileName);
        String url="http://127.0.0.1:8080/HxbDataFluxApi/HXBApiEntrance";
        List<String> listAF1001 =new ArrayList<String>();
        if(map.get("AF1001")!=null){
            listAF1001 = map.get("AF1001");
        }

        List<String> listAF1002 = new ArrayList<String>();
        if(map.get("AF1002")!=null){
            listAF1002 = map.get("AF1002");
        }
        if(listAF1001!=null) {
            for (String str : listAF1001) {
                try {
                    HttpUtil.doPost(url, str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if(listAF1002!=null) {
            for (String str1 : listAF1002) {
                try {
                    HttpUtil.doPost(url, str1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}