package com.example.a20201108.bean;

import com.example.a20201108.R;

import java.util.ArrayList;

public class GoodsInfo {
    public long rowid; // 行号
    public int sn; // 序号
    public String name; // 名称
    public String desc; // 描述
    public float price; // 价格
    public String thumb_path; // 小图的保存路径
    public String pic_path; // 大图的保存路径
    public int thumb; // 小图的资源编号
    public int pic; // 大图的资源编号

    public GoodsInfo() {
        rowid = 0L;
        sn = 0;
        name = "";
        desc = "";
        price = 0;
        thumb_path = "";
        pic_path = "";
        thumb = 0;
        pic = 0;
    }

    // 声明一个手机商品的名称数组
    private static String[] mNameArray = {
            "耐克1号", "耐克2号", "耐克3号",
            "耐克6号", "耐克5号", "耐克4号",
            "耐克7号","耐克8号"
    };
    // 声明一个手机商品的描述数组
    private static String[] mDescArray = {
            "1号 耐跑",
            "2号 透气",
            "3号 减震",
            "4号 包裹",
            "5号 耐磨",
            "6号 弹跳",
            "7号 运动",
            "8号 限量"
    };
    // 声明一个手机商品的价格数组
    private static float[] mPriceArray = {6299, 2199, 2499, 5299, 1899, 1998,1699,4999};
    // 声明一个手机商品的小图数组
    private static int[] mThumbArray = {
            R.drawable.one_s, R.drawable.two_s, R.drawable.three_s,
            R.drawable.four_s, R.drawable.five_s, R.drawable.six_s,
            R.drawable.seven_s,R.drawable.eight_s
    };
    // 声明一个手机商品的大图数组
    private static int[] mPicArray = {
            R.drawable.one, R.drawable.two, R.drawable.three,
            R.drawable.four, R.drawable.five, R.drawable.six,
            R.drawable.seven,R.drawable.eight
    };

    // 获取默认的手机信息列表
    public static ArrayList<GoodsInfo> getDefaultList() {
        ArrayList<GoodsInfo> goodsList = new ArrayList<GoodsInfo>();
        for (int i = 0; i < mNameArray.length; i++) {
            GoodsInfo info = new GoodsInfo();
            info.name = mNameArray[i];
            info.desc = mDescArray[i];
            info.price = mPriceArray[i];
            info.thumb = mThumbArray[i];
            info.pic = mPicArray[i];
            goodsList.add(info);
        }
        return goodsList;
    }
}
