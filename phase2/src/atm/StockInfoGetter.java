package atm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class get the information of given stock.
 */
public class StockInfoGetter implements Serializable {

    public double getQuote(String stockSymbol) throws IOException {
        String yahooFinance = "https://ca.finance.yahoo.com/quote/";
        String suffix = "?p=";
        URL url = new URL(yahooFinance + stockSymbol + suffix + stockSymbol);
        URLConnection urlConn = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        String inputLine;
        while((inputLine = reader.readLine()) != null) {
            if(inputLine.contains("regularMarketPrice")) {
                int target = inputLine.indexOf("regularMarketPrice");
                String subString = inputLine.substring(target);
                int deci = subString.indexOf(".");
                int end = subString.indexOf(",");
                int start = deci;
                while (subString.charAt(start) != ':') {
                    start--;
                }
                return Double.parseDouble(subString.substring(start+1, end));
            }
        }
        reader.close();
        throw new IOException();
    }

    public String getSymbol(String companyName) throws SymbolNotFoundException{
        switch (companyName){
            case "microsoft": return "MSFT";
            case "apple": return "AAPL";
            case "google": return "GOOG";
            case "alibaba": return "BABA";
        }
        throw new SymbolNotFoundException(companyName);
    }

}
