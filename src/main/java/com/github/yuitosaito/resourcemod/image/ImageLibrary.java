package com.github.yuitosaito.resourcemod.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentHashMap;

public class ImageLibrary {
    public static ConcurrentHashMap<String, ImageDataBase> bufferedImageMap = new ConcurrentHashMap<>();

    public static class ImageDataBase {
        public String url;

        private ImageDataBase(String url) {
            this.url = url;
        }
    }

    public static class ImageDataSingle extends ImageDataBase {
        public BufferedImage image;

        public ImageDataSingle(String url, BufferedImage image) {
            super(url);
            this.image = image;
        }
    }

    public static class ImageDataMulti extends ImageDataBase {
        public BufferedImage[] images;
        public int[] delays;

        public ImageDataMulti(String url, BufferedImage[] images, int[] delays) {
            super(url);
            assert (images.length == delays.length);
            this.images = images;
            this.delays = delays;
        }
    }

    private static final ConcurrentHashMap<String, GetOriginalImage> getOriginalImageThreads = new ConcurrentHashMap<>();

    public static GetOriginalImage getOrStartOriginalImageGetter(String url) throws MalformedURLException {
        GetOriginalImage before = getOriginalImageThreads.putIfAbsent(url, new GetOriginalImage(url));
        GetOriginalImage ret = getOriginalImageThreads.get(url);
        if (before == null) {
            ret.start();
        }
        return ret;
    }

    public static class GetOriginalImage extends Thread {
        URL url;
        String urlStr;

        private GetOriginalImage(String url) throws MalformedURLException {
            this.urlStr = url;
            this.url = new URL(urlStr);
        }

        @Override
        public void run() {
            try {
                BufferedImage image;
                BufferedImage[] images;
                int[] delays;

                URLConnection connection = url.openConnection();
                String mimeType = connection.getContentType();
                System.out.println(mimeType);
                    /*if ("image/svg+xml".equals(mimeType)) {
                        image = ImageTools.rasterize(connection.getInputStream());
                    } else */

                if ("image/gif".equals(mimeType)) {
                    GifDecoder g = new GifDecoder();
                    g.read(connection.getInputStream());
                    System.out.println(g.getFrameCount());
                    if (g.getFrameCount() == 1) {
                        image = ImageIO.read(connection.getInputStream());

                        ImageLibrary.bufferedImageMap.putIfAbsent(urlStr, new ImageLibrary.ImageDataSingle(urlStr, image));
                    } else {
                        images = new BufferedImage[g.getFrameCount()];
                        delays = new int[g.getFrameCount()];
                        for (int i = 0; i < images.length; ++i) {
                            BufferedImage ib = g.getFrame(i);
                            delays[i] = g.getDelay(i);
                            images[i] = ib;
                        }

                        ImageLibrary.bufferedImageMap.putIfAbsent(urlStr, new ImageLibrary.ImageDataMulti(urlStr, images, delays));
                    }
                } else {
                    image = ImageIO.read(connection.getInputStream());
                    ImageLibrary.bufferedImageMap.putIfAbsent(urlStr, new ImageLibrary.ImageDataSingle(urlStr, image));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
