package woo.demo.regex;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Created by wujianchao on 2019/9/25.
 */
public class RegexTest {

    @Test
    public void regexTest(){
        find("ac", "[^b]");
    }

    /**
     *  贪婪模式
     */
    @Test
    public void regexGreedyTest(){
        find("xfooxxxxxxfoo", ".*foo");
    }

    /**
     *  非贪婪模式
     */
    @Test
    public void regexReluctantTest(){
        find("xfooxxxxxxfoo", ".*?foo");
    }

    @Test
    public void regexPossessiveTest(){
        find("xfooxxxxxxfoo", ".*+foo");
    }

    @Test
    public void regexpQuotingContentTest(){
        find("123'abc'456'efg'", "((?<=[^\\\\])'(.*?)(?<=[^\\\\])')|((?<=[^\\\\])\"(.*?)(?<=[^\\\\])\")");
    }

    @Test
    public void regexpQuotingContentWithQuoteTest(){
//        find("123\'a\\'b\\'c\'456\'efg\'", "(?<=[^\\\\])'(.*?)(?<=[^\\\\])'");
        find("123'a\\'b\\'c'456\"efg\"", "((?<=[^\\\\])'(.*?)(?<=[^\\\\])')|((?<=[^\\\\])\"(.*?)(?<=[^\\\\])\")");
    }

    @Test
    public void regexpQuotingContentWithQuote2Test(){
//        find("123\'a\\'b\\'c\'456\'efg\'", "(?<=[^\\\\])'(.*?)(?<=[^\\\\])'");
        find("123'a\\'b\\'\"c'456\"efg\"", "((?<=[^\\\\])'(?<string>.*?)(?<=[^\\\\])')|((?<=[^\\\\])\"(?<identifier>.*?)(?<=[^\\\\])\")");
    }

    @Test
    public void regexpCommentTest(){
        find("123\"ab--c\"45--6\"efg\"", "\\*(?>(?:(?>[^*]+)|\\*(?!/))*)\\*");
    }

    @Test
    public void regexpNonCommentTest(){
        reverse("123\"abc\"456\"efg\"", "\".*?\"");
    }

    private void find(String input, String p){
        System.out.println("\n");
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(input);
        boolean found = false;
        while(matcher.find()){
            found = true;
            System.out.println(matcher.groupCount());
            System.out.println("I found the text "+matcher.group()+" starting at index "+
                    matcher.start()+" and ending at index "+matcher.end());
            IntStream.range(1, matcher.groupCount() + 1).forEach(i -> System.out.println(i + " -> " + matcher.group(i)));
            System.out.println("one match done\n");
        }
        if(!found){
            System.out.println("No match found.");
        }
    }

    private void reverse(String input, String p){
        System.out.println("\n");
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(input);
        List<Integer> positions = Lists.newLinkedList();
        positions.add(0);
        while(matcher.find()){
            System.out.println(matcher.groupCount());
            System.out.println("I found the text "+matcher.group()+" starting at index "+
                    matcher.start()+" and ending at index "+matcher.end());
            IntStream.range(1, matcher.groupCount() + 1).forEach(i -> System.out.println(i + " -> " + matcher.group(i)));
            positions.add(matcher.start());
            positions.add(matcher.end());
            System.out.println("one match done\n");
        }
        positions.add(input.length());
        List<String> parts = Lists.newLinkedList();
        if(!positions.isEmpty()){
            for(int i = 0; i < positions.size(); i+=2){
                parts.add(input.substring(positions.get(i), positions.get(i + 1)));
            }
        }
        parts.forEach(part -> System.out.println(part));
    }
}
