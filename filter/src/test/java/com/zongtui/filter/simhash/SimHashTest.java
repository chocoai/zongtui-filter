package com.zongtui.filter.simhash;

import org.junit.Test;

public class SimHashTest {
	@Test
	public void simhash() {
		String s = "This is a test string for testing";
		SimHash hash1 = new SimHash(s, 64);
		System.out.println(hash1.simHash());
		
		String content = "卓尔防线继续伤筋动骨 队长梅方出场再补漏说起来卓尔队长梅方本赛季就是个“补漏”的命！在中卫与右边后卫间不停地轮换。如果不出意外，今天与广州恒大一战梅方又要换位置，这也是汉军队长连续三场比赛中的第三次换位。而从梅方的身上也可以看出，本赛季汉军防线如此“折腾”，丢球多也不奇怪了。梅方自2009赛季中乙出道便一直司职中后卫，还曾入选过布拉泽维奇国奥队，也是司职的中卫。上赛季，梅方与忻峰搭档双中卫帮助武汉卓尔队中超成功，但谁知进入本赛季后从第一场比赛开始梅方便不断因为种种“意外”而居无定所。联赛首战江苏舜天时，也是由于登贝莱受伤，朱挺位置前移，梅方临危受命客串右边后卫。第二轮主场与北京国安之战梅方仅仅打了一场中卫，又因为柯钊受罚停赛4轮而不得不再次到边路“补漏”。随着马丁诺维奇被弃用，梅方一度成为中卫首选，在与上海东亚队比赛中，邱添一停赛，梅方与忻峰再度携手，紧接着与申鑫队比赛中移至边路，本轮忻峰又停赛，梅方和邱添一成为中卫线上最后的选择。至于左右边后卫位置，卓尔队方面人选较多，罗毅、周恒、刘尚坤等人均可出战。记者马万勇原标题：卓尔防线继续伤筋动骨队长梅方出场再补漏稿源：中新网作者：";
		hash1 = new SimHash(content, 64);
		System.out.println(hash1.simHash());
		
		s = "This is a test string for testing, This is a test string for testing abcdef";
		SimHash hash2 = new SimHash(s, 64);
		System.out.println(hash2.simHash());

		s = "This is a test string for testing als";
		SimHash hash3 = new SimHash(s, 64);
		System.out.println(hash3.simHash());

		//int dis = hash1.getDistance(hash1.strSimHash, hash2.strSimHash);
		//System.out.println(hash1.hammingDistance(hash2) + " " + dis);

		//int dis2 = hash1.getDistance(hash1.strSimHash, hash3.strSimHash);
		//System.out.println(hash1.hammingDistance(hash3) + " " + dis2);

		// 通过Unicode编码来判断中文
		/*
		 * String str = "中国chinese"; for (int i = 0; i < str.length(); i++) {
		 * System.out.println(str.substring(i, i +
		 * 1).matches("[\\u4e00-\\u9fbb]+")); }
		 */
	}
}