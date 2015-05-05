/**
 * Project Name:filter
 * File Name:KeyWordComputer.java
 * Package Name:com.zongtui.filter.tfidf.anjs
 * Date:2015年4月26日下午9:24:35
 * Copyright (c) 2015, 众推项目组版权所有.
 *
 */
package com.zongtui.filter.tfidf.anjs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.ansj.app.keyword.Keyword;
import org.ansj.dic.LearnTool;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;


/**
 * ClassName: KeyWordComputer <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: 2015年4月26日 下午9:24:35 <br/>
 *
 * @author Ahaha
 * @version 
 * @since JDK 1.7
 */
public class KeyWordComputer {
	private int nKeyword = 10;
    //default constructor keyword number=10
    public KeyWordComputer() {
        nKeyword = 10;
    }
    // constructor set keyword number
    public KeyWordComputer(int nKeyword) {
        this.nKeyword = nKeyword;

    }
    //get keywords object list
    private List<Keyword> computeArticleTfidf(String content, int titleLength) {
        Map<String, Keyword> tm = new HashMap<String, Keyword>();
        LearnTool learn = new LearnTool();
        List<Term> parse = NlpAnalysis.parse(content, learn);
        parse = NlpAnalysis.parse(content, learn);
        for (Term term : parse) {
            int weight = getWeight(term, content.length(), titleLength);
            if (weight == 0)
                continue;
            Keyword keyword = tm.get(term.getName());
            if (keyword == null) {
                keyword = new Keyword(term.getName(), term.termNatures().allFreq, weight);
                tm.put(term.getName(), keyword);
            } else {
                keyword.updateWeight(1);
            }
        }
        TreeSet<Keyword> treeSet = new TreeSet<Keyword>(tm.values());
        ArrayList<Keyword> arrayList = new ArrayList<Keyword>(treeSet);
        if (treeSet.size() < nKeyword) {
            return arrayList;
        } else {
            return arrayList.subList(0, nKeyword);
        }
    }
    //get keywords,need title and content
    public Collection<Keyword> computeArticleTfidf(String title, String content) {
        return computeArticleTfidf(title + "\t" + content, title.length());
    }
    //get keywords, just need content
    public Collection<Keyword> computeArticleTfidf(String content) {
        return computeArticleTfidf(content, 0);
    }
    //get keywords weight
    private int getWeight(Term term, int length, int titleLength) {
        if (term.getName().matches("(?s)\\d.*")) {
            return 0;
        }
        if (term.getName().trim().length() < 2) {
            return 0;
        }
        //String pos = term.getNatrue().natureStr;
        String pos = term.getNatureStr();
        if (!pos.startsWith("n") || "num".equals(pos)) {
            return 0;
        }
        int weight = 0;
        if (titleLength > term.getOffe()) {
            return 20;
        }
        // position
        double position = (term.getOffe() + 0.0) / length;
        if (position < 0.05)
            return 10;
        weight += (5 - 5 * position);
        return weight;
    }

}
