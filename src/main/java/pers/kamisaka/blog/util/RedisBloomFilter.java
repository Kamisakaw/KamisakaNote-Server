package pers.kamisaka.blog.util;

import com.google.common.hash.Hashing;

public class BloomFilter {
    //大约容纳多少元素
    private int numApproxElement;
    //误判率
    private double fpp;
    //hash函数个数
    private int numHashFunctions;
    //位图的位数
    private int bitmapLength;

    public BloomFilter(int numApproxElement, double fpp) {
        this.numApproxElement = numApproxElement;
        this.fpp = fpp;
        this.bitmapLength = (int)(-numApproxElement * Math.log(fpp) / (Math.log(2) * Math.log(2)));
        this.numHashFunctions = Math.max(1,(int)(Math.round((double) bitmapLength / numApproxElement * Math.log(2))));
    }

    public int getNumApproxElement() {
        return numApproxElement;
    }

    public double getFpp() {
        return fpp;
    }

    public int getNumHashFunctions() {
        return numHashFunctions;
    }

    public int getBitmapLength() {
        return bitmapLength;
    }

    //计算请求的参数Hash到哪些bit上
    private long[] getBitIndex(String element){
        long[] index = new long[numHashFunctions];
        byte[] bytes = Hashing.murmur3_128().Hash
    }
}
