package atm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class StockQuoteGetter {

    public double getQuote(String stockSymbol) throws IOException {
        String yahooFinance = "https://ca.finance.yahoo.com/quote/";
        String suffix = "?p=";
        URL url = new URL(yahooFinance + stockSymbol + suffix + stockSymbol);
        URLConnection urlConn = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        while((inputLine = reader.readLine()) != null) {
            if(inputLine.contains("regularMarketPrice")) {
                int target = inputLine.indexOf("regularMarketPrice");
                String subString = inputLine.substring(target);
                int deci = subString.indexOf(".");
                int start = deci;
                while (subString.charAt(start) != ':') {
                    start--;
                }
                return Double.parseDouble(subString.substring(start+1, deci+3));
            }
        }
        reader.close();
        return -1;
    }
    public static void main(String[] args) throws IOException {
        StockQuoteGetter getter = new StockQuoteGetter();
        System.out.println(getter.getQuote("BA"));
    }
}
