package com.dj.zk.manager.action;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dj.zk.manager.commons.Constants;

/**
 * 
 * @description: 验证码生成
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:18:39
 */
@Controller
@RequestMapping(value = Constants.BASE_PATH + "verify")
public class AuthCodeAction {
     private static Logger logger = Logger.getLogger(AuthCodeAction.class);
	 private static char  []  words = new char[52];
     static {
    	 int count =0;
    	 for(int i =97;i<=122;i++) {
    		 words[count++]=(char)i;
 		 }
    	 for(int i =65;i<=90;i++) {
    		 words[count++]=(char)i;
 		 }
     }
	@RequestMapping(value = {"code"}, method = {RequestMethod.GET})
	public ModelAndView config(HttpServletRequest request,OutputStream os,
			HttpServletResponse response, ModelMap modelMap) {
		try {
			String verCode = createRandomStr();
			request.getSession().setAttribute(Constants.VERIFY_CODE, verCode);
 			BufferedImage image = createVerifyImage(verCode, 100,50);
			ImageIO.write(image, "JPEG",os);
 		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		} 
		return null;
	}

	
	public static String createRandomStr() {
		StringBuffer buf = new StringBuffer();
		buf.append(RandomStringUtils.random(1, words));
		for(int i = 0; i < 3; i++) {
			buf.append(RandomUtils.nextInt(10));
		}
        buf.append(RandomStringUtils.random(1, words));
	
		return buf.toString();
	}
	
	public static BufferedImage createVerifyImage(String word,int width,int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // 创建BufferedImage类的对象
		Graphics g = image.getGraphics(); // 创建Graphics类的对象
		Graphics2D g2d = (Graphics2D) g; // 通过Graphics类的对象创建一个Graphics2D类的对象
		Random random = new Random(); // 实例化一个Random对象
		Font mFont = new Font("华文宋体", Font.BOLD, 25); // 通过Font构造字体
		g.setColor(getRandColor(200, 250)); // 改变图形的当前颜色为随机生成的颜色
		g.fillRect(0, 0, width, height); // 绘制一个填色矩形

		// 画一条折线
		BasicStroke bs = new BasicStroke(2f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL); // 创建一个供画笔选择线条粗细的对象
		g2d.setStroke(bs); // 改变线条的粗细
		g.setColor(Color.DARK_GRAY); // 设置当前颜色为预定义颜色中的深灰色
		int[] xPoints = new int[3];
		int[] yPoints = new int[3];
		for (int j = 0; j < 3; j++) {
			xPoints[j] = random.nextInt(width - 1);
			yPoints[j] = random.nextInt(height - 1);
		}
		g.drawPolyline(xPoints, yPoints, 3);
		// 生成并输出随机的验证文字
		g.setFont(mFont);
		g.drawString(String.valueOf(word), 10, 30);
		return image;
	}
	
	private static Color getRandColor(int s, int e) {
		Random random = new Random();
		if (s > 255)
			s = 255;
		if (e > 255)
			e = 255;
		int r = s + random.nextInt(e - s);
 		int g = s + random.nextInt(e - s);
		int b = s + random.nextInt(e - s);
		return new Color(r, g, b);
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
//		BufferedImage image = createVerifyImage(createRandomStr(), 100,50);
//		ImageIO.write(image, "JPEG", new FileOutputStream(new File("G:/liu.jpeg")));
//		System.out.println((char)122);
//		System.out.println((char)97);
//		System.out.println((char)65);
//		System.out.println((char)90);
		System.out.println(RandomStringUtils.random(2, words));
	 
	}
	
}
