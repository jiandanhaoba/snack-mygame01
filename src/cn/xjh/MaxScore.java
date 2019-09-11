package cn.xjh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



public class MaxScore {
    public int getMaxScore() throws IOException{
        File file=new File("maxScore.txt");
        if(!file.exists()){
            file.createNewFile();

        }

        //创建字符缓冲入流对象
        BufferedReader br=new BufferedReader(new FileReader(file));

        String s=br.readLine();

        int max=0;
        //逐行读取，并将数赋值给max
        if(!"".equals(s)&&s!=null){
            max=Integer.parseInt(s);
        }
        else{
            max=0;
        }
        //将最大值max写入文件
        if(SnackClient.score*100>max){
            max=SnackClient.score*100;

            BufferedWriter bw=new BufferedWriter(new FileWriter(file));
            bw.write(max+"");
            bw.close();
        }
        br.close();
        return max;
    }
}
