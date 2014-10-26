import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;


public class PGLGetter {

    private static String urlString = "http://3ds.pokemon-gl.com/frontendApi/gbu/getSeasonPokemonDetail";
	public static URLConnection createURLConnection(URL url) throws Exception {
        URLConnection uc = url.openConnection();
        uc.setDoOutput(true);//POST可能にする
        uc.setRequestProperty("Accept", "*/*");
        uc.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
        uc.setRequestProperty("Accept-Language", "ja,en-US;q=0.8,en;q=0.6");
        uc.setRequestProperty("Connection", "keep-alive");
        uc.setRequestProperty("Content-Length", "286");
        uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        //uc.setRequestProperty("Cookie", "locale=ja; __ulfpc=201310112334144663; region=0; language_id=1; site=1; NO_MEMBER_DATA=%7B%22language_id%22%3A1%2C%22site%22%3A1%2C%22region%22%3A0%7D; JSESSIONID=8899A93AE3C65888F532754FE119BBF5; PGLLOGINTIME=1399643876405; AWSELB=99C3FF770EA3504C46F25D799674203D12E259AC7AEEAD0A46196CC22515785A18E08D70C2E9A56469803B1A6B1F34F11AFC2ED1D045A267DB7E496BEA70327F1D05B86B1023FF697977A00E295CBB8437E703A8CE; __utma=234147713.1834208204.1381502041.1396156313.1399643869.16; __utmb=234147713.4.10.1399643869; __utmc=234147713; __utmz=234147713.1399643869.16.15.utmcsr=pokemon-gl.com|utmccn=(referral)|utmcmd=referral|utmcct=/");
        uc.setRequestProperty("Origin", "http://3ds.pokemon-gl.com");
        uc.setRequestProperty("Referer", "http://3ds.pokemon-gl.com/battle/");
        //uc.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        //uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36");// ヘッダを設定
        return uc;
	}
	
	
	public static String get(int pockemonNo) throws Exception {
        String s = "";
        try {
            URL url = new URL(urlString);
            URLConnection uc = createURLConnection(url);
            OutputStream os = uc.getOutputStream();//POST用のOutputStreamを取得

                // ヘッダを設定
                String postStr = "languageId=1&" + 
                        "seasonId=3&" + 
                        "battleType=0&" + 
                        "timezone=JST&" + 
                        "pokemonId=" + pockemonNo + "-0&" + 
                        "displayNumberWaza=10&" + 
                        "displayNumberTokusei=3&" + 
                        "displayNumberSeikaku=10&" + 
                        "displayNumberItem=10&";
                PrintStream ps = new PrintStream(os);
                ps.print(postStr);//データをPOSTする
                ps.close();

                
                InputStream is = uc.getInputStream();//POSTした結果を取得
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((s = reader.readLine()) != null) {
                    System.out.println(s);
                    return s;
                }
                reader.close();
        } catch (MalformedURLException e) {
            System.err.println("Invalid URL format: " + urlString);
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("Can't connect to " + urlString);
            System.exit(-1);
        }
		
		return s;
	}
	public static void main(String[] args) throws Exception {
		PrintWriter outFile = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:/Users/U-U/Desktop/filename.txt"),"UTF-8")));

		for (int i = 1; i < 2; i++) {
	        outFile.println(get(i));
	        Thread.sleep(1000);
		}
        outFile.close();
	}

}
