package com.imooc;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import java.util.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    public static void main(String[] args) {
//        File f = new File("D:\\IdeaProject");
//        File[] files = f.listFiles();
//        for(File file : files) {
//            System.out.println(file.getName() + "\t" + file.getAbsolutePath());
//        }
        List<Map> maps = genDirTree("D:\\IdeaProject\\java\\miaoshaProject", 1, "");
        Iterator<Map> iterator = maps.iterator();
        while(iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    public static List<Map> genDirTree(String path, int level, String dir) {
        level++;
        File file = new File(path);
        File[] files = file.listFiles();
        if (!file.exists()) {
            throw new RuntimeException("文件不存在");
        }
        List<Map> reList = new ArrayList<>();
        if (files.length != 0) {
            for (File f : files) {
                Map reMap = new HashMap();
                if (f.isDirectory()) {
                    dir = f.getName();
                    reMap.put("path",f.getAbsolutePath());
                    reMap.put("type","dir");
                    reMap.put("name",f.getName());
                    reMap.put("children",genDirTree(f.getAbsolutePath(), level, dir));
                    reList.add(reMap);
                } else {
                    reMap.put("path",f.getAbsolutePath());
                    reMap.put("type","file");
                    reMap.put("name",f.getName());
                    reList.add(reMap);
                }
            }
        }
        return reList;
    }
}
