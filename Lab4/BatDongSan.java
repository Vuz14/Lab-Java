package Lab4;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

class RealEstate {
    public String title;
    public String price;
    public String address;
    public String area;
    public String description;

    public RealEstate(String title, String price, String address, String area, String description) {
        this.title = title;
        this.price = price;
        this.address = address;
        this.area = area;
        this.description = description;
    }
}

class MenuItem {
    public String name;
    public String link;

    public MenuItem(String name, String link) {
        this.name = name;
        this.link = link;
    }
}

public class BatDongSan {
    private static final String BASE_URL = "https://alonhadat.com.vn";
    private static final String BASE_URL1 = "https://alonhadat.com.vn/can-ban-nha.htm";
    private static final List<RealEstate> properties = new ArrayList<>();
    private static final List<MenuItem> menuItems = new ArrayList<>();
    private static final Object LOCK = new Object();
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Crawl dữ liệu nhà đất
        executor.execute(() -> crawlRealEstate(BASE_URL1));

        // Crawl menu và danh mục
        executor.execute(() -> crawlMenu(BASE_URL));

        executor.shutdown();
        while (!executor.isTerminated()) {}

        saveToJson("real_estate_data.json", properties);
        saveToJson("menu_data.json", menuItems);

        System.out.println("Total properties crawled: " + counter.get());
        System.out.println("Total menu items crawled: " + menuItems.size());
    }

    private static void crawlRealEstate(String url) {

        try {
            Document doc = fetchDocument(url);
            if (doc == null) {
                System.out.println("Failed to fetch data from: " + url);
                return;
            }

            Elements listings = doc.select("div.content-item.content-item");
            if (listings.isEmpty()) {
                System.out.println("No real estate data found!");
                return;
            }

            for (Element item : listings) {
                String title = item.select("div.ct_title a").text();
                String price = item.select("div.ct_price").text();
                String address = item.select("div.ct_dis").text();
                String area = item.select("div.ct_dt").text().replace("Diện tích:", "").trim();
                String description = item.select("div.ct_brief").text();

                if (!title.isEmpty()) { // Đảm bảo không lưu dữ liệu rỗng
                    RealEstate realEstate = new RealEstate(title, price, address, area, description);
                    synchronized (LOCK) {
                        properties.add(realEstate);
                    }
                    counter.incrementAndGet();
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching data from: " + url);
            e.printStackTrace();
        }
    }
    private static Document fetchDocument(String url) {
        try {
            Connection.Response response = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36")
                    .timeout(10000)
                    .ignoreHttpErrors(true)
                    .execute();

            if (response.statusCode() == 200) {
                return response.parse();
            } else {
                System.out.println("Failed to fetch: " + url + " | Status: " + response.statusCode());
                return null;
            }
        } catch (IOException e) {
            System.err.println("Connection error: " + url);
            return null;
        }
    }

    // 📌 Crawl menu chính và danh mục nhà đất
    private static void crawlMenu(String url) {
        try {
            Document doc = Jsoup.connect(url).get();

            // Crawl menu chính
            Elements menu = doc.select("ul.menu > li > a");
            for (Element item : menu) {
                String name = item.text();
                String link = item.absUrl("href");
                menuItems.add(new MenuItem(name, link));
            }

            // Crawl danh mục Nhà Đất Bán
            Elements houseForSale = doc.select("li.list-sale ul > li > a");
            for (Element item : houseForSale) {
                String name = item.text();
                String link = item.absUrl("href");
                menuItems.add(new MenuItem("Nhà Đất Bán - " + name, link));
            }

            // Crawl danh mục Nhà Đất Cho Thuê
            Elements houseForRent = doc.select("li.list-rent ul > li > a");
            for (Element item : houseForRent) {
                String name = item.text();
                String link = item.absUrl("href");
                menuItems.add(new MenuItem("Nhà Đất Cho Thuê - " + name, link));
            }

            // Crawl danh mục Dự Án
            Elements projects = doc.select("li > a[href='/du-an-bat-dong-san'] + ul > li > a");
            for (Element item : projects) {
                String name = item.text();
                String link = item.absUrl("href");
                menuItems.add(new MenuItem("Dự Án - " + name, link));
            }

            String[] otherLinks = {"/nha-moi-gioi.html", "/tin-tuc-c4/kinh-nghiem.html", "/lien-he.html"};
            String[] otherNames = {"Nhà Môi Giới", "Kinh Nghiệm", "Liên Hệ - Góp Ý"};

            for (int i = 0; i < otherLinks.length; i++) {
                menuItems.add(new MenuItem(otherNames[i], BASE_URL + otherLinks[i]));
            }

        } catch (IOException e) {
            System.err.println("Error fetching menu from: " + url);
        }
    }

    // 📌 Lưu dữ liệu vào file JSON
    private static <T> void saveToJson(String filename, List<T> data) {
        ObjectMapper mapper = new ObjectMapper();
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            fos.write(jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
