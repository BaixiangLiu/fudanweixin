package edu.fudan.eservice.common.utils;

import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.logging.*;


public class ImageHelper {
	private static Log log = LogFactory.getLog(ImageHelper.class);

	/**
	 * 根据缩小比例生成缩略图
	 * 
	 * @param source
	 *            源文件 文件系统上的路径或者URL
	 * @param target
	 *            目标文件 文件系统上的路径
	 * @param scale
	 *            比例
	 */
	public static void makeThumbByScale(String source, String target,
			double scale) {
		BufferedImage bi = readImage(source);
		if (bi != null) {
			int newwidth = (int) (bi.getWidth() * scale);
			int newheight = (int) (bi.getHeight() * scale);
			BufferedImage to = doThumb(bi, newwidth, newheight);
			writeImage(target, to);
		}
	}

	/**
	 * 根据指定的图片宽度按比例生成缩略图
	 * 
	 * @param source
	 *            源文件 文件系统上的路径或者URL
	 * @param target
	 *            目标文件 文件系统上的路径
	 * @param width
	 *            缩略图的宽度(单位为像素)
	 */
	public static boolean makeThumbByWidth(String source, String target,
			int width) {
		BufferedImage bi = readImage(source);
		return makeThumbByWidth(bi,target,width);
	}
	public static boolean makeThumbByWidth(File source, String target,
			int width) {
		BufferedImage bi = readImage(source);
		return makeThumbByWidth(bi,target,width);
	}
	
	public static boolean makeThumbByWidth(BufferedImage bi,String target,int width)
	{
		
		if (bi != null) {
			double scale = (double) width / (double) bi.getWidth();
			if(scale>1){
			int newheight = (int) (bi.getHeight() * scale);
			BufferedImage to = doThumb(bi, width, newheight);
			writeImage(target, to);
			}else
			{
				writeImage(target,bi);
			}
			return true;
		} else
			return false;
	}

	/**
	 * 根据指定的图片高度按比例生成缩略图
	 * 
	 * @param source
	 *            源文件 文件系统上的路径或者URL
	 * @param target
	 *            目标文件 文件系统上的路径
	 * @param height
	 *            缩略图的高度(单位为像素)
	 */
	public static boolean makeThumbByHeight(String source, String target,
			int height) {
		BufferedImage bi = readImage(source);
		if (bi != null) {
			double scale = (double) height / (double) bi.getHeight();
			if(scale>1){
			int newwidth = (int) (bi.getWidth() * scale);
			BufferedImage to = doThumb(bi, newwidth, height);
			writeImage(target, to);}
			else
			{writeImage(target,bi);}
			return true;
		} else
			return false;
	}

	public static void writeImage(String target, BufferedImage to) {
		try {
			ImageIO.write(to, "jpg", new File(target));
		} catch (IOException e) {
			log.error("[writeImage]:" + e.getMessage());
		}
	}

	public static BufferedImage readImage(File source) 
	{
		try {
			return ImageIO.read(source);
		} catch (IOException e) {
			log.error(e);
			return null;
		}
		
	}
	
	public static BufferedImage readImage(String source) {
		BufferedImage bi = null;
		try {
			if (source.indexOf("://") > 0)
				
				bi = ImageIO.read(new URL(source));

			else
				bi = ImageIO.read(new File(source));

		} catch (MalformedURLException e) {
			log.error("[readImage]:" + e.getMessage());
		} catch (IOException e) {
			log.error("[readImage]:" + e.getMessage());

		}
		return bi;

	}

	public static BufferedImage doThumb(BufferedImage from, int width,
			int height) {
		BufferedImage to = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_BGR);
		to.getGraphics().drawImage(from, 0, 0, width, height, null);
		return to;
	}

}
