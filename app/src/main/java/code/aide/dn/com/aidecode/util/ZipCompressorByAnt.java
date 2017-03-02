package code.aide.dn.com.aidecode.util;

/**
 * Created by 大牛哥 on 2017/2/15.
 * QQ:201309512
 * EMAIL:201309512@QQ.COM
 */

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;

import java.io.File;

/**
 * @ClassName: ZipCompressorByAnt
 * @CreateTime Apr 28, 2013 1:23:45 PM
 * @author : Mayi
 * @Description: 压缩文件的通用工具类-采用ant中的org.apache.tools.ant.taskdefs.Zip来实现，更加简单。
 *
 */
public class ZipCompressorByAnt {

    private File zipFile;
    /**
     * 压缩文件
     *
     * @param zipFile
     * @param dir
     */
    public static void zip(String zipFile, String dir) {
        Zip zip = new Zip();
        zip.setBasedir(new File(dir));
        // zip.setIncludes(...); 包括哪些文件或文件夹eg:zip.setIncludes("*.java");
        // zip.setExcludes(...); 排除哪些文件或文件夹
        zip.setDestFile(new File(zipFile));
        Project p = new Project();
        // p.setBaseDir(new File(src));
        zip.setProject(p);
        zip.execute();
    }

    /**
     * 解压文件
     *
     * @param dir
     * @param zipFile
     */
    public static void unzip(String dir, String zipFile) {
        Expand expand = new Expand();
        // PatternSet set = new PatternSet();
        // set.setIncludes("*.doc"); 包括哪些文件或文件夹
        // set.setExcludes("*.xls"); 排除哪些文件或文件夹
        // expand.addPatternset(set);
        expand.setDest(new File(dir));
        expand.setSrc(new File(zipFile));
        Project p = new Project();
        expand.setProject(p);
        expand.execute();
    }
}