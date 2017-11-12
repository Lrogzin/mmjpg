package com.lrogzin.mmjpg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by 1U02UN on 2017/11/10.
 */

public class NetData {

    /**
     * 获取首页所有分类保存到LinkedHashMap中
     * @return categoryMap
     * @throws IOException
     */
    public Map<String,String> getCategory() throws IOException{
        Map<String,String> categoryMap = new LinkedHashMap<>();
        Document doc = Jsoup.connect("http://www.mmjpg.com/").get();
        Elements elementClass = doc.select("div.subnav > a");
        categoryMap.put("所有","http://www.mmjpg.com/home");

        for (Element e : elementClass) {
            categoryMap.put(e.text(), e.attr("href"));
        }
        return categoryMap;
    }

    /**
     * 根据类别地址category_url和所在页码page爬取套图封面图片的名称，封面图片地址和套图链接地址
     * @param category_url
     * @param page
     * @return albumList
     * @throws IOException
     */
    public List<PicBean> getIndexAlbum(String category_url,int page) throws IOException{
        List<PicBean> albumList = new ArrayList<PicBean>();
        String target_url="";
        if(category_url=="http://www.mmjpg.com/home" && page==1){
            target_url="http://www.mmjpg.com/";
        }else{
            target_url=category_url+"/"+page;
        }
        Document doc = Jsoup.connect(target_url).get();
        Elements elementClass = doc.select("div.pic > ul > li > a");
        for (Element e : elementClass) {
            PicBean picBean = new PicBean();
            picBean.setName(e.select("img").attr("alt"));
            picBean.setLink(e.attr("href"));
            picBean.setPic(e.select("img").attr("src"));
            albumList.add(picBean);
            //System.out.println(picBean.getName()+"\n"+picBean.getPic()+"\n"+picBean.getLink()+"\n");
        }
        return albumList;
    }

    /**
     * 根据套图url获取套图数量，从而构造出所有图片的url
     * @param album_link
     * @return picList
     * @throws IOException
     */
    public List<String> getAlbumCount(String album_link) throws IOException{
        List<String> picList = new ArrayList<>();
        Document doc = Jsoup.connect(album_link).get();
        Elements element_page = doc.select("div.page > a");
        Elements element_pic = doc.select("div.content > a");
        String first_url=element_pic.get(0).select("img").attr("src");
        String base_url=first_url.substring(0, first_url.lastIndexOf("/"));
        int count = Integer.parseInt(element_page.get(element_page.size()-2).text());
        for(int i=1;i<=count;i++){
            picList.add(base_url+"/"+i+".jpg");
        }
        return picList;
    }
}
